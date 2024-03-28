package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

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

}
