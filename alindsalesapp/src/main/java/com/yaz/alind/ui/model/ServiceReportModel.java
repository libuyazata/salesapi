package com.yaz.alind.ui.model;

import org.springframework.web.multipart.MultipartFile;

public class ServiceReportModel {
	
	
	private int srId;

	private String createdAt;

	private int isActive;

	private String srCallAttendDate;

	private String srCallClosedDate;

	private String srCallStatus;

	private String srNaturalOfService;

	private String srNotificationStatus;

	private String srRemarks;

	private String srReportedProblem;

	private String updatedAt;
	
	private int employeeId;
	
	private String empCode;
	
	private String firstName;
	
	private String lastName;
	
	private String fullName;
	
	private int sr_cd_id;
	
	private int cdId;

	private String cdAllotNo;

	private String cdAllotDate;

	private String cdBoardDivision;

	private String cdCustomerName;

	private int isNew;
	
	private int alEmpId;
	
	private String cdContactNo;
	
	private int alId;
	
	private String productSlNo;

	private String minutesFilePath;
	
	private String orginalFileName;
	
	private String guranteePeriod;
	
	private String relayPanelDetails;
	
	private String problemDetails;
	
	private String siteDetails;
	
	
	/**
	 *  For BOLD row view in UI
	 */
	private int viewAlert;
	
	/**
	 *  Review for Guarantee  Period By Admin
	 *  0- Default value
	 *  1- Review for Guarantee Period
	 */
	private int reviewStatus;
	
	
	private String jobNature;
	
	private String obervationDetails;
	
	private MultipartFile momFile;
	
	private int slNo;
	
	private String filePath;
	
	private String alAllotNo;
	
	private byte[] uploadedRepByte;
	
	private String fileType;

	public int getSrId() {
		return srId;
	}

	public void setSrId(int srId) {
		this.srId = srId;
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

	public String getSrCallAttendDate() {
		return srCallAttendDate;
	}

	public void setSrCallAttendDate(String srCallAttendDate) {
		this.srCallAttendDate = srCallAttendDate;
	}

	public String getSrCallClosedDate() {
		return srCallClosedDate;
	}

	public void setSrCallClosedDate(String srCallClosedDate) {
		this.srCallClosedDate = srCallClosedDate;
	}

	public String getSrCallStatus() {
		return srCallStatus;
	}

	public void setSrCallStatus(String srCallStatus) {
		this.srCallStatus = srCallStatus;
	}

	public String getSrNaturalOfService() {
		return srNaturalOfService;
	}

	public void setSrNaturalOfService(String srNaturalOfService) {
		this.srNaturalOfService = srNaturalOfService;
	}

	public String getSrNotificationStatus() {
		return srNotificationStatus;
	}

	public void setSrNotificationStatus(String srNotificationStatus) {
		this.srNotificationStatus = srNotificationStatus;
	}

	public String getSrRemarks() {
		return srRemarks;
	}

	public void setSrRemarks(String srRemarks) {
		this.srRemarks = srRemarks;
	}

	public String getSrReportedProblem() {
		return srReportedProblem;
	}

	public void setSrReportedProblem(String srReportedProblem) {
		this.srReportedProblem = srReportedProblem;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

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
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getSr_cd_id() {
		return sr_cd_id;
	}

	public void setSr_cd_id(int sr_cd_id) {
		this.sr_cd_id = sr_cd_id;
	}

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

	public String getCdCustomerName() {
		return cdCustomerName;
	}

	public void setCdCustomerName(String cdCustomerName) {
		this.cdCustomerName = cdCustomerName;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public int getAlEmpId() {
		return alEmpId;
	}

	public void setAlEmpId(int alEmpId) {
		this.alEmpId = alEmpId;
	}

	public String getCdContactNo() {
		return cdContactNo;
	}

	public void setCdContactNo(String cdContactNo) {
		this.cdContactNo = cdContactNo;
	}

	public int getAlId() {
		return alId;
	}

	public void setAlId(int alId) {
		this.alId = alId;
	}

	public String getProductSlNo() {
		return productSlNo;
	}

	public void setProductSlNo(String productSlNo) {
		this.productSlNo = productSlNo;
	}

	public String getMinutesFilePath() {
		return minutesFilePath;
	}

	public void setMinutesFilePath(String minutesFilePath) {
		this.minutesFilePath = minutesFilePath;
	}

	public String getOrginalFileName() {
		return orginalFileName;
	}

	public void setOrginalFileName(String orginalFileName) {
		this.orginalFileName = orginalFileName;
	}

	public int getViewAlert() {
		return viewAlert;
	}

	public void setViewAlert(int viewAlert) {
		this.viewAlert = viewAlert;
	}

	public int getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(int reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getJobNature() {
		return jobNature;
	}

	public void setJobNature(String jobNature) {
		this.jobNature = jobNature;
	}

	public String getObervationDetails() {
		return obervationDetails;
	}

	public void setObervationDetails(String obervationDetails) {
		this.obervationDetails = obervationDetails;
	}

	public MultipartFile getMomFile() {
		return momFile;
	}

	public void setMomFile(MultipartFile momFile) {
		this.momFile = momFile;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getAlAllotNo() {
		return alAllotNo;
	}

	public void setAlAllotNo(String alAllotNo) {
		this.alAllotNo = alAllotNo;
	}

	public byte[] getUploadedRepByte() {
		return uploadedRepByte;
	}

	public void setUploadedRepByte(byte[] uploadedRepByte) {
		this.uploadedRepByte = uploadedRepByte;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getGuranteePeriod() {
		return guranteePeriod;
	}

	public void setGuranteePeriod(String guranteePeriod) {
		this.guranteePeriod = guranteePeriod;
	}

	public String getRelayPanelDetails() {
		return relayPanelDetails;
	}

	public void setRelayPanelDetails(String relayPanelDetails) {
		this.relayPanelDetails = relayPanelDetails;
	}

	public String getProblemDetails() {
		return problemDetails;
	}

	public void setProblemDetails(String problemDetails) {
		this.problemDetails = problemDetails;
	}

	public String getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(String siteDetails) {
		this.siteDetails = siteDetails;
	}
	

}
