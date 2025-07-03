package com.education.enrollment_service.repository;

import com.education.enrollment_service.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUsername(String username);
    List<Enrollment> findByCourseId(Long courseId);
}
