package com.yaz.alind.ui.model;

import java.util.List;


public class CallDetailModel {

	private int slNo;

	private int cdId;

	private String cdAllotNo;

	private String cdAllotDate;

	private String cdBoardDivision;

	private String cdContactNo;

	private String cdCustomerName;

	private String cdEmail;

	private String cdGuranteePeriod;

	private String cdProblemDetails;

	private String cdRelayPanelDetails;

	private String cdStatus;

	private String createdAt;

	private int isActive;

	private String serviceRequestId;

	private String siteDetails;


	private String updatedAt;

	private String workPhNo;

	private String productDetails;
	/**
	 * New to add
	 */ 
	// Str 1 -> AMC exists 
	// String amc = 1/0 , String amcDetails

	private int alId ;
	/**
	 * Same as cdStatus, in db, it will be new,going on, completed etc.
	 * So these values set in cdStatus.
	 * For UI purpose, the status values changed to , new , in progress, processed etc.
	 * 
	 */
	private String uiStatus;

	/**
	 *  Foreign of the below values, decleared as 'String',
	 *  Due to old DB
	 */
	private String customerId;

	private String boardDivisionId;

	private String panelId;

	private String relayId;

	private String productSlNo;

	private String remarks;

	/**
	 *  Whether the new message Read or not.
	 *  If 0 -> Read
	 *  If 1 -> Unread
	 */
	private int viewAlert;

	private int natureJobId;

	/**
	 *  Primary Observation in Employee's view
	 */
	private String jobNature;

	private List<AllotModel> allots;

	public int getCdId() {
		return cdId;
	}

	public void setCdId(int cdId) {
		this.cdId = cdId;
	}

	public String getCdAllotNo() {
		return cdAllotNo;
	}

	public void setCdAllotNo(String cdAllotNo) {
		this.cdAllotNo = cdAllotNo;
	}

	public String getCdAllotDate() {
		return cdAllotDate;
	}

	public void setCdAllotDate(String cdAllotDate) {
		this.cdAllotDate = cdAllotDate;
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

	public String getCdEmail() {
		return cdEmail;
	}

	public void setCdEmail(String cdEmail) {
		this.cdEmail = cdEmail;
	}

	public String getCdGuranteePeriod() {
		return cdGuranteePeriod;
	}

	public void setCdGuranteePeriod(String cdGuranteePeriod) {
		this.cdGuranteePeriod = cdGuranteePeriod;
	}

	public String getCdProblemDetails() {
		return cdProblemDetails;
	}

	public void setCdProblemDetails(String cdProblemDetails) {
		this.cdProblemDetails = cdProblemDetails;
	}

	public String getCdRelayPanelDetails() {
		return cdRelayPanelDetails;
	}

	public void setCdRelayPanelDetails(String cdRelayPanelDetails) {
		this.cdRelayPanelDetails = cdRelayPanelDetails;
	}

	public String getCdStatus() {
		return cdStatus;
	}

	public void setCdStatus(String cdStatus) {
		this.cdStatus = cdStatus;
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

	public String getServiceRequestId() {
		return serviceRequestId;
	}

	public void setServiceRequestId(String serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}

	public String getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(String siteDetails) {
		this.siteDetails = siteDetails;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getWorkPhNo() {
		return workPhNo;
	}

	public void setWorkPhNo(String workPhNo) {
		this.workPhNo = workPhNo;
	}

	public String getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(String productDetails) {
		this.productDetails = productDetails;
	}

	public int getAlId() {
		return alId;
	}

	public void setAlId(int alId) {
		this.alId = alId;
	}

	public String getUiStatus() {
		return uiStatus;
	}

	public void setUiStatus(String uiStatus) {
		this.uiStatus = uiStatus;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBoardDivisionId() {
		return boardDivisionId;
	}

	public void setBoardDivisionId(String boardDivisionId) {
		this.boardDivisionId = boardDivisionId;
	}

	public String getPanelId() {
		return panelId;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public String getRelayId() {
		return relayId;
	}

	public void setRelayId(String relayId) {
		this.relayId = relayId;
	}

	public String getProductSlNo() {
		return productSlNo;
	}

	public void setProductSlNo(String productSlNo) {
		this.productSlNo = productSlNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getViewAlert() {
		return viewAlert;
	}

	public void setViewAlert(int viewAlert) {
		this.viewAlert = viewAlert;
	}

	public int getNatureJobId() {
		return natureJobId;
	}

	public void setNatureJobId(int natureJobId) {
		this.natureJobId = natureJobId;
	}

	public String getJobNature() {
		return jobNature;
	}

	public void setJobNature(String jobNature) {
		this.jobNature = jobNature;
	}

	public List<AllotModel> getAllots() {
		return allots;
	}

	public void setAllots(List<AllotModel> allots) {
		this.allots = allots;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}
	

}
