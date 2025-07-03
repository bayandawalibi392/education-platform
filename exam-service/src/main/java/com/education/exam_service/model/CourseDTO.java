package com.education.exam_service.model;

public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private double price;
    private String teacherUsername;

    // Constructors
    public CourseDTO() {
    }

    public CourseDTO(Long id, String title, String description, double price, String teacherUsername) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.teacherUsername = teacherUsername;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
    }
}
