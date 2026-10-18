package com.example.studentmanagementsystem.repository;

import com.example.studentmanagementsystem.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
}
