package com.education.exam_service.controller;

import com.education.exam_service.exception.PaymentRequiredException;
import com.education.exam_service.model.*;
import com.education.exam_service.repository.ExamRepository;
import com.education.exam_service.repository.ExamResultRepository;
import com.education.exam_service.service.ExamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;
    private final ObjectMapper objectMapper;
    private final ExamResultRepository examResultRepository;
    private final ExamRepository examRepository;
    public ExamController(ExamService examService, ObjectMapper objectMapper, ExamResultRepository examResultRepository, ExamRepository examRepository) {
        this.examService = examService;
        this.objectMapper = objectMapper;
        this.examResultRepository = examResultRepository;
        this.examRepository = examRepository;
    }

    // المدرب ينشئ اختبار
    @PostMapping("/create")
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam, Authentication auth) {
        String username = auth.getName();
        Exam savedExam = examService.createExam(exam, username);
        return ResponseEntity.ok(savedExam);
    }


    // إضافة سؤال لاختبار
//    @PostMapping("/{examId}/add-question")
//    public ResponseEntity<Question> addQuestion(@PathVariable Long examId,
//                                                @RequestBody Question question,
//                                                Authentication auth) {
//        String username = auth.getName(); // جلب اسم المستخدم من التوكن
//        Question savedQuestion = examService.addQuestion(examId, question, username);
//        return ResponseEntity.ok(savedQuestion);
//    }
//
    // عرض اختبارات دورة معينة
    @GetMapping
    public ResponseEntity<Object> getExamsByCourse(@RequestParam Long courseId) {
        return ResponseEntity.ok(examService.getExamsByCourseId(courseId));
    }
    @PostMapping("/{examId}/add-questions")
    public ResponseEntity<List<Question>> addQuestions(@PathVariable Long examId,
                                                       @RequestBody List<Question> questions,
                                                       Authentication auth) {
        String username = auth.getName(); // من التوكن
        List<Question> savedQuestions = examService.addQuestions(examId, questions, username);
        return ResponseEntity.ok(savedQuestions);
    }


    // عرض أسئلة اختبار
//    @GetMapping("/{examId}/questions")
//    public ResponseEntity<Object> getQuestions(@PathVariable Long examId, Authentication auth) {
//        String username = auth.getName();
//
//        // جلب الاختبار
//        Exam exam = examService.getExamById(examId);
//
//        // تحقق من الاشتراك
//        boolean isEnrolled = examService.checkStudentEnrollment(username, exam.getCourseId());
//
//        if (!isEnrolled) {
//            throw new PaymentRequiredException("أنت لست مشتركًا في هذه الدورة");
//        }
//
//        return ResponseEntity.ok(examService.getQuestionsForStudent(examId, username));
//    }
//
    @GetMapping("/{examId}/questions")
    public ResponseEntity<Object> getQuestions(@PathVariable Long examId, Authentication auth) {
        String username = auth.getName();

        // جلب بيانات الاختبار
        Exam exam = examService.getExamById(examId);

        // جلب بيانات الكورس المرتبط بالاختبار
        CourseDTO course = examService.getCourseById(exam.getCourseId());

        // تحقق: إذا الطالب مشترك في الدورة أو المدرّس هو صاحب الدورة
        boolean isEnrolledStudent = examService.checkStudentEnrollment(username, course.getId());
        boolean isCourseTeacher = course.getTeacherUsername().equals(username);

        if (!isEnrolledStudent && !isCourseTeacher) {
            throw new PaymentRequiredException("غير مصرح لك بعرض أسئلة هذا الاختبار");
        }

        return ResponseEntity.ok(examService.getQuestionsForStudent(examId, username));
    }


//     تقديم نتيجة اختبار
    @PostMapping("/{examId}/submit")
    public ResponseEntity<Object> submitExam(@PathVariable Long examId,
                                             @RequestBody ExamResult examResult,
                                             Authentication auth) {
        examResult.setExamId(examId);
        examResult.setUsername(auth.getName());
        ExamResult savedResult = examService.submitExamResult(examResult);
        return ResponseEntity.ok(savedResult);
    }
    // عرض نتائج المتعلم
    @GetMapping("/{examId}/results")
    public ResponseEntity<Object> getResultForUserAndExam(@PathVariable Long examId,
                                                          @RequestParam String username) {
        return ResponseEntity.ok(
                examService.getResultForExamAndUser(examId, username)
        );
    }
    @GetMapping("/status")
    public ResponseEntity<String> getExamStatus(@RequestParam Long courseId, @RequestParam String username) {
        String status = examService.getExamStatusForUser(courseId, username);
        return ResponseEntity.ok(status);
    }

}

