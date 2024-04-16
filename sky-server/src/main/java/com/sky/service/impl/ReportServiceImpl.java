package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
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
     * 创建集合用于存储从begin到end范围内的所有日期
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回日期集合
     */
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        //不超过结束日期时，计算指定日期后一天的日期并加入集合中
        for (; !begin.isAfter(end); begin = begin.plusDays(1)) {
            dateList.add(begin);
        }
        return dateList;
    }

    /**
     * 统计指定日期区间内的营业额数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回营业额报告VO
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);//创建集合用于存储从begin到end范围内的日期
        List<BigDecimal> turnoverList = new ArrayList<>();//创建集合用于存储每日营业额

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
        List<LocalDate> dateList = getDateList(begin, end);//创建集合用于存储从begin到end范围内的日期
        List<Integer> newUserList = new ArrayList<>();//创建集合用于存储每日新增用户数量
        List<Integer> totalUserList = new ArrayList<>();//创建集合用于存储每日当前总用户数量

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

    /**
     * 统计指定日期区间内的订单数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回订单报告VO
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);//创建集合用于存储从begin到end范围内的日期
        List<Integer> orderCountList = new ArrayList<>();//创建集合用于存储每日订单总数
        List<Integer> validOrderCountList = new ArrayList<>();//创建集合用于存储每日有效订单数

        for (LocalDate date : dateList) {
            orderCountList.add(orderMapper.countByDateAndStatus(date, null));
            validOrderCountList.add(orderMapper.countByDateAndStatus(date, Orders.COMPLETED));
        }

        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).orElseThrow();//日期区间内的订单总数
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).orElseThrow();//日期区间内的有效订单数
        Double orderCompletionRate = totalOrderCount.equals(0) ? 0.0 : validOrderCount.doubleValue() / totalOrderCount;//订单完成率

        //封装结果数据
        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 统计指定日期区间内销量排名前十的商品
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回销量前十报告VO
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        //构造开始时间和结束时间，调用Mapper查询数据库
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop(beginTime, endTime, 10, Orders.COMPLETED);

        //将查询得到的结果转换为需要返回的格式
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).toList();
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getTotal).toList();
        String nameList = StringUtils.join(names, ",");
        String numberList = StringUtils.join(numbers, ",");

        //封装结果数据
        return SalesTop10ReportVO
                .builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

}