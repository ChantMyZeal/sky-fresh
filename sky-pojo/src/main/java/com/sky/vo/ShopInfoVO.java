package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopInfoVO {

    private String shopName;//店铺名称
    private String shopPhone;//店铺电话
    private String shopAddress;//店铺地址
    private String shopIntro;//店铺简介
    private String feeRule;//配送费规则
    //private String shopId;

}
