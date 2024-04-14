package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO 订单提交DTO
     * @return 返回订单提交VO
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 订单支付DTO
     * @return 返回订单支付VO
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo 订单号
     */
    void paySuccess(String outTradeNo);

    /**
     * 用户端订单分页查询
     *
     * @param pageNum  分页数量
     * @param pageSize 分页大小
     * @param status   订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     * @return 返回分页结果
     */
    PageResult pageQuery4User(int pageNum, int pageSize, Integer status);

    /**
     * 查询订单详情
     *
     * @param id 订单ID
     * @return 返回订单VO
     */
    OrderVO details(Long id);

    /**
     * 用户取消订单
     *
     * @param id 订单ID
     * @throws Exception 抛出异常
     */
    void userCancelById(Long id) throws Exception;

}
