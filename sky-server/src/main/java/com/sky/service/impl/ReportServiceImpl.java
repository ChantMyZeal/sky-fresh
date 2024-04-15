package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {//todo 考虑在mysql或redis中新建一个表存放营业额，订单完成时自动更新

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 统计指定日期区间内的营业额数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回营业额报告VO
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //创建集合用于存储从begin到end范围内的日期
        List<LocalDate> dateList = new ArrayList<>();
        //不超过结束日期时，计算指定日期后一天的日期并加入集合中
        for (; !begin.isAfter(end); begin = begin.plusDays(1)) {
            dateList.add(begin);
        }

        //创建集合用于存储每日营业额
        List<BigDecimal> turnoverList = new ArrayList<>();
        //查询集合中每个日期对应的营业额数据，即状态为已完成的订单总金额
        for (LocalDate date : dateList) {
            BigDecimal turnover = orderMapper.sumByDateAndStatus(date, Orders.COMPLETED);
            if (turnover == null) {
                turnover = BigDecimal.valueOf(0.0);
            }
            turnoverList.add(turnover);
        }

        //封装结果数据
        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 统计指定日期区间内的用户数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回用户报告VO
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        //创建集合用于存储从begin到end范围内的日期
        List<LocalDate> dateList = new ArrayList<>();
        //不超过结束日期时，计算指定日期后一天的日期并加入集合中
        for (; !begin.isAfter(end); begin = begin.plusDays(1)) {
            dateList.add(begin);
        }

        //创建集合用于存储每日新增用户数量
        List<Integer> newUserList = new ArrayList<>();
        //创建集合用于存储每日当前总用户数量
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            totalUserList.add(userMapper.countByTimeRange(null, endTime));//总用户数量
            newUserList.add(userMapper.countByTimeRange(beginTime, endTime));//新增用户数量
        }

        //封装结果数据
        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

}
