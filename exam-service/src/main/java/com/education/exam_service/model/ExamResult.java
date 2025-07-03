//package com.education.exam_service.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
//import java.util.List;
//
//@Entity
//public class ExamResult {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long resultId;
//
//    private Long examId;
//    private String username; // المتعلم
//    private double score;
//    private String status; // "Pending", "Passed", "Failed"
//
//    private List<StudentAnswer> answers;
//
//    public ExamResult() {
//    }
//
//    public ExamResult(Long examId, String username, double score, String status) {
//        this.examId = examId;
//        this.username = username;
//        this.score = score;
//        this.status = status;
//    }
//
//    public Long getResultId() {
//        return resultId;
//    }
//
//    public void setResultId(Long resultId) {
//        this.resultId = resultId;
//    }
//
//    public Long getExamId() {
//        return examId;
//    }
//
//    public void setExamId(Long examId) {
//        this.examId = examId;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public double getScore() {
//        return score;
//    }
//
//    public void setScore(double score) {
//        this.score = score;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//    public List<StudentAnswer> getAnswers() {
//        return answers;
//    }
//
//    public void setAnswers(List<StudentAnswer> answers) {
//        this.answers = answers;
//    }
//}
package com.education.exam_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.util.List;

@Entity
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    private Long examId;
    private String username; // المتعلم
    private double score;
    private String status; // "Pending", "Passed", "Failed"

    @Transient  // ⬅️ هذا مهم حتى لا يحاول Hibernate حفظ الحقل
    private List<StudentAnswer> answers;

    public ExamResult() {
    }

    public ExamResult(Long examId, String username, double score, String status) {
        this.examId = examId;
        this.username = username;
        this.score = score;
        this.status = status;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StudentAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<StudentAnswer> answers) {
        this.answers = answers;
    }
}
