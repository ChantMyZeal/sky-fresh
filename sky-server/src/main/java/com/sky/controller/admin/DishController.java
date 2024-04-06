package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Tag(name = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 清理缓存数据
     *
     * @param pattern 字符串匹配
     */
    private void cleanCache(String pattern) {// todo 优化到service层中
        //将指定的缓存数据，即pattern匹配的key清理掉
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品DTO
     * @return 返回统一响应结果
     */
    @PostMapping
    @Operation(summary = "新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 菜品分页查询DTO
     * @return 返回统一响应结果
     */
    @GetMapping("/page")
    @Operation(summary = "菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     *
     * @param ids 菜品ID集合
     * @return 返回统一响应结果
     */
    @DeleteMapping
    @Operation(summary = "菜品批量删除")
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除：{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据ID查询菜品（附带口味信息）
     *
     * @param id 菜品ID
     * @return 返回统一响应结果
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据ID查询菜品：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO 菜品DTO
     * @return 返回统一响应结果
     */
    @PutMapping
    @Operation(summary = "修改菜品")// todo 考虑是否添加启售中的菜品无法修改的逻辑
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);

        //将所有的菜品缓存数据清理掉
        cleanCache("dish_*");

        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 分类ID
     * @return 返回统一响应结果
     */
    @GetMapping("/list")
    @Operation(summary = "根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 启售或禁售菜品
     *
     * @param status 传入的菜品状态参数
     * @param id     需要启售或禁售的菜品ID
     * @return 返回统一响应结果
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "启售或禁售菜品")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)//也要清除套餐的缓存 todo 看能否优化到service层中
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        log.info("启售或禁售菜品：{},{}", status, id);

        //将所有的菜品缓存数据清理掉
        cleanCache("dish_*");

        dishService.startOrStop(status, id);
        return Result.success();
    }

}
