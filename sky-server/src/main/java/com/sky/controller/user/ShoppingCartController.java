package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Tag(name = "C端-购物车相关接口")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车商品DTO
     * @return 返回统一响应结果
     */
    @PostMapping("/add")
    @Operation(summary = "添加购物车")
    public Result<String> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车，商品信息为：{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     *
     * @return 返回统一响应结果
     */
    @GetMapping("/list")
    @Operation(summary = "查看购物车")
    public Result<List<ShoppingCart>> list() {
        log.info("查看购物车");
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }

    /**
     * 清空购物车
     *
     * @return 返回统一响应结果
     */
    @DeleteMapping("/clean")
    @Operation(summary = "清空购物车")
    public Result<String> clean() {
        log.info("清空购物车");
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * 删除购物车中一个商品
     *
     * @param shoppingCartDTO 购物车商品DTO
     * @return 返回统一响应结果
     */
    @PostMapping("/sub")
    @Operation(summary = "删除购物车中一个商品")
    public Result<String> sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车中一个商品，商品：{}", shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }

}
