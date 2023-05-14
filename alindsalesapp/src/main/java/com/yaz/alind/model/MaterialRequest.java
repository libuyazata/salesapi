package com.yaz.alind.model;

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
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * MaterialRequest, requested by employee , customer/site name etc
 * @author Libu Mathew
 *
 */
@Entity
@Table(name="alind_t_material_request")
public class MaterialRequest {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "material_request_id", unique = true, nullable = false)
	private int materialRequestId;
	@Column(name = "cd_id")
	private int cdId;
	
	@ManyToOne
	@JoinColumn(name="cd_id",insertable = false, updatable = false)
	private CallDetail callDetail;
	
	@Column(name="metrial_request_number")
	private String metrialRequestNumber;
	@Column(name="updated_at")
	private Timestamp updatedAt;
	@Column(name="due_date")
	private Date dueDate;
	@Column(name="remarks")
	private String remarks;
	@Column(name="employee_id")
	private int employeeId;
	@ManyToOne
	@JoinColumn(name="employee_id",insertable = false, updatable = false)
	private Employee employee;
	// active-1 or cancelled - 0
	@Column(name="is_active")
	private int isActive;
	// 1-> NEW, 2-> IN PROGRESS,3-> COMPLETE, 4-> COMPLETED
	@Column(name="status_info_id")
	private int  statusInfoId;
	@ManyToOne
	@JoinColumn(name="status_info_id",insertable = false, updatable = false)
	private StatusInfo statusInfo;
	/**
	 *  Whether the new message Read or not.
	 *  If 0 -> Read
	 *  If 1 -> Unread
	 */
	@Column(name = "view_alert")
	private int viewAlert;
	// The item receiver
	@Column(name = "name")
	private String name;
	@Column(name = "address")
	private String address;
	@Column(name = "state")
	private String state;
	@Column(name = "country")
	private String country;
	@Column(name = "pincode")
	private String pincode;
	@Column(name = "phoneNumber1")
	private String phoneNumber1;
	@Column(name = "phoneNumber2")
	private String phoneNumber2;
	
	
	@Transient
	private EmployeeMinData employeeMinData;
	@Transient
	private List<RequestedItems> requestedItems;
	@Transient
	private List<DespatchDetails> despatchDetails;
	@Transient
	private String returnMessage;
	@Transient
	private int shippingStatus; // 0-> Not completed, 1-> completed
	
	public int getMaterialRequestId() {
		return materialRequestId;
	}
	public void setMaterialRequestId(int materialRequestId) {
		this.materialRequestId = materialRequestId;
	}
	
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp createdAt) {
		this.updatedAt = createdAt;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public int getCdId() {
		return cdId;
	}
	public void setCdId(int cdId) {
		this.cdId = cdId;
	}
	public CallDetail getCallDetail() {
		return callDetail;
	}
	public void setCallDetail(CallDetail callDetail) {
		this.callDetail = callDetail;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
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
	public String getMetrialRequestNumber() {
		return metrialRequestNumber;
	}
	public void setMetrialRequestNumber(String metrialRequestNumber) {
		this.metrialRequestNumber = metrialRequestNumber;
	}
	public int getViewAlert() {
		return viewAlert;
	}
	public void setViewAlert(int viewAlert) {
		this.viewAlert = viewAlert;
	}
	public EmployeeMinData getEmployeeMinData() {
		return employeeMinData;
	}
	public void setEmployeeMinData(EmployeeMinData employeeMinData) {
		this.employeeMinData = employeeMinData;
	}
	public List<RequestedItems> getRequestedItems() {
		return requestedItems;
	}
	public void setRequestedItems(List<RequestedItems> requestedItems) {
		this.requestedItems = requestedItems;
	}
	public List<DespatchDetails> getDespatchDetails() {
		return despatchDetails;
	}
	public void setDespatchDetails(List<DespatchDetails> despatchDetails) {
		this.despatchDetails = despatchDetails;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhoneNumber1() {
		return phoneNumber1;
	}
	public void setPhoneNumber1(String phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}
	public String getPhoneNumber2() {
		return phoneNumber2;
	}
	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getShippingStatus() {
		return shippingStatus;
	}
	public void setShippingStatus(int shippingStatus) {
		this.shippingStatus = shippingStatus;
	}
	
	
}
