package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 员工登录DTO
     * @return 返回实体对象
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 新增员工
     *
     * @param employeeDTO 员工DTO
     */
    void save(EmployeeDTO employeeDTO);

}
