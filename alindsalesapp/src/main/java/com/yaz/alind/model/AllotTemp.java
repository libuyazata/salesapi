package com.yaz.alind.model;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
/**
 * 
 * @author Libu
 * This model only for allotment to bind the UI values
 *
 */
public class AllotTemp {
	
	// same as cdId
	private int callMngtAllotId;
	private List<EmployeeAllotTemp> employees;
	private String mobileNumber;
	private String allotDate;
	
	public int getCallMngtAllotId() {
		return callMngtAllotId;
	}
	public void setCallMngtAllotId(int callMngtAllotId) {
		this.callMngtAllotId = callMngtAllotId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public List<EmployeeAllotTemp> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmployeeAllotTemp> employees) {
		this.employees = employees;
	}
	public String getAllotDate() {
		return allotDate;
	}
	public void setAllotDate(String allotDate) {
		this.allotDate = allotDate;
	}
	

}
