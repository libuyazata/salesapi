package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 *  Status like : on going, not completed, completed
 * @author dell
 *
 */

@Entity
@Table(name="alind_t_call_status")
public class CallStatus {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "call_status_id", unique = true, nullable = false)
	private int callStatusId ;
	@Column(name = "status" , nullable = false)
	private String status;
	@Column(name = "description")
	private String description ;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCallStatusId() {
		return callStatusId;
	}
	public void setCallStatusId(int callStatusId) {
		this.callStatusId = callStatusId;
	}
	
	
}
