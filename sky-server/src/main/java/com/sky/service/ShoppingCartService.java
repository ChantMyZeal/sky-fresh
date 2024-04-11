package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO 购物车商品DTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     *
     * @return 返回购物车实体对象集合
     */
    List<ShoppingCart> showShoppingCart();

    /**
     * 清空购物车
     */
    void cleanShoppingCart();

    /**
     * 删除购物车中一个商品
     *
     * @param shoppingCartDTO 购物车商品DTO
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);

}
