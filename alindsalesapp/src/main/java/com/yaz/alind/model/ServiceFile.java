package com.yaz.alind.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  MOM file upload details
 * @author dell
 *
 */

@Entity
@Table(name="alind_t_service_file")
public class ServiceFile {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "service_file_id", unique = true, nullable = false)
	private int serviceFileId;
	@Column(name = "sr_id", nullable = false)
	private int srId;
	@Column(name="file_name")
	private String fileName;
	@Column(name="orginal_file_name")
	private String orginalFileName;
	// jpg, pdf or any
	@Column(name="file_type")
	private String fileType;
	@Column(name="remarks")
	private String remarks;
	@Column(name="created_at")
	private Timestamp createdAt;
	
	
	public int getServiceFileId() {
		return serviceFileId;
	}
	public void setServiceFileId(int serviceFileId) {
		this.serviceFileId = serviceFileId;
	}
	public int getSrId() {
		return srId;
	}
	public void setSrId(int srId) {
		this.srId = srId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOrginalFileName() {
		return orginalFileName;
	}
	public void setOrginalFileName(String orginalFileName) {
		this.orginalFileName = orginalFileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	
	
}
