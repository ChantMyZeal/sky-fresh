package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopInfo {

    private String shopName; // 店铺名称
    private String shopPhone; // 店铺电话
    private String shopAddress; // 店铺地址
    private String shopIntro; // 店铺简介
    private BigDecimal packageFeePerItem; // 每件打包费
    private BigDecimal deliveryFeePerKm; // 每公里配送费
    private Double deliveryRange; // 配送范围
    //private String shopId;

}
