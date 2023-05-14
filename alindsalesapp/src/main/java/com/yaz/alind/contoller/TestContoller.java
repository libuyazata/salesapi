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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yaz.alind.model.CallDetail;
import com.yaz.alind.service.CallManagement;
import com.yaz.alind.service.UserService;
import com.yaz.alind.service.UtilService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class TestContoller {

	private static final Logger logger = LoggerFactory.getLogger(TestContoller.class);
	
	@Autowired
	UserService userService;
	@Autowired
	UtilService utilService;
	@Autowired
	CallManagement callManagement;
	
	@RequestMapping(value="/test/updateCallDetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> updateCallDetails() throws Exception {

		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			//			System.out.println("CallManagementController,getCallDetails,isActive: "+Integer.parseInt(isActive));
//			List<CallDetail> callDetails = callManagement.getAllCallDetails();
			List<CallDetail> callDetails = callManagement.createAllotTemp();
			resultMap.put("callDetails", callDetails);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("updateCallDetails, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);

		
	}
}
