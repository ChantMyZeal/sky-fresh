package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

public interface WorkspaceService {

    /**
     * 根据时间段统计营业数据
     *
     * @return 返回营业数据VO
     */
    BusinessDataVO getBusinessData();

    /**
     * 查询订单总览数据
     *
     * @return 返回订单总览VO
     */
    OrderOverViewVO getOrderOverView();

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
