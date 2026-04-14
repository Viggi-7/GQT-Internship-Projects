package com.example.ems.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ems.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
      String firstName, String lastName, String email, Pageable pageable);
}
