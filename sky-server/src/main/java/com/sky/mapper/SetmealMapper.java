package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类ID查询套餐的数量
     *
     * @param id 分类ID
     * @return 返回套餐数量查询结果
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 新增套餐
     *
     * @param setmeal 套餐实体类
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

}
