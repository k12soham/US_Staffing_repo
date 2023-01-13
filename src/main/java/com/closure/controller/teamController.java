package com.closure.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.closure.entity.Closure_details;
import com.closure.entity.Employee;
import com.closure.repository.ClosureRepo;
import com.closure.repository.EmployeeRepo;
import com.closure.services.TeamSrvices;

@CrossOrigin
@RestController
public class teamController {

	@Autowired
	SessionFactory sessionFactory = null;
	@Autowired
	private TeamSrvices service;
	@Autowired
	private ClosureRepo closureRepo;
	@Autowired
	private EmployeeRepo employeeRepo;

	@GetMapping("/home")
	public String home() {
		return "Hello there!!!!!!!!!!";
	}

	@PostMapping("/login")
	public ResponseEntity validateEmp(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String Username, @RequestParam String Password) {

		Employee emp = (Employee) service.validateEmp_jpa(Username, Password);
		if (emp != null) {
			return new ResponseEntity<>(emp, HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body("false");
		}
	}

	@GetMapping("/getEmpList_TM")
	public List<Employee> getEMP_TM(HttpServletRequest request, HttpServletResponse response) {
		return service.getAllEmp_TM();
	}

	@PostMapping(value = "/add_cls")
	public ResponseEntity<?> AddClosure(@RequestParam int req, @RequestParam int sub, @RequestParam int first,
			@RequestParam int second, @RequestParam int closure, @RequestParam int empid) {
		return service.AddClosure(req, sub, first, second, closure, empid);
	}

	@DeleteMapping(value = "/delete_clsByID")
	public boolean deleteClosure(@RequestParam int closureid) {
		this.service.deleteClosureByID(closureid);
		return true;
	}

	@GetMapping(value = "/get_cls")
	public List<Closure_details> GetClosure() {
		return this.service.GetClosure();
	}

	@GetMapping(value = "/get_cls_id")
	public List<Closure_details> GetClosurebyId(@RequestParam int empid) throws InterruptedException {
		return this.service.GetClosurebyId(empid);
	}

	@GetMapping(value = "/get_EmpById")
	public List<Employee> GetEmpById(@RequestParam int empid) throws InterruptedException {
		return this.service.GetEmployeebyId(empid);
	}

	@GetMapping(value = "/get_cls_by_month")
	public List<Closure_details> GetRecordofMonth(@RequestParam int empid, String month) throws InterruptedException {
		return this.service.GetRecordofMonth(empid, month);
	}

	@GetMapping(value = "/get_cls_byDate")
	public List<Closure_details> GetRecordsBetweenDate(@RequestParam int empid, String date1, String date2)
			throws InterruptedException, ParseException {
		return this.service.GetRecordBetDate(empid, date1, date2);
	}

	@GetMapping(value = "/get_cls_byDate2")
	public List<Closure_details> GetRecordsBetweenDateAllEmp(String date1, String date2)
			throws InterruptedException, ParseException {
		return this.service.GetRecordBetDate2(date1, date2);
	}

	@PostMapping(value = "/addEmp")
	public ResponseEntity<?> AddEmp(@RequestParam String empName, @RequestParam String Username,
			@RequestParam String Password) {

		return service.EmpRegistration(empName, Username, Password);
	}

	@GetMapping(value = "/get_cls_by_Quarterly")
	public List<Closure_details> getClsQua(@RequestParam int empid, @RequestParam String category) {
		return service.GetRecordsByQuarterly(empid, category);
	}

	@GetMapping("/getRecordByCate")
	public List<Closure_details> recordByCate(String category) {
		return service.RecordsByCate(category);
	}

	@PutMapping("/update_record")
	public Closure_details UpdateRecord(@RequestParam int clsid, @RequestParam int req, @RequestParam int sub,
			@RequestParam int first, @RequestParam int second, @RequestParam int closure) {

		return service.UpdateRecord(clsid, req, sub, first, second, closure);
	}

	@PutMapping("/update_record_admin")
	public Closure_details UpdateRecordAdmin(@RequestParam int clsid, @RequestParam int req, @RequestParam int sub,
			@RequestParam int first, @RequestParam int second, @RequestParam int closure, @RequestParam String date) {

		return service.UpdateRecordAdmin(clsid, req, sub, first, second, closure, date);
	}

	@GetMapping("/getRecordsOfCurMonth")
	public List<Closure_details> getCurrentRecords(@RequestParam int empid) {

		return (List<Closure_details>) service.RecordsOfCurMonth(empid);
	}

	@GetMapping("/CurMonthAll")
	public List<Closure_details> getCurrentRecordsAll() {

		return service.RecordsOfCurMonthAll();
	}

	@PostMapping("/updateProfile")
	public ResponseEntity<?> updateProfile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam int empID, String currentPass, String empName, String email, String newPass) {

		ResponseEntity<?> emp = service.validateProfileUpdate(empID, currentPass, empName, email, newPass);

		return emp;
	}

//	--------------------------------------Forgot password code----------------------------------
	@PostMapping(value = "/forgetpass")
	public ResponseEntity<String> ForgetPassword(@RequestParam String username, @RequestParam String password) {
		return this.service.ForgetPassword(username, password);
	}

	@PostMapping("/forgot_password")
	public ResponseEntity<String> processForgotPassword(@RequestParam String email, HttpServletRequest request) {
		return service.processForgotPassword(email, request);
	}

	@GetMapping("/reset_password")
	public ResponseEntity<String> showResetPasswordForm(HttpServletRequest request, @RequestParam int token) {

		return service.showResetPasswordForm(token);

	}
}
