package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *  Establishing the connection between ServiceReport & Allot table
 * @author dell
 *
 */

@Entity
@Table(name="alint_t_service_allot_connector")
public class ServiceAllotConnector {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "service_allot_id", unique = true, nullable = false)
	private int serviceAllotId;
	
	@Column(name = "sr_id")
	private int srId;
	
	@Column(name = "al_id")
	private int alId;
	
	@ManyToOne
	@JoinColumn(name="al_id",insertable = false, updatable = false)
	private Allot allot;
	
	@ManyToOne
	@JoinColumn(name="sr_id",insertable = false, updatable = false)
	private ServiceReport serviceReport;
	

	public int getServiceAllotId() {
		return serviceAllotId;
	}

	public void setServiceAllotId(int serviceAllotId) {
		this.serviceAllotId = serviceAllotId;
	}

	public int getSrId() {
		return srId;
	}

	public void setSrId(int srId) {
		this.srId = srId;
	}

	public int getAlId() {
		return alId;
	}

	public void setAlId(int alId) {
		this.alId = alId;
	}

	public ServiceReport getServiceReport() {
		return serviceReport;
	}

	public void setServiceReport(ServiceReport serviceReport) {
		this.serviceReport = serviceReport;
	}

	public Allot getAllot() {
		return allot;
	}

	public void setAllot(Allot allot) {
		this.allot = allot;
	}
	

}
