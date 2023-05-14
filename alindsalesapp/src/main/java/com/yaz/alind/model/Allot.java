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
 * The persistent class for the allots database table.
 * 
 */
@Entity
@Table(name="allots")

@NamedQuery(name="Allot.findAll", query="SELECT a FROM Allot a")
public class Allot implements Serializable,Comparable<Allot>  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "al_id", unique = true, nullable = false)
	private int alId;

	@Column(name="al_allot_no")
	private String alAllotNo;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="is_active")
	private int isActive;

	@Column(name="is_new")
	private int isNew;

	@Column(name="updated_at")
	private Timestamp updatedAt;
	
	@Column(name="al_emp_id")
	private int alEmpId;
	
//	@Column(name="cd_id",columnDefinition="Decimal(11) default '0'")
	@Column(name="cd_id")
	private int cdId;
	
	@ManyToOne
	@JoinColumn(name="cd_id",insertable = false, updatable = false)
	private CallDetail callDetail;
	
	/**
	 *  Status like : on going, not completed, completed
	 *  1 -> New, 2-> on going status , 3 -> completed, 4 -> pending
	 *  **/
	@Column(name = "call_status_id",columnDefinition="Decimal(11) default '2'")
	private int callStatusId ;
	
	@ManyToOne
	@JoinColumn(name="call_status_id",insertable = false, updatable = false)
	private CallStatus callStatus;
	
	@Column(name="allot_date")
	private Date allotDate;
	

	/**
	 *  Whether the new message Read or not.
	 *  If 0 -> Read
	 *  If 1 -> Unread
	 */
	@Column(name = "view_alert",columnDefinition="Decimal(11) default '0'")
	private int viewAlert;
	
	/**
	 * Same as cdStatus, in db, it will be new,going on, completed etc.
	 * So these values set in cdStatus.
	 * For UI purpose, the status values changed to , new , in progress, processed etc.
	 * 
	 */
	@Transient
	private String uiStatus;
	
	@Transient
	private List<AllottedEmployees> allottedEmployees;
	
	@Transient
	private int slNo;
	
	@Transient
	private String orginalFileName;
	@Transient
	private String filePath;
	
	public CallDetail getCallDetail() {
		return callDetail;
	}

	public void setCallDetail(CallDetail callDetail) {
		this.callDetail = callDetail;
	}

	public int getCdId() {
		return cdId;
	}

	public void setCdId(int cdId) {
		this.cdId = cdId;
	}

	public Allot() {
	}

	public int getAlId() {
		return this.alId;
	}

	public void setAlId(int alId) {
		this.alId = alId;
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

	public int getIsNew() {
		return this.isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getAlAllotNo() {
		return alAllotNo;
	}

	public void setAlAllotNo(String alAllotNo) {
		this.alAllotNo = alAllotNo;
	}

	public int getAlEmpId() {
		return alEmpId;
	}

	public void setAlEmpId(int alEmpId) {
		this.alEmpId = alEmpId;
	}

	public List<AllottedEmployees> getAllottedEmployees() {
		return allottedEmployees;
	}

	public void setAllottedEmployees(List<AllottedEmployees> allottedEmployees) {
		this.allottedEmployees = allottedEmployees;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	@Override
	public int compareTo(Allot allot) {
		return getCreatedAt().compareTo(allot.getCreatedAt());
	}

	public int getViewAlert() {
		return viewAlert;
	}

	public void setViewAlert(int viewAlert) {
		this.viewAlert = viewAlert;
	}

	public int getCallStatusId() {
		return callStatusId;
	}

	public void setCallStatusId(int callStatusId) {
		this.callStatusId = callStatusId;
	}
	
	public CallStatus getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(CallStatus callStatus) {
		this.callStatus = callStatus;
	}

	public String getUiStatus() {
		return uiStatus;
	}

	public void setUiStatus(String uiStatus) {
		this.uiStatus = uiStatus;
	}

	
	public Date getAllotDate() {
		return allotDate;
	}

	public void setAllotDate(Date allotDate) {
		this.allotDate = allotDate;
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

	
}