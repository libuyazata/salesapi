package com.yaz.alind.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.yaz.alind.dao.CallManagementDAO;
import com.yaz.alind.dao.UserDAO;
import com.yaz.alind.model.Allot;
import com.yaz.alind.model.AllotFactory;
import com.yaz.alind.model.AllottedEmployees;
import com.yaz.alind.model.AllottedEmployeesFactory;
import com.yaz.alind.model.AuditJson;
import com.yaz.alind.model.AuditJsonFactory;
import com.yaz.alind.model.AuditLog;
import com.yaz.alind.model.AuditLogFactory;
import com.yaz.alind.model.BoardDivisionDetails;
import com.yaz.alind.model.CallDetail;
import com.yaz.alind.model.CallDetailUpdateDateSortingFactory;
import com.yaz.alind.model.CustomerDetails;
import com.yaz.alind.model.CustomerSiteDetails;
import com.yaz.alind.model.DashBoardVariables;
import com.yaz.alind.model.Employee;
import com.yaz.alind.model.EmployeeMinData;
import com.yaz.alind.model.EmployeeMinDataFactory;
import com.yaz.alind.model.LatestRequestIds;
import com.yaz.alind.model.MailModel;
import com.yaz.alind.model.MailModelFactory;
import com.yaz.alind.model.NatureOfJobs;
import com.yaz.alind.model.NatureOfJobsCallReg;
import com.yaz.alind.model.ObservationBeforeMaintanence;
import com.yaz.alind.model.PanelDetails;
import com.yaz.alind.model.RelayDetails;
import com.yaz.alind.model.ServiceAllotConnector;
import com.yaz.alind.model.ServiceAllotConnectorFactory;
import com.yaz.alind.model.ServiceFile;
import com.yaz.alind.model.ServiceFileFactory;
import com.yaz.alind.model.ServiceReport;
import com.yaz.alind.model.SubmitServiceReportModel;
import com.yaz.alind.ui.model.AllotModel;
import com.yaz.alind.ui.model.AllottedEmployeesModel;
import com.yaz.alind.ui.model.CallDetailModel;
import com.yaz.alind.ui.model.CallDetailModelList;
import com.yaz.alind.ui.model.ServiceReportModel;
import com.yaz.alind.ui.model.ServiceReportModelList;
import com.yaz.security.Iconstants;

public class CallManagementImpl implements CallManagement {


	private static final Logger logger = LoggerFactory.getLogger(CallManagementImpl.class);
	@Autowired
	CallManagementDAO callManagementDAO;
	@Autowired
	UtilService utilService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	AllottedEmployeesFactory allottedEmployeesFactory;
	@Autowired
	AllotFactory allotFactory;
	@Autowired
	UserService userService;
	@Autowired
	ServiceAllotConnectorFactory serviceAllotConnectorFactory;
	@Autowired
	EmployeeMinDataFactory employeeMinDataFactory;
	@Autowired
	MailModelFactory mailModelFactory;
	@Autowired
	CallDetailUpdateDateSortingFactory callDetailUpdateDateSortingFactory;
	@Autowired
	AuditLogFactory auditLogFactory;
	@Autowired
	AuditJsonFactory auditJsonFactory;
	@Autowired
	ServiceFileFactory serviceFileFactory;


	/**
	 * 
	 * 
	 * -1= All call, 1 = On Going, 2 = Completed, 3 = Non Allotted, 4 = Pending
	 */
	@Override
	public List<CallDetail> getCallDetails(String dateFrom, String dateTo,String searchKeyWord,
			int callStatus,String gurenteePeriod) {
		List<CallDetail> updatedCallDetails = null;
		String status = null;
		//		System.out.println("Business,getCallDetails,callStatus: "+callStatus);
		try{

			switch (callStatus) {
			case 1:
				status = Iconstants.ON_GOING;
				break;
			case 2:
				status = Iconstants.COMPLETED;
				break;
			case 3:
				status = Iconstants.NON_ALLOTTED;
				break;
			case 4:
				status = Iconstants.PENDING;
				break;
			default:
				status ="";
				break;
			}
			Date frDate = utilService.getDateFromString(dateFrom);
			// Changing the format to Thu Jun 25 00:00:00 IST 2020,
			frDate = utilService.fromDateStartFromZeroHrs(frDate);

			Date toDate = utilService.getDateFromString(dateTo);
			//Changing the format to Thu Jun 25 23:59:59 IST 2020
			toDate = utilService.toDateEndToLastMin(toDate);
			//			System.out.println("CallMgt,getCallDetails,frDate: "+frDate+", toDate: "+toDate);

			List<CallDetail> callDetails = callManagementDAO.getCallDetails(frDate, 
					toDate , searchKeyWord, status,gurenteePeriod);
			//			System.out.println("Business,getCallDetails,size: "+callDetails.size());
			Map<Integer, CallDetail> map = new HashMap<>(); 
			for (CallDetail cd : callDetails) { 
				//				System.out.println("Business,getCallDetails,cdId: "+cd.getCdId());
				map.put(cd.getCdId(), cd); 
			} 
			/**
			 *  Searching First name in Employee table
			 */
			if(!searchKeyWord.isEmpty()){
				List<Employee> empList = callManagementDAO.searchEmployeesByName(searchKeyWord,searchKeyWord,searchKeyWord);
				for(int i=0;i<empList.size();i++){
					List<AllottedEmployees> alEmployeesList= callManagementDAO.getAllottedWorkDetailsByEmpId(empList.get(i).getEmployeeId());
					for(AllottedEmployees ae: alEmployeesList){
						if(!map.containsKey(ae.getCdId())){
							CallDetail callDetail = callManagementDAO.getCallDetailById(ae.getCdId());
							map.put(ae.getCdId(), callDetail);
						}
					}
				}
			}
			updatedCallDetails = new ArrayList<CallDetail>(map.values());

			/**
			 *  Sorting based on created date
			 */
			Collections.sort(updatedCallDetails, new Comparator<CallDetail>() {
				public int compare(CallDetail o1, CallDetail o2) {
					return o1.getCreatedAt().compareTo(o2.getCreatedAt());
				}
			});
			Collections.reverse(updatedCallDetails);

			for(int i=0;i<updatedCallDetails.size();i++){
				//				System.out.println("Business,getCallDetails: "+(i+1)+", cdId: "+updatedCallDetails.get(i).getCdId());
				updatedCallDetails.get(i).setSlNo(i+1);
				if(updatedCallDetails.get(i).getCdStatus().equals("on going")){
					updatedCallDetails.get(i).setUiStatus("IN PROGRESS");
				}else if(updatedCallDetails.get(i).getCdStatus().equals("pending")){
					updatedCallDetails.get(i).setUiStatus("NOT COMPLETED");
				}else if(updatedCallDetails.get(i).getCdStatus().equals("new")){
					updatedCallDetails.get(i).setUiStatus("NEW");
				}
				else{
					updatedCallDetails.get(i).setUiStatus(updatedCallDetails.get(i).getCdStatus());
				}
				//				System.out.println("Business,getCallDetails, cdId: "+updatedCallDetails.get(i).getCdId()
				//						+", Test cdId: "+callDetailsTest.get(i).getCdId());
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCallDetails: "+e.getMessage());
		}
		System.out.println("Business,getCallDetails,updatedCallDetails,size: "+updatedCallDetails.size());
		return updatedCallDetails;
	}

	/**
	 * 
	 * 
	 * -1= All call, 1 = On Going, 2 = Completed, 3 = Non Allotted, 4 = Pending
	 */
	@Override
	public CallDetailModelList searchCallDetails(String dateFrom, String dateTo,String searchKeyWord,
			int callStatus,String gurenteePeriod) {
		CallDetailModelList callDetailModelList = null;
		List<CallDetail> updatedCallDetails = null;
		String status = null;
		//		System.out.println("Business,getCallDetails,callStatus: "+callStatus);
		try{

			switch (callStatus) {
			case 1:
				status = Iconstants.ON_GOING;
				break;
			case 2:
				status = Iconstants.COMPLETED;
				break;
			case 3:
				status = Iconstants.NON_ALLOTTED;
				break;
			case 4:
				status = Iconstants.PENDING;
				break;
			default:
				status ="";
				break;
			}
			Date frDate = utilService.getDateFromString(dateFrom);
			// Changing the format to Thu Jun 25 00:00:00 IST 2020,
			frDate = utilService.fromDateStartFromZeroHrs(frDate);

			Date toDate = utilService.getDateFromString(dateTo);
			//Changing the format to Thu Jun 25 23:59:59 IST 2020
			toDate = utilService.toDateEndToLastMin(toDate);
			//			System.out.println("CallMgt,getCallDetails,frDate: "+frDate+", toDate: "+toDate);

			callDetailModelList = callManagementDAO.searchCallDetails(frDate, 
					toDate , searchKeyWord, status,gurenteePeriod);
			List<CallDetail> callDetails = callDetailModelList.getCallDetailEnityList();
			//			System.out.println("Business,getCallDetails,size: "+callDetailModelList.getTotalCount());
			Map<Integer, CallDetail> map = new HashMap<>(); 
			for (CallDetail cd : callDetails) { 
				//				System.out.println("Business,getCallDetails,cdId: "+cd.getCdId());
				map.put(cd.getCdId(), cd); 
			} 
			/**
			 *  Searching First name in Employee table
			 */
			if(!searchKeyWord.isEmpty()){
				List<Employee> empList = callManagementDAO.searchEmployeesByName(searchKeyWord,searchKeyWord,searchKeyWord);
				for(int i=0;i<empList.size();i++){
					List<AllottedEmployees> alEmployeesList= callManagementDAO.getAllottedWorkDetailsByEmpId(empList.get(i).getEmployeeId());
					for(AllottedEmployees ae: alEmployeesList){
						if(!map.containsKey(ae.getCdId())){
							CallDetail callDetail = callManagementDAO.getCallDetailById(ae.getCdId());
							map.put(ae.getCdId(), callDetail);
						}
					}
				}
			}
			updatedCallDetails = new ArrayList<CallDetail>(map.values());

			/**
			 *  Sorting based on created date
			 */
			Collections.sort(updatedCallDetails, new Comparator<CallDetail>() {
				public int compare(CallDetail o1, CallDetail o2) {
					return o1.getCreatedAt().compareTo(o2.getCreatedAt());
				}
			});
			Collections.reverse(updatedCallDetails);

			for(int i=0;i<updatedCallDetails.size();i++){
				//				System.out.println("Business,getCallDetails: "+(i+1)+", cdId: "+updatedCallDetails.get(i).getCdId());
				updatedCallDetails.get(i).setSlNo(i+1);
				if(updatedCallDetails.get(i).getCdStatus().equals("on going")){
					updatedCallDetails.get(i).setUiStatus("IN PROGRESS");
				}else if(updatedCallDetails.get(i).getCdStatus().equals("pending")){
					updatedCallDetails.get(i).setUiStatus("NOT COMPLETED");
				}else if(updatedCallDetails.get(i).getCdStatus().equals("new")){
					updatedCallDetails.get(i).setUiStatus("NEW");
				}
				else{
					updatedCallDetails.get(i).setUiStatus(updatedCallDetails.get(i).getCdStatus());
				}
				//				System.out.println("Business,getCallDetails, cdId: "+updatedCallDetails.get(i).getCdId()
				//						+", Test cdId: "+callDetailsTest.get(i).getCdId());
			}

			List<CallDetailModel> callDetailModels = new ArrayList<CallDetailModel>();
			//			for( CallDetail call : updatedCallDetails){
			//				CallDetailModel model = createCallDetailModel(call);
			//				callDetailModels.add(model);
			//			}
			for( int slNo=0;slNo < updatedCallDetails.size();slNo++){
				CallDetailModel model = createCallDetailModel(updatedCallDetails.get(slNo));
				model.setSlNo(slNo+1);
				callDetailModels.add(model);
				//				System.out.println("Business,searchCallDetails,SlNo: "+model.getSlNo());
			}
			callDetailModelList.setCallDetailEnityList(null);
			callDetailModelList.setCallDetailModelList(callDetailModels);
			// When its searching with a key word, its for page nation purpose


		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchCallDetails: "+e.getMessage());
		}
		//		System.out.println("Business,searchCallDetails,updatedCallDetails,size: "+updatedCallDetails.size());
		return callDetailModelList;
	}

	@Override
	public CallDetailModelList getCallDetails(String dateFrom, String dateTo,String searchKeyWord,
			int callStatus,String gurenteePeriod,int pageNo,int pageCount) {
		CallDetailModelList callDetailModelList = null;
		List<CallDetail> updatedCallDetails = null;
		String status = null;
		//		System.out.println("Business,getCallDetails,callStatus: "+callStatus+", pageNo: "+pageNo+
		//				", pageCount: "+pageCount);
		// searchKeyWord, whether, there is any allotted employee, then it will get CallDetails
		// If Zero, no employees are not allotted.
		int allotedEmpStatus = 0;
		try{

			switch (callStatus) {
			case 1:
				status = Iconstants.ON_GOING;
				break;
			case 2:
				status = Iconstants.COMPLETED;
				break;
			case 3:
				status = Iconstants.NON_ALLOTTED;
				break;
			case 4:
				status = Iconstants.PENDING;
				break;
			default:
				status ="";
				break;
			}
			Date frDate = utilService.getDateFromString(dateFrom);
			// Changing the format to Thu Jun 25 00:00:00 IST 2020,
			frDate = utilService.fromDateStartFromZeroHrs(frDate);

			Date toDate = utilService.getDateFromString(dateTo);
			//Changing the format to Thu Jun 25 23:59:59 IST 2020
			toDate = utilService.toDateEndToLastMin(toDate);
			//			System.out.println("CallMgt,getCallDetails,frDate: "+frDate+", toDate: "+toDate);

			//			List<CallDetail> callDetails = callManagementDAO.getCallDetails(frDate, 
			//					toDate , searchKeyWord, status,gurenteePeriod);

			callDetailModelList = callManagementDAO.getCallDetails(frDate, 
					toDate , searchKeyWord, status,gurenteePeriod,pageNo,pageCount);

			List<CallDetail> callDetails = callDetailModelList.getCallDetailEnityList();
			//			System.out.println("CallMgt,getCallDetails,callDetails size: "+callDetails.size());
			Map<Integer, CallDetail> map = new HashMap<>(); 
			for (CallDetail cd : callDetails) { 
				//				System.out.println("Business,getCallDetails,cdId: "+cd.getCdId());
				map.put(cd.getCdId(), cd); 
			} 
			/**
			 *  Searching First name in Employee table
			 * 

			//			if(!searchKeyWord.isEmpty()){
			//			System.out.println("Business,getCallDetails,before searchKeyWord, map.size: "+map.size());
			if(searchKeyWord != null){
				if(!searchKeyWord.isEmpty()){
					List<Employee> empList = callManagementDAO.searchEmployeesByName(searchKeyWord,searchKeyWord,searchKeyWord);
					//				List<Employee> empList = callManagementDAO.searchEmployeesByName(searchKeyWord,searchKeyWord,
					//						searchKeyWord, pageNo,pageCount);
					for(int i=0;i<empList.size();i++){
						List<AllottedEmployees> alEmployeesList= callManagementDAO.getAllottedWorkDetailsByEmpId(empList.get(i).getEmployeeId());
						for(AllottedEmployees ae: alEmployeesList){
							if(!map.containsKey(ae.getCdId())){
								CallDetail callDetail = callManagementDAO.getCallDetailById(ae.getCdId());
								map.put(ae.getCdId(), callDetail);
//								allotedEmpStatus = 1;
							}
						}
					}
				}//if(!searchKeyWord.isEmpty())
			}//if(searchKeyWord != null)
			System.out.println("Business,getCallDetails,map.size: "+map.size());
			 */
			updatedCallDetails = new ArrayList<CallDetail>(map.values());

			/**
			 *  Sorting based on created date
			 */
			Collections.sort(updatedCallDetails, new Comparator<CallDetail>() {
				public int compare(CallDetail o1, CallDetail o2) {
					return o1.getCreatedAt().compareTo(o2.getCreatedAt());
				}
			});
			//			Collections.reverse(updatedCallDetails);
			//			System.out.println("Business,getCallDetails, updatedCallDetails size: "+updatedCallDetails.size());
			for(int i=0;i<updatedCallDetails.size();i++){
				//				System.out.println("Business,getCallDetails: "+(i+1)+", cdId: "+updatedCallDetails.get(i).getCdId());
				updatedCallDetails.get(i).setSlNo(i+1);
				if(updatedCallDetails.get(i).getCdStatus().equals("on going")){
					updatedCallDetails.get(i).setUiStatus("IN PROGRESS");
				}else if(updatedCallDetails.get(i).getCdStatus().equals("pending")){
					updatedCallDetails.get(i).setUiStatus("NOT COMPLETED");
				}else if(updatedCallDetails.get(i).getCdStatus().equals("new")){
					updatedCallDetails.get(i).setUiStatus("NEW");
				}
				else{
					updatedCallDetails.get(i).setUiStatus(updatedCallDetails.get(i).getCdStatus());
				}
				//				System.out.println("Business,getCallDetails, cdId: "+updatedCallDetails.get(i).getCdId());
				//						+", Test cdId: "+callDetailsTest.get(i).getCdId());
			}

			List<CallDetailModel> callDetailModels = new ArrayList<CallDetailModel>();
			//			for( CallDetail call : updatedCallDetails){
			//				CallDetailModel model = createCallDetailModel(call);
			//				callDetailModels.add(model);
			//			}

			for( int slNo=0;slNo < updatedCallDetails.size();slNo++){
				CallDetailModel model = createCallDetailModel(updatedCallDetails.get(slNo));
				model.setSlNo(slNo+1);
				callDetailModels.add(model);
				//				System.out.println("Business,getCallDetails,SlNo: "+model.getSlNo());
			}

			callDetailModelList.setCallDetailEnityList(null);
			callDetailModelList.setCallDetailModelList(callDetailModels);
			// When its searching with a key word, its for page nation purpose

			if(allotedEmpStatus >0 ){
				//				if(!searchKeyWord.isEmpty()){
				callDetailModelList.setTotalCount(callDetailModels.size());
				//				}
			}
			//			System.out.println("Business,getCallDetails,getTotalCount: "+callDetailModelList.getTotalCount()
			//					+", allotedEmpStatus: "+allotedEmpStatus);
			//			System.out.println("Business,getCallDetails,getTotalCount: "+callDetailModelList.getTotalCount());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCallDetails: "+e.getMessage());
		}
		//		System.out.println("Business,getCallDetails,size: "+callDetailModelList.getCallDetailModelList().size());
		return callDetailModelList;
	}

	/**
	 *  CallDetail, Excel report
	 */

	@Override
	public XSSFWorkbook getCallDetailsReport(ServletContext context,String dateFrom, String dateTo,
			String searchKeyWord,int callStatus,String gurenteePeriod){

		XSSFWorkbook workbook = null;
		try{
			List<CallDetail> callDetails = getCallDetails(dateFrom,dateTo,searchKeyWord.trim()
					,callStatus,gurenteePeriod);

			String filePath = context.getRealPath("/WEB-INF/views/excelReport/detailedList.xlsx");
			//			System.out.println("Bussiness,getCallDetailsReport,filePath: "+filePath);
			File myFile = new File(filePath);
			FileInputStream fis = new FileInputStream(myFile);
			workbook = new XSSFWorkbook(fis);

			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow row = null;

			sheet.createFreezePane(0, 1); // Freeze 1st Row   sheet.createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow)
			int rowId = 5;
			for (int i=0; i< callDetails.size();i++) {
				List<ServiceReport> serviceReports = callManagementDAO.getServiceReportByCdId(callDetails.get(i).getCdId());
				List<Allot> allots = callManagementDAO.getAllotByCdId(callDetails.get(i).getCdId());
				int rowNo = rowId++;
				row = sheet.createRow(rowNo);
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellStyle( getCellStyle(rowNo, workbook) );
				cell0.setCellValue(i+1);

				XSSFCell cell1 = row.createCell(1);
				cell1.setCellStyle( getCellStyle(rowNo, workbook) );
				cell1.setCellValue(utilService.getTimeStampToString(callDetails.get(i).getCreatedAt()));

				XSSFCell cell2 = row.createCell(2);
				cell2.setCellStyle( getCellStyle(rowNo, workbook) );
				if(callDetails.get(i).getCdAllotNo() == null){
					cell2.setCellValue("");
				}else{
					cell2.setCellValue(callDetails.get(i).getCdAllotNo());
				}
				XSSFCell cell3 = row.createCell(3);
				// Color settings
				//				cell3.setCellStyle( rowNo % 2 == 0 ? oddRowStyle : evenRowStyle );
				cell3.setCellStyle( getCellStyle(rowNo, workbook) );
				cell3.setCellValue(callDetails.get(i).getCdCustomerName());

				XSSFCell cell4 = row.createCell(4);
				cell4.setCellStyle( getCellStyle(rowNo, workbook) );
				cell4.setCellValue(callDetails.get(i).getServiceRequestId());

				XSSFCell cell5 = row.createCell(5);
				cell5.setCellStyle( getCellStyle(rowNo, workbook) );
				cell5.setCellValue(callDetails.get(i).getCdBoardDivision());

				XSSFCell cell6 = row.createCell(6);
				cell6.setCellStyle( getCellStyle(rowNo, workbook) );
				cell6.setCellValue(callDetails.get(i).getSiteDetails());

				XSSFCell cell7 = row.createCell(7);
				cell7.setCellStyle( getCellStyle(rowNo, workbook) );
				cell7.setCellValue(callDetails.get(i).getCdGuranteePeriod());

				XSSFCell cell8 = row.createCell(8);
				cell8.setCellStyle( getCellStyle(rowNo, workbook) );
				cell8.setCellValue(callDetails.get(i).getProductDetails());

				String pdtSerNoByEmployee = "";
				String callAttendedDate = "";
				String callClosedDate = "";
				String obserVationStr = "";
				String employeeNames = "";
				String natureOfServiceUnderTaken = "";
				String remarks = "";
				for(ServiceReport sp: serviceReports){

					// Serial No, entered by employee at the service time
					pdtSerNoByEmployee = sp.getProductSlNo();
					remarks = sp.getSrRemarks();
					callAttendedDate = callAttendedDate + utilService.getDateToString(sp.getSrCallAttendDate())+ ", ";
					if(sp.getSrCallClosedDate() != null){
						callClosedDate = callClosedDate + utilService.getDateToString(sp.getSrCallClosedDate())+ ", ";
					}
					//					obserVationStr = obserVationStr + sp.getObervationDetails()+ ", ";
					//					natureOfServiceUnderTaken = sp.getSrNaturalOfService();
					if(sp.getObervationDetails()!=null){

						List<ObservationBeforeMaintanence> obs = getObservationBeforeFromJson(sp.getObervationDetails());
						for(ObservationBeforeMaintanence ob: obs){
							obserVationStr = obserVationStr + ob.getObervationDetails()+ ", ";
						}
					}

					if(sp.getSrNaturalOfService()!=null){
						if(isJson(sp.getSrNaturalOfService())){
							List<NatureOfJobs> jobs = getNatureOfJobsFromJson(sp.getSrNaturalOfService());
							for(NatureOfJobs j:jobs){
								natureOfServiceUnderTaken = natureOfServiceUnderTaken + j.getJobNature() + ", ";
							}
						}
					}

				}

				XSSFCell cell9 = row.createCell(9);
				cell9.setCellStyle( getCellStyle(rowNo, workbook) );
				cell9.setCellValue(pdtSerNoByEmployee);

				//NATURE OF WORK
				XSSFCell cell10 = row.createCell(10);
				cell10.setCellStyle( getCellStyle(rowNo, workbook) );
				cell10.setCellValue(callDetails.get(i).getNatureOfJobs().getJobNature());

				//REPORTED PROBLEM
				XSSFCell cell11 = row.createCell(11);
				cell11.setCellStyle( getCellStyle(rowNo, workbook) );
				cell11.setCellValue(callDetails.get(i).getRemarks());


				for(Allot allot: allots){
					List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(allot.getAlId());
					for(AllottedEmployees ae: allottedEmployees){
						Employee employee = userDAO.getEmployeeById(ae.getEmployeeId());
						employeeNames = employeeNames  +employee.getFirstName()+" "+employee.getLastName() + ", ";
					}
				}
				//PRIMARY Observation
				XSSFCell cell12 = row.createCell(12);
				cell12.setCellStyle( getCellStyle(rowNo, workbook) );
				cell12.setCellValue(obserVationStr);

				//ACTION UNDERTAKEN
				XSSFCell cell13 = row.createCell(13);
				cell13.setCellStyle( getCellStyle(rowNo, workbook) );
				cell13.setCellValue(natureOfServiceUnderTaken);
				//				cell12.setCellValue(callDetails.get(i).getNatureOfJobs().getJobNature());

				// Call Attended date
				XSSFCell cell14 = row.createCell(14);
				cell14.setCellStyle( getCellStyle(rowNo, workbook) );
				cell14.setCellValue(""+callAttendedDate);
				//				row.createCell(12).setCellValue(""+callAttendedDate);

				// call Closed Date
				XSSFCell cell15 = row.createCell(15);
				cell15.setCellStyle( getCellStyle(rowNo, workbook) );
				cell15.setCellValue(""+callClosedDate);
				//				row.createCell(13).setCellValue(""+callClosedDate);

				// Employee Names
				XSSFCell cell16 = row.createCell(16);
				cell16.setCellStyle( getCellStyle(rowNo, workbook) );
				cell16.setCellValue(employeeNames);
				//				row.createCell(14).setCellValue(employeeNames);

				XSSFCell cell17 = row.createCell(17);
				cell17.setCellStyle( getCellStyle(rowNo, workbook) );
				//B'ze DB status is on going,pending etc
				if(callDetails.get(i).getCdStatus().equals("on going")){
					cell17.setCellValue("IN PROGRESS");
				}else if(callDetails.get(i).getCdStatus().equals("pending")){
					cell17.setCellValue("NOT COMPLETED");
				}else{
					cell17.setCellValue(callDetails.get(i).getCdStatus().toUpperCase());
				}
				//				cell17.setCellValue(callDetails.get(i).getCdStatus());
				//				cell17.setCellValue(callDetails.get(i).getCdStatus());
				//				row.createCell(15).setCellValue(callDetails.get(i).getCdStatus());

				//   Details from, the Employee who filled remarks, at the time of service report
				XSSFCell cell18 = row.createCell(18);
				cell18.setCellStyle( getCellStyle(rowNo, workbook) );
				cell18.setCellValue(remarks);
			}
			sheet.setAutoFilter(CellRangeAddress.valueOf("A5:S5"));
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCallDetailsReport: "+e.getMessage());
		}
		return workbook;
	}
	/**
	 *    Checking the jSon format is valid or not.
	 *    Some old data ( before 2020), will not work
	 *    Mainly its for old data ( before 2020)
	 * @param jSon
	 * @return
	 */
	private boolean isJson(String jSon){
		boolean valid = false;
		try {
			JsonParser parser = new ObjectMapper().getJsonFactory()
					.createJsonParser(jSon);
			while (parser.nextToken() != null) {
			}
			valid = true;
			parser.close();
			//		} catch (JsonParseException jpe) {
			//			jpe.printStackTrace();
		} catch (Exception ioe) {
			//			ioe.printStackTrace();
			System.out.println("CallManagement, isJson : Not a vaild json");
		}

		return valid;
	}

	private CellStyle getCellStyle(int rowNo,XSSFWorkbook workbook){
		CellStyle cellStyle = null;
		try{
			/**
			if( rowNo % 2 == 0){
				CellStyle oddRowStyle  = workbook.createCellStyle();
				cellStyle = oddRowStyle;
			}else{

				CellStyle evenRowStyle  = workbook.createCellStyle();
				cellStyle = evenRowStyle;
			}
			 **/
			cellStyle  = workbook.createCellStyle();
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			//Setting up automatic line break
			cellStyle.setWrapText(true);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCellStyle: "+e.getMessage());
		}
		return cellStyle;

	}

	/**
	 *    Service Report , Excel Report
	 * @param context
	 * @param dateFrom
	 * @param dateTo
	 * @param searchKeyWord
	 * @param gurenteePeriod
	 * @param employeeId
	 * @param statusId
	 * @return
	 */
	@Override
	public XSSFWorkbook getServiceReportExcel(ServletContext context,String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod,int employeeId,int statusId){

		XSSFWorkbook workbook = null;
		try{
			List<ServiceReport> serviceReports = searchServiceReport(dateFrom, dateTo,
					searchKeyWord, gurenteePeriod, employeeId, statusId,null);


			String filePath = context.getRealPath("/WEB-INF/views/excelReport/serviceReport.xlsx");
			//			System.out.println("Bussiness,getCallDetailsReport,filePath: "+filePath);
			File myFile = new File(filePath);
			FileInputStream fis = new FileInputStream(myFile);
			workbook = new XSSFWorkbook(fis);

			XSSFSheet sheet = workbook.getSheetAt(0);

			XSSFRow row = null;


			sheet.createFreezePane(0, 1); // Freeze 1st Row   sheet.createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow)
			int rowId = 5;
			for (int i=0; i< serviceReports.size();i++) {
				CallDetail callDetail = callManagementDAO.getCallDetailById(serviceReports.get(i).getSr_cd_id());
				List<Allot> allots = callManagementDAO.getAllotByCdId(callDetail.getCdId());
				int rowNo = rowId++;
				row = sheet.createRow(rowNo);

				XSSFCell cell0 = row.createCell(0);
				cell0.setCellStyle( getCellStyle(rowNo, workbook) );
				cell0.setCellValue(i+1);
				//				row.createCell(0).setCellValue(i+1);
				XSSFCell cell1 = row.createCell(1);
				cell1.setCellStyle( getCellStyle(rowNo, workbook) );
				cell1.setCellValue(utilService.getTimeStampToString(callDetail.getCreatedAt()));

				XSSFCell cell2 = row.createCell(2);
				cell2.setCellStyle( getCellStyle(rowNo, workbook) );
				if(callDetail.getCdAllotNo() == null){
					cell2.setCellValue("");
				}else{
					cell2.setCellValue(callDetail.getCdAllotNo());
				}
				XSSFCell cell3 = row.createCell(3);
				cell3.setCellStyle( getCellStyle(rowNo, workbook) );
				cell3.setCellValue(callDetail.getCdCustomerName());

				XSSFCell cell4 = row.createCell(4);
				cell4.setCellStyle( getCellStyle(rowNo, workbook) );
				cell4.setCellValue(callDetail.getSiteDetails());

				XSSFCell cell5 = row.createCell(5);
				cell5.setCellStyle( getCellStyle(rowNo, workbook) );
				cell5.setCellValue(callDetail.getProductDetails());

				XSSFCell cell6 = row.createCell(6);
				cell6.setCellStyle( getCellStyle(rowNo, workbook) );
				cell6.setCellValue(callDetail.getNatureOfJobs().getJobNature());

				String dateStr = "";
				String obserVationStr = "";
				String employeeNames = "";

				dateStr = dateStr + utilService.getDateToString(serviceReports.get(i).getSrCallAttendDate())+ ", ";
				//				obserVationStr = obserVationStr + serviceReports.get(i).getObservationDetails()+ ", ";
				//				obserVationStr = obserVationStr + serviceReports.get(i).getObervationDetails()+ ", ";

				if(serviceReports.get(i).getObervationDetails()!=null){
					List<ObservationBeforeMaintanence> obs = getObservationBeforeFromJson(serviceReports.get(i).getObervationDetails());
					for(ObservationBeforeMaintanence ob: obs){
						obserVationStr = obserVationStr + ob.getObervationDetails()+ ", ";
					}
				}

				for(Allot allot: allots){
					List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(allot.getAlId());
					for(AllottedEmployees ae: allottedEmployees){
						Employee employee = userDAO.getEmployeeById(ae.getEmployeeId());
						employeeNames = employeeNames  +employee.getFirstName()+" "+employee.getLastName() + ", ";
					}
				}
				// Call Attended date
				XSSFCell cell7 = row.createCell(7);
				cell7.setCellStyle( getCellStyle(rowNo, workbook) );
				cell7.setCellValue(""+dateStr);

				XSSFCell cell8 = row.createCell(8);
				cell8.setCellStyle( getCellStyle(rowNo, workbook) );
				cell8.setCellValue(callDetail.getCdProblemDetails());

				//Observation before maintenance 
				XSSFCell cell9 = row.createCell(9);
				cell9.setCellStyle( getCellStyle(rowNo, workbook) );
				cell9.setCellValue(obserVationStr);

				// Employee Names
				XSSFCell cell10 = row.createCell(10);
				cell10.setCellStyle( getCellStyle(rowNo, workbook) );
				cell10.setCellValue(employeeNames);

				XSSFCell cell11 = row.createCell(11);
				cell11.setCellStyle( getCellStyle(rowNo, workbook) );
				//B'ze DB status is on going,pending etc
				if(callDetail.getCdStatus().equals("on going")){
					cell11.setCellValue("IN PROGRESS");
				}else if(callDetail.getCdStatus().equals("pending")){
					cell11.setCellValue("NOT COMPLETED");
				}else{
					cell11.setCellValue(callDetail.getCdStatus().toUpperCase());
				}

				XSSFCell cell12 = row.createCell(12);
				cell12.setCellStyle( getCellStyle(rowNo, workbook) );
				cell12.setCellValue(serviceReports.get(i).getSrRemarks());
				//				cell12.setCellValue(callDetail.getRemarks());
			}//for (int i=0; i< serviceReports.size();i++)
			sheet.setAutoFilter(CellRangeAddress.valueOf("A5:M5"));
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceReportExcel: "+e.getMessage());
		}
		return workbook;
	}



	@Override
	public List<CallDetail> getNonAllottedCalls() {
		List<CallDetail> callDetails = null;
		try{
			callDetails = callManagementDAO.getNonAllottedCalls();
			for(int i=0;i<callDetails.size();i++){
				callDetails.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNonAllottedCalls: "+e.getMessage());
		}
		return callDetails;
	}

	@Override
	public int getNumberOfNonAllottedCalls() {
		int no = 0;
		try{
			List<CallDetail> callList = callManagementDAO.getNonAllottedCalls();
			//			List<CallDetail> callList = callManagementDAO.getCallDetails(null, null, "", "new", "");
			no = callList.size();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNumberOfNonAllottedCall: "+e.getMessage());
		}
		return no;
	}

	@Override
	public List<CallDetail> getOnGoingCalls() {
		List<CallDetail> callDetails = null;
		try{
			callDetails = callManagementDAO.getOnGoingCalls();
			//			System.out.println("CallMgtImpl,getOnGoingCalls,callDetails size: "+callDetails.s);
			//			callDetails = callManagementDAO.getCallDetails(null, null, "", "on going", "");
			for(int i=0;i<callDetails.size();i++){
				callDetails.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getOnGoingCalls: "+e.getMessage());
		}
		return callDetails;
	}

	@Override
	public List<CallDetail> getCompletedCalls() {
		List<CallDetail> callDetails = null;
		try{
			callDetails = callManagementDAO.getCompletedCalls();
			for(int i=0;i<callDetails.size();i++){
				callDetails.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCompletedCalls: "+e.getMessage());
		}
		return callDetails;
	}

	/**
	 *  Dash board details
	 */
	@Override
	public DashBoardVariables getAdminDashboard() {
		DashBoardVariables dashBoardVariables = null;
		List<CallDetail> nonAllottedTemp = new ArrayList<CallDetail>();
		List<CallDetail> onGoingTemp = new ArrayList<CallDetail>();
		List<CallDetail> completedTemp = new ArrayList<CallDetail>();
		List<CallDetail> pendingTemp = new ArrayList<CallDetail>();

		try{

			List<CallDetail> nonAllottedCalls = getNonAllottedCalls();
			List<CallDetail> onGoingCalls = getOnGoingCalls();
			List<CallDetail> pendingCalls = getPendingCalls();
			//			Date todaysDate = utilService.getTodaysDate();
			//			Date tomorrowDate = utilService.getTheDayBeforeOrAfter(todaysDate, 1);
			// Getting last year's completed call details
			//			Date previousYearDate = utilService.getPreviousYearDate(todaysDate);
			//			Date theDayBefore = utilService.getTheDayBeforeOrAfter(previousYearDate, -1);
			//			System.out.println("CallManagementImpl,getAdminDashboard, previousYearDate: "+previousYearDate+",tomorrowDate: "+tomorrowDate);
			List<CallDetail> completedCalls = getCompletedCalls();
			//			System.out.println("CallManagementImpl,getAdminDashboard, completedCalls: "+completedCalls.size());

			dashBoardVariables = new DashBoardVariables();
			dashBoardVariables.setNoOfNonAllotedCalls(nonAllottedCalls.size());
			dashBoardVariables.setNoOfOngoingCalls(onGoingCalls.size());
			dashBoardVariables.setNoOfCompletedCalls(completedCalls.size());
			dashBoardVariables.setNoPendingCalls(pendingCalls.size());
			dashBoardVariables.setNewCallViewStatus(getCallViewStatus(nonAllottedCalls));
			dashBoardVariables.setPendingCallViewStatus(getCallViewStatus(pendingCalls));
			dashBoardVariables.setCompletedCallViewStatus(getCallViewStatus(completedCalls));

			//			System.out.println("CallManagementImpl,getAdminDashboard, pending call size: "+pendingCalls.size()+
			//					", nonAllottedCalls, size: "+nonAllottedCalls.size()+", completed call size : "+completedCalls.size());
			//			System.out.println("CallManagementImpl,getAdminDashboard, NewCall: "+dashBoardVariables.getNewCallViewStatus()+
			//					", PendingCall : "+dashBoardVariables.getPendingCallViewStatus()+
			//					", CompletedCall: "+dashBoardVariables.getCompletedCallViewStatus());

			if(nonAllottedCalls.size() >= 5){
				for(int i=0;i<5;i++){
					nonAllottedTemp.add(nonAllottedCalls.get(i));
				}
			}else{
				for(CallDetail ca:nonAllottedCalls){
					nonAllottedTemp.add(ca);
				}
			}
			if(onGoingCalls.size() >= 5){
				for(int j=0;j<5;j++){
					onGoingTemp.add(onGoingCalls.get(j));
				}
			}else{
				for(CallDetail oc:onGoingCalls){
					onGoingTemp.add(oc);
				}
			}
			for(int k=0;k<5;k++){
				completedTemp.add(completedCalls.get(k));
			}
			if(pendingCalls.size() >= 5){
				for(int m=0;m<5;m++){
					pendingTemp.add(pendingCalls.get(m));
				}
			}else{
				for(CallDetail ca:pendingCalls){
					pendingTemp.add(ca);
				}
			}

			//New
			dashBoardVariables.setNonAllottedCalls(nonAllottedTemp);
			//In Progress
			dashBoardVariables.setOnGoingCalls(updateEmployeeMinData(onGoingTemp));
			// Completed
			dashBoardVariables.setCompletedCalls(updateEmployeeMinData(completedTemp));
			///Not Completed
			dashBoardVariables.setPendingCalls(updateEmployeeMinData(pendingTemp));

			//			List<CallDetail> callOngoing = dashBoardVariables.getOnGoingCalls();
			//			for( CallDetail c : callOngoing){
			//				List<EmployeeMinData> min = c.getEmployeeList();
			//				System.out.println("CallMgt, getAdminDashboard, "+c.getCdId()+", Allot no: "+c.getCdAllotNo());
			//				for(EmployeeMinData emp: min){
			//					System.out.println("CallMgt, getAdminDashboard,emp name: "+emp.getFirstName());
			//				}
			//			}


			//			dashBoardVariables.setNonAllottedCalls(nonAllottedTemp);
			//			dashBoardVariables.setOnGoingCalls(onGoingTemp);
			//			dashBoardVariables.setCompletedCalls(completedTemp);
			//			dashBoardVariables.setPendingCalls(pendingTemp);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("DashBoardVariables: "+e.getMessage());
		}
		return dashBoardVariables;
	}

	private List<CallDetail> updateEmployeeMinData(List<CallDetail> callDetails){

		List<CallDetail> list = null;
		List<EmployeeMinData> employeeMinDatas = null;;
		Map<Integer,EmployeeMinData> empMap= new Hashtable<Integer, EmployeeMinData>();

		try{
			list = new ArrayList<CallDetail>();
			for(int i=0;i<callDetails.size();i++){
				List<EmployeeMinData> emDatas = new ArrayList<EmployeeMinData>();
				List<Allot> allots = callManagementDAO.getAllotByCdId(callDetails.get(i).getCdId());
				for(Allot allot: allots){
					emDatas = getAllEmployeeMinDataByAlId(allot.getAlId());
				}
				for(EmployeeMinData eMinData: emDatas){
					eMinData.setFullName(eMinData.getFirstName(), eMinData.getLastName());
					empMap.put(eMinData.getEmployeeId(), eMinData);
				}
				employeeMinDatas = new ArrayList<EmployeeMinData>(empMap.values());
				callDetails.get(i).setEmployeeList(employeeMinDatas);
				list.add(callDetails.get(i));
				empMap.clear();
				emDatas.clear();
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateEmployeeMinData: "+e.getMessage());
		}
		return list;

	}//updateEmployeeMinData

	/**
	 *  It provides Minimal data of Employee, mainly for UI purpose
	 * @param alId
	 * @return
	 */
	private List<EmployeeMinData> getAllEmployeeMinDataByAlId(int alId){
		List<EmployeeMinData> employeeMinDatas = null;
		try{
			List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(alId);
			employeeMinDatas = new ArrayList<EmployeeMinData>();
			for(AllottedEmployees ae: allottedEmployees){
				Employee employee = ae.getEmployee();
				EmployeeMinData employeeMinData = employeeMinDataFactory.createEmployeeMinData();
				employeeMinData.setEmpCode(employee.getEmpCode());
				employeeMinData.setEmployeeId(employee.getEmployeeId());
				employeeMinData.setFirstName(employee.getFirstName());
				employeeMinData.setLastName(employee.getLastName());
				employeeMinDatas.add(employeeMinData);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("employeeMinDatas: "+e.getMessage());
		}
		return employeeMinDatas;

	}

	@Override
	public List<Allot> getAllAllotDetails(int isNew) {
		List<Allot> allots = null;
		try{
			allots = callManagementDAO.getAllAllotDetails(isNew);
			for(int i=0;i<allots.size();i++){
				allots.get(i).setSlNo(i+1);;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllAllotDetails: "+e.getMessage());
		}
		return allots;
	}

	@Override
	public CallDetail saveOrUpdateCallDetails(ServletContext servletContext,CallDetail callDetail) {
		CallDetail cDetail = null;
		try{
			//			System.out.println("CallManagementImpl,saveOrUpdateCallDetails, getProductDetails: "
			//					+callDetail.getProductDetails()+", NatureJobId: "+callDetail.getNatureJobId());
			callDetail.setCdCustomerName(callDetail.getCustomerId());
			callDetail.setCdBoardDivision(callDetail.getBoardDivisionId());
			callDetail.setCustomerId(null);
			callDetail.setBoardDivisionId(null);

			//			if(callDetail.getNatureJobId() > 0){
			//				NatureOfJobs natureOfJobs = callManagementDAO.getNatureOfJobsById(callDetail.getNatureJobId());
			//			}
			if(callDetail.getPanelId() != null){
				//				PanelDetails panelDetails = callManagementDAO.getPanelDetailsById
				//						(Integer.parseInt(callDetail.getPanelId()));
				callDetail.setCdRelayPanelDetails(callDetail.getPanelId());
				callDetail.setProductDetails(callDetail.getPanelId());
			}
			if(callDetail.getRelayId() != null){
				//				RelayDetails relayDetails = callManagementDAO.getRelayDetailsById
				//						(Integer.parseInt(callDetail.getRelayId()));
				callDetail.setCdRelayPanelDetails(callDetail.getRelayId());
				callDetail.setProductDetails(callDetail.getRelayId());
			}
			Timestamp timeSt=utilService.dateToTimeStamp(utilService.getCurrentDateAndTime());
			//			CallDetail lastCallDetailsRecord = callManagementDAO.getLastCallDetailsRecord();
			LatestRequestIds lastServiceRequestId = callManagementDAO.getLastServiceRequestId(1);
			String newReviceRequestId = utilService.createServiceRequestId
					(lastServiceRequestId.getLastServiceRequestId());
			//			System.out.println("CallManagementImpl,saveOrUpdateCallDetails, newReviceRequestId: "+newReviceRequestId);
			callDetail.setServiceRequestId(newReviceRequestId);
			callDetail.setCreatedAt(timeSt);
			callDetail.setUpdatedAt(timeSt);
			callDetail.setCdStatus("new");
			callDetail.setCdGuranteePeriod("No");
			callDetail.setIsActive(1);
			callDetail.setCdProblemDetails(callDetail.getRemarks());
			/**
			 *  Whether the new message Read or not.
			 *  Initially set as Unread
			 *  If 1 -> Unread
			 */
			callDetail.setViewAlert(1);
			cDetail = callManagementDAO.saveOrUpdateCallDetails(callDetail);
			//			LastestRequestIds laServiceRequestId = new LastestRequestIds();
			//			laServiceRequestId.setLastId(1);

			lastServiceRequestId.setLastServiceRequestId(cDetail.getServiceRequestId());
			callManagementDAO.updateLatestServiceRequestId(lastServiceRequestId);
			//			System.out.println("CallManagementImpl,saveOrUpdateCallDetails, cdId: "+cDetail.getCdId());
			/**
			 *  Email for Customer
			 */
			String content = "Your call is successfully registered. Service request ID - "+newReviceRequestId
					+" "+"Please note same for further reference";
			MailModel mailModel = mailModelFactory.createMailModel();
			mailModel.setSubject("Alind Relays / Call Registration Details / SR ID : "+cDetail.getServiceRequestId());
			mailModel.setCallDetail(cDetail);
			mailModel.setHtmlFileName("callRegisterToCustomer");
			mailModel.setContent(content);
			mailModel.setTo(callDetail.getCdEmail());
			utilService.sendEmail(servletContext,mailModel);


			//
			//			String value = " Alind Support Team" ;
			//			String subject = " Alind Relays / Call Registration Details";
			//			MimeMessagePreparator  preparator =  utilService.getMessagePreparator
			//					(cDetail.getCdEmail(), cDetail.getCdCustomerName(), value,subject, messageToCustomer);
			//			JavaMailSender mailSender = utilService.getMailSender();
			//			mailSender.send(preparator);


		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceReportByCallStatus: "+e.getMessage());
		}
		return cDetail;
	}

	@Override
	public CallDetailModel updateCallDetail(CallDetailModel callDetailModel){
		CallDetailModel model = null;
		try{
			CallDetail callEntity = createCallDetailEntity(callDetailModel);

			callEntity = callManagementDAO.updateCallDetails(callEntity);
			//			System.out.println("Business,updateCallDetail, updated date: "+callEntity.getUpdatedAt()
			//					+", nature_job_id: "+callEntity.getNatureJobId());
			NatureOfJobsCallReg natureOfJobsCallReg = utilService.getNatureOfJobsCallRegById(callEntity.getNatureJobId());
			callEntity.setNatureOfJobs(natureOfJobsCallReg);
			model = createCallDetailModel(callEntity);
			//			NatureOfJobsCallReg natureOfJobsCallReg = callManagementDAO.getNatureOfJobsById(callEntity.getNatureJobId());

		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateCallDetail: "+e.getMessage());
		}

		return model;
	}


	/**
	 *  Deactivating the status. "isActive" to 0
	 */
	@Override
	public CallDetail deleteCallDetailsById(String token,int cdId) {
		CallDetail callDetail = null;
		try{

			callDetail = callManagementDAO.getCallDetailById(cdId);
			callDetail.setIsActive(0);
			callDetail = callManagementDAO.saveOrUpdateCallDetails(callDetail);

			/**
			 *  Making audit logs
			 */
			Employee employee = userService.getEmployeeByToken(token);
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(employee.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("CallDetail");
			auditJson.setId(Integer.toString(cdId));
			auditJson.setRemarks(callDetail.getServiceRequestId());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteCallDetailsById: "+e.getMessage());
		}
		return callDetail;
	}


	/**
	 *  Whether the new message Read or not.
	 *  If 0 -> Read
	 *  If 1 -> Unread
	 */
	@Override
	public int updateViewAlert(Object object){
		int value = 0;
		try{

			LinkedHashMap<String, String> lhm = (LinkedHashMap<String, String>) object;
			LinkedHashMap<String, String> viewObj=null;
			// Generating a Set of entries
			Set set = lhm.entrySet();
			// Displaying elements of LinkedHashMap
			Iterator iterator = set.iterator();
			while(iterator.hasNext()) {
				Map.Entry me = (Map.Entry)iterator.next();
				//				System.out.print("CallManagement, updateViewAlert, Key is: "+ me.getKey() + 
				//						"& Value is: "+me.getValue()+"\n");
				viewObj = (LinkedHashMap<String, String>) me.getValue();
			}
			String id = String.valueOf(viewObj.get("id"));
			String action = String.valueOf(viewObj.get("action"));
			//			System.out.println("CallManagement, updateViewAlert, allot, action: "+action+", id: "+id);
			switch (action) {
			case "workDetails":
				int alId = Integer.parseInt(id);
				Allot allot = callManagementDAO.getAllotByAlId(alId);
				//				System.out.println("CallManagement, updateViewAlert, allot, cdId: "+allot.getCdId());
				CallDetail callDetailWork = callManagementDAO.getCallDetailById(allot.getCdId());
				callDetailWork.setViewAlert(0);
				callDetailWork = callManagementDAO.saveOrUpdateCallDetails(callDetailWork);
				value = alId;
				//				System.out.println("CallManagement, updateViewAlert, alId: "+alId+", action: "+action
				//						+",cdId: "+callDetailWork.getCdId());
				break;
			case "callDetails":
				int cdId = Integer.parseInt(id);
				CallDetail callDetail = callManagementDAO.getCallDetailById(cdId);
				callDetail.setViewAlert(0);
				callDetail = callManagementDAO.saveOrUpdateCallDetails(callDetail);
				value = cdId;
				//				System.out.print("CallManagement, updateViewAlert, srId: "+cdId+", action: "+action);
				break;
			case "serviceReport":
				int srId = Integer.parseInt(id);
				//				System.out.print("CallManagement, updateViewAlert, srId: "+srId+", action: "+action);
				ServiceReport serviceReport = callManagementDAO.getServiceReportById(srId);
				serviceReport.setViewAlert(0);
				serviceReport = callManagementDAO.saveOrUpdateServiceReport(serviceReport);

				value = srId;
				break;
			case "newWork": // allottedEmployees - Employee Account/Login
				alId = Integer.parseInt(id);
				//				System.out.print("CallManagement, updateViewAlert, alId: "+alId+", action: "+action);
				Allot alot = callManagementDAO.getAllotByAlId(alId);
				alot.setViewAlert(0);
				alot = callManagementDAO.saveOrUpdateAllot(alot);
				value = alot.getAlId();
			default:
				break;
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateViewAlertById: "+e.getMessage());
		}
//		System.out.print("CallManagement, updateViewAlert, return value: "+value);
		return value;
	}



	@Override
	public List<ServiceReport> getServiceReportByCallStatus(String callStatus) {
		List<ServiceReport> serviceReports = null;
		try{
			serviceReports = callManagementDAO.getServiceReportByCallStatus(callStatus);
			//			System.out.println("getServiceReportByCallStatus");
			for(int i=0;i<serviceReports.size();i++){
				serviceReports.get(i).getEmployee().setViewPass(null);
				serviceReports.get(i).getEmployee().setUserName(null);
				//				System.out.println("Allot no:"+serviceReports.get(i).getCallDetail().getAlAllotNo());
				//				System.out.println(""+serviceReports.get(i).getEmployee().getEmployeeId());
				//				List<Allot> alt = serviceReports.get(i).getEmployee().getAllots();
				//				for(int j=0;j<alt.size();j++){
				//					System.out.println("allot no: "+alt.get(j));
				//				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceReportByCallStatus: "+e.getMessage());
		}
		return serviceReports;
	}

	@Override
	public List<CallDetail> getAllCompletedCalls(){
		List<CallDetail> callDetails = null;
		try{
			callDetails = callManagementDAO.getAllCompletedCalls();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllCompletedCalls: "+e.getMessage());
		}
		return callDetails;
	}

	@Override
	public List<CallDetail> getCompletedCallsFromToDate(Date fromDate,
			Date toDate) {
		List<CallDetail> callDetails = null;
		try{
			callDetails = callManagementDAO.getCompletedCallsFromToDate(fromDate, toDate);
			//			for(int i=0;i<callDetails.size();i++){
			//				callDetails.get(i).setSerialNo(i+1);
			//			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCompletedCallsFromToDate: "+e.getMessage());
		}

		return callDetails;
	}

	/**
	@Override
	public List<CallDetail> searchCallDetails(String keyWords,String fromDate, String toDate) {
		List<CallDetail> callDetails = null;
		try{
			callDetails = callManagementDAO.searchCallDetails(keyWords,utilService.getDateFromString(fromDate),
					utilService.getDateFromString(toDate));
			//			for(int i=0;i<callDetails.size();i++){
			//				System.out.println("CallManagementImpl, searchCallDetails, cdId: "+callDetails.get(i).getCdId());
			//			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchCallDetails: "+e.getMessage());
		}
		return callDetails;
	}**/

	@Override
	public List<CallDetail> getPendingCalls() {
		List<CallDetail> callDetails = null;
		try{
			callDetails = callManagementDAO.getPendingCalls();
			//			callDetails = callManagementDAO.getCallDetails(null, null, "", "pending", "");
			for(int i=0;i<callDetails.size();i++){
				callDetails.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getPendingCalls: "+e.getMessage());
		}
		return callDetails;
	}

	@Override
	public List<CallDetail> getAllWorkDetails() {
		List<CallDetail> callDetails = null;
		try{
			List<CallDetail> cDetails = getOnGoingCalls();
			for(int i=0;i<cDetails.size();i++){
				String allotNo = cDetails.get(i).getCdAllotNo();
				//				System.out.println("CallMgtserviceImpl, getAllWorkDetails, allotNo: "+allotNo);
				Allot allot = callManagementDAO.getAlloDetailsfromAllotNumber(allotNo);
				//				System.out.println("CallMgtserviceImpl, getAllWorkDetails,id: "+allot.getAlId());
				//				Employee employee = userDAO.getEmployeeById(allot.getAlEmpId());
				//				Employee employee = allot.getEmployee();
				//				callDetails.get(i).setEmployee(employee);
				//				Date altDate = allot.getAllotDate();
				//				String dateStr = utilService.getDateToString(altDate);
				//				callDetails.get(i).setAllotDateStr(dateStr);
				cDetails.get(i).setCdAllotDate(allot.getAllotDate());
			}
			callDetails = cDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllWorkDetails: "+e.getMessage());
		}
		return callDetails;
	}



	@Override
	public List<ServiceReport> searchServiceReport(String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod,int employeeId,int statusId
			,ServletContext context){
		List<ServiceReport> serviceReports = null;
		List<ServiceReport> updatedServiceReports = null;
		String firstName = "";
		String lastName = "";
		String status = "";
		String searchKeyTemp = "";
		List<Employee > eList = new ArrayList<Employee>();
		Employee employee = null;
		try{

			// Search based on Call Attend Date 
			Date frDate = utilService.getDateFromString(dateFrom);
			// Changing the format to Thu Jun 25 00:00:00 IST 2020,
			frDate = utilService.fromDateStartFromZeroHrs(frDate);

			Date toDate = utilService.getDateFromString(dateTo);
			//Changing the format to Thu Jun 25 23:59:59 IST 2020
			toDate = utilService.toDateEndToLastMin(toDate);
			//			System.out.println("CallMgtServiceImpl, searchServiceReport,frDate: "+frDate+", toDate: "+toDate);

			// If select an Employee
			if(employeeId > 0){
				//				employee = userDAO.getEmployeeById(employeeId);
				updatedServiceReports = new ArrayList<ServiceReport>();
				List<AllottedEmployees> allottedEmployeesList = callManagementDAO.
						getAllottedWorkDetailsByEmpId(employeeId);
				for(AllottedEmployees ae: allottedEmployeesList){
					//					System.out.println("CallMgtServiceImpl, searchServiceReport,alId: "+ae.getAlId());
					Allot allot = ae.getAllot();
					if(allot.getCallDetail().getIsActive() == 1){
						ServiceReport serviceReport = callManagementDAO.getServiceReportByAlId(ae.getAlId());
						// It's because of Old DB data not properly mapping with new updates 
						if(serviceReport!=null){
							//							System.out.println("CallMgtServiceImpl, searchServiceReport,srId: "+serviceReport.getSrId());
							updatedServiceReports.add(serviceReport);
						}
					}
				}
				//				System.out.println("CallMgtServiceImpl, searchServiceReport,updatedServiceReports size: "+updatedServiceReports.size());
				for(ServiceReport sr: updatedServiceReports){
					List<EmployeeMinData> emMinDatas = new ArrayList<EmployeeMinData>();
					//					System.out.println("CallMgtServiceImpl, searchServiceReport,srId: "+sr.getSrId());
					ServiceAllotConnector serviceAllotConnector = callManagementDAO.getServiceAllotConnectorBySrId(sr.getSrId());
					List<AllottedEmployees> allottedEmployees = callManagementDAO.
							getAllottedWorkDetailsByalId(serviceAllotConnector.getAlId());
					for(AllottedEmployees ae: allottedEmployees){
						Employee emp = ae.getEmployee();
						EmployeeMinData minData = userService.getEmployeeMinData(emp);
						emMinDatas.add(minData);
					}
					sr.setEmployeeMinDataList(emMinDatas);
				}

				Hashtable<Integer, ServiceAllotConnector> serAllotMap = new Hashtable<Integer, ServiceAllotConnector>();
				//*  Sorting based on service created date

				Collections.sort(updatedServiceReports, new Comparator<ServiceReport>() {
					public int compare(ServiceReport o1, ServiceReport o2) {
						return o1.getCreatedAt().compareTo(o2.getCreatedAt());
					}
				});
				Collections.reverse(updatedServiceReports);

				for(int i=0;i<updatedServiceReports.size();i++){
					updatedServiceReports.get(i).getEmployee().setViewPass(null);
					updatedServiceReports.get(i).getEmployee().setUserName(null);
					updatedServiceReports.get(i).getEmployee().setPasswordHash(null);
					updatedServiceReports.get(i).setSlNo(i+1);
					//					updatedServiceReports.get(i).getAllot().getAlAllotNo()

					if(updatedServiceReports.get(i).getMinutesFilePath()!= null){
						updatedServiceReports.get(i).setFilePath(Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+updatedServiceReports.get(i).getSrId()+"\\"+
								updatedServiceReports.get(i).getMinutesFilePath());
					}
					// Adding alId for Edit, Allot purpose
					ServiceAllotConnector sa = serAllotMap.get(updatedServiceReports.get(i).getSrId());
					//					System.out.println("CallMgtServiceImpl, searchServiceReport,srId: "+updatedServiceReports.get(i).getSrId());
					//					updatedServiceReports.get(i).setAlId(updatedServiceReports.get(i).getAllot().getAlId());
					Allot allot = callManagementDAO.getAllotByAlId(sa.getAlId());
					updatedServiceReports.get(i).setAlAllotNo(allot.getAlAllotNo());
					//					updatedServiceReports.get(i).setAlAllotNo(updatedServiceReports.get(i).getAllot().getAlAllotNo());
				}

				//				System.out.println("CallMgtServiceImpl, searchServiceReport,employeeId > 0, updatedServiceReports size: "+updatedServiceReports.size());
			}else{
				if(statusId > 0){
					status = getCallStatus(statusId);
				}
				//			System.out.println("CallMgtService,searchServiceReport,serviceReports, gurenteePeriod: "+gurenteePeriod
				//					+", status: "+status);

				if(employeeId > 0){
					employee = userDAO.getEmployeeById(employeeId);
					//				System.out.println("CallMgtService,searchServiceReport,serviceReports, employeeId: "+employeeId);
					firstName = employee.getFirstName();
					lastName = employee.getLastName();
					if(searchKeyWord.equals("")){
						searchKeyTemp = firstName +" "+ lastName;
					}else{
						searchKeyTemp = searchKeyWord ;
					}
				}else{
					firstName = searchKeyWord;
					lastName = searchKeyWord;
					searchKeyTemp = searchKeyWord ;
				}

				if(!gurenteePeriod.equals("all")){
					if(searchKeyWord.equals("")){
						searchKeyTemp = "all";
					}else{
						searchKeyTemp = searchKeyWord ;
					}
				}

				//				Date dateFr=utilService.getDateFromString(dateFrom);
				//				Date dateT=utilService.getDateFromString(dateTo);
				//			System.out.println("CallMgtService,searchServiceReport,serviceReports, dateFr: "+dateFr
				//					+", dateT: "+dateT);
				serviceReports = callManagementDAO.searchServiceReport(frDate, toDate, searchKeyTemp, 
						gurenteePeriod,status);
				//			serviceReports = callManagementDAO.searchServiceReportTest(dateFr, dateT, searchKeyTemp, 
				//					gurenteePeriod,status);
				//				System.out.println("CallMgtService,searchServiceReport,serviceReports, searchKeyTemp: "
				//						+searchKeyTemp+", serviceReports, size: "+serviceReports.size());
				Hashtable<Integer, ServiceAllotConnector> serAllotMap = new Hashtable<Integer, ServiceAllotConnector>();
				List<ServiceAllotConnector> serviceAllotConnectors = callManagementDAO.getServiceAllotList();
				for(ServiceAllotConnector sa: serviceAllotConnectors){
					serAllotMap.put(sa.getSrId(), sa);
				}

				Hashtable<Integer, ServiceReport> serviceTable = new Hashtable<Integer, ServiceReport>();
				for(ServiceReport sp: serviceReports){
					// Some old-un necessary values in the table, so its filtering again to opt out
					// in-active(isActive = 0) "CallDetails"  related service report
					if(serAllotMap.containsKey(sp.getSrId())){
						serviceTable.put(sp.getSrId(), sp);
					}
				}
				List<CallDetail> callDetails = new ArrayList<CallDetail>();
				if(callDetails.size() >0){
					for(CallDetail cd: callDetails){
						//					System.out.println("CallMgtService,searchServiceReport,serviceReports,getCdId: "+cd.getCdId());
						for(ServiceAllotConnector sa: serviceAllotConnectors){

							if(cd.getCdId()== sa.getServiceReport().getSr_cd_id()){
								serviceTable.put(sa.getSrId(), sa.getServiceReport());
							}
						}// for(ServiceAllotConnector sa: serviceAllotConnectors)
					}//for(CallDetail cd: callDetails)
				}//if(callDetails.size() >0){
				//				System.out.println("CallMgtServiceImpl, searchServiceReport,after callDetails, serviceTable size: "+serviceTable.size());
				//				if(employeeId > 0){
				//			String tomcatBase = System.getProperty("catalina.base");
				//			System.out.println("CallMgtServiceImpl,searchServiceReport,callDetails, size: "+callDetails.size()+", tomcatBase: "+tomcatBase);
				if(!firstName.isEmpty()){
					//				System.out.println("CallMgtServiceImpl,searchServiceReport,firstName: "+firstName+", lastName: "+lastName);
					eList = callManagementDAO.searchEmployeesByName(firstName,lastName,"");
				}
				//			System.out.println("CallMgtServiceImpl, searchServiceReport,eList,size: "+eList.size()
				//					+" ,serviceAllotConnectors size: "+serviceAllotConnectors.size());
				if(eList.size() >0){
					Hashtable<Integer, Employee> empTable = new Hashtable<Integer, Employee>();
					for(Employee e: eList){
						empTable.put(e.getEmployeeId(), e);
					}
					for(ServiceAllotConnector sa: serviceAllotConnectors){
						List<AllottedEmployees> allottedEmployeesList = callManagementDAO.
								getAllottedWorkDetailsByalId(sa.getAlId());
						for(AllottedEmployees ae: allottedEmployeesList){
							if(empTable.containsKey(ae.getEmployeeId())){
								ServiceReport serviceReport = callManagementDAO.getServiceReportById(sa.getSrId());
								if(serviceReport.getEmployeeId() == ae.getEmployeeId()){
									serviceTable.put(sa.getSrId(), serviceReport);
									//								System.out.println("CallMgtServiceImpl, searchServiceReport,serviceTable,srId: "
									//										+ ""+sa.getSrId()+", getEmployeeId: "+ae.getEmployeeId()+", AllotedEmpoyeesId: "+ae.getAllotedEmpoyeesId()
									//										+",Serv empid: "+serviceReport.getEmployeeId());
								}
							}
						}//for(AllottedEmployees ae: allottedEmployeesList)
					}//for(ServiceAllotConnector sa: serviceAllotConnectors)

				}//if(eList.size() >0)

				updatedServiceReports = new ArrayList<ServiceReport>(serviceTable.values());
				for(ServiceReport sr: updatedServiceReports){
					List<EmployeeMinData> emMinDatas = new ArrayList<EmployeeMinData>();
					ServiceAllotConnector serviceAllotConnector = callManagementDAO.getServiceAllotConnectorBySrId(sr.getSrId());
					List<AllottedEmployees> allottedEmployees = callManagementDAO.
							getAllottedWorkDetailsByalId(serviceAllotConnector.getAlId());
					for(AllottedEmployees ae: allottedEmployees){
						Employee emp = ae.getEmployee();
						EmployeeMinData minData = userService.getEmployeeMinData(emp);
						emMinDatas.add(minData);
					}
					sr.setEmployeeMinDataList(emMinDatas);
				}

				//*  Sorting based on service created date

				Collections.sort(updatedServiceReports, new Comparator<ServiceReport>() {
					public int compare(ServiceReport o1, ServiceReport o2) {
						return o1.getCreatedAt().compareTo(o2.getCreatedAt());
					}
				});
				Collections.reverse(updatedServiceReports);

				for(int i=0;i<updatedServiceReports.size();i++){
					updatedServiceReports.get(i).getEmployee().setViewPass(null);
					updatedServiceReports.get(i).getEmployee().setUserName(null);
					updatedServiceReports.get(i).getEmployee().setPasswordHash(null);
					updatedServiceReports.get(i).setSlNo(i+1);
					if(updatedServiceReports.get(i).getMinutesFilePath()!= null){
						updatedServiceReports.get(i).setFilePath(Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+updatedServiceReports.get(i).getSrId()+"\\"+
								updatedServiceReports.get(i).getMinutesFilePath());
					}
					// Adding alId for Edit, Allot purpose
					ServiceAllotConnector sa = serAllotMap.get(updatedServiceReports.get(i).getSrId());
					updatedServiceReports.get(i).setAlId(sa.getAlId());
					Allot allot = callManagementDAO.getAllotByAlId(sa.getAlId());
					updatedServiceReports.get(i).setAlAllotNo(allot.getAlAllotNo());
				}

			}// else (employeeId > 0)
		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchServiceReport: "+e.getMessage());
		}
		return updatedServiceReports;
	}
	
	/**
	 * ServiceReportModelList - its for UI purpose
	 */

	@Override
	public ServiceReportModelList searchServiceReportForUI(String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod,int employeeId,int statusId
			,ServletContext context){
		ServiceReportModelList serviceReportModelList = null;
		List<ServiceReport> serviceReports = null;
		List<ServiceReport> updatedServiceReports = null;
		String firstName = "";
		String lastName = "";
		String status = "";
		String searchKeyTemp = "";
		List<Employee > eList = new ArrayList<Employee>();
		Employee employee = null;
		try{
			serviceReportModelList = new ServiceReportModelList();
			// Search based on Call Attend Date 
			Date frDate = utilService.getDateFromString(dateFrom);
			// Changing the format to Thu Jun 25 00:00:00 IST 2020,
			frDate = utilService.fromDateStartFromZeroHrs(frDate);

			Date toDate = utilService.getDateFromString(dateTo);
			//Changing the format to Thu Jun 25 23:59:59 IST 2020
			toDate = utilService.toDateEndToLastMin(toDate);
//			System.out.println("CallMgtServiceImpl, searchServiceReport,frDate: "+frDate+", toDate: "+toDate);

			// If select an Employee
			if(employeeId > 0){
				//				employee = userDAO.getEmployeeById(employeeId);
				updatedServiceReports = new ArrayList<ServiceReport>();
				List<AllottedEmployees> allottedEmployeesList = callManagementDAO.
						getAllottedWorkDetailsByEmpId(employeeId);
				for(AllottedEmployees ae: allottedEmployeesList){
					//					System.out.println("CallMgtServiceImpl, searchServiceReport,alId: "+ae.getAlId());
					Allot allot = ae.getAllot();
					if(allot.getCallDetail().getIsActive() == 1){
						ServiceReport serviceReport = callManagementDAO.getServiceReportByAlId(ae.getAlId());
						// It's because of Old DB data not properly mapping with new updates 
						if(serviceReport!=null){
							//							System.out.println("CallMgtServiceImpl, searchServiceReport,srId: "+serviceReport.getSrId());
							updatedServiceReports.add(serviceReport);
						}
					}
				}
				//				System.out.println("CallMgtServiceImpl, searchServiceReport,updatedServiceReports size: "+updatedServiceReports.size());
				for(ServiceReport sr: updatedServiceReports){
					List<EmployeeMinData> emMinDatas = new ArrayList<EmployeeMinData>();
					//					System.out.println("CallMgtServiceImpl, searchServiceReport,srId: "+sr.getSrId());
					ServiceAllotConnector serviceAllotConnector = callManagementDAO.getServiceAllotConnectorBySrId(sr.getSrId());
					List<AllottedEmployees> allottedEmployees = callManagementDAO.
							getAllottedWorkDetailsByalId(serviceAllotConnector.getAlId());
					for(AllottedEmployees ae: allottedEmployees){
						Employee emp = ae.getEmployee();
						EmployeeMinData minData = userService.getEmployeeMinData(emp);
						emMinDatas.add(minData);
					}
					sr.setEmployeeMinDataList(emMinDatas);
				}

				Hashtable<Integer, ServiceAllotConnector> serAllotMap = new Hashtable<Integer, ServiceAllotConnector>();
				//*  Sorting based on service created date

				Collections.sort(updatedServiceReports, new Comparator<ServiceReport>() {
					public int compare(ServiceReport o1, ServiceReport o2) {
						return o1.getCreatedAt().compareTo(o2.getCreatedAt());
					}
				});
				Collections.reverse(updatedServiceReports);

				for(int i=0;i<updatedServiceReports.size();i++){
					updatedServiceReports.get(i).getEmployee().setViewPass(null);
					updatedServiceReports.get(i).getEmployee().setUserName(null);
					updatedServiceReports.get(i).getEmployee().setPasswordHash(null);
					updatedServiceReports.get(i).setSlNo(i+1);
					//					updatedServiceReports.get(i).getAllot().getAlAllotNo()

					if(updatedServiceReports.get(i).getMinutesFilePath()!= null){
						updatedServiceReports.get(i).setFilePath(Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+updatedServiceReports.get(i).getSrId()+"\\"+
								updatedServiceReports.get(i).getMinutesFilePath());
					}
					// Adding alId for Edit, Allot purpose
					ServiceAllotConnector sa = serAllotMap.get(updatedServiceReports.get(i).getSrId());
					//					System.out.println("CallMgtServiceImpl, searchServiceReport,srId: "+updatedServiceReports.get(i).getSrId());
					//					updatedServiceReports.get(i).setAlId(updatedServiceReports.get(i).getAllot().getAlId());
					Allot allot = callManagementDAO.getAllotByAlId(sa.getAlId());
					updatedServiceReports.get(i).setAlAllotNo(allot.getAlAllotNo());
					//					updatedServiceReports.get(i).setAlAllotNo(updatedServiceReports.get(i).getAllot().getAlAllotNo());
				}

				//				System.out.println("CallMgtServiceImpl, searchServiceReport,employeeId > 0, updatedServiceReports size: "+updatedServiceReports.size());
			}else{
				if(statusId > 0){
					status = getCallStatus(statusId);
				}
				//			System.out.println("CallMgtService,searchServiceReport,serviceReports, gurenteePeriod: "+gurenteePeriod
				//					+", status: "+status);

				if(employeeId > 0){
					employee = userDAO.getEmployeeById(employeeId);
					//				System.out.println("CallMgtService,searchServiceReport,serviceReports, employeeId: "+employeeId);
					firstName = employee.getFirstName();
					lastName = employee.getLastName();
					if(searchKeyWord.equals("")){
						searchKeyTemp = firstName +" "+ lastName;
					}else{
						searchKeyTemp = searchKeyWord ;
					}
				}else{
					firstName = searchKeyWord;
					lastName = searchKeyWord;
					searchKeyTemp = searchKeyWord ;
				}

				if(!gurenteePeriod.equals("all")){
					if(searchKeyWord.equals("")){
						searchKeyTemp = "all";
					}else{
						searchKeyTemp = searchKeyWord ;
					}
				}

				serviceReports = callManagementDAO.searchServiceReport(frDate, toDate, searchKeyTemp, 
						gurenteePeriod,status);
				Hashtable<Integer, ServiceAllotConnector> serAllotMap = new Hashtable<Integer, ServiceAllotConnector>();
				List<ServiceAllotConnector> serviceAllotConnectors = callManagementDAO.getServiceAllotList();
				for(ServiceAllotConnector sa: serviceAllotConnectors){
					serAllotMap.put(sa.getSrId(), sa);
				}

				Hashtable<Integer, ServiceReport> serviceTable = new Hashtable<Integer, ServiceReport>();
				for(ServiceReport sp: serviceReports){
					// Some old-un necessary values in the table, so its filtering again to opt out
					// in-active(isActive = 0) "CallDetails"  related service report
					if(serAllotMap.containsKey(sp.getSrId())){
						serviceTable.put(sp.getSrId(), sp);
					}
				}
				List<CallDetail> callDetails = new ArrayList<CallDetail>();
				if(callDetails.size() >0){
					for(CallDetail cd: callDetails){
						//					System.out.println("CallMgtService,searchServiceReport,serviceReports,getCdId: "+cd.getCdId());
						for(ServiceAllotConnector sa: serviceAllotConnectors){

							if(cd.getCdId()== sa.getServiceReport().getSr_cd_id()){
								serviceTable.put(sa.getSrId(), sa.getServiceReport());
							}
						}// for(ServiceAllotConnector sa: serviceAllotConnectors)
					}//for(CallDetail cd: callDetails)
				}//if(callDetails.size() >0){
				if(!firstName.isEmpty()){
					//				System.out.println("CallMgtServiceImpl,searchServiceReport,firstName: "+firstName+", lastName: "+lastName);
					eList = callManagementDAO.searchEmployeesByName(firstName,lastName,"");
				}
				if(eList.size() >0){
					Hashtable<Integer, Employee> empTable = new Hashtable<Integer, Employee>();
					for(Employee e: eList){
						empTable.put(e.getEmployeeId(), e);
					}
					for(ServiceAllotConnector sa: serviceAllotConnectors){
						List<AllottedEmployees> allottedEmployeesList = callManagementDAO.
								getAllottedWorkDetailsByalId(sa.getAlId());
						for(AllottedEmployees ae: allottedEmployeesList){
							if(empTable.containsKey(ae.getEmployeeId())){
								ServiceReport serviceReport = callManagementDAO.getServiceReportById(sa.getSrId());
								if(serviceReport.getEmployeeId() == ae.getEmployeeId()){
									serviceTable.put(sa.getSrId(), serviceReport);
									//								System.out.println("CallMgtServiceImpl, searchServiceReport,serviceTable,srId: "
									//										+ ""+sa.getSrId()+", getEmployeeId: "+ae.getEmployeeId()+", AllotedEmpoyeesId: "+ae.getAllotedEmpoyeesId()
									//										+",Serv empid: "+serviceReport.getEmployeeId());
								}
							}
						}//for(AllottedEmployees ae: allottedEmployeesList)
					}//for(ServiceAllotConnector sa: serviceAllotConnectors)

				}//if(eList.size() >0)

				updatedServiceReports = new ArrayList<ServiceReport>(serviceTable.values());
				for(ServiceReport sr: updatedServiceReports){
					List<EmployeeMinData> emMinDatas = new ArrayList<EmployeeMinData>();
					ServiceAllotConnector serviceAllotConnector = callManagementDAO.getServiceAllotConnectorBySrId(sr.getSrId());
					List<AllottedEmployees> allottedEmployees = callManagementDAO.
							getAllottedWorkDetailsByalId(serviceAllotConnector.getAlId());
					for(AllottedEmployees ae: allottedEmployees){
						Employee emp = ae.getEmployee();
						EmployeeMinData minData = userService.getEmployeeMinData(emp);
						emMinDatas.add(minData);
					}
					sr.setEmployeeMinDataList(emMinDatas);
				}

				//*  Sorting based on service created date

				Collections.sort(updatedServiceReports, new Comparator<ServiceReport>() {
					public int compare(ServiceReport o1, ServiceReport o2) {
						return o1.getCreatedAt().compareTo(o2.getCreatedAt());
					}
				});
				Collections.reverse(updatedServiceReports);
				for(int i=0;i<updatedServiceReports.size();i++){
					updatedServiceReports.get(i).getEmployee().setViewPass(null);
					updatedServiceReports.get(i).getEmployee().setUserName(null);
					updatedServiceReports.get(i).getEmployee().setPasswordHash(null);
					updatedServiceReports.get(i).setSlNo(i+1);
					if(updatedServiceReports.get(i).getMinutesFilePath()!= null){
						updatedServiceReports.get(i).setFilePath(Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+updatedServiceReports.get(i).getSrId()+"\\"+
								updatedServiceReports.get(i).getMinutesFilePath());
					}
					// Adding alId for Edit, Allot purpose
					ServiceAllotConnector sa = serAllotMap.get(updatedServiceReports.get(i).getSrId());
					updatedServiceReports.get(i).setAlId(sa.getAlId());
					Allot allot = callManagementDAO.getAllotByAlId(sa.getAlId());
					updatedServiceReports.get(i).setAlAllotNo(allot.getAlAllotNo());
				}

				List<ServiceReportModel> serviceReportModels = new ArrayList<ServiceReportModel>();
				for(int k=0;k < updatedServiceReports.size() ; k++){
					ServiceReportModel model = createServiceReportModel(updatedServiceReports.get(k));
					model.setSlNo(k+1);
					serviceReportModels.add(model);
				}
				serviceReportModelList.setServiceReportModels(serviceReportModels);
				serviceReportModelList.setTotalCount(serviceReportModels.size());
			}// else (employeeId > 0)
		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchServiceReportForUI: "+e.getMessage());
		}
		return serviceReportModelList;
	}

	@Override
	public ServiceReportModelList getAllServiceReport(int pageNo,int pageCount) {
		ServiceReportModelList serviceReportModelList = null;
		try{
			List<ServiceReportModel> serviceModelList = new ArrayList<ServiceReportModel>();
			serviceReportModelList = callManagementDAO.getAllServiceReport(pageNo, pageCount) ;
			List<ServiceReport> serviceReportEnitiyList = serviceReportModelList.getServiceReportList();
			for(int i=0;i<serviceReportEnitiyList.size();i++){
				ServiceReportModel model = createServiceReportModel(serviceReportEnitiyList.get(i));
				model.setSlNo(i+1);
				serviceModelList.add(model);
			}
			serviceReportModelList.setServiceReportList(null);
			serviceReportModelList.setServiceReportModels(serviceModelList);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllServiceReport: "+e.getMessage());
		}
		return serviceReportModelList;
	}


	@Override
	public List<CallDetail> searchWorkAllotDetails(String dateFrom,
			String dateTo, String searchKeyWord, String gurenteePeriod) {
		List<CallDetail> updatedCallDetails = null;
		try{

			/**
			 *  Date search based on "AllotDate"
			 */
			Date frDate = utilService.getDateFromString(dateFrom);
			// Changing the format to Thu Jun 25 00:00:00 IST 2020,
			frDate = utilService.fromDateStartFromZeroHrs(frDate);

			Date toDate = utilService.getDateFromString(dateTo);
			//Changing the format to Thu Jun 25 23:59:59 IST 2020
			toDate = utilService.toDateEndToLastMin(toDate);
			//			System.out.println("CallMgtServiceImpl, searchWorkAllotDetails,frDate: "+frDate+", toDate: "+toDate);

			List<CallDetail> callDetails = callManagementDAO.searchWorkAllotDetails(frDate, toDate, searchKeyWord, gurenteePeriod);


			Map<Integer, CallDetail> map = new HashMap<>(); 
			for (CallDetail cd : callDetails) { 
				map.put(cd.getCdId(), cd); 
			} 
			/**
			 *  Searching First name in Employee table
			 */
			if(!searchKeyWord.isEmpty()){
				List<Employee> empList = callManagementDAO.searchEmployeesByName(searchKeyWord,searchKeyWord,searchKeyWord);
				for(int i=0;i<empList.size();i++){
					List<AllottedEmployees> alEmployeesList= callManagementDAO.getAllottedWorkDetailsByEmpId(empList.get(i).getEmployeeId());
					for(AllottedEmployees ae: alEmployeesList){
						if(!map.containsKey(ae.getCdId())){
							CallDetail callDetail = callManagementDAO.getCallDetailById(ae.getCdId());
							map.put(ae.getCdId(), callDetail);
						}
					}
				}
			}
			updatedCallDetails = new ArrayList<CallDetail>(map.values());
			// For update date, reverse comparison 
			//			 Comparator c = Collections.reverseOrder(new CallDetailUpdateDateSorting());
			//			Comparator c = Collections.reverseOrder(callDetailUpdateDateSortingFactory.createCallDetailUpdateDateSorting()); 
			//			Collections.sort(updatedCallDetails, c);

			/**
			 *  Sorting based on Call Updated date
			 */
			Collections.sort(updatedCallDetails, new Comparator<CallDetail>() {
				public int compare(CallDetail o1, CallDetail o2) {
					return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
				}
			});
			Collections.reverse(updatedCallDetails);

			for(int i=0;i<updatedCallDetails.size();i++){
				updatedCallDetails.get(i).setSlNo(i+1);
				//Only for UI purpose
				//				updatedCallDetails.get(i).setUiStatus("IN PROGRESS");
				if(updatedCallDetails.get(i).getCdStatus().equals("on going")){
					updatedCallDetails.get(i).setUiStatus("IN PROGRESS");
				}else{
					updatedCallDetails.get(i).setUiStatus(updatedCallDetails.get(i).getCdStatus());
				}
				List<Allot> allots = callManagementDAO.getAllotByCdId(updatedCallDetails.get(i).getCdId());
				/**
				 * 	Taking the 0th value of allots, because its came in the descending order
				 *  Also need the last the allotment, then it will be the 'on going' / latest status
				 */
				if(allots.size() > 0){
					List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(allots.get(0).getAlId());
					List<EmployeeMinData> employeeMinDatas = new ArrayList<EmployeeMinData>();
					for(AllottedEmployees ae: allottedEmployees){
						Employee employee = ae.getEmployee();
						EmployeeMinData employeeMinData = employeeMinDataFactory.createEmployeeMinData();
						employeeMinData.setEmpCode(employee.getEmpCode());
						employeeMinData.setEmployeeId(employee.getEmployeeId());
						employeeMinData.setFirstName(employee.getFirstName());
						employeeMinData.setLastName(employee.getLastName());
						employeeMinDatas.add(employeeMinData);
					}
					updatedCallDetails.get(i).setEmployeeList(employeeMinDatas);
					updatedCallDetails.get(i).setAlId(allots.get(0).getAlId());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchWorkAllotDetails: "+e.getMessage());
		}
		return updatedCallDetails;
	}

	/**
	 *  Work Details (In Progress) Excel Report
	 */
	@Override
	public XSSFWorkbook getWorkDetailsExcelReport(ServletContext context, String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod){
		XSSFWorkbook workbook = null;
		//		org.apache.poi.ss.usermodel.Workbook workbook = null;
		try{
			//			String reportTitle = "IN PROGRESS LIST";
			List<CallDetail> callDetails = searchWorkAllotDetails(dateFrom, dateTo, searchKeyWord, gurenteePeriod);
			//			String searchingCriteriaStr = "Searching Criteria - " 
			//					+" Date From: " + dateFrom +
			//					", Date To: " + dateTo+
			//					", Search : " + searchKeyWord.trim().toUpperCase()+
			//					", Gurentee Period: "+gurenteePeriod.toUpperCase();
			String filePath = context.getRealPath("/WEB-INF/views/excelReport/inProgressList.xlsx");
			//			System.out.println("Bussiness,getCallDetailsReport,filePath: "+filePath);
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			//			workbook = WorkbookFactory.create(file);

			XSSFSheet sheet = workbook.getSheetAt(0);
			//			org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
			XSSFRow row = null;

			sheet.createFreezePane(0, 1); // Freeze 1st Row   sheet.createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow)
			int rowId = 5;
			for (int i=0; i< callDetails.size();i++) {
				List<Allot> allots = callManagementDAO.getAllotByCdId(callDetails.get(i).getCdId());
				int rowNo = rowId++;
				row = sheet.createRow(rowNo);

				XSSFCell cell0 = row.createCell(0);
				cell0.setCellStyle( getCellStyle(rowNo, workbook) );
				cell0.setCellValue(i+1);

				XSSFCell cell1 = row.createCell(1);
				cell1.setCellStyle( getCellStyle(rowNo, workbook) );
				cell1.setCellValue(utilService.getTimeStampToString(callDetails.get(i).getCreatedAt()));

				XSSFCell cell2 = row.createCell(2);
				cell2.setCellStyle( getCellStyle(rowNo, workbook) );
				if(callDetails.get(i).getCdAllotNo() == null){
					cell2.setCellValue("");
				}else{
					cell2.setCellValue(callDetails.get(i).getCdAllotNo());
				}
				XSSFCell cell3 = row.createCell(3);
				cell3.setCellStyle( getCellStyle(rowNo, workbook) );
				if(callDetails.get(i).getCdAllotDate() == null){
					cell3.setCellValue("");
				}else{
					cell3.setCellValue(utilService.getDateToString(callDetails.get(i).getCdAllotDate()));
				}

				XSSFCell cell4 = row.createCell(4);
				cell4.setCellStyle( getCellStyle(rowNo, workbook) );
				cell4.setCellValue(callDetails.get(i).getCdCustomerName());

				XSSFCell cell5 = row.createCell(5);
				cell5.setCellStyle( getCellStyle(rowNo, workbook) );
				cell5.setCellValue(callDetails.get(i).getSiteDetails());

				XSSFCell cell6 = row.createCell(6);
				cell6.setCellStyle( getCellStyle(rowNo, workbook) );
				cell6.setCellValue(callDetails.get(i).getNatureOfJobs().getJobNature());

				String employeeNames = "";
				for(Allot allot: allots){
					List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(allot.getAlId());
					for(AllottedEmployees ae: allottedEmployees){
						Employee employee = userDAO.getEmployeeById(ae.getEmployeeId());
						employeeNames = employeeNames  +employee.getFirstName()+" "+employee.getLastName() + ", ";
					}
				}
				// Employee Names
				XSSFCell cell7 = row.createCell(7);
				cell7.setCellStyle( getCellStyle(rowNo, workbook) );
				cell7.setCellValue(employeeNames);
			}//for (int i=0; i< callDetails.size();i++)
			sheet.setAutoFilter(CellRangeAddress.valueOf("A5:H5"));
			fis.close();

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getWorkDetailsExcelReport: "+e.getMessage());
		}
		return workbook;
	}


	@Override
	public ServiceReport getServiceReportById(int srId,String realPath) {
		ServiceReport serviceReport = null;
		try{
			System.out.println("CallMgtImpl,getServiceReportById,srId: "+srId);
			String fileLocation = Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+srId;
			String[] arrOfStr = realPath.split(Iconstants.BUILD_NAME, 2); 
			String path = arrOfStr[0]+fileLocation;
			serviceReport = callManagementDAO.getServiceReportById(srId);
			serviceReport.setFilePath(path);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceReportById: "+e.getMessage());
		}
		return serviceReport;
	}


	@Override
	public List<RelayDetails> getAllRelayDetails() {
		List<RelayDetails> relayDetails = null;
		try{
			relayDetails = callManagementDAO.getAllRelayDetails();
			for(int i=0;i<relayDetails.size();i++){
				relayDetails.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllRelayDetails: "+e.getMessage());
		}
		return relayDetails;
	}

	@Override
	public List<PanelDetails> getAllPanelDetails() {
		List<PanelDetails> panelDetails = null;
		try{
			panelDetails = callManagementDAO.getAllPanelDetails();
			for(int i=0;i<panelDetails.size();i++){
				panelDetails.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllPanelDetails: "+e.getMessage());
		}
		return panelDetails;
	}

	@Override
	public RelayDetails saveOrUpdateRelayDetails(RelayDetails relayDetails) {
		RelayDetails rDetails = null;
		try{
			relayDetails.setIsActive(1);
			rDetails = callManagementDAO.saveOrUpdateRelayDetails(relayDetails);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateRelayDetails: "+e.getMessage());
		}
		return rDetails;
	}

	@Override
	public PanelDetails saveOrUpdatePanelDetails(PanelDetails panelDetails) {
		PanelDetails pDetails = null;
		try{
			panelDetails.setIsActive(1);
			System.out.println("CallMgtBussiness, saveOrUpdatePanelDetails,panel name: "+panelDetails.getPanelName());
			pDetails = callManagementDAO.saveOrUpdatePanelDetails(panelDetails);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdatePanelDetails: "+e.getMessage());
		}
		return pDetails;
	}

	@Override
	public List<CallDetail> getAllCallDetails() {
		return callManagementDAO.getAllCallDetailsTemp();
	}

	@Override
	public List<CallDetail> createAllotTemp() {
		List<CallDetail> callDetails = callManagementDAO.createAllotTemp();
		for(int i=0;i<callDetails.size();i++){
			//		for(int i=0;i<1;i++){
			Allot allot = new Allot();
			CallDetail callDetail = callDetails.get(i);
			allot.setAlAllotNo(callDetail.getCdAllotNo());
			allot.setAlEmpId(1);
			allot.setIsNew(0);
			allot.setIsActive(1);
			allot.setCreatedAt(new Timestamp( utilService.getTodaysDate().getTime()));
			//			allot.setCdId(callDetail.getCdId());
			//			Allot alt = callManagementDAO.saveAllotTemp(allot);
			//			callDetail.setAlId(alt.getAlId());
			callDetail = callManagementDAO.saveOrUpdateCallDetails(callDetail);
			//			System.out.println("CallDetails,cd id: "+callDetail.getCdId()+", Al-id: "+callDetail.getAlId()+", alt id: "+alt.getAlId());
			//			callDetail.setAlId(alt.getAlId());
			//			this.sessionFactory.getCurrentSession().saveOrUpdate(callDetail);
		}
		return callDetails;
	}

	@Override
	public CustomerDetails saveOrUpdateCusotmerDetails(
			CustomerDetails customerDetails) {
		CustomerDetails cDetails = null;
		try{
			customerDetails.setIsActive(1);
			cDetails = callManagementDAO.saveOrUpdateCusotmerDetails(customerDetails);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateCusotmerDetails: "+e.getMessage());
		}
		return cDetails;
	}

	//	@Override
	//	public CustomerDetails deleCustomerDetails(int customerId){
	//		CustomerDetails customerDetails = null;
	//		try{
	//			customerDetails = callManagementDAO.getCustomerDetailsById(customerId);
	//			customerDetails.setIsActive(0);
	//			customerDetails = callManagementDAO.saveOrUpdateCusotmerDetails(customerDetails);
	//			
	//			/**
	//			 *  Making audit logs
	//			 */
	//			Employee employee = userService.getEmployeeByToken(token);
	//			AuditLog auditLog = auditLogFactory.createAuditLog();
	//			AuditJson auditJson = auditJsonFactory.createAuditJson();
	//			auditLog.setEmployeeId(employee.getEmployeeId());
	//			auditJson.setActionType("delete");
	//			auditJson.setType("ObservationBeforeMaintanence");
	//			auditJson.setId(Integer.toString(obervationId));
	//			auditJson.setRemarks(oMaintanence.getObervationDetails());
	//
	//			ObjectMapper mapper = new ObjectMapper();
	//			String jSon = mapper.writeValueAsString(auditJson);
	//
	//			//saving as jSon
	//			auditLog.setAuditLog(jSon);
	//			auditLog = utilService.saveOrUpdateAuditLog(auditLog);
	//
	//		}catch(Exception e){
	//			e.printStackTrace();
	//			logger.error("saveOrUpdateCusotmerDetails: "+e.getMessage());
	//		}
	//		return customerDetails;
	//	}

	@Override
	public List<CustomerDetails> getAllCustomerDetails() {

		List<CustomerDetails> customerDetails = null;
		try{
			customerDetails = callManagementDAO.getAllCustomerDetails();
			for(int i=0;i<customerDetails.size();i++){
				customerDetails.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllCustomerDetails: "+e.getMessage());
		}
		return customerDetails;
	}

	@Override
	public CustomerDetails getCustomerDetailsById(int customerId) {
		return callManagementDAO.getCustomerDetailsById(customerId);
	}

	@Override
	public BoardDivisionDetails saveOrUpdateBoardDivisionDetails(
			BoardDivisionDetails boardDivisionDetails) {
		BoardDivisionDetails bDivisionDetails = null;
		try{
			boardDivisionDetails.setIsActive(1);
			bDivisionDetails = callManagementDAO.saveOrUpdateBoardDivisionDetails(boardDivisionDetails);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateBoardDivisionDetails: "+e.getMessage());
		}
		return bDivisionDetails;
	}

	@Override
	public List<BoardDivisionDetails> getAllBoardDivisionDetails() {
		List<BoardDivisionDetails> boardDivisionDetails = null;
		try{
			boardDivisionDetails = callManagementDAO.getAllBoardDivisionDetails();
			for(int i=0;i<boardDivisionDetails.size();i++){
				boardDivisionDetails.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllBoardDivisionDetails: "+e.getMessage());
		}
		return boardDivisionDetails;
	}

	@Override
	public BoardDivisionDetails getBoardDivisionDetailsById(int boardDivisionId) {
		return callManagementDAO.getBoardDivisionDetailsById(boardDivisionId);
	}

	/**
	 *   Allot employees to work
	 */
	@Override
	public int saveOrUpdateAllottedEmployees(ServletContext servletContext,Object object) {
		int status = 0;
		List<AllottedEmployees> allottedEmployeeList = null;
		try{
			allottedEmployeeList = new ArrayList<AllottedEmployees>();
			LinkedHashMap<String, String> lhm = (LinkedHashMap<String, String>) object;
			LinkedHashMap<String, String> objAllotT=null;
			// Generating a Set of entries
			Set set = lhm.entrySet();
			// Displaying elements of LinkedHashMap
			Iterator iterator = set.iterator();
			while(iterator.hasNext()) {
				Map.Entry me = (Map.Entry)iterator.next();
				//	            System.out.print("Key is: "+ me.getKey() + 
				//	                    "& Value is: "+me.getValue()+"\n");
				objAllotT = (LinkedHashMap<String, String>) me.getValue();
			}
			String alId = String.valueOf(objAllotT.get("alId"));
			System.out.println("saveOrUpdateAllottedEmployees,alId : "+alId);
			List<LinkedHashMap<String, LinkedHashMap<String, String>>> list = new ArrayList(objAllotT.values());
			List Objlist = (List) list.get(1);

			// Updating the allot list
			if(!alId.equals("null")){
				Allot allot = callManagementDAO.getAllottedWorkById(Integer.parseInt(alId));

				List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(Integer.parseInt(alId));
				//				String cdId = String.valueOf(objAllotT.get("cdId"));
				System.out.println("saveOrUpdateAllottedEmployees,alId : "+alId+ " ,cdId:  "+allot.getCdId());
				allot.setUpdatedAt(utilService.dateToTimeStamp(utilService.getCurrentDateAndTime()));
				HashMap<Integer, AllottedEmployees> updatedEmpMap = new HashMap<Integer, AllottedEmployees>();
				for(int i=0;i<Objlist.size();i++){
					/**
					 *  {employeeId=6, firstName=MANIKANDAN}
					 *  Taking "employeeId" -> 6, only
					 */
					int iend = Objlist.get(i).toString().indexOf(",");
					String employeeId = Objlist.get(i).toString().substring(12, iend);


					AllottedEmployees altedEmployees = allottedEmployeesFactory.createAllottedEmployees();

					//				System.out.println("CallManagementImpl,saveOrUpdateAllottedEmployees,data: "+Objlist.get(i));
					//					int end = Objlist.get(i).toString().indexOf(",");
					//					String empId = Objlist.get(i).toString().substring(12, end);
					//				String id = Objlist.get(i).toString().substring(12, 13); 
					//				EmployeeAllotTemp alt =  mapper.readValue(allotTemps.get(i).toString(), EmployeeAllotTemp.class);
					//				System.out.println("CallManagementImpl,saveOrUpdateAllottedEmployees,id: "+id);
					altedEmployees.setEmployeeId(Integer.parseInt(employeeId));
					altedEmployees.setAlId(allot.getAlId());
					altedEmployees.setCdId(allot.getCdId());
					// For UI Purpose
					allot.setViewAlert(1);
					allottedEmployeeList.add(altedEmployees);
					updatedEmpMap.put(Integer.parseInt(employeeId), altedEmployees);

				}

				for(AllottedEmployees ae: allottedEmployees){
					if(updatedEmpMap.containsKey(ae.getEmployeeId())){
						updatedEmpMap.remove(ae.getEmployeeId());
						System.out.println("saveOrUpdateAllottedEmployees,removed employeeId : "+ae.getEmployeeId());
					}else{
						System.out.println("saveOrUpdateAllottedEmployees,deleted employeeId : "+ae.getEmployeeId());
						int value = callManagementDAO.deleteAllottedEmployee(ae.getAllotedEmpoyeesId());
					}
				}
				List<AllottedEmployees> updatedAllotList = new ArrayList(updatedEmpMap.values());

				for(int i=0;i<updatedAllotList.size();i++){
					System.out.println("CallManagementImpl,update saveOrUpdateAllottedEmployees,emp id: "
							+allottedEmployeeList.get(i).getEmployeeId());
					callManagementDAO.saveOrUpdateAllottedEmployees(updatedAllotList.get(i));
				}

				CallDetail callDetail = callManagementDAO.getCallDetailById(allot.getCdId());
				String dateStr = String.valueOf(objAllotT.get("allotDate"));
				System.out.println("CallManagementImpl,update saveOrUpdateAllottedEmployees,dateStr: "
						+dateStr);
				String mob = String.valueOf(objAllotT.get("mobileNumber"));
				Date date = utilService.getDateFromString(dateStr);
				callDetail.setCdAllotDate(date);
				callDetail.setUpdatedAt(utilService.dateToTimeStamp(utilService.getCurrentDateAndTime()));
				callDetail.setWorkPhNo(mob);
				callDetail = callManagementDAO.saveOrUpdateCallDetails(callDetail);
				// For UI Purpose,Updating UI view status (BOLD)
				allot.setViewAlert(1);
				allot.setAllotDate(date);
				allot = callManagementDAO.saveOrUpdateAllot(allot);

				/**
				 *  Email to Employees for call allotment notifications

				String content = "New call has alloted . Allo No - "+allot.getAlAllotNo()
						+" "+"Please note the call details";
				MailModel mailModel = mailModelFactory.createMailModel();
				mailModel.setSubject("Alind Relays / Call Allotment Details");
				mailModel.setCallDetail(callDetail);
				mailModel.setHtmlFileName("callAllotToEmployee");
				mailModel.setContent(content);
				for(AllottedEmployees ae: updatedAllotList){
					int empId = ae.getEmployeeId();
					Employee employee = userDAO.getEmployeeById(empId);
					mailModel.setEmployee(employee);
					mailModel.setTo(employee.getEmailId());
					utilService.sendMessage(mailModel);
				}*/

			}
			// New employee allotment
			else{

				Allot allot = allotFactory.createAllot();

				//			allot.setCdId(allotTemp.getCallMngtAllotId());
				Allot lastAllot = callManagementDAO.getLastAllotRecord();
				String alotmentNo= utilService.createAllotNumber(lastAllot.getAlAllotNo());
				allot.setAlAllotNo(alotmentNo);
				allot.setAlEmpId(1);
				allot.setCreatedAt(utilService.dateToTimeStamp(utilService.getCurrentDateAndTime()));
				allot.setUpdatedAt(utilService.dateToTimeStamp(utilService.getCurrentDateAndTime()));

				// For UI Purpose
				allot.setViewAlert(1);
				String cdId = String.valueOf(objAllotT.get("cdId"));
				String mob = String.valueOf(objAllotT.get("mobileNumber"));
				String dateStr = objAllotT.get("allotDate");
				Date date = utilService.getDateFromString(dateStr);
				System.out.println("CallManagementImpl,saveOrUpdateAllottedEmployees,cdId: "+cdId
						+",mob: "+mob+", dateStr: "+dateStr);
				allot.setCdId(Integer.parseInt(cdId));
				// 1 -> New, its only for the UI View Status for Employee Session, 
				allot.setCallStatusId(1);
				allot.setAllotDate(date);
				Allot allot1 = callManagementDAO.saveAllotTemp(allot);			

				CallDetail callDetail = callManagementDAO.getCallDetailById(Integer.parseInt(cdId));
				// Changing status from "new" to "on going", after the allotment
				callDetail.setCdStatus("on going");
				callDetail.setWorkPhNo(mob);
				//				callDetail.setCdAllotNo(allot.getAlAllotNo());
				callDetail.setCdAllotNo(allot1.getAlAllotNo());
				callDetail.setUpdatedAt(utilService.dateToTimeStamp(utilService.getCurrentDateAndTime()));

				callDetail.setCdAllotDate(date);
				//				System.out.println("CallManagementImpl,saveOrUpdateAllottedEmployees,getAlId: "+allot1.getAlId()+
				//						",AllotNo: "+allot1.getAlAllotNo()+", allotted date: "+date);

				callDetail = callManagementDAO.saveOrUpdateCallDetails(callDetail);
				callDetail.setAllotDateStr(utilService.getDateToString(callDetail.getCdAllotDate()));

				for(int i=0;i<Objlist.size();i++){
					AllottedEmployees allottedEmployees = allottedEmployeesFactory.createAllottedEmployees();

					//				System.out.println("CallManagementImpl,saveOrUpdateAllottedEmployees,data: "+Objlist.get(i));
					int iend = Objlist.get(i).toString().indexOf(",");
					String employeeId = Objlist.get(i).toString().substring(12, iend);
					//				String id = Objlist.get(i).toString().substring(12, 13); 
					//				EmployeeAllotTemp alt =  mapper.readValue(allotTemps.get(i).toString(), EmployeeAllotTemp.class);
					//				System.out.println("CallManagementImpl,saveOrUpdateAllottedEmployees,id: "+id);
					allottedEmployees.setEmployeeId(Integer.parseInt(employeeId));
					allottedEmployees.setAlId(allot.getAlId());
					allottedEmployees.setCdId(Integer.parseInt(cdId));
					allottedEmployeeList.add(allottedEmployees);
					//				System.out.println("CallManagementImpl,saveOrUpdateAllottedEmployees,emp id: "+allottedEmployees.getEmployeeId());
				}

				//			System.out.println("CallManagementImpl,saveOrUpdateAllottedEmployees,getCallMngtAllotId: "+allotTemp.getCallMngtAllotId());
				//			List<EmployeeAllotTemp> employees = allotTemp.getEmployees();
				//			System.out.println("CallManagementImpl,saveOrUpdateAllottedEmployees,employees,size: "+employees.size());
				/****/
				for(int i=0;i<allottedEmployeeList.size();i++){
					System.out.println("CallManagementImpl,saveOrUpdateAllottedEmployees,emp id: "
							+allottedEmployeeList.get(i).getEmployeeId());
					callManagementDAO.saveOrUpdateAllottedEmployees(allottedEmployeeList.get(i));
				}

				/**
				 *  Email to Employees for call allotment notifications
				 */
				String content = "New call has alloted . Allot No - "+allot.getAlAllotNo();
				MailModel mailModel = mailModelFactory.createMailModel();

				mailModel.setCallDetail(callDetail);
				mailModel.setHtmlFileName("callAllotToEmployee");
				mailModel.setContent(content);
				List<Employee> empMailList = new ArrayList<Employee>();
				int slNo =0;
				for(AllottedEmployees ae: allottedEmployeeList){
					int empId = ae.getEmployeeId();
					Employee employee = userDAO.getEmployeeById(empId);
					employee.setSlNo(slNo);
					empMailList.add(employee);
					mailModel.setSubject("Call Allotment Details /"+" Emp Code: "+employee.getEmpCode()+ " / Allot No. - "+allot.getAlAllotNo());
					mailModel.setEmployee(employee);
					mailModel.setTo(employee.getEmailId());
					utilService.sendEmail(servletContext,mailModel);
					slNo++;
					// Creating a delay while sending eMail to more than one employee
					Thread.sleep(3000);
				}
				/**
				 *  Email to customer, regarding the allocation of Employees

				MailModel mailModelToCustomer = mailModelFactory.createMailModel();
				mailModelToCustomer.setSubject("Alind Relays / Engineers' Allotment /"+ "SR ID - "+callDetail.getServiceRequestId());
				mailModelToCustomer.setCallDetail(callDetail);
				mailModelToCustomer.setHtmlFileName("allocationOfEngineersToCustomer");
				// For getting Employee,the leader to provide the mobile number to customer
				mailModelToCustomer.setEmployee(empMailList.get(0));
				if(empMailList.size() > 1){
					empMailList.remove(0);
					mailModelToCustomer.setEmpList(empMailList);
				}
				utilService.sendEmail(servletContext,mailModelToCustomer);
				 */

			}//else if(alId != null)
			status = 1;



		}catch(Exception e){
			status = 0;
			e.printStackTrace();
			logger.error("saveOrUpdateAllottedEmployees: "+e.getMessage());
		}
		return status;
	}

	@Override
	public ServiceReport saveOrUpdateServiceReport(ServiceReport serviceReport,
			MultipartFile mintesOfMeeting,String contextPath,String token) {
		ServiceReport sReport = null;
		String originalFileName = null;
		String fileName = null;
		String fileLocation = null;
		int status = -1;
		try{
			Employee employee = userService.getEmployeeByToken(token);
			if(mintesOfMeeting != null){
				originalFileName = mintesOfMeeting.getOriginalFilename();
				fileName = utilService.createFileName(mintesOfMeeting.getOriginalFilename());
			}
			System.out.println("Business,saveOrUpdateServiceReport,cd id: "+serviceReport.getSr_cd_id()+
					",ProductSlNo: "+serviceReport.getProductSlNo()+",JobNature: "+serviceReport.getJobNature());
			//			System.out.println("Business,saveOrUpdateServiceReport,cd id: "+serviceReport.getSr_cd_id()+
			//					",Observation details: "+serviceReport.getObervationDetails()+",NaturalOfService: "+serviceReport.getSrNaturalOfService());
			CallDetail callDetail = callManagementDAO.getCallDetailById(serviceReport.getSr_cd_id());
			//            ServiceAllotConnector serviceAllotConnector = callManagementDAO.getServiceAllotConnectorBySrId(serviceReport.getSrId());
			Allot allot = callManagementDAO.getAlloDetailsfromAllotNumber(callDetail.getCdAllotNo());
			Date today = utilService.getCurrentDateAndTime();
			Timestamp todayTimeStamp = utilService.dateToTimeStamp(utilService.getTodaysDate());
			serviceReport.setCreatedAt(utilService.dateToTimeStamp(today));
			serviceReport.setUpdatedAt(utilService.dateToTimeStamp(today));
			serviceReport.setOrginalFileName(originalFileName);
			serviceReport.setMinutesFilePath(fileName);
			serviceReport.setSrNotificationStatus("unviewed");
			serviceReport.setAlId(allot.getAlId());

			// Review for Guarantee Period 
			serviceReport.setReviewStatus(1);
			// For Fresh View purpose
			serviceReport.setViewAlert(1);
			serviceReport.setIsActive(1);
			serviceReport.setEmployeeId(employee.getEmployeeId());

			if(serviceReport.getSrCallStatus().equals("1")){
				serviceReport.setSrCallStatus("completed");
				// 3 -> completed
				allot.setCallStatusId(3);
				callDetail.setCdStatus("completed");
			}else{
				serviceReport.setSrCallStatus("not completed");
				//4 -> pending
				allot.setCallStatusId(4);
				callDetail.setCdStatus("pending");
			}

			//			System.out.println("Business,saveOrUpdateServiceReport,sReport Emp id: "+serviceReport.getEmployeeId());
			sReport = callManagementDAO.saveOrUpdateServiceReport(serviceReport);
			callDetail.setUpdatedAt(todayTimeStamp);
			callDetail=callManagementDAO.saveOrUpdateCallDetails(callDetail);
			ServiceAllotConnector serviceAllotConnector=serviceAllotConnectorFactory.createServiceAllotConnector();
			//			Allot allot = callManagementDAO.getAlloDetailsfromAllotNumber(callDetail.getCdAllotNo());
			serviceAllotConnector.setAlId(allot.getAlId());
			serviceAllotConnector.setSrId(sReport.getSrId());
			serviceAllotConnector = callManagementDAO.saveOrUpdateServiceAllotConnector(serviceAllotConnector);
			// Updating CallStatus in Allot
			allot = callManagementDAO.saveOrUpdateAllot(allot);

			//File saving
			if(mintesOfMeeting != null){
				fileLocation = Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+sReport.getSrId();
				/**
				 *  File details saving
				 */
				status = utilService.saveFile(mintesOfMeeting, contextPath, fileLocation);
				//				System.out.println("Business,saveOrUpdateServiceReport,contextPath: "+sReport.getSrId()+
				//											", ServiceAllotId id: "+serviceAllotConnector.getServiceAllotId()+",Emp id: "+sReport.getEmployeeId());
				ServiceFile serviceFile = serviceFileFactory.createServiceFile();
				serviceFile.setSrId(sReport.getAlId());
				serviceFile.setFileName(fileName);
				serviceFile.setOrginalFileName(originalFileName);
				String extension = FilenameUtils.getExtension(mintesOfMeeting.getOriginalFilename());
				serviceFile.setFileType(extension);
				serviceFile.setCreatedAt(utilService.dateToTimeStamp(today));
				serviceFile = callManagementDAO.saveServiceFile(serviceFile);
				System.out.println("CallMgtBusiness,saveOrUpdateServiceReport, serviceFile, ServiceFileId: "+serviceFile.getServiceFileId());
			}
			//			System.out.println("Business,saveOrUpdateServiceReport,sReport id: "+sReport.getSrId()+
			//					", ServiceAllotId id: "+serviceAllotConnector.getServiceAllotId()+",Emp id: "+sReport.getEmployeeId());
			if(status < 0){
				sReport = null;
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateServiceReport: "+e.getMessage());
		}
		//		System.out.println("Business,saveOrUpdateServiceReport,sReport id: "+sReport.getSrId());
		return sReport;
	}

	/**
	 *   Employee Work details by employee Id
	 */
	@Override
	public List<Allot> getAllottedWorkDetailsByEmpId(String searchKeyword,int employeeId,String dateFrom,String dateTo ){
		List<AllottedEmployees> allottedWorks = null;
		List<Allot> allotList = null;
		try{
			allotList = new ArrayList<Allot>();
			//			List<AllottedEmployees> allottedEmployees = new ArrayList<AllottedEmployees>();
			//			List<AllottedEmployees> allottedWorksOrg = callManagementDAO.getAllottedWorkDetailsByEmpId(employeeId);
			//			Date dateFr = utilService.getDateFromString(dateFrom);
			//			Date dateT = utilService.getDateFromString(dateTo);

			/**
			 *  Date search based on "AllotDate"
			 */
			Date frDate = utilService.getDateFromString(dateFrom);
			// Changing the format to Thu Jun 25 00:00:00 IST 2020,
			frDate = utilService.fromDateStartFromZeroHrs(frDate);

			Date toDate = utilService.getDateFromString(dateTo);
			//Changing the format to Thu Jun 25 23:59:59 IST 2020
			toDate = utilService.toDateEndToLastMin(toDate);
			//			System.out.println("CallMgtServiceImpl, getAllottedWorkDetailsByEmpId,frDate: "+frDate+", toDate: "+toDate);

			allottedWorks = callManagementDAO.searchAllottedEmployeesById(frDate, toDate, searchKeyword, employeeId);
			//			System.out.println("CallMgtImpl,getAllottedWorkDetailsByEmpId,allottedWorks,size: "+allottedWorks.size()+
			//					", Org size: "+allottedWorksOrg.size());
			for(int i=0;i<allottedWorks.size();i++){
				//				System.out.println("CallMgtImpl,getAllottedWorkDetailsByEmpId,AllotedEmpoyeesId:"
				//						+ ""+allottedWorks.get(i).getAllotedEmpoyeesId());
				Allot allot = callManagementDAO.getAllottedWorkById(allottedWorks.get(i).getAlId());
				//				System.out.println("CallMgtImpl,getAllottedWorkDetailsByEmpId,AllotedEmpoyeesId:"
				//						+ ""+allottedWorks.get(i).getAllotedEmpoyeesId()+", alId: "+allot.getAlId());
				ServiceAllotConnector serviceAllotConnector = callManagementDAO.getServiceAllotConnectorByAlId(allottedWorks.get(i).getAlId());
				//				System.out.println("CallMgtImpl,getAllottedWorkDetailsByEmpId,alId: "+allot.getAlId()+", "+", CdStatus: "+allot.getCallDetail().getCdStatus());
				if(allot.getCallDetail().getCdStatus().equals("on going")){
					allot.getCallDetail().setUiStatus("IN PROGRESS");
					//					allot.setUiStatus("IN PROGRESS");
					allot.setUiStatus("NEW");
				}else if(allot.getCallDetail().getCdStatus().equals("pending")){
					//					System.out.println("CallMgtImpl,getAllottedWorkDetailsByEmpId,alId: "+allot.getAlId()+", "+", CdStatus: "+allot.getCallDetail().getCdStatus());
					allot.getCallDetail().setUiStatus("NOT COMPLETED");
					allot.setUiStatus("NOT COMPLETED");
				}
				else{
					allot.getCallDetail().setUiStatus(allot.getCallDetail().getCdStatus());
					//					System.out.println("CallMgtImpl,getAllottedWorkDetailsByEmpId,alId: "+allot.getAlId()+", "+", CdStatus: "+allot.getCallDetail().getCdStatus());
					allot.setUiStatus(allot.getCallStatus().getStatus().toUpperCase());
					//					System.out.println("CallMgtImpl,getAllottedWorkDetailsByEmpId,alId: "+allot.getAlId()+", "+", CdStatus: "+allot.getCallDetail().getCdStatus());
				}
				/**
				if(allot.getCallStatus().getStatus().equals("on going")){
					allot.setUiStatus("IN PROGRESS");
				}else if(allot.getCallStatus().getStatus().equals("pending")){
					allot.setUiStatus("NOT COMPLETED");
				}else{
					allot.setUiStatus(allot.getCallStatus().getStatus().toUpperCase());
				}**/
				//Testing
				/**
				AllottedEmployees alEmployees = allottedWorks.get(i);
				alEmployees.getEmployee().setViewPass(null);

				alEmployees.getAllot().setCallDetail(null);
				alEmployees.setEmployee(null);
				alEmployees.setAllot(null);
				allottedEmployees.add(null);
				 **/

				//				allot.setSlNo(allottedWorks.size() - i);
				if(serviceAllotConnector != null){
					//					System.out.println("CallMgtImpl,getAllottedWorkDetailsByEmpId,srId: "
					//				+serviceAllotConnector.getSrId()+", Alid: "+allot.getAlId());
					ServiceReport serviceReport = callManagementDAO.getServiceReportById(serviceAllotConnector.getSrId());
					allot.setOrginalFileName(serviceReport.getOrginalFileName());
					allot.setFilePath(Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+serviceReport.getSrId()+"\\"+
							serviceReport.getMinutesFilePath());
				}
				allotList.add(allot);
			}//for(int i=0;i<allottedWorks.size();i++)

			/**
			 *  Sorting based on Allotment created date
			 */
			Collections.sort(allotList, new Comparator<Allot>() {
				public int compare(Allot o1, Allot o2) {
					return o1.getCreatedAt().compareTo(o2.getCreatedAt());
				}
			});
			Collections.reverse(allotList);
			for(int i=0;i<allotList.size();i++){
				allotList.get(i).setSlNo(i+1);
			}
			System.out.println("CallMgtImpl,getAllottedWorkDetailsByEmpId,allotList size:"+allotList.size());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllottedWorkDetailsByEmpId: "+e.getMessage());
		}

		return allotList;
	}

	@Override
	public XSSFWorkbook getAllottedWorkDetailsReportByEmpId(ServletContext context,String searchKeyword,int employeeId,
			String dateFrom,String dateTo){
		XSSFWorkbook workbook = null;
		try{
			List<Allot> allotList = getAllottedWorkDetailsByEmpId(searchKeyword, employeeId, dateFrom, dateTo);
			Map<Integer,CallDetail> callDetailsMap = new Hashtable<Integer, CallDetail>();
			for(Allot alt:allotList){
				CallDetail callDetail = alt.getCallDetail();
				callDetailsMap.put(callDetail.getCdId(), callDetail);
			}
			List<CallDetail> callDetails = new ArrayList<CallDetail>(callDetailsMap.values());
			/**
			 *  Sorting based on created date
			 */
			Collections.sort(callDetails, new Comparator<CallDetail>() {
				public int compare(CallDetail o1, CallDetail o2) {
					return o1.getCreatedAt().compareTo(o2.getCreatedAt());
				}
			});
			Collections.reverse(callDetails);

			String filePath = context.getRealPath("/WEB-INF/views/excelReport/employeeWorkReport.xlsx");
			//			System.out.println("Bussiness,getCallDetailsReport,filePath: "+filePath);
			File myFile = new File(filePath);
			FileInputStream fis = new FileInputStream(myFile);
			workbook = new XSSFWorkbook(fis);

			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow row = null;

			sheet.createFreezePane(0, 1); // Freeze 1st Row   sheet.createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow)
			int rowId = 5;
			for (int i=0; i< callDetails.size();i++) {
				List<ServiceReport> serviceReports = callManagementDAO.getServiceReportByCdId(callDetails.get(i).getCdId());
				List<Allot> allots = callManagementDAO.getAllotByCdId(callDetails.get(i).getCdId());
				int rowNo = rowId++;
				row = sheet.createRow(rowNo);
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellStyle( getCellStyle(rowNo, workbook) );
				cell0.setCellValue(i+1);
				//				row.createCell(0).setCellValue(i+1);

				XSSFCell cell1 = row.createCell(1);
				cell1.setCellStyle( getCellStyle(rowNo, workbook) );
				cell1.setCellValue(utilService.getTimeStampToString(callDetails.get(i).getCreatedAt()));
				//				row.createCell(1).setCellValue(utilService.getTimeStampToString(callDetails.get(i).getCreatedAt()));

				XSSFCell cell2 = row.createCell(2);
				cell2.setCellStyle( getCellStyle(rowNo, workbook) );
				if(callDetails.get(i).getCdAllotNo() == null){
					//					row.createCell(2).setCellValue("");
					cell2.setCellValue("");
				}else{
					//					row.createCell(2).setCellValue(callDetails.get(i).getCdAllotNo());
					cell2.setCellValue(callDetails.get(i).getCdAllotNo());
				}
				XSSFCell cell3 = row.createCell(3);
				// Color settings
				//				cell3.setCellStyle( rowNo % 2 == 0 ? oddRowStyle : evenRowStyle );
				cell3.setCellStyle( getCellStyle(rowNo, workbook) );
				cell3.setCellValue(callDetails.get(i).getCdCustomerName());

				XSSFCell cell4 = row.createCell(4);
				cell4.setCellStyle( getCellStyle(rowNo, workbook) );
				cell4.setCellValue(callDetails.get(i).getServiceRequestId());
				//				cell4.setCellValue(callDetails.get(i).getCdBoardDivision());

				XSSFCell cell5 = row.createCell(5);
				cell5.setCellStyle( getCellStyle(rowNo, workbook) );
				cell5.setCellValue(callDetails.get(i).getCdBoardDivision());
				//				cell5.setCellValue(callDetails.get(i).getSiteDetails());

				XSSFCell cell6 = row.createCell(6);
				cell6.setCellStyle( getCellStyle(rowNo, workbook) );
				cell6.setCellValue(callDetails.get(i).getSiteDetails());
				//				cell6.setCellValue(callDetails.get(i).getCdGuranteePeriod());

				XSSFCell cell7 = row.createCell(7);
				cell7.setCellStyle( getCellStyle(rowNo, workbook) );
				//				cell7.setCellValue(callDetails.get(i).getProductDetails());
				cell7.setCellValue(callDetails.get(i).getCdGuranteePeriod());

				XSSFCell cell8 = row.createCell(8);
				cell8.setCellStyle( getCellStyle(rowNo, workbook) );
				cell8.setCellValue(callDetails.get(i).getProductDetails());
				//				cell8.setCellValue(callDetails.get(i).getProductSlNo());

				XSSFCell cell9 = row.createCell(9);
				cell9.setCellStyle( getCellStyle(rowNo, workbook) );
				cell9.setCellValue(callDetails.get(i).getProductSlNo());
				//				cell9.setCellValue(callDetails.get(i).getNatureOfJobs().getJobNature());

				XSSFCell cell10 = row.createCell(10);
				cell10.setCellStyle( getCellStyle(rowNo, workbook) );
				cell10.setCellValue(callDetails.get(i).getNatureOfJobs().getJobNature());
				//				cell10.setCellValue(callDetails.get(i).getCdProblemDetails());

				//REPORTED PROBLEM
				XSSFCell cell11 = row.createCell(11);
				cell11.setCellStyle( getCellStyle(rowNo, workbook) );
				cell11.setCellValue(callDetails.get(i).getRemarks());
				//				cell11.setCellValue(callDetails.get(i).getCdProblemDetails());

				String callAttendedDate = "";
				String callClosedDate = "";
				String obserVationStr = "";
				String employeeNames = "";
				String natureOfServiceUnderTaken = "";
				for(ServiceReport sp: serviceReports){
					callAttendedDate = callAttendedDate + utilService.getDateToString(sp.getSrCallAttendDate())+ ", ";
					if(sp.getSrCallClosedDate() != null){
						callClosedDate = callClosedDate + utilService.getDateToString(sp.getSrCallClosedDate())+ ", ";
					}
					//					obserVationStr = obserVationStr + sp.getObservationDetails()+ ", ";
					obserVationStr = obserVationStr + sp.getObervationDetails()+ ", ";
					natureOfServiceUnderTaken = sp.getSrNaturalOfService();
				}
				for(Allot allot: allots){
					List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(allot.getAlId());
					for(AllottedEmployees ae: allottedEmployees){
						Employee employee = userDAO.getEmployeeById(ae.getEmployeeId());
						employeeNames = employeeNames  +employee.getFirstName()+" "+employee.getLastName() + ", ";
					}
				}
				//Observation before maintenance 
				XSSFCell cell12 = row.createCell(12);
				cell12.setCellStyle( getCellStyle(rowNo, workbook) );
				cell12.setCellValue(obserVationStr);

				XSSFCell cell13 = row.createCell(13);
				cell13.setCellStyle( getCellStyle(rowNo, workbook) );
				cell13.setCellValue(natureOfServiceUnderTaken);
				//				cell12.setCellValue(callDetails.get(i).getNatureOfJobs().getJobNature());

				// Call Attended date
				XSSFCell cell14 = row.createCell(14);
				cell14.setCellStyle( getCellStyle(rowNo, workbook) );
				cell14.setCellValue(""+callAttendedDate);
				//				row.createCell(12).setCellValue(""+callAttendedDate);

				// call Closed Date
				XSSFCell cell15 = row.createCell(15);
				cell15.setCellStyle( getCellStyle(rowNo, workbook) );
				cell15.setCellValue(""+callClosedDate);
				//				row.createCell(13).setCellValue(""+callClosedDate);

				// Employee Names
				XSSFCell cell16 = row.createCell(16);
				cell16.setCellStyle( getCellStyle(rowNo, workbook) );
				cell16.setCellValue(employeeNames);
				//				row.createCell(14).setCellValue(employeeNames);

				XSSFCell cell17 = row.createCell(17);
				cell17.setCellStyle( getCellStyle(rowNo, workbook) );

				//B'ze DB status is on going,pending etc
				if(callDetails.get(i).getCdStatus().equals("on going")){
					cell17.setCellValue("IN PROGRESS");
				}else if(callDetails.get(i).getCdStatus().equals("pending")){
					cell17.setCellValue("NOT COMPLETED");
				}else{
					cell17.setCellValue(callDetails.get(i).getCdStatus().toUpperCase());
				}

				//				cell17.setCellValue(callDetails.get(i).getCdStatus());

				XSSFCell cell18 = row.createCell(18);
				cell18.setCellStyle( getCellStyle(rowNo, workbook) );
				cell18.setCellValue(callDetails.get(i).getRemarks());
				//				row.createCell(16).setCellValue(callDetails.get(i).getRemarks());
			}
			sheet.setAutoFilter(CellRangeAddress.valueOf("A5:S5"));
			fis.close();

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllottedWorkDetailsReportByEmpId: "+e.getMessage());
		}
		return workbook;
	}

	@Override
	public Allot getAllottedWorkById(int alId){
		Allot allot = null;
		try{
			allot = callManagementDAO.getAllottedWorkById(alId);
			List<AllottedEmployees> allEmployees = callManagementDAO.getAllottedWorkDetailsByalId(alId);
			for(int i=0;i<allEmployees.size();i++){
				allEmployees.get(i).getEmployee().setViewPass(null);
			}
			allot.setAllottedEmployees(allEmployees);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllottedWorkById: "+e.getMessage());
		}

		return allot;
	}

	/** 
	 *  MOM / Minutes Of Meeting PDF report
	 */

	@Override
	public ByteArrayInputStream getMinutesOfMeetingPDFReport(ServletContext servletContext,int alId){

		String imageHeaderPath = servletContext.getRealPath("/WEB-INF/views/images/Alind_Banner.png");
		//String signaturePath = servletContext.getRealPath("/WEB-INF/views/images/emailSignature.jpg");
		ByteArrayInputStream arrayInputStream = null;
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int maxLineLenght = 70;

		try {
			Allot allot = getAllottedWorkById(alId);
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();
			// Add Text to PDF file ->
			Font minutesFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.BLACK);
			Font subHeadFont = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 12, BaseColor.BLACK);
			Font normalFont = FontFactory.getFont(FontFactory.TIMES, 10, BaseColor.BLACK);
			//			Font dbDataFont = FontFactory.getFont(FontFactory.TIMES_ITALIC, 9, BaseColor.BLACK);
			//			CMYKColor blueColor = new CMYKColor(85, 99, 23, 36);
			//			CMYKColor blueColor = new CMYKColor(70, 36, 0, 4);
			CMYKColor blueColor = new CMYKColor(72, 54, 0, 0);

			Image image = Image.getInstance(imageHeaderPath);
			image.setAlignment(Element.ALIGN_RIGHT);
			image.setAbsolutePosition(45, 750);
			image.scalePercent(45f, 45f);
			writer.getDirectContent().addImage(image, true);
			document.add(Chunk.NEWLINE);

			PdfContentByte canvas = writer.getDirectContent();
			Phrase minutesPhrase = new Phrase("MINUTES OF MEETING", minutesFont);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, minutesPhrase, 260, 720, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, minutesPhrase, 280, 720, 0);
			// Customer details
			Phrase customerPhrase = new Phrase("CUSTOMER DETAILS", subHeadFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, customerPhrase, 50, 680, 0);
			// Line
			canvas.setColorStroke(blueColor);
			canvas.moveTo(50, 670);
			canvas.lineTo(550, 670);
			canvas.closePathStroke();
			document.add(Chunk.NEWLINE);

			Phrase customerNamePhrase = new Phrase("NAME", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, customerNamePhrase, 60, 650, 0);
			Phrase colunPhrase = new Phrase(":", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 650, 0);
			Phrase customerNameVariablePhrase = new Phrase(allot.getCallDetail().getCdCustomerName(), normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, customerNameVariablePhrase, 225, 650, 0);

			Phrase customerContactPhrase = new Phrase("CONTACT NUMBER", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, customerContactPhrase, 60, 635, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 635, 0);
			Phrase customerContactVarPhrase = new Phrase(allot.getCallDetail().getCdContactNo(), normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, customerContactVarPhrase, 225, 635, 0);

			Phrase boardDivPhrase = new Phrase("BOARD / DIVISION", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, boardDivPhrase, 60, 620, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 620, 0);
			Phrase boardDivVarPhrase = new Phrase(allot.getCallDetail().getCdBoardDivision(), normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, boardDivVarPhrase, 225, 620, 0);

			Phrase sitePhrase = new Phrase("SITE DETAILS", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, sitePhrase, 60, 605, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 605, 0);
			Phrase siteVarPhrase = new Phrase(allot.getCallDetail().getSiteDetails(), normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, siteVarPhrase, 225, 605, 0);

			Phrase serReqIdPhrase = new Phrase("SERVICE REQUEST ID ", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, serReqIdPhrase, 60, 590, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 590, 0);
			Phrase serReqIdVarPhrase = new Phrase(allot.getCallDetail().getServiceRequestId(), normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, serReqIdVarPhrase, 225, 590, 0);

			// EMPLOYEE details
			Phrase employeeDetPhrase = new Phrase("EMPLOYEE DETAILS", subHeadFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, employeeDetPhrase, 50, 570, 0);
			// Line
			canvas.setColorStroke(blueColor);
			canvas.moveTo(50, 560);
			canvas.lineTo(550, 560);
			canvas.closePathStroke();
			document.add(Chunk.NEWLINE);

			Phrase empNamePhrase = new Phrase("NAME", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, empNamePhrase, 60, 540, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 540, 0);

			List<EmployeeMinData> employeeMinDatas = getAllottedEmployeeMinDataByAlId(alId);
			Phrase empNameVarPhrase = new Phrase(getEmpNameConcat(employeeMinDatas), normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, empNameVarPhrase, 225, 540, 0);

			Phrase empCodePhrase = new Phrase("CODE", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, empCodePhrase, 60, 525, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 525, 0);
			Phrase empCodeVarPhrase = new Phrase(getEmpCodeConcat(employeeMinDatas), normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, empCodeVarPhrase, 225, 525, 0);

			// CALL details
			Phrase callDetPhrase = new Phrase("CALL DETAILS", subHeadFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, callDetPhrase, 50, 505, 0);
			// Line
			canvas.setColorStroke(blueColor);
			canvas.moveTo(50, 495);
			canvas.lineTo(550, 495);
			canvas.closePathStroke();
			document.add(Chunk.NEWLINE);

			Phrase allotNoPhrase = new Phrase("ALLOT NO", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, allotNoPhrase, 60, 480, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 480, 0);
			Phrase allotNoVarPhrase = new Phrase(allot.getAlAllotNo(), normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, allotNoVarPhrase, 225, 480, 0);

			Phrase callAttDatePhrase = new Phrase("CALL ATTEND DATE", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, callAttDatePhrase, 60, 465, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 465, 0);

			ServiceAllotConnector serviceAllotConnector = callManagementDAO.getServiceAllotConnectorByAlId(alId);
			ServiceReport serviceReport = null;
			String callAttendDate = "";
			String closedDate = "";
			if(serviceAllotConnector != null){
				serviceReport = serviceAllotConnector.getServiceReport();
				closedDate = utilService.getDateToString(serviceReport.getSrCallClosedDate());
				callAttendDate = utilService.getDateToString(serviceReport.getSrCallAttendDate());
			}

			Phrase callAttDateVarPhrase = new Phrase(callAttendDate, normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, callAttDateVarPhrase, 225, 465, 0);

			Phrase callCloseDatePhrase = new Phrase("CALL CLOSED DATE", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, callCloseDatePhrase, 300, 465, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 400, 465, 0);
			Phrase callCloseVarDatePhrase = new Phrase(closedDate, normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, callCloseVarDatePhrase, 425, 465, 0);

			// PRODUCT details
			Phrase pdtDetPhrase = new Phrase("PRODUCT DETAILS", subHeadFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, pdtDetPhrase, 50, 445, 0);
			// Line
			canvas.setColorStroke(blueColor);
			canvas.moveTo(50, 435);
			canvas.lineTo(550, 435);
			canvas.closePathStroke();
			document.add(Chunk.NEWLINE);

			Phrase panelRelPhrase = new Phrase("PANEL / RELAY DETAILS ", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, panelRelPhrase, 60, 420, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 420, 0);
			Phrase panelRelVarPhrase = new Phrase(allot.getCallDetail().getCdRelayPanelDetails(), normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, panelRelVarPhrase, 225, 420, 0);

			Phrase serialNoPhrase = new Phrase("SERIAL NUMBER", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, serialNoPhrase, 60, 405, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 405, 0);
			if(serviceReport != null){
				Phrase serialNoVarPhrase = new Phrase(serviceReport.getProductSlNo(), normalFont);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, serialNoVarPhrase, 225, 405, 0);
			}

			Phrase repPblmPhrase = new Phrase("REPORTED PROBLEM", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, repPblmPhrase, 60, 390, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 390, 0);
			if(allot.getCallDetail().getCdProblemDetails() != null){
				List<String> pblmDetailsList = utilService.getLinebreaks(allot.getCallDetail().getCdProblemDetails().trim(),maxLineLenght);
				int k=0;
				for(int i=0; i<pblmDetailsList.size(); i++){
					Paragraph repPblmParagraph = new Paragraph();
					repPblmParagraph.setFont(normalFont);
					Chunk chunk = new Chunk(pblmDetailsList.get(i).toLowerCase());
					repPblmParagraph.add(chunk);
					repPblmParagraph.add(Chunk.NEWLINE);
					ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, repPblmParagraph,225 , 390-k, 0);
					k= k + 10;
				}
			}//if(allot.getCallDetail().getCdProblemDetails() != null)
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, repPblmParagraph, 225, 390, 0);
			//GURANTEE PERIOD
			Phrase guranteePeriod = new Phrase("GURANTEE PERIOD", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, guranteePeriod, 60, 365, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 365, 0);
			if(serviceReport != null){
				Phrase guranteePeriodStr = new Phrase(serviceReport.getCallDetail().getCdGuranteePeriod(), normalFont);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, guranteePeriodStr, 225, 365, 0);
			}

			// ACTION details
			Phrase actionPhrase = new Phrase("ACTION", subHeadFont);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, actionPhrase, 50, 370, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, actionPhrase, 50, 345, 0);
			// Line
			canvas.setColorStroke(blueColor);
			canvas.moveTo(50, 335);
			canvas.lineTo(550, 335);
			canvas.closePathStroke();
			document.add(Chunk.NEWLINE);

			Phrase obeserBfrePhrase = new Phrase("PRIMARY OBSERVATION", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, obeserBfrePhrase, 60, 320, 0);
			//ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, obeserBfrePhrase, 60, 345, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 320, 0);
			if(serviceReport != null){
				String observationStr = null;
				//				Phrase obeserBfreVarPhrase = new Phrase(serviceReport.getObservationDetails(), normalFont);
				if(serviceReport.getObervationDetails() != null){
					List<ObservationBeforeMaintanence> obs = getObservationBeforeFromJson(serviceReport.getObervationDetails());
					observationStr = getObservationBeforeMaintanenceString(obs);
					List<String> observationsList = utilService.getLinebreaks(observationStr,maxLineLenght);
					int k=0;
					for(int i=0; i<observationsList.size(); i++){
						Paragraph observationParagraph = new Paragraph();
						observationParagraph.setFont(normalFont);
						Chunk chunk = new Chunk(observationsList.get(i).toLowerCase());
						observationParagraph.add(chunk);
						observationParagraph.add(Chunk.NEWLINE);
						ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, observationParagraph,225 , 320-k, 0);
						k= k + 10;
					}
				}
				//				Phrase obeserBfreVarPhrase = new Phrase(observationStr, normalFont);
				//				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, obeserBfreVarPhrase, 225, 320, 0);
			}

			Phrase natJobPhrase = new Phrase("NATURE OF JOB", normalFont);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, natJobPhrase, 60, 260, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 260, 0);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, natJobPhrase, 60, 305, 0);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 305, 0);
			if(serviceReport != null){
				//				Phrase natJobVarPhrase = new Phrase("", normalFont);
				Phrase natJobVarPhrase = new Phrase(serviceReport.getCallDetail().getNatureOfJobs().getJobNature(), normalFont);
				//				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, natJobVarPhrase, 225, 305, 0);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, natJobVarPhrase, 225, 260, 0);
			}


			Phrase statusPhrase = new Phrase("STATUS", normalFont);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, statusPhrase, 60, 290, 0);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 290, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, statusPhrase, 60, 245, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 245, 0);
			if(serviceReport != null){
				Phrase statusVarPhrase = new Phrase(serviceReport.getSrCallStatus(), normalFont);
				//				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, statusVarPhrase, 225, 290, 0);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, statusVarPhrase, 225, 245, 0);
			}

			Phrase uploadPhrase = new Phrase("UPLOAD FILE", normalFont);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, uploadPhrase, 60, 275, 0);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 275, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, uploadPhrase, 60, 230, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 230, 0);
			if(serviceReport != null){
				Phrase uploadVarPhrase = new Phrase(serviceReport.getOrginalFileName(), normalFont);
				//				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, uploadVarPhrase, 225, 275, 0);
				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, uploadVarPhrase, 225, 230, 0);
			}

			Phrase remarksPhrase = new Phrase("REMARKS", normalFont);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, remarksPhrase, 60, 260, 0);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 260, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, remarksPhrase, 60, 215, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 215, 0);
			int h=0;
			if(serviceReport != null){
				if(serviceReport.getSrRemarks() != null){
					//					Phrase remarksVarPhrase = new Phrase(serviceReport.getSrRemarks(), normalFont);
					List<String> remarkList = utilService.getLinebreaks(serviceReport.getSrRemarks().trim(),maxLineLenght);
					for(int i=0; i<remarkList.size(); i++){
						Paragraph remarkParagraph = new Paragraph();
						remarkParagraph.setFont(normalFont);
						Chunk chunk = new Chunk(remarkList.get(i).toLowerCase());
						remarkParagraph.add(chunk);
						remarkParagraph.add(Chunk.NEWLINE);
						//						ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, remarkParagraph, 225, 260-h, 0);
						ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, remarkParagraph, 225, 215-h, 0);
						h= h + 10;
					}
				}
			}
			Phrase natSerPhrase = new Phrase("ACTIONS UNDERTAKEN", normalFont);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, natSerPhrase, 60, 200, 0);
			//			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 200, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, natSerPhrase, 60, 155, 0);
			ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, colunPhrase, 200, 155, 0);
			if(serviceReport != null){
				if(serviceReport.getSrNaturalOfService() != null){
					List<NatureOfJobs> jobList = getNatureOfJobsFromJson(serviceReport.getSrNaturalOfService());
					String serviceUnderTakenStr = getNatureOfJobsToString(jobList);
					List<String> serviceUnderTakenList = utilService.getLinebreaks(serviceUnderTakenStr,maxLineLenght);
					int g=0;
					for(int i=0; i<serviceUnderTakenList.size(); i++){
						Paragraph repPblmParagraph = new Paragraph();
						repPblmParagraph.setFont(normalFont);
						Chunk chunk = new Chunk(serviceUnderTakenList.get(i).toLowerCase());
						repPblmParagraph.add(chunk);
						repPblmParagraph.add(Chunk.NEWLINE);
						//						ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, repPblmParagraph,225 , 200-g, 0);
						ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, repPblmParagraph,225 , 155-g, 0);
						g= g + 10;
					}
				}
				//				Phrase natSerVarPhrase = new Phrase(serviceReport.getSrNaturalOfService(), normalFont);
				//				ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, natSerVarPhrase, 225, 290, 0);
			}

			document.close();
			arrayInputStream = new ByteArrayInputStream(out.toByteArray());
			System.out.println("CallMgtImpl,getMinutesOfMeetingPDFReport,size: "+out.size());

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getMinutesOfMeetingPDFReport: "+e.getMessage());
		}

		return arrayInputStream;
	}

	private List<EmployeeMinData> getAllottedEmployeeMinDataByAlId(int alId){
		List<EmployeeMinData> employees = null;
		try{
			employees = new ArrayList<EmployeeMinData>();
			List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(alId);
			for(AllottedEmployees ae: allottedEmployees){
				EmployeeMinData em = userService.getEmployeeMinData(ae.getEmployee());
				employees.add(em);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllottedEmployeeMinDataByAlId: "+e.getMessage());
		}
		return employees;
	}

	private String getEmpNameConcat(List<EmployeeMinData> employeeMinDatas){
		String names = null;
		try{
			StringBuilder str = new StringBuilder(); 
			for(EmployeeMinData emData: employeeMinDatas){
				str.append(emData.getFullName());
				str.append(",");
				str.append(" ");
			}
			names = str.toString();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("empNameConcat: "+e.getMessage());
		}
		return names;
	}

	private String getEmpCodeConcat(List<EmployeeMinData> employeeMinDatas){
		String codes = null;
		try{
			StringBuilder str = new StringBuilder(); 
			for(EmployeeMinData emData: employeeMinDatas){
				str.append(emData.getEmpCode());
				str.append(",");
				str.append(" ");
			}
			codes = str.toString();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("empCodeConcat: "+e.getMessage());
		}
		return codes;
	}


	/**
	 *  Temp
	 */
	@Override
	public int updateCdIdInAllot() {
		return callManagementDAO.updateCdIdInAllotTemp();
	}

	@Override
	public int allotEmpolyeeTemp(){
		return callManagementDAO.allotEmpolyeeTemp();
	}

	// Updating cdStatus from "processed" to "completed" 
	@Override
	public int updateCallDetailsTemp() {
		try{
			List<CallDetail> callDetails = callManagementDAO.getCallDetails(null, null, "", "","");
			//			System.out.println("CallMgtBuss, updateCallDetailsTemp, size:"+callDetails.size());
			for(int i=0;i<callDetails.size();i++){
				if(callDetails.get(i).getCdStatus().equals("processed")){
					callDetails.get(i).setCdStatus("completed");
					callManagementDAO.saveOrUpdateCallDetails(callDetails.get(i));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateCallDetailsTemp: "+e.getMessage());
		}
		return 0;
	}

	@Override
	public CallDetail updateGuranteePeriod( Object object) {
		CallDetail cDetail = null;
		try{
			LinkedHashMap<String, String> callDetailsObj = (LinkedHashMap<String, String>) object;
			LinkedHashMap<String, String> objVal = null;
			Set set = callDetailsObj.entrySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext()) {
				Map.Entry me = (Map.Entry)iterator.next();
				//				System.out.print("Key is: "+ me.getKey() + 
				//						"& Value is: "+me.getValue()+"\n");
				objVal = (LinkedHashMap<String, String>) me.getValue();
			}

			String objStr = objVal.toString();
			//			System.out.println("Business,updateGuranteePeriod,objStr: "+objStr+", objVal: "+objVal.toString());
			int cdIdEnd = objStr.indexOf(",");
			String guranteeStr = "cdGuranteePeriod=";
			String guranteePeriod = objStr.substring(objStr.lastIndexOf("cdGuranteePeriod=")+
					guranteeStr.length(), objStr.indexOf("}")); 
			String cdId = objStr.toString().substring(6, cdIdEnd);
			//			System.out.println("Business,updateGuranteePeriod,cdId: "+cdId+", guranteePeriod: "+guranteePeriod);
			cDetail = callManagementDAO.getCallDetailById(Integer.parseInt(cdId));
			cDetail.setCdGuranteePeriod(guranteePeriod);
			cDetail = callManagementDAO.saveOrUpdateCallDetails(cDetail);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateGuranteePeriod: "+e.getMessage());
		}

		return cDetail;
	}

	/**
	 *  Updating Guarantee Period / Review
	 */
	@Override
	public ServiceReport submitServiceReport(SubmitServiceReportModel submitServiceReportModel){
		ServiceReport serviceReport = null;
		try{

			ServiceReport sReport = callManagementDAO.getServiceReportById(submitServiceReportModel.getSrId());
			sReport.setReviewStatus(0);
			CallDetail cDetail = callManagementDAO.getCallDetailById(sReport.getSr_cd_id());
			cDetail.setCdGuranteePeriod(submitServiceReportModel.getGurenteePeriod());
			cDetail = callManagementDAO.saveOrUpdateCallDetails(cDetail);
			//			List<ServiceReport> serviceRepList = callManagementDAO.getServiceReportByCdId(cDetail.getCdId());
			//			for(ServiceReport sr: serviceRepList){
			//				sr.setReviewStatus(0);
			//				callManagementDAO.saveOrUpdateServiceReport(sr);
			//			}
			sReport = callManagementDAO.saveOrUpdateServiceReport(sReport);
//			System.out.println("CallManagementController,submitServiceReport,SrId: "+serviceReport.getSrId()+", CdId: "+cDetail.getCdId());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("submitServiceReport: "+e.getMessage());
		}

		return serviceReport;
	}

	/**
	 *  Temp
	 */
	@Override
	public int updateAllotedEmployeesTemp() {
		try{
			List<Allot> allots = callManagementDAO.getAllAllotDetails(-1);
			for(int i=0;i<allots.size();i++){
				List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(allots.get(i).getAlId());
				for(int j=0;j<allottedEmployees.size();j++){
					AllottedEmployees alEmployees = allottedEmployees.get(j);
					alEmployees.setCdId(allots.get(i).getCdId());
					AllottedEmployees alEmp = callManagementDAO.saveOrUpdateAllottedEmployees(alEmployees);
					System.out.println("updateAllotedEmployeesTemp, cdId: "+alEmp.getAlId());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateAllotedEmployeesTemp: "+e.getMessage());
		}
		return 0;
	}



	@Override
	public List<NatureOfJobs> getAllNatureOfJobs() {
		List<NatureOfJobs> natureOfJobs = null;
		try{
			natureOfJobs = callManagementDAO.getAllNatureOfJobs();
			for(int i=0;i<natureOfJobs.size();i++){
				natureOfJobs.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllNatureOfJobs: "+e.getMessage());
		}
		return natureOfJobs;
	}

	@Override
	public NatureOfJobs saveOrUpdateNatureOfJobs(NatureOfJobs natureOfJobs) {
		NatureOfJobs naJobs = null;
		try{
			natureOfJobs.setIsActive(1);
			naJobs = callManagementDAO.saveOrUpdateNatureOfJobs(natureOfJobs);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateNatureOfJobs: "+e.getMessage());
		}
		return naJobs;
	}

	@Override
	public NatureOfJobs getNatureOfJobsById(int natureJobId) {
		return callManagementDAO.getNatureOfJobsById(natureJobId);
	}

	/**
	 *  Checking Call - Read status
	 * @param callDetails
	 * @return
	 */
	private int getCallViewStatus(List<CallDetail> callDetails){
		int status = 0;
		for(CallDetail ca: callDetails){
			if(ca.getViewAlert() == 1){
				//				System.out.println("CallMgtImpl,getCallViewStatus,ca.getViewAlert: "+status);
				status = 1;
			}
		}
		//		System.out.println("CallMgtImpl,getCallViewStatus,status: "+status);
		return status;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	private String getCallStatus(int id){
		String callStatus = "";
		switch (id) {
		case 1:
			callStatus = "on going" ;
			break;
		case 2:
			callStatus = "completed" ;
			break;
		case 3:
			callStatus = "new" ;
			break;
		case 4:
			callStatus = "not completed" ;
			break;
		default:
			break;
		}
		return callStatus;
	}


	/**
	 *  Temp
	 */

	@Override
	public int updateServiceAllotTemp() {
		try{
			List<ServiceReport> serviceReports = callManagementDAO.
					searchServiceReport(null, null, "", "all","");
			System.out.println("updateServiceAllotTemp, serviceReports size: "+serviceReports.size());
			for(ServiceReport sr: serviceReports){
				Allot allot = callManagementDAO.getAlloDetailsfromAllotNumber(sr.getCallDetail().getCdAllotNo());
				ServiceAllotConnector serviceAllotConnector = new ServiceAllotConnector();
				if(allot != null){
					serviceAllotConnector.setAlId(allot.getAlId());
					serviceAllotConnector.setSrId(sr.getSrId());
					serviceAllotConnector = callManagementDAO.saveOrUpdateServiceAllotConnector(serviceAllotConnector);
					System.out.println("updateServiceAllotTemp, id:"+serviceAllotConnector.getServiceAllotId());
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateServiceAllotTemp: "+e.getMessage());
		}
		return 0;
	}

	@Override
	public int deleteNatureOfJobs(String token,int natureJobId) {
		int status = 0;
		try{
			NatureOfJobs natureOfJobs = callManagementDAO.getNatureOfJobsById(natureJobId);
			natureOfJobs.setIsActive(0);
			natureOfJobs = callManagementDAO.saveOrUpdateNatureOfJobs(natureOfJobs);
			status = 1;

			/**
			 *  Making audit logs
			 */
			Employee employee = userService.getEmployeeByToken(token);
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(employee.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("NatureOfJobs");
			auditJson.setId(Integer.toString(natureJobId));
			auditJson.setRemarks(natureOfJobs.getJobNature());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteNatureOfJobs: "+e.getMessage());
			status = 0;
		}
		return status;
	}

	@Override
	public List<ObservationBeforeMaintanence> getAllObservationBeforeMaintanence() {
		List<ObservationBeforeMaintanence> obList = null;
		try{
			obList = callManagementDAO.getAllObservationBeforeMaintanence();
			for(int i=0;i<obList.size();i++){
				obList.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllObservationBeforeMaintanence: "+e.getMessage());
		}
		return obList;
	}

	@Override
	public ObservationBeforeMaintanence getObservationBeforeMaintanenceById(
			int obervationId) {
		ObservationBeforeMaintanence obBeforeMaintanence = null;
		try{
			obBeforeMaintanence = callManagementDAO.getObservationBeforeMaintanenceById(obervationId);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getObservationBeforeMaintanenceById: "+e.getMessage());
		}

		return obBeforeMaintanence;
	}

	@Override
	public ObservationBeforeMaintanence saveOrUpdateObervationBeforeMaintanence(
			ObservationBeforeMaintanence observationBeforeMaintanence) {
		ObservationBeforeMaintanence obBeforeMaintanence = null;
		try{
			observationBeforeMaintanence.setIsActive(1);
			System.out.println("CallMgtBusinessImpl, saveOrUpdateObervationBeforeMaintanence, ");
			obBeforeMaintanence = callManagementDAO.saveOrUpdateObervationBeforeMaintanence(observationBeforeMaintanence);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateObervationBeforeMaintanence: "+e.getMessage());
		}

		return obBeforeMaintanence;
	}

	@Override
	public int deleteObservationBeforeMaintanence(String token,int obervationId) {
		int status = 0;
		try{
			ObservationBeforeMaintanence oMaintanence = callManagementDAO.
					getObservationBeforeMaintanenceById(obervationId);
			oMaintanence.setIsActive(0);
			oMaintanence = callManagementDAO.saveOrUpdateObervationBeforeMaintanence(oMaintanence);
			status = 1;

			/**
			 *  Making audit logs
			 */
			Employee employee = userService.getEmployeeByToken(token);
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(employee.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("ObservationBeforeMaintanence");
			auditJson.setId(Integer.toString(obervationId));
			auditJson.setRemarks(oMaintanence.getObervationDetails());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteObservationBeforeMaintanence: "+e.getMessage());
			status = 0;
		}
		return status;
	}

	@Override
	public List<CustomerSiteDetails> getAllCustomerSiteDetails() {
		List<CustomerSiteDetails> customerSiteDetails = null;
		try{
			customerSiteDetails = callManagementDAO.getAllCustomerSiteDetails();
			for(int i=0;i<customerSiteDetails.size();i++){
				customerSiteDetails.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllCustomerSiteDetails: "+e.getMessage());
		}
		return customerSiteDetails;
	}

	@Override
	public CustomerSiteDetails saveOrUpdateCustomerSiteDetails(
			CustomerSiteDetails customerSiteDetails) {
		CustomerSiteDetails cSiteDetails = null;
		try{
			customerSiteDetails.setIsActive(1);
			cSiteDetails = callManagementDAO.saveOrUpdateCustomerSiteDetails(customerSiteDetails);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateCustomerSiteDetails: "+e.getMessage());
		}
		return cSiteDetails;
	}

	@Override
	public CustomerSiteDetails getCustomerSiteDetailsById(int siteId) {
		return callManagementDAO.getCustomerSiteDetailsById(siteId);
	}

	@Override
	public int deleteCustomerSiteDetailsById(String token,int siteId) {
		int  status = 0;
		try{
			CustomerSiteDetails customerSiteDetails = callManagementDAO.getCustomerSiteDetailsById(siteId);
			customerSiteDetails.setIsActive(0);
			customerSiteDetails = callManagementDAO.saveOrUpdateCustomerSiteDetails(customerSiteDetails);
			status = 1;

			/**
			 *  Making audit logs
			 */
			Employee employee = userService.getEmployeeByToken(token);
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(employee.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("CustomerSiteDetails");
			auditJson.setId(Integer.toString(siteId));
			auditJson.setRemarks(customerSiteDetails.getSiteName());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteCustomerSiteDetailsById: "+e.getMessage());
			status = 0;
		}
		return status;
	}

	@Override
	public ServiceReport getServiceReportByAlId(int alId,ServletContext context) {
		ServiceReport serviceReport = null;
		try{
			//			System.out.println("CallMgtBusinss,getServiceReportByAlId,alId: "+alId);
			ServiceAllotConnector serviceAllotConnector = callManagementDAO.getServiceAllotConnectorByAlId(alId);
			//			System.out.println("CallMgtBusinss,getServiceReportByAlId,alId: "+alId+", SrId: "+serviceAllotConnector.getSrId());
			serviceReport = callManagementDAO.getServiceReportById(serviceAllotConnector.getSrId());
			List<ServiceFile> serviceFiles = callManagementDAO.getServiceFileByServiceId(serviceReport.getSrId());

			serviceReport.getEmployee().setViewPass("");
			//			String realPath = context.getRealPath("");
			//			String fileLocation = Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+serviceReport.getMinutesFilePath();
			//			String[] arrOfStr = realPath.split(Iconstants.BUILD_NAME, 2); 
			//			String path = arrOfStr[0]+fileLocation;
			String path = Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+serviceReport.getSrId()+"\\"+
					serviceReport.getMinutesFilePath();
			//			String path = null;
			//			if(serviceFiles.size() > 0){
			//				//				String path = Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+serviceReport.getSrId()+"\\"+
			//				//						serviceReport.getMinutesFilePath();
			//				path = Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+serviceReport.getSrId()+"\\"+
			//						serviceFiles.get(0).getFileName();
			//			}

			//			allot.setOrginalFileName(serviceReport.getOrginalFileName());
			//		     allot.setFilePath(Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+serviceReport.getSrId()+"\\"+
			//		    		 serviceReport.getMinutesFilePath());
			//			serviceReport.setMinutesFilePath(realPath+"\\"+Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+serviceReport.getMinutesFilePath());
			serviceReport.setFilePath(path);
			System.out.println("CallMgtBusinss,getServiceReportByAlId,alId: "+alId+", path: "+path);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceReportByAlId: "+e.getMessage());
		}
		return serviceReport;
	}
	// Temp
	@Override
	public int updateAllotCallStatusTemp() {
		try{
			List<Allot> allots = callManagementDAO.getAllAllotDetails(-1);
			for(int i=0;i<allots.size();i++){
				CallDetail callDetail = callManagementDAO.getCallDetailById(allots.get(i).getCdId());
				if(callDetail.getCdStatus().equals("on going")){
					allots.get(i).setCallStatusId(2);
				}else if(callDetail.getCdStatus().equals("completed")){
					allots.get(i).setCallStatusId(3);
				}else if(callDetail.getCdStatus().equals("pending")){
					allots.get(i).setCallStatusId(4);
				}
				Allot allot = callManagementDAO.saveOrUpdateAllot(allots.get(i));
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceReportByAlId: "+e.getMessage());
		}
		return 0;
	}

	/**
	 *  Get Call Details & Updating UI view (Bold to lower )
	 */
	@Override
	public CallDetailModel getCallDetailByCdId(int cdId) {
		CallDetailModel callDetailModel = null;
		try{
			//			CallDetail callDetail = null;
			List<Allot> allotList = callManagementDAO.getAllotByCdId(cdId);
			List<Allot> allots = new ArrayList<Allot>();
			for(Allot allot:allotList){
				List<AllottedEmployees> altEmpList = new ArrayList<AllottedEmployees>();
				//				callDetail = allot.getCallDetail();
				allot.setCallDetail(null);
				List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(allot.getAlId());
				for(AllottedEmployees ae: allottedEmployees){
					EmployeeMinData  employeeMinData = userService.getEmployeeMinData(ae.getEmployee());
					//					ae.setAllot(null);
					//					ae.setEmployee(null);
					ae.setEmployeeMinData(employeeMinData);
					altEmpList.add(ae);
				}
				allot.setAllottedEmployees(allottedEmployees);
				allots.add(allot);
			}
			// If New, then allot size is '0'
			//			if(allots.size() > 0){
			//				callDetail.setAllots(allots);
			//			}

			CallDetail callDetailWork = callManagementDAO.getCallDetailById(cdId);
			/**
			 *  View updating
			 */
			callDetailWork.setViewAlert(0);
			callDetailWork = callManagementDAO.saveOrUpdateCallDetails(callDetailWork);
			callDetailModel = createCallDetailModel(callDetailWork);
			List<AllotModel> allotModelList = new ArrayList<AllotModel>();
			for(Allot alt : allots){
				AllotModel model = createAllotModel(alt);
				List<AllottedEmployees> altEmpList = alt.getAllottedEmployees();
				List<AllottedEmployeesModel> altEmpModelList = new ArrayList<AllottedEmployeesModel>();
				for(AllottedEmployees altEmp: altEmpList){
					AllottedEmployeesModel mod = createAllottedEmployeesModel(altEmp);
					altEmpModelList.add(mod);
				}
				model.setAllottedEmployees(altEmpModelList);
				allotModelList.add(model);
			}
			// If New, then allot size is '0'
			if(allots.size() > 0){
				callDetailModel.setAllots(allotModelList);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCallDetailByCdId: "+e.getMessage());
		}
		return callDetailModel;
	}

	//	@Override
	//	public CallDetail getCallDetailByCdId(int cdId) {
	//		CallDetail callDetail = null;
	//		try{
	//			List<Allot> allotList = callManagementDAO.getAllotByCdId(cdId);
	//			List<Allot> allots = new ArrayList<Allot>();
	//			for(Allot allot:allotList){
	//				List<AllottedEmployees> altEmpList = new ArrayList<AllottedEmployees>();
	//				callDetail = allot.getCallDetail();
	//				allot.setCallDetail(null);
	//				List<AllottedEmployees> allottedEmployees = callManagementDAO.getAllottedWorkDetailsByalId(allot.getAlId());
	//				for(AllottedEmployees ae: allottedEmployees){
	//					EmployeeMinData  employeeMinData = userService.getEmployeeMinData(ae.getEmployee());
	//					ae.setAllot(null);
	//					ae.setEmployee(null);
	//					ae.setEmployeeMinData(employeeMinData);
	//					altEmpList.add(ae);
	//				}
	//				allot.setAllottedEmployees(allottedEmployees);
	//				allots.add(allot);
	//			}
	//			// If New, then allot size is '0'
	//			if(allots.size() > 0){
	//				callDetail.setAllots(allots);
	//			}
	//			/**
	//			 *  View updating
	//			 */
	//			CallDetail callDetailWork = callManagementDAO.getCallDetailById(cdId);
	//			callDetailWork.setViewAlert(0);
	//			callDetailWork = callManagementDAO.saveOrUpdateCallDetails(callDetailWork);
	//		}catch(Exception e){
	//			e.printStackTrace();
	//			logger.error("getCallDetailByCdId: "+e.getMessage());
	//		}
	//		return callDetail;
	//	}



	/**
	 *  Uploaded report to byte 
	 */
	@Override
	public ServiceReport getUploadedServiceReportBySrId(ServletContext context,int srId) {
		ServiceReport serviceReport = null;
		try{
			byte[] byteData = null;
			String filePath = null;
			serviceReport = callManagementDAO.getServiceReportById(srId);
			if(serviceReport.getMinutesFilePath() != null){
				String realPath = context.getRealPath("");
				String[] arrOfStr = realPath.split(Iconstants.BUILD_NAME, 2); 
				//				System.out.println("CallMgtImpl,getUploadedServiceReportBySrId,realPath: "+realPath);
				filePath = arrOfStr[0]+Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION
						+srId+"\\"+serviceReport.getMinutesFilePath();
				//			String fileLocation = Iconstants.SERVICE_REPORT_MINUTES_FILE_LOCATION+sReport.getSrId();
				File file = new File(filePath);
				//			 System.out.println("CallMgtImpl,getUploadedServiceReportBySrId,file lenght: "+file.length());
				//init array with file length
				byte[] bFile = Files.readAllBytes(new File(filePath).toPath());
				serviceReport.setUploadedRepByte(bFile);
				String fileType = utilService.getFileExtension(serviceReport.getMinutesFilePath());
				if(fileType.equals("jpg")){
					serviceReport.setFileType("image/jpg");
				}else if(fileType.equals("jpeg")){
					serviceReport.setFileType("image/jpeg");
				}else if(fileType.equals("pdf")){
					serviceReport.setFileType("application/pdf");
				}else if(fileType.equals("png")){
					serviceReport.setFileType("image/png");
				}
			}
			//			System.out.println("CallMgtImpl,getUploadedServiceReportBySrId,filePath: "+filePath);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getUploadedServiceReportBySrId: "+e.getMessage());
		}
		return serviceReport;
	}

	/**
	 *  Allots based on call status - > on going, not completed, completed
	 */
	@Override
	public List<Allot> getAllotListByCallStatusId(String token, int callStatusId) {
		List<Allot> allots = null;
		try{
			allots = new ArrayList<Allot>();
			Employee employee = userService.getEmployeeByToken(token);
			List<AllottedEmployees> allottedEmployees = callManagementDAO.
					getAllottedWorkDetailsByEmpId(employee.getEmployeeId());
			for(AllottedEmployees alEmployees:allottedEmployees){
				if(alEmployees.getAllot().getCallStatusId() == callStatusId){
					allots.add(alEmployees.getAllot());
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllotListByCallStatusId: "+e.getMessage());
		}
		return allots;
	}

	//// Updating alId  in service report
	@Override
	public int updateAlIdInServiceReportTemp() {
		int status = 0;
		try{

			List<ServiceAllotConnector> serviceAllotConnectors = callManagementDAO.getServiceAllotList();
			for(ServiceAllotConnector sConnector: serviceAllotConnectors){
				ServiceReport serviceReport = callManagementDAO.getServiceReportById(sConnector.getSrId());
				//				ServiceReport serviceReport = sConnector.getServiceReport();
				serviceReport.setAlId(sConnector.getAlId());
				ServiceReport sr = callManagementDAO.saveOrUpdateServiceReport(serviceReport);
				//				System.out.println("updateAlIdInServiceReport, alid: ");
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateAlIdInServiceReport: "+e.getMessage());
		}
		return status;
	}

	@Override
	public int deleteRelayDetails(String token,int relayId) {
		int status = 0;
		try{
			RelayDetails relayDetails = callManagementDAO.getRelayDetailsById(relayId);
			relayDetails.setIsActive(0);
			relayDetails = callManagementDAO.saveOrUpdateRelayDetails(relayDetails);
			status = 1;

			/**
			 *  Making audit logs
			 */
			Employee employee = userService.getEmployeeByToken(token);
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(employee.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("RelayDetails");
			auditJson.setId(Integer.toString(relayId));
			auditJson.setRemarks(relayDetails.getRelayName());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteRelayDetails: "+e.getMessage());
			status = 0;
		}
		return status;
	}

	@Override
	public int deletePanelDetails(String token,int panelId) {
		int status = 0;
		try{
			PanelDetails panelDetails = callManagementDAO.getPanelDetailsById(panelId);
			panelDetails.setIsActive(0);
			panelDetails = callManagementDAO.saveOrUpdatePanelDetails(panelDetails);
			status = 1;

			/**
			 *  Making audit logs
			 */
			Employee employee = userService.getEmployeeByToken(token);
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(employee.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("PanelDetails");
			auditJson.setId(Integer.toString(panelId));
			auditJson.setRemarks(panelDetails.getPanelName());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deletePanelDetails: "+e.getMessage());
			status = 0;
		}
		return status;
	}

	@Override
	public int deleteCustomerDetails(String token,int customerId) {
		int status = 0;
		try{
			CustomerDetails customerDetails = callManagementDAO.getCustomerDetailsById(customerId);
			customerDetails.setIsActive(0);
			customerDetails = callManagementDAO.saveOrUpdateCusotmerDetails(customerDetails);

			/**
			 *  Making audit logs
			 */
			Employee employee = userService.getEmployeeByToken(token);
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(employee.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("CustomerDetails");
			auditJson.setId(Integer.toString(customerId));
			auditJson.setRemarks(customerDetails.getCustomerName());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);

			status = 1;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteCustomerDetails: "+e.getMessage());
			status = 0;
		}
		return status;
	}

	@Override
	public int deleteBoardDivisionDetails(String token,int boardDivisionId) {
		int status = 0;
		try{
			BoardDivisionDetails boardDivisionDetails = callManagementDAO.getBoardDivisionDetailsById(boardDivisionId);
			boardDivisionDetails.setIsActive(0);
			boardDivisionDetails = callManagementDAO.saveOrUpdateBoardDivisionDetails(boardDivisionDetails);
			status = 1;

			/**
			 *  Making audit logs
			 */
			Employee employee = userService.getEmployeeByToken(token);
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(employee.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("BoardDivisionDetails");
			auditJson.setId(Integer.toString(boardDivisionId));
			auditJson.setRemarks(boardDivisionDetails.getZoneDivisionName());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteBoardDivisionDetails: "+e.getMessage());
			status = 0;
		}
		return status;
	}

	/**
	 *   Json to ObservationBeforeMaintanence
	 * @param observJson
	 * @return
	 */
	private List<ObservationBeforeMaintanence> getObservationBeforeFromJson( String observJson){
		List<ObservationBeforeMaintanence> oBeforeMaintanences = null;
		try{
			ObjectMapper mapper = new ObjectMapper();
			ObservationBeforeMaintanence[] obs = mapper.readValue(observJson, ObservationBeforeMaintanence[].class);
			oBeforeMaintanences = Arrays.asList(obs);
			//			System.out.println("CallMgtImpl,getObservationBeforeFromJson, size: "+oBeforeMaintanences.size());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getObservationBeforeFromJson: "+e.getMessage());
		}
		return oBeforeMaintanences;
	}

	/**
	 * ObservationBeforeMaintanence to String, for PDF report
	 * @param obList
	 * @return
	 */
	private String getObservationBeforeMaintanenceString(List<ObservationBeforeMaintanence> obList){
		String str = "";
		int k = 1;
		try{
			for(int i=0;i<obList.size();i++){
				str = str + obList.get(i).getObervationDetails();
				if(k != obList.size()){
					str =  str + ", ";
				}
				k++;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getObservationBeforeMaintanenceString: "+e.getMessage());
		}
		//		return str.toLowerCase();
		return str;
	}

	/**
	 *  NatureOfJobs from Json
	 * @param jobJson
	 * @return
	 */
	private List<NatureOfJobs> getNatureOfJobsFromJson(String jobJson){
		List<NatureOfJobs> natureOfJobs = null;
		try{
			ObjectMapper mapper = new ObjectMapper();
			NatureOfJobs[] jobs = mapper.readValue(jobJson, NatureOfJobs[].class);
			natureOfJobs = Arrays.asList(jobs);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNatureOfJobsFromJson: "+e.getMessage());
		}
		return natureOfJobs;
	}

	/**
	 *    NatureOfJobs To String, its for PDF report
	 * @param jobs
	 * @return
	 */
	private String getNatureOfJobsToString(List<NatureOfJobs> jobs){
		String str = "";
		int k = 1;
		try{
			for(int i=0;i<jobs.size();i++){
				str = str + jobs.get(i).getJobNature();
				if(k != jobs.size()){
					str =  str + ", ";
				}
				k++;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNatureOfJobsToString: "+e.getMessage());
		}
		return str;
	}


	private CallDetail createCallDetailEntity(CallDetailModel model){
		CallDetail entity = null;
		try{
			entity = new CallDetail();

			entity.setAlId(entity.getAlId());
			//			model.setAllots(allots);
			if(model.getBoardDivisionId() == null){
			}else{
				entity.setBoardDivisionId(model.getBoardDivisionId());
			}
			entity.setCdAllotDate(utilService.stringToDate(model.getCdAllotDate()));
			entity.setCdAllotNo(model.getCdAllotNo());
			entity.setCdBoardDivision(model.getCdBoardDivision());
			entity.setCdContactNo(model.getCdContactNo());
			entity.setCdCustomerName(model.getCdCustomerName());
			entity.setCdEmail(model.getCdEmail());
			entity.setCdGuranteePeriod(model.getCdGuranteePeriod());
			entity.setCdId(model.getCdId());
			entity.setCdProblemDetails(model.getCdProblemDetails());
			entity.setCdRelayPanelDetails(model.getCdRelayPanelDetails());
			entity.setCdStatus(model.getCdStatus());
			entity.setCreatedAt(utilService.stringDateToTimestamp(model.getCreatedAt()));
			entity.setCustomerId(model.getCustomerId());
			entity.setCdCustomerName(model.getCdCustomerName());
			entity.setIsActive(model.getIsActive());
			entity.setNatureJobId(model.getNatureJobId());
			entity.setPanelId(model.getPanelId());
			entity.setProductDetails(model.getProductDetails());
			entity.setProductSlNo(model.getProductSlNo());
			entity.setRelayId(model.getRelayId());
			entity.setRemarks(model.getRemarks());
			entity.setServiceRequestId(model.getServiceRequestId());
			entity.setSiteDetails(model.getSiteDetails());
			entity.setUiStatus(model.getUiStatus());
			entity.setUpdatedAt(utilService.stringDateToTimestamp(model.getUpdatedAt()));
			entity.setViewAlert(model.getViewAlert());
			entity.setWorkPhNo(model.getWorkPhNo());

		}catch(Exception e){
			e.printStackTrace();
			logger.error("createCallDetailEntity: "+e.getMessage());
		}
		return entity;
	}

	private CallDetailModel createCallDetailModel(CallDetail entity){
		CallDetailModel model = null;
		try{
			model = new CallDetailModel();
			model.setAlId(entity.getAlId());
			//			model.setAllots(allots);
			model.setBoardDivisionId(entity.getBoardDivisionId());
			if(entity.getCdAllotDate() != null){
				model.setCdAllotDate(utilService.getDateToString(entity.getCdAllotDate()));
			}
			model.setCdAllotNo(entity.getCdAllotNo());
			model.setCdBoardDivision(entity.getCdBoardDivision());
			model.setCdContactNo(entity.getCdContactNo());
			model.setCdCustomerName(entity.getCdCustomerName());
			model.setCdEmail(entity.getCdEmail());
			model.setCdGuranteePeriod(entity.getCdGuranteePeriod());
			model.setCdId(entity.getCdId());
			model.setCdProblemDetails(entity.getCdProblemDetails());
			model.setCdRelayPanelDetails(entity.getCdRelayPanelDetails());
			model.setCdStatus(entity.getCdStatus());
			if(entity.getCreatedAt() != null){
				model.setCreatedAt(utilService.getDateToString(entity.getCreatedAt()));
			}
			model.setCustomerId(entity.getCustomerId());
			model.setCdCustomerName(entity.getCdCustomerName());
			model.setIsActive(entity.getIsActive());
			//			System.out.println("createCallDetailModel,NatureOfJobs,"+entity.getNatureOfJobs());
			model.setJobNature(entity.getNatureOfJobs().getJobNature());
			model.setNatureJobId(entity.getNatureOfJobs().getNatureJobId());
			model.setPanelId(entity.getPanelId());
			model.setProductDetails(entity.getProductDetails());
			model.setProductSlNo(entity.getProductSlNo());
			model.setRelayId(entity.getRelayId());
			model.setRemarks(entity.getRemarks());
			model.setServiceRequestId(entity.getServiceRequestId());
			model.setSiteDetails(entity.getSiteDetails());
			model.setUiStatus(entity.getUiStatus());
			if(entity.getUpdatedAt() != null){
				model.setUpdatedAt(utilService.getDateToString(entity.getUpdatedAt()));
			}
			model.setViewAlert(entity.getViewAlert());
			model.setWorkPhNo(entity.getWorkPhNo());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNatureOfJobsToString: "+e.getMessage());
		}
		return model;
	}

	private AllotModel createAllotModel(Allot entity){
		AllotModel model = null;
		try{
			model = new AllotModel();
			model.setAlAllotNo(entity.getAlAllotNo());
			model.setAlEmpId(entity.getAlEmpId());
			model.setAlId(entity.getAlId());
			if(entity.getAllotDate() !=  null){
				model.setAllotDate(utilService.getDateToString(entity.getAllotDate()));
			}
			//			if(entity.getAllotDate() ==  null){
			//
			//			}else{
			//				model.setAllotDate(utilService.getDateToString(entity.getAllotDate()));
			//			}
			//model.setAllottedEmployees(allottedEmployees);
			model.setCallStatus(entity.getCallStatus().getStatus());
			model.setCallStatusId(entity.getCallStatusId());
			//			model.setCdBoardDivision(entity.getCallDetail().getCdBoardDivision());
			//model.setCdContactNo(entity.getCallDetail().getCdContactNo());
			//			model.setCdCustomerName(entity.getCallDetail().getCdCustomerName());
			//			model.setCdId(entity.getCdId());
			model.setCreatedAt(utilService.getDateToString(entity.getCreatedAt()));
			model.setFilePath(entity.getFilePath());
			model.setIsActive(entity.getIsActive());
			model.setOrginalFileName(entity.getOrginalFileName());
			model.setSlNo(entity.getSlNo());
			model.setUiStatus(entity.getUiStatus());
			model.setUpdatedAt(utilService.getDateToString(entity.getUpdatedAt()));
			model.setViewAlert(entity.getViewAlert());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("createAllotModel: "+e.getMessage());
		}
		return model;
	}

	private Allot createAllotEntity(AllotModel model){
		Allot entity = null;
		try{
			entity = new Allot();

			entity.setAlAllotNo(model.getAlAllotNo());
			entity.setAlEmpId(model.getAlEmpId());
			entity.setAlId(model.getAlId());
			entity.setAllotDate(utilService.stringToDate(model.getAllotDate()));
			//entity.setAllottedEmployees(allottedEmployees);
			entity.setCallStatusId(model.getCallStatusId());
			entity.setCdId(model.getCdId());
			entity.setCreatedAt(utilService.stringDateToTimestamp(model.getCreatedAt()));
			entity.setFilePath(model.getFilePath());
			entity.setIsActive(model.getIsActive());
			entity.setOrginalFileName(model.getOrginalFileName());
			entity.setSlNo(model.getSlNo());
			entity.setUiStatus(model.getUiStatus());
			entity.setUpdatedAt(utilService.stringDateToTimestamp(model.getUpdatedAt()));
			entity.setViewAlert(model.getViewAlert());

		}catch(Exception e){
			e.printStackTrace();
			logger.error("createAllotEntity: "+e.getMessage());
		}
		return entity;
	}

	private AllottedEmployeesModel createAllottedEmployeesModel(AllottedEmployees entity){

		AllottedEmployeesModel model = null;
		try{

			model = new AllottedEmployeesModel();
			model.setAlAllotNo(entity.getAllot().getAlAllotNo());
			model.setAlId(entity.getAlId());
			model.setAllottedEmpoyeesId(entity.getAllotedEmpoyeesId());
			model.setCdId(entity.getCdId());
			model.setEmpCode(entity.getEmployee().getEmpCode());
			model.setEmployeeId(entity.getEmployeeId());
			model.setFirstName(entity.getEmployee().getFirstName());
			model.setLastName(entity.getEmployee().getLastName());
			model.setFullName(entity.getEmployee().getFirstName()+" "+entity.getEmployee().getLastName());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("createAllottedEmployeesModel: "+e.getMessage());
		}
		return model;

	}

	private AllottedEmployees createAllottedEmployees(AllottedEmployeesModel model){
		AllottedEmployees entity = null;
		try{
			entity = new AllottedEmployees();
			entity.setAlId(model.getAlId());
			entity.setAllottedEmpoyeesId(model.getAllottedEmpoyeesId());
			entity.setCdId(model.getCdId());
			entity.setEmployeeId(model.getEmployeeId());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("createAllottedEmployees: "+e.getMessage());
		}
		return entity ;
	}

	private ServiceReportModel createServiceReportModel(ServiceReport entity){
		ServiceReportModel model = null;
		try{
			model  = new ServiceReportModel();
			model.setAlAllotNo(entity.getAlAllotNo());
			model.setAlEmpId(entity.getAlId());
			model.setAlId(entity.getAlId());
			//			System.out.println("Business,createServiceReportModel, getAlAllotNo: "+entity.getAllot().getAlAllotNo()+
			//					", AllotDate: "+entity.getAllot().getAllotDate());
			model.setCdAllotNo(entity.getAllot().getAlAllotNo());
			if(entity.getAllot().getAllotDate()!= null){
				model.setCdAllotDate(utilService.dateToString(entity.getAllot().getAllotDate()));
			}

			model.setCdBoardDivision(entity.getCallDetail().getCdBoardDivision());
			model.setCdContactNo(entity.getCallDetail().getCdContactNo());
			model.setCdCustomerName(entity.getCallDetail().getCdCustomerName());
			model.setCdId(entity.getCallDetail().getCdId());
			model.setCreatedAt(utilService.dateToString(entity.getCreatedAt()));
			model.setEmpCode(entity.getEmployee().getEmpCode());
			model.setEmployeeId(entity.getEmployeeId());
			model.setFirstName(entity.getEmployee().getFirstName());
			model.setLastName(entity.getEmployee().getLastName());
			model.setFullName(entity.getEmployee().getFirstName()+" "+entity.getEmployee().getLastName());
			model.setIsActive(entity.getIsActive());
			model.setJobNature(entity.getJobNature());
			model.setObervationDetails(entity.getObervationDetails());
			model.setProductSlNo(entity.getProductSlNo());
			model.setReviewStatus(entity.getReviewStatus());
			model.setSr_cd_id(entity.getCallDetail().getCdId());
			model.setSrCallAttendDate(utilService.dateToString(entity.getSrCallAttendDate()));
			model.setSrCallClosedDate(utilService.dateToString(entity.getSrCallClosedDate()));
			model.setSrCallStatus(entity.getSrCallStatus());
			model.setSrId(entity.getSrId());
			model.setSrNaturalOfService(entity.getSrNaturalOfService());
			model.setSrNotificationStatus(entity.getSrNotificationStatus());
			model.setSrRemarks(entity.getSrRemarks());
			model.setSrReportedProblem(entity.getSrReportedProblem());
			model.setViewAlert(entity.getViewAlert());

			model.setSiteDetails(entity.getCallDetail().getSiteDetails());
			model.setGuranteePeriod(entity.getCallDetail().getCdGuranteePeriod());
			model.setRelayPanelDetails(entity.getCallDetail().getCdRelayPanelDetails());
			model.setProblemDetails(entity.getCallDetail().getCdProblemDetails());

		}catch(Exception e){
			e.printStackTrace();
			logger.error("createServiceReportModel: "+e.getMessage());
		}

		return model;
	}


}
