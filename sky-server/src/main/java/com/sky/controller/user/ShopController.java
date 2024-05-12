package com.sky.controller.user;

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

@RestController("userShopController")
@RequestMapping("/user/shop")
@Tag(name = "C端-店铺相关接口")
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;

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
