package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
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

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO 套餐分页查询DTO
     * @return 返回封装好的Page对象
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐
     *
     * @param id 套餐ID
     * @return 返回套餐实体类
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据id删除套餐
     *
     * @param id 套餐ID
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据ID修改套餐信息
     *
     * @param setmeal 套餐实体对象
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

}
