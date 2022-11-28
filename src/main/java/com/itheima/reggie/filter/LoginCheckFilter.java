package com.itheima.reggie.filter;


import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//检测用户是否已经完成登录
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
  public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    String[] urls = new String[]{"/employee/login", "/employee/logout", "/backend/**", "/front/**"};
    log.info("拦截到请求:{},{},{},{},{}", request.getRequestURI(), request.getMethod(),request.getParts(),request.getQueryString(),request.getRemoteHost());
    boolean checkResult = check(urls, request.getRequestURI());
    System.out.println(checkResult);
    if (!checkResult && request.getSession().getAttribute("employee") != null) {
//      说明未登录
      response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
      return;
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  //    检查本次请求是否需要放行
  public boolean check(String[] urls, String requestURI) {
    for (String S : urls) {
//      这个地方不能用字符串 因为存在通配符 要么就用正则
//      if (S.equals(requestURI)) {
//        return true;
//      }
      if(PATH_MATCHER.match(S,requestURI)){
        return true;
      }
    }
    return false;
  }
}
