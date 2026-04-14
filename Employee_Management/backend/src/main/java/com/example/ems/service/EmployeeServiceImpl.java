package com.example.ems.service;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.ems.model.Employee;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.exception.ResourceNotFoundException;

@Service
public class EmployeeServiceImpl implements EmployeeService {
  private final EmployeeRepository repo;

  @Autowired
  public EmployeeServiceImpl(EmployeeRepository repo) { this.repo = repo; }

  @Override
  public Page<Employee> getAll(String search, Pageable pageable) {
    if (search == null || search.isBlank()) {
      return repo.findAll(pageable);
    }
    return repo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
      search, search, search, pageable);
  }

  @Override
  public Employee getById(Long id) {
    return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
  }

  @Override
  public Employee create(Employee e) { return repo.save(e); }

  @Override
  public Employee update(Long id, Employee e) {
    Employee existing = getById(id);
    existing.setFirstName(e.getFirstName());
    existing.setLastName(e.getLastName());
    existing.setEmail(e.getEmail());
    existing.setDepartment(e.getDepartment());
    existing.setSalary(e.getSalary());
    return repo.save(existing);
  }

  @Override
  public void delete(Long id) {
    Employee existing = getById(id);
    repo.delete(existing);
  }
}
