package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPath {

    //运输距离（单位：米）
    private Integer distance;
    //运输时间（单位：秒）
    private Integer duration;

}
