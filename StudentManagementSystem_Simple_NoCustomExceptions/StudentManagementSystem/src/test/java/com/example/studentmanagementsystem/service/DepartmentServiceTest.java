package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void createDepartmentShouldKeepMultipleStudentsInOneDepartment() {
        Department department = new Department("Information Technology");
        Student student1 = new Student(
                "Rahul", "rahul.it@example.com", "Java"
        );
        Student student2 = new Student(
                "Priya", "priya.it@example.com", "Spring Boot"
        );

        department.addStudent(student1);
        department.addStudent(student2);

        when(departmentRepository.save(any(Department.class)))
                .thenAnswer(invocation -> {
                    Department saved = invocation.getArgument(0);
                    saved.setId(10L);
                    return saved;
                });

        Department result = departmentService.createDepartment(department);

        assertEquals(10L, result.getId());
        assertEquals("Information Technology", result.getDepartmentName());
        assertEquals(2, result.getStudents().size());
        assertSame(result, student1.getDepartment());
        assertSame(result, student2.getDepartment());
        verify(departmentRepository).save(department);
    }

    @Test
    void getAllDepartmentsShouldReturnRepositoryResults() {
        Department it = new Department("IT");
        Department hr = new Department("HR");
        when(departmentRepository.findAll()).thenReturn(List.of(it, hr));

        List<Department> result = departmentService.getAllDepartments();

        assertEquals(2, result.size());
        assertEquals("IT", result.get(0).getDepartmentName());
        verify(departmentRepository).findAll();
    }
}
