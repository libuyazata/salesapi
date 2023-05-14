package com.yaz.alind.contoller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionHandlerContoller implements HandlerInterceptor {
	
	
//	@Override
//    public boolean preHandle(HttpServletRequest request,
//            HttpServletResponse response, Object handler) throws Exception {
////		String token = (String) request.getSession().getAttribute("token");
//		String token = request.getHeader("token");
//        System.out.println("SessionHandlerContoller,Inside pre handle,token: "+token);
//        return true;
//    }
	
	@Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
//		String token = (String) request.getSession().getAttribute("token");
		String token = request.getHeader("token");
        System.out.println("SessionHandlerContoller,Inside pre handle,token: "+token);
        return true;
    }
	
	@RequestMapping(value="/sessionhandler", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> sessionHandler() throws Exception {
		System.out.println("SessionHandlerContoller,sessionHandler, ");
		
		return null;
	}

}
