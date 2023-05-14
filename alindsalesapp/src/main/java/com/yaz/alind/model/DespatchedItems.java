package com.yaz.alind.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;

/**
 *  DespatchDetials/Shipped against each Material Request
 * @author Libu Mathew
 *
 */
@Entity
@Table(name="alind_t_despatched_items")
public class DespatchedItems {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "despatch_items_id", unique = true, nullable = false)
	private int despatchItemsId;
	@Column(name = "despatch_details_id")
	private int despatchDetailsId;
	@ManyToOne
	@JoinColumn(name="despatch_details_id",insertable = false, updatable = false)
	private DespatchDetails despatchDetails;
	
	@Column(name = "material_stock_id")
	private int materialStockId;
	@ManyToOne
	@JoinColumn(name="material_stock_id",insertable = false, updatable = false)
	private MaterialStockInfo materialStockInfo;
	@Column(name = "despatch_quantity")
	private int despatchQuantity;
	/**
	 *  If the requested quantity not sent, then update the remaining items
	
	@Column(name="balance_item_to_send")
	private int balanceItemToSend;
	 */
	
	@Transient
	private List<ItemReceivedInfo> itemReceivedInfos;
	@Column(name="balance_item_to_send",columnDefinition="Decimal(11) default '0'")
	private int balanceItemToSend;
    @Transient
	private int requestedMaterialsId;
	
	public int getDespatchItemsId() {
		return despatchItemsId;
	}
	public void setDespatchItemsId(int despatchItemsId) {
		this.despatchItemsId = despatchItemsId;
	}
	public int getDespatchDetailsId() {
		return despatchDetailsId;
	}
	public void setDespatchDetailsId(int despatchDetailsId) {
		this.despatchDetailsId = despatchDetailsId;
	}
	public int getMaterialStockId() {
		return materialStockId;
	}
	public void setMaterialStockId(int materialStockId) {
		this.materialStockId = materialStockId;
	}
	public int getDespatchQuantity() {
		return despatchQuantity;
	}
	public void setDespatchQuantity(int despatchQuantity) {
		this.despatchQuantity = despatchQuantity;
	}
	public DespatchDetails getDespatchDetails() {
		return despatchDetails;
	}
	public void setDespatchDetails(DespatchDetails despatchDetails) {
		this.despatchDetails = despatchDetails;
	}
	public MaterialStockInfo getMaterialStockInfo() {
		return materialStockInfo;
	}
	public void setMaterialStockInfo(MaterialStockInfo materialStockInfo) {
		this.materialStockInfo = materialStockInfo;
	}
	public List<ItemReceivedInfo> getItemReceivedInfos() {
		return itemReceivedInfos;
	}
	public void setItemReceivedInfos(List<ItemReceivedInfo> itemReceivedInfos) {
		this.itemReceivedInfos = itemReceivedInfos;
	}
	public int getBalanceItemToSend() {
		return balanceItemToSend;
	}
	public void setBalanceItemToSend(int balanceItemToSend) {
		this.balanceItemToSend = balanceItemToSend;
	}
	public int getRequestedMaterialsId() {
		return requestedMaterialsId;
	}
	public void setRequestedMaterialsId(int requestedMaterialsId) {
		this.requestedMaterialsId = requestedMaterialsId;
	}

}
