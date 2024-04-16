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

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类ID和套餐状态查询套餐的数量
     *
     * @param categoryId 分类ID
     * @param status     套餐状态
     * @return 返回套餐数量查询结果
     */
    Integer countByCategoryIdAndStatus(Long categoryId, Integer status);

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
     * 根据ID查询套餐
     *
     * @param id 套餐ID
     * @return 返回套餐实体类
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据套餐ID集合和状态参数查询满足条件的套餐数量
     *
     * @param ids    套餐ID集合
     * @param status 状态参数
     * @return 返回满足条件的套餐数量
     */
    Long getCountByIdsAndStatus(List<Long> ids, Integer status);

    /**
     * 根据ID删除套餐
     *
     * @param id 套餐ID
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据ID集合批量删除套餐
     *
     * @param ids 套餐ID集合
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据ID修改套餐信息
     *
     * @param setmeal 套餐实体对象
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 动态条件查询套餐
     *
     * @param setmeal 套餐实体对象
     * @return 返回套餐实体对象集合
     */
    List<Setmeal> list(Setmeal setmeal);

}
