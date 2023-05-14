package com.yaz.alind.model;


import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author dell
 *
 */

@Entity
@Table(name="alind_t_material_category")
public class MaterialCategory {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "material_category_id", unique = true, nullable = false)
	private int materialCategoryId;
	@Column(name="material_category")
	private String materialCategory;
	@Column(name="description")
	private String description;
	@Column(name="updated_at")
	private Date updatedAt;
	@Column(name="is_active")
	private int isActive;
	@Transient
	private int slNo;
	
	public int getMaterialCategoryId() {
		return materialCategoryId;
	}
	public void setMaterialCategoryId(int materialCategoryId) {
		this.materialCategoryId = materialCategoryId;
	}
	public String getMaterialCategory() {
		return materialCategory;
	}
	public void setMaterialCategory(String materialCategory) {
		this.materialCategory = materialCategory;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public int getSlNo() {
		return slNo;
	}
	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
}
