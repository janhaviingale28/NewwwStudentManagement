package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository repository;
    public DepartmentService(DepartmentRepository repository) { this.repository = repository; }
    public Department createDepartment(Department d) { validate(d); d.setId(null); return repository.save(d); }
    public List<Department> getAllDepartments() { return repository.findAll(); }
    public Department getDepartmentById(Long id) { return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + id)); }
    public Department updateDepartment(Long id, Department input) { validate(input); Department d = getDepartmentById(id); d.setDepartmentName(input.getDepartmentName().trim()); return repository.save(d); }
    @Transactional public void deleteDepartment(Long id) { Department d = getDepartmentById(id); if (!d.getStudents().isEmpty()) throw new IllegalArgumentException("Cannot delete a department that has students. Reassign or delete students first."); repository.delete(d); }
    private void validate(Department d) { if (d == null || d.getDepartmentName() == null || d.getDepartmentName().trim().isEmpty()) throw new IllegalArgumentException("departmentName is required."); }
}
