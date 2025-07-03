package com.education.enrollment_service.client;

import com.education.enrollment_service.config.FeignClientConfig;
import com.education.enrollment_service.model.CourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "course-service", configuration = FeignClientConfig.class)
public interface CourseClient {
    @GetMapping("/courses")
    List<CourseDTO> getAllApprovedCourses();

    @GetMapping("/courses/{id}")
    CourseDTO getCourseById(@PathVariable("id") Long id);
}
