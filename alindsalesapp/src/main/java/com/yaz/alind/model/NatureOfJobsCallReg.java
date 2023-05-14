package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *  Its for the Call Registration page
 * @author dell
 *
 */

@Entity
@Table(name="alind_t_nature_of_jobs")
public class NatureOfJobsCallReg {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "nature_job_id", unique = true, nullable = false)
	private int natureJobId;
	@Column(name = "job_nature")
	private String jobNature;
	@Column(name = "description")
	private String description;
	@Column(name = "is_active",columnDefinition="Decimal(11) default '0'")
	private int isActive;
	@Transient
	private int slNo;
	
	
	public int getNatureJobId() {
		return natureJobId;
	}
	public void setNatureJobId(int natureJobId) {
		this.natureJobId = natureJobId;
	}
	public String getJobNature() {
		return jobNature;
	}
	public void setJobNature(String jobNature) {
		this.jobNature = jobNature;
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
