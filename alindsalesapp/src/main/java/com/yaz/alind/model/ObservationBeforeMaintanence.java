package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="alind_t_observation_before_maintanence")
public class ObservationBeforeMaintanence {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "obervation_id", unique = true, nullable = false)
	private int obervationId;
	
	@Column(name = "obervation_details")
	private String obervationDetails;
	
	@Column(name = "is_active",columnDefinition="Decimal(11) default '0'")
	private int isActive;
	
	@Transient
	private int slNo;
	
	public int getObervationId() {
		return obervationId;
	}

	public void setObervationId(int obervationId) {
		this.obervationId = obervationId;
	}

	public String getObervationDetails() {
		return obervationDetails;
	}

	public void setObervationDetails(String obervationDetails) {
		this.obervationDetails = obervationDetails;
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
