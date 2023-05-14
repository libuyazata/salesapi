package com.yaz.alind.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the admin_options database table.
 * 
 */
@Entity
@Table(name="admin_options")
@NamedQuery(name="AdminOption.findAll", query="SELECT a FROM AdminOption a")
public class AdminOption implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ao_id")
	private int aoId;

	@Column(name="ao_display_name")
	private String aoDisplayName;

	@Column(name="ao_enabled")
	private int aoEnabled;

	@Lob
	@Column(name="ao_notes")
	private String aoNotes;

	@Column(name="ao_values")
	private String aoValues;

	@Column(name="ao_varname")
	private String aoVarname;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="is_active")
	private int isActive;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	public AdminOption() {
	}

	public int getAoId() {
		return this.aoId;
	}

	public void setAoId(int aoId) {
		this.aoId = aoId;
	}

	public String getAoDisplayName() {
		return this.aoDisplayName;
	}

	public void setAoDisplayName(String aoDisplayName) {
		this.aoDisplayName = aoDisplayName;
	}

	public int getAoEnabled() {
		return this.aoEnabled;
	}

	public void setAoEnabled(int aoEnabled) {
		this.aoEnabled = aoEnabled;
	}

	public String getAoNotes() {
		return this.aoNotes;
	}

	public void setAoNotes(String aoNotes) {
		this.aoNotes = aoNotes;
	}

	public String getAoValues() {
		return this.aoValues;
	}

	public void setAoValues(String aoValues) {
		this.aoValues = aoValues;
	}

	public String getAoVarname() {
		return this.aoVarname;
	}

	public void setAoVarname(String aoVarname) {
		this.aoVarname = aoVarname;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public int getIsActive() {
		return this.isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}