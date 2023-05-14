package com.yaz.security;

public interface Iconstants {

	public static final String BEARER_TOKEN= "Bearer ";
	public static final String HEADER= "authorization";
	public static final String ISSUER= "ducat-springboot-jwttoken";
	public static final String SECRET_KEY= "Springbootjwttutorial";
	public static final String ON_GOING = "on going";
	public static final String PENDING = "pending" ;
	public static final String COMPLETED = "completed";
	public static final String NON_ALLOTTED = "new";
	
	// The session time in minutes
	public static final int SESSION_TIME = 90;
	//OTP
	public static final int OTP_TIME = 10;
	
	public final static String BUILD_NAME = "dev-alindsales";
//	public final static String BUILD_NAME = "alindsalesapp";
//	public final static String BUILD_NAME = "ROOT";
	//Image path : http://149.56.14.20:8080/AlindSalesUploadFiles/ServiceReport/353/20200617113314.jpg
//	public final static String IP_ADDRESS_PORT = "http:\\149.56.14.20:8080\\";
//	public final static String EMPLOYEE_PROFILE_PIC_LOCATION = "AlindSalesUploadFiles/Employee/ProfilePic/";
//	public final static String SERVICE_REPORT_MINUTES_FILE_LOCATION = "AlindSalesUploadFiles/ServiceReport/";
	///usr/local/tomcat/AlindUploadFiles/ServiceReport
	// Test
	public final static String SERVICE_REPORT_MINUTES_FILE_LOCATION = "AlindSalesUploadFilesTest/ServiceReport/";
	public final static String EMPLOYEE_PROFILE_PIC_LOCATION = "AlindSalesUploadFilesTest/Employee/ProfilePic/";
//	public final static String SERVICE_REPORT_MINUTES_FILE_LOCATION = "AlindSalesUploadFilesTest/Employee/ProfilePic/";
//	public final static String PROJECT_DOCUMENT_LOCATION = "AlindSalesUploadFiles_Test/Employee/ProfilePic";
	public final static String ALIND_RELAYS_LOGO = "/WEB-INF/views/images/Alind_Banner.png";
//	public final static String ALIND_RELAYS_LOGO = "D:/Alind_Banner.png";

}