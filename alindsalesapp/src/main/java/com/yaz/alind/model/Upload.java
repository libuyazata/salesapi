package com.yaz.alind.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the uploads database table.
 * 
 */
@Entity
@Table(name="uploads")
@NamedQuery(name="Upload.findAll", query="SELECT u FROM Upload u")
public class Upload implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="upload_id")
	private int uploadId;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(name="is_active")
	private int isActive;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	@Column(name="upload_name")
	private String uploadName;

	@Lob
	@Column(name="upload_notes")
	private String uploadNotes;

	@Column(name="upload_url")
	private String uploadUrl;

	//bi-directional many-to-one association to Employee
	@OneToMany(mappedBy="upload")
	private List<Employee> employees;

	//bi-directional many-to-one association to ServiceReport
//	@ManyToOne
//	@JoinColumn(name="sr_id")
//	private ServiceReport serviceReport;

	public Upload() {
	}

	public int getUploadId() {
		return this.uploadId;
	}

	public void setUploadId(int uploadId) {
		this.uploadId = uploadId;
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

	public String getUploadName() {
		return this.uploadName;
	}

	public void setUploadName(String uploadName) {
		this.uploadName = uploadName;
	}

	public String getUploadNotes() {
		return this.uploadNotes;
	}

	public void setUploadNotes(String uploadNotes) {
		this.uploadNotes = uploadNotes;
	}

	public String getUploadUrl() {
		return this.uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	public List<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

//	public Employee addEmployee(Employee employee) {
//		getEmployees().add(employee);
//		employee.setUpload(this);
//
//		return employee;
//	}
//
//	public Employee removeEmployee(Employee employee) {
//		getEmployees().remove(employee);
//		employee.setUpload(null);
//
//		return employee;
//	}

//	public ServiceReport getServiceReport() {
//		return this.serviceReport;
//	}
//
//	public void setServiceReport(ServiceReport serviceReport) {
//		this.serviceReport = serviceReport;
//	}

}