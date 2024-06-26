package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.DeliveryInfo;
import com.sky.result.PageResult;
import com.sky.vo.*;

public interface OrderService {

    /**
     * 订单分页条件查询
     *
     * @param ordersPageQueryDTO 订单分页查询DTO
     * @return 返回分页结果
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     *
     * @return 返回订单统计VO
     */
    OrderStatisticsVO statistics();

    /**
     * 接单
     *
     * @param ordersConfirmDTO 订单确认DTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO 订单拒绝DTO
     * @throws Exception 抛出异常
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 取消订单
     *
     * @param ordersCancelDTO 订单取消DTO
     * @throws Exception 抛出异常
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 派送订单
     *
     * @param id 订单ID
     */
    void delivery(Long id);

    /**
     * 完成订单
     *
     * @param id 订单ID
     */
    void complete(Long id);

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
     * @param pageNum   分页数量
     * @param pageSize  分页大小
     * @param status    订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     * @param payStatus 支付状态 0未支付 1已支付 2已退款
     * @return 返回分页结果
     */
    PageResult pageQuery4User(int pageNum, int pageSize, Integer status, Integer payStatus);

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

    /**
     * 再来一单
     *
     * @param id 订单ID
     */
    void repetition(Long id);

    /**
     * 用户催单
     *
     * @param id 订单ID
     */
    void reminder(Long id);

    /**
     * 查询配送距离、配送费和预估送达时间
     *
     * @param userAddress 用户收货地址
     * @return 返回快递信息实体对象
     */
    DeliveryInfo getDeliveryInfo(String userAddress);

}
