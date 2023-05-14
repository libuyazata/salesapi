package com.yaz.alind.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AlindException extends RuntimeException  {
	
	public AlindException(){
		super();
//		getSessionExpiredException();
	}
	
	public ResponseEntity<Map<String,Object>> getSessionExpiredException(){
		System.out.println("AlindException, getSessionExpiredException");
		return  new ResponseEntity<Map<String,Object>>(HttpStatus.UNAUTHORIZED);
	}

}
