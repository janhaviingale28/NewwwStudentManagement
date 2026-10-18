package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.StudentProfile;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import com.example.studentmanagementsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    public StudentService(StudentRepository studentRepository, DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository; this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Student createStudent(Student student) {
        validateStudent(student);
        if (studentRepository.findByEmail(student.getEmail().trim()).isPresent()) throw new IllegalArgumentException("Email already exists: " + student.getEmail());
        student.setId(null);
        Department department = findDepartment(student);
        student.setDepartment(department);
        if (student.getProfile().getId() != null) student.getProfile().setId(null);
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true) public List<Student> getAllStudents() { return studentRepository.findAll(); }
    @Transactional(readOnly = true) public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Student updateStudent(Long id, Student input) {
        validateStudent(input);
        Student existing = getStudentById(id);
        if (studentRepository.findByEmail(input.getEmail()).filter(s -> !s.getId().equals(id)).isPresent()) {
            throw new IllegalArgumentException("Email is already used by another student: " + input.getEmail());
        }
        existing.setName(input.getName().trim()); existing.setEmail(input.getEmail().trim()); existing.setCourse(input.getCourse().trim());
        existing.setDepartment(findDepartment(input));
        StudentProfile incoming = input.getProfile();
        StudentProfile profile = existing.getProfile();
        if (profile == null) { profile = new StudentProfile(); existing.setProfile(profile); }
        profile.setAddress(incoming.getAddress().trim()); profile.setPhone(incoming.getPhone().trim());
        return studentRepository.save(existing);
    }

    @Transactional public void deleteStudent(Long id) { studentRepository.delete(getStudentById(id)); }

    private Department findDepartment(Student student) {
        if (student.getDepartment() == null || student.getDepartment().getId() == null) {
            throw new IllegalArgumentException("department.id is required. Use GET /api/departments and send a valid id.");
        }
        Long departmentId = student.getDepartment().getId();
        return departmentRepository.findById(departmentId).orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + departmentId));
    }

    private void validateStudent(Student s) {
        if (s == null) throw new IllegalArgumentException("Student request body is required.");
        if (blank(s.getName())) throw new IllegalArgumentException("name is required.");
        if (blank(s.getEmail()) || !s.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) throw new IllegalArgumentException("A valid email is required.");
        if (blank(s.getCourse())) throw new IllegalArgumentException("course is required.");
        if (s.getProfile() == null) throw new IllegalArgumentException("profile is required and must include address and phone.");
        if (blank(s.getProfile().getAddress())) throw new IllegalArgumentException("profile.address is required.");
        if (blank(s.getProfile().getPhone()) || !s.getProfile().getPhone().matches("^[0-9+() -]{7,20}$")) throw new IllegalArgumentException("A valid profile.phone is required.");
    }
    private boolean blank(String value) { return value == null || value.trim().isEmpty(); }
}
