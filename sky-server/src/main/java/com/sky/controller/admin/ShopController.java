package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import com.sky.service.ShopService;
import com.sky.entity.ShopInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Tag(name = "店铺相关接口")
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 设置店铺营业状态
     *
     * @param status 状态参数
     * @return 返回统一响应结果
     */
    @PutMapping("/status/{status}")
    @Operation(summary = "设置店铺营业状态")
    public Result<String> setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态：{}", Objects.equals(status, StatusConstant.ENABLE) ? "营业中" : "打烊中");
        shopService.setStatus(status);
        return Result.success();
    }

    /**
     * 获取店铺的营业状态
     *
     * @return 返回统一响应结果
     */
    @GetMapping("/status")
    @Operation(summary = "获取店铺营业状态")
    public Result<Integer> getStatus() {
        Integer status = shopService.getStatus();
        log.info("获取到店铺的营业状态：{}", Objects.equals(status, StatusConstant.ENABLE) ? "营业中" : "打烊中");
        return Result.success(status);
    }

    /**
     * 设置店铺信息
     *
     * @param shopInfo 店铺信息实体对象
     * @return 返回统一响应结果
     */
    @PostMapping("/info")
    @Operation(summary = "设置店铺信息")
    public Result<String> setInfo(@RequestBody ShopInfo shopInfo) {
        log.info("设置店铺信息：{}", shopInfo);
        shopService.setInfo(shopInfo);
        return Result.success();
    }

    /**
     * 获取店铺信息
     *
     * @return 返回店铺信息实体对象
     */
    @GetMapping("/info")
    @Operation(summary = "获取店铺信息")
    public Result<ShopInfo> getInfo() {
        ShopInfo shopInfo = shopService.getInfo();
        log.info("获取到店铺的信息：{}", shopInfo);
        return Result.success(shopInfo);
    }

}
