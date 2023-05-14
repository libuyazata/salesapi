package com.yaz.alind.dao;

import java.util.List;

import javax.persistence.criteria.Expression;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yaz.alind.model.Department;
import com.yaz.alind.model.Employee;
import com.yaz.alind.model.ForgetPasswordModel;
import com.yaz.alind.model.TokenModel;


public class UserDAOImpl implements UserDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	@Autowired
	private SessionFactory sessionFactory;


	@Override
	@Transactional
	public TokenModel saveOrUpdateToken(TokenModel tokenModel) {
		try{
			TokenModel tModel = getTokenModelByUserId(tokenModel.getUserId());
//			System.out.println("saveOrUpdateToken,tModel: "+tModel);
			if(tModel != null){
				this.sessionFactory.getCurrentSession().merge(tokenModel);
			}else{
				this.sessionFactory.getCurrentSession().save(tokenModel);
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateToken: "+e.getMessage());
		}
		return tokenModel;
	}


	@Override
	@Transactional
	public TokenModel getTokenModelByUserId(int userId) {
		TokenModel tokenModel = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(TokenModel.class);
//			System.out.println("UserDAOImpl,getTokenModelByUserId,userId: "+userId);
			cr.add(Restrictions.eq("userId", userId));
			List<TokenModel> list = cr.list();
			if(list.size() > 0){
				tokenModel = list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getTokenModelByUserId: "+e.getMessage());
		}
		return tokenModel;
	}

	@Override
	@Transactional
	public TokenModel getTokenModelByToken(String token) {
		TokenModel tokenModel = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(TokenModel.class);
			cr.add(Restrictions.eq("token", token));
			List<TokenModel> list = cr.list();
			if(list.size() > 0){
				tokenModel = list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getTokenModelByToken: "+e.getMessage());
		}
		return tokenModel;
	}


	@Override
	@Transactional
	public List<Employee> getAllEmployee() {
		List<Employee> emplList = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Employee.class);
			cr.add(Restrictions.eq("isActive", 1));
			List<Employee> list = cr.list();
//			System.out.println("DAO,getAllEmployee,list, size: "+list.size());
			emplList = list;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllEmployee: "+e.getMessage());
		}
		return emplList;
	}


	@Override
	@Transactional
	public List<Department> getAllDepartment() {
		List<Department> departments = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Department.class);
			List<Department> list = cr.list();
//			System.out.println("DAO,getAllDepartment,list, size: "+list.size());
			departments = list;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAllDepartment: "+e.getMessage());
		}
		return departments;
	}


	@Override
	@Transactional
	public Department saveOrUpdateDepartment(Department department) {
		Department dept = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(department);
			dept = department;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateDepartment: "+e.getMessage());
		}
		return dept;
	}


	@Override
	@Transactional
	public Employee getAuthentication(String userName, String password) {
		Employee employee = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Employee.class);
			cr.add(Restrictions.eq("userName", userName));
			cr.add(Restrictions.eq("viewPass", password));
			cr.add(Restrictions.eq("isActive", 1));
			List<Employee> list = cr.list();
			if(list.size() > 0){
				employee = list.get(0);
			}
//			System.out.println("DAO,getAuthentication,list, size: "+list.size());
			logger.info("getAuthentication,list, size: "+list.size());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAuthentication: "+e.getMessage());
		}
		return employee;
	}


	@Override
	@Transactional
	public Employee getEmployeeById(int employeeId) {
		Employee employee = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Employee.class);
			cr.add(Restrictions.eq("employeeId", employeeId));
			List<Employee> list = cr.list();
			if(list.size() > 0){
				employee = list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getEmployeeById: "+e.getMessage());
		}
		return employee;
	}


	@Override
	@Transactional
	public Employee saveOrUpdateEmployee(Employee employee) {
		Employee emply = null;
		try{
//			System.out.println("DAOUserService,saveOrUpdateEmployee,New, emp id: "+employee.getEmployeeId()
//					+",view Pass: "+employee.getViewPass());
			this.sessionFactory.getCurrentSession().saveOrUpdate(employee);
			emply=employee;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateEmployee: "+e.getMessage());
		}
		return emply;
	}


	@Override
	@Transactional
	public Employee addNewEmployee(Employee employee) {
		Employee emply = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(employee);
			emply = employee;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("addNewEmployee: "+e.getMessage());
		}
		return emply;
	}


	@Override
	@Transactional
	public Department addNewDepartment(Department department) {
		Department dept = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(department);
			dept = department;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("addNewDepartment: "+e.getMessage());
		}
		return dept;
	}


	@Override
	@Transactional
	public Department getDepartmentById(int departmentId) {
		Department department = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Department.class);
			cr.add(Restrictions.eq("departmentId", departmentId));
			List<Department> depList= cr.list();
			if(depList.size() >0){
				department = depList.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getDepartmentById: "+e.getMessage());
		}
		return department;
	}
	
	@Override
	@Transactional
	public List<Employee> searchEmployee(String searchKeyword){
		
		List<Employee> employees = null;
		try{
//			System.out.println("UserDAO,searchEmployee, searchKeyword: "+searchKeyword);
			Criteria employeeCriteria = this.sessionFactory.getCurrentSession().createCriteria(Employee.class,"emp");
			Criteria deptCriteria = employeeCriteria.createAlias("emp.department", "dept");
			deptCriteria.add(Restrictions.eq("emp.isActive", 1));
			
			if(!searchKeyword.isEmpty()){
				Criterion empFirstName = Restrictions.ilike("emp.firstName", searchKeyword, MatchMode.ANYWHERE);
				Criterion emailId = Restrictions.ilike("emp.emailId", searchKeyword, MatchMode.ANYWHERE);
				Criterion empCode = Restrictions.ilike("emp.empCode", searchKeyword, MatchMode.ANYWHERE);
				Criterion gender = Restrictions.ilike("emp.gender", searchKeyword, MatchMode.ANYWHERE);
				Criterion lastName = Restrictions.ilike("emp.lastName", searchKeyword, MatchMode.ANYWHERE);
				Criterion position = Restrictions.ilike("emp.position", searchKeyword, MatchMode.ANYWHERE);
				Criterion primaryMobileNo = Restrictions.ilike("emp.primaryMobileNo", searchKeyword, MatchMode.ANYWHERE);
				Criterion secondaryEmailId = Restrictions.ilike("emp.secondaryEmailId", searchKeyword, MatchMode.ANYWHERE);
				Criterion secondaryMobileNo = Restrictions.ilike("emp.secondaryMobileNo", searchKeyword, MatchMode.ANYWHERE);
				Criterion userName = Restrictions.ilike("emp.userName", searchKeyword, MatchMode.ANYWHERE);
				Criterion departmentName = Restrictions.ilike("dept.departmentName", searchKeyword, MatchMode.ANYWHERE);
				Criterion fullNameEmp =  Restrictions.ilike("emp.fullNameEmp", searchKeyword, MatchMode.ANYWHERE);
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(empFirstName);
				disjunction.add(emailId);
				disjunction.add(empCode);
				disjunction.add(gender);
				disjunction.add(lastName);
				disjunction.add(position);
				disjunction.add(primaryMobileNo);
				disjunction.add(secondaryEmailId);
				disjunction.add(secondaryMobileNo);
				disjunction.add(userName);
				disjunction.add(departmentName);
				disjunction.add(fullNameEmp);
				deptCriteria.add(disjunction);
			}//if(!searchKeyword.isEmpty())
			employees = deptCriteria.list();
		}catch(Exception e){
			e.printStackTrace();
			logger.error("searchEmployee: "+e.getMessage());
		}
		return employees;
		
	}
	
	@Override
	@Transactional
	public Employee getEmployeeByEmpCodeEmailId(String userName, String emailId) {
		Employee employee = null;
		try{
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(Employee.class);
			cr.add(Restrictions.eq("userName", userName));
			cr.add(Restrictions.eq("emailId", emailId));
			List list = cr.list();
			if(list.size() > 0){
				employee = (Employee)list.get(0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getEmployeeByEmpCodeEmailId: "+e.getMessage());
		}
		return employee;
	}


	@Override
	@Transactional
	public ForgetPasswordModel saveOrUpdateForgotPassword(
			ForgetPasswordModel forgetPasswordModel) {
		ForgetPasswordModel fModel = null;
		try{
			this.sessionFactory.getCurrentSession().saveOrUpdate(forgetPasswordModel);
			fModel = forgetPasswordModel;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveOrUpdateForgotPassword: "+e.getMessage());
		}
		return fModel;
	}


	@Override
	@Transactional
	public ForgetPasswordModel getForgetPasswordModel(int otp, String uuId) {
		ForgetPasswordModel fModel = null;
		try{
			
			Criteria cr = this.sessionFactory.getCurrentSession().createCriteria(ForgetPasswordModel.class);
			cr.add(Restrictions.eq("otp", otp));
			cr.add(Restrictions.eq("uuId", uuId));
			List list = cr.list();
			if(list.size() > 0){
				fModel = (ForgetPasswordModel) list.get(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getForgetPasswordModel: "+e.getMessage());
		}
		return fModel;
	}

}
