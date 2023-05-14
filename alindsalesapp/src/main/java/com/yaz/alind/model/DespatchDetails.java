package com.yaz.alind.model;

import java.sql.Timestamp;
import java.util.List;

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
@Table(name="alind_t_despatched_details")
public class DespatchDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "despatch_details_id", unique = true, nullable = false)
	private int despatchDetailsId;
	@Column(name = "material_request_id")
	private int materialRequestId;

	@ManyToOne
	@JoinColumn(name="material_request_id",insertable = false, updatable = false)
	private MaterialRequest materialRequest;

	@Column(name = "despatch_number")
	private String despatchNumber;
	@Column(name = "details")
	private String details;
	@Column(name="created_at")
	private Timestamp createdAt;
	
	@Column(name = "courier_service_id")
	private int courierServiceId;
	@ManyToOne
	@JoinColumn(name="courier_service_id",insertable = false, updatable = false)
	private CourierServiceDetails courierServiceDetails;

	@Column(name = "tracking_id")
	private String trackingId;
	@Column(name="is_active")
	private int isActive;
	
	// Despatch status -> SENT, RECEIVED
	@Column(name = "despatch_status_id")
	private int despatchStatusId;
	@ManyToOne
	@JoinColumn(name="despatch_status_id",insertable = false, updatable = false)
	private DespatchStatus despatchStatus;
	
	/**
	 *  The employee, who processed despatch items.
	 */
	@Column(name="employee_id")
	private int employeeId;
	@ManyToOne
	@JoinColumn(name="employee_id",insertable = false, updatable = false)
	private Employee employee;
	
	@Transient
	private List<DespatchedItems> despatchedItems;

	// OPEN, IN PROGRESS, COMPLETE
	@Column(name="status_info_id")
	private int  statusInfoId;
	@ManyToOne
	@JoinColumn(name="status_info_id",insertable = false, updatable = false)
	private StatusInfo statusInfo;
	@Column(name="remarks")
	private String remarks;
	@Transient
	private EmployeeMinData employeeMinData;
	@Transient
	private String returnMessage;
	
	

	public MaterialRequest getMaterialRequest() {
		return materialRequest;
	}
	public void setMaterialRequest(MaterialRequest materialRequest) {
		this.materialRequest = materialRequest;
	}
	public CourierServiceDetails getCourierServiceDetails() {
		return courierServiceDetails;
	}
	public void setCourierServiceDetails(CourierServiceDetails courierServiceDetails) {
		this.courierServiceDetails = courierServiceDetails;
	}

	public int getDespatchDetailsId() {
		return despatchDetailsId;
	}
	public void setDespatchDetailsId(int despatchDetailsId) {
		this.despatchDetailsId = despatchDetailsId;
	}
	public int getMaterialRequestId() {
		return materialRequestId;
	}
	public void setMaterialRequestId(int materialRequestId) {
		this.materialRequestId = materialRequestId;
	}
	public String getDespatchNumber() {
		return despatchNumber;
	}
	public void setDespatchNumber(String despatchNumber) {
		this.despatchNumber = despatchNumber;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public int getCourierServiceId() {
		return courierServiceId;
	}
	public void setCourierServiceId(int courierServiceId) {
		this.courierServiceId = courierServiceId;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public int getStatusInfoId() {
		return statusInfoId;
	}
	public void setStatusInfoId(int statusInfoId) {
		this.statusInfoId = statusInfoId;
	}
	public StatusInfo getStatusInfo() {
		return statusInfo;
	}
	public void setStatusInfo(StatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}
	public String getTrackingId() {
		return trackingId;
	}
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	public List<DespatchedItems> getDespatchedItems() {
		return despatchedItems;
	}
	public void setDespatchedItems(List<DespatchedItems> despatchedItems) {
		this.despatchedItems = despatchedItems;
	}
	public int getDespatchStatusId() {
		return despatchStatusId;
	}
	public void setDespatchStatusId(int despatchStatusId) {
		this.despatchStatusId = despatchStatusId;
	}
	public DespatchStatus getDespatchStatus() {
		return despatchStatus;
	}
	public void setDespatchStatus(DespatchStatus despatchStatus) {
		this.despatchStatus = despatchStatus;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

}
