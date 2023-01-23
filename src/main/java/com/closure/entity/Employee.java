package com.closure.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@Table(name = "employee")
public class Employee {
	
	@Id
	@Column(name = "empid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.TABLE, generator = "employee")
//	@TableGenerator(name="employee", table="employee_generator", schema="closure_database")

	private Integer empid;
	private String emp_name;
	private String username;
	private String password;
	private String role;
	private int resetPasswordToken;

	@OneToMany(mappedBy = "employee")
	@JsonIgnore
	private List<Closure_details> cl_details;

	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(Integer empid, String emp_name, String username, String password, String role,
			int resetPasswordToken, List<Closure_details> cl_details) {
		super();
		this.empid = empid;
		this.emp_name = emp_name;
		this.username = username;
		this.password = password;
		this.role = role;
		this.resetPasswordToken = resetPasswordToken;
		this.cl_details = cl_details;
	}



	public Integer getEmpid() {
		return empid;
	}

	public void setEmpid(Integer empid) {
		this.empid = empid;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Closure_details> getCl_details() {
		return cl_details;
	}

	public void setCl_details(List<Closure_details> cl_details) {
		this.cl_details = cl_details;
	}

	public int getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(int resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}
}

