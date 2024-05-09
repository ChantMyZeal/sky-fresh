package com.sky.controller.user;

import com.sky.dto.UserLoginDTO;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/user")
@Tag(name = "C端-用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录DTO
     * @return 返回统一响应结果
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录，授权码：{}，手机号：{}", userLoginDTO.getCode(), userLoginDTO.getPhone());
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }

    /**
     * 退出
     *
     * @return 返回统一响应结果
     */
    @PostMapping("/logout")
    @Operation(summary = "用户退出")
    public Result<String> logout() {
        log.info("用户退出...");
        userService.logout();
        return Result.success();
    }

    /**
     * 用户注册
     *
     * @param userLoginDTO 用户登录DTO
     * @return 返回统一响应结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<String> register(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户注册，手机号：{}", userLoginDTO.getPhone());
        userService.register(userLoginDTO);
        return Result.success();
    }

}
