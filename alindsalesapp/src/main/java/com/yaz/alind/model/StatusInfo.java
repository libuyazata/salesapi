package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Master table - NEW, IN PROGRESS, COMPLETED
 * @author Libu Mathew
 *
 */
@Entity
@Table(name="alind_t_status_info")
public class StatusInfo {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "status_info_id", unique = true, nullable = false)
	private int statusInfoId;
	// 1-> NEW, 2-> IN PROGRESS,3-> COMPLETE, 4-> COMPLETED
	@Column(name = "status")
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStatusInfoId() {
		return statusInfoId;
	}
	public void setStatusInfoId(int statusInfoId) {
		this.statusInfoId = statusInfoId;
	}
	
}
