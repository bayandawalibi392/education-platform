    package com.education.enrollment_service.controller;
    
    import com.education.enrollment_service.client.CourseClient;
    import com.education.enrollment_service.model.CourseDTO;
    import com.education.enrollment_service.model.CourseWithExamStatusDTO;
    import com.education.enrollment_service.model.Enrollment;
    import com.education.enrollment_service.repository.EnrollmentRepository;
    import com.education.enrollment_service.service.EnrollmentService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;
    
    import java.util.List;
    import java.util.stream.Collectors;
    
    @RestController
    @RequestMapping("/enrollments")
    public class EnrollmentController {
    
        private final EnrollmentService service;
        private final EnrollmentRepository repository;
        private final CourseClient courseClient;
        public EnrollmentController(EnrollmentService service, EnrollmentRepository repository, CourseClient courseClient) {
            this.service = service;
            this.repository = repository;
            this.courseClient = courseClient;
        }
    
        // الاشتراك في دورة
        @PostMapping("/enroll/{courseId}")
        public ResponseEntity<?> enroll(@PathVariable Long courseId, Authentication auth) {
            String username = auth.getName();
            Enrollment enrollment = service.enroll(username, courseId);
            System.out.println(">> User trying to enroll: " + username + ", courseId = " + courseId);
            return ResponseEntity.ok(enrollment);
    
        }

    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseWithExamStatusDTO>> myCourses(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(service.getUserEnrolledCourseDetails(username));
    }

        @GetMapping("/check")
        public boolean isUserEnrolled(@RequestParam String username, @RequestParam Long courseId) {
            return service.isUserEnrolled(username, courseId);
        }
        @GetMapping("/users-by-course")
        public List<String> getEnrolledUsers(@RequestParam Long courseId) {
            return repository.findByCourseId(courseId)
                    .stream()
                    .map(Enrollment::getUsername)
                    .collect(Collectors.toList());
        }
        @GetMapping("/available-courses")
        public ResponseEntity<List<CourseDTO>> getAvailableCourses() {
            return ResponseEntity.ok(courseClient.getAllApprovedCourses());
        }
    
    }
