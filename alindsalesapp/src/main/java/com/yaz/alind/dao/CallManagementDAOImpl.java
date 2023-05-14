package com.yaz.alind.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.jpa.Search;
//import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.yaz.alind.service.UtilService;
import com.yaz.alind.ui.model.CallDetailModelList;
import com.yaz.alind.ui.model.ServiceReportModelList;

@Transactional
public class CallManagementDAOImpl implements CallManagementDAO {

	private static final Logger logger = LoggerFactory.getLogger(CallManagementDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UtilService utilService;


	/**
	 *  search call details
	 */
	@Override
	@Transactional
	public List<CallDetail> getCallDetails(Date dateFrom, Date dateTo,String searchKeyWord
			,String callStatus,String gurenteePeriod){
		List<CallDetail> callDetailList = null;
		int pageNo= 0;
		int pageCount = 5;
		int expectedRowSize = 0;
		try{
			expectedRowSize = ((pageNo - 1) * pageCount);
			//			System.out.println("DAO,getCallDetails,callStatus: "+callStatus+", searchKeyWord: "+searchKeyWord);
			//			System.out.println("DAO,getCallDetails,dateFrom: "+dateFrom+", dateTo: "+dateTo);
			//			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class,"callDetail");
			cr.createAlias("natureOfJobs", "natureOfJobs"); 
			cr.add(Restrictions.eq("callDetail.isActive", 1));
			//			cr.addOrder(Order.desc("updatedAt"));

			if(!gurenteePeriod.equals("all")){
				cr.add(Restrictions.eq("callDetail.cdGuranteePeriod", gurenteePeriod) );
				//				cr.add(Restrictions.eq("cdGuranteePeriod", gurenteePeriod) );
			}
			if(!callStatus.isEmpty()){
				//				System.out.println("DAO,getCallDetails,inside callStatus: "+callStatus);
				cr.add(Restrictions.eq("callDetail.cdStatus", callStatus));
			}
			if(dateFrom != null){
				cr.add(Restrictions.ge("callDetail.createdAt", dateFrom) );
				//				}
			}
			if(dateTo !=null){
				cr.add(Restrictions.lt("callDetail.createdAt", dateTo) );
			}
			// Only for the same day searching
			//			if( dateFrom != null && dateTo !=null ){
			//				if(dateFrom.equals(dateTo)){
			//					// Changing To date. 
			//					// Eg : Thu Jun 25 00:00:00 IST 2020 to - Thu Jun 25 23:59:59 IST 2020
			//					Date dateToTemp = dateTo;
			//					dateToTemp.setHours(23);
			//					dateToTemp.setMinutes(59);
			//					dateToTemp.setSeconds(59);
			////			        System.out.println("DAO,getCallDetails,dateFrom & dateTo are same: "+dateFrom+" - "+dateToTemp);
			//			        cr.add(Restrictions.ge("callDetail.createdAt", dateFrom) );
			//			        cr.add(Restrictions.lt("callDetail.createdAt", dateToTemp) );
			//				}
			//			}

			if(!searchKeyWord.isEmpty()){

				Criterion cdCustomerName = Restrictions.ilike("callDetail.cdCustomerName", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdContactNo = Restrictions.ilike("callDetail.cdContactNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion siteDetails = Restrictions.ilike("callDetail.siteDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdRelayPanelDetails = Restrictions.ilike("callDetail.cdRelayPanelDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion serviceRequestId = Restrictions.ilike("callDetail.serviceRequestId", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdEmail = Restrictions.ilike("callDetail.cdEmail", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdBoardDivision = Restrictions.ilike("callDetail.cdBoardDivision", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdGuranteePeriod = Restrictions.ilike("callDetail.cdGuranteePeriod", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdProblemDetails = Restrictions.ilike("callDetail.cdProblemDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdStatus = Restrictions.ilike("callDetail.cdStatus", searchKeyWord, MatchMode.ANYWHERE);
				Criterion workPhNo = Restrictions.ilike("callDetail.workPhNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdAllotNo = Restrictions.ilike("callDetail.cdAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion productDetails = Restrictions.ilike("callDetail.productDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion remarks = Restrictions.ilike("callDetail.remarks", searchKeyWord, MatchMode.ANYWHERE);
				Criterion productSlNo = Restrictions.ilike("callDetail.productSlNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion natureOfJobs = Restrictions.ilike("natureOfJobs.jobNature", searchKeyWord, MatchMode.ANYWHERE);
				//				cr = cr.createCriteria("natureOfJobs").add(Restrictions.eq("jobNature", searchKeyWord));
				//				cr = cr.createCriteria("natureOfJobs").add(Restrictions.ilike("jobNature", searchKeyWord, MatchMode.ANYWHERE));

				/**
				Criterion cdCustomerName = Restrictions.ilike("cdCustomerName", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdContactNo = Restrictions.ilike("cdContactNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion siteDetails = Restrictions.ilike("siteDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdRelayPanelDetails = Restrictions.ilike("cdRelayPanelDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion serviceRequestId = Restrictions.ilike("serviceRequestId", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdEmail = Restrictions.ilike("cdEmail", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdBoardDivision = Restrictions.ilike("cdBoardDivision", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdGuranteePeriod = Restrictions.ilike("cdGuranteePeriod", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdProblemDetails = Restrictions.ilike("cdProblemDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdStatus = Restrictions.ilike("cdStatus", searchKeyWord, MatchMode.ANYWHERE);
				Criterion workPhNo = Restrictions.ilike("workPhNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdAllotNo = Restrictions.ilike("cdAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion productDetails = Restrictions.ilike("productDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion remarks = Restrictions.ilike("remarks", searchKeyWord, MatchMode.ANYWHERE);
				Criterion productSlNo = Restrictions.ilike("productSlNo", searchKeyWord, MatchMode.ANYWHERE);
				 **/
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(cdCustomerName);
				disjunction.add(cdContactNo);
				disjunction.add(siteDetails);
				disjunction.add(cdRelayPanelDetails);
				disjunction.add(serviceRequestId);
				disjunction.add(cdEmail);
				disjunction.add(cdBoardDivision);
				disjunction.add(cdGuranteePeriod);
				disjunction.add(cdProblemDetails);
				disjunction.add(cdStatus);
				disjunction.add(workPhNo);
				disjunction.add(cdAllotNo);
				disjunction.add(productDetails);
				disjunction.add(remarks);
				disjunction.add(productSlNo);
				disjunction.add(natureOfJobs);
				cr.add(disjunction);

			}//if(!callStatus.isEmpty())

			// Based on pagination
			//			cr.setFirstResult(expectedRowSize);
			//			cr.setMaxResults(pageCount);

			callDetailList = cr.list();
			System.out.println("DAO,getCallDetails,callDetailList, size: "+callDetailList.size());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCallDetails: "+e.getMessage());
		}
		return callDetailList;
	}

	@Override
	@Transactional
	public CallDetailModelList searchCallDetails(Date dateFrom, Date dateTo,String searchKeyWord
			,String callStatus,String gurenteePeriod){
		CallDetailModelList callDetailModelList = null; 
		List<CallDetail> callDetailList = null;

		try{
			//			System.out.println("DAO,getCallDetails,pageNo: "+pageNo+", pageCount: "+pageCount+", expectedRowSize: "+expectedRowSize);
			callDetailModelList = new CallDetailModelList();
			//			System.out.println("DAO,getCallDetails,callStatus: "+callStatus+", searchKeyWord: "+searchKeyWord);
			//			System.out.println("DAO,getCallDetails,dateFrom: "+dateFrom+", dateTo: "+dateTo);
			//			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class,"callDetail");
			cr.createAlias("natureOfJobs", "natureOfJobs"); 
			cr.add(Restrictions.eq("callDetail.isActive", 1));
			cr.addOrder(Order.desc("callDetail.updatedAt"));

			if(!gurenteePeriod.equals("all")){
				cr.add(Restrictions.eq("callDetail.cdGuranteePeriod", gurenteePeriod) );
			}
			if(!callStatus.isEmpty()){
				cr.add(Restrictions.eq("callDetail.cdStatus", callStatus));
			}
			if(dateFrom != null){
				cr.add(Restrictions.ge("callDetail.createdAt", dateFrom) );
			}
			if(dateTo !=null){
				cr.add(Restrictions.lt("callDetail.createdAt", dateTo) );
			}

			//			if(!searchKeyWord.isEmpty()){
			if(searchKeyWord != null){

				Criterion cdCustomerName = Restrictions.ilike("callDetail.cdCustomerName", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdContactNo = Restrictions.ilike("callDetail.cdContactNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion siteDetails = Restrictions.ilike("callDetail.siteDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdRelayPanelDetails = Restrictions.ilike("callDetail.cdRelayPanelDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion serviceRequestId = Restrictions.ilike("callDetail.serviceRequestId", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdEmail = Restrictions.ilike("callDetail.cdEmail", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdBoardDivision = Restrictions.ilike("callDetail.cdBoardDivision", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdGuranteePeriod = Restrictions.ilike("callDetail.cdGuranteePeriod", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdProblemDetails = Restrictions.ilike("callDetail.cdProblemDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdStatus = Restrictions.ilike("callDetail.cdStatus", searchKeyWord, MatchMode.ANYWHERE);
				Criterion workPhNo = Restrictions.ilike("callDetail.workPhNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdAllotNo = Restrictions.ilike("callDetail.cdAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion productDetails = Restrictions.ilike("callDetail.productDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion remarks = Restrictions.ilike("callDetail.remarks", searchKeyWord, MatchMode.ANYWHERE);
				Criterion productSlNo = Restrictions.ilike("callDetail.productSlNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion natureOfJobs = Restrictions.ilike("natureOfJobs.jobNature", searchKeyWord, MatchMode.ANYWHERE);
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(cdCustomerName);
				disjunction.add(cdContactNo);
				disjunction.add(siteDetails);
				disjunction.add(cdRelayPanelDetails);
				disjunction.add(serviceRequestId);
				disjunction.add(cdEmail);
				disjunction.add(cdBoardDivision);
				disjunction.add(cdGuranteePeriod);
				disjunction.add(cdProblemDetails);
				disjunction.add(cdStatus);
				disjunction.add(workPhNo);
				disjunction.add(cdAllotNo);
				disjunction.add(productDetails);
				disjunction.add(remarks);
				disjunction.add(productSlNo);
				disjunction.add(natureOfJobs);
				cr.add(disjunction);

			}//if(!callStatus.isEmpty())
			callDetailList = cr.list();
			callDetailModelList.setCallDetailEnityList(callDetailList);
			callDetailModelList.setTotalCount(callDetailList.size());

			// Based on pagination
			//			cr.setFirstResult(expectedRowSize);
			//			cr.setMaxResults(pageCount);	
			//			List<CallDetail> pageNationCallDetails = cr.list();
			//			callDetailModelList.setCallDetailEnityList(pageNationCallDetails);
			//			System.out.println("DAO,getCallDetails,pageNationCallDetails, size: "+pageNationCallDetails.size()
			//					+", TotalCount: "+callDetailModelList.getTotalCount());

		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchCallDetails: "+e.getMessage());
		}
		return callDetailModelList;

	}


	@Override
	@Transactional
	public CallDetailModelList getCallDetails(Date dateFrom, Date dateTo,String searchKeyWord
			,String callStatus,String gurenteePeriod,int pageNo,int pageCount){
		CallDetailModelList callDetailModelList = null; 
		List<CallDetail> callDetailList = null;
		int expectedRowSize = 0;

		try{
			expectedRowSize = ((pageNo - 1) * pageCount);
			//			System.out.println("DAO,getCallDetails,pageNo: "+pageNo+", pageCount: "+pageCount+", expectedRowSize: "+expectedRowSize);
			callDetailModelList = new CallDetailModelList();
			//			System.out.println("DAO,getCallDetails,callStatus: "+callStatus+", searchKeyWord: "+searchKeyWord);
			//			System.out.println("DAO,getCallDetails,dateFrom: "+dateFrom+", dateTo: "+dateTo);
			//			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class,"callDetail");
			cr.createAlias("natureOfJobs", "natureOfJobs"); 
			cr.add(Restrictions.eq("callDetail.isActive", 1));
			cr.addOrder(Order.desc("callDetail.updatedAt"));

			if(!gurenteePeriod.equals("all")){
				cr.add(Restrictions.eq("callDetail.cdGuranteePeriod", gurenteePeriod) );
			}
			if(!callStatus.isEmpty()){
				cr.add(Restrictions.eq("callDetail.cdStatus", callStatus));
			}
			if(dateFrom != null){
				cr.add(Restrictions.ge("callDetail.createdAt", dateFrom) );
			}
			if(dateTo !=null){
				cr.add(Restrictions.lt("callDetail.createdAt", dateTo) );
			}

			//			if(!searchKeyWord.isEmpty()){
			if(searchKeyWord != null){

				Criterion cdCustomerName = Restrictions.ilike("callDetail.cdCustomerName", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdContactNo = Restrictions.ilike("callDetail.cdContactNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion siteDetails = Restrictions.ilike("callDetail.siteDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdRelayPanelDetails = Restrictions.ilike("callDetail.cdRelayPanelDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion serviceRequestId = Restrictions.ilike("callDetail.serviceRequestId", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdEmail = Restrictions.ilike("callDetail.cdEmail", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdBoardDivision = Restrictions.ilike("callDetail.cdBoardDivision", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdGuranteePeriod = Restrictions.ilike("callDetail.cdGuranteePeriod", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdProblemDetails = Restrictions.ilike("callDetail.cdProblemDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdStatus = Restrictions.ilike("callDetail.cdStatus", searchKeyWord, MatchMode.ANYWHERE);
				Criterion workPhNo = Restrictions.ilike("callDetail.workPhNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdAllotNo = Restrictions.ilike("callDetail.cdAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion productDetails = Restrictions.ilike("callDetail.productDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion remarks = Restrictions.ilike("callDetail.remarks", searchKeyWord, MatchMode.ANYWHERE);
				Criterion productSlNo = Restrictions.ilike("callDetail.productSlNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion natureOfJobs = Restrictions.ilike("natureOfJobs.jobNature", searchKeyWord, MatchMode.ANYWHERE);
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(cdCustomerName);
				disjunction.add(cdContactNo);
				disjunction.add(siteDetails);
				disjunction.add(cdRelayPanelDetails);
				disjunction.add(serviceRequestId);
				disjunction.add(cdEmail);
				disjunction.add(cdBoardDivision);
				disjunction.add(cdGuranteePeriod);
				disjunction.add(cdProblemDetails);
				disjunction.add(cdStatus);
				disjunction.add(workPhNo);
				disjunction.add(cdAllotNo);
				disjunction.add(productDetails);
				disjunction.add(remarks);
				disjunction.add(productSlNo);
				disjunction.add(natureOfJobs);
				cr.add(disjunction);

			}//if(!callStatus.isEmpty())
			callDetailList = cr.list();
			callDetailModelList.setTotalCount(callDetailList.size());

			// Based on pagination
			cr.setFirstResult(expectedRowSize);
			cr.setMaxResults(pageCount);	
			List<CallDetail> pageNationCallDetails = cr.list();
			callDetailModelList.setCallDetailEnityList(pageNationCallDetails);
			//			System.out.println("DAO,getCallDetails,pageNationCallDetails, size: "+pageNationCallDetails.size()
			//					+", TotalCount: "+callDetailModelList.getTotalCount());

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCallDetails: "+e.getMessage());
		}
		return callDetailModelList;

	}

	/**
	 *  cdStatus -> new
	 */
	@Override
	@Transactional
	public List<CallDetail> getNonAllottedCalls() {
		List<CallDetail> callDetailList = null;
		List<ServiceReport> serviceReports = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			cr.add(Restrictions.eq("cdStatus", "new"));
			cr.add(Restrictions.eq("isActive", 1));
			cr.addOrder(Order.desc("createdAt"));
			callDetailList = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNonAllottedCalls: "+e.getMessage());
		}
		return callDetailList;
	}


	@Override
	@Transactional
	public List<CallDetail> getOnGoingCalls() {
		List<CallDetail> callDetailList = null;
		try{

			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			cr.add(Restrictions.eq("cdStatus", "on going"));
			cr.add(Restrictions.eq("isActive", 1));
			cr.addOrder(Order.desc("createdAt"));
			callDetailList = cr.list();
			//			System.out.println("CallManagementDAOImpl,getOnGoingCalls,size: "+callDetailList.size());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getOnGoingCalls: "+e.getMessage());
		}
		return callDetailList;
	}


	@Override
	@Transactional
	public List<CallDetail> getPendingCalls(){
		List<CallDetail> callDetailList = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			cr.add(Restrictions.eq("cdStatus", "pending"));
			cr.add(Restrictions.eq("isActive", 1));
			cr.addOrder(Order.desc("createdAt"));
			callDetailList = cr.list();

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getPendingCalls: "+e.getMessage());
		}
		return callDetailList;

	}


	@Override
	@Transactional
	public List<CallDetail> getCompletedCalls() {
		List<CallDetail> callDetailList = null;
		//		List<ServiceReport> serviceReports = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			cr.addOrder(Order.desc("createdAt"));
			cr.add(Restrictions.eq("isActive", 1));
			cr.add(Restrictions.eq("cdStatus", "completed"));
			callDetailList = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCompletedCalls: "+e.getMessage());
		}
		return callDetailList;
	}

	/**
	 *  
	 * @param serviceReports
	 * @return
	 */
	@Transactional
	private List<CallDetail> getCallDetailList(List<ServiceReport> serviceReports){
		List<CallDetail> callDetailList = null;
		try{
			if(serviceReports.size() > 0){

				callDetailList = new ArrayList<CallDetail>();
				for(int i=0;i<serviceReports.size();i++){
					Criteria callDetailcr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
					//					System.out.println("getCallDetailList,closed date: "+serviceReports.get(i).getSrCallClosedDate());
					//					System.out.println("getCallDetailList,call attended date: "+serviceReports.get(i).getSrCallAttendDate());
					//					callDetailcr.add(Restrictions.eq("cdId",serviceReports.get(i).getSr_cd_id()));
					callDetailcr.add(Restrictions.eq("cdId",serviceReports.get(i).getCallDetail().getCdId()));
					if(callDetailcr.list().size() > 0){
						callDetailList.add((CallDetail) callDetailcr.list().get(0));
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCallDetailList: "+e.getMessage());
		}
		return callDetailList;
	}

	/**
	 * callStatus->  completed, rejected , not completed
	 */



	@Override
	@Transactional
	public CallDetail getCallDetailById(int cdId) {
		CallDetail callDetail = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			cr.add(Restrictions.eq("cdId",cdId));
			List<CallDetail> list = cr.list();
			if(list.size() >0){
				callDetail = list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCallDetailById: "+e.getMessage());
		}
		return callDetail;
	}

	/**
	 *    Case-1:
	 *    New call allot to employee. Then cd_status (Call Management) -> on going . Then service_reports of sr_call_status -> 'on going'
	 *    If the employee completed the work, then service_reports of sr_call_status -> 'processed'. (Call Management) -> 'completed' 
	 *    
	 *    Case-2:
	 *    New call allot to employee. Then cd_status (Call Management) -> on going . Then service_reports of sr_call_status -> 'on going'
	 *    If the employee put the, then service_reports of sr_call_status -> 'not completed'. (Call Management) -> 'pending'
	 *    
	 *   "not completed" ('service_reports' - call status )-> the status, which allot from call management to an Employee. Call Management status (cd_status) will be "Pending"
	 *   "rejected" ('service_reports' - call status ) -> the employee returned back the assigned work. Then it will go to Call Management.It shows "Not Completed"
	 *   "completed" ('service_reports' - call status ) -> The alloted work completed. Call Management status (cd_status) will be "processed"
	 */
	@Override
	@Transactional
	public List<ServiceReport> getServiceReportByCallStatus(String srCallStatus) {
		List<ServiceReport> serviceReports = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(ServiceReport.class);
			cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			//			cr.setProjection(Projections.distinct(Projections.property("srId")));
			//			cr.addOrder(Order.desc("srCallClosedDate"));
			cr.addOrder(Order.desc("srCallClosedDate"));
			if( ! srCallStatus.equals("all") ){
				//				System.out.println("CallManagementDAOImpl,getServiceReportByCallStatus,Inside, if -> srCallStatus: "+srCallStatus);
				cr.add(Restrictions.eq("srCallStatus", srCallStatus));
			}

			serviceReports = cr.list();
			//			System.out.println("CallManagementDAOImpl,getServiceReportByCallStatus,srCallStatus: "+srCallStatus+", cr.toString(): -> "+cr.toString());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceReportByCallStatus: "+e.getMessage());
		}
		return serviceReports;
	}

	/**
	 *  0 -> old or completed/pending call
	 *  1 -> Newly alloted call
	 *  
	 */

	@Override
	@Transactional
	public List<Allot> getAllAllotDetails(int isNew) {
		List<Allot> allots = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			if(isNew != -1){
				cr.add(Restrictions.eq("isNew", isNew));
			}
			cr.addOrder(Order.desc("createdAt"));
			allots = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllAllotDetails: "+e.getMessage());
		}
		return allots;
	}

	@Override
	@Transactional
	public Allot getAllotByAlId(int alId) {
		Allot allot = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			cr.add(Restrictions.eq("alId", alId));
			allot = (Allot) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllotByAlId: "+e.getMessage());
		}
		return allot;
	}


	@Override
	@Transactional
	public List<Allot> getEmployeeAllotmentList(int employeeId) {
		List<Allot> allots = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			cr.addOrder(Order.desc("createdAt"));
			cr.add(Restrictions.eq("employeeId", employeeId));
			allots = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getEmployeeAllotmentList: "+e.getMessage());
		}
		return allots;
	}


	@Override
	@Transactional
	public CallDetail saveOrUpdateCallDetails(CallDetail callDetail) {
		CallDetail detail = null;
		try{
			//			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			//			cr.add(Restrictions.eq("cdId",callDetail.getCdId()));
			//			Allot allot=(Allot) cr.list().get(0);
			//			callDetail.setAlId(allot.getAlId());
			this.sessionFactory.getCurrentSession().saveOrUpdate(callDetail);
			detail = callDetail;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateCallDetails: "+e.getMessage());
		}
		return detail;
	}

	@Override
	@Transactional
	public CallDetail updateCallDetails(CallDetail callDetail) {
		CallDetail detail = null;
		try{
			this.sessionFactory.getCurrentSession().update(callDetail);
			detail = callDetail;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateCallDetails: "+e.getMessage());
		}
		return detail;
	}

	@Override
	@Transactional
	public List<CallDetail> getAllCompletedCalls(){
		List<CallDetail> callDetails = null;
		try{

			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			cr.add(Restrictions.eq("cdStatus", "completed"));
			cr.add(Restrictions.eq("isActive",1));
			callDetails = cr.list();

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllCompletedCalls: "+e.getMessage());
		}
		//		System.out.println("CallMgtDAO,getCompletedCallsFromToDate,size: "
		//		+callDetails.size()+", fromDate: "+fromDate+", toDate: "+toDate);
		return callDetails;
	}

	@Override
	@Transactional
	public List<CallDetail> getCompletedCallsFromToDate(Date fromDate,
			Date toDate) {
		List<CallDetail> callDetails = null;
		try{

			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			cr.add(Restrictions.eq("cdStatus", "completed"));
			cr.add(Restrictions.ge("updatedAt", fromDate) );
			cr.add(Restrictions.lt("updatedAt", toDate) );
			cr.add(Restrictions.eq("isActive",1));
			callDetails = cr.list();

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCompletedCallsFromToDate: "+e.getMessage());
		}
		//		System.out.println("CallMgtDAO,getCompletedCallsFromToDate,size: "
		//		+callDetails.size()+", fromDate: "+fromDate+", toDate: "+toDate);
		return callDetails;
	}

	@Override
	@Transactional
	public Allot getAlloDetailsfromAllotNumber(String allotNumber) {
		Allot allot = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			cr.add(Restrictions.eq("alAllotNo", allotNumber));
			List list = cr.list();
			if(list.size() > 0){
				allot = (Allot) list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAlloDetailsfromAllotNumber: "+e.getMessage());
		}
		return allot;
	}
	/**
	 *  Service report
	 *  Search based on Call Attend Date 
	 */
	@Override
	@Transactional
	public List<ServiceReport> searchServiceReport(Date dateFrom, Date dateTo,
			String searchKeyWord, String gurenteePeriod,String status){
		List<ServiceReport> serviceReports = null;
		try{
			Criteria serviceCriteria = this.sessionFactory.getCurrentSession().createCriteria(ServiceReport.class,"serviceReports");
			Criteria allotCriteria = serviceCriteria.createAlias("allot","alt");
			//			Criteria callDetailCriteria = serviceCriteria.createAlias("callDetail","call");
			Criteria callDetailCriteria = allotCriteria.createAlias("callDetail","call");

			if(!status.isEmpty()){
				callDetailCriteria.add(Restrictions.eq("serviceReports.srCallStatus", status));
			}
			if(!gurenteePeriod.equals("all")){
				callDetailCriteria.add(Restrictions.eq("call.cdGuranteePeriod", gurenteePeriod));
			}
			//			
			callDetailCriteria.add(Restrictions.eq("call.isActive", 1));
			//			callDetailCriteria.addOrder(Order.desc("serviceReports.srCallAttendDate"));

			if(dateFrom != null){
				callDetailCriteria.add(Restrictions.ge("serviceReports.srCallAttendDate", dateFrom) );
			}
			if(dateTo !=null){
				callDetailCriteria.add(Restrictions.lt("serviceReports.srCallAttendDate", dateTo) );
			}
			//If Same date came
			//			if( dateFrom != null && dateTo !=null ){
			//				if(dateFrom.equals(dateTo)){
			//					// Changing To date. 
			//					// Eg : Thu Jun 25 00:00:00 IST 2020 to - Thu Jun 25 23:59:59 IST 2020
			//					Date dateToTemp = dateTo;
			//					dateToTemp.setHours(23);
			//					dateToTemp.setMinutes(59);
			//					dateToTemp.setSeconds(59);
			////			        System.out.println("DAO,getCallDetails,dateFrom & dateTo are same: "+dateFrom+" - "+dateToTemp);
			//					callDetailCriteria.add(Restrictions.ge("serviceReports.srCallAttendDate", dateFrom) );
			//					callDetailCriteria.add(Restrictions.lt("serviceReports.srCallAttendDate", dateToTemp) );
			//				}
			//			}

			if(!gurenteePeriod.equals("all")){
				callDetailCriteria.add(Restrictions.ilike("call.cdGuranteePeriod", gurenteePeriod, MatchMode.ANYWHERE));
			}

			if(!searchKeyWord.isEmpty() ){
//				System.out.println("DAO searchServiceReport,searchKeyWord, searchKeyWord: "+searchKeyWord);
				Criterion cdCustomerName = Restrictions.ilike("call.cdCustomerName", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdContactNo = Restrictions.ilike("call.cdContactNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion siteDetails = Restrictions.ilike("call.siteDetails", searchKeyWord, MatchMode.ANYWHERE);

				Criterion cdRelayPanelDetails = Restrictions.ilike("call.cdRelayPanelDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion serviceRequestId = Restrictions.ilike("call.serviceRequestId", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdEmail = Restrictions.ilike("call.cdEmail", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdBoardDivision = Restrictions.ilike("call.cdBoardDivision", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdProblemDetails = Restrictions.ilike("call.cdProblemDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion workPhNo = Restrictions.ilike("call.workPhNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdAllotNo = Restrictions.ilike("call.cdAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				// Allot no from Allot table
				Criterion allotNo = Restrictions.ilike("alt.alAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				// Service Report
				Criterion productSlNo = Restrictions.ilike("serviceReports.productSlNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion obervationDetails = Restrictions.ilike("serviceReports.obervationDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion srRemarks = Restrictions.ilike("serviceReports.srRemarks", searchKeyWord, MatchMode.ANYWHERE);
				Criterion srReportedProblem = Restrictions.ilike("serviceReports.srReportedProblem", searchKeyWord, MatchMode.ANYWHERE);
				Criterion srNaturalOfService = Restrictions.ilike("serviceReports.srNaturalOfService", searchKeyWord, MatchMode.ANYWHERE);

				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(cdCustomerName);
				disjunction.add(cdContactNo);
				disjunction.add(siteDetails);
				disjunction.add(cdRelayPanelDetails);
				disjunction.add(serviceRequestId);
				disjunction.add(cdEmail);
				disjunction.add(cdBoardDivision);
				disjunction.add(cdProblemDetails);
				disjunction.add(workPhNo);
				disjunction.add(cdAllotNo);
				disjunction.add(allotNo);

				disjunction.add(productSlNo);
				disjunction.add(obervationDetails);
				disjunction.add(srRemarks);
				disjunction.add(srReportedProblem);
				disjunction.add(srNaturalOfService);

				callDetailCriteria.add(disjunction);
			}
			serviceReports = callDetailCriteria.list();
//			System.out.println("DAO searchServiceReport, size: "+serviceReports.size());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchServiceReport: "+e.getMessage());
		}
		return serviceReports;

	}

	@Override
	@Transactional
	//	public ServiceReportModelList getAllServiceReport(Date dateFrom, Date dateTo,
	//			String searchKeyWord, String gurenteePeriod,String status,int pageNo,int pageCount){
	public ServiceReportModelList getAllServiceReport(int pageNo,int pageCount){

		ServiceReportModelList serviceReportModelList = null;

		List<ServiceReport> serviceReports = null;

		int expectedRowSize = 0;

		try{
			expectedRowSize = ((pageNo - 1) * pageCount);
			serviceReportModelList = new ServiceReportModelList();
			Criteria serviceCriteria = this.sessionFactory.getCurrentSession().createCriteria(ServiceReport.class,"serviceReports");
			Criteria allotCriteria = serviceCriteria.createAlias("allot","alt");
			//			Criteria callDetailCriteria = serviceCriteria.createAlias("callDetail","call");
			Criteria callDetailCriteria = allotCriteria.createAlias("callDetail","call");
			callDetailCriteria.addOrder(Order.desc("serviceReports.srCallClosedDate"));

			//			if(!status.isEmpty()){
			//				callDetailCriteria.add(Restrictions.eq("serviceReports.srCallStatus", status));
			//			}
			//			if(!gurenteePeriod.equals("all")){
			//				callDetailCriteria.add(Restrictions.eq("call.cdGuranteePeriod", gurenteePeriod));
			//			}
			//			
			callDetailCriteria.add(Restrictions.eq("call.isActive", 1));
			//			callDetailCriteria.addOrder(Order.desc("serviceReports.srCallAttendDate"));

			//			if(dateFrom != null){
			//				callDetailCriteria.add(Restrictions.ge("serviceReports.srCallAttendDate", dateFrom) );
			//			}
			//			if(dateTo !=null){
			//				callDetailCriteria.add(Restrictions.lt("serviceReports.srCallAttendDate", dateTo) );
			//			}

			//			if(!gurenteePeriod.equals("all")){
			//				callDetailCriteria.add(Restrictions.ilike("call.cdGuranteePeriod", gurenteePeriod, MatchMode.ANYWHERE));
			//			}
			/**
			if(!searchKeyWord.isEmpty() ){
				//				System.out.println("DAO searchServiceReport,searchKeyWord, searchKeyWord: "+searchKeyWord);
				Criterion cdCustomerName = Restrictions.ilike("call.cdCustomerName", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdContactNo = Restrictions.ilike("call.cdContactNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion siteDetails = Restrictions.ilike("call.siteDetails", searchKeyWord, MatchMode.ANYWHERE);

				Criterion cdRelayPanelDetails = Restrictions.ilike("call.cdRelayPanelDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion serviceRequestId = Restrictions.ilike("call.serviceRequestId", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdEmail = Restrictions.ilike("call.cdEmail", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdBoardDivision = Restrictions.ilike("call.cdBoardDivision", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdProblemDetails = Restrictions.ilike("call.cdProblemDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion workPhNo = Restrictions.ilike("call.workPhNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdAllotNo = Restrictions.ilike("call.cdAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				// Allot no from Allot table
				Criterion allotNo = Restrictions.ilike("alt.alAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				// Service Report
				Criterion productSlNo = Restrictions.ilike("serviceReports.productSlNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion obervationDetails = Restrictions.ilike("serviceReports.obervationDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion srRemarks = Restrictions.ilike("serviceReports.srRemarks", searchKeyWord, MatchMode.ANYWHERE);
				Criterion srReportedProblem = Restrictions.ilike("serviceReports.srReportedProblem", searchKeyWord, MatchMode.ANYWHERE);
				Criterion srNaturalOfService = Restrictions.ilike("serviceReports.srNaturalOfService", searchKeyWord, MatchMode.ANYWHERE);

				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(cdCustomerName);
				disjunction.add(cdContactNo);
				disjunction.add(siteDetails);
				disjunction.add(cdRelayPanelDetails);
				disjunction.add(serviceRequestId);
				disjunction.add(cdEmail);
				disjunction.add(cdBoardDivision);
				disjunction.add(cdProblemDetails);
				disjunction.add(workPhNo);
				disjunction.add(cdAllotNo);
				disjunction.add(allotNo);

				disjunction.add(productSlNo);
				disjunction.add(obervationDetails);
				disjunction.add(srRemarks);
				disjunction.add(srReportedProblem);
				disjunction.add(srNaturalOfService);

				callDetailCriteria.add(disjunction);
			}
			 **/
			serviceReports = callDetailCriteria.list();
			serviceReportModelList.setServiceReportList(serviceReports);
			serviceReportModelList.setTotalCount(serviceReports.size());

			// Based on pagination
			callDetailCriteria.setFirstResult(expectedRowSize);
			callDetailCriteria.setMaxResults(pageCount);	
			List<ServiceReport> pageNationServiceReports = callDetailCriteria.list();
			serviceReportModelList.setServiceReportList(pageNationServiceReports);

//			System.out.println("DAO,getAllServiceReport,pageNationCallDetails, size: "+pageNationServiceReports.size());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllServiceReport: "+e.getMessage());
		}

		return serviceReportModelList;

	}


	@Override
	@Transactional
	public List<CallDetail> searchWorkAllotDetails(Date dateFrom, Date dateTo,
			String searchKeyWord, String gurenteePeriod) {
		List<CallDetail> callDetails = null;

		try{
			//			System.out.println("DAO,searchWorkAllotDetails,gurenteePeriod: "+gurenteePeriod);
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class,"callDetail");
			//			cr.addOrder(Order.desc("updatedAt"));
			cr.add(Restrictions.eq("cdStatus", "on going"));
			cr.add(Restrictions.eq("isActive",1));

			if(dateFrom != null){
				//				cr.add(Restrictions.ge("updatedAt", dateFrom) );
				cr.add(Restrictions.ge("cdAllotDate", dateFrom) );
			}
			if(dateTo !=null){
				//				cr.add(Restrictions.lt("updatedAt", dateTo) );
				cr.add(Restrictions.lt("cdAllotDate", dateTo) );
			}
			//			System.out.println("CallMgtDAO, searchWorkAllotDetails,dateFrom: "+dateFrom+", dateTo: "+dateTo);
			//			//If Same date came
			//			if( dateFrom != null && dateTo !=null ){
			//				if(dateFrom.equals(dateTo)){
			//					// Changing To date. 
			//					// Eg : Thu Jun 25 00:00:00 IST 2020 to - Thu Jun 25 23:59:59 IST 2020
			//					Date dateToTemp = dateTo;
			//					dateToTemp.setHours(23);
			//					dateToTemp.setMinutes(59);
			//					dateToTemp.setSeconds(59);
			////			        System.out.println("DAO,getCallDetails,dateFrom & dateTo are same: "+dateFrom+" - "+dateToTemp);
			//					cr.add(Restrictions.ge("updatedAt", dateFrom) );
			//					cr.add(Restrictions.lt("updatedAt", dateToTemp) );
			//				}
			//			}

			if(! gurenteePeriod.equals("all")){
				cr.add(Restrictions.eq("cdGuranteePeriod", gurenteePeriod) );
			}
			if(!searchKeyWord.isEmpty()){
				Criterion cdCustomerName = Restrictions.ilike("cdCustomerName", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdContactNo = Restrictions.ilike("cdContactNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion siteDetails = Restrictions.ilike("callDetail.siteDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdRelayPanelDetails = Restrictions.ilike("cdRelayPanelDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion serviceRequestId = Restrictions.ilike("serviceRequestId", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdEmail = Restrictions.ilike("cdEmail", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdBoardDivision = Restrictions.ilike("cdBoardDivision", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdGuranteePeriod = Restrictions.ilike("cdGuranteePeriod", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdProblemDetails = Restrictions.ilike("cdProblemDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdStatus = Restrictions.ilike("cdStatus", searchKeyWord, MatchMode.ANYWHERE);
				Criterion workPhNo = Restrictions.ilike("workPhNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdAllotNo = Restrictions.ilike("cdAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(cdCustomerName);
				disjunction.add(cdContactNo);
				disjunction.add(siteDetails);
				disjunction.add(cdRelayPanelDetails);
				disjunction.add(serviceRequestId);
				disjunction.add(cdEmail);
				disjunction.add(cdBoardDivision);
				disjunction.add(cdGuranteePeriod);
				disjunction.add(cdProblemDetails);
				disjunction.add(cdStatus);
				disjunction.add(workPhNo);
				disjunction.add(cdAllotNo);
				cr.add(disjunction);
			}
			callDetails = cr.list();

		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchWorkDetails: "+e.getMessage());
		}
		return callDetails;
	}


	@Override
	@Transactional
	public ServiceReport getServiceReportById(int srId) {
		ServiceReport serviceReport = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(ServiceReport.class);
			cr.add(Restrictions.eq("srId", srId));
			List<ServiceReport> list = cr.list();
			if(list.size() > 0){
				serviceReport = list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceReportById: "+e.getMessage());
		}
		return serviceReport;
	}

	@Override
	@Transactional
	public List<RelayDetails> saveRelayDetails(List<RelayDetails> relayDetails) {
		List<RelayDetails> reDetails = null;
		try{
			for(int i=0;i<relayDetails.size();i++){
				this.sessionFactory.getCurrentSession().save(relayDetails.get(i));
			}
			reDetails = relayDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveRelayDetails: "+e.getMessage());
		}
		return reDetails;
	}

	@Override
	@Transactional
	public List<PanelDetails> savePanelDetails(List<PanelDetails> panelDetails) {
		List<PanelDetails> reDetails = null;
		try{
			for(int i=0;i<panelDetails.size();i++){
				this.sessionFactory.getCurrentSession().save(panelDetails.get(i));
			}
			reDetails = panelDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("savePanelDetails: "+e.getMessage());
		}
		return reDetails;
	}

	@Override
	@Transactional
	public List<RelayDetails> getAllRelayDetails() {
		List<RelayDetails> relayDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(RelayDetails.class);
			cr.add(Restrictions.eq("isActive", 1));
			relayDetails = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllRelayDetails: "+e.getMessage());
		}
		return relayDetails;
	}

	@Override
	@Transactional
	public List<PanelDetails> getAllPanelDetails() {
		List<PanelDetails> panelDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(PanelDetails.class);
			cr.add(Restrictions.eq("isActive", 1));
			panelDetails = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllPanelDetails: "+e.getMessage());
		}
		return panelDetails;
	}

	@Override
	@Transactional
	public RelayDetails saveOrUpdateRelayDetails(RelayDetails relayDetails) {
		RelayDetails reDetails = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(relayDetails);
			reDetails = relayDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateRelayDetails: "+e.getMessage());
		}
		return reDetails;
	}

	@Override
	@Transactional
	public PanelDetails saveOrUpdatePanelDetails(PanelDetails panelDetails) {
		PanelDetails panDetails = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(panelDetails);
			panDetails = panelDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdatePanelDetails: "+e.getMessage());
		}
		return panDetails;
	}

	/**
	 * Test purpose, updating, Allot -id (al_id) to Call details
	 */
	@Override
	@Transactional
	public List<CallDetail> getAllCallDetailsTemp() {
		List<CallDetail> callDetails = null;
		List<Allot> allots = new ArrayList<Allot>();
		try{
			Criteria callCr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			callDetails = callCr.list();
			for(int i=0;i<callDetails.size();i++){
				//				System.out.println("i: "+i+",AllotNo: "+callDetails.get(i).getCdAllotNo());
				Criteria allotCr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
				allotCr.add(Restrictions.eq("alAllotNo", callDetails.get(i).getCdAllotNo()));
				System.out.println("i: "+i+",AllotNo: "+callDetails.get(i).getCdAllotNo()+",size: "+allotCr.list().size());
				if(allotCr.list().size()>0 ){
					System.out.println("i: "+i+",AllotNo: "+callDetails.get(i).getCdAllotNo());
					//					Allot alt = (Allot)allotCr.list().get(0);
					allots.add((Allot)allotCr.list().get(0));
					//					if(callDetails.get(i).getCdAllotNo().equals(((Allot)allotCr.list().get(0)).getAlAllotNo())){
					//						System.out.println("Al id: "+callDetails.get(i).getAlId()+", cd id: "+callDetails.get(i).getCdId());
					//						callDetails.get(i).setAlId(((Allot)allotCr.list().get(0)).getAlId());
					//						this.sessionFactory.getCurrentSession().saveOrUpdate(callDetails.get(i));
					//					}
					//					System.out.println("Al id: "+callDetails.get(i).getAlId()+", cd id: "+callDetails.get(i).getCdId());
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllCallDetails: "+e.getMessage());
		}
		return callDetails;
	}

	@Override
	@Transactional
	public List<CallDetail> createAllotTemp() {
		List<CallDetail> callDetails = null;
		try{
			Criteria callCr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			callCr.add(Restrictions.eq("alId", 0));
			callDetails = callCr.list();
			//			List<Allot> altList = new ArrayList<Allot>();
			//			for(int j=0;j<callDetails.size();j++){
			//				Allot allot = new Allot();
			//				altList.add(allot);
			//			}


		}catch(Exception e){
			e.printStackTrace();
			logger.error("createAllotTemp: "+e.getMessage());
		}
		return callDetails;
	}

	@Override
	@Transactional
	public Allot saveAllotTemp(Allot allot){
		Allot alt = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(allot);
			alt = allot;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveAllotTemp: "+e.getMessage());
		}
		System.out.println("saveAllotTemp,Al id: "+alt.getAlId());
		return alt;
	}

	@Override
	@Transactional
	public CallDetail saveOrUpdateTemp(CallDetail callDetail) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(callDetail);
		return callDetail;
	}

	@Override
	@Transactional
	public CustomerDetails saveOrUpdateCusotmerDetails(
			CustomerDetails customerDetails) {
		CustomerDetails cusDetails = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(customerDetails);
			cusDetails = customerDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateCusotmerDetails: "+e.getMessage());
		}
		return cusDetails;
	}

	@Override
	@Transactional
	public List<CustomerDetails> getAllCustomerDetails() {
		List<CustomerDetails> customerDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CustomerDetails.class);
			cr.add(Restrictions.eq("isActive",1));
			customerDetails = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllCustomerDetails: "+e.getMessage());
		}
		return customerDetails;
	}

	@Override
	@Transactional
	public CustomerDetails getCustomerDetailsById(int customerId) {
		CustomerDetails customerDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CustomerDetails.class);
			cr.add(Restrictions.eq("customerId", customerId));
			customerDetails = (CustomerDetails) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCustomerDetailsById: "+e.getMessage());
		}
		return customerDetails;
	}

	@Override
	@Transactional
	public BoardDivisionDetails saveOrUpdateBoardDivisionDetails(
			BoardDivisionDetails boardDivisionDetails) {
		BoardDivisionDetails bDetails = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(boardDivisionDetails);
			bDetails = boardDivisionDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateBoardDivisionDetails: "+e.getMessage());
		}
		return bDetails;
	}

	@Override
	@Transactional
	public List<BoardDivisionDetails> getAllBoardDivisionDetails() {
		List<BoardDivisionDetails> boardDivisionDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(BoardDivisionDetails.class);
			cr.add(Restrictions.eq("isActive", 1));
			boardDivisionDetails = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateCusotmerDetails: "+e.getMessage());
		}
		return boardDivisionDetails;
	}

	@Override
	@Transactional
	public BoardDivisionDetails getBoardDivisionDetailsById(
			int boardDivisionId) {
		BoardDivisionDetails boardDivisionDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(BoardDivisionDetails.class);
			cr.add(Restrictions.eq("boardDivisionId", boardDivisionId));
			boardDivisionDetails = (BoardDivisionDetails) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getBoardDivisionDetailsById: "+e.getMessage());
		}
		return boardDivisionDetails;
	}

	@Override
	@Transactional
	public RelayDetails getRelayDetailsById(int relayId) {
		RelayDetails relayDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(RelayDetails.class);
			cr.add(Restrictions.eq("relayId", relayId));
			relayDetails = (RelayDetails) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getRelayDetailsById: "+e.getMessage());
		}
		return relayDetails;
	}

	@Override
	@Transactional
	public PanelDetails getPanelDetailsById(int panelId) {
		PanelDetails panelDetails = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(PanelDetails.class);
			cr.add(Restrictions.eq("panelId", panelId));
			panelDetails = (PanelDetails) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getPanelDetailsById: "+e.getMessage());
		}
		return panelDetails;
	}

	@Override
	@Transactional
	public Allot allotWork(Allot allot) {
		Allot alot = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(allot);
			alot = allot;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("allotWork: "+e.getMessage());
		}
		return alot;
	}

	@Override
	@Transactional
	public Allot getLastAllotRecord() {
		Allot allot = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			cr.addOrder(Order.desc("alId"));
			cr.setMaxResults(1);
			allot = (Allot) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getLastAllotRecord: "+e.getMessage());
		}
		return allot;
	}

	@Override
	@Transactional
	public AllottedEmployees saveOrUpdateAllottedEmployees(
			AllottedEmployees allottedEmployees) {
		AllottedEmployees alEmployees = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(allottedEmployees);
			alEmployees = allottedEmployees;
			//			System.out.println("DAO CallManagement,saveOrUpdateAllottedEmployees,getAllottedEmpoyeesId: "
			//					+allottedEmployees.getAllottedEmpoyeesId()+", id: "+alEmployees.getAllottedEmpoyeesId());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateAllottedEmployees: "+e.getMessage());
		}
		return alEmployees;
	}

	@Override
	@Transactional
	public ServiceReport saveOrUpdateServiceReport(ServiceReport serviceReport) {
		ServiceReport seReport = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(serviceReport);
			seReport = serviceReport;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateServiceReport: "+e.getMessage());
		}
		return seReport;
	}

	@Override
	@Transactional
	public List<AllottedEmployees> getAllottedWorkDetailsByEmpId(int employeeId) {
		List<AllottedEmployees> allottedList = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(AllottedEmployees.class);
			cr.add(Restrictions.eq("employeeId",employeeId));
			allottedList = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllottedWorkDetailsByEmpId: "+e.getMessage());
		}
		return allottedList;
	}

	@Override
	@Transactional
	public AllottedEmployees getAllottedWorkDetailsByAllottedEmpId(int allottedEmpoyeesId) {
		AllottedEmployees allotted = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(AllottedEmployees.class);
			cr.add(Restrictions.eq("allottedEmpoyeesId",allottedEmpoyeesId));
			allotted = (AllottedEmployees) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllottedWorkDetailsByAllottedEmpId: "+e.getMessage());
		}
		return allotted;
	}

	@Override
	@Transactional
	public List<AllottedEmployees> getAllottedWorkDetailsByalId(int alId){
		List<AllottedEmployees> allottedList = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(AllottedEmployees.class);
			cr.add(Restrictions.eq("alId",alId));
			//			cr.addOrder(Order.asc("alId"));
			cr.addOrder(Order.desc("allottedEmpoyeesId"));
			allottedList = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllottedWorkDetailsByalId: "+e.getMessage());
		}
		return allottedList;
	}

	@Override
	@Transactional
	public Allot getAllottedWorkById(int alId){
		Allot allot = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			cr.add(Restrictions.eq("alId",alId));
			//			cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			allot = (Allot) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllottedWorkByAlId: "+e.getMessage());
		}
		return allot;
	}

	@Override
	@Transactional
	public int updateCdIdInAllotTemp() {
		int val = 0;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			List<CallDetail> callDetails = cr.list();
			System.out.println("updateCdIdInAllot,callDetails size: "+callDetails.size());
			for(int i=0;i<callDetails.size();i++){
				Criteria crAllot = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
				crAllot.add(Restrictions.eq("alAllotNo", callDetails.get(i).getCdAllotNo()));
				List<Allot> altList = crAllot.list();
				System.out.println("updateCdIdInAllot,cdAllotNo: "+callDetails.get(i).getCdAllotNo()
						+", altList size: "+altList.size());
				//				System.out.println("updateCdIdInAllot,alAllotNo: "+callDetails.get(i).getAlAllotNo()
				//						+", altList size: "+altList.size());
				//				if(altList.size() > 1){
				for(int j=0;j<altList.size();j++){
					System.out.println("updateCdIdInAllot,getCdId : "+callDetails.get(i).getCdId());
					altList.get(j).setCdId(callDetails.get(i).getCdId());
					this.sessionFactory.getCurrentSession().saveOrUpdate(altList.get(j));
				}
			}
			//			}
			val = 1;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateCdIdInAllotTemp: "+e.getMessage());
		}
		return val;
	}

	@Override
	@Transactional
	public int allotEmpolyeeTemp(){
		int val = 0;
		try{
			Criteria crAllot = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			List<Allot> altList = crAllot.list();
			List<AllottedEmployees> allList = new ArrayList<AllottedEmployees>();

			for(int i=0;i<altList.size();i++){
				AllottedEmployees allottedEmployees = new AllottedEmployees();
				allottedEmployees.setEmployeeId(altList.get(i).getAlEmpId());
				allottedEmployees.setAlId(altList.get(i).getAlId());
				allList.add(allottedEmployees);
				System.out.println("updateCdIdInAllot,getEmployeeId : "+allList.get(i).getEmployeeId());
			}

			for(int j=0;j<altList.size();j++){
				//				this.sessionFactory.getCurrentSession().saveOrUpdate(allList.get(j));
				this.sessionFactory.getCurrentSession().saveOrUpdate(allList.get(j));
				System.out.println("updateCdIdInAllot,getAllotedEmpoyeesId : "+allList.get(j).getAllotedEmpoyeesId());
			}

			val = 1;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("allotEmpolyeeTemp: "+e.getMessage());
		}
		return val;
	}

	@Override
	@Transactional
	public CallDetail getLastCallDetailsRecord() {
		CallDetail callDetail = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			cr.addOrder(Order.desc("cdId"));
			cr.setMaxResults(1);
			callDetail = (CallDetail) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getLastCallDetailsRecord: "+e.getMessage());
		}
		return callDetail;
	}

	@Override
	@Transactional
	public List<Employee> searchEmployeesByName(String firstName,String lastName,String searchKeyWord) {
		List<Employee> employees = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Employee.class,"employee");
			Disjunction disjunction = Restrictions.disjunction();
			//			if(!firstName.isEmpty()){
			Criterion empFirstName = Restrictions.ilike("firstName", firstName, MatchMode.ANYWHERE);
			Criterion empLastName = Restrictions.ilike("lastName", lastName, MatchMode.ANYWHERE);

			disjunction.add(empFirstName);
			disjunction.add(empLastName);
			cr.add(disjunction);
			employees = cr.list();
			//			System.out.println("CallMgtDAO, searchEmployeesByName, employees size: "+employees.size()
			//					+",firstName:  "+firstName+", lastName: "+lastName+", searchKeyWord: "+searchKeyWord);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchEmployeesByFirstName: "+e.getMessage());
		}
		return employees;
	}

	@Override
	@Transactional
	public List<Employee> searchEmployeesByName(String firstName,String lastName,String searchKeyWord
			,int pageNo,int pageCount) {
		List<Employee> employees = null;
		int expectedRowSize = 0;
		try{
			expectedRowSize = ((pageNo - 1) * pageCount);
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Employee.class,"employee");
			Disjunction disjunction = Restrictions.disjunction();
			//			if(!firstName.isEmpty()){
			Criterion empFirstName = Restrictions.ilike("firstName", firstName, MatchMode.ANYWHERE);
			Criterion empLastName = Restrictions.ilike("lastName", lastName, MatchMode.ANYWHERE);

			disjunction.add(empFirstName);
			disjunction.add(empLastName);
			cr.add(disjunction);

			// Based on pagination
			cr.setFirstResult(expectedRowSize);
			cr.setMaxResults(pageCount);		
			employees = cr.list();
			//			System.out.println("CallMgtDAO, searchEmployeesByName, employees size: "+employees.size()
			//					+",firstName:  "+firstName+", lastName: "+lastName+", searchKeyWord: "+searchKeyWord);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchEmployeesByFirstName: "+e.getMessage());
		}
		return employees;
	}

	@Override
	@Transactional
	public boolean isAllotNumberExists(String alAllotNo) {
		boolean status = false;
		Allot allot = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			cr.add(Restrictions.eq("alAllotNo",alAllotNo));
			List list = cr.list();
			if(list.size() > 0){
				allot = (Allot) cr.list().get(0);
			}
			if(allot == null){
				status = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("isAllotNumberExists: "+e.getMessage());
		}
		return status;
	}

	@Override
	@Transactional
	public boolean isServicerequetIdExists(String serviceRequestId) {
		boolean status = false;
		//		CallDetail callDetail = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(CallDetail.class);
			cr.add(Restrictions.eq("serviceRequestId",serviceRequestId));
			//			callDetail = (CallDetail) cr.list().get(0);
			List list = cr.list();
			if(list.size() == 0)
				//			if(callDetail == null){
				status = true;
			//			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("isServicerequetIdExists: "+e.getMessage());
		}
		return status;
	}

	@Override
	@Transactional
	public ServiceAllotConnector saveOrUpdateServiceAllotConnector(
			ServiceAllotConnector serviceAllotConnector) {
		ServiceAllotConnector sConnector = null;
		try{

			this.sessionFactory.getCurrentSession().saveOrUpdate(serviceAllotConnector);
			sConnector = serviceAllotConnector;

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateServiceAllotConnector: "+e.getMessage());
		}
		return sConnector;
	}

	@Override
	public Allot getAllotByAllotNumber(String alAllotNo) {
		Allot allot = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			cr.add(Restrictions.eq("alAllotNo",alAllotNo));
			allot = (Allot) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllotByAllotNumber: "+e.getMessage());
		}
		return allot;
	}

	@Override
	@Transactional
	public List<ServiceAllotConnector> getServiceAllotList() {
		List<ServiceAllotConnector> allotConnectors = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(ServiceAllotConnector.class);
			allotConnectors = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceAllotList: "+e.getMessage());
		}
		return allotConnectors;
	}

	@Override
	@Transactional
	public List<Allot> getAllotByCdId(int cdId) {
		List<Allot> allots = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Allot.class);
			cr.addOrder(Order.desc("alId"));
			cr.add(Restrictions.eq("cdId",cdId));
			allots = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllotByCdId: "+e.getMessage());
		}
		return allots;
	}

	@Override
	@Transactional
	public List<NatureOfJobs> getAllNatureOfJobs() {
		List<NatureOfJobs> natureOfJobs = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(NatureOfJobs.class);
			cr.add(Restrictions.eq("isActive",1));
			natureOfJobs = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllNatureOfJobs: "+e.getMessage());
		}
		return natureOfJobs;
	}

	@Override
	@Transactional
	public NatureOfJobs saveOrUpdateNatureOfJobs(NatureOfJobs natureOfJobs) {
		NatureOfJobs natJobs = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(natureOfJobs);
			natJobs = natureOfJobs;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateNatureOfJobs: "+e.getMessage());
		}
		return natJobs;
	}

	@Override
	@Transactional
	public NatureOfJobs getNatureOfJobsById(int  natureJobId) {
		NatureOfJobs natJobs = null;
		try{
			//			System.out.println("CallMgtDAO,deleteNatureOfJobs,natureJobId: "+natureJobId);
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(NatureOfJobs.class);
			//			cr.add(Restrictions.eq("isActive",1));
			cr.add(Restrictions.eq("natureJobId",natureJobId));
			natJobs = (NatureOfJobs) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNatureOfJobsById: "+e.getMessage());
		}
		return natJobs;
	}

	@Override
	@Transactional
	public int deleteAllottedEmployee(int allottedEmpoyeesId) {
		int value = 0;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(AllottedEmployees.class);
			cr.add(Restrictions.eq("allottedEmpoyeesId",allottedEmpoyeesId));
			List list = cr.list();
			if(list.size() > 0){
				AllottedEmployees allottedEmployees = (AllottedEmployees) list.get(0);
				this.sessionFactory.getCurrentSession().delete(allottedEmployees);
			}
			value = 1;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("deleteAllottedEmployee: "+e.getMessage());
		}
		return value;
	}

	@Override
	@Transactional
	public List<ObservationBeforeMaintanence> getAllObservationBeforeMaintanence() {
		List<ObservationBeforeMaintanence> obList = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(ObservationBeforeMaintanence.class);
			cr.add(Restrictions.eq("isActive",1));
			obList = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllObservationBeforeMaintanence: "+e.getMessage());
		}
		return obList;
	}

	@Override
	@Transactional
	public ObservationBeforeMaintanence getObservationBeforeMaintanenceById(
			int obervationId) {
		ObservationBeforeMaintanence obBeforeMaintanence = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(ObservationBeforeMaintanence.class);
			obBeforeMaintanence = (ObservationBeforeMaintanence) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getObservationBeforeMaintanenceById: "+e.getMessage());
		}
		return obBeforeMaintanence;
	}

	@Override
	@Transactional
	public ObservationBeforeMaintanence saveOrUpdateObervationBeforeMaintanence(
			ObservationBeforeMaintanence observationBeforeMaintanence) {
		ObservationBeforeMaintanence obBeforeMaintanence = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(observationBeforeMaintanence);
			obBeforeMaintanence = observationBeforeMaintanence;

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateObervationBeforeMaintanence: "+e.getMessage());
		}
		return obBeforeMaintanence;
	}

	@Override
	@Transactional
	public List<AllottedEmployees> searchAllottedEmployeesById(Date dateFrom,
			Date dateTo, String searchKeyWord, int employeeId) {
		List<AllottedEmployees> allottedEmployees = null;
		try{
			Criteria allottedEmpCriteria = this.sessionFactory.getCurrentSession().createCriteria(AllottedEmployees.class,"allottedEmp");
			Criteria allotCriteria = allottedEmpCriteria.createAlias("allottedEmp.allot","allot");
			Criteria callDetailCriteria = allotCriteria.createAlias("allot.callDetail", "call");
			callDetailCriteria.add(Restrictions.eq("allottedEmp.employeeId", employeeId) );
			callDetailCriteria.add(Restrictions.eq("call.isActive", 1) );
			if(dateFrom != null){
				callDetailCriteria.add(Restrictions.ge("call.cdAllotDate", dateFrom) );
			}
			if(dateTo !=null){
				callDetailCriteria.add(Restrictions.lt("call.cdAllotDate", dateTo) );
			}
			//			//If Same date came
			//			if( dateFrom != null && dateTo !=null ){
			//				if(dateFrom.equals(dateTo)){
			//					// Changing To date. 
			//					// Eg : Thu Jun 25 00:00:00 IST 2020 to - Thu Jun 25 23:59:59 IST 2020
			//					Date dateToTemp = dateTo;
			//					dateToTemp.setHours(23);
			//					dateToTemp.setMinutes(59);
			//					dateToTemp.setSeconds(59);
			//			        System.out.println("DAO,getCallDetails,dateFrom & dateTo are same: "+dateFrom+" - "+dateToTemp);
			//					callDetailCriteria.add(Restrictions.ge("call.cdAllotDate", dateFrom) );
			//					callDetailCriteria.add(Restrictions.lt("call.cdAllotDate", dateToTemp) );
			//				}
			//			}

			if(searchKeyWord !=  null){

				Criterion cdCustomerName = Restrictions.ilike("call.cdCustomerName", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdContactNo = Restrictions.ilike("call.cdContactNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion siteDetails = Restrictions.ilike("call.siteDetails", searchKeyWord, MatchMode.ANYWHERE);

				Criterion cdRelayPanelDetails = Restrictions.ilike("call.cdRelayPanelDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion serviceRequestId = Restrictions.ilike("call.serviceRequestId", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdEmail = Restrictions.ilike("call.cdEmail", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdBoardDivision = Restrictions.ilike("call.cdBoardDivision", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdProblemDetails = Restrictions.ilike("call.cdProblemDetails", searchKeyWord, MatchMode.ANYWHERE);
				Criterion workPhNo = Restrictions.ilike("call.workPhNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdAllotNo = Restrictions.ilike("call.cdAllotNo", searchKeyWord, MatchMode.ANYWHERE);
				Criterion cdStatus = Restrictions.ilike("call.cdStatus", searchKeyWord, MatchMode.ANYWHERE);

				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(cdCustomerName);
				disjunction.add(cdContactNo);
				disjunction.add(siteDetails);
				disjunction.add(cdRelayPanelDetails);
				disjunction.add(serviceRequestId);
				disjunction.add(cdEmail);
				disjunction.add(cdBoardDivision);
				disjunction.add(cdProblemDetails);
				disjunction.add(workPhNo);
				disjunction.add(cdAllotNo);
				disjunction.add(cdStatus);

				callDetailCriteria.add(disjunction);

			}//if(!searchKeyWord.isEmpty())
			allottedEmployees = callDetailCriteria.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchAllottedEmployeesById: "+e.getMessage());
		}
		return allottedEmployees;
	}

	@Override
	@Transactional
	public List<CustomerSiteDetails> getAllCustomerSiteDetails() {
		List<CustomerSiteDetails> customerSiteDetails = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(CustomerSiteDetails.class);
			cr.add(Restrictions.eq("isActive", 1) );
			customerSiteDetails = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllCustomerSiteDetails: "+e.getMessage());
		}
		return customerSiteDetails;
	}

	@Override
	@Transactional
	public CustomerSiteDetails saveOrUpdateCustomerSiteDetails(
			CustomerSiteDetails customerSiteDetails) {
		CustomerSiteDetails cSiteDetails = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(customerSiteDetails);
			cSiteDetails = customerSiteDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateCustomerSiteDetails: "+e.getMessage());
		}
		return cSiteDetails;
	}

	@Override
	@Transactional
	public CustomerSiteDetails getCustomerSiteDetailsById(int siteId) {
		CustomerSiteDetails customerSiteDetails = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(CustomerSiteDetails.class);
			cr.add(Restrictions.eq("siteId", siteId) );
			customerSiteDetails = (CustomerSiteDetails) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCustomerSiteDetailsById: "+e.getMessage());
		}
		return customerSiteDetails;
	}

	@Override
	@Transactional
	public List<ServiceReport> getServiceReportByCdId(int cdId){
		List<ServiceReport> serviceReports = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(ServiceReport.class);
			cr.add(Restrictions.eq("sr_cd_id", cdId) );
			serviceReports =  cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceReportByCdId: "+e.getMessage());
		}

		return serviceReports;
	}

	@Override
	@Transactional
	public ServiceAllotConnector getServiceAllotConnectorByAlId(int alId) {
		ServiceAllotConnector serviceAllotConnector = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(ServiceAllotConnector.class);
			cr.add(Restrictions.eq("alId", alId) );
			if(cr.list().size() > 0){
				serviceAllotConnector =  (ServiceAllotConnector) cr.list().get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceAllotConnectorByAlId: "+e.getMessage());
		}
		return serviceAllotConnector;
	}

	@Override
	@Transactional
	public ServiceAllotConnector getServiceAllotConnectorBySrId(int srId) {
		ServiceAllotConnector serviceAllotConnector = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(ServiceAllotConnector.class);
			cr.add(Restrictions.eq("srId", srId) );
			serviceAllotConnector =  (ServiceAllotConnector) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceAllotConnectorBySrId: "+e.getMessage());
		}
		return serviceAllotConnector;
	}


	@Override
	@Transactional
	public LatestRequestIds getLastServiceRequestId(int lastId) {
		LatestRequestIds lastServiceRequestId = null;
		try{
			Criteria cr= this.sessionFactory.getCurrentSession().createCriteria(LatestRequestIds.class);
			cr.add(Restrictions.eq("lastId", lastId) );
			lastServiceRequestId =  (LatestRequestIds) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getLastServiceRequestId: "+e.getMessage());
		}
		return lastServiceRequestId;
	}

	@Override
	@Transactional
	public LatestRequestIds updateLatestServiceRequestId(
			LatestRequestIds lastServiceRequestId) {
		LatestRequestIds lastRequestId = null;
		try{
			this.sessionFactory.getCurrentSession().update(lastServiceRequestId);
			lastRequestId =  lastServiceRequestId;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("updateLatestServiceRequestId: "+e.getMessage());
		}
		return lastRequestId;
	}

	@Override
	@Transactional
	public Allot saveOrUpdateAllot(Allot allot) {
		Allot alt = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(allot);
			alt = allot;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateAllot: "+e.getMessage());
		}
		return alt;
	}

	@Override
	@Transactional
	public ServiceReport getServiceReportByAlId(int alId) {
		ServiceReport serviceReport = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(ServiceReport.class);
			cr.add(Restrictions.eq("alId", alId) );
			List<ServiceReport> list = cr.list();
			if(list.size()>0){
				serviceReport = list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceReportByAlId: "+e.getMessage());
		}
		return serviceReport;
	}

	@Override
	@Transactional
	public ServiceFile saveServiceFile(ServiceFile serviceFile) {
		ServiceFile sFile = null;
		try{
			this.sessionFactory.getCurrentSession().save(serviceFile);
			sFile = serviceFile;

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveServiceFile: "+e.getMessage());
		}
		return sFile;
	}

	@Override
	@Transactional
	public List<ServiceFile> getServiceFileByServiceId(int serviceFileId) {
		List<ServiceFile> serviceFiles = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(ServiceFile.class);
			cr.add(Restrictions.eq("serviceFileId", serviceFileId) );
			serviceFiles = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getServiceFileByServiceId: "+e.getMessage());
		}
		return serviceFiles;
	}



}
