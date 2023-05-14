package com.yaz.alind.service;

import java.util.List;

import com.yaz.alind.model.CourierServiceDetails;
import com.yaz.alind.model.DespatchDetails;
import com.yaz.alind.model.DespatchDetailsIntegrator;
import com.yaz.alind.model.DespatchReceivedStatus;
import com.yaz.alind.model.DespatchedItems;
import com.yaz.alind.model.ItemReceivedInfo;
import com.yaz.alind.model.MaterialCategory;
import com.yaz.alind.model.MaterialRequest;
import com.yaz.alind.model.MaterialRequestIntegrater;
import com.yaz.alind.model.MaterialStockInfo;
import com.yaz.alind.model.RequestedItems;
import com.yaz.alind.model.StatusInfo;

public interface MetrialRequestService {
	public List<MaterialStockInfo> getAllMaterialStockInfo(int isActive);
	public MaterialStockInfo saveOrUpdateMaterialStock(MaterialStockInfo materialStockInfo);
	public MaterialStockInfo deleteMaterialStockInfo(int materialStockId);
	public MaterialStockInfo getMaterialStockInfoById(int materialStockId);
	public MaterialRequestIntegrater saveOrUpdateMaterialRequest(String token,Object object);
	public MaterialRequest getMaterialRequestById(int materialRequestId);
	public MaterialRequest deleteMaterialRequestById(String token,int materialRequestId);
	public List<RequestedItems> saveOrUpdateRequestedItems(List<RequestedItems> requestedItems,int materialRequestId);
	public List<RequestedItems> getRequestedItemsByMatReqId(int materialRequestId);
	public int deleteRequestedItemById(int materialRequestId);
	public boolean checkStockStatus(List<DespatchedItems> despatchedItems);
	public DespatchDetailsIntegrator saveOrUpdateDespatchDetails(String token,Object Object);
	public DespatchDetails getDespatchDetailsById(int despatchDetailsId);
	public List<DespatchDetails> getAllDespatchDetails(int isActive);
	public List<StatusInfo> getAllStatusInfo();
	public List<MaterialRequest> getAllMaterialRequest(String token,String dateFrom,String dateTo,String searchKeyWord);
	public List<DespatchDetails> getAllDespatchDetailsByToken(String token);
	public List<CourierServiceDetails> getAllCourierServiceDetails(int isActive);
	public CourierServiceDetails getCourierServiceDetailsById(int courierServiceId);
	public CourierServiceDetails deleteCourierServiceDetailsById(int courierServiceId,String token);
	public CourierServiceDetails saveOrUpdateCourierServiceDetails(CourierServiceDetails courierServiceDetails);	
	
	public List<DespatchDetails> getDespatchDetailsByMatReqId(int materialRequestId);
	public int deleteDespatchDetailsById(int despatchDetailsId);
	public List<ItemReceivedInfo> getAllItemReceivedInfo();
	public DespatchDetails updateDespatchStatus(String token,int despatchDetailsId);
	public List<MaterialRequest> getAllOpenMaterialRequest();
//	public List<MaterialStockInfo> getMaterialStockByMaterialName(String materialName);
//	public List<MaterialStockInfo> getAllMaterialName(int isActive);
	
	public MaterialCategory saveOrUpdateMaterialCategory(MaterialCategory materialCategory);
	public List<MaterialCategory> getAllMaterialCategory(int isActive);
	public MaterialCategory getMaterialCategoryById(int materialCategoryId);
	public MaterialCategory deleteMaterialCategory(int materialCategoryId,String token);
	public List<MaterialStockInfo> getMaterialStockInfoByCategoryId(int materialCategoryId);
	public DespatchReceivedStatus saveOrUpdateDespatchReceivedStatus(
			DespatchReceivedStatus despatchReceivedStatus,String token) ;
	
	
	
}
