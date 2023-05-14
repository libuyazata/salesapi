package com.yaz.alind.contoller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.yaz.alind.service.AlindException;
//https://www.mkyong.com/spring-boot/spring-rest-error-handling-example/
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {


	@ExceptionHandler(AlindException.class)
	public void sessionExpired(HttpServletResponse response) throws IOException{
		System.out.println("CustomGlobalExceptionHandler, sessionExpired: ");
		response.sendError(HttpStatus.UNAUTHORIZED.value());
	}

}
