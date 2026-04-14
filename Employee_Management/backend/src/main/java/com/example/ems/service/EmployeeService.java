package com.example.ems.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.ems.model.Employee;

public interface EmployeeService {
  Page<Employee> getAll(String search, Pageable pageable);
  Employee getById(Long id);
  Employee create(Employee e);
  Employee update(Long id, Employee e);
  void delete(Long id);
}
