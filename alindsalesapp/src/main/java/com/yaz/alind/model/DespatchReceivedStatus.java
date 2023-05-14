package com.yaz.alind.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *  Material received by the Employee
 * @author Libu
 *
 */

@Entity
@Table(name="alind_t_despatch_received_status")
public class DespatchReceivedStatus {


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "despatch_received_id", unique = true, nullable = false)
	private int despatchReceivedId;
	@Column(name="employee_id")
	private int employeeId;
	
	@ManyToOne
	@JoinColumn(name="employee_id",insertable = false, updatable = false)
	private Employee employee;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name = "despatch_items_id")
	private int despatchItemsId;
	
	@Column(name = "despatch_details_id", nullable = false)
	private int despatchDetailsId;
	
	// Despatch status -> SENT, RECEIVED
	@Column(name = "despatch_status_id")
	private int despatchStatusId;
	@ManyToOne
	@JoinColumn(name="despatch_status_id",insertable = false, updatable = false)
	private DespatchStatus despatchStatus;

	@Column(name="created_at")
	private Timestamp createdAt;

	
	public int getDespatchReceivedId() {
		return despatchReceivedId;
	}

	public void setDespatchReceivedId(int despatchReceivedId) {
		this.despatchReceivedId = despatchReceivedId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getDespatchItemsId() {
		return despatchItemsId;
	}

	public void setDespatchItemsId(int despatchItemsId) {
		this.despatchItemsId = despatchItemsId;
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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public int getDespatchDetailsId() {
		return despatchDetailsId;
	}

	public void setDespatchDetailsId(int despatchDetailsId) {
		this.despatchDetailsId = despatchDetailsId;
	}
	

}
