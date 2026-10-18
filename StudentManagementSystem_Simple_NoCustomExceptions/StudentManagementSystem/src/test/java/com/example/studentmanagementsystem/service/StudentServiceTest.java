package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.entity.StudentProfile;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import com.example.studentmanagementsystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock private StudentRepository studentRepository;
    @Mock private DepartmentRepository departmentRepository;
    @InjectMocks private StudentService studentService;
    private Student student;

    @BeforeEach
    void setUp() { student = new Student("Janhavi Ingale", "janhavi@example.com", "Information Technology"); student.setProfile(new StudentProfile("Ahmedabad", "9876543210")); Department d = new Department("IT"); d.setId(1L); student.setDepartment(d); }

    @Test
    void createStudentShouldSaveAndReturnStudent() {
        Department department = new Department("IT");
        department.setId(1L);
        student.setDepartment(department);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student saved = invocation.getArgument(0); saved.setId(1L); return saved;
        });
        Student result = studentService.createStudent(student);
        assertEquals(1L, result.getId());
        assertEquals("Janhavi Ingale", result.getName());
        verify(studentRepository).save(student);
        assertSame(department, result.getDepartment());
    }

    @Test
    void getAllStudentsShouldReturnRepositoryResults() {
        when(studentRepository.findAll()).thenReturn(List.of(student));
        assertEquals(1, studentService.getAllStudents().size());
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentByIdShouldReturnStudentWhenFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        assertSame(student, studentService.getStudentById(1L));
    }

    @Test
    void getStudentByIdShouldThrowWhenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(java.util.NoSuchElementException.class, () -> studentService.getStudentById(99L));
    }

    @Test
    void updateStudentShouldUpdateFieldsAndSave() {
        student.setId(1L);
        Student changes = new Student("Janhavi S. Ingale", "janhavi2@example.com", "Java Full Stack");
        changes.setProfile(new StudentProfile("Pune", "9876543211"));
        Department department = new Department("IT"); department.setId(1L); changes.setDepartment(department);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Student result = studentService.updateStudent(1L, changes);
        assertEquals("Janhavi S. Ingale", result.getName());
        assertEquals("janhavi2@example.com", result.getEmail());
        assertEquals("Java Full Stack", result.getCourse());
        verify(studentRepository).save(student);
    }

    @Test
    void deleteStudentShouldDeleteExistingStudent() {
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        studentService.deleteStudent(1L);
        verify(studentRepository).delete(student);
    }
}
