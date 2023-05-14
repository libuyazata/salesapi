package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  Master table
 *  Despatch status -> SENT, RECEIVED
 *
 */

@Entity
@Table(name="alind_t_despatch_status")
public class DespatchStatus {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "despatch_status_id", unique = true, nullable = false)
	private int despatchStatusId;
	@Column(name = "status")
	private String status ;
	
	public int getDespatchStatusId() {
		return despatchStatusId;
	}
	public void setDespatchStatusId(int despatchStatusId) {
		this.despatchStatusId = despatchStatusId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
