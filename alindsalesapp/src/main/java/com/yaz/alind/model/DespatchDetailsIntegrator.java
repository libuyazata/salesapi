package com.yaz.alind.model;

import java.util.List;

public class DespatchDetailsIntegrator {
	
	private DespatchDetails despatchDetails;
	private List<DespatchedItems> despatchedItems;
	
	public DespatchDetails getDespatchDetails() {
		return despatchDetails;
	}
	public void setDespatchDetails(DespatchDetails despatchDetails) {
		this.despatchDetails = despatchDetails;
	}
	public List<DespatchedItems> getDespatchedItems() {
		return despatchedItems;
	}
	public void setDespatchedItems(List<DespatchedItems> despatchedItems) {
		this.despatchedItems = despatchedItems;
	}
	
	

}
