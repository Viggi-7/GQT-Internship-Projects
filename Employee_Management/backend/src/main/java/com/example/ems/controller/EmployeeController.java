package com.example.ems.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import com.example.ems.model.Employee;
import com.example.ems.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173") // Vite default dev server
@Validated
public class EmployeeController {
  private final EmployeeService service;

  public EmployeeController(EmployeeService service) { this.service = service; }

  @GetMapping
  public Page<Employee> list(@RequestParam(required = false) String search, Pageable pageable) {
    return service.getAll(search, pageable);
  }

  @GetMapping("/{id}")
  public Employee get(@PathVariable Long id) {
    return service.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Employee create(@Valid @RequestBody Employee e) {
    return service.create(e);
  }

  @PutMapping("/{id}")
  public Employee update(@PathVariable Long id, @Valid @RequestBody Employee e) {
    return service.update(id, e);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
