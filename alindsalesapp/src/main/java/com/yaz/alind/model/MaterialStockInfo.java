package com.yaz.alind.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/* 
 *  Master table, Inventory details
 * 
 */

@Entity
@Table(name="alind_t_material_stock_info")
public class MaterialStockInfo {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "material_stock_id", unique = true, nullable = false)
	private int materialStockId;
//	@Column(name="material_name")
//	private String materialName;
	@Column(name="material_type")
	private String materialType;
	@Column(name="no_of_stocks")
	private int noOfStocks;
	@Column(name="description")
	private String description;
	@Column(name="updated_at")
	private Timestamp updatedAt;
	@Column(name="is_active")
	private int isActive;
//	@Column(name = "material_category_id",columnDefinition="Decimal(11) default '0'")
	@Column(name = "material_category_id",nullable = false)
	private int materialCategoryId;
	
	@ManyToOne
	@JoinColumn(name="material_category_id",insertable = false, updatable = false)
	private MaterialCategory materialCategory;
	
	@Transient
	private int slNo;
	
	
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public int getMaterialStockId() {
		return materialStockId;
	}
	public void setMaterialStockId(int materialStockId) {
		this.materialStockId = materialStockId;
	}
	public int getNoOfStocks() {
		return noOfStocks;
	}
	public void setNoOfStocks(int noOfStocks) {
		this.noOfStocks = noOfStocks;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public int getSlNo() {
		return slNo;
	}
	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}
	public MaterialCategory getMaterialCategory() {
		return materialCategory;
	}
	public void setMaterialCategory(MaterialCategory materialCategory) {
		this.materialCategory = materialCategory;
	}
	public int getMaterialCategoryId() {
		return materialCategoryId;
	}
	public void setMaterialCategoryId(int materialCategoryId) {
		this.materialCategoryId = materialCategoryId;
	}
	
}
