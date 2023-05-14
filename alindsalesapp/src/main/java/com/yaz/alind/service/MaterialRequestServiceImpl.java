package com.yaz.alind.service;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaz.alind.dao.CallManagementDAO;
import com.yaz.alind.dao.MaterialRequestDAO;
import com.yaz.alind.model.AuditJson;
import com.yaz.alind.model.AuditJsonFactory;
import com.yaz.alind.model.AuditLog;
import com.yaz.alind.model.AuditLogFactory;
import com.yaz.alind.model.CourierServiceDetails;
import com.yaz.alind.model.DespatchDetails;
import com.yaz.alind.model.DespatchDetailsFactory;
import com.yaz.alind.model.DespatchDetailsIntegrator;
import com.yaz.alind.model.DespatchDetailsIntegratorFactory;
import com.yaz.alind.model.DespatchReceivedStatus;
import com.yaz.alind.model.DespatchedItems;
import com.yaz.alind.model.Employee;
import com.yaz.alind.model.EmployeeMinData;
import com.yaz.alind.model.ItemReceivedInfo;
import com.yaz.alind.model.ItemReceivedInfoFactory;
import com.yaz.alind.model.LatestRequestIds;
import com.yaz.alind.model.MaterialCategory;
import com.yaz.alind.model.MaterialRequest;
import com.yaz.alind.model.MaterialRequestFactory;
import com.yaz.alind.model.MaterialRequestIntegrater;
import com.yaz.alind.model.MaterialRequestIntegraterFactory;
import com.yaz.alind.model.MaterialStockInfo;
import com.yaz.alind.model.RequestedItems;
import com.yaz.alind.model.StatusInfo;

public class MaterialRequestServiceImpl implements MetrialRequestService {

	private static final Logger logger = LoggerFactory.getLogger(MaterialRequestServiceImpl.class);

	@Autowired
	MaterialRequestDAO materialRequestDAO;
	@Autowired
	UtilService utilService;
	@Autowired
	UserService userService;
	@Autowired
	MaterialRequestIntegraterFactory materialRequestIntegraterFactory;
	@Autowired
	DespatchDetailsIntegratorFactory despatchDetailsIntegratorFactory;
	@Autowired
	CallManagementDAO callManagementDAO;
	@Autowired
	MaterialRequestFactory materialRequestFactory;
	@Autowired
	CallManagement callManagement;
	@Autowired
	DespatchDetailsFactory despatchDetailsFactory;
	@Autowired
	ItemReceivedInfoFactory itemReceivedInfoFactory; 
	@Autowired
	AuditLogFactory auditLogFactory;
	@Autowired
	AuditJsonFactory auditJsonFactory;

	@Override
	public List<MaterialStockInfo> getAllMaterialStockInfo(int isActive) {
		List<MaterialStockInfo> materialStockInfos = null;
		try{
			materialStockInfos = materialRequestDAO.getAllMaterialStockInfo(isActive);
			for(int i=0;i<materialStockInfos.size();i++){
				materialStockInfos.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllMaterialStockInfo: "+e.getMessage());
		}
		return materialStockInfos;
	}

	@Override
	public MaterialStockInfo saveOrUpdateMaterialStock(
			MaterialStockInfo materialStockInfo) {
		MaterialStockInfo stockInfo = null;
		try{
			Timestamp timeSt=utilService.dateToTimeStamp(utilService.getCurrentDateAndTime());
			materialStockInfo.setUpdatedAt(timeSt);
			materialStockInfo.setIsActive(1);
			stockInfo = materialRequestDAO.saveOrUpdateMaterialStock(materialStockInfo);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateMaterialStock: "+e.getMessage());
		}
		return stockInfo;
	}

	@Override
	public MaterialStockInfo deleteMaterialStockInfo(int materialStockId) {
		MaterialStockInfo materialStockInfo = null;
		try{
			materialStockInfo = materialRequestDAO.getMaterialStockInfoById(materialStockId);
			materialStockInfo.setIsActive(0);
			materialStockInfo = materialRequestDAO.saveOrUpdateMaterialStock(materialStockInfo);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateMaterialStock: "+e.getMessage());
		}
		return materialStockInfo;
	}

	@Override
	public MaterialStockInfo getMaterialStockInfoById(int materialStockId) {
		return materialRequestDAO.getMaterialStockInfoById(materialStockId);
	}

	@Override
	public MaterialRequestIntegrater saveOrUpdateMaterialRequest(String token,
			Object object) {
		MaterialRequestIntegrater materialRequestIntegrater = null;
		try{
			materialRequestIntegrater = getMaterialRequestIntegrater(object);
			MaterialRequest materialRequest = materialRequestIntegrater.getMaterialRequest();
			List<RequestedItems> requestedItems = materialRequestIntegrater.getRequestedItemList();
			Timestamp timestamp = utilService.dateToTimeStamp(utilService.getCurrentDateAndTime());
			//			System.out.println("MaterialRequestBusiness,saveOrUpdateMaterialRequest,timestamp: "+timestamp);
			materialRequest.setUpdatedAt(timestamp);
			materialRequest.setIsActive(1);
			// Before processing
			materialRequest.setStatusInfoId(1);
			materialRequest.setViewAlert(1);
			Employee employee = userService.getEmployeeByToken(token);
			materialRequest.setEmployeeId(employee.getEmployeeId());
			LatestRequestIds latestRequestIds = callManagementDAO.getLastServiceRequestId(1);
			String newMatReqNo = utilService.createMetrialRequestId(latestRequestIds.getLatestMaterialRequestId());
			System.out.println("MaterialRequestBusiness,saveOrUpdateMaterialRequest, newMatReqNo: "+newMatReqNo); 
			materialRequest.setMetrialRequestNumber(newMatReqNo);
			materialRequest = materialRequestDAO.saveOrUpdateMaterialRequest(materialRequest);
			// Updating material request id
			latestRequestIds.setLatestMaterialRequestId(newMatReqNo);
			latestRequestIds = callManagementDAO.updateLatestServiceRequestId(latestRequestIds);
			List<RequestedItems> reList = saveOrUpdateRequestedItems(requestedItems,materialRequest.getMaterialRequestId());
			//			mRequest = materialRequestIntegraterFactory.createMaterialRequestIntegrater();
			materialRequestIntegrater.setMaterialRequest(materialRequest);
			materialRequestIntegrater.setRequestedItemList(reList);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateMaterialRequest, "+e.getMessage());
		}
		return materialRequestIntegrater;
	}

	private MaterialRequestIntegrater getMaterialRequestIntegrater(Object object){
		MaterialRequestIntegrater materialRequestIntegrater = materialRequestIntegraterFactory.createMaterialRequestIntegrater();
		LinkedHashMap<String, Object> lhm = (LinkedHashMap<String, Object>) object;
		LinkedHashMap<String, String> viewObj=null;
		// Generating a Set of entries
		Set set = lhm.entrySet();
		// Displaying elements of LinkedHashMap
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry me = (Map.Entry)iterator.next();
			System.out.print("MetrialRequestBusiness, getMaterialRequestIntegrater, Key is: "+ me.getKey() + 
					"& Value is: "+me.getValue()+"\n");
			viewObj = (LinkedHashMap<String, String>) me.getValue();
		}
		String cdId = String.valueOf(viewObj.get("cdId"));
		String dueDateStr = String.valueOf(viewObj.get("dueDate"));
		String remarks = String.valueOf(viewObj.get("remarks"));
		Date dueDate = utilService.getDateFromString(dueDateStr);
		String name = String.valueOf(viewObj.get("name"));
		String address = String.valueOf(viewObj.get("address"));
		String state = String.valueOf(viewObj.get("state"));
		String country = String.valueOf(viewObj.get("country"));
		String pincode = String.valueOf(viewObj.get("pincode"));
		String phoneNumber1 = String.valueOf(viewObj.get("phoneNumber1"));
		String phoneNumber2 = String.valueOf(viewObj.get("phoneNumber2"));
		String obj = viewObj.get("requestedItemList");
		//		String obj = "{"+'"'+"requestedItems"+'"'+":"+viewObj.get("requestedItemList")+"}";
		//		String obj = "{"+viewObj.get("requestedItemList")+"}";
		//		obj.replace("[", "{").replace("]", "}");
		//		System.out.println("MetrialBusines,saveOrUpdateMaterialStock,obj: "+obj);
		List<RequestedItems> requestedItems = getRequestedItemsFromJson(obj);
		//		System.out.println("MetrialBusines,saveOrUpdateMaterialStock,getCdId: "+cdId+",dueDate: "+dueDate);
		//		System.out.println("MetrialBusines,saveOrUpdateMaterialStock,obj: "+obj);
		MaterialRequest materialRequest = materialRequestFactory.createMaterialRequest();
		materialRequest.setCdId(Integer.parseInt(cdId));
		materialRequest.setDueDate(dueDate);
		materialRequest.setRemarks(remarks);

		materialRequest.setName(name);
		materialRequest.setAddress(address);
		materialRequest.setState(state);
		materialRequest.setCountry(country);
		materialRequest.setPincode(pincode);
		materialRequest.setPhoneNumber1(phoneNumber1);
		materialRequest.setPhoneNumber2(phoneNumber2);

		materialRequestIntegrater.setMaterialRequest(materialRequest);
		materialRequestIntegrater.setRequestedItemList(requestedItems);
		return materialRequestIntegrater;
	}
	/**
	 *  Creating List of RequestedItems
	 * @param json
	 * @return
	 */
	private List<RequestedItems> getRequestedItemsFromJson(String json){
		List<RequestedItems> items = null;
		try{
			ObjectMapper mapper = new ObjectMapper();
			//			System.out.println("MetrialBusines,getRequestedItemsFromJson,json: "+json);
			items = Arrays.asList(mapper.readValue(json, RequestedItems[].class));
			//			System.out.println("MetrialBusines,getRequestedItemsFromJson,size: "+items.size());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getRequestedItemsFromJson, "+e.getMessage());
		}
		return items;
	}

	@Override
	public MaterialRequest getMaterialRequestById(int materialRequestId) {
		MaterialRequest mRequest = null;
		try{
			//			System.out.println("MatReqBusiness,getMaterialRequestById,materialRequestId : "+materialRequestId);
			mRequest = materialRequestDAO.getMaterialRequestById(materialRequestId);
			List<RequestedItems> reItems = materialRequestDAO.getRequestedItemsByMatReqId(mRequest.getMaterialRequestId());
			//			for(RequestedItems rt: reItems){
			//				System.out.println("MatReqBusiness,getMaterialRequestById,materialRequestId : "
			//			+rt.getMaterialRequestId()+", MaterialStockId: "+rt.getMaterialStockId());
			//			}
			mRequest.setRequestedItems(reItems);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getMaterialRequestById, "+e.getMessage());
		}
		return mRequest;
	}

	@Override
	public MaterialRequest deleteMaterialRequestById(String token,int materialRequestId) {
		MaterialRequest mRequest = null;
		boolean deleteStatus = false;
		try{
			mRequest = materialRequestDAO.getMaterialRequestById(materialRequestId);
			Employee employee = userService.getEmployeeByToken(token);
			Timestamp timestamp = utilService.dateToTimeStamp(utilService.getCurrentDateAndTime());
			/**
			 *  Making audit logs
			 */
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
//			System.out.println("MaterialRequestServiceImpl,deleteMaterialRequestById, Emp RoleId: "+employee.getRole().getRoleId()+", user name: "+employee.getUserName());
			switch (employee.getRole().getRoleId()) {

			case 1: //Super Admin
			case 2: // Admin
				// Deletion is possible, if the StatusInfo is only in OPEN (1), stage
				if(mRequest.getStatusInfoId() == 1){
					mRequest.setIsActive(0);
					auditJson.setActionType("delete");
					deleteStatus = true;
				}if(mRequest.getStatusInfoId() == 2){ // In Progress
					mRequest.setIsActive(4); // Suspended
					auditJson.setActionType("suspended");
					deleteStatus = true;
				}
				
			case 3: // Employee
				if(mRequest.getStatusInfoId() == 1){
					mRequest.setIsActive(0);
					auditJson.setActionType("delete");
					deleteStatus = true;
				}
			}
			// Checking the StatusInfoId = 1 or 2
			if(deleteStatus ){
				mRequest.setUpdatedAt(timestamp);
				mRequest = materialRequestDAO.saveOrUpdateMaterialRequest(mRequest);
				auditLog.setEmployeeId(employee.getEmployeeId());
				auditJson.setType("MaterialRequest");
				auditJson.setId(Integer.toString(materialRequestId));
				auditJson.setRemarks(mRequest.getMetrialRequestNumber());

				ObjectMapper mapper = new ObjectMapper();
				String jSon = mapper.writeValueAsString(auditJson);

				//saving as jSon
				auditLog.setAuditLog(jSon);
				auditLog = utilService.saveOrUpdateAuditLog(auditLog);
			}
//			System.out.println("MaterialRequestServiceImpl,deleteMaterialRequestById, deleteStatus: "+deleteStatus
//					+", StatusInfoId: "+mRequest.getStatusInfoId());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteMaterialRequestById, "+e.getMessage());
		}
		return mRequest;
	}

	@Override
	public List<RequestedItems> saveOrUpdateRequestedItems(List<RequestedItems> requestedItems,int materialRequestId) {
		List<RequestedItems> rItems = null;
		try{
			rItems = new ArrayList<RequestedItems>();
			for(RequestedItems ri: requestedItems){
				ri.setMaterialRequestId(materialRequestId);
				int reqQuantity = ri.getRequestedQuantity();
				ri.setBalanceItemToSend(reqQuantity);
				RequestedItems rItem = materialRequestDAO.saveOrUpdateRequestedItems(ri);
				rItems.add(rItem);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateRequestedItems, "+e.getMessage());
		}
		return rItems;
	}

	@Override
	public List<RequestedItems> getRequestedItemsByMatReqId(
			int materialRequestId) {
		List<RequestedItems> requestedItems = null;
		try{
			requestedItems = materialRequestDAO.getRequestedItemsByMatReqId(materialRequestId);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getRequestedItemsByMatReqId, "+e.getMessage());
		}
		return requestedItems;
	}

	@Override
	public int deleteRequestedItemById(int materialRequestId) {
		return materialRequestDAO.deleteRequestedItemById(materialRequestId);
	}

	/**
	 *  Checking the stock list
	 */
	@Override
	public boolean checkStockStatus(List<DespatchedItems> despatchedItems) {
		boolean status = false;
		try{
			for(DespatchedItems dItems: despatchedItems){
				MaterialStockInfo stockInfo = materialRequestDAO.
						getMaterialStockInfoById(dItems.getMaterialStockId());
				//				System.out.println("MatReqBusiness,checkStockStatus,MaterialStockId: "+dItems.getMaterialStockId());
				if(stockInfo.getNoOfStocks() >= dItems.getDespatchQuantity()){
					status = true;
				}else{
					status = false;
				}
			}

		}catch(Exception e){
			status = false;
			e.printStackTrace();
			logger.error("checkStockStatus, "+e.getMessage());
		}
		return status;
	}

	@Override
	public DespatchDetailsIntegrator saveOrUpdateDespatchDetails(
			String token,Object object) {
		DespatchDetailsIntegrator despatchDetailsIntegrator = null;
		try{
			despatchDetailsIntegrator = despatchDetailsIntegratorFactory.createDespatchDetailsIntegrator();
			Employee employee = userService.getEmployeeByToken(token);
			DespatchDetailsIntegrator dIntegrater = getDespatchDetailsIntegrator(object);
			DespatchDetails despatchDetails = dIntegrater.getDespatchDetails();
			Date today = utilService.getCurrentDateAndTime();
			despatchDetails.setCreatedAt(utilService.dateToTimeStamp(today));
			LatestRequestIds latestRequestIds = callManagementDAO.getLastServiceRequestId(1);
			String newDespatchNo = utilService.createDespatchId(latestRequestIds.getLatestDespatchId());
			despatchDetails.setDespatchNumber(newDespatchNo);
			// Despatch status as SENT - 1
			despatchDetails.setDespatchStatusId(1);
			List<DespatchedItems> despatchedItems = dIntegrater.getDespatchedItems();
			// Updating Material Request, Status Info , OPEN -> IN PROGRESS
			MaterialRequest materialRequest = getMaterialRequestById(despatchDetails.getMaterialRequestId());
			Map<Integer, RequestedItems> reqMap = new Hashtable<Integer, RequestedItems>();
			for(RequestedItems rItems: materialRequest.getRequestedItems()){
				reqMap.put(rItems.getMaterialStockId(), rItems);
			}

			Map<Integer,DespatchedItems> hashMap = new Hashtable<Integer, DespatchedItems>();
			// If the same items came more than once, then adding the total quantitys
			for(DespatchedItems dItems: despatchedItems){
				if(!hashMap.containsKey(dItems.getMaterialStockId())){
					hashMap.put(dItems.getMaterialStockId(), dItems);
				}else{
					DespatchedItems item = hashMap.get(dItems.getMaterialStockId());
					int quantity = item.getDespatchQuantity();
					item.setDespatchQuantity(quantity+dItems.getDespatchQuantity());
					hashMap.put(dItems.getMaterialStockId(), item);
				}
			}
			// updatedDespatchedItems -> only for verifying stock details
			ArrayList<DespatchedItems> updatedDespatchedItems = new ArrayList<DespatchedItems>(hashMap.values());
			//			System.out.println(",MatReqBusiness,saveOrUpdateDespatchDetails,updatedDespatchedItems, size:"+updatedDespatchedItems.size());
			/**
			 *  Code for update stock based on despatch
			 */
			boolean stockStatus = checkStockStatus(updatedDespatchedItems);

			System.out.println(",MatReqBusiness,saveOrUpdateDespatchDetails,stockStatus: "+stockStatus);
			if(stockStatus){
				List<DespatchedItems > dList = new ArrayList<DespatchedItems>();
				despatchDetails.setEmployeeId(employee.getEmployeeId());
				despatchDetails.setIsActive(1);
				despatchDetails = materialRequestDAO.saveOrUpdateDespatchDetails(despatchDetails);

				// Updating despatch Id
				latestRequestIds.setLatestDespatchId(despatchDetails.getDespatchNumber());
				latestRequestIds = callManagementDAO.updateLatestServiceRequestId(latestRequestIds);

				RequestedItems rItems = new RequestedItems() ;
				for(DespatchedItems dItems: despatchedItems){
					dItems.setDespatchDetailsId(despatchDetails.getDespatchDetailsId());
					rItems = reqMap.get(dItems.getMaterialStockId());
					rItems.setBalanceItemToSend(dItems.getBalanceItemToSend());
					// Shipping status
					if(dItems.getBalanceItemToSend() == 0){
						materialRequest.setShippingStatus(1);// Completed
						materialRequest.setStatusInfoId(3);
					}else{
						materialRequest.setShippingStatus(0); // Not completed
						materialRequest.setStatusInfoId(2);
					}
					DespatchedItems item = materialRequestDAO.saveOrUpdateDespatchedItems(dItems);
					rItems = materialRequestDAO.saveOrUpdateRequestedItems(rItems);
					dList.add(item);
				}

				dIntegrater = despatchDetailsIntegratorFactory.createDespatchDetailsIntegrator();
				dIntegrater.setDespatchDetails(despatchDetails);
				dIntegrater.setDespatchedItems(dList);
				dIntegrater.getDespatchDetails().setReturnMessage("success");
				// Updating stock table
				boolean stockStaus = updateStock(updatedDespatchedItems);
				// If any exception occurs while updating stock
				if(!stockStaus){
					dIntegrater = null;
					//					despatchDetailsIntegrator.getDespatchDetails().setReturnMessage("Failed");
				}
				//				materialRequest.setStatusInfoId(2);

				materialRequest = materialRequestDAO.saveOrUpdateMaterialRequest(materialRequest);

				despatchDetailsIntegrator = dIntegrater;
			}else{
				// If not, sufficient stock
				despatchDetailsIntegrator.getDespatchDetails().setReturnMessage("Stock not sufficient");
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateDespatchDetails, "+e.getMessage());
			despatchDetailsIntegrator.getDespatchDetails().setReturnMessage("Failed");
		}
		return despatchDetailsIntegrator;
	}
	/**
	 *  Stock updating
	 * @param despatchedItems
	 * @return
	 */
	private boolean updateStock(List<DespatchedItems> despatchedItems){
		boolean status = false;
		try{
			for(DespatchedItems items: despatchedItems){
				MaterialStockInfo stockInfo = materialRequestDAO.
						getMaterialStockInfoById(items.getMaterialStockId());
				int stock = stockInfo.getNoOfStocks() - items.getDespatchQuantity();
				stockInfo.setNoOfStocks(stock);
				stockInfo = materialRequestDAO.saveOrUpdateMaterialStock(stockInfo);
			}
			status = true;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateStock, "+e.getMessage());
			status = false;
		}

		return status;

	}

	private DespatchDetailsIntegrator getDespatchDetailsIntegrator(Object object){
		DespatchDetailsIntegrator despatchDetailsIntegrator = despatchDetailsIntegratorFactory.createDespatchDetailsIntegrator();
		LinkedHashMap<String, Object> lhm = (LinkedHashMap<String, Object>) object;
		LinkedHashMap<String, String> viewObj=null;
		// Generating a Set of entries
		Set set = lhm.entrySet();
		// Displaying elements of LinkedHashMap
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry me = (Map.Entry)iterator.next();
			//			System.out.print("MetrialRequestBusiness, getMaterialRequestIntegrater, Key is: "+ me.getKey() + 
			//					" & Value is: "+me.getValue()+"\n");
			viewObj =  (LinkedHashMap<String, String>) me.getValue();
		}
		String matReqId = String.valueOf(viewObj.get("materialRequestId"));
		String details = String.valueOf(viewObj.get("details"));
		String courierServiceId = String.valueOf(viewObj.get("courierServiceId"));
		String trackingId = String.valueOf(viewObj.get("trackingId"));
		String despatchedItemsJson = viewObj.get("despatchItemsList");
		//		System.out.print("MetrialRequestBusiness, getMaterialRequestIntegrater, matReqId: "+ matReqId + 
		//				" & despatchedItemsJson: "+despatchedItemsJson+"\n");
		DespatchDetails despatchDetails = despatchDetailsFactory.createDespatchDetails();

		despatchDetails.setMaterialRequestId(Integer.parseInt(matReqId));
		despatchDetails.setDetails(details);
		if(courierServiceId != null){
			despatchDetails.setCourierServiceId(Integer.parseInt(courierServiceId));
		}
		despatchDetails.setTrackingId(trackingId);
		// IN PROGRESS
		despatchDetails.setStatusInfoId(2);

		List<DespatchedItems> despatchedItems = getDespatchedItemsFromJson(despatchedItemsJson);

		despatchDetailsIntegrator.setDespatchDetails(despatchDetails);
		despatchDetailsIntegrator.setDespatchedItems(despatchedItems);

		return despatchDetailsIntegrator;
	}

	/**
	 *  Creating DespatchedItems List from jSon
	 * @param json
	 * @return
	 */
	private List<DespatchedItems> getDespatchedItemsFromJson(String json){
		List<DespatchedItems> items = null;
		try{
			//			System.out.println("MatReqBusiness,getRequestedItemsFromJson,jSon : "+json);
			ObjectMapper mapper = new ObjectMapper();
			items = Arrays.asList(mapper.readValue(json, DespatchedItems[].class));
			//			System.out.println("MetrialBusines,getDespatchedItemsFromJson,size: "+items.size());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getRequestedItemsFromJson, "+e.getMessage());
		}
		return items;
	}

	@Override
	public DespatchDetails getDespatchDetailsById(int despatchDetailsId) {
		DespatchDetails despatchDetails = null;
		try{
			despatchDetails = materialRequestDAO.getDespatchDetailsById(despatchDetailsId);
			List<DespatchedItems> despatchedItems = materialRequestDAO.getDespatchedItemsByDesId(despatchDetailsId);
			despatchDetails.setDespatchedItems(despatchedItems);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getDespatchDetailsById, "+e.getMessage());
		}
		return despatchDetails;
	}

	@Override
	public List<DespatchDetails> getAllDespatchDetails(int isActive) {
		List<DespatchDetails> despatchDetailList = null;
		try{
			despatchDetailList = materialRequestDAO.getAllDespatchDetails(isActive);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllDespatchDetails, "+e.getMessage());
		}
		return despatchDetailList;
	}

	@Override
	public List<StatusInfo> getAllStatusInfo() {
		return materialRequestDAO.getAllStatusInfo();
	}

	@Override
	public List<MaterialRequest> getAllMaterialRequest(String token,
			String dateFrom,String dateTo,String searchKeyWord) {
		List<MaterialRequest> materialRequests = null;
		try{

			Employee employee = userService.getEmployeeByToken(token);
			//			System.out.println("MaterialReqBusiness,getAllMaterialRequest,role id: "+employee.getRole().getRoleId());

			Date dateFr=utilService.getDateFromString(dateFrom);
			Date dateT=utilService.getDateFromString(dateTo);
			switch (employee.getRole().getRoleId()) {
			//Super Admin
			case 1:
				//Admin
			case 2:
				if(searchKeyWord == null){
					searchKeyWord = "";
				}
				materialRequests = materialRequestDAO.getAllMaterialRequest(-1,1,dateFr,dateT,searchKeyWord);
				break;
				// Employee	
			case 3:
				materialRequests = materialRequestDAO.getAllMaterialRequest(employee.getEmployeeId(),1,dateFr,dateT,searchKeyWord);
			default:
				break;
			}
			//			System.out.println("MaterialReqBusiness,getAllMaterialRequest,materialRequests size: "+materialRequests.size());
			for(int i=0;i<materialRequests.size();i++){
				Employee emp = materialRequests.get(i).getEmployee();
				EmployeeMinData employeeMinData = userService.getEmployeeMinData(emp);
				materialRequests.get(i).setEmployeeMinData(employeeMinData);
				materialRequests.get(i).setEmployee(null);
				List<RequestedItems> items = materialRequestDAO.getRequestedItemsByMatReqId(materialRequests.get(i).getMaterialRequestId());
				materialRequests.get(i).setRequestedItems(items);

			}
			// Adding Despatch details, based on the Material Request
			materialRequests = getMaterialDespatchDetails(materialRequests);
			Collections.sort(materialRequests, new Comparator<MaterialRequest>() {
				public int compare(MaterialRequest o1, MaterialRequest o2) {
					return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
				}
			});
			Collections.reverse(materialRequests);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllMaterialRequest, "+e.getMessage());
		}
		return materialRequests;
	}



	/**
	 *  Adding despatch for each Material Request
	 * @param materialRequests
	 * @return
	 */
	private List<MaterialRequest> getMaterialDespatchDetails(List<MaterialRequest> materialRequests){
		List<MaterialRequest> maRequests = null;
		try{
			for(int i=0;i<materialRequests.size();i++){
				List<DespatchDetails> despatchDetails =  materialRequestDAO.getDespatchDetailsByMatReqId
						(materialRequests.get(i).getMaterialRequestId());
				for(int j=0;j<despatchDetails.size();j++){
					Employee employee = despatchDetails.get(j).getEmployee();
					EmployeeMinData employeeMinData = userService.getEmployeeMinData(employee);
					despatchDetails.get(j).setEmployee(null);
					despatchDetails.get(j).setEmployeeMinData(employeeMinData);
					despatchDetails.get(j).setMaterialRequest(null);
					List<DespatchedItems> despatchedItems = materialRequestDAO.
							getDespatchedItemsByDesId(despatchDetails.get(j).getDespatchDetailsId());
					for(int k=0;k<despatchedItems.size();k++){
						despatchedItems.get(k).setDespatchDetails(null);
					}
					despatchDetails.get(j).setDespatchedItems(despatchedItems);
				}
				materialRequests.get(i).setDespatchDetails(despatchDetails);
			}
			maRequests = materialRequests;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllMaterialRequestByToken, "+e.getMessage());
		}
		return maRequests;
	}

	@Override
	public List<DespatchDetails> getAllDespatchDetailsByToken(String token) {
		List<DespatchDetails> despatchDetails = null;
		try{
			Employee employee = userService.getEmployeeByToken(token);
			if(employee.getRole().getRoleId() == 1){//Super Admin
				despatchDetails = materialRequestDAO.getAllDespatchDetails(-1,1);
			}else if(employee.getRole().getRoleId() == 2){ //Admin
				despatchDetails = materialRequestDAO.getAllDespatchDetails(-1,1);
			}else if(employee.getRole().getRoleId()== 3){ // Employee
				despatchDetails = materialRequestDAO.getAllDespatchDetails(employee.getEmployeeId(),1);
			}
			for(int i=0;i<despatchDetails.size();i++){
				MaterialRequest materialRequest = despatchDetails.get(i).getMaterialRequest();
				Employee emp = materialRequest.getEmployee();
				EmployeeMinData reEmployeeMinData = userService.getEmployeeMinData(emp);
				materialRequest.setEmployee(null);
				materialRequest.setEmployeeMinData(reEmployeeMinData);

				EmployeeMinData deEmpMinData = userService.getEmployeeMinData(despatchDetails.get(i).getEmployee());
				despatchDetails.get(i).setEmployeeMinData(deEmpMinData);
				despatchDetails.get(i).setEmployee(null);
				List<DespatchedItems> items = materialRequestDAO.getDespatchedItemsByDesId(
						despatchDetails.get(i).getDespatchDetailsId());
				for(DespatchedItems di: items){
					di.setDespatchDetails(null);
				}
				despatchDetails.get(i).setDespatchedItems(items);
			}
			Collections.sort(despatchDetails, new Comparator<DespatchDetails>() {
				public int compare(DespatchDetails o1, DespatchDetails o2) {
					return o1.getCreatedAt().compareTo(o2.getCreatedAt());
				}
			});
			Collections.reverse(despatchDetails);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllDespatchDetailsByToken, "+e.getMessage());
		}
		return despatchDetails;
	}

	@Override
	public List<CourierServiceDetails> getAllCourierServiceDetails(int isActive) {
		List<CourierServiceDetails> courierServiceDetails = null;
		try{
			courierServiceDetails = materialRequestDAO.getAllCourierServiceDetails(isActive);
			for(int i=0;i<courierServiceDetails.size();i++){
				courierServiceDetails.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllCourierServiceDetails, "+e.getMessage());
		}
		return courierServiceDetails;
	}

	@Override
	public List<DespatchDetails> getDespatchDetailsByMatReqId(
			int materialRequestId) {
		List<DespatchDetails> despatchDetails = null;
		try{
			despatchDetails = materialRequestDAO.getDespatchDetailsByMatReqId(materialRequestId);
			for(int i=0;i<despatchDetails.size();i++){
				EmployeeMinData deEmpMinData = userService.getEmployeeMinData(despatchDetails.get(i).getEmployee());
				despatchDetails.get(i).setEmployeeMinData(deEmpMinData);
				despatchDetails.get(i).setEmployee(null);
				List<DespatchedItems> despatchedItems = materialRequestDAO.getDespatchedItemsByDesId
						(despatchDetails.get(i).getDespatchDetailsId());
				despatchDetails.get(i).setDespatchedItems(despatchedItems);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getDespatchDetailsByMatReqId, "+e.getMessage());
		}

		return despatchDetails;
	}

	@Override
	public int deleteDespatchDetailsById(int despatchDetailsId) {
		int status = 0;
		try{
			DespatchDetails despatchDetails = materialRequestDAO.getDespatchDetailsById(despatchDetailsId);
			despatchDetails.setIsActive(0);
			despatchDetails = materialRequestDAO.saveOrUpdateDespatchDetails(despatchDetails);
			status = 1;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteDespatchDetailsById, "+e.getMessage());
		}
		return status;
	}

	@Override
	public List<ItemReceivedInfo> getAllItemReceivedInfo() {
		return materialRequestDAO.getAllItemReceivedInfo();
	}

	@Override
	public DespatchDetails updateDespatchStatus(String token,
			int despatchDetailsId) {
		DespatchDetails desDetails = null;
		try{
			//			despatchDetails = materialRequestDAO.getDespatchDetailsById(despatchDetailsId);
			Employee employee = userService.getEmployeeByToken(token);
			DespatchDetails despatchDetails = materialRequestDAO.getDespatchDetailsById(despatchDetailsId);
			// Set status, 2 -> Received
			despatchDetails.setDespatchStatusId(2);
			desDetails = materialRequestDAO.saveOrUpdateDespatchDetails(despatchDetails);
			ItemReceivedInfo itemReceivedInfo = itemReceivedInfoFactory.createItemReceivedInfo();
			itemReceivedInfo.setEmployeeId(employee.getEmployeeId());
			Date today = utilService.getCurrentDateAndTime();
			Timestamp timestamp = utilService.dateToTimeStamp(today);
			itemReceivedInfo.setReceivedDate(timestamp);
			itemReceivedInfo = materialRequestDAO.saveOrUpdateItemReceivedInfo(itemReceivedInfo);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateDespatchStatus, "+e.getMessage());
		}
		return desDetails;
	}

	/**
	 *  Getting OPEN & IN PROGRESS, MaterialRequest details
	 */
	@Override
	public List<MaterialRequest> getAllOpenMaterialRequest() {
		List<MaterialRequest> materialRequests = null;
		try{
			materialRequests = new ArrayList<MaterialRequest>();
			List<MaterialRequest> mList= materialRequestDAO.getAllMaterialRequest(0, 1, null, null, "");
			for(MaterialRequest mr: mList){
				// 2 -> Completed
				if(mr.getStatusInfoId() != 2){
					EmployeeMinData employeeMinData = userService.getEmployeeMinData(mr.getEmployee());
					mr.setEmployeeMinData(employeeMinData);
					mr.setEmployee(null);
					materialRequests.add(mr);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllOpenMaterialRequest, "+e.getMessage());
		}
		return materialRequests;
	}

	@Override
	public MaterialCategory saveOrUpdateMaterialCategory(
			MaterialCategory materialCategory) {
		MaterialCategory maCategory = null;
		try{
			materialCategory.setIsActive(1);
			Date date = utilService.getCurrentDateAndTime();
			materialCategory.setUpdatedAt(date);
			maCategory = materialRequestDAO.saveOrUpdateMaterialCategory(materialCategory);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateMaterialCategory, "+e.getMessage());
		}
		return maCategory;
	}

	@Override
	public List<MaterialCategory> getAllMaterialCategory(int isActive) {
		List<MaterialCategory> materialCategories = null;
		try{
			materialCategories = materialRequestDAO.getAllMaterialCategory(isActive);
			for(int i=0;i<materialCategories.size();i++){
				materialCategories.get(i).setSlNo(i+1);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllMaterialCategory, "+e.getMessage());
		}
		return materialCategories;
	}

	@Override
	public MaterialCategory getMaterialCategoryById(int materialCategoryId) {
		MaterialCategory materialCategory = null;
		try{
			materialCategory = materialRequestDAO.getMaterialCategoryById(materialCategoryId);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getMaterialCategoryById, "+e.getMessage());
		}
		return materialCategory;
	}

	@Override
	public MaterialCategory deleteMaterialCategory(
			int materialCategoryId,String token) {
		MaterialCategory mCategory = null;
		try{
			mCategory = materialRequestDAO.getMaterialCategoryById(materialCategoryId);
			mCategory.setIsActive(0);
			mCategory = materialRequestDAO.saveOrUpdateMaterialCategory(mCategory);

			/**
			 *  Making audit logs
			 */
			Employee employee = userService.getEmployeeByToken(token);
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(employee.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("MaterialCategory");
			auditJson.setId(Integer.toString(materialCategoryId));
			auditJson.setRemarks(mCategory.getMaterialCategory());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);

		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteMaterialCategory, "+e.getMessage());
		}
		return mCategory;
	}

	@Override
	public List<MaterialStockInfo> getMaterialStockInfoByCategoryId(
			int materialCategoryId) {
		List<MaterialStockInfo> materialStockInfos = null;
		try{
			materialStockInfos = materialRequestDAO.getMaterialStockInfoByCategoryId(materialCategoryId);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getMaterialStockInfoByCategoryId, "+e.getMessage());
		}
		return materialStockInfos;
	}

	@Override
	public CourierServiceDetails saveOrUpdateCourierServiceDetails(CourierServiceDetails
			courierServiceDetails){
		CourierServiceDetails cServiceDetails = null;
		try{
			Timestamp timeSt=utilService.dateToTimeStamp(utilService.getCurrentDateAndTime());
			courierServiceDetails.setCreatedAt(timeSt);
			courierServiceDetails.setIsActive(1);
			cServiceDetails = materialRequestDAO.saveOrUpdateCourierServiceDetails(courierServiceDetails);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateCourierServiceDetails, "+e.getMessage());
		}
		return cServiceDetails;


	}

	@Override
	public CourierServiceDetails getCourierServiceDetailsById(
			int courierServiceId) {
		CourierServiceDetails courierServiceDetails = null;
		try{
			courierServiceDetails = materialRequestDAO.getCourierServiceDetailsById(courierServiceId);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCourierServiceDetailsById, "+e.getMessage());
		}
		return courierServiceDetails;
	}

	@Override
	public CourierServiceDetails deleteCourierServiceDetailsById(
			int courierServiceId,String token) {
		CourierServiceDetails courierServiceDetails = null;
		try{
			courierServiceDetails = materialRequestDAO.getCourierServiceDetailsById(courierServiceId);
			courierServiceDetails.setIsActive(0);
			Timestamp timeSt=utilService.dateToTimeStamp(utilService.getCurrentDateAndTime());
			courierServiceDetails.setCreatedAt(timeSt);
			courierServiceDetails = materialRequestDAO.saveOrUpdateCourierServiceDetails(courierServiceDetails);

			/**
			 *  Making audit logs
			 */
			Employee employee = userService.getEmployeeByToken(token);
			AuditLog auditLog = auditLogFactory.createAuditLog();
			AuditJson auditJson = auditJsonFactory.createAuditJson();
			auditLog.setEmployeeId(employee.getEmployeeId());
			auditJson.setActionType("delete");
			auditJson.setType("CourierServiceDetails");
			auditJson.setId(Integer.toString(courierServiceId));
			auditJson.setRemarks(courierServiceDetails.getCourierCompanyName());

			ObjectMapper mapper = new ObjectMapper();
			String jSon = mapper.writeValueAsString(auditJson);

			//saving as jSon
			auditLog.setAuditLog(jSon);
			auditLog = utilService.saveOrUpdateAuditLog(auditLog);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCourierServiceDetailsById, "+e.getMessage());
		}
		return courierServiceDetails;
	}

	@Override
	public DespatchReceivedStatus saveOrUpdateDespatchReceivedStatus(
			DespatchReceivedStatus despatchReceivedStatus, String token) {
		DespatchReceivedStatus despReceivedStatus = null;
		try{
			System.out.println("MetrialRequestBusiness,saveOrUpdateDespatchReceivedStatus,despatchDetailsId: "+despatchReceivedStatus.getDespatchDetailsId());
			Timestamp timeSt=utilService.dateToTimeStamp(utilService.getCurrentDateAndTime());
			despatchReceivedStatus.setCreatedAt(timeSt);
			Employee employee = userService.getEmployeeByToken(token);
			despatchReceivedStatus.setEmployeeId(employee.getEmployeeId());
			despReceivedStatus = materialRequestDAO.saveOrUpdateDespatchReceivedStatus(despatchReceivedStatus);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateDespatchReceivedStatus, "+e.getMessage());
		}
		return despReceivedStatus;
	}


	//	@Override
	//	public List<MaterialStockInfo> getMaterialStockByMaterialName(
	//			String materialName) {
	//		List<MaterialStockInfo> materialStockInfos = null;
	//		try{
	//			materialStockInfos = materialRequestDAO.getMaterialStockByMaterialName(materialName);
	//		}catch(Exception e){
	//			e.printStackTrace();
	//			logger.error("getMaterialStockByMaterialName, "+e.getMessage());
	//		}
	//		return materialStockInfos;
	//	}

	/**
	@Override
	public List<MaterialStockInfo> getAllMaterialName(int isActive) {
		List<MaterialStockInfo> stockInfos = null;
		try{

//			List<MaterialCategory> materialCategories = materialRequestDAO.getAllMaterialCategory(1);
//			for(int i=0;i<materialCategories.size();i++){
//				
//				List<MaterialStockInfo> materialStockInfos = materialRequestDAO
//						.getMaterialStockByMaterialName(materialCategories.get(i).getMaterialCategory());
//				for(int j=0;j<materialStockInfos.size();j++){
//					materialStockInfos.get(j).setMaterialCategoryId(materialCategories.get(i).getMaterialCategoryId());
//					MaterialStockInfo materialStockInfo = materialRequestDAO.
//							saveOrUpdateMaterialStock(materialStockInfos.get(j));
//					System.out.println("Stock id: "+materialStockInfo.getMaterialStockId()+
//							", MaterialCategoryId: "+materialStockInfo.getMaterialCategoryId()+", MaterialName: "+materialStockInfo.getMaterialName());
//				}
//			}
//			stockInfos = materialRequestDAO.getAllMaterialName(isActive);
//			Hashtable<String, String> hashtable = new Hashtable<String, String>();
//			for(int i=0;i<stockInfos.size();i++){
//				hashtable.put(stockInfos.get(i).getMaterialName(), stockInfos.get(i).getMaterialName());
//			}
//			ArrayList<String> stock = new ArrayList<String>(hashtable.keySet());
//			for(int j=0;j<stock.size();j++){
//				MaterialCategory materialCategory = new MaterialCategory();
//				materialCategory.setMaterialCategory(stock.get(j));
//				materialCategory.setIsActive(1);
//				materialRequestDAO.saveOrUpdateMaterialCategory(materialCategory);
//			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getMaterialStockByMaterialName, "+e.getMessage());
		}
		return stockInfos;
	}
	 **/

}
