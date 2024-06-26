package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.entity.DeliveryInfo;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Tag(name = "C端-订单相关接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO 订单提交DTO
     * @return 返回统一响应结果
     */
    @PostMapping("/submit")
    @Operation(summary = "用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单，参数为：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 订单支付DTO
     * @return 返回统一响应结果
     */
    @PutMapping("/payment")
    @Operation(summary = "订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 历史订单查询
     *
     * @param page      分页数量
     * @param pageSize  分页大小
     * @param status    订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     * @param payStatus 支付状态 0未支付 1已支付 2已退款
     * @return 返回统一响应结果
     */
    @GetMapping("/historyOrders")
    @Operation(summary = "历史订单查询")
    public Result<PageResult> page(int page, int pageSize, Integer status, Integer payStatus) {
        log.info("历史订单查询，page={},pageSize={},status={},payStatus={}", page, pageSize, status, payStatus);
        PageResult pageResult = orderService.pageQuery4User(page, pageSize, status, payStatus);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情
     *
     * @param id 订单ID
     * @return 返回统一响应结果
     */
    @GetMapping("/orderDetail/{id}")
    @Operation(summary = "查询订单详情")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        log.info("查询订单详情，订单ID：{}", id);
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 用户取消订单
     *
     * @param id 订单ID
     * @return 返回统一响应结果
     * @throws Exception 抛出异常
     */
    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消订单")
    public Result<String> cancel(@PathVariable("id") Long id) throws Exception {
        log.info("取消订单，订单ID：{}", id);
        orderService.userCancelById(id);
        return Result.success();
    }

    /**
     * 再来一单
     *
     * @param id 订单ID
     * @return 返回统一响应结果
     */
    @PostMapping("/repetition/{id}")
    @Operation(summary = "再来一单")
    public Result<String> repetition(@PathVariable Long id) {
        log.info("再来一单，原订单ID：{}", id);
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * 用户催单
     *
     * @param id 订单ID
     * @return 返回统一响应结果
     */
    @GetMapping("/reminder/{id}")
    @Operation(summary = "用户催单")
    public Result<String> reminder(@PathVariable Long id) {
        log.info("用户催单，订单ID：{}", id);
        orderService.reminder(id);
        return Result.success();
    }

    /**
     * 查询配送距离、配送费和预估送达时间
     *
     * @param customerAddress 用户收货地址
     * @return 返回快递信息实体对象
     */
    @GetMapping("/getDeliveryInfo")
    @Operation(summary = "查询配送距离、配送费和预估送达时间")
    public Result<DeliveryInfo> getDeliveryInfo(String customerAddress) {
        log.info("查询配送距离、配送费和预估送达时间，收货地址：{}", customerAddress);
        return Result.success(orderService.getDeliveryInfo(customerAddress));
    }

}
