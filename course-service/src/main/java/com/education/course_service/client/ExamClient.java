package com.education.course_service.client;

//import com.education.course_service.model.ExamDTO;
//import com.education.course_service.model.ExamResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "exam-service")
public interface ExamClient {
//    @GetMapping("/exams")
//    List<ExamDTO> getExamsByCourseId(@RequestParam Long courseId);
//
//    @GetMapping("/exams/{examId}/results")
//    Optional<ExamResultDTO> getExamResultForUser(@PathVariable Long examId, @RequestParam String username);
    @GetMapping("/exams/status")
    String getExamStatusForUser(@RequestParam Long courseId, @RequestParam String username);

}


