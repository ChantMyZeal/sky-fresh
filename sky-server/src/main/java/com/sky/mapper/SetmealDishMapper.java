package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据套餐id删除套餐和菜品的关联关系
     *
     * @param setmealId 套餐ID
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询套餐和菜品的关联关系
     *
     * @param setmealId 套餐ID
     * @return 返回套餐-菜品关系的集合
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

}
