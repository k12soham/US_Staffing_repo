package com.closure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.closure.entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

	public Employee findByUsername(String ename);
    public Employee findByResetPasswordToken(int token);
}
