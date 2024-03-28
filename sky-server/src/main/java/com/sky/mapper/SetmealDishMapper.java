package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品ID集合查询对应的套餐ID
     *
     * @param dishIds 菜品ID集合
     * @return 返回套餐ID集合
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 根据菜品ID集合查询对应的套餐数量
     *
     * @param dishIds 菜品ID集合
     * @return 返回对应的套餐数量
     */
    Long getSetmealCountByDishIds(List<Long> dishIds);

    /**
     * 批量保存套餐和菜品的关联关系
     *
     * @param setmealDishes 套餐菜品关系实体类
     */
    void insertBatch(List<SetmealDish> setmealDishes);

}
