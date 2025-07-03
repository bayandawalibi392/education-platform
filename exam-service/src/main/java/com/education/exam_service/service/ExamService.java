package com.education.exam_service.service;

import com.education.exam_service.client.CourseClient;
import com.education.exam_service.client.EnrollmentClient;
import com.education.exam_service.exception.PaymentRequiredException;
import com.education.exam_service.model.*;
import com.education.exam_service.repository.ExamRepository;
import com.education.exam_service.repository.ExamResultRepository;
import com.education.exam_service.repository.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ExamResultRepository examResultRepository;
    private final CourseClient courseClient;
    private final EnrollmentClient enrollmentClient;

    public ExamService(ExamRepository examRepository, QuestionRepository questionRepository, ExamResultRepository examResultRepository, CourseClient courseClient, EnrollmentClient enrollmentClient) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.examResultRepository = examResultRepository;
        this.courseClient = courseClient;

        this.enrollmentClient = enrollmentClient;
    }

//    public Exam createExam(Exam exam, String username) {
//        CourseDTO course;
//
//        try {
//            // جلب الكورس من الخدمة
//            course = courseClient.getCourseById(exam.getCourseId());
//        } catch (Exception e) {
//            // في حال لم يتم العثور على الكورس أو فشل الاتصال
//            throw new PaymentRequiredException("هذا الكورس غير موجود");
//        }
//
//        // تحقق إذا المدرّب الحالي هو صاحب الكورس
//        if (!course.getTeacherUsername().equals(username)) {
//            throw new PaymentRequiredException("لا يمكنك إنشاء اختبار لدورة لا تملكها");
//        }
//        // إعداد حالة الاختبار
//        exam.setStatus("Active");
//        exam.setCreatedBy(username);
//
//        return examRepository.save(exam);
//    }
public Exam createExam(Exam exam, String username) {
    CourseDTO course;

    try {
        course = courseClient.getCourseById(exam.getCourseId());
    } catch (Exception e) {
        throw new PaymentRequiredException("هذا الكورس غير موجود");
    }

    if (!course.getTeacherUsername().equals(username)) {
        throw new PaymentRequiredException("لا يمكنك إنشاء اختبار لدورة لا تملكها");
    }

    if (exam.getTotalGrade() == null || exam.getTotalGrade() <= 0) {
        throw new PaymentRequiredException("يجب تحديد علامة كلية صحيحة للاختبار");
    }

    exam.setStatus("Active");
    exam.setCreatedBy(username);

    return examRepository.save(exam);
}



//public Question addQuestion(Long examId, Question question, String username) {
//    Exam exam = examRepository.findById(examId)
//            .orElseThrow(() -> new PaymentRequiredException("الاختبار غير موجود"));
//
//    if (!exam.getCreatedBy().equals(username)) {
//        throw new PaymentRequiredException("لا يمكنك إضافة سؤال لاختبار لم تقم بإنشائه.");
//    }
//
//    // مجموع العلامات الحالية
//    double currentTotal = questionRepository.findByExamId(examId)
//            .stream()
//            .mapToDouble(q -> q.getGrade() != null ? q.getGrade() : 0.0)
//            .sum();
//
//    double newTotal = currentTotal + question.getGrade();
//
//    if (newTotal > exam.getTotalGrade()) {
//        throw new PaymentRequiredException("مجموع علامات الأسئلة تجاوز علامة الاختبار الكلية (" + exam.getTotalGrade() + ")");
//    }
//
//    question.setExamId(examId);
//    return questionRepository.save(question);
//}
public List<Question> addQuestions(Long examId, List<Question> questions, String username) {
    Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new PaymentRequiredException("الاختبار غير موجود"));

    if (!exam.getCreatedBy().equals(username)) {
        throw new PaymentRequiredException("لا يمكنك إضافة سؤال لاختبار لم تقم بإنشائه.");
    }

    // مجموع العلامات الحالية
    double currentTotal = questionRepository.findByExamId(examId)
            .stream()
            .mapToDouble(q -> q.getGrade() != null ? q.getGrade() : 0.0)
            .sum();

    // حساب مجموع العلامات القادمة
    double addedTotal = questions.stream()
            .mapToDouble(q -> q.getGrade() != null ? q.getGrade() : 0.0)
            .sum();

    double newTotal = currentTotal + addedTotal;

    if (newTotal > exam.getTotalGrade()) {
        throw new PaymentRequiredException("مجموع علامات الأسئلة تجاوز علامة الاختبار الكلية (" + exam.getTotalGrade() + ")");
    }

    // إضافة examId للأسئلة وربطها بالاختبار
    for (Question q : questions) {
        q.setExamId(examId);
    }

    return questionRepository.saveAll(questions);
}

    public boolean checkStudentEnrollment(String username, Long courseId) {
        return enrollmentClient.isUserEnrolled(username, courseId);
    }
//    public List<QuestionViewDTO> getQuestionsForStudent(Long examId, String username) {
//        // تحقق من اشتراك الطالب
//        Exam exam = examRepository.findById(examId)
//                .orElseThrow(() -> new PaymentRequiredException("الاختبار غير موجود"));
//
//        boolean enrolled = enrollmentClient.isUserEnrolled(username, exam.getCourseId());
//        if (!enrolled) {
//            throw new PaymentRequiredException("يجب أن تكون مشتركًا في الدورة لرؤية الأسئلة" );
//        }
//
//        // استرجاع الأسئلة بدون correctAnswer
//        return questionRepository.findByExamId(examId)
//                .stream()
//                .map(q -> new QuestionViewDTO(q.getQuestionId(), q.getContent(), q.getType(), q.getOptions().toString()))
//                .collect(Collectors.toList());
//    }
public List<QuestionViewDTO> getQuestionsForStudent(Long examId, String username) {
    // جلب بيانات الاختبار
    Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new PaymentRequiredException("الاختبار غير موجود"));

    // جلب بيانات الدورة المرتبطة بالاختبار
    CourseDTO course = courseClient.getCourseById(exam.getCourseId());

    boolean isEnrolled = enrollmentClient.isUserEnrolled(username, course.getId());
    boolean isTeacher = course.getTeacherUsername().equals(username);

    // السماح فقط للمشترك أو المدرّب
    if (!isEnrolled && !isTeacher) {
        throw new PaymentRequiredException("أنت لست مشتركًا أو صاحب هذه الدورة");
    }

    // استرجاع الأسئلة بدون correctAnswer
    return questionRepository.findByExamId(examId)
            .stream()
            .map(q -> new QuestionViewDTO(
                    q.getQuestionId(),
                    q.getContent(),
                    q.getType(),
                    q.getOptions().toString()))
            .collect(Collectors.toList());
}

    public Exam getExamById(Long examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new PaymentRequiredException("الاختبار غير موجود"));
    }

    public List<Exam> getExamsByCourseId(Long courseId) {
        return examRepository.findByCourseId(courseId);
    }

    public List<Question> getQuestionsByExamId(Long examId) {
        return questionRepository.findByExamId(examId);
    }



//public ExamResult submitExamResult(ExamResult examResult) {
//    Long examId = examResult.getExamId();
//    String username = examResult.getUsername();
//
//    Exam exam = examRepository.findById(examId)
//            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "الاختبار غير موجود"));
//
//    List<Question> questions = questionRepository.findByExamId(examId);
//    List<StudentAnswer> studentAnswers = examResult.getAnswers();
//
//    double totalScore = 0;
//
//    for (Question question : questions) {
//        StudentAnswer answer = studentAnswers.stream()
//                .filter(a -> a.getQuestionId().equals(question.getQuestionId()))
//                .findFirst()
//                .orElse(null);
//
//        if (answer != null) {
//            // أضف الجواب الصحيح دائمًا
//            answer.setCorrectAnswer(question.getCorrectAnswer());
//
//            // تطابق الإجابة
//            if (question.getCorrectAnswer().trim().equalsIgnoreCase(answer.getAnswer().trim())) {
//                totalScore += question.getGrade();
//            }
//        }
//    }
//
//    examResult.setScore(totalScore);
//    double percentage = (totalScore / exam.getTotalGrade()) * 100;
//    examResult.setStatus(percentage >= 50.0 ? "Passed" : "Failed");
//
//    return examResultRepository.save(examResult);
//}
public ExamResult submitExamResult(ExamResult examResult) {
    Long examId = examResult.getExamId();
    String username = examResult.getUsername();

    // ✅ تحقق إن كان الطالب قد قدم هذا الاختبار من قبل
    Optional<ExamResult> existing = examResultRepository.findByExamIdAndUsername(examId, username);
    if (existing.isPresent()) {
        throw new PaymentRequiredException("لقد قمت بتقديم هذا الاختبار سابقًا");
    }

    Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new PaymentRequiredException("الاختبار غير موجود"));


    List<Question> questions = questionRepository.findByExamId(examId);
    List<StudentAnswer> studentAnswers = examResult.getAnswers();

    double totalScore = 0;

    for (Question question : questions) {
        StudentAnswer answer = studentAnswers.stream()
                .filter(a -> a.getQuestionId().equals(question.getQuestionId()))
                .findFirst()
                .orElse(null);

        if (answer != null) {
            answer.setCorrectAnswer(question.getCorrectAnswer());

            if (question.getCorrectAnswer().trim().equalsIgnoreCase(answer.getAnswer().trim())) {
                totalScore += question.getGrade();
            }
        }
    }

    examResult.setScore(totalScore);
    double percentage = (totalScore / exam.getTotalGrade()) * 100;
    examResult.setStatus(percentage >= 50.0 ? "Passed" : "Failed");

    return examResultRepository.save(examResult);
}

    public CourseDTO getCourseById(Long courseId) {
        try {
            return courseClient.getCourseById(courseId);
        } catch (Exception e) {
            throw new PaymentRequiredException("لم يتم العثور على الدورة");
        }
    }



    public ExamResult getResultForExamAndUser(Long examId, String username) {
        return examResultRepository.findByExamIdAndUsername(examId, username)
                .orElseThrow(() -> new PaymentRequiredException("لا يوجد نتيجة لهذا المستخدم"));
    }
    public String getExamStatusForUser(Long courseId, String username) {
        List<Exam> exams = examRepository.findByCourseId(courseId);

        if (exams.isEmpty()) {
            return "لم يتقدم للاختبار";
        }

        boolean allPassed = true;
        boolean attemptedAny = false;

        for (Exam exam : exams) {
            Optional<ExamResult> resultOpt = examResultRepository.findByExamIdAndUsername(exam.getExamId(), username);

            if (resultOpt.isEmpty()) {
                allPassed = false;
            } else {
                attemptedAny = true;
                if (!"Passed".equalsIgnoreCase(resultOpt.get().getStatus())) {
                    allPassed = false;
                }
            }
        }

        if (!attemptedAny) return "لم يتقدم للاختبار";
        if (allPassed) return "اجتاز جميع الاختبارات";
        return "لم يجتز جميع الاختبارات";
    }

}

