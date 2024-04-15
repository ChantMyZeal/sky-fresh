package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     *
     * @param orders 订单数据实体对象
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber 订单号
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders 订单实体对象
     */
    void update(Orders orders);

    /**
     * 分页条件查询并按下单时间排序
     *
     * @param ordersPageQueryDTO 订单分页查询DTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     *
     * @param id 订单ID
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据状态统计订单数量
     *
     * @param status 订单状态
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 根据订单状态和下单时间阈值查询订单
     *
     * @param status    订单状态
     * @param orderTime 下单时间阈值
     * @return 返回订单实体对象集合
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 根据下单日期和订单状态计算总金额
     *
     * @param date   下单日期
     * @param status 订单状态
     * @return 返回总金额
     */
    @Select("select sum(amount) from orders where order_time like concat(#{date},'%') and status = #{status}")
    BigDecimal sumByDateAndStatus(LocalDate date, Integer status);

    /**
     * 根据下单日期和订单状态查询订单数量
     *
     * @param date   下单
     * @param status 订单状态
     * @return 返回订单数量
     */
    Integer countByDateAndStatus(LocalDate date, Integer status);

    /**
     * 统计指定订单状态和时间区间内销量处于前几名的商品及其销量
     *
     * @param begin  开始时间
     * @param end    结束时间
     * @param limit  排名限制
     * @param status 订单状态
     * @return 返回商品销售DTO集合
     */
    List<GoodsSalesDTO> getSalesTop(LocalDateTime begin, LocalDateTime end, Integer limit, Integer status);

}
