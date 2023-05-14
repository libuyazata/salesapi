package com.yaz.alind.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaz.alind.dao.UserDAO;
import com.yaz.alind.model.AuditJson;
import com.yaz.alind.model.AuditJsonFactory;
import com.yaz.alind.model.AuditLog;
import com.yaz.alind.model.AuditLogFactory;
import com.yaz.alind.model.Department;
import com.yaz.alind.model.Employee;
import com.yaz.alind.model.EmployeeMinData;
import com.yaz.alind.model.EmployeeMinDataFactory;
import com.yaz.alind.model.ForgetPasswordFactory;
import com.yaz.alind.model.ForgetPasswordModel;
import com.yaz.alind.model.MailModel;
import com.yaz.alind.model.MailModelFactory;
import com.yaz.alind.model.NatureOfJobsCallReg;
import com.yaz.alind.model.TokenModel;

public class UserServiceImpl implements UserService {


	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UtilService utilService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	MailModelFactory mailModelFactory;
	@Autowired
	EmployeeMinDataFactory employeeMinDataFactory;
	@Autowired
	AuditLogFactory auditLogFactory;
	@Autowired
	AuditJsonFactory auditJsonFactory;
	@Autowired
	ForgetPasswordFactory forgetPasswordFactory;


	@Override
	public TokenModel saveOrUpdateToken(TokenModel tokenModel) {
		try{
			String uuid = utilService.createToken();
			tokenModel.setToken(uuid);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateToken: "+e.getMessage());
		}
		return tokenModel;
	}

	@Override
	public TokenModel getTokenModelByUserId(int userId) {
		return userDAO.getTokenModelByUserId(userId);
	}

	@Override
	public List<Employee> getAllEmployee(String searchKeyWord) {
		List<Employee> employees = null;
		try{
			if(searchKeyWord.isEmpty()){
				employees = userDAO.getAllEmployee();
			}else{
				employees = searchEmployee(searchKeyWord);
			}
			for(int i=0;i<employees.size();i++){
				employees.get(i).setViewPass(null);
				employees.get(i).setPasswordHash("");
				employees.get(i).setSlNo(i+1);
				//				System.out.println("FullName: "+employees.get(i).getFullName());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAuthentication: "+e.getMessage());
		}
		return employees;
	}

	@Override
	public List<Department> getAllDepartment() {
		List<Department> departments = null;
		try{
			departments = userDAO.getAllDepartment();
			for(int i=0;i<departments.size();i++){
				departments.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllDepartment: "+e.getMessage());
		}
		return departments;
	}

	@Override
	public Department saveOrUpdateDepartment(Department department) {
		return userDAO.saveOrUpdateDepartment(department);
	}

	@Override
	public Employee getAuthentication(String userName, String password) {
		Employee employee = null;
		TokenModel tokenModel = null;
		boolean timeStatus = false;
		try{
//			System.out.println("UserServiceImpl,getAuthentication,userName: "+userName+", password: "+password);
			employee = userDAO.getAuthentication(userName, password);
			tokenModel = userDAO.getTokenModelByUserId(employee.getEmployeeId());
			//			System.out.println("UserServiceImpl,getAuthentication,getEmployeeId: "+employee.getEmployeeId()+", token: "+tokenModel.getToken());
			if(employee != null ){
				if(tokenModel != null){
					//					employee.setPassword(null);
					employee.setViewPass(null);
					timeStatus = utilService.evaluateSessionTime(tokenModel.getDateTime(), utilService.getCurrentDateAndTime());
					//				System.out.println("UserServiceImpl,getAuthentication,timeStatus: "+timeStatus);
					String token = utilService.createToken();
					Date date = utilService.getCurrentDateAndTime();
					tokenModel.setToken(token);
					tokenModel.setDateTime(date);
					tokenModel = userDAO.saveOrUpdateToken(tokenModel);
					employee.setToken(tokenModel.getToken());
				}else{
					tokenModel = new TokenModel();
					//				System.out.println("UserServiceImpl,getAuthentication,timeStatus: "+timeStatus);
					String token = utilService.createToken();
					Date date = utilService.getCurrentDateAndTime();
					tokenModel.setUserId(employee.getEmployeeId());
					tokenModel.setToken(token);
					tokenModel.setDateTime(date);
					tokenModel = userDAO.saveOrUpdateToken(tokenModel);
					employee.setToken(tokenModel.getToken());
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAuthentication: "+e.getMessage());
		}
		return employee;
	}

	@Override
	public Employee getEmployeeById(int employeeId) {
		return userDAO.getEmployeeById(employeeId);
	}

	@Override
	public Employee saveOrUpdateEmployee(Employee employee,ServletContext context) {
		Employee emp = null;
		try{
			System.out.println("User Service,saveOrUpdateEmployee, emp id: "+employee.getEmployeeId()
					+",view Pass: "+employee.getViewPass()+", getCreatedAt: "+employee.getCreatedAt());
			Timestamp timeSt=utilService.dateToTimeStamp(utilService.getCurrentDateAndTime());
			// Existing employee
			if(employee.getEmployeeId() > 0){
				int empId =   employee.getEmployeeId();
				System.out.println("UserServiceImpl,saveOrUpdateEmployee,update, emp id: "+empId);
				Employee emply = userDAO.getEmployeeById(empId);
				System.out.println("UserServiceImpl,saveOrUpdateEmployee,DB view password: "+emply.getViewPass());
				employee.setCreatedAt(emply.getCreatedAt());
				employee.setUserName(emply.getUserName());
				employee.setViewPass(emply.getViewPass());
				employee.setIsActive(emply.getIsActive());
				employee.setEmpCode(emply.getEmpCode());
				System.out.println("UserServiceImpl,saveOrUpdateEmployee,update,view Pass: "+employee.getViewPass());
				employee.setUpdatedAt(timeSt);
				emp = userDAO.saveOrUpdateEmployee(employee);

			}else{// Adding new employee
				employee.setCreatedAt(timeSt);
				employee.setIsActive(1);
				employee.setEmpCode(utilService.createEmployId(employee.getUserName()));
				employee.setViewPass(utilService.createEmployId(employee.getUserName()));
				employee.setUserName(utilService.createEmployId(employee.getUserName()));
				employee.setUpdatedAt(timeSt);
				//				System.out.println("UserServiceImpl,saveOrUpdateEmployee,New, emp id: "+employee.getEmployeeId()
				//						+",view Pass: "+employee.getViewPass());
				emp = userDAO.saveOrUpdateEmployee(employee);
				// Sending Password to email
				sendNewPasswordMail(employee, context);
			}
			//			employee.setUpdatedAt(timeSt);
			//			System.out.println("User Service,saveOrUpdateEmployee,Before saving, emp id: "+employee.getEmployeeId()
			//					+",view Pass: "+employee.getViewPass());
			//			emp = userDAO.saveOrUpdateEmployee(employee);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateEmployee: "+e.getMessage());
		}
		return emp;
	}

	/**
	 * Sending Password to email
	 * @param employee
	 * @param context
	 */
	private void sendNewPasswordMail(Employee employee,ServletContext context){
		try{
			String content =  "Please note your password: "+ employee.getViewPass();
			MailModel mailModel = mailModelFactory.createMailModel();
			mailModel.setContent(content);
			mailModel.setFrom("alind.yazata@yazataconsulting.com");
			mailModel.setSubject("Alind / Password / Employee code: "+employee.getEmpCode());
			mailModel.setTo(employee.getEmailId());
			mailModel.setEmployee(employee);
			mailModel.setHtmlFileName("resetPassword");
			utilService.sendEmail(context, mailModel);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("sendNewPasswordMail: "+e.getMessage());
		}
	}

	@Override
	public Employee addNewEmployee(Employee employee) {
		return userDAO.addNewEmployee(employee);
	}

	@Override
	public Department addNewDepartment(Department department) {
		return userDAO.addNewDepartment(department);
	}

	@Override
	public Department getDepartmentById(int departmentId) {
		return userDAO.getDepartmentById(departmentId);
	}

	@Override
	public Employee getEmployeeByToken(String token) {
		Employee employee = null;
		try{
			TokenModel tokenModel = userDAO.getTokenModelByToken(token);
			employee = userDAO.getEmployeeById(tokenModel.getUserId());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getEmployeeByToken: "+e.getMessage());
		}
		return employee;
	}

	@Override
	public List<Employee> searchEmployee(String searchKeyword) {
		List<Employee> employees = null;
		try{
			//			employees = userDAO.searchEmployee(searchKeyword.replaceAll("\\s", ""));
			employees = userDAO.searchEmployee(searchKeyword.trim());
			for(Employee em: employees){
				em.setViewPass(null);
				em.setPasswordHash(null);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchEmployee: "+e.getMessage());
		}
		return employees;
	}

	@Override
	public String resetPassword(ServletContext servletContext,int employeeId) {
		String newPass = null;
		try{
			Employee employee = userDAO.getEmployeeById(employeeId);

			String pass = generatePassword(employee.getFirstName());
			String newPassEncode = utilService.getEncodeBase64(pass);
			newPass = utilService.getDecodeBase64(newPassEncode);
			//			System.out.println("UserServiceImpl,resetPassword: "+newPass);
			employee.setViewPass(newPass);
			userDAO.saveOrUpdateEmployee(employee);
			String content =  "Please note your new password: "+ newPass;
			MailModel mailModel = mailModelFactory.createMailModel();
			mailModel.setContent(content);
			mailModel.setFrom("alind.yazata@yazataconsulting.com");
			mailModel.setSubject("Alind / Reset Password / Employee code: "+employee.getEmpCode());
			mailModel.setTo(employee.getEmailId());
			mailModel.setEmployee(employee);
			mailModel.setHtmlFileName("resetPassword");
			utilService.sendEmail(servletContext, mailModel);

			/**
			String value = " Alind Support Team" ;
			MimeMessagePreparator  preparator =  utilService.getMessagePreparator
					(employee.getEmailId(), employee.getUserName(), value, "Alind / Reset Password", 
							"Please note your new password: "+ newPass);
			JavaMailSender mailSender = utilService.getMailSender();
			mailSender.send(preparator);
			 **/
			//			System.out.println("UserServiceImpl,resetPassword, mail send to : "+employee.getEmailId());

		}catch(Exception e){
			e.printStackTrace();
			logger.error("resetPassword: "+e.getMessage());
		}
		return newPass;
	}

	/**
	 *  For setting random password generator 
	 * @param str
	 * @return
	 */
	private String generatePassword(String str){
		int n = 4;
		StringBuilder sb = new StringBuilder(n); 

		for (int i = 0; i < n; i++) { 

			// generate a random number between 
			// 0 to AlphaNumericString variable length 
			int index 
			= (int)(str.length() 
					* Math.random()); 

			// add Character one by one in end of sb 
			sb.append(str.charAt(index)); 
		} 

		return sb.toString().toLowerCase(); 
	}

	@Override
	public Employee deleteEmployee(String token,Object object) {
		Employee employee = null;
		try{
			//			System.out.println("UserServiceImpl,deleteEmployee,employeeId: "+object);
			Employee empToken = getEmployeeByToken(token);
			String strId = object.toString();
			int iEnd = strId.indexOf("}");
			String empIdStr = "employeeId=";
			String empId = strId.substring(strId.lastIndexOf("employeeId=")+empIdStr.length(), iEnd); 
			//			System.out.println("UserServiceImpl,deleteEmployee,strId: "+strId);;
			//			System.out.println("UserServiceImpl,deleteEmployee,"+empId);
			employee = userDAO.getEmployeeById(Integer.parseInt(empId));
			employee.setIsActive(0);
			employee = userDAO.saveOrUpdateEmployee(employee);

			/**
			 *  Making audit logs
			 */
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(empToken.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("Employee");
			auditJson.setId(empId);
			auditJson.setRemarks(employee.getEmpCode());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteEmployee: "+e.getMessage());
		}
		return employee;
	}

	@Override
	public EmployeeMinData getEmployeeMinData (Employee employee){
		EmployeeMinData employeeMinData = null;
		try{
			employeeMinData = employeeMinDataFactory.createEmployeeMinData();
			if(employee.getEmpCode()== null){
			}else{
				employeeMinData.setEmpCode(employee.getEmpCode());
			}
			employeeMinData.setEmployeeId(employee.getEmployeeId());
			employeeMinData.setFirstName(employee.getFirstName());
			employeeMinData.setLastName(employee.getLastName());
			employeeMinData.setFullName(employee.getFirstName(), 
					employee.getLastName());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getEmployeeMinData: "+e.getMessage());
		}
		return employeeMinData;

	}

	@Override
	public ForgetPasswordModel forgetPassword(ServletContext servletContext,ForgetPasswordModel forgetPassModel) {
		ForgetPasswordModel forgetModel = null;
		try{
			System.out.println("UserserviceImpl,forgetPassword,UserName: "+forgetPassModel.getUsername()+
					", EmailId: "+forgetPassModel.getEmailId()+", empCode: "+forgetPassModel.getEmpCode());
			Employee employee = userDAO.getEmployeeByEmpCodeEmailId(forgetPassModel.getUsername(), forgetPassModel.getEmailId());
			System.out.println("UserserviceImpl,forgetPassword,employee : "+employee);
			if(employee != null){
				int otp = utilService.generateOTP();
				ForgetPasswordModel forgetPasswordModel = forgetPasswordFactory.createForgetPasswordModel();
				forgetPasswordModel.setEmployeeId(employee.getEmployeeId());
				forgetPasswordModel.setOtp(otp);
				Date date = utilService.getCurrentDateAndTime();
				Timestamp timestamp = utilService.dateToTimeStamp(date);
				forgetPasswordModel.setUpdatedAt(timestamp);
				String uuId = utilService.createToken();
				forgetPasswordModel.setUuId(uuId);
				forgetModel = userDAO.saveOrUpdateForgotPassword(forgetPasswordModel);
				//				Employee emp = forgetModel.getEmployee();
				EmployeeMinData employeeMinData = getEmployeeMinData(employee);
				forgetModel.setEmployee(null);
				forgetModel.setFullName(employee.getFullName());
				forgetModel.setEmployeeMinData(employeeMinData);
				//sales.wintergreen.com/#/reset-password?req=
				String url = forgetPassModel.getBaseUrl()+uuId;
				forgetModel.setBaseUrl(url);
				// Email

				String content =  "Please note the OTP: "+ forgetModel.getOtp();
				MailModel mailModel = mailModelFactory.createMailModel();
				mailModel.setContent(content);
				mailModel.setFrom("alind.yazata@yazataconsulting.com");
				mailModel.setSubject("Alind / Forget Password - OTP / Employee Code: "+employee.getEmpCode());
				mailModel.setTo(employee.getEmailId());
				mailModel.setForgetPasswordModel(forgetModel);
				mailModel.setHtmlFileName("forgetPassword");
				utilService.sendEmail(servletContext, mailModel);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("forgetPassword: "+e.getMessage());
		}
		return forgetModel;
	}

	/**
	 *  Forget Password Reset
	 * @param req
	 * @param otp
	 * @param newPassword
	 * @return
	 */
	@Override
	public String forgetPasswordReset(ForgetPasswordModel forgetPassModel) {
		String staus = null;
		try{
			//			System.out.println("UserServiceImpl,otp : "+forgetPassModel.getOtp()+", uuId: "+forgetPassModel.getUuId());
			ForgetPasswordModel forgetPasswordModel = userDAO.getForgetPasswordModel(forgetPassModel.getOtp(), forgetPassModel.getUuId());

			if(forgetPasswordModel != null){
				boolean otpStatus = utilService.evaluateOTPTime(forgetPasswordModel.getUpdatedAt(), utilService.getCurrentDateAndTime());
				if(otpStatus){
					Employee employee = userDAO.getEmployeeById(forgetPasswordModel.getEmployeeId());
					employee.setViewPass(forgetPassModel.getNewPassword());
					employee = userDAO.saveOrUpdateEmployee(employee);
					staus = "success";
				}
			}else{
				staus = "falied";
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("forgetPasswordReset: "+e.getMessage());
			staus = "error";
		}
		return staus;
	}

	@Override
	public String resetPersonalPassword(String token,
			ForgetPasswordModel forgetPasswordModel) {
		String staus = null;
		Employee employee = null;
		try{

			employee = getEmployeeByToken(token);
			//			System.out.println("UserServiceImpl,resetPersonalPassword,CurrentPassword: "+forgetPasswordModel.getCurrentPassword()
			//					+",new pass: "+forgetPasswordModel.getNewPassword()
			//					+" Emp: "+employee);
			if(forgetPasswordModel.getCurrentPassword().equals(employee.getViewPass())){
				employee.setViewPass(forgetPasswordModel.getNewPassword());;
				employee = userDAO.saveOrUpdateEmployee(employee);
				staus = "success";
				//				System.out.println("UserServiceImpl,resetPersonalPassword, new pass: "+employee.getViewPass());
			}else{
				staus = "Current password is not matching";
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("forgetPasswordReset: "+e.getMessage());
			staus = "error";
		}
		return staus;
	}

	@Override
	public Department deleteDepartment(String token, int departmentId) {
		Department dept = null;
		try{
			Employee empToken = getEmployeeByToken(token);
			Department department = userDAO.getDepartmentById(departmentId);
			department.setIsActive(0);
			dept = userDAO.saveOrUpdateDepartment(department);
			if(dept.getIsActive() == 0){
				/**
				 *  Making audit logs
				 */
				AuditLog auditLog = auditLogFactory.createAuditLog();
				AuditJson auditJson = auditJsonFactory.createAuditJson();
				auditLog.setEmployeeId(empToken.getEmployeeId());
				auditJson.setActionType("delete");
				auditJson.setType("Department");
				auditJson.setId(""+empToken.getEmployeeId());
				auditJson.setRemarks(empToken.getEmpCode());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteDepartment: "+e.getMessage());
		}
		
		return dept;
	}




}
