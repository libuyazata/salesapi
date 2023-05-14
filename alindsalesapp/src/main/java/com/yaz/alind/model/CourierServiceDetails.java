package com.yaz.alind.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *  Master table for saving details of Courier/Transpotation companies
 * @author dell
 *
 */

@Entity
@Table(name="alind_t_courier_service_details")
public class CourierServiceDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "courier_service_id", unique = true, nullable = false)
	private int courierServiceId;
	@Column(name = "courier_company_name")
	private String courierCompanyName;
	@Column(name = "web_site_url")
	private String webSiteUrl;
	@Column(name = "other_details")
	private String otherDetails;
	@Column(name = "mobile_number1")
	private String mobileNumber1;
	@Column(name = "mobile_number2")
	private String mobileNumber2;
	@Column(name="is_active")
	private int isActive;
	@Column(name="created_at")
	private Timestamp createdAt;
	@Transient
	private int slNo;
	
	public int getCourierServiceId() {
		return courierServiceId;
	}
	public void setCourierServiceId(int courierServiceId) {
		this.courierServiceId = courierServiceId;
	}
	public String getCourierCompanyName() {
		return courierCompanyName;
	}
	public void setCourierCompanyName(String courierCompanyName) {
		this.courierCompanyName = courierCompanyName;
	}
	public String getWebSiteUrl() {
		return webSiteUrl;
	}
	public void setWebSiteUrl(String webSiteUrl) {
		this.webSiteUrl = webSiteUrl;
	}
	public String getOtherDetails() {
		return otherDetails;
	}
	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}
	public String getMobileNumber1() {
		return mobileNumber1;
	}
	public void setMobileNumber1(String mobileNumber1) {
		this.mobileNumber1 = mobileNumber1;
	}
	public String getMobileNumber2() {
		return mobileNumber2;
	}
	public void setMobileNumber2(String mobileNumber2) {
		this.mobileNumber2 = mobileNumber2;
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
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
