package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="alind_t_last_service_request_id")
public class LatestRequestIds {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "last_id", unique = true, nullable = false)
	private int lastId;
	@Column(name = "last_service_request_id")
	private String lastServiceRequestId;
	@Column(name = "latest_material_request_id")
	private String latestMaterialRequestId;
	@Column(name = "latest_despatch_id")
	private String latestDespatchId;
	
	public int getLastId() {
		return lastId;
	}
	public void setLastId(int lastId) {
		this.lastId = lastId;
	}
	public String getLastServiceRequestId() {
		return lastServiceRequestId;
	}
	public void setLastServiceRequestId(String lastServiceRequestId) {
		this.lastServiceRequestId = lastServiceRequestId;
	}
	public String getLatestMaterialRequestId() {
		return latestMaterialRequestId;
	}
	public void setLatestMaterialRequestId(String latestMaterialRequestId) {
		this.latestMaterialRequestId = latestMaterialRequestId;
	}
	public String getLatestDespatchId() {
		return latestDespatchId;
	}
	public void setLatestDespatchId(String latestDespatchId) {
		this.latestDespatchId = latestDespatchId;
	}
	
	

}
