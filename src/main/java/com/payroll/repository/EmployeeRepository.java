package com.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payroll.entity.Employee;

/*
 * Interface to implement JPA
 * */
public interface EmployeeRepository extends JpaRepository <Employee, Long> {

}
