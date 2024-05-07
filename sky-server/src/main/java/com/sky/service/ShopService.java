package com.sky.service;

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

}
