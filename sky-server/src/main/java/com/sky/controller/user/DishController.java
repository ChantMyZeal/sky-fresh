package com.sky.controller.user;

import com.alibaba.fastjson2.JSON;
import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Tag(name = "C端-菜品浏览接口")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据分类ID查询菜品
     *
     * @param categoryId 分类ID
     * @return 返回统一响应结果
     */
    @GetMapping("/list")
    @Operation(summary = "根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {//todo 优化为使用 spring cache
        //构造redis中的key，规则：dish_分类ID
        String key = "dish_" + categoryId;
        //查询redis中是否存在菜品数据
        Object object = redisTemplate.opsForValue().get(key);
        String jsonString = JSON.toJSONString(object);
        List<DishVO> list = JSON.parseArray(jsonString, DishVO.class);

        //如果存在，直接返回，无需查询数据库
        if (list != null && !list.isEmpty()) {
            return Result.success(list);
        }

        //如果不存在，需要查询数据库，将查询到的数据放入redis中，再返回
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//在数据库中查询起售中的菜品，并存入redis中
        list = dishService.listWithFlavor(dish);
        redisTemplate.opsForValue().set(key, list);
        return Result.success(list);
    }

}
