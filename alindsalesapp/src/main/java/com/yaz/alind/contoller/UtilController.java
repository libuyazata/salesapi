package com.yaz.alind.contoller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

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

import com.yaz.alind.model.AuditLog;
import com.yaz.alind.model.CallDetail;
import com.yaz.alind.model.NatureOfJobsCallReg;
import com.yaz.alind.model.RelayDetails;
import com.yaz.alind.service.CallManagement;
import com.yaz.alind.service.UserService;
import com.yaz.alind.service.UtilService;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UtilController {

	private static final Logger logger = LoggerFactory.getLogger(UtilController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	UtilService utilService;
	@Autowired
	CallManagement callManagement;
	@Autowired
	private ServletContext context;
	
	@RequestMapping(value="/util/getAllAuditLog", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllAuditLog(@RequestHeader("token") String token) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("UtilController,getAllAuditLog,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<AuditLog> auditLogs = utilService.getAllAuditLog();
				
				resultMap.put("auditLogs", auditLogs);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllAuditLog, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}
	
	@RequestMapping(value="/util/saveOrUdpateNatureOfJobsCallReg", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateNatureOfJobsCallReg(@RequestHeader("token") String token
			,@RequestBody NatureOfJobsCallReg natureOfJobsCallReg) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("UtilController,saveOrUpdateNatureOfJobsCallReg,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				NatureOfJobsCallReg natCallReg = utilService.saveOrUpdateNatureOfJobsCallReg(natureOfJobsCallReg);
				resultMap.put("natureOfJobsCallReg", natCallReg);
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
	
	@RequestMapping(value="/util/deleteNatureOfJobsCallReg", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> deleteNatureOfJobsCallReg(@RequestHeader("token") String token
			,@RequestBody int natureJobId) throws Exception {

		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("UtilController,deleteNatureOfJobsCallReg,token: "+token+", natureJobId: "+natureJobId);
			if(tokenStatus){
				NatureOfJobsCallReg natureOfJobsCallRegs = utilService.deleteNatureOfJobsCallReg(token,natureJobId);
				resultMap.put("natureOfJobsCallReg", natureOfJobsCallRegs);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteNatureOfJobsCallReg, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	
	@RequestMapping(value="/util/getAllNatureOfJobsCallReg", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllNatureOfJobsCallReg(int isActive) throws Exception {

		Map<String,Object> resultMap = null;
//		boolean tokenStatus = false;
		try{
			resultMap = new HashMap<String,Object>();
//			tokenStatus = utilService.evaluateToken(token);
			System.out.println("UtilController,getAllNatureOfJobsCallReg, ");
//			if(tokenStatus){
				List<NatureOfJobsCallReg> natureOfJobsCallRegs = utilService.getAllNatureOfJobsCallRegs(isActive);
				resultMap.put("natureOfJobs", natureOfJobsCallRegs);
				resultMap.put("status", "success");
//				System.out.println("UtilController,getAllNatureOfJobsCallReg,size: "+natureOfJobsCallRegs.size());
//			}else{
//				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
//			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllNatureOfJobsCallReg, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

}
