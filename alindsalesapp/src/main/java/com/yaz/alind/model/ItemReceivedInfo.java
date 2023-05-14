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
 *  The send items (Despatched Items), received by whom, date etc
 * @author Libu
 *
 */

@Entity
@Table(name="alind_t_item_received_info")
public class ItemReceivedInfo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "item_received__id", unique = true, nullable = false)
	private int itemReceivedId;
	@Column(name = "despatch_details_id")
	private int despatchDetailsId;
	@ManyToOne
	@JoinColumn(name="despatch_details_id",insertable = false, updatable = false)
	private DespatchDetails despatchDetails;
	
	/**
	 *  The employee, who received the despatched items
	 */
	@Column(name="employee_id")
	private int employeeId;
	@ManyToOne
	@JoinColumn(name="employee_id",insertable = false, updatable = false)
	private Employee employee;
	
	/**
	 *  Received date
	 */
	@Column(name="received_date")
	private Timestamp receivedDate;

	public int getItemReceivedId() {
		return itemReceivedId;
	}

	public void setItemReceivedId(int itemReceivedId) {
		this.itemReceivedId = itemReceivedId;
	}

	public int getDespatchDetailsId() {
		return despatchDetailsId;
	}

	public void setDespatchDetailsId(int despatchDetailsId) {
		this.despatchDetailsId = despatchDetailsId;
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

	public Timestamp getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Timestamp receivedDate) {
		this.receivedDate = receivedDate;
	}

	public DespatchDetails getDespatchDetails() {
		return despatchDetails;
	}

	public void setDespatchDetails(DespatchDetails despatchDetails) {
		this.despatchDetails = despatchDetails;
	}
	
	

}
