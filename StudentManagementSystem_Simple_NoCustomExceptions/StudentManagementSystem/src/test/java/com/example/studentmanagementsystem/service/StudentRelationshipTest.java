package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.StudentProfile;
import com.example.studentmanagementsystem.repository.StudentRepository;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentRelationshipTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void createStudentShouldKeepOneToOneProfile() {
        Student student = new Student(
                "Janhavi Ingale", "janhavi.profile@example.com", "Java"
        );
        StudentProfile profile = new StudentProfile("Ahmedabad", "9876543210");
        student.setProfile(profile);
        Department department = new Department("IT");
        department.setId(1L);
        student.setDepartment(department);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Student result = studentService.createStudent(student);

        assertNotNull(result.getProfile());
        assertEquals("Ahmedabad", result.getProfile().getAddress());
        assertEquals("9876543210", result.getProfile().getPhone());
        verify(studentRepository).save(student);
    }

    @Test
    void createStudentShouldRejectMissingProfile() {
        Student student = new Student("Amit", "amit.no.profile@example.com", "Spring Boot");
        Department department = new Department("IT");
        department.setId(1L);
        student.setDepartment(department);
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(student));
        verify(studentRepository, never()).save(any(Student.class));
    }
}
