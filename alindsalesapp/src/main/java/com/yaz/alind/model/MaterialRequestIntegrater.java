package com.yaz.alind.model;

import java.util.List;
/**
 *  MaterialRequest & Collection of RequestedItems
 *  
 * @author Libu Mathew
 *
 */
public class MaterialRequestIntegrater {
	
//	private int cdId;
//	private Date dueDate;
//	private String remarks;
//	private List<RequestedItems> requestedItemList ;
	
	
	
	private MaterialRequest materialRequest;
	private List<RequestedItems> requestedItemList ;
	
	public MaterialRequest getMaterialRequest() {
		return materialRequest;
	}
	public void setMaterialRequest(MaterialRequest materialRequest) {
		this.materialRequest = materialRequest;
	}
	
	public List<RequestedItems> getRequestedItemList() {
		return requestedItemList;
	}
	public void setRequestedItemList(List<RequestedItems> requestedItemList) {
		this.requestedItemList = requestedItemList;
	}
	

}
