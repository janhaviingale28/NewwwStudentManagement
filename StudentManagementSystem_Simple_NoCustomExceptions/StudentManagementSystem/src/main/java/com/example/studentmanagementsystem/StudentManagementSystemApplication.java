package com.example.studentmanagementsystem;

import com.example.studentmanagementsystem.entity.Department;
import com.example.studentmanagementsystem.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StudentManagementSystemApplication {
    public static void main(String[] args) { SpringApplication.run(StudentManagementSystemApplication.class, args); }

    // Seed departments only when the table is empty; existing data is preserved.
    @Bean
    CommandLineRunner seedDepartments(DepartmentRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Department("Information Technology"));
                repository.save(new Department("Computer Science"));
                repository.save(new Department("Electronics"));
                repository.save(new Department("Mechanical"));
            }
        };
    }
}
