package io.pivotal.poc.spring.ratings.controllers;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sgupta on 11/4/14.
 */
public class ResponseInterceptor extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    response.addHeader("x-pivotal-ip-address", request.getLocalAddr());
    return true;
  }

}
