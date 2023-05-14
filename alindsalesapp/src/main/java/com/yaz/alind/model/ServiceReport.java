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

import org.springframework.web.multipart.MultipartFile;


/**
 * The persistent class for the service_reports database table.
 * 
 */
@Entity
@Table(name="service_reports")
@NamedQuery(name="ServiceReport.findAll", query="SELECT s FROM ServiceReport s")
public class ServiceReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "sr_id", unique = true, nullable = false)
	private int srId;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="is_active")
	private int isActive;

	@Column(name="sr_call_attend_date")
	private Date srCallAttendDate;

	@Column(name="sr_call_closed_date")
	private Date srCallClosedDate;

	@Column(name="sr_call_status")
	private String srCallStatus;

	@Column(name="sr_natural_of_service")
	private String srNaturalOfService;

	@Column(name="sr_notification_status")
	private String srNotificationStatus;

	@Column(name="sr_remarks")
	private String srRemarks;

	@Column(name="sr_reported_problem")
	private String srReportedProblem;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	@Column(name="sr_cd_id")
	private int sr_cd_id;
	
	@Column(name="sr_employee_id")
	private int employeeId;
	
	
	/**
     *  The employee, who submit the report
     */

	//bi-directional many-to-one association to Employee
	/**
	 *  The employee who submit the Service Report
	 */
	@ManyToOne
	@JoinColumn(name="sr_employee_id",insertable = false, updatable = false)
	private Employee employee;
	

	//bi-directional many-to-one association to CallDetail
	@ManyToOne
	@JoinColumn(name="sr_cd_id",insertable = false, updatable = false)
	private CallDetail callDetail;
	
	@ManyToOne
	@JoinColumn(name="al_id",insertable = false, updatable = false)
	private Allot allot;
	
	public Allot getAllot() {
		return allot;
	}

	public void setAllot(Allot allot) {
		this.allot = allot;
	}

	@Column(name="product_sl_no")
	             //productSlNo
	private String productSlNo;

	@Column(name="minutes_file_path")
	private String minutesFilePath;
	@Column(name="orginal_file_name")
	private String orginalFileName;
//	@Column(name = "observation_details")
//	private String observationDetails;
	
	/**
	 *  For BOLD row view in UI
	 */
	@Column(name = "view_alert",columnDefinition="Decimal(11) default '0'")
	private int viewAlert;
	
	/**
	 *  Review for Guarantee  Period By Admin
	 *  0- Default value
	 *  1- Review for Guarantee Period
	 */
	@Column(name = "review_status",columnDefinition="Decimal(11) default '0'")
	private int reviewStatus;
	
	@Column(name = "al_id",columnDefinition="Decimal(11) default '0'")
	private int alId;
	@Column(name = "job_nature")
	private String jobNature;
	@Column(name = "observation_details")
	private String obervationDetails;
	
	@Transient
	private MultipartFile momFile;
	@Transient
	private int slNo;
	@Transient
	private String filePath;
	@Transient
	private String alAllotNo;
	@Transient
	private byte[] uploadedRepByte;
	@Transient
	private String fileType;
	@Transient
	private List<EmployeeMinData> employeeMinDataList;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}


	public ServiceReport() {
	}

	public CallDetail getCallDetail() {
		return callDetail;
	}

	public void setCallDetail(CallDetail callDetail) {
		this.callDetail = callDetail;
	}

	public int getSrId() {
		return this.srId;
	}

	public void setSrId(int srId) {
		this.srId = srId;
	}


	public int getIsActive() {
		return this.isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public Date getSrCallAttendDate() {
		return this.srCallAttendDate;
	}

	public void setSrCallAttendDate(Date srCallAttendDate) {
		this.srCallAttendDate = srCallAttendDate;
	}

	public Date getSrCallClosedDate() {
		return this.srCallClosedDate;
	}

	public void setSrCallClosedDate(Date srCallClosedDate) {
		this.srCallClosedDate = srCallClosedDate;
	}

	public String getSrCallStatus() {
		return this.srCallStatus;
	}

	public void setSrCallStatus(String srCallStatus) {
		this.srCallStatus = srCallStatus;
	}

	public String getSrNaturalOfService() {
		return this.srNaturalOfService;
	}

	public void setSrNaturalOfService(String srNaturalOfService) {
		this.srNaturalOfService = srNaturalOfService;
	}

	public String getSrNotificationStatus() {
		return this.srNotificationStatus;
	}

	public void setSrNotificationStatus(String srNotificationStatus) {
		this.srNotificationStatus = srNotificationStatus;
	}

	public String getSrRemarks() {
		return this.srRemarks;
	}

	public void setSrRemarks(String srRemarks) {
		this.srRemarks = srRemarks;
	}

	public String getSrReportedProblem() {
		return this.srReportedProblem;
	}

	public void setSrReportedProblem(String srReportedProblem) {
		this.srReportedProblem = srReportedProblem;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getSr_cd_id() {
		return sr_cd_id;
	}

	public void setSr_cd_id(int sr_cd_id) {
		this.sr_cd_id = sr_cd_id;
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

	public MultipartFile getMomFile() {
		return momFile;
	}

	public void setMomFile(MultipartFile momFile) {
		this.momFile = momFile;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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


	public int getViewAlert() {
		return viewAlert;
	}

	public void setViewAlert(int viewAlert) {
		this.viewAlert = viewAlert;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(int reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

//	public String getObservationDetails() {
//		return observationDetails;
//	}
//
//	public void setObservationDetails(String observationDetails) {
//		this.observationDetails = observationDetails;
//	}

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

	public List<EmployeeMinData> getEmployeeMinDataList() {
		return employeeMinDataList;
	}

	public void setEmployeeMinDataList(List<EmployeeMinData> employeeMinDataList) {
		this.employeeMinDataList = employeeMinDataList;
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
	
	
}