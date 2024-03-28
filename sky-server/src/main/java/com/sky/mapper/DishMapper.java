package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类ID查询菜品数量
     *
     * @param categoryId 分类ID
     * @return 返回菜品数量查询结果
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     *
     * @param dish 菜品实体类
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 菜品分页查询DTO
     * @return 返回封装好的Page对象
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ID查询菜品
     *
     * @param id 菜品ID
     * @return 返回菜品实体类
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 在提供的菜品ID集合中查询某种状态的菜品数量
     *
     * @param ids    菜品ID集合
     * @param status 需要查询数量的状态参数
     * @return 返回已起售的菜品数量
     */
    Long getStatusCountByIds(List<Long> ids, Integer status);

    /**
     * 根据菜品ID集合批量删除菜品
     *
     * @param ids 菜品ID集合
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据ID修改菜品信息
     *
     * @param dish 菜品实体类
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 动态条件查询菜品
     *
     * @param dish 菜品实体类
     * @return 返回菜品集合
     */
    List<Dish> list(Dish dish);

}
