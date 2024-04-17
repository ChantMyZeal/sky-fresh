package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 工作台
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Tag(name = "工作台相关接口")
public class WorkSpaceController {// todo 前端定时自动请求或用websocket实时更新工作台数据

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 工作台今日数据查询
     *
     * @return 返回统一响应结果
     */
    @GetMapping("/businessData")
    @Operation(summary = "工作台今日数据查询")
    public Result<BusinessDataVO> businessData() {
        log.info("工作台今日数据查询...");
        LocalDate today = LocalDate.now();
        return Result.success(workspaceService.getBusinessData(today.atTime(LocalTime.MIN), today.atTime(LocalTime.MAX)));
    }

    /**
     * 查询今日订单总览数据
     *
     * @return 返回统一响应结果
     */
    @GetMapping("/overviewOrders")
    @Operation(summary = "查询今日订单总览数据")
    public Result<OrderOverViewVO> orderOverView() {
        log.info("查询今日订单总览数据...");
        LocalDate today = LocalDate.now();
        return Result.success(workspaceService.getOrderOverView(today.atTime(LocalTime.MIN), today.atTime(LocalTime.MAX)));
    }

    /**
     * 查询菜品总览数据
     *
     * @return 返回统一响应结果
     */
    @GetMapping("/overviewDishes")
    @Operation(summary = "查询菜品总览数据")
    public Result<DishOverViewVO> dishOverView() {
        log.info("查询菜品总览数据...");
        return Result.success(workspaceService.getDishOverView());
    }

    /**
     * 查询套餐总览数据
     *
     * @return 返回统一响应结果
     */
    @GetMapping("/overviewSetmeals")
    @Operation(summary = "查询套餐总览数据")
    public Result<SetmealOverViewVO> setmealOverView() {
        log.info("查询套餐总览数据...");
        return Result.success(workspaceService.getSetmealOverView());
    }

}
