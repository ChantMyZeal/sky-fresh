package com.sky.service.impl;

import com.sky.service.ShopService;
import com.sky.entity.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public static final String KEY = "shopCache";

    /**
     * 设置店铺营业状态
     *
     * @param status 状态参数
     */
    @Override
    public void setStatus(Integer status) {
        redisTemplate.opsForHash().put(KEY, "status", status);
    }

    /**
     * 获取店铺营业状态
     *
     * @return 返回状态参数
     */
    @Override
    public Integer getStatus() {
        return (Integer) redisTemplate.opsForHash().get(KEY, "status");
    }

    /**
     * 获取店铺信息
     *
     * @return 返回店铺信息实体对象
     */
    @Override
    public ShopInfo getInfo() {
        return ShopInfo.builder()
                .shopName((String) redisTemplate.opsForHash().get(KEY, "shopName"))
                .shopPhone((String) redisTemplate.opsForHash().get(KEY, "shopPhone"))
                .shopAddress((String) redisTemplate.opsForHash().get(KEY, "shopAddress"))
                .shopIntro((String) redisTemplate.opsForHash().get(KEY, "shopIntro"))
                .deliveryFeePerKm((BigDecimal) redisTemplate.opsForHash().get(KEY, "deliveryFeePerKm"))
                .build();
    }

    /**
     * 设置店铺信息
     *
     * @param shopInfo 店铺信息实体对象
     */
    @Override
    public void setInfo(ShopInfo shopInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("shopName", shopInfo.getShopName());
        map.put("shopPhone", shopInfo.getShopPhone());
        map.put("shopAddress", shopInfo.getShopAddress());
        map.put("shopIntro", shopInfo.getShopIntro());
        map.put("deliveryFeePerKm", shopInfo.getDeliveryFeePerKm());
        redisTemplate.opsForHash().putAll(KEY, map);
    }

}
