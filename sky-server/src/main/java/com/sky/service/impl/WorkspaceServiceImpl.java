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
import java.time.LocalDateTime;

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
     * 根据时间范围统计各项营业数据
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 返回营业数据VO
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        //总订单数
        Integer totalOrderCount = orderMapper.countByStatusAndTimeRange(null, begin, end);
        //营业额：已完成订单的总金额
        BigDecimal turnover = orderMapper.sumByStatusAndTimeRange(Orders.COMPLETED, begin, end);
        if (turnover == null) turnover = BigDecimal.valueOf(0.0);
        //有效订单数：已完成订单的数量
        Integer validOrderCount = orderMapper.countByStatusAndTimeRange(Orders.COMPLETED, begin, end);
        //订单完成率：有效订单数 / 总订单数
        Double orderCompletionRate = totalOrderCount.equals(0) ? 0.0 : validOrderCount.doubleValue() / totalOrderCount;
        //平均客单价：营业额 / 有效订单数
        BigDecimal unitPrice = validOrderCount.equals(0) ? BigDecimal.valueOf(0.0) : turnover.divide(BigDecimal.valueOf(validOrderCount), 2, RoundingMode.HALF_UP);
        //新增用户数
        Integer newUsers = userMapper.countByTimeRange(begin, end);

        return BusinessDataVO.builder()
                .turnover(turnover.doubleValue())
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice.doubleValue())
                .newUsers(newUsers)
                .build();
    }

    /**
     * 根据时间范围统计订单总览数据
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 返回订单总览VO
     */
    @Override
    public OrderOverViewVO getOrderOverView(LocalDateTime begin, LocalDateTime end) {
        Integer waitingOrders = orderMapper.countByStatusAndTimeRange(Orders.TO_BE_CONFIRMED, begin, end);//待接单的订单数
        Integer deliveredOrders = orderMapper.countByStatusAndTimeRange(Orders.CONFIRMED, begin, end);//待派送的订单数
        Integer completedOrders = orderMapper.countByStatusAndTimeRange(Orders.COMPLETED, begin, end);//已完成的订单数
        Integer cancelledOrders = orderMapper.countByStatusAndTimeRange(Orders.CANCELLED, begin, end);//已取消的订单数
        Integer allOrders = orderMapper.countByStatusAndTimeRange(null, begin, end);//总订单数

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
