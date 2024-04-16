package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类ID和菜品状态查询菜品数量
     *
     * @param categoryId 分类ID
     * @param status     菜品状态
     * @return 返回菜品数量查询结果
     */
    Integer countByCategoryIdAndStatus(Long categoryId, Integer status);

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
     * 根据菜品ID集合和状态参数查询满足条件的的菜品数量
     *
     * @param ids    菜品ID集合
     * @param status 状态参数
     * @return 返回满足条件的菜品数量
     */
    Long countByIdsAndStatus(List<Long> ids, Integer status);

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

    /**
     * 根据套餐ID查询相关菜品
     *
     * @param setmealId 套餐ID
     * @return 返回菜品实体集合
     */
    @Select("select a.* from dish a right join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * 根据套餐ID查询菜品选项
     *
     * @param setmealId 套餐ID
     * @return 返回菜品选项VO集合
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 根据套餐ID和状态参数查询满足条件的菜品数量
     *
     * @param setmealId 套餐ID
     * @param status    状态参数
     * @return 返回满足条件的菜品数量
     */
    Long countBySetmealIdAndStatus(Long setmealId, Integer status);

}
