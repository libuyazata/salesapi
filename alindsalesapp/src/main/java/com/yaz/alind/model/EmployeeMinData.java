package com.yaz.alind.model;


/**
 *  For showing the minimum details of the employee in search details
 * @author dell
 *
 */
public class EmployeeMinData {

	private int employeeId;
	private String empCode;
	private String firstName;
	private String lastName;
	private String fullName;

	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFullName() {
		return this.fullName; 
	}
	public void setFullName(String firstName,String lastName) {
		this.fullName = firstName+" "+lastName;
	}


}
