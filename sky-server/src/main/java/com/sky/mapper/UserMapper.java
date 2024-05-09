package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

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

    /**
     * 根据用户ID查询用户数据
     *
     * @param userId 用户ID
     * @return 返回用户实体对象
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    /**
     * 根据时间范围统计用户数量
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @return 返回用户数量
     */
    Integer countByTimeRange(LocalDateTime begin, LocalDateTime end);

    /**
     * 根据用户电话号码查询用户数据
     *
     * @param phone 用户电话号码
     * @return 返回用户实体对象
     */
    @Select("select * from user where phone = #{phone}")
    User getByPhone(String phone);

}
