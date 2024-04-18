package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

public interface UserService {

    /**
     * 微信登录
     *
     * @param userLoginDTO 用户登录DTO
     * @return 返回用户登录VO
     */
    UserLoginVO wxLogin(UserLoginDTO userLoginDTO);

}
