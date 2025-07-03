package com.education.exam_service.client;

import com.education.exam_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "enrollment-service", configuration = FeignClientConfig.class)
public interface EnrollmentClient {

    @GetMapping("/enrollments/check")
    boolean isUserEnrolled(@RequestParam String username, @RequestParam Long courseId);
}
