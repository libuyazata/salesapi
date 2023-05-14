package com.yaz.alind.service;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.web.bind.annotation.RequestBody;

import com.yaz.alind.model.Department;
import com.yaz.alind.model.Employee;
import com.yaz.alind.model.EmployeeMinData;
import com.yaz.alind.model.ForgetPasswordModel;
import com.yaz.alind.model.NatureOfJobsCallReg;
import com.yaz.alind.model.TokenModel;

public interface UserService {
	
	public TokenModel saveOrUpdateToken(TokenModel tokenModel);
	public TokenModel getTokenModelByUserId(int userId);
	public List<Employee> getAllEmployee(String searchKeyWord);
	public List<Department> getAllDepartment();
	public Department saveOrUpdateDepartment(Department department);
	public Employee getAuthentication(String userName, String password);
	public Employee getEmployeeById(int employeeId);
	public Employee saveOrUpdateEmployee(Employee employee,ServletContext context);
	public Employee addNewEmployee(Employee employee);
	public Department addNewDepartment(Department department);
	public Department getDepartmentById(int departmentId);
	public Employee getEmployeeByToken(String token);
	public List<Employee> searchEmployee(String searchKeyword);
	public String resetPassword(ServletContext servletContext,int employeeId);
	public Employee deleteEmployee(String token,Object object);
	public EmployeeMinData getEmployeeMinData (Employee employee);
	public ForgetPasswordModel forgetPassword(ServletContext servletContext,ForgetPasswordModel forgetPasswordModel);
	public String forgetPasswordReset(ForgetPasswordModel forgetPasswordModel);
	public String resetPersonalPassword(String token,ForgetPasswordModel forgetPasswordModel);
	public Department deleteDepartment(String token, int departmentId );
	
}
