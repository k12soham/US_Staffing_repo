package com.closure.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Closure_details {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "closureid")	
	private Integer closureid;
	private LocalDate clo_date;
	private int requirement;
	private int submission;
	private int first;
	private int second;
	private int closure;
//	private String clo_month;
	
	@ManyToOne
	@JoinColumn(name ="empid")
	private Employee employee;

	public Closure_details() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Closure_details(Integer closureid, LocalDate clo_date, int requirement, int submission,
			int first, int second, int closure, Employee employee) {
		super();
		this.closureid = closureid;
//		this.date_closure = date_closure;
		this.clo_date = clo_date;
		this.requirement = requirement;
		this.submission = submission;
		this.first = first;
		this.second = second;
		this.closure = closure;
//		this.clo_month = clo_month;
		this.employee = employee;
	}



	public int getClosureid() {
		return closureid;
	}

	public void setClosureid(int closureid) {
		this.closureid = closureid;
	}

//	public String getDate_closure() {
//		return date_closure;
//	}
//
//	public void setDate_closure(String date_closure) {
//		this.date_closure = date_closure;
//	}

	public int getRequirement() {
		return requirement;
	}

	public void setRequirement(int requirement) {
		this.requirement = requirement;
	}

	public int getSubmission() {
		return submission;
	}

	public void setSubmission(int submission) {
		this.submission = submission;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getClosure() {
		return closure;
	}

	public void setClosure(int closure) {
		this.closure = closure;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

//	public String getClo_month() {
//		return clo_month;
//	}
//
//	public void setClo_month(String clo_month) {
//		this.clo_month = clo_month;
//	}

	public LocalDate getClo_date() {
		return clo_date;
	}
	
	public void setClo_date(LocalDate clo_date) {
		this.clo_date = clo_date;
	}

	public void setClosureid(Integer closureid) {
		this.closureid = closureid;
	}
}
