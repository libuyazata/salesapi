package com.yaz.alind.ui.model;

import java.util.List;



public class AllotModel {
	
	private int alId;

	private String alAllotNo;

	private String createdAt;

	private int isActive;

	private int isNew;

	private String updatedAt;
	
	private int alEmpId;
	
	private int cdId;
	
	private String cdBoardDivision;

	private String cdContactNo;

	private String cdCustomerName;
	
	/**
	 *  Status like : on going, not completed, completed
	 *  1 -> New, 2-> on going status , 3 -> completed, 4 -> pending
	 *  **/
	private int callStatusId ;
	

	private String callStatus;
	
//	private String callDescription;
	
	private String allotDate;
	

	/**
	 *  Whether the new message Read or not.
	 *  If 0 -> Read
	 *  If 1 -> Unread
	 */
	private int viewAlert;
	
	/**
	 * Same as cdStatus, in db, it will be new,going on, completed etc.
	 * So these values set in cdStatus.
	 * For UI purpose, the status values changed to , new , in progress, processed etc.
	 * 
	 */
	private String uiStatus;
	
	
	private int slNo;
	
	private String orginalFileName;
	
	private String filePath;
	
	private List<AllottedEmployeesModel> allottedEmployees;

	public int getAlId() {
		return alId;
	}

	public void setAlId(int alId) {
		this.alId = alId;
	}

	public String getAlAllotNo() {
		return alAllotNo;
	}

	public void setAlAllotNo(String alAllotNo) {
		this.alAllotNo = alAllotNo;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getAlEmpId() {
		return alEmpId;
	}

	public void setAlEmpId(int alEmpId) {
		this.alEmpId = alEmpId;
	}

	public int getCdId() {
		return cdId;
	}

	public void setCdId(int cdId) {
		this.cdId = cdId;
	}

	public String getCdBoardDivision() {
		return cdBoardDivision;
	}

	public void setCdBoardDivision(String cdBoardDivision) {
		this.cdBoardDivision = cdBoardDivision;
	}

	public String getCdContactNo() {
		return cdContactNo;
	}

	public void setCdContactNo(String cdContactNo) {
		this.cdContactNo = cdContactNo;
	}

	public String getCdCustomerName() {
		return cdCustomerName;
	}

	public void setCdCustomerName(String cdCustomerName) {
		this.cdCustomerName = cdCustomerName;
	}

	public int getCallStatusId() {
		return callStatusId;
	}

	public void setCallStatusId(int callStatusId) {
		this.callStatusId = callStatusId;
	}

	public String getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}


	public String getAllotDate() {
		return allotDate;
	}

	public void setAllotDate(String allotDate) {
		this.allotDate = allotDate;
	}

	public int getViewAlert() {
		return viewAlert;
	}

	public void setViewAlert(int viewAlert) {
		this.viewAlert = viewAlert;
	}

	public String getUiStatus() {
		return uiStatus;
	}

	public void setUiStatus(String uiStatus) {
		this.uiStatus = uiStatus;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	public String getOrginalFileName() {
		return orginalFileName;
	}

	public void setOrginalFileName(String orginalFileName) {
		this.orginalFileName = orginalFileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<AllottedEmployeesModel> getAllottedEmployees() {
		return allottedEmployees;
	}

	public void setAllottedEmployees(List<AllottedEmployeesModel> allottedEmployees) {
		this.allottedEmployees = allottedEmployees;
	}

}
