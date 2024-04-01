package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品和对应的口味数据
     *
     * @param dishDTO 菜品DTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 菜品分页查询DTO
     * @return 返回封装好的PageResult对象
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     *
     * @param ids 菜品ID集合
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据ID查询菜品及其口味数据
     *
     * @param id 菜品ID
     * @return 返回菜品VO
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品及其口味数据
     *
     * @param dishDTO 菜品DTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 分类ID
     * @return 返回菜品集合
     */
    List<Dish> list(Long categoryId);

    /**
     * 条件查询菜品和口味
     *
     * @param dish 菜品实体对象
     * @return 返回菜品VO集合
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 启售或禁售菜品
     *
     * @param status 传入的菜品状态参数
     * @param id     菜品ID
     */
    void startOrStop(Integer status, Long id);

}
