//package com.education.course_service.service;
//
//
//import com.education.course_service.model.Course;
//import com.education.course_service.repository.CourseRepository;
//import org.springframework.stereotype.Service;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class CourseService {
//
//    private final CourseRepository repository;
//
//    public CourseService(CourseRepository repository) {
//        this.repository = repository;
//    }
//
//    public Course createCourse(Course course) {
//        course.setApproved(false); // كل دورة جديدة تحتاج موافقة
//        return repository.save(course);
//    }
//
//    public List<Course> getAllApprovedCourses() {
//        return repository.findAll().stream()
//                .filter(Course::isApproved)
//                .toList();
//    }
//
//    public Optional<Course> getCourseById(Long id) {
//        return repository.findById(id);
//    }
//
//    public Course approveCourse(Long id) {
//        Course course = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//        course.setApproved(true);
//        return repository.save(course);
//    }
//}
//
// CourseService.java
package com.education.course_service.service;

import com.education.course_service.client.EnrollmentClient;
import com.education.course_service.client.ExamClient;
import com.education.course_service.exception.PaymentRequiredException;
import com.education.course_service.model.Course;
import com.education.course_service.model.CourseWithStudentsDTO;
import com.education.course_service.model.StudentInCourseDTO;
import com.education.course_service.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository repository;
    private final EnrollmentClient enrollmentClient;
    private final ExamClient examClient;

    public CourseService(CourseRepository repository, EnrollmentClient enrollmentClient, ExamClient examClient) {
        this.repository = repository;
        this.enrollmentClient = enrollmentClient;
        this.examClient = examClient;
    }

    public Course createCourse(Course course) {
        course.setApproved(false); // كل دورة جديدة تحتاج موافقة
        return repository.save(course);
    }

    public List<Course> getAllApprovedCourses() {
        return repository.findAll().stream()
                .filter(Course::isApproved)
                .toList();
    }

    public Optional<Course> getCourseById(Long id) {
        return repository.findById(id);
    }

    public Course approveCourse(Long id) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new PaymentRequiredException("Course not found"));
        course.setApproved(true);
        return repository.save(course);
    }

    public Course save(Course course) {
        return repository.save(course);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    public List<CourseWithStudentsDTO> getCoursesWithStudents(String teacherUsername) {
        List<Course> courses = repository.findByTeacherUsername(teacherUsername);

        return courses.stream().map(course -> {
            List<String> studentUsernames = enrollmentClient.getEnrolledUsersByCourseId(course.getId());

            List<StudentInCourseDTO> students = studentUsernames.stream().map(username -> {
                String status = examClient.getExamStatusForUser(course.getId(), username);
                StudentInCourseDTO s = new StudentInCourseDTO();
                s.setUsername(username);
                s.setExamStatus(status);
                return s;
            }).collect(Collectors.toList());

            CourseWithStudentsDTO dto = new CourseWithStudentsDTO();
            dto.setCourseId(course.getId());
            dto.setTitle(course.getTitle());
            dto.setDescription(course.getDescription());
            dto.setStudents(students);
            return dto;
        }).collect(Collectors.toList());
    }
}

