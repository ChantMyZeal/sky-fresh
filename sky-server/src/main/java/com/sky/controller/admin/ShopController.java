package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Tag(name = "店铺相关接口")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置店铺营业状态
     *
     * @param status 状态参数
     * @return 返回统一响应结果
     */
    @PutMapping("/{status}")
    @Operation(summary = "设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态：{}", Objects.equals(status, StatusConstant.ENABLE) ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(KEY, status);
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
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到店铺的营业状态：{}", Objects.equals(status, StatusConstant.ENABLE) ? "营业中" : "打烊中");
        return Result.success(status);
    }

}
