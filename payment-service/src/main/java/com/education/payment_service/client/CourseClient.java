package com.education.payment_service.client;

import com.education.payment_service.config.FeignClientConfig;
import com.education.payment_service.model.CourseDTO;
import com.education.payment_service.model.CourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", configuration = FeignClientConfig.class)
public interface CourseClient {

    @GetMapping("/courses/{id}")
    CourseDTO getCourseById(@PathVariable("id") Long id);
}
