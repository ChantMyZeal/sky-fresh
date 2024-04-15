package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    /**
     * 统计指定日期区间内的营业额数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回营业额报告VO
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计指定日期区间内的用户数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回用户报告VO
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

}
