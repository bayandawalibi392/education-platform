package com.education.enrollment_service.model;

public class ExamResultDTO {
    private Long resultId;
    private Long examId;
    private String username;
    private double score;
    private String status; // "Passed" أو "Failed"

    // دالة مساعدة لتحديد هل الطالب اجتاز الامتحان أم لا
    public boolean isPassed() {
        return "Passed".equalsIgnoreCase(status);
    }

    // Getters and Setters
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
}
