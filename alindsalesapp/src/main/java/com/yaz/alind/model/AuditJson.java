package com.yaz.alind.model;

/**
 * 
 * @author Libu
 * Makeing json for Audit log
 *
 */
public class AuditJson {
	
	// employee id, materialRequestId etc
	private String id;
	// material request, employee etc;
	private String type; 
	// delete, in activate etc
	private String actionType;
	
	private String remarks;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
}
