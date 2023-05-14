package com.yaz.alind.model;

/**
 *  This is only for this action "/call/submitServiceReport"
 * @author dell
 *
 */
public class SubmitServiceReportModel {
	
	private int srId;
	private String gurenteePeriod;
	
	public int getSrId() {
		return srId;
	}
	public void setSrId(int srId) {
		this.srId = srId;
	}
	public String getGurenteePeriod() {
		return gurenteePeriod;
	}
	public void setGurenteePeriod(String gurenteePeriod) {
		this.gurenteePeriod = gurenteePeriod;
	}
	
	

}
