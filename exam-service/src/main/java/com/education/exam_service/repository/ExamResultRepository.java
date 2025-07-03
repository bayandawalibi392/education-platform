package com.education.exam_service.repository;

import com.education.exam_service.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByUsername(String username);
    List<ExamResult> findByExamId(Long examId);
    Optional<ExamResult> findByExamIdAndUsername(Long examId, String username);
}