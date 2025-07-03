package com.education.enrollment_service.client;

import com.education.enrollment_service.model.ExamDTO;
import com.education.enrollment_service.model.ExamResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "exam-service")
public interface ExamClient {
    @GetMapping("/exams")
    List<ExamDTO> getExamsByCourseId(@RequestParam Long courseId);

    @GetMapping("/exams/{examId}/results")
    Optional<ExamResultDTO> getExamResultForUser(@PathVariable Long examId, @RequestParam String username);
}


