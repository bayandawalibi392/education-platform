//package com.education.course_service.controller;
//
//import com.education.course_service.model.Course;
//import com.education.course_service.service.CourseService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//@RestController
//@RequestMapping("/courses")
//public class CourseController {
//
//    private final CourseService service;
//
//    public CourseController(CourseService service) {
//        this.service = service;
//    }
//
//    @PostMapping("/create")
//    @PreAuthorize("hasRole('TEACHER')")
//    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
//        Course created = service.createCourse(course);
//        return ResponseEntity.ok(created);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Course>> getApprovedCourses() {
//        List<Course> courses = service.getAllApprovedCourses();
//        return ResponseEntity.ok(courses);
//    }
//
//    @PutMapping("/approve/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Course> approveCourse(@PathVariable Long id) {
//        Course approved = service.approveCourse(id);
//        return ResponseEntity.ok(approved);
//    }
//}
// CourseController.java
package com.education.course_service.controller;

import com.education.course_service.client.EnrollmentClient;
import com.education.course_service.exception.PaymentRequiredException;
import com.education.course_service.model.Course;
import com.education.course_service.model.CourseWithStudentsDTO;
import com.education.course_service.model.Lesson;
import com.education.course_service.service.CourseService;
import com.education.course_service.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService service;
    @Autowired
    private EnrollmentClient enrollmentClient;
    private final LessonService lessonService;
    public CourseController(CourseService service, LessonService lessonService) {
        this.service = service;
        this.lessonService = lessonService;
    }

//    @PostMapping("/create")
//    @PreAuthorize("hasRole('TEACHER')")
//    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
//        Course created = service.createCourse(course);
//        return ResponseEntity.ok(created);
//    }
@PostMapping("/create")
@PreAuthorize("hasRole('TEACHER')")
public ResponseEntity<Course> createCourse(@RequestBody Course course, Authentication auth) {
    String loggedInTeacher = auth.getName();

    // تأكد أن المستخدم لا يرسل اسم معلم مختلف
    if (course.getTeacherUsername() != null &&
            !course.getTeacherUsername().equals(loggedInTeacher)) {
        return ResponseEntity.badRequest().body(null); // أو يمكنك إرسال رسالة توضح الخطأ
    }

    // إجبار تعيين اسم المعلم الحالي
    course.setTeacherUsername(loggedInTeacher);

    Course created = service.createCourse(course);
    return ResponseEntity.ok(created);
}


    @GetMapping
    public ResponseEntity<List<Course>> getApprovedCourses() {
        List<Course> courses = service.getAllApprovedCourses();
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> approveCourse(@PathVariable Long id) {
        Course approved = service.approveCourse(id);
        return ResponseEntity.ok(approved);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateCourse(@PathVariable Long id,
                                          @RequestBody Course updatedCourse,
                                          Authentication auth) {
        String username = auth.getName();
        Course existing = service.getCourseById(id)
                .orElseThrow(() -> new PaymentRequiredException("Course not found"));

        if (!existing.getTeacherUsername().equals(username)) {
            return ResponseEntity.status(403).body("You can only edit your own courses.");
        }

        existing.setTitle(updatedCourse.getTitle());
        existing.setDescription(updatedCourse.getDescription());
        existing.setPrice(updatedCourse.getPrice());
        return ResponseEntity.ok(service.save(existing));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id, Authentication auth) {
        String username = auth.getName();
        Course existing = service.getCourseById(id)
                .orElseThrow(() -> new PaymentRequiredException("Course not found"));

        if (!existing.getTeacherUsername().equals(username)) {
            return ResponseEntity.status(403).body("You can only delete your own courses.");
        }

        service.deleteById(id);
        return ResponseEntity.ok("Course deleted successfully.");
    }
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = service.getCourseById(id)
                .orElseThrow(() -> new PaymentRequiredException("Course not found"));
        return ResponseEntity.ok(course);
    }
    @PostMapping("/{courseId}/add_lessons")
    public ResponseEntity<Lesson> addLessonToCourse(
            @PathVariable Long courseId,
            @RequestBody Lesson lesson,
            Authentication auth) {

        String teacher = auth.getName();
        Course course = service.getCourseById(courseId)
                .orElseThrow(() -> new PaymentRequiredException("Course not found"));


        if (!course.getTeacherUsername().equals(teacher)) {
            return ResponseEntity.status(403).build(); // Not authorized
        }

        lesson.setCourse(course);
        Lesson saved = lessonService.save(lesson);
        return ResponseEntity.ok(saved);
    }

    // 📌 عرض دروس الكورس
//    @GetMapping("/{courseId}/lessons")
//    public ResponseEntity<List<Lesson>> getLessons(@PathVariable Long courseId) {
//        return ResponseEntity.ok(lessonService.getLessonsByCourseId(courseId));
//    }
    @GetMapping("/{courseId}/lessons")
    public ResponseEntity<?> getLessons(@PathVariable Long courseId, Authentication auth) {
        String username = auth.getName();

        Course course = service.getCourseById(courseId)
                .orElseThrow(() -> new PaymentRequiredException("الدورة غير موجودة"));

        boolean isOwner = course.getTeacherUsername().equals(username);
        boolean isEnrolled = enrollmentClient.isUserEnrolled(username, courseId);

        if (!isOwner && !isEnrolled) {
            throw new PaymentRequiredException("غير مصرح لك بمشاهدة دروس هذه الدورة");
        }

        List<Lesson> lessons = lessonService.getLessonsByCourseId(courseId);
        return ResponseEntity.ok(lessons);
    }
    @GetMapping("/my-courses-with-students")
    public ResponseEntity<List<CourseWithStudentsDTO>> getCoursesWithStudents(Authentication auth) {
        String teacherUsername = auth.getName();
        return ResponseEntity.ok(service.getCoursesWithStudents(teacherUsername));
    }
}

