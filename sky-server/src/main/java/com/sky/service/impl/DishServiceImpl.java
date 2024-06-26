package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 抽取出的插入口味公共方法
     *
     * @param flavors 口味集合
     * @param dishId  菜品ID
     */
    public void saveFlavor(List<DishFlavor> flavors, Long dishId) {
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 新增菜品和对应的口味数据
     *
     * @param dishDTO 菜品DTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //向菜品表插入数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);

        //获取insert语句生成的主键值
        Long dishId = dish.getId();

        //复用抽取出的插入口味公共方法
        saveFlavor(dishDTO.getFlavors(), dishId);
        /*List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavors);
        }*/
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 菜品分页查询DTO
     * @return 返回封装好的PageResult对象
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //1.设置分页参数
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //2.执行查询
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        //3.封装PageResult对象
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 菜品批量删除
     *
     * @param ids 菜品ID集合
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断集合中的菜品是否满足删除条件——是否已经起售
        Long enabledDishCount = dishMapper.countByIdsAndStatus(ids, StatusConstant.ENABLE);
        if (enabledDishCount > 0) {
            //有菜品起售中，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE_CANNOT_DELETE);
        }
        /*for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                //当前菜品起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }*/

        //判断当前菜品是否满足删除条件——是否被套餐关联
        Long relatedSetmealCount = setmealDishMapper.getSetmealCountByDishIds(ids);
        if (relatedSetmealCount > 0) {
            //有菜品被套餐关联，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        /*List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            //当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }*/

        //根据菜品ID集合批量删除菜品数据和口味数据
        dishMapper.deleteByIds(ids);//删除菜品表中的菜品数据
        dishFlavorMapper.deleteByDishIds(ids);//删除菜品关联的口味数据
    }

    /**
     * 根据ID查询菜品及其对应口味数据
     *
     * @param id 菜品ID
     * @return 返回菜品VO
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //根据ID查询菜品
        Dish dish = dishMapper.getById(id);

        //根据菜品ID查询对应的口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);

        //将查询到的数据封装到菜品VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 修改菜品及其口味数据
     *
     * @param dishDTO 菜品DTO
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        //获取菜品ID
        Long dishId = dishDTO.getId();

        //封装为单例列表，复用批量查询接口，判断菜品是否满足修改条件——是否已经起售
        Long enabledDishCount = dishMapper.countByIdsAndStatus(Collections.singletonList(dishId), StatusConstant.ENABLE);
        if (enabledDishCount > 0) {
            //菜品起售中，不能修改
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE_CANNOT_UPDATE);
        }

        //修改菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        //封装为单例列表，复用批量删除接口，删除原有的口味数据
        dishFlavorMapper.deleteByDishIds(Collections.singletonList(dishId));

        //重新插入口味数据，复用抽取出的插入口味公共方法
        saveFlavor(dishDTO.getFlavors(), dishId);
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 分类ID
     * @return 返回菜品集合
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口味
     *
     * @param dish 菜品实体对象
     * @return 返回菜品VO集合
     */
    @Override
    @Cacheable(cacheNames = "dishCache", key = "#dish.getCategoryId()")//key: dishCache::{categoryId}
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    /**
     * 启售或禁售菜品
     *
     * @param status 传入的菜品状态参数
     * @param id     菜品ID
     */
    @Override
    @Transactional
    @CacheEvict(cacheNames = {"dishCache", "setmealCache"}, allEntries = true)//也要清除套餐的缓存
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);

        //停售菜品时自动停售关联的套餐
        if (Objects.equals(status, StatusConstant.DISABLE)) {
            //查找要停售的菜品ID对应的套餐ID
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(Collections.singletonList(id));
            if (setmealIds != null && !setmealIds.isEmpty()) {
                //遍历套餐ID集合，停售套餐
                setmealIds.forEach(setmealId -> {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(status)
                            .build();
                    setmealMapper.update(setmeal);
                });
            }
        }
    }

}
