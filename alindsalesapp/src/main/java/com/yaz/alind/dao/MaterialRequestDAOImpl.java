package com.yaz.alind.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

public class MaterialRequestDAOImpl implements MaterialRequestDAO {


	private static final Logger logger = LoggerFactory.getLogger(MaterialRequestDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<MaterialStockInfo> getAllMaterialStockInfo(int isActive) {
		List<MaterialStockInfo> materialStockInfo = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(MaterialStockInfo.class);
			if(isActive != -1){
				cr.add(Restrictions.eq("isActive", 1));
			}
			materialStockInfo = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllMaterialStockInfo: "+e.getMessage());
		}
		return materialStockInfo;
	}

	@Override
	@Transactional
	public List<MaterialStockInfo> getAllMaterialName(int isActive){
		List<MaterialStockInfo> materialStockInfo = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(MaterialStockInfo.class);
//			cr.setProjection(Projections.distinct(Projections.property("materialName")));
//			ProjectionList projectionList = Projections.projectionList();
//			projectionList.add(Projections.property("materialName"));
//			cr.setProjection(projectionList);
//			cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			cr.addOrder(Order.asc("materialName"));
			if(isActive != -1){
				cr.add(Restrictions.eq("isActive", 1));
			}
			materialStockInfo = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllMaterialName: "+e.getMessage());
		}
		return materialStockInfo;
	}
	
	@Override
	@Transactional
	public MaterialStockInfo saveOrUpdateMaterialStock(
			MaterialStockInfo materialStockInfo) {
		MaterialStockInfo maStockInfo = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(materialStockInfo);
			maStockInfo = materialStockInfo;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateMaterialStock: "+e.getMessage());
		}
		return maStockInfo;
	}

	@Override
	@Transactional
	public MaterialStockInfo getMaterialStockInfoById(int materialStockId) {
		MaterialStockInfo materialStockInfo = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(MaterialStockInfo.class);
			cr.add(Restrictions.eq("materialStockId", materialStockId));
			List list = cr.list();
			if(list.size()>0){
				materialStockInfo = (MaterialStockInfo) cr.list().get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getMaterialStockInfoById: "+e.getMessage());
		}
		return materialStockInfo;
	}
	
//	@Override
//	@Transactional
//	public List<MaterialStockInfo> getMaterialStockByMaterialName(
//			String materialName) {
//		List<MaterialStockInfo> materialStockInfos = null;
//		try{
//			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(MaterialStockInfo.class);
//			cr.add(Restrictions.eq("materialName", materialName));
//			materialStockInfos = cr.list();
//		}catch(Exception e){
//			e.printStackTrace();
//			logger.error("getMaterialStockByMaterialName: "+e.getMessage());
//		}
//		return materialStockInfos;
//	}

	@Override
	@Transactional
	public MaterialRequest saveOrUpdateMaterialRequest(
			MaterialRequest materialRequest) {
		MaterialRequest mRequest = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(materialRequest);
			mRequest = materialRequest;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateMaterialRequest: "+e.getMessage());
		}
		return mRequest;
	}

	@Override
	@Transactional
	public MaterialRequest getMaterialRequestById(int materialRequestId) {
		MaterialRequest materialRequest = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(MaterialRequest.class);
			cr.add(Restrictions.eq("materialRequestId", materialRequestId));
			List list = cr.list();
			if(list.size()>0){
				materialRequest = (MaterialRequest) cr.list().get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getMaterialRequestById: "+e.getMessage());
		}
		return materialRequest;
	}

	@Override
	@Transactional
	public RequestedItems saveOrUpdateRequestedItems(
			RequestedItems requestedItems) {
		RequestedItems reItems = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(requestedItems);
			reItems = requestedItems;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateRequestedItems: "+e.getMessage());
		}
		return reItems;
	}

	@Override
	@Transactional
	public List<RequestedItems> getRequestedItemsByMatReqId(
			int materialRequestId) {
		List<RequestedItems> requestedItems = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(RequestedItems.class);
			cr.add(Restrictions.eq("materialRequestId", materialRequestId));
			requestedItems = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getRequestedItemsByMatReqId: "+e.getMessage());
		}
		return requestedItems;
	}

	@Override
	@Transactional
	public int deleteRequestedItemById(int materialRequestId) {
		int val = 0;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(RequestedItems.class);
			cr.add(Restrictions.eq("materialRequestId", materialRequestId));
			List list = cr.list();
			if(list.size() > 0){
				this.sessionFactory.getCurrentSession().delete(list.get(0));
			}
			val = 1;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteRequestedItemById: "+e.getMessage());
		}
		return val;
	}

	@Override
	@Transactional
	public CourierServiceDetails saveOrUpdateCourierServiceDetails(
			CourierServiceDetails courierServiceDetails) {
		CourierServiceDetails cDetails = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(courierServiceDetails);
			cDetails = courierServiceDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateCourierServiceDetails: "+e.getMessage());
		}
		return cDetails;
	}

	@Override
	@Transactional
	public List<CourierServiceDetails> getAllCourierServiceDetails(int isActive) {
		List<CourierServiceDetails> courierServiceDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CourierServiceDetails.class);
			if(isActive != -1){
				cr.add(Restrictions.eq("isActive", isActive));
			}
			courierServiceDetails = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllCourierServiceDetails: "+e.getMessage());
		}
		return courierServiceDetails;
	}

	@Override
	@Transactional
	public CourierServiceDetails getCourierServiceDetailsById(
			int courierServiceId) {
		CourierServiceDetails courierServiceDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CourierServiceDetails.class);
			cr.add(Restrictions.eq("courierServiceId", courierServiceId));
			List list = cr.list();
			if(list.size() > 0){
				courierServiceDetails = (CourierServiceDetails) list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllCourierServiceDetails: "+e.getMessage());
		}
		return courierServiceDetails;
	}

	@Override
	@Transactional
	public DespatchDetails saveOrUpdateDespatchDetails(
			DespatchDetails despatchDetails) {
		DespatchDetails desDetails = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(despatchDetails);
			desDetails = despatchDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateDespatchDetails: "+e.getMessage());
		}
		return desDetails;
	}

	@Override
	@Transactional
	public DespatchDetails getDespatchDetailsById(int despatchDetailsId) {
		DespatchDetails desDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(DespatchDetails.class);
			cr.add(Restrictions.eq("despatchDetailsId", despatchDetailsId));
			List<DespatchDetails> list = cr.list();
			if(list.size() > 0){
				desDetails = list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getDespatchDetailsById: "+e.getMessage());
		}
		return desDetails;
	}

	@Override
	@Transactional
	public List<DespatchDetails> getAllDespatchDetails(int isActive) {
		List<DespatchDetails> despatchDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(DespatchDetails.class);
			if(isActive != -1){
				cr.add(Restrictions.eq("isActive", isActive));
			}
			despatchDetails = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getDespatchDetailsById: "+e.getMessage());
		}
		return despatchDetails;
	}

	@Override
	@Transactional
	public DespatchedItems saveOrUpdateDespatchedItems(
			DespatchedItems despatchedItems) {
		DespatchedItems deItems = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(despatchedItems);
			deItems = despatchedItems;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateDespatchedItems: "+e.getMessage());
		}
		return deItems;
	}

	@Override
	@Transactional
	public List<DespatchedItems> getDespatchedItemsByDesId(int despatchDetailsId) {
		List<DespatchedItems> despatchedItems = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(DespatchedItems.class);
			cr.add(Restrictions.eq("despatchDetailsId", despatchDetailsId));
			despatchedItems = cr.list();

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getDespatchedItemsByDesId: "+e.getMessage());
		}
		return despatchedItems;
	}

	@Override
	@Transactional
	public int deleteDespatchedItemsById(int despatchDetailsId) {
		int val = 0;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(DespatchedItems.class);
			cr.add(Restrictions.eq("despatchDetailsId", despatchDetailsId));
			List<DespatchDetails> list = cr.list();
			if(list.size() > 0){
				this.sessionFactory.getCurrentSession().delete(list.get(0));
			}
			val = 1;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteDespatchedItemsById: "+e.getMessage());
		}
		return val;
	}

	@Override
	@Transactional
	public List<StatusInfo> getAllStatusInfo() {
		List<StatusInfo> statusInfos = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(StatusInfo.class);
			statusInfos = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllStatusInfo: "+e.getMessage());
		}
		return statusInfos;
	}


	@Override
	@Transactional
	public List<MaterialRequest> getAllMaterialRequest(int employeeId,int isActive,
			Date dateFr,Date dateTo,String searchKeyWord) {
		List<MaterialRequest> materialRequests = null;
		try{
			Criteria matReqCr= this.sessionFactory.getCurrentSession().createCriteria(MaterialRequest.class,"matReq");
			Criteria callDetCr=matReqCr.createAlias("callDetail", "call");
			Criteria empCr = matReqCr.createAlias("employee", "emp");
			Criteria statusInfoCr = matReqCr.createAlias("statusInfo", "statusInfo");
			
			callDetCr.add(Restrictions.eq("matReq.isActive", isActive));
			if(employeeId > 0){
				callDetCr.add(Restrictions.eq("matReq.employeeId", employeeId));
			}
			
			if(dateFr != null){
				callDetCr.add(Restrictions.ge("matReq.updatedAt", dateFr) );
			}
			if(dateTo !=null){
				callDetCr.add(Restrictions.lt("matReq.updatedAt", dateTo) );
			}
			if(!searchKeyWord.isEmpty()){
				Criterion metReqNumber = Restrictions.ilike("matReq.metrialRequestNumber", searchKeyWord, MatchMode.ANYWHERE);
				Criterion metRemarks = Restrictions.ilike("matReq.remarks", searchKeyWord, MatchMode.ANYWHERE);
				Criterion metName = Restrictions.ilike("matReq.name", searchKeyWord, MatchMode.ANYWHERE);
				Criterion metaddress = Restrictions.ilike("matReq.address", searchKeyWord, MatchMode.ANYWHERE);
				Criterion metState = Restrictions.ilike("matReq.state", searchKeyWord, MatchMode.ANYWHERE);
				Criterion metCountry = Restrictions.ilike("matReq.country", searchKeyWord, MatchMode.ANYWHERE);
				Criterion metPincode = Restrictions.ilike("matReq.pincode", searchKeyWord, MatchMode.ANYWHERE);
				Criterion metPhoneNumber1 = Restrictions.ilike("matReq.phoneNumber1", searchKeyWord, MatchMode.ANYWHERE);
				Criterion metPhoneNumber2 = Restrictions.ilike("matReq.phoneNumber2", searchKeyWord, MatchMode.ANYWHERE);
				
				Criterion metStatusInfo = Restrictions.ilike("statusInfo.status", searchKeyWord, MatchMode.ANYWHERE);
			     
				Criterion cdAllotNo = Restrictions.ilike("call.cdAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdCustomerName = Restrictions.ilike("call.cdCustomerName", searchKeyWord, MatchMode.ANYWHERE);
				Criterion siteDetails = Restrictions.ilike("call.siteDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion boardDivision = Restrictions.ilike("call.cdBoardDivision", searchKeyWord, MatchMode.ANYWHERE);
				
				
				Criterion empFirstName = Restrictions.ilike("emp.firstName", searchKeyWord, MatchMode.ANYWHERE);
				Criterion empLastName = Restrictions.ilike("emp.lastName", searchKeyWord, MatchMode.ANYWHERE);
				
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(metReqNumber);
				disjunction.add(metRemarks);
				disjunction.add(metName);
				disjunction.add(metStatusInfo);
				disjunction.add(metaddress);
				disjunction.add(metState);
				disjunction.add(metCountry);
				disjunction.add(metPincode);
				disjunction.add(metPhoneNumber1);
				disjunction.add(metPhoneNumber2);
				
				disjunction.add(cdAllotNo);
				disjunction.add(cdCustomerName);
				disjunction.add(siteDetails);
				disjunction.add(boardDivision);
				
				disjunction.add(empFirstName);
				disjunction.add(empLastName);
				
				callDetCr.add(disjunction);
			}//if(!searchKeyWord.isEmpty())
			materialRequests = callDetCr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllMaterialRequest: "+e.getMessage());
		}
		return materialRequests;
	}

	@Override
	@Transactional
	public List<DespatchDetails> getAllDespatchDetails(int employeeId,
			int isActive) {
		List<DespatchDetails> despatchDetails = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(DespatchDetails.class);
			System.out.println("Dao,getAllDespatchDetails,employeeId: "+employeeId+", isActive: "+isActive);
			cr.add(Restrictions.eq("isActive", isActive));
			if(employeeId > 0){
				cr.add(Restrictions.eq("employeeId", employeeId));
			}
			despatchDetails = cr.list();
			System.out.println("Dao,getAllDespatchDetails,size: "+despatchDetails.size());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllDespatchDetails: "+e.getMessage());
		}
		return despatchDetails;
	}

	@Override
	@Transactional
	public List<DespatchDetails> getDespatchDetailsByMatReqId(
			int materialRequestId) {
		List<DespatchDetails> despatchDetails = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(DespatchDetails.class);
			cr.add(Restrictions.eq("materialRequestId", materialRequestId));
			despatchDetails = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getDespatchDetailsByMatReqId: "+e.getMessage());
		}
		return despatchDetails;
	}

	@Override
	@Transactional
	public List<ItemReceivedInfo> getAllItemReceivedInfo() {
		List<ItemReceivedInfo> itemReceivedInfos = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(ItemReceivedInfo.class);
			itemReceivedInfos = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllItemReceivedInfo: "+e.getMessage());
		}
		return itemReceivedInfos;
	}

	@Override
	@Transactional
	public ItemReceivedInfo getItemReceivedById(int itemReceivedId) {
		ItemReceivedInfo itemReceivedInfo = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(ItemReceivedInfo.class);
			cr.add(Restrictions.eq("itemReceivedId", itemReceivedId));
			List list = cr.list();
			if(list.size() > 0){
				itemReceivedInfo = (ItemReceivedInfo) cr.list().get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllItemReceivedInfo: "+e.getMessage());
		}
		return null;
	}

	@Override
	@Transactional
	public ItemReceivedInfo saveOrUpdateItemReceivedInfo(ItemReceivedInfo itemReceivedInfo) {
		ItemReceivedInfo iReceivedInfo = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(itemReceivedInfo);
			iReceivedInfo = itemReceivedInfo;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateItemReceivedInfo: "+e.getMessage());
		}
		return iReceivedInfo;
	}

	@Override
	@Transactional
	public MaterialCategory saveOrUpdateMaterialCategory(
			MaterialCategory materialCategory) {
		MaterialCategory maCategory = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(materialCategory);
			maCategory = materialCategory;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateMaterialCategory: "+e.getMessage());
		}
		return maCategory;
	}

	@Override
	@Transactional
	public List<MaterialCategory> getAllMaterialCategory(int isActive) {
		List<MaterialCategory> materialCategories = null;
		try{
			Criteria cr=this.sessionFactory.getCurrentSession().createCriteria(MaterialCategory.class);
			cr.add(Restrictions.eq("isActive", isActive));
			materialCategories = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllMaterialCategory: "+e.getMessage());
		}
		return materialCategories;
	}

	@Override
	@Transactional
	public MaterialCategory getMaterialCategoryById(int materialCategoryId) {
		MaterialCategory maCategory = null;
		try{
			Criteria cr=this.sessionFactory.getCurrentSession().createCriteria(MaterialCategory.class);
			cr.add(Restrictions.eq("materialCategoryId", materialCategoryId));
			maCategory = (MaterialCategory) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getMaterialCategoryById: "+e.getMessage());
		}
		return maCategory;
	}

	@Override
	@Transactional
	public List<MaterialStockInfo> getMaterialStockInfoByCategoryId(
			int materialCategoryId) {
		List<MaterialStockInfo> materialStockInfos = null;
		try{
			Criteria cr=this.sessionFactory.getCurrentSession().createCriteria(MaterialStockInfo.class);
			cr.add(Restrictions.eq("materialCategoryId", materialCategoryId));
			materialStockInfos = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getMaterialStockInfoByCategoryId: "+e.getMessage());
		}
		return materialStockInfos;
	}

	@Override
	@Transactional
	public DespatchReceivedStatus saveOrUpdateDespatchReceivedStatus(
			DespatchReceivedStatus despatchReceivedStatus) {
		DespatchReceivedStatus deReceivedStatus = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(despatchReceivedStatus);
			deReceivedStatus = despatchReceivedStatus;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateDespatchReceivedStatus: "+e.getMessage());
		}
		return deReceivedStatus;
	}

//	@Override
//	@Transactional
//	public List<MaterialStockInfo> getMaterialStockInfoByName(String materialCategory) {
//		List<MaterialStockInfo> materialStockInfo = null;
//		try{
//			Criteria cr=this.sessionFactory.getCurrentSession().createCriteria(MaterialCategory.class);
//			cr.add(Restrictions.eq("materialCategory", materialCategory));
//			materialStockInfo = cr.list();
//		}catch(Exception e){
//			e.printStackTrace();
//			logger.error("getMaterialStockInfoByName: "+e.getMessage());
//		}
//		return materialStockInfo;
//	}

	





}
