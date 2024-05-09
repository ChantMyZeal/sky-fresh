package com.sky.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.AccountAlreadyExistsException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.LoginFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 调用微信接口服务，获取微信用户的openid
     *
     * @param code 用户授权码
     * @return 返回openid
     */
    private String getOpenid(String code) {
        //调用微信接口服务，获取当前用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }

    /**
     * 微信登录
     *
     * @param userLoginDTO 用户登录DTO
     * @return 返回用户登录VO
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        String code = userLoginDTO.getCode();
        String phone = userLoginDTO.getPhone();
        String openid;
        User user;

        //判断是小程序登录还是网页登陆
        if (code == null || code.isEmpty()) {//网页登陆
            user = userMapper.getByPhone(phone);
            if (user == null) {//账号不存在
                throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
            }
            String password = DigestUtils.md5DigestAsHex(userLoginDTO.getPassword().getBytes());//对前端传过来的明文密码进行md5加密处理
            //密码比对
            if (!password.equals(user.getPassword())) {//密码错误
                throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
            }
        } else {//小程序登录
            openid = getOpenid(code);
            //判断openid是否为空，如果为空表示登录失败，抛出业务异常
            if (openid == null || openid.isEmpty()) {
                throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
            }
            //判断当前用户是否为新用户
            user = userMapper.getByOpenid(openid);
            //如果是新用户，自动完成注册
            if (user == null) {
                user = User.builder()
                        .openid(openid)
                        .createTime(LocalDateTime.now())
                        .build();
                userMapper.insert(user);
            }
        }

        //为用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        //返回这个用户对象
        return UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
    }

    /**
     * 退出登录
     */
    @Override
    public void logout() {
        //手动删除ThreadLocal线程局部变量，防止内存泄漏
        BaseContext.removeCurrentId();
    }

    /**
     * 用户注册
     *
     * @param userLoginDTO 用户登录DTO
     */
    @Override
    public void register(UserLoginDTO userLoginDTO) {
        String phone = userLoginDTO.getPhone();
        User user = userMapper.getByPhone(phone);

        if (user != null) {//账号已存在
            throw new AccountAlreadyExistsException(MessageConstant.ACCOUNT_ALREADY_EXISTS);
        } else {//账号不存在，立即注册
            String password = DigestUtils.md5DigestAsHex(userLoginDTO.getPassword().getBytes());//对前端传过来的明文密码进行md5加密处理
            user = User.builder()
                    .phone(phone)
                    .password(password)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
    }

}
