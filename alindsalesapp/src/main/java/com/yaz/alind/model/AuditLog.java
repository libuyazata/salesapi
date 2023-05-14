package com.yaz.alind.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="alind_t_audit_log")
public class AuditLog {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "audit_id", unique = true, nullable = false)
	private int auditId;
	//Json format -> AuditJson
	@Column(name = "audit_log")
	private String auditLog;
	@Column(name="employee_id")
	private int employeeId;
	@ManyToOne
	@JoinColumn(name="employee_id",insertable = false, updatable = false)
	private Employee employee;
	@Transient
	private EmployeeMinData employeeMinData;
	@Column(name="updated_at")
	private Timestamp updatedAt;
	
	public int getAuditId() {
		return auditId;
	}
	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}
	public String getAuditLog() {
		return auditLog;
	}
	public void setAuditLog(String auditLog) {
		this.auditLog = auditLog;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
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
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
	
}
