package com.yaz.alind.dao;

import java.util.Date;
import java.util.List;

import com.yaz.alind.model.CourierServiceDetails;
import com.yaz.alind.model.DespatchDetails;
import com.yaz.alind.model.DespatchReceivedStatus;
import com.yaz.alind.model.DespatchedItems;
import com.yaz.alind.model.ItemReceivedInfo;
import com.yaz.alind.model.MaterialCategory;
import com.yaz.alind.model.MaterialRequest;
import com.yaz.alind.model.MaterialStockInfo;
import com.yaz.alind.model.RequestedItems;
import com.yaz.alind.model.StatusInfo;

public interface MaterialRequestDAO {

	public List<MaterialStockInfo> getAllMaterialStockInfo(int isActive);
	public List<MaterialStockInfo> getAllMaterialName(int isActive);
	public MaterialStockInfo saveOrUpdateMaterialStock(MaterialStockInfo materialStockInfo);
	public MaterialStockInfo getMaterialStockInfoById(int materialStockId);
	public MaterialRequest saveOrUpdateMaterialRequest(MaterialRequest materialRequest);
	public MaterialRequest getMaterialRequestById(int materialRequestId);
//	public List<MaterialStockInfo> getMaterialStockByMaterialName(String materialName);
	
	
	public RequestedItems saveOrUpdateRequestedItems(RequestedItems requestedItems);
	public List<RequestedItems> getRequestedItemsByMatReqId(int materialRequestId);
	public int deleteRequestedItemById(int materialRequestId);
	public CourierServiceDetails saveOrUpdateCourierServiceDetails(CourierServiceDetails courierServiceDetails);
	public List<CourierServiceDetails> getAllCourierServiceDetails(int isActive);
	public CourierServiceDetails getCourierServiceDetailsById(int courierServiceId);
	public DespatchDetails saveOrUpdateDespatchDetails(DespatchDetails despatchDetails);
	public DespatchDetails getDespatchDetailsById(int despatchDetailsId);
	public List<DespatchDetails> getAllDespatchDetails(int isActive);
	public DespatchedItems saveOrUpdateDespatchedItems(DespatchedItems despatchedItems);
	public List<DespatchedItems> getDespatchedItemsByDesId(int despatchDetailsId);
	public int deleteDespatchedItemsById(int despatchDetailsId);
	public List<StatusInfo> getAllStatusInfo();
	public List<MaterialRequest> getAllMaterialRequest(int employeeId,int isActive, Date dateFr,
			Date dateTo,String searchKeyWord);
	public List<DespatchDetails> getAllDespatchDetails(int employeeId,int isActive);
	public List<DespatchDetails> getDespatchDetailsByMatReqId(int materialRequestId);
	public List<ItemReceivedInfo> getAllItemReceivedInfo();
	public ItemReceivedInfo getItemReceivedById(int itemReceivedId);
	public ItemReceivedInfo saveOrUpdateItemReceivedInfo(ItemReceivedInfo itemReceivedInfo);
	public MaterialCategory saveOrUpdateMaterialCategory(MaterialCategory materialCategory);
	public List<MaterialCategory> getAllMaterialCategory(int isActive);
	public MaterialCategory getMaterialCategoryById(int materialCategoryId);
	public List<MaterialStockInfo> getMaterialStockInfoByCategoryId(int materialCategoryId);
	
	public DespatchReceivedStatus saveOrUpdateDespatchReceivedStatus(DespatchReceivedStatus despatchReceivedStatus);
	
	


}
