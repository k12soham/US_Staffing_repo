package com.closure.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.closure.entity.Closure_details;

public interface ClosureRepo extends JpaRepository<Closure_details, Integer> {
//	List<Closure_details> findByDateAtBetween(Date startDate, Date endDate);
}
