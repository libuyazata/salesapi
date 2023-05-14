package com.yaz.alind.ui.model;

import java.util.List;

import com.yaz.alind.model.CallDetail;

public class CallDetailModelList {

	private List<CallDetailModel> callDetailModelList ;
	private List<CallDetail> callDetailEnityList;
	private int totalCount ;
	
	public List<CallDetailModel> getCallDetailModelList() {
		return callDetailModelList;
	}
	public void setCallDetailModelList(List<CallDetailModel> callDetailModelList) {
		this.callDetailModelList = callDetailModelList;
	}
	public List<CallDetail> getCallDetailEnityList() {
		return callDetailEnityList;
	}
	public void setCallDetailEnityList(List<CallDetail> callDetailEnityList) {
		this.callDetailEnityList = callDetailEnityList;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
