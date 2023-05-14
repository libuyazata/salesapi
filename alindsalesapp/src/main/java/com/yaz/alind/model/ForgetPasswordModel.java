package com.yaz.alind.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *  
 * @author dell
 *
 */

@Entity
@Table(name="alind_t_forget_password")
public class ForgetPasswordModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "forget_password_id", unique = true, nullable = false)
	private int forgetPasswordId;
	@Column(name="employee_id")
	private int employeeId; 
	
	@ManyToOne
	@JoinColumn(name="employee_id",insertable = false, updatable = false)
	private Employee employee;
	
	@Column(name="uuId")
	private String uuId;
	@Column(name="otp")
	private int otp;
	@Column(name="updated_at")
	private Timestamp updatedAt;
	@Transient
	private EmployeeMinData employeeMinData;
	@Transient
	private String baseUrl;
	@Transient
	private String fullName;
	@Transient
	private String empCode;
	@Transient
	private String username;
	@Transient
	private String emailId;
	@Transient
	private String newPassword;
	@Transient
	private String currentPassword;
	
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public int getForgetPasswordId() {
		return forgetPasswordId;
	}
	public void setForgetPasswordId(int forgetPasswordId) {
		this.forgetPasswordId = forgetPasswordId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public int getOtp() {
		return otp;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getUuId() {
		return uuId;
	}
	public void setUuId(String uuId) {
		this.uuId = uuId;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public EmployeeMinData getEmployeeMinData() {
		return employeeMinData;
	}
	public void setEmployeeMinData(EmployeeMinData employeeMinData) {
		this.employeeMinData = employeeMinData;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
