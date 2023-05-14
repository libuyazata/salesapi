package com.yaz.alind.dao;

import java.util.List;

import com.yaz.alind.model.Department;
import com.yaz.alind.model.Employee;
import com.yaz.alind.model.ForgetPasswordModel;
import com.yaz.alind.model.TokenModel;

public interface UserDAO {

	public TokenModel saveOrUpdateToken(TokenModel tokenModel);
	public TokenModel getTokenModelByUserId(int userId);
	public TokenModel getTokenModelByToken(String token);
	public List<Employee> getAllEmployee();
	public List<Department> getAllDepartment();
	public Department saveOrUpdateDepartment(Department department);
	public Department getDepartmentById(int departmentId);
	public Employee getAuthentication(String userName,String password);
	public Employee getEmployeeById(int employeeId);
	public Employee saveOrUpdateEmployee(Employee employee);
	public Employee addNewEmployee(Employee employee);
	public Department addNewDepartment(Department department);
	public List<Employee> searchEmployee(String searchKeyword);
	public Employee getEmployeeByEmpCodeEmailId(String userName, String emailId);
	public ForgetPasswordModel saveOrUpdateForgotPassword(ForgetPasswordModel forgetPasswordModel);
	public ForgetPasswordModel getForgetPasswordModel(int otp, String uuId);
	
}
