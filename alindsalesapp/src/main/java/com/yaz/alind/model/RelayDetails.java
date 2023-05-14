package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="alind_t_relay_details")
public class RelayDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "relay_id", unique = true, nullable = false)
	private int relayId;
	
	@Column(name = "relay_name")
	private String relayName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "is_active",columnDefinition="Decimal(11) default '0'")
	private int isActive;
	
	@Transient
	private int slNo;

	public int getRelayId() {
		return relayId;
	}

	public void setRelayId(int relayId) {
		this.relayId = relayId;
	}

	public String getRelayName() {
		return relayName;
	}

	public void setRelayName(String relayName) {
		this.relayName = relayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}
	

}
