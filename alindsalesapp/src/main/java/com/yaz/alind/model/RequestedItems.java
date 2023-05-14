package com.yaz.alind.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *  Item ways details
 * @author Libu Mathew
 *
 */
@Entity
@Table(name="alind_t_requested_items")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestedItems {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "requested_materials_id", unique = true, nullable = false)
	private int requestedMaterialsId;
	@Column(name = "material_stock_id")
	private int materialStockId;
	@ManyToOne
	@JoinColumn(name="material_stock_id",insertable = false, updatable = false)
	private MaterialStockInfo materialStockInfo;
	
	@Column(name = "material_request_id")
	private int materialRequestId;
	
	@Column(name = "requested_quantity")
	private int requestedQuantity;
	@Column(name="remarks")
	private String remarks;
	/**
	 *  If the requested quantity not sent, then update the remaining items
	 */
	@Column(name="balance_item_to_send",columnDefinition="Decimal(11) default '0'")
	private int balanceItemToSend;
	
	public int getRequestedMaterialsId() {
		return requestedMaterialsId;
	}
	public void setRequestedMaterialsId(int requestedMaterialsId) {
		this.requestedMaterialsId = requestedMaterialsId;
	}
	public int getMaterialStockId() {
		return materialStockId;
	}
	public void setMaterialStockId(int materialStockId) {
		this.materialStockId = materialStockId;
	}
	public int getRequestedQuantity() {
		return requestedQuantity;
	}
	public void setRequestedQuantity(int requestedQuantity) {
		this.requestedQuantity = requestedQuantity;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getMaterialRequestId() {
		return materialRequestId;
	}
	public void setMaterialRequestId(int materialRequestId) {
		this.materialRequestId = materialRequestId;
	}
	public MaterialStockInfo getMaterialStockInfo() {
		return materialStockInfo;
	}
	public void setMaterialStockInfo(MaterialStockInfo materialStockInfo) {
		this.materialStockInfo = materialStockInfo;
	}
	public int getBalanceItemToSend() {
		return balanceItemToSend;
	}
	public void setBalanceItemToSend(int balanceItemToSend) {
		this.balanceItemToSend = balanceItemToSend;
	}
	
	
}
