package com.fytmss.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wgq
 * @create 2024/5/28-周二 17:07
 */
public class RoleNameMap {

    public static final Map<String, Integer> map;

    static {
        map = new HashMap<>();
        map.put("财务总监", 1);
        map.put("出纳员", 2);
        map.put("超管", 3);
        map.put("售票员", 4);
        map.put("检票员", 5);
        map.put("管理员", 6);
        map.put("普通员工", 7);
        map.put("旅行社", 8);
    }
}
