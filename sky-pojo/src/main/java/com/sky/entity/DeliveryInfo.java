package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfo {

    //配送距离
    private Double deliveryDistance;
    //配送费
    private BigDecimal deliveryFee;
    //预估配送时间
    private LocalDateTime time;

}
