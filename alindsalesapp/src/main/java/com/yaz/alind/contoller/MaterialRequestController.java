package com.yaz.alind.contoller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yaz.alind.model.CourierServiceDetails;
import com.yaz.alind.model.DespatchDetails;
import com.yaz.alind.model.DespatchDetailsIntegrator;
import com.yaz.alind.model.DespatchReceivedStatus;
import com.yaz.alind.model.MaterialCategory;
import com.yaz.alind.model.MaterialRequest;
import com.yaz.alind.model.MaterialRequestIntegrater;
import com.yaz.alind.model.MaterialStockInfo;
import com.yaz.alind.model.StatusInfo;
import com.yaz.alind.service.MetrialRequestService;
import com.yaz.alind.service.UserService;
import com.yaz.alind.service.UtilService;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class MaterialRequestController {

	private static final Logger logger = LoggerFactory.getLogger(MaterialRequestController.class);

	@Autowired
	MetrialRequestService materialRequestService;
	@Autowired
	UserService userService;
	@Autowired
	UtilService utilService;

	@RequestMapping(value="/mreq/getMaterialStockInfo", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getMaterialStockInfo(@RequestHeader("token") String token
			,int isActive) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getMaterialStockInfo,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<MaterialStockInfo> materialStockInfos = materialRequestService.getAllMaterialStockInfo(isActive);
				resultMap.put("materialStockInfos", materialStockInfos);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getMaterialStockInfo, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/getMaterialStockInfoByCategoryId", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getMaterialStockInfoByCategoryId(@RequestHeader("token") String token
			,int materialCategoryId) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getMaterialStockInfo,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<MaterialStockInfo> materialStockInfos = materialRequestService.getMaterialStockInfoByCategoryId(materialCategoryId);
				resultMap.put("materialStockInfos", materialStockInfos);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getMaterialStockInfo, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/saveOrUpdateMaterialCategory", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateMaterialCategory(@RequestHeader("token") String token
			,@RequestBody MaterialCategory materialCategory) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,saveOrUpdateMaterialCategory,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				MaterialCategory matCategory = materialRequestService.saveOrUpdateMaterialCategory(materialCategory);
				resultMap.put("materialCategory", matCategory);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateMaterialCategory, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}
	@RequestMapping(value="/mreq/getAllMaterialCategory", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllMaterialCategory(@RequestHeader("token") String token
			,int isActive) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getAllMaterialCategory,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<MaterialCategory> materialCategories = materialRequestService.getAllMaterialCategory(isActive);
				resultMap.put("materialCategories", materialCategories);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllMaterialCategory, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/deleteMaterialCategory", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteMaterialCategory(@RequestHeader("token") String token
			,int materialCategoryId) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,deleteMaterialCategory,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				MaterialCategory materialCategory = materialRequestService.deleteMaterialCategory(materialCategoryId,token);
				resultMap.put("materialCategory", materialCategory);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteMaterialCategory, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/getMaterialCategoryById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getMaterialCategoryById(@RequestHeader("token") String token
			,int materialCategoryId) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getMaterialCategoryById,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				MaterialCategory materialCategory = materialRequestService.getMaterialCategoryById(materialCategoryId);
				resultMap.put("materialCategory", materialCategory);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getMaterialCategoryById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/saveOrUpdateMaterialStock", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateMaterialStock(@RequestHeader("token") String token
			,@RequestBody MaterialStockInfo materialStockInfo) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,saveOrUpdateMaterialStock,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				MaterialStockInfo materialStk = materialRequestService.saveOrUpdateMaterialStock(materialStockInfo);
				resultMap.put("materialStockInfo", materialStk);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateMaterialStock, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/deleteMaterialStockInfo", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteMaterialStockInfo(@RequestHeader("token") String token
			,int materialStockId) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,deleteMaterialStockInfo,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				MaterialStockInfo materialStockInfo = materialRequestService.deleteMaterialStockInfo(materialStockId);
				resultMap.put("materialStockInfo", materialStockInfo);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteMaterialStockInfo, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/getMaterialStockInfoById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getMaterialStockInfoById(@RequestHeader("token") String token
			,int materialStockId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getMaterialStockInfoById,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				MaterialStockInfo materialStockInfo = materialRequestService.getMaterialStockInfoById(materialStockId);
				resultMap.put("materialStockInfo", materialStockInfo);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getMaterialStockInfoById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	/**
	 * MaterialRequestIntegrater - MaterialRequest & Collection of RequestedItems
	 * 
	 * @param token
	 * @param materialRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mreq/saveOrUpdateMaterialRequest", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateMaterialRequest(@RequestHeader("token") String token
			,@RequestBody Object object) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,saveOrUpdateMaterialStock,token: "+token+", tokenStatus: "+tokenStatus);
			//			System.out.println("MetrialRequestController,saveOrUpdateMaterialStock,getCdId: "+materialRequestIntegrater.getCdId());

			//			List<RequestedItems> items = (List<RequestedItems>) obj;
			//			System.out.println("MetrialRequestController,saveOrUpdateMaterialStock,getCdId: "+cdId+",dueDate: "+dueDate);

			if(tokenStatus){
				MaterialRequestIntegrater materialStk = materialRequestService.saveOrUpdateMaterialRequest(token,object);
				//				resultMap.put("materialRequest", materialStk);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateMaterialRequest, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/deleteMaterialRequestById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteMaterialRequestById(@RequestHeader("token") String token
			,int materialRequestId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,deleteMaterialRequestById,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				MaterialRequest materialRequest = materialRequestService.deleteMaterialRequestById(token,materialRequestId);
				resultMap.put("materialRequest", materialRequest);
				if(materialRequest.getIsActive() == 0){
					resultMap.put("status", "success");
				}else{
					resultMap.put("status", "error");
				}
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "error");
			logger.error("deleteMaterialRequestById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/getMaterialRequestById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getMaterialRequestById(@RequestHeader("token") String token
			,int materialRequestId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getMaterialRequestById,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				MaterialRequest materialRequest = materialRequestService.getMaterialRequestById(materialRequestId);
				resultMap.put("materialRequest", materialRequest);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getMaterialRequestById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/saveOrUpdateDespatchDetails", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateDespatchDetails(@RequestHeader("token") String token
			,@RequestBody Object object) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,saveOrUpdateDespatchDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				DespatchDetailsIntegrator desDetailsIntegrator = materialRequestService.saveOrUpdateDespatchDetails(token,object);
				System.out.println("MetrialRequestController,saveOrUpdateDespatchDetails,desDetailsIntegrator, return message: "+desDetailsIntegrator.getDespatchDetails().getReturnMessage());
				resultMap.put("despatchDetailsIntegrator", desDetailsIntegrator);
				resultMap.put("status", desDetailsIntegrator.getDespatchDetails().getReturnMessage());
				//				if(desDetailsIntegrator != null){
				//					resultMap.put("status", "success");
				//				}else{
				//					resultMap.put("status", "failed");
				//				}
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateDespatchDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/getDespatchDetailsById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getDespatchDetailsById(@RequestHeader("token") String token
			,int despatchDetailsId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getDespatchDetailsById,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				DespatchDetails despatchDetails = materialRequestService.getDespatchDetailsById(despatchDetailsId);
				resultMap.put("despatchDetails", despatchDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getDespatchDetailsById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	//	@RequestMapping(value="/mreq/getAllDespatchDetails", method = RequestMethod.GET)
	//	public ResponseEntity<Map<String,Object>> getAllDespatchDetails(@RequestHeader("token") String token
	//			,int isActive) throws Exception {
	//		Map<String,Object> resultMap = null;
	//		boolean tokenStatus = false;
	//		try{
	//			resultMap = new HashMap<String,Object>();
	//			tokenStatus = utilService.evaluateToken(token);
	//			System.out.println("MetrialRequestController,getAllDespatchDetails,token: "+token+", tokenStatus: "+tokenStatus);
	//			if(tokenStatus){
	//				List<DespatchDetails> despatchDetailList = materialRequestService.getAllDespatchDetails(isActive);
	//				resultMap.put("despatchDetailList", despatchDetailList);
	//				resultMap.put("status", "success");
	//			}else{
	//				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
	//			}
	//		}catch(Exception e){
	//			e.printStackTrace();
	//			resultMap.put("status", "failed");
	//			logger.error("getAllDespatchDetails, "+e.getMessage());
	//			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
	//		}
	//		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	//	}

	// OPEN, IN PROGRESS, COMPLETED
	@RequestMapping(value="/mreq/getAllStatusInfo", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllStatusInfo(@RequestHeader("token") String token
			) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getAllStatusInfo,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<StatusInfo> statusInfos = materialRequestService.getAllStatusInfo();
				resultMap.put("statusInfos", statusInfos);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllStatusInfo, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/getAllMaterialRequest", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllMaterialRequest(@RequestHeader("token") String token
			,String dateFrom,String dateTo,String searchKeyWord) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
//			System.out.println("MetrialRequestController,getAllMaterialRequest,dateFrom: "+dateFrom
//					+", dateTo: "+dateTo+", searchKeyWord: "+searchKeyWord);
			if(tokenStatus){
				List<MaterialRequest> materialRequests = materialRequestService.getAllMaterialRequest(token,dateFrom,dateTo,searchKeyWord);
				resultMap.put("materialRequests", materialRequests);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllMaterialRequest, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/getAllDespatchDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllDespatchDetails(@RequestHeader("token") String token
			) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getAllDespatchDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<DespatchDetails> despatchDetails = materialRequestService.getAllDespatchDetailsByToken(token);
				resultMap.put("despatchDetails", despatchDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllDespatchDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/getAllCourierServiceDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllCourierServiceDetails(@RequestHeader("token") String token,
			String isActive) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getAllCourierServiceDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				//				List<CourierServiceDetails> courierServiceDetails = materialRequestService.getAllCourierServiceDetails(isActive);
				List<CourierServiceDetails> courierServiceDetails = materialRequestService.getAllCourierServiceDetails(1);
				resultMap.put("courierServiceDetails", courierServiceDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllCourierServiceDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/deleteDespatchDetailsById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteDespatchDetailsById(@RequestHeader("token") String token,
			int despatchDetailsId) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,deleteDespatchDetailsById,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				int status = materialRequestService.deleteDespatchDetailsById(despatchDetailsId);
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
			logger.error("deleteDespatchDetailsById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/updateDespatchStatus", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> updateDespatchStatus(@RequestHeader("token") String token,
			int despatchDetailsId ) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,updateDespatchStatus,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				DespatchDetails desDetails = materialRequestService.updateDespatchStatus(token,despatchDetailsId);
				resultMap.put("despatchDetails", desDetails);
				resultMap.put("status", "failed");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("updateDespatchStatus, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/getAllOpenMaterialRequest", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllOpenMaterialRequest(@RequestHeader("token") String token
			) throws Exception {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getAllOpenMaterialRequest,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<MaterialRequest> materialRequests = materialRequestService.getAllOpenMaterialRequest();
				resultMap.put("materialRequests", materialRequests);
				resultMap.put("status", "success");
			}else{
				resultMap.put("status", "error");
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "error");
			logger.error("deleteMaterialRequestById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/mreq/deleteCourierServiceDetailsById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> deleteCourierServiceDetailsById(@RequestHeader("token") String token
			,int courierServiceId) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,deleteMaterialCategory,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				CourierServiceDetails courierServiceDetails = materialRequestService.deleteCourierServiceDetailsById(courierServiceId,token);
				resultMap.put("courierServiceDetails", courierServiceDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteCourierServiceDetailsById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/mreq/getCourierServiceDetailsById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getCourierServiceDetailsById(@RequestHeader("token") String token
			,int courierServiceId) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,getCourierServiceDetailsById,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				CourierServiceDetails courierServiceDetails = materialRequestService.getCourierServiceDetailsById(courierServiceId);
				resultMap.put("courierServiceDetails", courierServiceDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getCourierServiceDetailsById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/saveOrUpdateCourierServiceDetails", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateCourierServiceDetails(@RequestHeader("token") String token
			,@RequestBody CourierServiceDetails courierServiceDetails) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,saveOrUpdateCourierServiceDetails,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				CourierServiceDetails cServiceDetails = materialRequestService.saveOrUpdateCourierServiceDetails(courierServiceDetails);
				resultMap.put("courierServiceDetails", cServiceDetails);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateCourierServiceDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/mreq/saveOrUpdateDespatchReceivedStatus", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateDespatchReceivedStatus(@RequestHeader("token") String token
			,@RequestBody DespatchReceivedStatus despatchReceivedStatus) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("MetrialRequestController,saveOrUpdateDespatchReceivedStatus,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				DespatchReceivedStatus dReceivedStatus = materialRequestService.saveOrUpdateDespatchReceivedStatus(despatchReceivedStatus,token);
				resultMap.put("despatchReceivedStatus", dReceivedStatus);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateDespatchReceivedStatus, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


}
