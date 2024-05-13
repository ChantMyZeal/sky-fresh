package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

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

    /**
     * 统计指定日期区间内的订单数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回订单报告VO
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计指定日期区间内销量排名前十的商品
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 返回销量前十报告VO
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * 导出运营数据报表
     *
     * @param response 输出流所需的响应对象
     * @param begin    开始日期
     * @param end      结束日期
     */
    void exportBusinessData(HttpServletResponse response, LocalDate begin, LocalDate end);

}
