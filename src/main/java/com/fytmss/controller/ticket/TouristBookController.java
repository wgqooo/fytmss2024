package com.fytmss.controller.ticket;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fytmss.beans.ticket.TicketBean;
import com.fytmss.common.utils.DateUtils;
import com.fytmss.common.utils.R;
import com.fytmss.config.RabbitMQConfig;
import com.fytmss.service.ticket.TicketBeanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wgq
 * @create 2024/6/27-周四 9:25
 */
@RestController
@RequestMapping("/ticket/touristBook")
public class TouristBookController {

    @Resource
    private TicketBeanService ticketBeanService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ObjectMapper objectMapper;

    int time = 20000;


    @PostMapping("/addTickets")
    public R addTickets(@RequestBody List<TicketBean> tickets) {
        int vipNum = 0, firNum = 0, secNum = 0;
        String voyDate = tickets.get(0).getStartDate().substring(0, 10);
        String voyNo = tickets.get(0).getStartVoyNo();
        String voyKey = "voyage:" + voyDate + ":" + voyNo;
        String lockKey = "voyLock:" + voyDate + ":" + voyNo;
        String lockValue = UUID.randomUUID().toString(); // 用于唯一标识锁的值
        int maxRetries = time / 200;
        int retryCount = 0;

        for (TicketBean ticket : tickets) {
            switch (ticket.getStartSeat()) {
                case "VIP座":
                    ++vipNum;
                    break;
                case "一等座":
                    ++firNum;
                    break;
                default:
                    ++secNum;
                    break;
            }
        }

        // Lua脚本：确保SETNX和EXPIRE操作的原子性，获取锁的原子性
        String lockScript = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end";
        while (retryCount < maxRetries) {
            Boolean lockAcquired = stringRedisTemplate.execute(
                    new DefaultRedisScript<>(lockScript, Boolean.class),
                    Collections.singletonList(lockKey),
                    lockValue, "10" // 10秒过期时间
            );

            if (Boolean.TRUE.equals(lockAcquired)) {
                try {
                    // 开启Redis事务
                    stringRedisTemplate.setEnableTransactionSupport(true);
                    stringRedisTemplate.multi();

                    // 从Redis读取相应航次的各个座位剩余数量
                    int vipLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "vipLeft")));
                    int firLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "firLeft")));
                    int secLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "secLeft")));

                    // 检查余票是否充足
                    if (secNum > secLeft || firNum > firLeft || vipNum > vipLeft) {
                        stringRedisTemplate.discard(); // 回滚事务
                        return R.error("余票不足");
                    }

                    // 预扣减
                    stringRedisTemplate.opsForHash().put(voyKey, "vipLeft", String.valueOf(vipLeft - vipNum));
                    stringRedisTemplate.opsForHash().put(voyKey, "firLeft", String.valueOf(firLeft - firNum));
                    stringRedisTemplate.opsForHash().put(voyKey, "secLeft", String.valueOf(secLeft - secNum));

                    // 提交事务
                    stringRedisTemplate.exec();

                    // 调用支付接口，生成支付二维码
                    Map<String, Object> paymentResult = paymentService.generatePaymentQRCode(tickets, totalAmount); // 假设你有一个paymentService

                    // 判断支付接口调用结果
                    if (paymentResult.get("status").equals("success")) {
                        // 启动支付超时任务
                        TimerTask paymentTimeoutTask = new TimerTask() {
                            @Override
                            public void run() {
                                // 回滚座位扣减
                                stringRedisTemplate.opsForHash().put(voyKey, "vipLeft", String.valueOf(vipLeft));
                                stringRedisTemplate.opsForHash().put(voyKey, "firLeft", String.valueOf(firLeft));
                                stringRedisTemplate.opsForHash().put(voyKey, "secLeft", String.valueOf(secLeft));
                                stringRedisTemplate.execute(new DefaultRedisScript<>(unlockScript, Boolean.class),
                                        Collections.singletonList(lockKey), lockValue); // 释放锁
                                log.info("支付超时，订单已取消");
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(paymentTimeoutTask, 10 * 60 * 1000); // 10分钟超时

                        // 支付请求成功，返回支付二维码信息
                        return R.ok("订单生成成功，请扫描二维码支付").put("qrCode", paymentResult.get("qrCode"));
                    } else {
                        // 如果支付接口调用失败，回滚座位扣减
                        stringRedisTemplate.opsForHash().put(voyKey, "vipLeft", String.valueOf(vipLeft));
                        stringRedisTemplate.opsForHash().put(voyKey, "firLeft", String.valueOf(firLeft));
                        stringRedisTemplate.opsForHash().put(voyKey, "secLeft", String.valueOf(secLeft));
                        return R.error("支付请求失败，请稍后重试");
                    }
                } catch (Exception e) {
                    stringRedisTemplate.discard(); // 回滚事务
                    log.error("购买票时出现异常: {}", e.getMessage(), e);
                    return R.error("购买失败，请稍后重试");
                } finally {
                    // 确保释放锁的原子性
                    String unlockScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                    stringRedisTemplate.execute(new DefaultRedisScript<>(unlockScript, Boolean.class),
                            Collections.singletonList(lockKey), lockValue);
                }
            } else {
                retryCount++;
                try {
                    Thread.sleep(200); // 等待一段时间后重试
                } catch (InterruptedException e) {
                    log.error("线程休眠时被中断: {}", e.getMessage(), e);
                    Thread.currentThread().interrupt(); // 恢复中断状态
                }
            }
        }
        if (retryCount >= maxRetries) {
            return R.error("购买失败，系统繁忙，请稍后重试");
        }

        // 放入消息队列
//        try {
//            rabbitTemplate.convertAndSend("voyage-exchange", "voyage-routing-key", objectMapper.writeValueAsString(tickets));
//        } catch (Exception e) {
//            log.error("发送消息到队列失败: {}", e.getMessage(), e);
//            return R.error("订单生成成功，但部分操作失败，请联系管理员");
//        }
//        return R.ok("订单生成成功，请稍后...");
    }


//    @PostMapping("/addTickets")
//    public R addTickets(@RequestBody List<TicketBean> tickets) {
//        int vipNum = 0, firNum = 0, secNum = 0;
//        String voyDate = tickets.get(0).getStartDate().substring(0, 10);
//        String voyNo = tickets.get(0).getStartVoyNo();
//        String voyKey = "voyage:" + voyDate + ":" + voyNo;
//        String lockKey = "voyLock:" + voyDate + ":" + voyNo;
//        int maxRetries = time / 200;
//        int retryCount = 0;
//
//        for (TicketBean ticket : tickets) {
//            switch (ticket.getStartSeat()) {
//                case "VIP座":
//                    ++vipNum;
//                    break;
//                case "一等座":
//                    ++firNum;
//                    break;
//                default:
//                    ++secNum;
//                    break;
//            }
//        }
//
//        while (retryCount < maxRetries) {
//            //获取锁在高并发的场景下可能也会出现问题：使用 Redis Lua 脚本实现原子操作：
//            Boolean lockAcquired = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "", 10, TimeUnit.SECONDS);
//            if (Boolean.TRUE.equals(lockAcquired)) {
//                try {
//                    // 开启Redis事务
//                    stringRedisTemplate.setEnableTransactionSupport(true);
//                    stringRedisTemplate.multi();
//
//                    // 从Redis读取相应航次的各个座位剩余数量
//                    int vipLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "vipLeft")));
//                    int firLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "firLeft")));
//                    int secLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "secLeft")));
//
//                    // 检查余票是否充足
//                    if (secNum > secLeft || firNum > firLeft || vipNum > vipLeft) {
//                        stringRedisTemplate.discard(); // 回滚事务
//                        return R.error("余票不足");
//                    }
//
//                    // 预扣减
//                    stringRedisTemplate.opsForHash().put(voyKey, "vipLeft", String.valueOf(vipLeft - vipNum));
//                    stringRedisTemplate.opsForHash().put(voyKey, "firLeft", String.valueOf(firLeft - firNum));
//                    stringRedisTemplate.opsForHash().put(voyKey, "secLeft", String.valueOf(secLeft - secNum));
//
//                    // 提交事务
//                    stringRedisTemplate.exec();
//
//                    // 调用支付接口，生成支付二维码
//                    Map<String, Object> paymentResult = paymentService.generatePaymentQRCode(tickets, totalAmount); // 假设你有一个paymentService
//
//                    // 判断支付接口调用结果
//                    if (paymentResult.get("status").equals("success")) {
//                        // 支付请求成功，返回支付二维码信息
//                        return R.ok("订单生成成功，请扫描二维码支付").put("qrCode", paymentResult.get("qrCode"));
//                    } else {
//                        // 如果支付接口调用失败，回滚座位扣减
//                        stringRedisTemplate.opsForHash().put(voyKey, "vipLeft", String.valueOf(vipLeft));
//                        stringRedisTemplate.opsForHash().put(voyKey, "firLeft", String.valueOf(firLeft));
//                        stringRedisTemplate.opsForHash().put(voyKey, "secLeft", String.valueOf(secLeft));
//                        return R.error("支付请求失败，请稍后重试");
//                    }
//                } catch (Exception e) {
//                    stringRedisTemplate.discard(); // 回滚事务
//                    log.error("购买票时出现异常: {}", e.getMessage(), e);
//                    return R.error("购买失败，请稍后重试");
//                } finally {
//                    stringRedisTemplate.delete(lockKey); // 释放锁
//                }
//            } else {
//                retryCount++;
//                try {
//                    Thread.sleep(200); // 等待一段时间后重试
//                } catch (InterruptedException e) {
//                    log.error("线程休眠时被中断: {}", e.getMessage(), e);
//                    Thread.currentThread().interrupt(); // 恢复中断状态
//                }
//            }
//        }
//
//        if (retryCount >= maxRetries) {
//            return R.error("购买失败，系统繁忙，请稍后重试");
//        }
//
//        // 放入消息队列
//        try {
//            rabbitTemplate.convertAndSend("voyage-exchange", "voyage-routing-key", objectMapper.writeValueAsString(tickets));
//        } catch (Exception e) {
//            log.error("发送消息到队列失败: {}", e.getMessage(), e);
//            return R.error("订单生成成功，但部分操作失败，请联系管理员");
//        }
//
//        return R.ok("订单生成成功，请稍后...");
//    }


//    @PostMapping("/addTickets")
//    public R addTickets(@RequestBody List<TicketBean> tickets) {
//        int vipNum = 0, firNum = 0, secNum = 0;
//        String voyDate = tickets.get(0).getStartDate().substring(0, 10);
//        String voyNo = tickets.get(0).getStartVoyNo();
//        String voyKey = "voyage:" + voyDate + ":" + voyNo;
//        String lockKey = "voyLock:" + voyDate + ":" + voyNo;
//        int maxRetries = time / 200;
//        int retryCount = 0;
//
//        for (TicketBean ticket : tickets) {
//            switch (ticket.getStartSeat()) {
//                case "VIP座":
//                    ++vipNum;
//                    break;
//                case "一等座":
//                    ++firNum;
//                    break;
//                default:
//                    ++secNum;
//                    break;
//            }
//        }
//
//        while (retryCount < maxRetries) {
//            Boolean lockAcquired = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "", 10, TimeUnit.SECONDS);
//            if (Boolean.TRUE.equals(lockAcquired)) {
//                try {
//                    // 开启Redis事务
//                    stringRedisTemplate.setEnableTransactionSupport(true);
//                    stringRedisTemplate.multi();
//
//                    // 从Redis读取相应航次的各个座位剩余数量
//                    int vipLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "vipLeft")));
//                    int firLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "firLeft")));
//                    int secLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "secLeft")));
//
//                    // 检查余票是否充足
//                    if (secNum > secLeft || firNum > firLeft || vipNum > vipLeft) {
//                        stringRedisTemplate.discard(); // 回滚事务
//                        return R.error("余票不足");
//                    }
//
//                    // 预扣减
//                    stringRedisTemplate.opsForHash().put(voyKey, "vipLeft", String.valueOf(vipLeft - vipNum));
//                    stringRedisTemplate.opsForHash().put(voyKey, "firLeft", String.valueOf(firLeft - firNum));
//                    stringRedisTemplate.opsForHash().put(voyKey, "secLeft", String.valueOf(secLeft - secNum));
//
//                    // 提交事务
//                    stringRedisTemplate.exec();
//                    break; // 退出循环，表示操作成功
//
//                } catch (Exception e) {
//                    stringRedisTemplate.discard(); // 回滚事务
//                    System.out.printf("购买票时出现异常: {}", e.getMessage());
//                    return R.error("购买失败，请稍后重试");
//                } finally {
//                    stringRedisTemplate.delete(lockKey); // 释放锁
//                }
//            } else {
//                retryCount++;
//                try {
//                    Thread.sleep(200); // 等待一段时间后重试
//                } catch (InterruptedException e) {
//                    System.out.printf("线程休眠时被中断: {}", e.getMessage());
//                    Thread.currentThread().interrupt(); // 恢复中断状态
//                }
//            }
//        }
//
//        if (retryCount >= maxRetries) {
//            return R.error("购买失败，系统繁忙，请稍后重试");
//        }
//
//        // 放入消息队列
//        try {
//            rabbitTemplate.convertAndSend("voyage-exchange", "voyage-routing-key", objectMapper.writeValueAsString(tickets));
//        } catch (Exception e) {
//            System.out.printf("发送消息到队列失败: {}", e.getMessage());
//            return R.error("订单生成成功，但部分操作失败，请联系管理员");
//        }
//
//        return R.ok("订单生成成功，请稍后...");
//    }

//    @PostMapping("/addTickets")
//    public R addTickets(@RequestBody List<TicketBean> tickets){
//        int vipNum = 0, firNum = 0, secNum = 0;
//        String voyDate = tickets.get(0).getStartDate().substring(0, 10);
//        String voyNo = tickets.get(0).getStartVoyNo();
//        String voyKey = "voyage:" + voyDate + ":" + voyNo;
//        for (TicketBean ticket : tickets) {
//            if(Objects.equals(ticket.getStartSeat(), "VIP座")){
//                ++vipNum;
//            }else if(Objects.equals(ticket.getStartSeat(), "一等座")){
//                ++firNum;
//            }else{
//                ++secNum;
//            }
//        }
//        int current = 0;
//        while(current <= time){
////            stringRedisTemplate.setEnableTransactionSupport(true);
////            stringRedisTemplate.multi();
//            Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent("voyLock:" + voyDate + ":" + voyNo, "", 10, TimeUnit.SECONDS);
//            if(Boolean.TRUE.equals(flag)){
//                try{
//                    //从redis读取相应航次的各个座位剩余数量
//                    int vipLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "vipLeft")));
//                    int firLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "firLeft")));
//                    int secLeft = Integer.parseInt((String) Objects.requireNonNull(stringRedisTemplate.opsForHash().get(voyKey, "secLeft")));
//                    System.out.println(vipLeft + ", " + firLeft + ", " + secLeft);
//                    if(secNum > secLeft || firNum > firLeft || vipNum > vipLeft) {
//                        System.out.println("----------------------------------------------");
//                        if(secNum > secLeft) return R.error("二等座余票不足，剩余" + secLeft + "张票");
//                        else if(firNum > firLeft) return R.error("一等座余票不足，剩余" + firLeft + "张票");
//                        else return R.error("VIP座余票不足，剩余" + vipLeft + "张票");
//                    }
//                    //预扣减
//                    stringRedisTemplate.opsForHash().put(voyKey, "vipLeft", String.valueOf(vipLeft - vipNum));
//                    stringRedisTemplate.opsForHash().put(voyKey, "firLeft", String.valueOf(firLeft - firNum));
//                    stringRedisTemplate.opsForHash().put(voyKey, "secLeft", String.valueOf(secLeft - secNum));
//                    //提交事务
//                    //stringRedisTemplate.exec();
//                    break;
//                }catch (Exception e) {
//                    //stringRedisTemplate.discard();
//                    return R.error("购买失败，请稍后重试");
//                }finally {
//                    stringRedisTemplate.delete("voyLock:" + voyDate + ":" + voyNo);
//                }
//            }else{
//                current += 200;
//                try{
//                    Thread.sleep(200);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//        //放入mq
//        try {
//            rabbitTemplate.convertAndSend("voyage-exchange", "voyage-routing-key", objectMapper.writeValueAsString(tickets));
//        } catch (Exception e) {
//            return R.error("Failed to send some tickets. Please try again later.");
//        }
//        return R.ok("订单生成成功，请稍后...");
//    }


}
