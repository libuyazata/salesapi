package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Employee types are Office,  Workers, Stores, Transpotation etc.
 */

@Entity
@Table(name="alind_t_employee_types")
public class EmployeeTypes {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "empolyee_type_id", unique = true, nullable = false)
	private int empolyeeTypeId;
	@Column(name = "employee_type_name")
	private String employeeTypeName;
	
	public String getEmployeeTypeName() {
		return employeeTypeName;
	}
	public void setEmployeeTypeName(String employeeTypeName) {
		this.employeeTypeName = employeeTypeName;
	}
	public int getEmpolyeeTypeId() {
		return empolyeeTypeId;
	}
	public void setEmpolyeeTypeId(int empolyeeTypeId) {
		this.empolyeeTypeId = empolyeeTypeId;
	}
	

}
