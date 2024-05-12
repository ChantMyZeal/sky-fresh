package com.sky.service;

import com.sky.entity.ShopInfo;

public interface ShopService {

    /**
     * 设置店铺营业状态
     *
     * @param status 状态参数
     */
    void setStatus(Integer status);

    /**
     * 获取店铺营业状态
     *
     * @return 返回状态参数
     */
    Integer getStatus();

    /**
     * 获取店铺信息
     *
     * @return 返回店铺信息实体对象
     */
    ShopInfo getInfo();

    /**
     * 设置店铺信息
     *
     * @param shopInfo 店铺信息实体对象
     */
    void setInfo(ShopInfo shopInfo);

}
