package com.yaz.alind.dao;

import java.util.Date;
import java.util.List;

import com.yaz.alind.model.Allot;
import com.yaz.alind.model.AllottedEmployees;
import com.yaz.alind.model.BoardDivisionDetails;
import com.yaz.alind.model.CallDetail;
import com.yaz.alind.model.CustomerDetails;
import com.yaz.alind.model.CustomerSiteDetails;
import com.yaz.alind.model.Employee;
import com.yaz.alind.model.LatestRequestIds;
import com.yaz.alind.model.NatureOfJobs;
import com.yaz.alind.model.ObservationBeforeMaintanence;
import com.yaz.alind.model.PanelDetails;
import com.yaz.alind.model.RelayDetails;
import com.yaz.alind.model.ServiceAllotConnector;
import com.yaz.alind.model.ServiceFile;
import com.yaz.alind.model.ServiceReport;
import com.yaz.alind.ui.model.CallDetailModelList;
import com.yaz.alind.ui.model.ServiceReportModelList;

public interface CallManagementDAO {
	
//	public List<CallDetail> getCallDetails(int isActive);
	public List<CallDetail> getCallDetails(Date dateFrom, Date dateTo,String searchKeyWord
			,String callStatus,String gurenteePeriod);
	public CallDetailModelList getCallDetails(Date dateFrom, Date dateTo,String searchKeyWord
			,String callStatus,String gurenteePeriod,int pageNo,int pageCount);
	public CallDetailModelList searchCallDetails(Date dateFrom, Date dateTo,String searchKeyWord
			,String callStatus,String gurenteePeriod);
	public List<CallDetail> getNonAllottedCalls();
	public List<CallDetail> getOnGoingCalls();
	public List<CallDetail> getCompletedCalls();
	public List<CallDetail> getCompletedCallsFromToDate(Date fromDate,Date toDate);
	public List<CallDetail> getPendingCalls();
//	public List<ServiceReport> getServiceReportByCallStatus(String callStatus);
	public CallDetail getCallDetailById(int cdId);
	public List<ServiceReport> getServiceReportByCallStatus(String srCallStatus);
	public List<Allot> getAllAllotDetails(int isNew);
	public List<Allot> getEmployeeAllotmentList(int employeeId);
	public CallDetail saveOrUpdateCallDetails(CallDetail callDetail);
	public CallDetail updateCallDetails(CallDetail callDetail);
	public Allot getAlloDetailsfromAllotNumber(String allotNumber);
	public List<ServiceReport> searchServiceReport(Date dateFrom, Date dateTo,String searchKeyWord,
			String gurenteePeriod,String status);
//	public ServiceReportModelList getAllServiceReport(Date dateFrom, Date dateTo,String searchKeyWord,
//			String gurenteePeriod,String status, int pageNo,int pageCount);
	public ServiceReportModelList getAllServiceReport(int pageNo,int pageCount);
	
	public List<CallDetail> searchWorkAllotDetails(Date dateFrom, Date dateTo,String searchKeyWord,String gurenteePeriod);
	public List<AllottedEmployees> searchAllottedEmployeesById(Date dateFrom, Date dateTo,
			String searchKeyWord,int employeeId);
	public ServiceReport getServiceReportById(int srId);
	public List<ServiceReport> getServiceReportByCdId(int cdId);
	public List<RelayDetails> saveRelayDetails(List<RelayDetails> relayDetails);
	public List<PanelDetails> savePanelDetails(List<PanelDetails> panelDetails);
	public List<RelayDetails> getAllRelayDetails();
	public List<PanelDetails> getAllPanelDetails();
	public RelayDetails saveOrUpdateRelayDetails(RelayDetails relayDetails);
	public RelayDetails getRelayDetailsById(int relayId );
	public PanelDetails saveOrUpdatePanelDetails(PanelDetails panelDetails);
	public PanelDetails getPanelDetailsById(int panelId);
	
	public CustomerDetails saveOrUpdateCusotmerDetails(CustomerDetails customerDetails);
	public List<CustomerDetails> getAllCustomerDetails();
	public CustomerDetails getCustomerDetailsById(int customerId);
	
	public BoardDivisionDetails saveOrUpdateBoardDivisionDetails(BoardDivisionDetails boardDivisionDetails);
	public List<BoardDivisionDetails> getAllBoardDivisionDetails();
	public BoardDivisionDetails getBoardDivisionDetailsById(int boardDivisionId);
	public Allot allotWork(Allot allot);
	public Allot getLastAllotRecord();
	public Allot getAllotByAlId(int alId);
	public AllottedEmployees saveOrUpdateAllottedEmployees(AllottedEmployees allottedEmployees);
	public ServiceReport saveOrUpdateServiceReport(ServiceReport serviceReport);
	public List<AllottedEmployees> getAllottedWorkDetailsByEmpId(int employeeId);
	public AllottedEmployees getAllottedWorkDetailsByAllottedEmpId(int allottedEmpoyeesId);
	public Allot getAllottedWorkById(int alId);
	public List<AllottedEmployees> getAllottedWorkDetailsByalId(int alId);
	public CallDetail getLastCallDetailsRecord();
	public List<Employee> searchEmployeesByName(String firstName,String lastName,String searchKeyWord);
	public List<Employee> searchEmployeesByName(String firstName,String lastName,String searchKeyWord
			,int pageNo,int pageCount);
	public boolean isAllotNumberExists(String alAllotNo);
	public boolean isServicerequetIdExists(String serviceRequestId);
	public ServiceAllotConnector saveOrUpdateServiceAllotConnector(ServiceAllotConnector serviceAllotConnector);
	public Allot getAllotByAllotNumber(String alAllotNo);
	public List<ServiceAllotConnector> getServiceAllotList();
	public ServiceAllotConnector getServiceAllotConnectorByAlId(int alId);
	public ServiceAllotConnector getServiceAllotConnectorBySrId(int srId);
	public List<Allot> getAllotByCdId(int cdId);
	public List<CallDetail> getAllCompletedCalls();
	public List<NatureOfJobs> getAllNatureOfJobs();
	public NatureOfJobs saveOrUpdateNatureOfJobs(NatureOfJobs natureOfJobs);
	public NatureOfJobs getNatureOfJobsById(int  natureJobId);
	public int deleteAllottedEmployee(int allottedEmpoyeesId );
	public List<ObservationBeforeMaintanence> getAllObservationBeforeMaintanence();
	public ObservationBeforeMaintanence getObservationBeforeMaintanenceById(int obervationId);
	public ObservationBeforeMaintanence saveOrUpdateObervationBeforeMaintanence
	(ObservationBeforeMaintanence observationBeforeMaintanence);
	public List<CustomerSiteDetails> getAllCustomerSiteDetails();
	public CustomerSiteDetails saveOrUpdateCustomerSiteDetails(CustomerSiteDetails customerSiteDetails);
	public CustomerSiteDetails getCustomerSiteDetailsById(int siteId);
	public LatestRequestIds getLastServiceRequestId(int lastId);
	public LatestRequestIds updateLatestServiceRequestId(LatestRequestIds lastServiceRequestId);
	public Allot saveOrUpdateAllot(Allot allot);
	public ServiceReport getServiceReportByAlId(int alId);
	public ServiceFile saveServiceFile(ServiceFile serviceFile);
	public List<ServiceFile> getServiceFileByServiceId(int serviceFileId);
	
	//Test purpose
	public List<CallDetail> getAllCallDetailsTemp();
	public List<CallDetail> createAllotTemp();
	public Allot saveAllotTemp(Allot allot);
	public CallDetail saveOrUpdateTemp(CallDetail callDetail);
	public int updateCdIdInAllotTemp();
	public int allotEmpolyeeTemp();

	
	

}
