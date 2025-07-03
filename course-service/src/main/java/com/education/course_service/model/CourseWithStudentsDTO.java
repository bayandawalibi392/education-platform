package com.education.course_service.model;

import java.util.List;

public class CourseWithStudentsDTO {
    private Long courseId;
    private String title;
    private String description;
    private List<StudentInCourseDTO> students;

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<StudentInCourseDTO> getStudents() { return students; }
    public void setStudents(List<StudentInCourseDTO> students) { this.students = students; }
}
