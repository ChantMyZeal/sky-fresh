package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * 根据时间范围统计各项营业数据
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 返回营业数据VO
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 根据时间范围统计订单总览数据
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 返回订单总览VO
     */
    OrderOverViewVO getOrderOverView(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询菜品总览数据
     *
     * @return 返回菜品总览VO
     */
    DishOverViewVO getDishOverView();

    /**
     * 查询套餐总览数据
     *
     * @return 返回套餐总览VO
     */
    SetmealOverViewVO getSetmealOverView();

}
