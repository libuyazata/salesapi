package com.yaz.alind.contoller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yaz.alind.model.Department;
import com.yaz.alind.model.Employee;
import com.yaz.alind.model.ForgetPasswordModel;
import com.yaz.alind.service.UserService;
import com.yaz.alind.service.UtilService;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService;
	@Autowired
	UtilService utilService;
	@Autowired
	private ServletContext context;

	@RequestMapping(value="/user/getAuthentication", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> getAuthentication(@RequestBody Employee employee) throws Exception {

		Map<String,Object> resultMap = null;
		try{
//			System.out.println("UserController,getAuthentication,userName: "+employee.getUserName()+", password: "+employee.getViewPass());
			resultMap = new HashMap<String,Object>();
			Employee employeeDetails = userService.getAuthentication(employee.getUserName(), employee.getViewPass());
			resultMap.put("loginDetails", employeeDetails);
			resultMap.put("token", employeeDetails.getToken());
			//			System.out.println("UserController,getAuthentication,loginDetails: "+employeeDetails+", token: "+employeeDetails.getToken());
			resultMap.put("status", "success");
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAuthentication, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/user/getAllEmployee", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllEmployee(@RequestHeader("token") String token,
			@RequestParam String departmentId,@RequestParam String designationId,
			@RequestParam String searchKeyWord) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{

			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("UserController,getAllEmployee,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<Employee> employeeList = userService.getAllEmployee(searchKeyWord);
				resultMap.put("employeeList", employeeList);
				//			resultMap.put("token", user.getToken());
				resultMap.put("status", "success");
				//				System.out.println("UserController,getAllEmployee,employeeList, size: "+employeeList.size());
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllEmployee, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/user/saveOrUpdateEmployee", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateEmployee(@RequestHeader("token") String token
			,@RequestBody Employee employee) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{

			resultMap = new HashMap<String,Object>();
			//			System.out.println("UserController,saveOrUpdateDepartment,employeeList, size: "+departmentList.size());
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("UserController,saveOrUpdateEmployee,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				Employee emply = userService.saveOrUpdateEmployee(employee,context);
				resultMap.put("employee", emply);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateEmployee, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/user/getEmployeeById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getEmployeeById(@RequestParam String employeeId,@RequestHeader("token") String token) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			System.out.println("UserController,getEmployeeById,token: "+token);
			resultMap = new HashMap<String,Object>();
			//			System.out.println("UserController,saveOrUpdateDepartment,employeeList, size: "+departmentList.size());
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				Employee emply = userService.getEmployeeById(Integer.parseInt(employeeId));
				resultMap.put("employee", emply);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getEmployeeById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/user/getAllDepartment", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getAllDepartment(@RequestHeader("token") String token,
			String status) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{

			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			//			System.out.println("UserController,getAllEmployee,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				List<Department> departmentList = userService.getAllDepartment();
				resultMap.put("departmentList", departmentList);
				//				System.out.println("UserController,getAllDepartment,employeeList, size: "+departmentList.size());
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}

		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getAllDepartment, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/user/saveOrUpdateDepartment", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> saveOrUpdateDepartment(@RequestBody Department dept,@RequestHeader("token") String token) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			System.out.println("UserController,getAllEmployee,token: "+token);
			resultMap = new HashMap<String,Object>();
			//			System.out.println("UserController,saveOrUpdateDepartment,employeeList, size: "+departmentList.size());
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				Department department = userService.saveOrUpdateDepartment(dept);
				resultMap.put("department", department);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("saveOrUpdateDepartment, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="c", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> addNewEmployee(@RequestBody Employee employee,@RequestHeader("token") String token) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			System.out.println("UserController,addNewEmployee,token: "+token);
			resultMap = new HashMap<String,Object>();
			//			System.out.println("UserController,addNewEmployee,employeeList, size: "+departmentList.size());
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				Employee emply = userService.addNewEmployee(employee);
				resultMap.put("employee", emply);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("addNewEmployee, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/user/addNewDepartment", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> addNewDepartment(@RequestBody Department dept,@RequestHeader("token") String token) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			System.out.println("UserController,addNewDepartment,token: "+token);
			resultMap = new HashMap<String,Object>();
			//			System.out.println("UserController,addNewDepartment,employeeList, size: "+departmentList.size());
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				Department department = userService.addNewDepartment(dept);
				resultMap.put("department", department);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("addNewDepartment, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/user/getDepartmentById", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getDepartmentById(@RequestHeader("token") String token,String departmentId) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			System.out.println("UserController,getDepartmentById,token: "+token);
			resultMap = new HashMap<String,Object>();
			//			System.out.println("UserController,getDepartmentById,employeeList, size: "+departmentList.size());
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				Department department = userService.getDepartmentById(Integer.parseInt(departmentId));
				resultMap.put("department", department);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("getDepartmentById, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}


	@RequestMapping(value="/user/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> resetPassword(@RequestHeader("token") String token,
			@RequestBody Employee employee) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{

			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			System.out.println("UserController,resetPassword,token: "+token+", tokenStatus: "+tokenStatus);
			if(tokenStatus){
				String newPassword = userService.resetPassword(context,employee.getEmployeeId());
				resultMap.put("newPassword", newPassword);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("resetPassword, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/user/deleteEmployee", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> deleteEmployee(@RequestHeader("token") String token,
			@RequestBody Object object) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			//			System.out.println("UserController,deleteEmployee,token: "+token+", employeeId: "+employeeId);
			resultMap = new HashMap<String,Object>();
			//			System.out.println("UserController,getDepartmentById,employeeList, size: "+departmentList.size());
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				Employee employee = userService.deleteEmployee(token,object);
				resultMap.put("employee", employee);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteEmployee, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/user/forgetPassword", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> forgetPassword(@RequestBody ForgetPasswordModel forgetPasswordModel) throws Exception {

		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			System.out.println("UserController,forgetPassword,: ");
			ForgetPasswordModel forPasswordModel = userService.forgetPassword(context,forgetPasswordModel);
			resultMap.put("forgetPasswordModel", forPasswordModel);
			resultMap.put("status", "success");
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("forgetPassword, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/user/forgetPasswordReset", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> forgetPasswordReset(@RequestBody 
			ForgetPasswordModel forgetPasswordModel ) throws Exception {

		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			System.out.println("UserController,forgetPasswordReset,: ");
			String status = userService.forgetPasswordReset(forgetPasswordModel);
			resultMap.put("status", status);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "error");
			logger.error("forgetPasswordReset, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/user/resetPersonalPassword", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> resetPersonalPassword(@RequestHeader("token") String token,@RequestBody 
			ForgetPasswordModel forgetPasswordModel ) throws Exception {

		Map<String,Object> resultMap = null;
		try{
			resultMap = new HashMap<String,Object>();
			System.out.println("UserController,resetPersonalPassword,: ");
			String status = userService.resetPersonalPassword(token,forgetPasswordModel);
			resultMap.put("status", status);
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "error");
			logger.error("forgetPasswordReset, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

	@RequestMapping(value="/user/deleteDepartment", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> deleteDepartment(@RequestHeader("token") String token,
			@RequestBody int departmentId) throws ServletException {
		Map<String,Object> resultMap = null;
		boolean tokenStatus = false;
		try{
			System.out.println("UserController,deleteDepartment,token: "+token+", departmentId: "+departmentId);
			resultMap = new HashMap<String,Object>();
			tokenStatus = utilService.evaluateToken(token);
			if(tokenStatus){
				Department department = userService.deleteDepartment(token,departmentId);
				resultMap.put("department", department);
				resultMap.put("status", "success");
			}else{
				return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put("status", "failed");
			logger.error("deleteDepartment, "+e.getMessage());
			return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.NOT_FOUND);
		}
		return  new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
	}

}
