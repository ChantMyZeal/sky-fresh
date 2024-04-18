package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.vo.EmployeeLoginVO;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 员工登录DTO
     * @return 返回员工登录VO
     */
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 修改密码
     *
     * @param passwordEditDTO 密码编辑DTO
     */
    void editPassword(PasswordEditDTO passwordEditDTO);

    /**
     * 新增员工
     *
     * @param employeeDTO 员工DTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO 员工分页查询DTO
     * @return 返回封装好的PageResult对象
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用或禁用员工账号
     *
     * @param status 状态参数，0表示禁用，1表示启用
     * @param id     需要启用或禁用的员工ID
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据ID查询员工信息
     *
     * @param id 员工ID
     * @return 返回员工实体类
     */
    Employee getById(Long id);

    /**
     * 编辑员工信息
     *
     * @param employeeDTO 员工DTO
     */
    void update(EmployeeDTO employeeDTO);

}
