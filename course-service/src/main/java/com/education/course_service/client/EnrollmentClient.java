package com.education.course_service.client;

import com.education.course_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "enrollment-service", configuration = FeignClientConfig.class)
public interface EnrollmentClient {

    @GetMapping("/enrollments/check")
    boolean isUserEnrolled(@RequestParam String username, @RequestParam Long courseId);
    @GetMapping("/enrollments/users-by-course")
    List<String> getEnrolledUsersByCourseId(@RequestParam Long courseId);
}
