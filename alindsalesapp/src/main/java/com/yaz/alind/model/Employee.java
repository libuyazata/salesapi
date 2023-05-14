package com.yaz.alind.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;


/**
 * The persistent class for the employees database table.
 * 
 */
@Entity
@Table(name="employees")
@NamedQuery(name="Employee.findAll", query="SELECT e FROM Employee e")
public class Employee implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="employee_id")
	private int employeeId;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="email_id")
	private String emailId;

	@Column(name="emp_code")
	private String empCode;

	@Column(name="first_name")
	private String firstName;

	@Column(name="forgot_pw")
	private String forgotPw;

	@Column(name="gender")
	private String gender;

	@Column(name="is_active")
	private int isActive;

	@Column(name="last_name")
	private String lastName;

	@Column(name="password_hash")
	private String passwordHash;

	@Column(name="position")
	private String position;

	@Column(name="primary_mobile_no")
	private String primaryMobileNo;

	@Column(name="salt_hash")
	private String saltHash;

	@Column(name="secondary_email_id")
	private String secondaryEmailId;

	@Column(name="secondary_mobile_no")
	private String secondaryMobileNo;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	@Column(name="user_name")
	private String userName;

	@Column(name="view_pass")
	private String viewPass;


	//bi-directional many-to-one association to Allot
	//	@JsonIgnore
	//	@OneToMany(mappedBy="employee",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	//	private List<Allot> allots;

	//bi-directional many-to-one association to Department
	@ManyToOne
	@JoinColumn(name="department_id" ,insertable = false, updatable = false)
	private Department department;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;

	@Column(name="photo")
	private String photo;

	//	@Column(name="role_id")
	//	private int roleId;

	//bi-directional many-to-one association to Upload
	//	@ManyToOne
	//	@JoinColumn(name="upload_id")
	//	private Upload upload;
	@Column(name="upload_id")
	private int uploadId;

	@Column(name="department_id")
	private int departmentId; 

	@Transient
	private String token;
	// firstName + lastName
	@Transient
	private String fullName;
	@Transient
	private int slNo;
	@Transient
	private String confirmPassword;

	@Transient
	private String password;
	@Transient
	private int roleId;

	@Formula("nullif(concat(first_name,' ',last_name),' ')") // first_name and last_name are column names
	private String fullNameEmp;


	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Employee() {
	}

	public int getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getEmpCode() {
		return this.empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getForgotPw() {
		return this.forgotPw;
	}

	public void setForgotPw(String forgotPw) {
		this.forgotPw = forgotPw;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getIsActive() {
		return this.isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPasswordHash() {
		return this.passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPrimaryMobileNo() {
		return this.primaryMobileNo;
	}

	public void setPrimaryMobileNo(String primaryMobileNo) {
		this.primaryMobileNo = primaryMobileNo;
	}

	public String getSaltHash() {
		return this.saltHash;
	}

	public void setSaltHash(String saltHash) {
		this.saltHash = saltHash;
	}

	public String getSecondaryEmailId() {
		return this.secondaryEmailId;
	}

	public void setSecondaryEmailId(String secondaryEmailId) {
		this.secondaryEmailId = secondaryEmailId;
	}

	public String getSecondaryMobileNo() {
		return this.secondaryMobileNo;
	}

	public void setSecondaryMobileNo(String secondaryMobileNo) {
		this.secondaryMobileNo = secondaryMobileNo;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getViewPass() {
		return this.viewPass;
	}

	public void setViewPass(String viewPass) {
		this.viewPass = viewPass;
	}

	//	public List<Allot> getAllots() {
	//		return this.allots;
	//	}
	//
	//	public void setAllots(List<Allot> allots) {
	//		this.allots = allots;
	//	}
	//	public Allot addAllot(Allot allot) {
	//		getAllots().add(allot);
	//		allot.setEmployee(this);
	//
	//		return allot;
	//	}

	//	public Allot removeAllot(Allot allot) {
	//		getAllots().remove(allot);
	//		allot.setEmployee(null);
	//
	//		return allot;
	//	}

	//	public Department getDepartment() {
	//		return this.department;
	//	}
	//
	//	public void setDepartment(Department department) {
	//		this.department = department;
	//	}

	//	public Role getRole() {
	//		return this.role;
	//	}
	//
	//	public void setRole(Role role) {
	//		this.role = role;
	//	}

	//	public Upload getUpload() {
	//		return this.upload;
	//	}
	//
	//	public void setUpload(Upload upload) {
	//		this.upload = upload;
	//	}
	//
	//	public List<ServiceReport> getServiceReports() {
	//		return this.serviceReports;
	//	}
	//
	//	public void setServiceReports(List<ServiceReport> serviceReports) {
	//		this.serviceReports = serviceReports;
	//	}

	//	public ServiceReport addServiceReport(ServiceReport serviceReport) {
	//		getServiceReports().add(serviceReport);
	//		serviceReport.setEmployee(this);
	//
	//		return serviceReport;
	//	}

	//	public ServiceReport removeServiceReport(ServiceReport serviceReport) {
	//		getServiceReports().remove(serviceReport);
	//		serviceReport.setEmployee(null);
	//		return serviceReport;
	//	}

	//	public int getDepartmentId() {
	//		return departmentId;
	//	}
	//
	//	public void setDepartmentId(int departmentId) {
	//		this.departmentId = departmentId;
	//	}
	//
	//	public int getRoleId() {
	//		return roleId;
	//	}

	//	public void setRoleId(int roleId) {
	//		this.roleId = roleId;
	//	}
	//
	//	public int getUploadId() {
	//		return uploadId;
	//	}

	public void setUploadId(int uploadId) {
		this.uploadId = uploadId;
	}

	//	public List<Allot> getAllots() {
	//		return allots;
	//	}
	//
	//	public void setAllots(List<Allot> allots) {
	//		this.allots = allots;
	//	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getUploadId() {
		return uploadId;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return firstName+" "+lastName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullNameEmp() {
		return fullNameEmp;
	}

	public void setFullNameEmp(String fullNameEmp) {
		this.fullNameEmp = fullNameEmp;
	}


}