package com.yaz.alind.contoller;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
import com.yaz.alind.model.ServiceReportFactory;
import com.yaz.alind.model.SubmitServiceReportModel;
import com.yaz.alind.service.CallManagement;
import com.yaz.alind.service.UserService;
import com.yaz.alind.service.UtilService;
import com.yaz.alind.ui.model.CallDetailModel;
import com.yaz.alind.ui.model.CallDetailModelList;
import com.yaz.alind.ui.model.ServiceReportModelList;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CallManagementController  {

	private static final Logger logger = LoggerFactory.getLogger(CallManagementController.class);

	@Autowired
	UserService userService;
	@Autowired
	UtilService utilService;
	@Autowired
	CallManagement callManagement;
	@Autowired
	private ServletContext context;
	@Autowired
	private ServiceReportFactory serviceReportFactory;

	@RequestMapping(value="/call/getCallDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getCallDetails(@RequestHeader("token") String token,String dateFrom,
			String dateTo,String searchKeyWord,int callStatus,String gurenteePeriod) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getCallDetails,searchKeyWord: "+searchKeyWord
					+", callStatus: "+callStatus+", gurenteePeriod: "+gurenteePeriod+", dateTo: "+dateTo+
					",dateFrom: "+dateFrom);
			//			System.out.println("CallManagementController,getCallDetails,isActive: "+Integer.parseInt(isActive));
			if(tokenStatus){
				List<CallDetail> callDetails = callManagement.getCallDetails(dateFrom,dateTo,searchKeyWord.trim()
						,callStatus,gurenteePeriod);
				resultMap.put("callDetails", callDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getCallDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);

	}

	//	@RequestMapping(value="/call/getCallDetailsTest/{dateFrom}/{dateTo}/{searchKeyWord}/{callStatus}/{gurenteePeriod}/{pageNo}/{pageCount}", method = RequestMethod.GET)
	@RequestMapping(value="/call/getCallDetailsTest", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getCallDetailsTest(@RequestHeader("token") String token,
			@RequestParam(value="dateFrom", required=false)  String dateFrom, @RequestParam(value="dateTo", required=false) String dateTo,
			@RequestParam(value="searchKeyWord", required=false) String searchKeyWord,
			@RequestParam(value="callStatus") int callStatus,@RequestParam(value="gurenteePeriod") 	String gurenteePeriod,
			@RequestParam("pageNo") int pageNo,@RequestParam("pageCount") int pageCount) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,getCallDetailsTest,searchKeyWord: "+searchKeyWord+", callStatus: "+callStatus+", gurenteePeriod: "+gurenteePeriod);
			//			System.out.println("CallManagementController,getCallDetails,isActive: "+Integer.parseInt(isActive));
			if(tokenStatus){
				if (searchKeyWord != null) {
					searchKeyWord = searchKeyWord.trim();
				}
				CallDetailModelList callDetails = callManagement.getCallDetails(dateFrom,dateTo,searchKeyWord
						,callStatus,gurenteePeriod,pageNo,pageCount);
				resultMap.put("callDetails", callDetails);
				//				System.out.println("CallManagementController,getCallDetails,callDetails size: "+callDetails.getCallDetailModelList().size());
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getCallDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/searchCallDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getCallDetailsTest(@RequestHeader("token") String token,
			@RequestParam(value="dateFrom", required=false)  String dateFrom, @RequestParam(value="dateTo", required=false) String dateTo,
			@RequestParam(value="searchKeyWord", required=false) String searchKeyWord,
			@RequestParam(value="callStatus") int callStatus,@RequestParam(value="gurenteePeriod") 	String gurenteePeriod
			) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,searchCallDetails,searchKeyWord: "+searchKeyWord+", callStatus: "+callStatus+", gurenteePeriod: "+gurenteePeriod);
			//			System.out.println("CallManagementController,getCallDetails,isActive: "+Integer.parseInt(isActive));
			if(tokenStatus){
				if (searchKeyWord != null) {
					searchKeyWord = searchKeyWord.trim();
				}
				CallDetailModelList callDetails = callManagement.searchCallDetails(dateFrom,dateTo,searchKeyWord
						,callStatus,gurenteePeriod);
				resultMap.put("callDetails", callDetails);
				//				System.out.println("CallManagementController,getCallDetails,callDetails size: "+callDetails.getCallDetailModelList().size());
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("searchCallDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	/**
	 *  Excel report
	 * @param token
	 * @param dateFrom
	 * @param dateTo
	 * @param searchKeyWord
	 * @param callStatus
	 * @param gurenteePeriod
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/call/getCallDetailsReport", method = RequestMethod.GET)
	public ResponseEntity<OutputStream> getCallDetailsReport(HttpServletResponse response,
			@RequestHeader("token") String token,
			String dateFrom,
			String dateTo,String searchKeyWord,int callStatus,String gurenteePeriod) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		OutputStream out =  null;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getCallDetailsReport,token: "+token+", tokenStatus: "+tokenStatus+", isActive: "+callStatus);
			if(tokenStatus){
				XSSFWorkbook workbook = callManagement.getCallDetailsReport(context,dateFrom, dateTo, 
						searchKeyWord, callStatus, gurenteePeriod);
				String fileName =utilService.createFileName("Yazata_test.xlsx");

				out = response.getOutputStream();
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition","attachment; filename=" + "DETAILED_LIST_"+fileName);
				workbook.write(out);


			}else{
				return  null;
			}

		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getCallDetailsReport, "+e.getMessage());
		}
		out.close();
		return  new ResponseEntity<OutputStream>(out,HttpStatus.OK);

	}

	/**
	 *  Work Details Excel Report
	 * @param response
	 * @param token
	 * @param dateFrom
	 * @param dateTo
	 * @param searchKeyWord
	 * @param callStatus
	 * @param gurenteePeriod
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/call/getWorkDetailsExcelReport", method = RequestMethod.GET)
	public ResponseEntity<OutputStream> getWorkDetailsExcelReport(HttpServletResponse response,
			@RequestHeader("token") String token,
			String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		OutputStream out =  null;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getWorkDetailsExcelReport,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				XSSFWorkbook workbook = callManagement.getWorkDetailsExcelReport(context, dateFrom,
						dateTo, searchKeyWord, gurenteePeriod);
				String fileName =utilService.createFileName("Yazata_test.xlsx");

				out = response.getOutputStream();
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition","attachment; filename=" +"IN_PROGRESS_"+fileName);
				workbook.write(out);
			}else{
				return  null;
			}

		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getWorkDetailsExcelReport, "+e.getMessage());
		}
		out.close();
		return  new ResponseEntity<OutputStream>(out,HttpStatus.OK);

	}


	@RequestMapping(value="/call/getNonAllottedCalls", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getNonAllottedCalls(@RequestHeader("token") String token) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getNonAllottedCall,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<CallDetail> callDetails = callManagement.getNonAllottedCalls();
				System.out.println("CallManagementController,getNonAllottedCall, size: "+callDetails.size());
				resultMap.put("callDetails", callDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getNonAllottedCalls, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getOnGoingCalls", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getOnGoingCalls(@RequestHeader("token") String token) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getOnGoingCalls,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<CallDetail> callDetails = callManagement.getOnGoingCalls();
				System.out.println("CallManagementController,getOnGoingCalls, size: "+callDetails.size());
				resultMap.put("callDetails", callDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getOnGoingCalls, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getCompletedCalls", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getCompletedCalls(@RequestHeader("token") String token) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getCompletedCalls,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<CallDetail> callDetails = callManagement.getCompletedCalls();
				System.out.println("CallManagementController,getCompletedCalls, size: "+callDetails.size());
				resultMap.put("callDetails", callDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getCompletedCalls, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	/**
	 *  Dash Board
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/call/admindashboard", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>>  getAdmindashboard(@RequestHeader("token") String token)  {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,getAdmindashboard,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				DashBoardVariables dashBoardVariables = callManagement.getAdminDashboard();
				//				System.out.println("CallManagementController,getAdmindashboard, size: "+callDetails.size());
				resultMap.put("dashBoardVariables", dashBoardVariables);
				resultMap.put("status", "success");
			}else{
				//				throw new AlindException();
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAdmindashboard, "+e.getMessage());
			//			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);

		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getAllAllotDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>>  getAllAllotDetails(@RequestHeader("token") String token,String isNew) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getAllAllotDetails,token: "+token+", tokenStatus: "+tokenStatus+", isNew:"+isNew);
			if(tokenStatus){
				List<Allot> allots = callManagement.getAllAllotDetails(Integer.parseInt(isNew));
				System.out.println("CallManagementController,getAllAllotDetails, size: "+allots.size());
				resultMap.put("allots", allots);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllAllotDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	/**
	 *   Call Registration from Client side
	 * @param callDetail
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/call/saveOrUpdateCallDetails", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateCallDetails(@RequestBody CallDetail callDetail) throws Exception {

		Map<String,Object> resultMap = null;
		//		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			//			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,saveOrUpdateCallDetails,token: "+token+", tokenStatus: "+tokenStatus);
			//			if(tokenStatus){
			CallDetail cDetails= callManagement.saveOrUpdateCallDetails(context,callDetail);
			//				System.out.println("CallManagementController,saveOrUpdateCallDetails, size: "+allots.size());
			resultMap.put("callDetail", cDetails);
			resultMap.put("status", "success");
			//			}else{
			//				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			//			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateCallDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/updateCallDetails", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> updateCallDetails(@RequestHeader("token") String token,
			@RequestBody CallDetailModel callDetailModel) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,updateCallDetails,token: "+token);
			if(tokenStatus){
				CallDetailModel model= callManagement.updateCallDetail(callDetailModel);
				//				System.out.println("CallManagementController,saveOrUpdateCallDetails, size: "+allots.size());
				resultMap.put("callDetail", model);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("updateCallDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getServiceReportByCallStatus", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getServiceReportByCallStatus(@RequestHeader("token") String token,String callStatus) throws Exception {
		//		public Map<String,Object> getServiceReportByCallStatus(@RequestHeader("token") String token,String callStatus) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getAllAllotDetails,token: "+token+", tokenStatus: "+tokenStatus+", callStatus:"+callStatus);
			if(tokenStatus){
				List<ServiceReport> serviceReports = callManagement.getServiceReportByCallStatus(callStatus);
				System.out.println("CallManagementController,getServiceReportByCallStatus, size: "+serviceReports.size());
				resultMap.put("serviceReports", serviceReports);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getServiceReportByCallStatus, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getAllWorkDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllWorkDetails(@RequestHeader("token") String token) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getAllWorkDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<CallDetail> callDetails = callManagement.getAllWorkDetails();
				System.out.println("CallManagementController,getAllWorkDetails, size: "+callDetails.size());
				resultMap.put("callDetails", callDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllWorkDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

/**
	@RequestMapping(value="/call/searchServiceReport", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> searchServiceReport(@RequestHeader("token") String token ,
			int statusId,int employeeId,String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,searchServiceReport,token: "+token+","
			//					+ " statusId: "+statusId+",employeeId: "+employeeId);
			//			if(tokenStatus){
			List<ServiceReport> serviceReports = callManagement.searchServiceReport(dateFrom, dateTo, 
					searchKeyWord.trim(), gurenteePeriod,employeeId,statusId,context);
			//				System.out.println("CallManagementController,searchServiceReport, size: "+serviceReports.size());
			resultMap.put("serviceReports", serviceReports);
			resultMap.put("status", "success");
			//			}else{
			//				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			//			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("searchServiceReport, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	} **/

	@RequestMapping(value="/call/searchServiceReport", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> searchServiceReportTest(@RequestHeader("token") String token ,
			int statusId,int employeeId,String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,searchServiceReport,token: "+token+","
			//					+ " statusId: "+statusId+",employeeId: "+employeeId);
			if(tokenStatus){
				if (searchKeyWord != null) {
					searchKeyWord = searchKeyWord.trim();
				}

				ServiceReportModelList serviceReports = callManagement.searchServiceReportForUI(dateFrom, dateTo, 
						searchKeyWord, gurenteePeriod,employeeId,statusId,context);
				//				System.out.println("CallManagementController,searchServiceReport, size: "+serviceReports.size());
				resultMap.put("serviceReports", serviceReports);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("searchServiceReportTest, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/call/getAllServiceReport", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllServiceReport(@RequestHeader("token") String token ,
			@RequestParam("pageNo") int pageNo,@RequestParam("pageCount") int pageCount) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,getAllServiceReport,token: "+token+","
			//					+ " pageNo: "+pageNo+",pageCount: "+pageCount);
			if(tokenStatus){
				ServiceReportModelList serviceReports = callManagement.getAllServiceReport(pageNo, pageCount);
				//				System.out.println("CallManagementController,searchServiceReport, size: "+serviceReports.size());
				resultMap.put("serviceReports", serviceReports);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllServiceReport, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getServiceReportExcel", method = RequestMethod.GET)
	public ResponseEntity<OutputStream> getServiceReportExcel(@RequestHeader("token") String token ,
			HttpServletResponse response,
			int statusId,int employeeId,String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		OutputStream out =  null;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getServiceReportExcel,token: "+token+","
					+ " statusId: "+statusId+",employeeId: "+employeeId);
			//			if(tokenStatus){
			XSSFWorkbook workbook = callManagement.getServiceReportExcel(context,dateFrom,
					dateTo,searchKeyWord,gurenteePeriod,employeeId, statusId);
			String fileName =utilService.createFileName("Yazata_test.xlsx");

			out = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition","attachment; filename=" +"SERVICE_REPORT_"+fileName);
			workbook.write(out);
			resultMap.put("status", "success");
			//			}else{
			//				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			//			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getServiceReportExcel, "+e.getMessage());
			//			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		out.close();
		return  new ResponseEntity<OutputStream>(out,HttpStatus.OK);
	}

	@RequestMapping(value="/call/searchWorkAllotDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> searchWorkAllotDetails(@RequestHeader("token") String token ,String dateFrom,
			String dateTo,String searchKeyWord,String gurenteePeriod) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
//			System.out.println("CallManagementController,searchWorkAllotDetails,token: "+token+", tokenStatus: "+tokenStatus+", searchKeyWord: "+searchKeyWord);
			if(tokenStatus){
				List<CallDetail> callDetails = callManagement.searchWorkAllotDetails(dateFrom, dateTo, searchKeyWord.trim(), gurenteePeriod);
//				System.out.println("CallManagementController,searchWorkAllotDetails, size: "+callDetails.size());
				resultMap.put("callDetails", callDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("searchWorkAllotDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getServiceReportById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getServiceReportById(@RequestHeader("token") String token 
			,String srId) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getServiceReportById,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				String realPath = context.getRealPath("");
				ServiceReport serviceReport = callManagement.getServiceReportById(Integer.parseInt(srId),realPath);
				resultMap.put("serviceReport", serviceReport);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getServiceReportById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/saveOrUpdateRelayDetails", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateRelayDetails(@RequestHeader("token") String token
			,@RequestBody RelayDetails relayDetails) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,saveRelayDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				RelayDetails reDetails = callManagement.saveOrUpdateRelayDetails(relayDetails);
				resultMap.put("relayDetails", reDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveRelayDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/saveOrUpdatePanelDetails", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdatePanelDetails(@RequestHeader("token") String token
			,@RequestBody PanelDetails panelDetails) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,saveOrUpdatePanelDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				PanelDetails panDetails = callManagement.saveOrUpdatePanelDetails(panelDetails);
				resultMap.put("panelDetails", panDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdatePanelDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/call/getAllRelayDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllRelayDetails() throws Exception {
		Map<String,Object> resultMap = null;
		//		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			//			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,saveOrUpdatePanelDetails,token: "+token+", tokenStatus: "+tokenStatus);
			//			/if(tokenStatus){
			List<RelayDetails> relayDetails = callManagement.getAllRelayDetails();
			resultMap.put("relayDetails", relayDetails);
			resultMap.put("status", "success");
			//			}else{
			//				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			//			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllRelayDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getAllPanelDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllPanelDetails() throws Exception {
		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			List<PanelDetails> panDetails = callManagement.getAllPanelDetails();
			resultMap.put("panelDetails", panDetails);
			resultMap.put("status", "success");
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllPanelDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/deletePanelDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deletePanelDetails(@RequestHeader("token") String token,
			int panelId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,deletePanelDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				int status  = callManagement.deletePanelDetails(token,panelId);
				if(status == 1){
					resultMap.put("status", "success");
				} else{
					resultMap.put("status", "failed");
				}
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deletePanelDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/deleteRelayDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteRelayDetails(@RequestHeader("token") String token,
			int relayId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,deleteRelayDetails,relayId: "+relayId+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				int status  = callManagement.deleteRelayDetails(token,relayId);
				if(status == 1){
					resultMap.put("status", "success");
				} else{
					resultMap.put("status", "failed");
				}
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteRelayDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/saveOrUpdateCusotmerDetails", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateCusotmerDetails(@RequestHeader("token") String token,
			@RequestBody CustomerDetails customerDetails) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,saveOrUpdateCusotmerDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				CustomerDetails cusDetails = callManagement.saveOrUpdateCusotmerDetails(customerDetails);
				resultMap.put("customerDetails", cusDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateCusotmerDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getAllCustomerDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllCustomerDetails() throws Exception {
		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			List<CustomerDetails> customerDetails = callManagement.getAllCustomerDetails();
			resultMap.put("customerDetails", customerDetails);
			resultMap.put("status", "success");
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllCustomerDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	//	@RequestMapping(value="/call/deleteCustomerDetails", method = RequestMethod.GET)
	//	public ResponseEntity<Map<String,Object>> deleteCustomerDetails(@RequestHeader("token") String token,
	//			int customerId) throws Exception {
	@RequestMapping(value="/call/deleteCustomerDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteCustomerDetails(@RequestHeader("token") String token,
			int customerId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,deleteCustomerDetails,customerId: "+customerId);
			if(tokenStatus){
				int status  = callManagement.deleteCustomerDetails(token,customerId);
				if(status == 1){
					resultMap.put("status", "success");
				} else{
					resultMap.put("status", "failed");
				}
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteCustomerDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/call/getCustomerDetailsById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getCustomerDetailsById(@RequestHeader("token") String token,
			@RequestParam String customerId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getCustomerDetailsById,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				CustomerDetails customerDetails = callManagement.getCustomerDetailsById(Integer.parseInt(customerId));
				resultMap.put("customerDetails", customerDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getCustomerDetailsById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/saveOrUpdateBoardDivisionDetails", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateBoardDivisionDetails(@RequestHeader("token") String token,
			@RequestBody BoardDivisionDetails boardDivisionDetails) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,/call/saveOrUpdateBoardDivisionDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				BoardDivisionDetails boDivisionDetails = callManagement.saveOrUpdateBoardDivisionDetails(boardDivisionDetails);
				resultMap.put("boardDivisionDetails", boDivisionDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateBoardDivisionDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getAllBoardDivisionDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllBoardDivisionDetails() throws Exception {
		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			List<BoardDivisionDetails> boardDivisionDetails = callManagement.getAllBoardDivisionDetails();
			resultMap.put("boardDivisionDetails", boardDivisionDetails);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllBoardDivisionDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getBoardDivisionDetailsById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getBoardDivisionDetailsById(@RequestHeader("token") String token,
			@RequestParam String boardDivisionId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getBoardDivisionDetailsById,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				BoardDivisionDetails boardDivisionDetails = callManagement.getBoardDivisionDetailsById
						(Integer.parseInt(boardDivisionId));
				resultMap.put("boardDivisionDetails", boardDivisionDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getBoardDivisionDetailsById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/deleteBoardDivisionDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteBoardDivisionDetails(@RequestHeader("token") String token,
			int boardDivisionId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,deleteBoardDivisionDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				int status  = callManagement.deleteBoardDivisionDetails(token,boardDivisionId);
				if(status == 1){
					resultMap.put("status", "success");
				} else{
					resultMap.put("status", "failed");
				}
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteBoardDivisionDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/saveOrUpdateAllottedEmployees", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateAllottedEmployees(@RequestHeader("token") String token,
			@RequestBody Object object) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		System.out.println("Controller, saveOrUpdateAllottedEmployees,token: "+token);
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				/**
				LinkedHashMap<String, String> lhm = (LinkedHashMap<String, String>) object;
				LinkedHashMap<String, String> objAllotT=null;
				Set set = lhm.entrySet();
				// Displaying elements of LinkedHashMap
				Iterator iterator = set.iterator();
				while(iterator.hasNext()) {
					Map.Entry me = (Map.Entry)iterator.next();
					//	            System.out.print("Key is: "+ me.getKey() + 
					//	                    "& Value is: "+me.getValue()+"\n");
					objAllotT = (LinkedHashMap<String, String>) me.getValue();
				}
				String cdId = String.valueOf(objAllotT.get("cdId"));
				String alId = String.valueOf(objAllotT.get("alId"));
				System.out.println("Controller, saveOrUpdateAllottedEmployees,alId: "+alId);
				 **/

				int status = callManagement.saveOrUpdateAllottedEmployees(context,object);
				resultMap.put("status", status);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateAllottedEmployees, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getAllottedWorkDetailsByEmpId", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllottedWorkDetailsByEmpId(@RequestHeader("token") String token,
			String searchKeyWord, String employeeId,String dateFrom,String dateTo) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallMgtImpl,getAllottedWorkDetailsByEmpId,allottedWorks,employeeId: "+employeeId+
					", dateFrom: "+dateFrom+", searchKeyword: "+searchKeyWord);
			//			if(tokenStatus){
			List<Allot> allottedWorks = callManagement.getAllottedWorkDetailsByEmpId
					( searchKeyWord,Integer.parseInt(employeeId),dateFrom,dateTo);
			resultMap.put("allottedWorks", allottedWorks);
			resultMap.put("status", "success");
			//			}else{
			//				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			//			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllottedWorkDetailsByEmpId, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}
	/**
	 *  Excel report
	 * @param token
	 * @param searchKeyWord
	 * @param employeeId
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/call/getAllottedWorkDetailsReportByEmpId", method = RequestMethod.GET)
	public ResponseEntity<OutputStream> getAllottedWorkDetailsReportByEmpId(HttpServletResponse response,
			@RequestHeader("token") String token,
			String searchKeyWord, String employeeId,String dateFrom,String dateTo) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		OutputStream out =  null;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallMgtImpl,getAllottedWorkDetailsReportByEmpId,allottedWorks,employeeId: "+employeeId+
					", dateFrom: "+dateFrom+", searchKeyword: "+searchKeyWord);
			if(tokenStatus){
				XSSFWorkbook workbook = callManagement.getAllottedWorkDetailsReportByEmpId(context, searchKeyWord, 
						Integer.parseInt(employeeId), dateFrom, dateTo);
				String fileName =utilService.createFileName("Yazata_test.xlsx");

				out = response.getOutputStream();
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition","attachment; filename=" + "EMPLOYEE_WORK_REPORT_"+fileName);
				workbook.write(out);


			}else{
				return  null;
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllottedWorkDetailsReportByEmpId, "+e.getMessage());
		}
		out.close();
		return  new ResponseEntity<OutputStream>(out,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getAllotListByCallStatusId", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllotByCallStatusId(@RequestHeader("token") String token,
			int callStatusId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallMgtImpl,getAllotListByCallStatusId,callStatusId; "+callStatusId);
			if(tokenStatus){
				List<Allot> allots = callManagement.getAllotListByCallStatusId(token,callStatusId);
				resultMap.put("allots", allots);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllotListByCallStatusId, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getMinitesOfMettingPDFReport", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> getMinitesOfMettingPDFReport(@RequestHeader("token") String token ,
			HttpServletResponse response,
			int alId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		InputStreamResource input =  null;
		byte[] content = null;
		try{
			System.out.println("CallMgtImpl,getMinitesOfMettingPDFReport,alId; "+alId);
			resultMap = new HashMap<String,Object>();
			ByteArrayInputStream bis = callManagement.getMinutesOfMeetingPDFReport(context,alId);
			ServiceReport serviceReport = callManagement.getServiceReportByAlId(alId, context);
			String newFileName = utilService.createDownLoadFileName();
			content = IOUtils.toByteArray(bis); 
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename="+serviceReport.getCallDetail().getSiteDetails()
					+"_"+newFileName+".pdf");
			//			response.setHeader("Content-disposition","attachment; filename=minitesOfMeeing.pdf");
			input = new InputStreamResource(bis);
			//	        System.out.println("CallMgtImpl,getMinitesOfMettingPDFReport,content len:  "+content.length);

			//			tokenStatus = utilService.evaluateToken(token);
			//			if(tokenStatus){
			//				Allot allot = callManagement.getAllottedWorkById
			//						(Integer.parseInt(alId));
			//				resultMap.put("allot", allot);
			//				resultMap.put("status", "success");
			//			}else{
			//				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			//			}

			//			response.setHeader("Content-Disposition", "inline; filename=customers.pdf");
			resultMap.put("status", "success");
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllottedWorkPDFById, "+e.getMessage());
			//			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}

		return  new ResponseEntity<ByteArrayResource>(new ByteArrayResource(content),HttpStatus.OK);
	}

	@RequestMapping(value="/call/getAllottedWorkByAlId", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllottedWorkByAlId(@RequestHeader("token") String token,
			@RequestParam String alId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				Allot allot = callManagement.getAllottedWorkById
						(Integer.parseInt(alId));
				resultMap.put("allot", allot);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllottedWorkByAlId, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/saveOrUpdateServiceReport" 
			,method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<Map<String,Object>> saveOrUpdateServiceReport(@RequestHeader("token") String token,
			@RequestParam(value = "momFile", required = false)MultipartFile momFile,
			@RequestParam(value = "srId", required = false)String srId,
			@RequestParam(value = "srCallAttendDate", required = false)String srCallAttendDate,
			@RequestParam(value = "srCallClosedDate", required = false) String srCallClosedDate,
			@RequestParam(value = "srCallStatus", required = false)String srCallStatus,
			@RequestParam(value = "srNaturalOfService", required = false)String srNaturalOfService,
			@RequestParam(value = "srReportedProblem", required = false)String srReportedProblem,
			@RequestParam(value = "srRemarks", required = false)String srRemarks,
			@RequestParam(value = "sr_cd_id", required = false)Integer sr_cd_id,
			@RequestParam(value = "obervationDetails", required = false)String obervationDetails,
			@RequestParam(value = "productSlNo", required = false)String productSlNo) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		//		System.out.println("CallMgtCtrl, saveOrUpdateServiceReport,token: "+token+","
		//				+ " srCallAttendDate: "+srCallAttendDate+", sr_cd_id: "+sr_cd_id+
		//				", srNaturalOfService: "+srNaturalOfService+" file name: "+momFile.getOriginalFilename()+
		//				"file size: "+momFile.getSize());
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				String contextPath = context.getRealPath(""); 
				ServiceReport serviceReport = serviceReportFactory.createServiceReport();
				serviceReport.setSrCallAttendDate(utilService.getDateFromString(srCallAttendDate));
				serviceReport.setSrCallClosedDate(utilService.getDateFromString(srCallClosedDate));
				serviceReport.setSrCallStatus(srCallStatus);
				serviceReport.setSrReportedProblem(srReportedProblem);
				serviceReport.setSrNaturalOfService(srNaturalOfService);
				serviceReport.setSrRemarks(srRemarks);
				serviceReport.setSr_cd_id(sr_cd_id);
				serviceReport.setProductSlNo(productSlNo);
				serviceReport.setObervationDetails(obervationDetails);
				ServiceReport report = callManagement.saveOrUpdateServiceReport(serviceReport,momFile,contextPath,token);
				resultMap.put("serviceReport", report);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateServiceReport, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/call/updateGuranteePeriod", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> updateGuranteePeriod(@RequestHeader("token") String token
			,@RequestBody Object object) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,updateGuranteePeriod,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				CallDetail cDetails = callManagement.updateGuranteePeriod(object);
				resultMap.put("callDetail", cDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("updateGuranteePeriod, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/call/deleteCallDetails", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> deleteCallDetails(@RequestHeader("token") String token
			,@RequestBody CallDetail callDetail) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,deleteCallDetails,token: "+token+", cdId: "+callDetail.getCdId());
			//			System.out.println("CallManagementController,deleteCallDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				CallDetail cDetails = callManagement.deleteCallDetailsById(token,callDetail.getCdId());
				resultMap.put("callDetail", cDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteCallDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/updateViewAlert", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> updateViewAlert(@RequestHeader("token") String token
			,@RequestBody Object object) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,updateViewAlert,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				//				int updateStatus = callManagement.updateViewAlert(Integer.parseInt(id),action);
				int updateStatus = callManagement.updateViewAlert(object);
				resultMap.put("updateStatus",updateStatus);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("updateViewAlert, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/call/getAllNatureOfJobs", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllNatureOfJobs()
			throws Exception {
		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			List<NatureOfJobs> natureOfJobs = callManagement.getAllNatureOfJobs();
			resultMap.put("natureOfJobs", natureOfJobs);
			resultMap.put("status", "success");
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllNatureOfJobs, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/saveOrUpdateNatureOfJobs", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateNatureOfJobs(@RequestHeader("token") String token,
			@RequestBody NatureOfJobs natureOfJobs) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				NatureOfJobs natJobs = callManagement.saveOrUpdateNatureOfJobs(natureOfJobs);
				resultMap.put("natureOfJobs", natJobs);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateNatureOfJobs, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getNatureOfJobsById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getNatureOfJobsById(@RequestHeader("token") String token,
			@RequestParam String natureJobId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				NatureOfJobs natJobs = callManagement.getNatureOfJobsById(Integer.parseInt(natureJobId));
				resultMap.put("natureOfJobs", natJobs);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getNatureOfJobsById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getAllObservationBeforeMaintanence", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllObservationBeforeMaintanence
	() throws Exception {
		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			List<ObservationBeforeMaintanence> obList= callManagement.getAllObservationBeforeMaintanence();
			resultMap.put("obList", obList);
			resultMap.put("status", "success");

		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllObservationBeforeMaintanence, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getObservationBeforeMaintanenceById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getObservationBeforeMaintanenceById(@RequestHeader("token") String token,
			@RequestParam String obervationId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				resultMap = new HashMap<String,Object>();
				ObservationBeforeMaintanence obMaintanence = callManagement.getObservationBeforeMaintanenceById(Integer.parseInt(obervationId));
				resultMap.put("obMaintanence", obMaintanence);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getObservationBeforeMaintanenceById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/saveOrUpdateObervationBeforeMaintanence", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateObervationBeforeMaintanence(@RequestHeader("token") String token,
			@RequestBody ObservationBeforeMaintanence obBeforeMaintanence) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallMgtControler,saveOrUpdateObervationBeforeMaintanence");
			if(tokenStatus){
				resultMap = new HashMap<String,Object>();
				ObservationBeforeMaintanence obMaintanence = callManagement.saveOrUpdateObervationBeforeMaintanence(obBeforeMaintanence);
				resultMap.put("obMaintanence", obMaintanence);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getNatureOfJobsById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/deleteObservationBeforeMaintanence", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteObservationBeforeMaintanence(@RequestHeader("token") String token,
			int obervationId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallMgtController,deleteObservationBeforeMaintanence,obervationId: "+obervationId);
			if(tokenStatus){
				resultMap = new HashMap<String,Object>();
				int status = callManagement.deleteObservationBeforeMaintanence(token,obervationId);
				if(status == 1){
					resultMap.put("status", "success");
				}else{
					resultMap.put("status", "failed");
				}
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteObservationBeforeMaintanence, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/deleteNatureOfJobs", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteNatureOfJobs(@RequestHeader("token") String token,
			int natureJobId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallContoller,deleteNatureOfJobs");
			if(tokenStatus){
				resultMap = new HashMap<String,Object>();
				int status = callManagement.deleteNatureOfJobs(token,natureJobId);
				if(status == 1){
					resultMap.put("status", "success");
				}else{
					resultMap.put("status", "failed");
				}
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteNatureOfJobs, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getAllCustomerSiteDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllCustomerSiteDetails() throws Exception {
		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			List<CustomerSiteDetails> customerSiteDetails = callManagement.getAllCustomerSiteDetails();
			resultMap.put("customerSiteDetails", customerSiteDetails);
			resultMap.put("status", "success");
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllCustomerSiteDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/saveOrUpdateCustomerSiteDetails", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateCustomerSiteDetails(@RequestHeader("token") String token,
			@RequestBody CustomerSiteDetails customerSiteDetails) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				resultMap = new HashMap<String,Object>();
				CustomerSiteDetails custSiteDetails = callManagement.saveOrUpdateCustomerSiteDetails(customerSiteDetails);
				resultMap.put("customerSiteDetails", custSiteDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateCustomerSiteDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getCustomerSiteDetailsById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getCustomerSiteDetailsById(@RequestHeader("token") String token,
			@RequestParam String siteId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				resultMap = new HashMap<String,Object>();
				CustomerSiteDetails customerSiteDetails = callManagement.getCustomerSiteDetailsById(Integer.parseInt(siteId));
				resultMap.put("customerSiteDetails", customerSiteDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getCustomerSiteDetailsById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/deleteCustomerSiteDetailsById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteCustomerSiteDetailsById(@RequestHeader("token") String token,
			int siteId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallContoller,deleteCustomerSiteDetailsById");
			if(tokenStatus){
				resultMap = new HashMap<String,Object>();
				int status = callManagement.deleteCustomerSiteDetailsById(token,siteId);
				if( status == 1){
					resultMap.put("status", "success");
				}else{
					resultMap.put("status", "failed");
				}
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteCustomerSiteDetailsById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	/**
	 *  Updating Guarantee Period etc 
	 * @param token
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/call/submitServiceReport", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> submitServiceReport(@RequestHeader("token") String token
			,@RequestBody SubmitServiceReportModel submitServiceReportModel) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,submitServiceReport,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){

//				System.out.println("CallManagementController,submitServiceReport,SrId: "+submitServiceReportModel.getSrId()
//						+", GurenteePeriod: "+submitServiceReportModel.getGurenteePeriod());
				ServiceReport serviceReport = callManagement.submitServiceReport(submitServiceReportModel);
				resultMap.put("serviceReport", serviceReport);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("submitServiceReport, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getServiceReportByAlId", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getServiceReportByAlId(@RequestHeader("token") String token,
			@RequestParam String alId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				resultMap = new HashMap<String,Object>();
				ServiceReport serviceReport = callManagement.getServiceReportByAlId(Integer.parseInt(alId),context);
				resultMap.put("serviceReport", serviceReport);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getServiceReportByAlId, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getCallDetailByCdId", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getCallDetailByCdId(@RequestHeader("token") String token 
			,int cdId) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("CallManagementController,getCallDetailByCdId,token: "+token);
			if(tokenStatus){
				//				CallDetail callDetail = callManagement.getCallDetailByCdId(cdId);
				//				resultMap.put("callDetail", callDetail);
				CallDetailModel callDetail = callManagement.getCallDetailByCdId(cdId);
				resultMap.put("callDetail", callDetail);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getCallDetailByCdId, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/getUploadedServiceReportBySrId", method = RequestMethod.GET)
	public ResponseEntity<OutputStream> getUploadedServiceReportBySrId(HttpServletResponse response,
			@RequestHeader("token") String token,int srId) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		OutputStream out =  null;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("CallManagementController,getUploadedServiceReportBySrId,token: "+token);
			if(tokenStatus){
				ServiceReport serviceReport = callManagement.getUploadedServiceReportBySrId(context, srId);
				byte[] fileData = serviceReport.getUploadedRepByte();

				out = response.getOutputStream();
				response.setContentType(serviceReport.getFileType());
				//				System.out.println("CallManagementController,getUploadedServiceReportBySrId,fileData: "
				//						+ ""+fileData.length+", ContentType: "+"image/"+serviceReport.getFileType());
				response.setHeader("Content-disposition","attachment; filename=" +serviceReport.getOrginalFileName());
				out.write(fileData);

			}else{
				return  null;
			}

		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getUploadedServiceReportBySrId, "+e.getMessage());
		}
		out.close();
		return  new ResponseEntity<OutputStream>(out,HttpStatus.OK);

	}


	/**

	@RequestMapping(value="/call/saveRelayDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> searchCallDetails() throws Exception {
		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			List<RelayDetails> relayDetails = callManagement.saveRelayDetails();
			resultMap.put("relayDetails", relayDetails);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("relayDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/call/savePanelDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> savePanelDetails() throws Exception {
		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			List<PanelDetails> panelDetails = callManagement.savePanelDetails();
			resultMap.put("panelDetails", panelDetails);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("savePanelDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/call/updateCdIdInAllot", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> updateCdIdInAllot() throws Exception {
		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			int val = callManagement.updateCdIdInAllot();
			resultMap.put("val", val);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("updateCdIdInAllot, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}**/


	@RequestMapping(value="/call/allotEmpolyeeTemp", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> allotEmpolyeeTemp() throws Exception {
		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			//			int val = callManagement.allotEmpolyeeTemp();
			//			int val = callManagement.updateCallDetailsTemp();
			//			int val = callManagement.updateAllotedEmployeesTemp();
			//			int val = callManagement.updateServiceAllotTemp();
			//			int val = utilService.saveCustomerDetailsTemp();
			//			int val = utilService.saveBoardDivisionDetailsTemp();
			//			int val = callManagement.updateAllotCallStatusTemp();
			//			ServiceReport serviceReport = callManagement.getUploadedServiceReportBySrId(context,367);
			//			System.out.println("allotEmpolyeeTemp,file size: "+serviceReport.getUploadedRepByte());
			//			int val = utilService.saveSiteDetailsTemp();
			//			int val = callManagement.updateAlIdInServiceReport();
			//			int val = utilService.saveObeservationBeforeMaintanenceTemp();
			//			int val = utilService.saveNatureOfServiceTemp();
			//			int val = utilService.saveMaterialStockInfoTemp();
			int val = utilService.savePanelDetialsTemp();
			resultMap.put("val", val);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("allotEmpolyeeTemp, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}





}
