package com.yaz.alind.ui.model;

import java.util.List;

import com.yaz.alind.model.ServiceReport;

public class ServiceReportModelList {
	
	private List<ServiceReport> serviceReportList;
	private List<ServiceReportModel> serviceReportModels;
	private int totalCount ;
	
	public List<ServiceReport> getServiceReportList() {
		return serviceReportList;
	}
	public void setServiceReportList(List<ServiceReport> serviceReportList) {
		this.serviceReportList = serviceReportList;
	}
	public List<ServiceReportModel> getServiceReportModels() {
		return serviceReportModels;
	}
	public void setServiceReportModels(List<ServiceReportModel> serviceReportModels) {
		this.serviceReportModels = serviceReportModels;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	

}
