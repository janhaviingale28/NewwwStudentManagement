package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService service;
    public DepartmentController(DepartmentService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public Department create(@RequestBody Department d) { return service.createDepartment(d); }
    @GetMapping public List<Department> all() { return service.getAllDepartments(); }
    @GetMapping("/{id}") public Department one(@PathVariable Long id) { return service.getDepartmentById(id); }
    @PutMapping("/{id}") public Department update(@PathVariable Long id, @RequestBody Department d) { return service.updateDepartment(id, d); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteDepartment(id); }
}
