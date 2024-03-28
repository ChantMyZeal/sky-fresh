package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     *
     * @param flavors 口味集合
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品ID集合批量删除关联的口味数据
     *
     * @param dishIds 菜品ID集合
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根据菜品ID查询对应的口味数据
     *
     * @param dishId 菜品ID
     * @return 返回菜品-口味关系类的集合
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);

}
