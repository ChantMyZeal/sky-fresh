package com.sky.service.impl;

import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置店铺营业状态
     *
     * @param status 状态参数
     */
    @Override
    public void setStatus(Integer status) {
        redisTemplate.opsForValue().set(KEY, status);
    }

    /**
     * 获取店铺营业状态
     *
     * @return 返回状态参数
     */
    @Override
    public Integer getStatus() {
        return (Integer) redisTemplate.opsForValue().get(KEY);
    }

}
