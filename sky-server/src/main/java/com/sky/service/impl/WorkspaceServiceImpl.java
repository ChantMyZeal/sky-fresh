package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据时间段统计营业数据
     *
     * @return 返回营业数据VO
     */
    @Override
    public BusinessDataVO getBusinessData() {
        LocalDate date = LocalDate.now();
        LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

        //总订单数
        Integer totalOrderCount = orderMapper.countByDateAndStatus(date, null);
        //营业额：当日已完成订单的总金额
        BigDecimal turnover = orderMapper.sumByDateAndStatus(date, Orders.COMPLETED);
        if (turnover == null) turnover = BigDecimal.valueOf(0.0);
        //有效订单数：当日已完成订单的数量
        Integer validOrderCount = orderMapper.countByDateAndStatus(date, Orders.COMPLETED);
        //订单完成率：有效订单数 / 总订单数
        Double orderCompletionRate = totalOrderCount.equals(0) ? 0.0 : validOrderCount.doubleValue() / totalOrderCount;
        //平均客单价：营业额 / 有效订单数
        BigDecimal unitPrice = validOrderCount.equals(0) ? BigDecimal.valueOf(0.0) : turnover.divide(BigDecimal.valueOf(validOrderCount), 2, RoundingMode.HALF_UP);
        //新增用户数：当日新增用户的数量
        Integer newUsers = userMapper.countByTimeRange(beginTime, endTime);

        return BusinessDataVO.builder()
                .turnover(turnover.doubleValue())
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice.doubleValue())
                .newUsers(newUsers)
                .build();
    }

    /**
     * 查询订单总览数据
     *
     * @return 返回订单总览VO
     */
    @Override
    public OrderOverViewVO getOrderOverView() {
        LocalDate date = LocalDate.now();
        Integer waitingOrders = orderMapper.countByDateAndStatus(date, Orders.TO_BE_CONFIRMED);//待接单的订单数
        Integer deliveredOrders = orderMapper.countByDateAndStatus(date, Orders.CONFIRMED);//待派送的订单数
        Integer completedOrders = orderMapper.countByDateAndStatus(date, Orders.COMPLETED);//已完成的订单数
        Integer cancelledOrders = orderMapper.countByDateAndStatus(date, Orders.CANCELLED);//已取消的订单数
        Integer allOrders = orderMapper.countByDateAndStatus(date, null);//总订单数

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 查询菜品总览数据
     *
     * @return 返回菜品总览VO
     */
    @Override
    public DishOverViewVO getDishOverView() {
        Integer sold = dishMapper.countByCategoryIdAndStatus(null, StatusConstant.ENABLE);
        Integer discontinued = dishMapper.countByCategoryIdAndStatus(null, StatusConstant.DISABLE);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 查询套餐总览数据
     *
     * @return 返回套餐总览VO
     */
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        Integer sold = setmealMapper.countByCategoryIdAndStatus(null, StatusConstant.ENABLE);
        Integer discontinued = setmealMapper.countByCategoryIdAndStatus(null, StatusConstant.DISABLE);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

}
