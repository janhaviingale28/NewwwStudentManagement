package com.example.studentmanagementsystem.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_name", nullable = false, unique = true, length = 100)
    private String departmentName;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Student> students = new ArrayList<>();

    public Department() {}

    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        students.add(student);
        student.setDepartment(this);
    }

    public void removeStudent(Student student) {
        if (student != null && students.remove(student)) {
            student.setDepartment(null);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) {
        this.students = students == null ? new ArrayList<>() : students;
        for (Student student : this.students) {
            student.setDepartment(this);
        }
    }
}
