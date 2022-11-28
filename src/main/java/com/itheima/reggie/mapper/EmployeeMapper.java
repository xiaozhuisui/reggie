package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/*做一个mapper注解映射
需要继承自BaseMapper
需要一个泛型的实体类*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
