//package com.education.exam_service.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
//@Entity
//public class Question {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long questionId;
//
//    private Long examId;
//    private String content;
//    private String type; // مثلاً "MULTIPLE_CHOICE", "TRUE_FALSE"
//    private String options; // JSON أو نص للفروض (في حالة اختيار من متعدد)
//    private String correctAnswer;
//
//    public Question() {
//    }
//
//    public Question(Long examId, String content, String type, String options, String correctAnswer) {
//        this.examId = examId;
//        this.content = content;
//        this.type = type;
//        this.options = options;
//        this.correctAnswer = correctAnswer;
//    }
//
//    public Long getQuestionId() {
//        return questionId;
//    }
//
//    public void setQuestionId(Long questionId) {
//        this.questionId = questionId;
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
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getOptions() {
//        return options;
//    }
//
//    public void setOptions(String options) {
//        this.options = options;
//    }
//
//    public String getCorrectAnswer() {
//        return correctAnswer;
//    }
//
//    public void setCorrectAnswer(String correctAnswer) {
//        this.correctAnswer = correctAnswer;
//    }
//}
//
package com.education.exam_service.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    private Double grade;
    private double score;
    private Long examId;
    private String content;
    private String type; // "MULTIPLE_CHOICE", "TRUE_FALSE" وغيرها

    @ElementCollection
    private List<String> options;  // تغيير من String إلى List<String>

    private String correctAnswer;

    public Question() {
    }

    public Question(Long examId, String content, String type, List<String> options, String correctAnswer) {
        this.examId = examId;
        this.content = content;
        this.type = type;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
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

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

}
