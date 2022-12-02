package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {
  //自动装配
  @Autowired
  protected EmployeeService employeeService;

//  这个是定义方法
  @PostMapping("/login")
  public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
    //1、将页面提交的密码password进行md5加密处理
    String password = employee.getPassword();
    password = DigestUtils.md5DigestAsHex(password.getBytes());
    //2、根据页面提交的用户名username查询数据库 支持lambda
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    //查询条件条件 名称相等
    queryWrapper.eq(Employee::getUsername,employee.getUsername());

    Employee emp = employeeService.getOne(queryWrapper);

    //3、如果没有查询到则返回登录失败结果
    if(emp == null){
      return R.error("登录失败");
    }

    //4、密码比对，如果不一致则返回登录失败结果
    if(!emp.getPassword().equals(password)){
      return R.error("登录失败");
    }

    //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
    if(emp.getStatus() == 0){
      return R.error("账号已禁用");
    }

    //6、登录成功，将员工id存入Session并返回登录成功结果
    request.getSession().setAttribute("employee",emp.getId());
    return R.success(emp);
  }

  @PostMapping("/logout")
  public R<String> loginOut(HttpServletRequest request){
    request.getSession().removeAttribute("employee");
    System.out.println(request);
    return R.success("退出成功");
  }

  @PostMapping
  public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
    log.info("接收到的参数已经请求对象{},{},{}",request,employee,"123456".getBytes());
    employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
    employee.setCreateTime(LocalDateTime.now());
    employee.setUpdateTime(LocalDateTime.now());

    Long empId=(Long) request.getSession().getAttribute("employee");
//    employee.setCreateUser(empId);
//    employee.setUpdateUser(empId);
    employeeService.save(employee);
    return R.success("新增员工成功");
  }
  @GetMapping("/page")
  public R<Page> page(int page,int pageSize,String name ){
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    log.info("page={},pageSize={},name={}",page,pageSize,name);
    Page pageInfo = new Page(page,pageSize);
    //    找相似 并且降序
    queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name).orderByDesc(Employee::getUpdateTime);
    employeeService.page(pageInfo,queryWrapper);
    return R.success(pageInfo);
  }
  @PutMapping()
  public R<String> updateStatus(@RequestBody Employee employee,HttpServletRequest request){
    log.info("{},{}",employee,request);
    Long empId =(Long) request.getSession().getAttribute("employee");
//    employee.setUpdateUser(empId);
//    employee.setUpdateTime(LocalDateTime.now());
    employeeService.updateById(employee);
    return R.success("更新成功！");
  }

  @GetMapping("/{id}")
  public R<Employee> getDetailById(@PathVariable("id") String id){
    Employee emp=employeeService.getById(id);
    if(emp != null){
    return R.success(emp);
    }
    return R.error("没找到指定人员！");
  }

}
