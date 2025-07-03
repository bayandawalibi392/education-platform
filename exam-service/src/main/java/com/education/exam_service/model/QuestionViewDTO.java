package com.education.exam_service.model;

public class QuestionViewDTO {
    private Long questionId;
    private String content;
    private String type;
    private String options;
    private Double grade;
    public QuestionViewDTO() {}

    public QuestionViewDTO(Long questionId, String content, String type, String options) {
        this.questionId = questionId;
        this.content = content;
        this.type = type;
        this.options = options;
    }

    // Getters and setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
