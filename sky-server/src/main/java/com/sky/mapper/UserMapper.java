package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户
     *
     * @param openid 用户openid
     * @return 返回用户实体对象
     */
    @Select("select * from user where openid = #{openid} limit 1")
    User getByOpenid(String openid);

    /**
     * 插入新用户数据
     *
     * @param user 用户实体对象
     */
    void insert(User user);

}
