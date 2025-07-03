//package com.education.enrollment_service.service;
//
//import com.education.enrollment_service.model.Enrollment;
//import com.education.enrollment_service.client.CourseClient;
//import com.education.enrollment_service.model.CourseDTO;
//import com.education.enrollment_service.model.Enrollment;
//import com.education.enrollment_service.repository.EnrollmentRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class EnrollmentService {
//
//    private final EnrollmentRepository repository;
//    private final CourseClient courseClient;
//    public EnrollmentService(EnrollmentRepository repository, CourseClient courseClient) {
//        this.repository = repository;
//        this.courseClient = courseClient;
//    }
//    public List<CourseDTO> getUserEnrolledCourseDetails(String username) {
//        List<Enrollment> enrollments = repository.findByUsername(username);
//        return enrollments.stream()
//                .map(e -> courseClient.getCourseById(e.getCourseId()))
//                .collect(Collectors.toList());
//    }
//    public Enrollment enroll(String username, Long courseId) {
//        Enrollment enrollment = new Enrollment(username, courseId);
//        return repository.save(enrollment);
//    }
//
//    public List<Enrollment> getUserEnrollments(String username) {
//        return repository.findByUsername(username);
//    }
//}
//////////////////////////////////////////////////////////
//package com.education.enrollment_service.service;
//
//import com.education.enrollment_service.client.CourseClient;
//import com.education.enrollment_service.client.PaymentClient;
//import com.education.enrollment_service.model.CourseDTO;
//import com.education.enrollment_service.model.Enrollment;
//import com.education.enrollment_service.repository.EnrollmentRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class EnrollmentService {
//
//    private final EnrollmentRepository repository;
//    private final CourseClient courseClient;
//    private final PaymentClient paymentClient;
//
//    public EnrollmentService(EnrollmentRepository repository,
//                             CourseClient courseClient,
//                             PaymentClient paymentClient) {
//        this.repository = repository;
//        this.courseClient = courseClient;
//        this.paymentClient = paymentClient;
//    }
//
//    public List<CourseDTO> getUserEnrolledCourseDetails(String username) {
//        List<Enrollment> enrollments = repository.findByUsername(username);
//        return enrollments.stream()
//                .map(e -> courseClient.getCourseById(e.getCourseId()))
//                .collect(Collectors.toList());
//    }
//
//    public Enrollment enroll(String username, Long courseId) {
//        // تحقق إذا المستخدم دفع للكورس قبل الاشتراك
//        boolean hasPaid = paymentClient.hasPaidForCourse(username, courseId);
//
//        if (!hasPaid) {
//            throw new RuntimeException("يجب دفع قيمة الدورة قبل الاشتراك.");
//        }
//
//        // تحقق إذا المستخدم مشترك مسبقًا بالدورة
//        boolean alreadyEnrolled = repository.findByUsername(username)
//                .stream()
//                .anyMatch(e -> e.getCourseId().equals(courseId));
//
//        if (alreadyEnrolled) {
//            throw new RuntimeException("أنت مشترك بالفعل في هذه الدورة.");
//        }
//
//        Enrollment enrollment = new Enrollment(username, courseId);
//        return repository.save(enrollment);
//    }
//
//    public List<Enrollment> getUserEnrollments(String username) {
//        return repository.findByUsername(username);
//    }
//}
package com.education.enrollment_service.service;

import com.education.enrollment_service.client.CourseClient;
import com.education.enrollment_service.client.ExamClient;
import com.education.enrollment_service.client.PaymentClient;
import com.education.enrollment_service.exception.PaymentRequiredException;
import com.education.enrollment_service.model.*;
import com.education.enrollment_service.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    @Autowired
    private ExamClient examClient;
    private final EnrollmentRepository repository;
    private final CourseClient courseClient;

    private final PaymentClient paymentClient;


    public EnrollmentService(ExamClient examClient, EnrollmentRepository repository,
                             CourseClient courseClient,
                             PaymentClient paymentClient) {
        this.examClient = examClient;
        this.repository = repository;
        this.courseClient = courseClient;

        this.paymentClient = paymentClient;
    }

//    public List<CourseDTO> getUserEnrolledCourseDetails(String username) {
//        List<Enrollment> enrollments = repository.findByUsername(username);
//        return enrollments.stream()
//                .map(e -> courseClient.getCourseById(e.getCourseId()))
//                .collect(Collectors.toList());
//    }
public List<CourseWithExamStatusDTO> getUserEnrolledCourseDetails(String username) {
    List<Enrollment> enrollments = repository.findByUsername(username);

    return enrollments.stream().map(enrollment -> {
        CourseDTO course = courseClient.getCourseById(enrollment.getCourseId());

        String examStatus = getExamStatusForUser(course.getId(), username);

        CourseWithExamStatusDTO dto = new CourseWithExamStatusDTO();
        dto.setCourseId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setTeacherUsername(course.getTeacherUsername());
        dto.setExamStatus(examStatus);
        return dto;
    }).collect(Collectors.toList());
}

    private String getExamStatusForUser(Long courseId, String username) {
        List<ExamDTO> exams = examClient.getExamsByCourseId(courseId);

        if (exams.isEmpty()) {
            return "لم يتقدم للاختبار";
        }

        boolean allPassed = true;
        boolean attemptedAny = false;

        for (ExamDTO exam : exams) {
            try {
                Optional<ExamResultDTO> resultOpt = examClient.getExamResultForUser(exam.getExamId(), username);
                if (resultOpt.isEmpty()) {
                    allPassed = false;
                } else {
                    attemptedAny = true;
                    if (!resultOpt.get().isPassed()) {
                        allPassed = false;
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error fetching result for examId=" + exam.getExamId() + ", user=" + username);
                e.printStackTrace();
                allPassed = false;
            }
        }

        if (!attemptedAny) return "لم يتقدم للاختبار";
        if (allPassed) return "اجتاز جميع الاختبارات";
        return "لم يجتز جميع الاختبارات";
    }


    public Enrollment enroll(String username, Long courseId) {
        boolean hasPaid = paymentClient.hasPaidForCourse(username, courseId);
        if (!hasPaid) {
            throw new PaymentRequiredException("يرجى دفع قيمة الدورة قبل الاشتراك.");
        }


        boolean alreadyEnrolled = repository.findByUsername(username)
                .stream()
                .anyMatch(e -> e.getCourseId().equals(courseId));

        if (alreadyEnrolled) {
            throw new PaymentRequiredException("أنت مشترك بالفعل في هذه الدورة.");
        }

        Enrollment enrollment = new Enrollment(username, courseId);
        return repository.save(enrollment);
    }
    public boolean isUserEnrolled(String username, Long courseId) {
        return repository.findByUsername(username).stream()
                .anyMatch(e -> e.getCourseId().equals(courseId));
    }
//    public List<CourseWithExamStatusDTO> getUserEnrolledCourseDetails(String username) {
//        List<Enrollment> enrollments = repository.findByUsername(username);
//        return enrollments.stream().map(enrollment -> {
//            CourseDTO course = courseClient.getCourseById(enrollment.getCourseId());
//            String examStatus = examClient.getExamStatus(course.getId(), username);
//
//            CourseWithExamStatusDTO dto = new CourseWithExamStatusDTO();
//            dto.setCourseId(course.getId());
//            dto.setTitle(course.getTitle());
//            dto.setDescription(course.getDescription());
//            dto.setTeacherUsername(course.getTeacherUsername());
//            dto.setExamStatus(examStatus);
//            return dto;
//        }).collect(Collectors.toList());
//    }

    public List<Enrollment> getUserEnrollments(String username) {
        return repository.findByUsername(username);
    }
}
