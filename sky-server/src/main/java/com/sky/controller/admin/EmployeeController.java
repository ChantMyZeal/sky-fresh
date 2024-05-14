package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Tag(name = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     *
     * @param employeeLoginDTO 数据传输实体类
     * @return 返回统一响应结果
     */
    @PostMapping("/login")
    @Operation(summary = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        EmployeeLoginVO employeeLoginVO = employeeService.login(employeeLoginDTO);
        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return 返回统一响应结果
     */
    @PostMapping("/logout")
    @Operation(summary = "员工退出")
    public Result<String> logout() {
        log.info("员工退出...");
        employeeService.logout();
        return Result.success();
    }

    /**
     * 修改密码
     *
     * @param passwordEditDTO 密码编辑DTO
     * @return 返回统一响应结果
     */
    @PutMapping("/editPassword")
    @Operation(summary = "修改密码")
    public Result<String> editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改密码，员工ID：{}", passwordEditDTO.getEmpId());
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 员工DTO
     * @return 返回统一响应结果
     */
    @PostMapping
    @Operation(summary = "新增员工")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        //System.out.println("当前线程的ID："+Thread.currentThread().getId());
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO 员工分页查询DTO
     * @return 返回统一响应结果
     */
    @GetMapping("/page")
    @Operation(summary = "员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询，参数为：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用或禁用员工账号
     *
     * @param status 状态参数，0表示禁用，1表示启用
     * @param id     需要启用或禁用的员工ID
     * @return 返回统一响应结果
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "启用或禁用员工账号")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用或禁用员工账号：{},{}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据ID查询员工信息
     *
     * @param id 员工ID
     * @return 返回统一响应结果
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询员工信息")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据ID查询员工信息，ID：{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO 员工DTO
     * @return 返回统一响应结果
     */
    @PutMapping
    @Operation(summary = "编辑员工信息")
    public Result<String> update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * 删除员工信息
     *
     * @param id 员工ID
     * @return 返回统一响应结果
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除员工信息")
    public Result<String> delete(Long id) {
        log.info("删除员工信息，ID：{}", id);
        employeeService.delete(id);
        return Result.success();
    }

}
