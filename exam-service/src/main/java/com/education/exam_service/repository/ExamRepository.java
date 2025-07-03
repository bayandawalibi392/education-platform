package com.education.exam_service.repository;

import com.education.exam_service.model.Exam;
import com.education.exam_service.model.ExamResult;
import com.education.exam_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCourseId(Long courseId);
}


