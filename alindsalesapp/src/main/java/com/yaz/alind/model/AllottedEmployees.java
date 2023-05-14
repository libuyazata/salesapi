package com.yaz.alind.model;

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
@Table(name="alind_t_allotted_employees")
public class AllottedEmployees {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "allotted_empoyees_id", unique = true, nullable = false)
	private int allottedEmpoyeesId;
	
	@Column(name="al_id")
	private int alId;
	
	@ManyToOne
	@JoinColumn(name="al_id",insertable = false, updatable = false)
	private Allot allot;
	
	@Column(name="employee_id")
	private int employeeId;
	
	@ManyToOne
	@JoinColumn(name="employee_id",insertable = false, updatable = false)
	private Employee employee;
	
	@Column(name = "cd_id",columnDefinition="Decimal(11) default '0'")
	private int cdId;
	
	@Transient
	private EmployeeMinData employeeMinData;
	
	public int getAllottedEmpoyeesId() {
		return allottedEmpoyeesId;
	}

	public void setAllottedEmpoyeesId(int allottedEmpoyeesId) {
		this.allottedEmpoyeesId = allottedEmpoyeesId;
	}

	public int getCdId() {
		return cdId;
	}

	public void setCdId(int cdId) {
		this.cdId = cdId;
	}

	public int getAllotedEmpoyeesId() {
		return allottedEmpoyeesId;
	}

	public void setAllotedEmpoyeesId(int allotedEmpoyeesId) {
		this.allottedEmpoyeesId = allotedEmpoyeesId;
	}

	public int getAlId() {
		return alId;
	}

	public void setAlId(int alId) {
		this.alId = alId;
	}

	public Allot getAllot() {
		return allot;
	}

	public void setAllot(Allot allot) {
		this.allot = allot;
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

}
