package com.fytmss.controller.ticket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fytmss.beans.ticket.TicketBean;
import com.fytmss.common.utils.DateUtils;
import com.fytmss.common.utils.R;
import com.fytmss.service.ticket.TicketBeanService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * @author wgq
 * @create 2024/8/11-周日 1:14
 */
@Component
@Slf4j
public class OrderListener {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private TicketBeanService ticketBeanService;


    @RabbitListener(queues = {"voyage-queue"})
    public void onMessage(Message message){
        //todo 可以实现消息手动确认等功能
        byte[] body = message.getBody();
        String msg = new String(body);
        try {
            List<TicketBean> tickets = objectMapper.readValue(msg, new TypeReference<List<TicketBean>>() {});
            String dateTime = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
            for (TicketBean ticket : tickets) {
                ticket.setTickDate(dateTime);
                ticket.setStateDate(dateTime);
            }
            try{
                ticketBeanService.insertTicketsByBatch(tickets);
            }catch (Exception e){
                throw new RuntimeException("购票失败");
            }
        } catch (IOException e) {
            log.error("购票失败, 消息内容: {}, 错误信息: {}", msg, e.getMessage());
        }
    }


}
