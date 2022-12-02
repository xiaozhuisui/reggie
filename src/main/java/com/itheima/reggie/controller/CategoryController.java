package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("category")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @GetMapping("/page")
  public R<Page> page(int page,int pageSize,String name){
    LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByAsc(Category::getSort);
    Page pageInfo = new Page(page,pageSize);
    categoryService.page(pageInfo);
    return R.success(pageInfo);
  }

  @PostMapping()
  public R<String> add(HttpServletRequest request, @RequestBody Category category){
    categoryService.save(category);
    return R.success("添加成功");
  }

  @PutMapping
  public R<String> update(HttpServletRequest request, @RequestBody Category category){
    categoryService.updateById(category);
    return R.success("更新成功");
  }
}
