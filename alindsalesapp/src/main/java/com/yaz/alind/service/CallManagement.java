package com.yaz.alind.service;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.yaz.alind.model.Allot;
import com.yaz.alind.model.BoardDivisionDetails;
import com.yaz.alind.model.CallDetail;
import com.yaz.alind.model.CustomerDetails;
import com.yaz.alind.model.CustomerSiteDetails;
import com.yaz.alind.model.DashBoardVariables;
import com.yaz.alind.model.NatureOfJobs;
import com.yaz.alind.model.ObservationBeforeMaintanence;
import com.yaz.alind.model.PanelDetails;
import com.yaz.alind.model.RelayDetails;
import com.yaz.alind.model.ServiceReport;
import com.yaz.alind.model.SubmitServiceReportModel;
import com.yaz.alind.ui.model.CallDetailModel;
import com.yaz.alind.ui.model.CallDetailModelList;
import com.yaz.alind.ui.model.ServiceReportModelList;

public interface CallManagement {

	public List<CallDetail> getCallDetails(String dateFrom, String dateTo,
			String searchKeyWord,int callStatus,String gurenteePeriod);
	public CallDetailModelList getCallDetails(String dateFrom, String dateTo,String searchKeyWord,
			int callStatus,String gurenteePeriod,int pageNo,int pageCount);
	public CallDetailModelList searchCallDetails(String dateFrom, String dateTo,String searchKeyWord,
			int callStatus,String gurenteePeriod);
	public XSSFWorkbook getCallDetailsReport(ServletContext context,String dateFrom, String dateTo,
			String searchKeyWord,int callStatus,String gurenteePeriod);
	public List<CallDetail> getNonAllottedCalls();
	public int getNumberOfNonAllottedCalls();
	public List<CallDetail> getOnGoingCalls();
	public List<CallDetail> getCompletedCalls();
	public List<CallDetail> getPendingCalls();
	public DashBoardVariables getAdminDashboard();
	public List<Allot> getAllAllotDetails(int isNew);
	public CallDetail saveOrUpdateCallDetails(ServletContext servletContext,CallDetail callDetail);
	public CallDetailModel updateCallDetail(CallDetailModel callDetailModel);
	public List<ServiceReport> getServiceReportByCallStatus(String callStatus);
	public List<CallDetail> getCompletedCallsFromToDate(Date fromDate,Date toDate);
	public List<CallDetail> getAllWorkDetails();
	public List<ServiceReport> searchServiceReport(String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod,int employeeId,int statusId,ServletContext context);
	public ServiceReportModelList searchServiceReportForUI(String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod,int employeeId,int statusId
			,ServletContext context);
	public ServiceReportModelList getAllServiceReport(int pageNo,int pageCount);
	
	public ServiceReport submitServiceReport(SubmitServiceReportModel submitServiceReportModel);
	public XSSFWorkbook getServiceReportExcel(ServletContext context,String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod,int employeeId,int statusId);
	
	public List<CallDetail> searchWorkAllotDetails(String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod);
	public XSSFWorkbook getWorkDetailsExcelReport(ServletContext context,String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod);
	public ServiceReport getServiceReportById(int srId,String realPath) ;
	public ServiceReport getUploadedServiceReportBySrId(ServletContext context,int srId);
	public ServiceReport getServiceReportByAlId(int alId,ServletContext context);
	public List<RelayDetails> getAllRelayDetails();
	public int deleteRelayDetails(String token,int relayId);
	public List<PanelDetails> getAllPanelDetails();
	public int deletePanelDetails(String token,int panelId);
	public RelayDetails saveOrUpdateRelayDetails(RelayDetails relayDetails);
	public PanelDetails saveOrUpdatePanelDetails(PanelDetails panelDetails);
	public List<CallDetail> getAllCallDetails();
	public List<CallDetail> createAllotTemp();
	
	public CustomerDetails saveOrUpdateCusotmerDetails(CustomerDetails customerDetails);
	public List<CustomerDetails> getAllCustomerDetails();
	public int deleteCustomerDetails(String token,int customerId );
	public CustomerDetails getCustomerDetailsById(int customerId);
//	public CustomerDetails deleCustomerDetails(int customerId);
	
	public BoardDivisionDetails saveOrUpdateBoardDivisionDetails(BoardDivisionDetails boardDivisionDetails);
	public List<BoardDivisionDetails> getAllBoardDivisionDetails();
	public BoardDivisionDetails getBoardDivisionDetailsById(int boardDivisionId);
	public int deleteBoardDivisionDetails(String token,int boardDivisionId );
	public ServiceReport saveOrUpdateServiceReport
	(ServiceReport serviceReport,MultipartFile mintesOfMeeting,String contextPath,String token);
	
	public int saveOrUpdateAllottedEmployees(ServletContext servletContext,Object object);
	public List<Allot> getAllottedWorkDetailsByEmpId(String searchKeyword,int employeeId,String dateFrom,String dateTo);
	public XSSFWorkbook getAllottedWorkDetailsReportByEmpId(ServletContext context,String searchKeyword,
			int employeeId,String dateFrom,String dateTo);
	public Allot getAllottedWorkById(int alId);
	public ByteArrayInputStream getMinutesOfMeetingPDFReport(ServletContext servletContext, int alId);
	public CallDetail updateGuranteePeriod(Object object);
	public CallDetail deleteCallDetailsById(String token,int cdId);
	public int updateViewAlert(Object object);
	public List<CallDetail> getAllCompletedCalls();
//	public CallDetail getCallDetailByCdId(int cdId);
	public CallDetailModel getCallDetailByCdId(int cdId);
	
	public List<NatureOfJobs> getAllNatureOfJobs();
	public NatureOfJobs saveOrUpdateNatureOfJobs(NatureOfJobs natureOfJobs);
	public NatureOfJobs getNatureOfJobsById(int  natureJobId);
	public int deleteNatureOfJobs(String token,int natureJobId);
	
	public List<ObservationBeforeMaintanence> getAllObservationBeforeMaintanence();
	public ObservationBeforeMaintanence getObservationBeforeMaintanenceById(int obervationId);
	public ObservationBeforeMaintanence saveOrUpdateObervationBeforeMaintanence
	(ObservationBeforeMaintanence observationBeforeMaintanence);
	public int deleteObservationBeforeMaintanence(String token,int obervationId);
	
	public List<CustomerSiteDetails> getAllCustomerSiteDetails();
	public CustomerSiteDetails saveOrUpdateCustomerSiteDetails(CustomerSiteDetails customerSiteDetails);
	public CustomerSiteDetails getCustomerSiteDetailsById(int siteId);
	public int deleteCustomerSiteDetailsById(String token,int siteId);
	
	public List<Allot> getAllotListByCallStatusId(String token,int callStatusId);
	
	
	//Temp
	public int updateCdIdInAllot();
	public int allotEmpolyeeTemp();
	// Updating cdStatus from "processed" to "completed" 
	public int updateCallDetailsTemp();
	// updating cdId values
	public int updateAllotedEmployeesTemp();
	// Updating Service Allot table
	public int updateServiceAllotTemp();
	// Updating CallStatus in allot
	public int updateAllotCallStatusTemp();
	// Updating alId  in service report
	public int updateAlIdInServiceReportTemp();
	
	
	
	
}
