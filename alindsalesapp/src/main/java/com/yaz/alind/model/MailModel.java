package com.yaz.alind.model;

import java.util.List;
import java.util.Map;

public class MailModel {
	
    private Employee employee;
    private CallDetail callDetail;
    private ForgetPasswordModel forgetPasswordModel;
	private String from;
	private String to;
	private String subject;
	private String content;
	private String htmlFileName;
	private String signatureImagePath;
	private List<Employee> empList;
	
	private Map<String, Object> modal;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Map<String, Object> getModal() {
		return modal;
	}
	public void setModal(Map<String, Object> modal) {
		this.modal = modal;
	}
	
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public CallDetail getCallDetail() {
		return callDetail;
	}
	public void setCallDetail(CallDetail callDetail) {
		this.callDetail = callDetail;
	}
	
	public String getHtmlFileName() {
		return htmlFileName;
	}
	public void setHtmlFileName(String htmlFileName) {
		this.htmlFileName = htmlFileName;
	}
	
	
	
	public String getSignatureImagePath() {
		return signatureImagePath;
	}
	public void setSignatureImagePath(String signatureImagePath) {
		this.signatureImagePath = signatureImagePath;
	}
	@Override
    public String toString() {
        return "Mail{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
	public List<Employee> getEmpList() {
		return empList;
	}
	public void setEmpList(List<Employee> empList) {
		this.empList = empList;
	}
	public ForgetPasswordModel getForgetPasswordModel() {
		return forgetPasswordModel;
	}
	public void setForgetPasswordModel(ForgetPasswordModel forgetPasswordModel) {
		this.forgetPasswordModel = forgetPasswordModel;
	}
	
	
}
