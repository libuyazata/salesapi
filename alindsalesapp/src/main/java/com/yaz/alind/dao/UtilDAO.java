package com.yaz.alind.dao;

import java.util.List;

import com.yaz.alind.model.AuditLog;
import com.yaz.alind.model.Department;
import com.yaz.alind.model.NatureOfJobsCallReg;

public interface UtilDAO {
	
	public AuditLog saveOrUpdateAuditLog(AuditLog auditLog);
	public List<AuditLog> getAllAuditLog();
	public NatureOfJobsCallReg saveOrUpdateNatureOfJobsCallReg(NatureOfJobsCallReg natureOfJobsCallReg);
	public List<NatureOfJobsCallReg> getAllNatureOfJobsCallRegs(int isActive);
	public NatureOfJobsCallReg getNatureOfJobsCallReg(int natureJobId);
	

}
