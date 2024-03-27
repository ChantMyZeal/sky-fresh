package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 员工登录DTO
     * @return 返回实体对象
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端传过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 退出登录
     */
    @Override
    public void logout() {
        //手动删除ThreadLocal线程局部变量，防止内存泄漏
        BaseContext.removeCurrentId();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 员工DTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {

        //System.out.println("当前线程的ID："+Thread.currentThread().getId());
        Employee employee = new Employee();

        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);
        //设置帐号状态，默认正常 1正常0锁定
        employee.setStatus(StatusConstant.ENABLE);
        //设置密码，默认123456,并调用工具类实现md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //设置当前记录的创建时间和修改时间，已在AutoFillAspect中实现公共字段自动填充
        //employee.setCreateTime(LocalDateTime.now());employee.setUpdateTime(LocalDateTime.now());
        //从ThreadLocal线程局部变量中取出当前登录用户ID，设置当前记录创建人ID和修改人ID，已在AutoFillAspect中实现公共字段自动填充
        //employee.setCreateUser(BaseContext.getCurrentId());employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);

    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO 员工分页查询DTO
     * @return 返回封装好的PageResult对象
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //1.设置分页参数
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //2.执行查询
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        //3.封装PageBean对象
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 启用或禁用员工账号
     *
     * @param status 状态参数，0表示禁用，1表示启用
     * @param id     需要启用或禁用的员工ID
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        /*Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);*/
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据ID查询员工信息
     *
     * @param id 员工ID
     * @return 返回员工实体类
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO 员工DTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);
        //设置当前记录的修改时间和修改人，已在AutoFillAspect中实现公共字段自动填充
        //employee.setUpdateTime(LocalDateTime.now());
        //从ThreadLocal线程局部变量中取出当前登录用户ID，设置当前记录创建人ID和修改人ID，已在AutoFillAspect中实现公共字段自动填充
        //employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }

}
