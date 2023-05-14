package com.yaz.alind.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.yaz.alind.model.AuditLog;
import com.yaz.alind.model.NatureOfJobsCallReg;
import com.yaz.alind.service.UtilService;

@Transactional
public class UtilDAOImpl implements UtilDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(UtilDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UtilService utilService;

	@Override
	@Transactional
	public AuditLog saveOrUpdateAuditLog(AuditLog auditLog) {
		AuditLog auLog = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(auditLog);
			auLog = auditLog;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getCallDetails: "+e.getMessage());
		}
		return auLog;
	}

	@Override
	@Transactional
	public List<AuditLog> getAllAuditLog() {
		List<AuditLog> auditLogs = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(AuditLog.class);
			auditLogs = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllAuditLog: "+e.getMessage());
		}
		return auditLogs;
	}

	@Override
	@Transactional
	public NatureOfJobsCallReg saveOrUpdateNatureOfJobsCallReg(
			NatureOfJobsCallReg natureOfJobsCallReg) {
		NatureOfJobsCallReg nCallReg = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(natureOfJobsCallReg);
			nCallReg = natureOfJobsCallReg;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateNatureOfJobsCallReg: "+e.getMessage());
		}
		return nCallReg;
	}

	@Override
	@Transactional
	public List<NatureOfJobsCallReg> getAllNatureOfJobsCallRegs(int isActive) {
		List<NatureOfJobsCallReg> natureOfJobsCallRegs = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(NatureOfJobsCallReg.class);
			if(isActive != -1){
				cr.add(Restrictions.eq("isActive", isActive));
			}
			natureOfJobsCallRegs = cr.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllNatureOfJobsCallRegs: "+e.getMessage());
		}
		return natureOfJobsCallRegs;
	}

	@Override
	public NatureOfJobsCallReg getNatureOfJobsCallReg(int natureJobId) {
		NatureOfJobsCallReg natureOfJobsCallReg = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(NatureOfJobsCallReg.class);
			cr.add(Restrictions.eq("natureJobId", natureJobId));
			natureOfJobsCallReg = (NatureOfJobsCallReg) cr.list().get(0);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNatureOfJobsCallReg: "+e.getMessage());
		}
		return natureOfJobsCallReg;
	}


}
