package com.sky.service.impl;

import com.sky.service.ShopService;
import com.sky.vo.ShopInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    // todo 把商家信息存储在redis或mysql中方便修改，而不是写在yml配置文件，添加查询与修改商家信息的接口
    @Value("${sky.shop.name}")
    private String shopName;
    @Value("${sky.shop.phone}")
    private String shopPhone;
    @Value("${sky.shop.address}")
    private String shopAddress;
    @Value("${sky.shop.intro}")
    private String shopIntro;
    @Value("${sky.shop.fee-rule}")
    private String feeRule;
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

    /**
     * 获取店铺信息
     *
     * @return 返回店铺信息VO
     */
    @Override
    public ShopInfoVO getInfo() {
        return ShopInfoVO.builder()
                .shopName(shopName)
                .shopPhone(shopPhone)
                .shopAddress(shopAddress)
                .shopIntro(shopIntro)
                .feeRule(feeRule)
                .build();
    }

}
