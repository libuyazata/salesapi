package com.yaz.alind.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the call_details database table.
 * 
 */
@Entity
@Table(name="call_details")
@NamedQuery(name="CallDetail.findAll", query="SELECT c FROM CallDetail c")
public class CallDetail implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "cd_id", unique = true, nullable = false)
	private int cdId;

	@Column(name="cd_allot_no")
	private String cdAllotNo;

	@Column(name="cd_allot_date")
	private Date cdAllotDate;


	@Column(name="cd_board_division")
	private String cdBoardDivision;

	@Column(name="cd_contact_no")
	private String cdContactNo;

	@Column(name="cd_customer_name")
	private String cdCustomerName;

	@Column(name="cd_email")
	private String cdEmail;

	@Column(name="cd_gurantee_period")
	private String cdGuranteePeriod;

	@Column(name="cd_problem_details")
	private String cdProblemDetails;

	@Column(name="cd_relay_panel_details")
	private String cdRelayPanelDetails;

	@Column(name="cd_status")
	private String cdStatus;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="is_active")
	private int isActive;

	@Column(name="service_request_id")
	private String serviceRequestId;

	@Column(name="site_details")
	private String siteDetails;


	@Column(name="updated_at")
	private Timestamp updatedAt;

	@Column(name="work_ph_no")
	private String workPhNo;

	@Column(name="product_details")
	private String productDetails;
	/**
	 * New to add
	 */ 
	// Str 1 -> AMC exists 
	// String amc = 1/0 , String amcDetails

	@Transient
	private int alId ;
	/**
	 * Same as cdStatus, in db, it will be new,going on, completed etc.
	 * So these values set in cdStatus.
	 * For UI purpose, the status values changed to , new , in progress, processed etc.
	 * 
	 */
	@Transient
	private String uiStatus;

	/**
	 *  Foreign of the below values, decleared as 'String',
	 *  Due to old DB
	 */
	@Column(name="customer_id", nullable = true)
	private String customerId;

	@Column(name="board_division_id", nullable = true)
	private String boardDivisionId;

	@Column(name = "panel_id", nullable = true)
	private String panelId;

	@Column(name = "relay_id", nullable = true)
	private String relayId;

	@Column(name="product_sl_no")
	private String productSlNo;

	@Column(name="remarks")
	private String remarks;

	/**
	 *  Whether the new message Read or not.
	 *  If 0 -> Read
	 *  If 1 -> Unread
	 */
	@Column(name = "view_alert",columnDefinition="Decimal(11) default '0'")
	private int viewAlert;


	/**
	 *  Primary Observation in Employee's view
	 */
	@ManyToOne
	@JoinColumn(name="nature_job_id",insertable = false, updatable = false)
	private NatureOfJobsCallReg natureOfJobs;

	@Column(name = "nature_job_id",columnDefinition="Decimal(11) default '1'")
	private int natureJobId;
	


	/**
	 *  Ends here
	 */

	@Transient
	private int slNo;

	@Transient
	private List<EmployeeMinData> employeeList;

	@Transient 
	private List<Allot> allots;

	@Transient
	private String allotDateStr;


	public CallDetail() {
	}

	public int getCdId() {
		return this.cdId;
	}

	public void setCdId(int cdId) {
		this.cdId = cdId;
	}

	public Date getCdAllotDate() {
		return this.cdAllotDate;
	}

	public void setCdAllotDate(Date cdAllotDate) {
		this.cdAllotDate = cdAllotDate;
	}

	public String getCdAllotNo() {
		return this.cdAllotNo;
	}

	public void setCdAllotNo(String cdAllotNo) {
		this.cdAllotNo = cdAllotNo;
	}


	public String getCdBoardDivision() {
		return this.cdBoardDivision;
	}



	public void setCdBoardDivision(String cdBoardDivision) {
		this.cdBoardDivision = cdBoardDivision;
	}

	public String getCdContactNo() {
		return this.cdContactNo;
	}

	public void setCdContactNo(String cdContactNo) {
		this.cdContactNo = cdContactNo;
	}

	public String getCdCustomerName() {
		return this.cdCustomerName;
	}

	public void setCdCustomerName(String cdCustomerName) {
		this.cdCustomerName = cdCustomerName;
	}

	public String getCdEmail() {
		return this.cdEmail;
	}

	public void setCdEmail(String cdEmail) {
		this.cdEmail = cdEmail;
	}

	public String getCdGuranteePeriod() {
		return this.cdGuranteePeriod;
	}

	public void setCdGuranteePeriod(String cdGuranteePeriod) {
		this.cdGuranteePeriod = cdGuranteePeriod;
	}

	public String getCdProblemDetails() {
		return this.cdProblemDetails;
	}

	public void setCdProblemDetails(String cdProblemDetails) {
		this.cdProblemDetails = cdProblemDetails;
	}

	public String getCdRelayPanelDetails() {
		return this.cdRelayPanelDetails;
	}

	public void setCdRelayPanelDetails(String cdRelayPanelDetails) {
		this.cdRelayPanelDetails = cdRelayPanelDetails;
	}

	public String getCdStatus() {
		return this.cdStatus;
	}

	public void setCdStatus(String cdStatus) {
		this.cdStatus = cdStatus;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public int getIsActive() {
		return this.isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public String getServiceRequestId() {
		return this.serviceRequestId;
	}

	public void setServiceRequestId(String serviceRequestId) {
		this.serviceRequestId = serviceRequestId;
	}

	public String getSiteDetails() {
		return this.siteDetails;
	}

	public void setSiteDetails(String siteDetails) {
		this.siteDetails = siteDetails;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getWorkPhNo() {
		return this.workPhNo;
	}

	public void setWorkPhNo(String workPhNo) {
		this.workPhNo = workPhNo;
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

	public List<EmployeeMinData> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<EmployeeMinData> employeeList) {
		this.employeeList = employeeList;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	public String getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(String productDetails) {
		this.productDetails = productDetails;
	}

	public String getProductSlNo() {
		return productSlNo;
	}

	public void setProductSlNo(String productSlNo) {
		this.productSlNo = productSlNo;
	}

	public int getAlId() {
		return alId;
	}

	public void setAlId(int alId) {
		this.alId = alId;
	}

	public int getViewAlert() {
		return viewAlert;
	}

	public void setViewAlert(int viewAlert) {
		this.viewAlert = viewAlert;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

//	public NatureOfJobs getNatureOfJobs() {
//		return natureOfJobs;
//	}
//
//	public void setNatureOfJobs(NatureOfJobs natureOfJobs) {
//		this.natureOfJobs = natureOfJobs;
//	}
	

	public int getNatureJobId() {
		return natureJobId;
	}

	public NatureOfJobsCallReg getNatureOfJobs() {
		return natureOfJobs;
	}

	public void setNatureOfJobs(NatureOfJobsCallReg natureOfJobs) {
		this.natureOfJobs = natureOfJobs;
	}

	public void setNatureJobId(int natureJobId) {
		this.natureJobId = natureJobId;
	}
	public String getUiStatus() {
		return uiStatus;
	}

	public void setUiStatus(String uiStatus) {
		this.uiStatus = uiStatus;
	}

	public List<Allot> getAllots() {
		return allots;
	}

	public void setAllots(List<Allot> allots) {
		this.allots = allots;
	}

	public String getAllotDateStr() {
		return allotDateStr;
	}

	public void setAllotDateStr(String allotDateStr) {
		this.allotDateStr = allotDateStr;
	}


	//	@Override
	//	public int compareTo(CallDetail callDetail) {
	//		return getCreatedAt().compareTo(callDetail.getCreatedAt());
	//	}


}