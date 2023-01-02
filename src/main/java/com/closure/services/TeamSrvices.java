package com.closure.services;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.closure.entity.Closure_details;
import com.closure.entity.Employee;
import com.closure.repository.ClosureRepo;
import com.closure.repository.EmployeeRepo;
import com.closure.utility.Utility;

@Service
public class TeamSrvices {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private ClosureRepo closureRepo;
	
	@Autowired 
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private JavaMailSender mailSender;	

	public String toString() {
		return null;
	}

	Closure_details cd = new Closure_details();
	Employee emp = new Employee();

//	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	DateTimeFormatter dtf_month = DateTimeFormatter.ofPattern("MMM-yyyy");
	DateTimeFormatter dtf_month2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	LocalDate now = LocalDate.now();
	int currentMonth = LocalDate.now().getMonthValue();
	int currentYear = LocalDate.now().getYear();
	int lastMonth = LocalDate.now().getMonthValue() - 1;
	int lastYear = LocalDate.now().getYear() - 1;

	public Employee validateEmp_jpa(String Username, String Password) {

		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
		Root<Employee> root = cr.from(Employee.class);

		cr.select(root).where(cb.equal(root.get("username"), Username), cb.equal(root.get("password"), Password));

		Query query = session.createQuery(cr);
		Employee results = (Employee) query.getSingleResult();
		session.close();
		return results;
	}

	public ResponseEntity<?> validateProfileUpdate(int empID,String currentPass, String empName, String email,String newPass ) {
//		

		Transaction transaction = null;
		Session session = sessionFactory.openSession();
		
		transaction = session.beginTransaction();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
		Root<Employee> root = cr.from(Employee.class);

		cr.select(root).where(cb.equal(root.get("empid"), empID), cb.equal(root.get("password"), currentPass));

		Query query = session.createQuery(cr);
		Employee results = null;
		results = (Employee) query.getSingleResult();
		System.out.print("results : "+ results);

		if (results != null) {
			System.out.println("User is already exist..!");
			
			emp = employeeRepo.getById(empID);
			emp.setEmp_name(empName);
			emp.setUsername(email);
			emp.setPassword(newPass);
//			emp.setRole("TM");

			System.out.print("Profile updated successful!");
//			session.save(emp);
			employeeRepo.save(emp);
			transaction.commit();

			session.close();
			return new ResponseEntity<Employee>(emp, HttpStatus.OK);			
//			return new ResponseEntity<Employee>(results, HttpStatus.OK);
		} 
		else {
			System.out.println("// write update code here");
			session.close();
			return (ResponseEntity<?>) ResponseEntity.badRequest().body("Account dosn't exist!!!");
		}
		
//		return "hello";

	}

	public List<Employee> getAllEmp_TM() {

		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
		Root<Employee> root = cr.from(Employee.class);
		cr.select(root).where(cb.equal(root.get("role"), "TM"));

		Query query = session.createQuery(cr);
		List<Employee> results = query.getResultList();
		session.close();
		return results;
	}

	public Employee validateEmployee(String name, String password) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
		Root<Employee> root = cr.from(Employee.class);
		cr.select(root).where(cb.equal(root.get("ename"), name), (cb.equal(root.get("password"), password)));
		Query query = session.createQuery(cr);
		Employee results = (Employee) query.getSingleResult();
		session.close();
		return results;
	}

	public ResponseEntity<?> AddClosure(int req, int sub, int first, int second, int closure, int empid) {

		Session hbmsession = null;
		Transaction transaction = null;

		cd.setClo_date(now);
//		cd.setClo_month(dtf_month.format(now));
		cd.setRequirement(req);
		cd.setSubmission(sub);
		cd.setFirst(first);
		cd.setSecond(second);
		cd.setClosure(closure);
		emp.setEmpid(empid);
		cd.setEmployee(emp);

		hbmsession = sessionFactory.openSession();
		transaction = hbmsession.beginTransaction();
		hbmsession.save(cd);
		transaction.commit();
		hbmsession.close();

		return new ResponseEntity<Closure_details>(cd, HttpStatus.OK);
	}

	public List<Closure_details> GetClosure() {
		return closureRepo.findAll();
	}

	public void deleteClosureByID(int closureid) {
		System.out.println("Closure id for deletion : " + closureid);
		closureRepo.deleteById(closureid);
	}

	public List<Closure_details> GetClosurebyId(int empid) throws InterruptedException {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Closure_details> cr = cb.createQuery(Closure_details.class);
		Root<Closure_details> root = cr.from(Closure_details.class);
		cr.select(root).where(cb.equal(root.get("employee").get("empid"), empid));
		Query query = session.createQuery(cr);
		List<Closure_details> results = query.getResultList();
		session.close();

		return results;
	}

	public List<Employee> GetEmployeebyId(int empid) throws InterruptedException {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Employee> cr = cb.createQuery(Employee.class);
		Root<Employee> root = cr.from(Employee.class);
		cr.select(root).where(cb.equal(root.get("empid"), empid));
		Query query = session.createQuery(cr);
		List<Employee> results = query.getResultList();
		session.close();
		System.out.print(results + "#####################");
		return results;
	}

	public List<Closure_details> GetRecordofMonth(int empid, String month) throws InterruptedException {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Closure_details> cr = cb.createQuery(Closure_details.class);
		Root<Closure_details> root = cr.from(Closure_details.class);
		cr.select(root).where(cb.equal(root.get("employee").get("empid"), empid),
				(cb.equal(root.get("clo_month"), month)));
		Query query = session.createQuery(cr);
		List<Closure_details> results = query.getResultList();

		System.out.println(results);
		session.close();
		return results;
	}

	public List<Closure_details> GetRecordBetDate(int empid, String date1, String date2)
			throws InterruptedException, ParseException {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		System.out.println("Start date: " + date1 + " End date: " + date2);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dt1 = LocalDate.parse(date1, formatter);
		System.out.println(dt1);
		LocalDate dt2 = LocalDate.parse(date2, formatter);
		System.out.println(dt2);

		CriteriaQuery<Closure_details> cr = cb.createQuery(Closure_details.class);
		Root<Closure_details> root = cr.from(Closure_details.class);
		cr.select(root).where(cb.equal(root.get("employee").get("empid"), empid),
				(cb.between(root.get("clo_date"), dt1, dt2)));
		Query query = session.createQuery(cr);
		List<Closure_details> results = query.getResultList();

		System.out.println(results);
		session.close();

//		if(results.isEmpty()) {
		if ((results.size()) == 0) {
			return (List<Closure_details>) ResponseEntity.badRequest();
		} else {

			return results;
		}
	}

	public List<Closure_details> GetRecordBetDate2(String date1, String date2)
			throws InterruptedException, ParseException {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		System.out.println("Start date: " + date1 + " End date: " + date2);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dt1 = LocalDate.parse(date1, formatter);
		System.out.println(dt1);
		LocalDate dt2 = LocalDate.parse(date2, formatter);
		System.out.println(dt2);

		CriteriaQuery<Closure_details> cr = cb.createQuery(Closure_details.class);
		Root<Closure_details> root = cr.from(Closure_details.class);
		cr.select(root).where(cb.between(root.get("clo_date"), dt1, dt2));
		Query query = session.createQuery(cr);
		List<Closure_details> results = query.getResultList();

		System.out.println(results);
		session.close();
//		return results;

		if (results.isEmpty()) {
			return (List<Closure_details>) ResponseEntity.badRequest();
		} else {

			return results;
		}
	}

	public List<Closure_details> GetRecordsByQuarterly(int empid, String category) {

		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();

		LocalDate a = now;
		System.out.println(a);

		LocalDate e = now.minusMonths(1);
		System.out.println(e);

		LocalDate b = now.minusMonths(3);
		System.out.println(b);

		LocalDate c = now.minusMonths(6);
		System.out.println(c);

		LocalDate d = now.minusMonths(12);
		System.out.println(d);

		CriteriaQuery<Closure_details> cr = cb.createQuery(Closure_details.class);
		Root<Closure_details> root = cr.from(Closure_details.class);

		if (category.equals("Last_Month")) {

			cr.select(root).where((cb.equal(root.get("employee").get("empid"), empid)),
					(cb.equal(cb.function("MONTH", Integer.class, root.get("clo_date")), lastMonth)),
					(cb.equal(cb.function("YEAR", Integer.class, root.get("clo_date")), currentYear)));

			Query query = session.createQuery(cr);
			List<Closure_details> results = query.getResultList();

			session.close();
//			return results;
			if (results.isEmpty()) {
				return (List<Closure_details>) ResponseEntity.badRequest();
			} else {

				return results;
			}

		} else if (category.equals("Quarterly")) {

			cr.select(root).where(cb.equal(root.get("employee").get("empid"), empid),
					(cb.between(root.get("clo_date"), b, a)));
			Query query = session.createQuery(cr);
			List<Closure_details> results = query.getResultList();

			session.close();
//			return results;
			if (results.isEmpty()) {
				return (List<Closure_details>) ResponseEntity.badRequest();
			} else {

				return results;
			}

		} else if (category.equals("Half-yearly")) {
			cr.select(root).where(cb.equal(root.get("employee").get("empid"), empid),
					(cb.between(root.get("clo_date"), c, a)));
			Query query = session.createQuery(cr);
			List<Closure_details> results = query.getResultList();

			System.out.println(results);
			session.close();
//			return results;

			if (results.isEmpty()) {
				return (List<Closure_details>) ResponseEntity.badRequest();
			} else {

				return results;
			}

		} else if (category.equals("Yearly")) {
			cr.select(root).where(cb.equal(root.get("employee").get("empid"), empid),
					(cb.between(root.get("clo_date"), d, a)));
			Query query = session.createQuery(cr);
			List<Closure_details> results = query.getResultList();

			System.out.println(results);
			session.close();
//			return results;
			if (results.isEmpty()) {
				return (List<Closure_details>) ResponseEntity.badRequest();
			} else {

				return results;
			}

		} else if (category.equals("allcat")) {
			cr.select(root).where(cb.equal(root.get("employee").get("empid"), empid));

			Query query = session.createQuery(cr);
			List<Closure_details> results = query.getResultList();

			session.close();
//            return results;
			if (results.isEmpty()) {
				return (List<Closure_details>) ResponseEntity.badRequest();
			} else {

				return results;
			}

		} else {
//			return null;
			return (List<Closure_details>) ResponseEntity.badRequest();
		}
//		System.out.printf("Today: %s\n", thisMonth.format(monthYearFormatter));		
	}

	public ResponseEntity<?> EmpRegistration(String empName, String Username, String Password) {
		// TODO Auto-generated method stub

		Session session = null;
		Transaction transaction = null;

		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

		Criteria crt = session.createCriteria(Employee.class);
		crt.add(Restrictions.eq("username", Username));

		Employee z = (Employee) crt.uniqueResult();
		System.out.print("z = "+z);

		if (z != null) {
			System.out.println("User is already exist..!");
			session.close();
			return (ResponseEntity<?>) ResponseEntity.badRequest().body("User is already exist!");

		} else {
			emp.setEmp_name(empName);
			emp.setUsername(Username);
			emp.setPassword(Password);
			emp.setRole("TM");

			System.out.print("Registration Successful Employee");
			session.save(emp);
			transaction.commit();

			session.close();
//			return null;
			return new ResponseEntity<Employee>(emp, HttpStatus.OK);
		}
	}

	public Closure_details UpdateRecord(int clsid, int req, int sub, int first, int second, int closure) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		cd = closureRepo.getById(clsid);
		System.out.println("Closure id = " + cd);
		cd.setRequirement(req);
		cd.setSubmission(sub);
		cd.setFirst(first);
		cd.setSecond(second);
		cd.setClosure(closure);
		closureRepo.save(cd);

		// session.saveOrUpdate(cd);
		transaction.commit();
		session.close();
		return null;
	}

	public Closure_details UpdateRecordAdmin(int clsid, int req, int sub, int first, int second, int closure,
			String date) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dt1 = LocalDate.parse(date, formatter);

		cd = closureRepo.getById(clsid);
		System.out.println("Closure id = " + cd);
		cd.setRequirement(req);
		cd.setSubmission(sub);
		cd.setFirst(first);
		cd.setSecond(second);
		cd.setClosure(closure);
		cd.setClo_date(dt1);
		closureRepo.save(cd);

		// session.saveOrUpdate(cd);
		transaction.commit();
		session.close();
		return null;
	}

	public List<Closure_details> RecordsOfCurMonth(int empid) {
//	public ResponseEntity<Closure_details> RecordsOfCurMonth(int empid){

		System.out.println("LastMonth : " + lastMonth);

		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();

		CriteriaQuery<Closure_details> cr = cb.createQuery(Closure_details.class);
		Root<Closure_details> root = cr.from(Closure_details.class);

		cr.select(root).where(cb.equal(root.get("employee").get("empid"), empid),
				(cb.equal(cb.function("MONTH", Integer.class, root.get("clo_date")), currentMonth)),
				(cb.equal(cb.function("YEAR", Integer.class, root.get("clo_date")), currentYear)));

		Query query = session.createQuery(cr);
		List<Closure_details> results = query.getResultList();
		System.out.println("Arraylength= " + results.size());

		session.close();
		if ((results.size()) == 0) {
			return (List<Closure_details>) ResponseEntity.badRequest();
		} else {
			return results;
		}
//		return results;
	}

	public List<Closure_details> RecordsOfCurMonthAll() {

		System.out.println("currentMonth : " + currentMonth);
		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();

		CriteriaQuery<Closure_details> cr = cb.createQuery(Closure_details.class);
		Root<Closure_details> root = cr.from(Closure_details.class);

		cr.select(root).where(cb.equal(cb.function("MONTH", Integer.class, root.get("clo_date")), currentMonth),
				(cb.equal(cb.function("YEAR", Integer.class, root.get("clo_date")), currentYear)));
		Query query = session.createQuery(cr);

		List<Closure_details> results = query.getResultList();

		session.close();
		return results;
	}

	public List<Closure_details> RecordsByCate(String category) {

		Session session = sessionFactory.openSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();

		LocalDate a = now;
		System.out.println(a);

		LocalDate b = now.minusMonths(3);
		System.out.println(b);

		LocalDate c = now.minusMonths(6);
		System.out.println(c);

		LocalDate d = now.minusMonths(12);
		System.out.println(d);

		CriteriaQuery<Closure_details> cr = cb.createQuery(Closure_details.class);
		Root<Closure_details> root = cr.from(Closure_details.class);

		if (category.equals("Last_Month")) {

			cr.select(root).where(cb.equal(cb.function("MONTH", Integer.class, root.get("clo_date")), lastMonth),
					(cb.equal(cb.function("YEAR", Integer.class, root.get("clo_date")), currentYear)));

			Query query = session.createQuery(cr);

			List<Closure_details> results = query.getResultList();

			session.close();
			return results;
		} else if (category.equals("Quarterly")) {

			cr.select(root).where(cb.between(root.get("clo_date"), b, a));
			Query query = session.createQuery(cr);
			List<Closure_details> results = query.getResultList();

			session.close();
			return results;

		} else if (category.equals("Half-yearly")) {
			cr.select(root).where(cb.between(root.get("clo_date"), c, a));
			Query query = session.createQuery(cr);
			List<Closure_details> results = query.getResultList();

			System.out.println(results);
			session.close();
			return results;

		} else if (category.equals("Yearly")) {
			cr.select(root).where(cb.between(root.get("clo_date"), d, a));
			Query query = session.createQuery(cr);
			List<Closure_details> results = query.getResultList();

			System.out.println(results);
			session.close();
			return results;

		} else if (category.equals("allcat")) {
			cr.select(root);

			Query query = session.createQuery(cr);
			List<Closure_details> results = query.getResultList();

			session.close();
			return results;
		} else {
			return null;
		}	
	}
	
//------------------------------------Forgot Password Code-------------------------------------
	public ResponseEntity<String> ForgetPassword(String username, String password) {
		// TODO Auto-generated method stub
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();		

		Criteria crt = session.createCriteria(Employee.class);
		crt.add(Restrictions.eq("ename", username));
		Employee z= (Employee) crt.uniqueResult();			
		
	    if(z==null) {
	        System.out.println("User not exist..!");
	        return  
			ResponseEntity.badRequest().body("User not exist!");
	    }
		
	    else {	    	
	    		emp= employeeRepo.findByUsername(username);
				emp.setPassword(password);				
				
				System.out.print("Password Changed");
				employeeRepo.save(emp);
				transaction.commit();
			
				session.close();
				return ResponseEntity.ok("Password Changed");
			}
	    
		}

	public String updateResetPasswordToken(int token, String email) throws Exception {
	    emp = employeeRepo.findByUsername(email);
	   String a= emp.getEmp_name();
	    //e=emp.getEmpname();
	    //System.out.println(a);
	    if (emp != null) {
	        emp.setResetPasswordToken(token);
	        employeeRepo.save(emp);
	    } else {
	        throw new Exception("Could not find any customer with the email " + email);
	    }
	    return a;
	}
	 
	public Employee getByResetPasswordToken(int token) {
	    return employeeRepo.findByResetPasswordToken(token);
	}
	 
	public void updatePassword(Employee emp, String newPassword) {
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(newPassword);
	    emp.setPassword(encodedPassword);
	     
	    emp.setResetPasswordToken(0);
	    employeeRepo.save(emp);
	}

	public  ResponseEntity<String> processForgotPassword(String email, HttpServletRequest request) {

		int min = 10000;  
		int max = 99999;  
		int token = (int)(Math.random()*(max-min+1)+min); 
		//email = "anjulikajawadekar@gmail.com";
		
		  //  String token = RandomString.make(5);
		     //String e=null;
		    try {
		      String name=  this.updateResetPasswordToken(token, email);
		   
		      //  String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
		        String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password";
		        sendEmail(email, resetPasswordLink,token,name);
		        System.out.println("We have sent a reset password link to your email. Please check.");
		        
		        return  ResponseEntity.ok("ok");
		        
		    } catch (Exception ex) {
		       System.out.println(ex);
		       return ResponseEntity.badRequest().body("User not exist!");
		    }
		         
		   // return null;
	}

	public void sendEmail(String recipientEmail, String link,int token,String name)
	        throws MessagingException, UnsupportedEncodingException {
		

		 MimeMessage message = mailSender.createMimeMessage();              
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		    
		    
		    
	   //     MimeMessagePreparator preparator = mimeMessage -> {
	    //        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
		    
		    
		    helper.setFrom("anjulikajawadekar@gmail.com", "US Staffing");
		  
	    //helper.setFrom(new InternetAddress("k12soham@gmail.com"));
		    helper.setTo(recipientEmail);
	    
	    String subject = "Here's the link to reset your password";
	     
	    String content = "<p>Hello "+name+",</p>"
	    		
	            + "<p>You have requested to reset your password.</p>"
	            + "<p>Here is your OTP: "+token+"<br>"
	            + "<p>Ignore this email if you do remember your password, "
	            + "or you have not made the request.</p>";
	     
	    message.setSubject(subject);	     
	    helper.setText(content, true);  	        
	    mailSender.send(message);

	}



	public ResponseEntity<String> showResetPasswordForm(int token) {
		System.out.println(token);
		Employee e= getByResetPasswordToken(token);
	    if (e == null) {
	    	 System.out.println("wrong");
		 return ResponseEntity.badRequest().body("User not exist!");
		
	    }
	    else
	    {
	    	 System.out.println("okkk");
	    	return ResponseEntity.ok("ok");
	    }
	}
		

	}


