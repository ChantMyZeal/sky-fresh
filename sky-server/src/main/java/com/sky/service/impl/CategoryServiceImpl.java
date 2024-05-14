package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类业务层
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     *
     * @param categoryDTO 分类DTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        //属性拷贝
        BeanUtils.copyProperties(categoryDTO, category);

        //分类状态默认为禁用状态0
        category.setStatus(StatusConstant.DISABLE);

        //设置创建时间和创建人，已在AutoFillAspect中实现公共字段自动填充
        //category.setCreateTime(LocalDateTime.now());category.setCreateUser(BaseContext.getCurrentId());
        //设置修改时间和修改人，已在AutoFillAspect中实现公共字段自动填充
        //category.setUpdateTime(LocalDateTime.now());category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO 分类分页查询DTO
     * @return 返回封装好的PageResult对象
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据ID删除分类
     *
     * @param id 分类ID
     */
    @Override
    public void deleteById(Long id) {
        //查询当前分类是否已启用，如果启用了就抛出业务异常
        if (categoryMapper.getStatusById(id) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_ON_USE_CANNOT_DELETE);
        }

        //查询当前分类是否关联了菜品，如果关联了就抛出业务异常
        Integer count = dishMapper.countByCategoryIdAndStatus(id, null);
        if (count > 0) {
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        //查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryIdAndStatus(id, null);
        if (count > 0) {
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        //删除分类数据
        categoryMapper.deleteById(id);
    }

    /**
     * 修改分类
     *
     * @param categoryDTO 分类DTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        //查询当前分类是否已启用，如果启用了就抛出业务异常
        if (categoryMapper.getStatusById(categoryDTO.getId()) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_ON_USE_CANNOT_UPDATE);
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        //设置修改时间和修改人，已在AutoFillAspect中实现公共字段自动填充
        //category.setUpdateTime(LocalDateTime.now());category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    /**
     * 启用、禁用分类
     *
     * @param status 分类状态参数
     * @param id     分类ID
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                //.updateTime(LocalDateTime.now())
                //.updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    /**
     * 根据类型查询分类
     *
     * @param type 分类类型参数
     * @return 返回分类实体集合
     */
    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }

}
