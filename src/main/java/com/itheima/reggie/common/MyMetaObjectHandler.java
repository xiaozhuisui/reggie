package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;


@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
  //  插入操作自动填充 这个可以
//  @Autowired
//  HttpSession session;

  @Override
  public void insertFill(MetaObject metaObject) {
    log.info("公共字段自动填充 insert");
    log.info(metaObject.toString());
    metaObject.setValue("createTime", LocalDateTime.now());
    metaObject.setValue("updateTime", LocalDateTime.now());
//    metaObject.setValue("createUser", session.getAttribute("employee"));
//    metaObject.setValue("updateUser", session.getAttribute("employee"));
    metaObject.setValue("updateUser", BaseContext.getCurrentId());
    metaObject.setValue("createUser", BaseContext.getCurrentId());
  }
  //更新操作自动填充
  @Override
  public void updateFill(MetaObject metaObject) {
    log.info("公共字段自动填充 update");
    log.info(metaObject.toString());
//    metaObject.setValue("updateUser", session.getAttribute("employee"));
    metaObject.setValue("updateUser", BaseContext.getCurrentId());
    metaObject.setValue("updateTime", LocalDateTime.now());
  }
}
