package com.education.course_service.repository;


import com.education.course_service.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacherUsername(String teacherUsername);
}
