package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 提取出的插入套餐-菜品关系表的公共方法
     *
     * @param setmealDishes 套餐-菜品关系集合
     * @param setmealId     套餐ID
     */
    public void saveSetmealDish(List<SetmealDish> setmealDishes, Long setmealId) {
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
            setmealDishMapper.insertBatch(setmealDishes);
        }

    }

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDTO 套餐DTO
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        //向套餐表插入数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);

        //获取insert语句生成的套餐ID
        Long setmealId = setmeal.getId();

        //复用抽取出的公共方法，保存套餐和菜品的关联关系
        saveSetmealDish(setmealDTO.getSetmealDishes(), setmealId);
        /*List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);*/
    }

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO 套餐分页查询DTO
     * @return 返回封装好的PageResult对象
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        //设置分页参数
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        //执行分页查询
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        //封装PageResult对象
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     *
     * @param ids 套餐ID集合
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断集合中的菜品是否满足删除条件——是否已经起售
        Long enabledSetmealCount = setmealMapper.getCountByIdsAndStatus(ids, StatusConstant.ENABLE);
        if (enabledSetmealCount > 0) {
            //有套餐起售中，不能删除
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        /*ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if (StatusConstant.ENABLE.equals(setmeal.getStatus())) {
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });*/

        //根据套餐ID集合批量删除套餐表数据和套餐菜品关系表数据
        setmealMapper.deleteByIds(ids);
        setmealDishMapper.deleteBySetmealIds(ids);
        /*ids.forEach(setmealId -> {
            //删除套餐表中的数据
            setmealMapper.deleteById(setmealId);
            //删除套餐菜品关系表中的数据
            setmealDishMapper.deleteBySetmealId(setmealId);
        });*/
    }

    /**
     * 根据id查询套餐和关联的菜品数据
     *
     * @param id 套餐ID
     * @return 返回套餐VO
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO 套餐DTO
     */
    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //获取套餐ID
        Long setmealId = setmealDTO.getId();

        //封装为单例列表，复用批量查询接口，判断套餐是否满足修改条件——是否已经起售
        Long enabledSetmealCount = setmealMapper.getCountByIdsAndStatus(Collections.singletonList(setmealId), StatusConstant.ENABLE);
        if (enabledSetmealCount > 0) {
            //套餐起售中，不能修改
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }

        //修改套餐基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        //封装为单例列表，复用批量删除接口，删除套餐和菜品的关联关系
        setmealDishMapper.deleteBySetmealIds(Collections.singletonList(setmealId));

        //复用抽取出的公共方法，重新插入套餐和菜品的关联关系
        saveSetmealDish(setmealDTO.getSetmealDishes(), setmealId);
        /*List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);*/
    }

    /**
     * 套餐条件查询
     *
     * @param setmeal 套餐实体对象
     * @return 返回套餐实体对象集合
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        return setmealMapper.list(setmeal);
    }

    /**
     * 根据ID查询菜品选项
     *
     * @param id 菜品ID
     * @return 返回菜品选项VO集合
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return dishMapper.getDishItemBySetmealId(id);
    }

    /**
     * 套餐起售、停售
     *
     * @param status 套餐状态参数
     * @param id     套餐ID
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        //启售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        if (Objects.equals(status, StatusConstant.ENABLE)) {
            Long disabledDishCount = dishMapper.countBySetmealIdAndStatus(id, StatusConstant.DISABLE);
            if (disabledDishCount > 0) {
                //套餐内有停售菜品，不能启售
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
            /*List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList != null && !dishList.isEmpty()) {
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE.equals(dish.getStatus())) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }*/
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

}
