//package com.education.payment_service.service;
//
//import com.education.payment_service.model.Payment;
//import com.education.payment_service.repository.PaymentRepository;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class PaymentService {
//
//    private final PaymentRepository repository;
//
//    public PaymentService(PaymentRepository repository) {
//        this.repository = repository;
//    }
//
//    public Payment processPayment(String username, Long courseId, double amount) {
//        // ⚠️ هنا ممكن تضع منطق الاتصال بـ Stripe أو أي خدمة حقيقية
//        boolean success = true; // نفترض الدفع ناجح للتجربة
//
//        Payment payment = new Payment(username, courseId, amount, success, LocalDateTime.now());
//        return repository.save(payment);
//    }
//
//    public List<Payment> getPaymentsByUser(String username) {
//        return repository.findByUsername(username);
//    }
//
//    public boolean hasUserPaidForCourse(String username, Long courseId) {
//        return repository.findByUsername(username).stream()
//                .anyMatch(p -> p.getCourseId().equals(courseId) && p.isSuccess());
//    }
//}
package com.education.payment_service.service;

import com.education.payment_service.client.CourseClient;
import com.education.payment_service.client.UserClient;
import com.education.payment_service.exception.PaymentRequiredException;
import com.education.payment_service.model.CourseDTO;
import com.education.payment_service.model.Payment;
import com.education.payment_service.model.UserDTO;
import com.education.payment_service.repository.PaymentRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final CourseClient courseClient;
    @Autowired
    private UserClient userClient;

    public PaymentService(PaymentRepository repository, CourseClient courseClient) {
        this.repository = repository;
        this.courseClient = courseClient;
    }
////////////1/////////////////////
//    public Payment processPayment(String username, Long courseId, double amount) {
//        // ✅ جلب بيانات الدورة من course-service
//        CourseDTO course = courseClient.getCourseById(courseId);
//
//        // ✅ تحقق من أن المبلغ يطابق السعر الحقيقي للدورة
//        if (Double.compare(course.getPrice(), amount) != 0) {
//            throw new PaymentRequiredException("المبلغ المدفوع لا يطابق سعر الدورة (" + course.getPrice() + ")");
//        }
//
//        // ✅ إنشاء وتخزين الدفع
//        Payment payment = new Payment(username, courseId, amount, true, LocalDateTime.now());
//        return repository.save(payment);
//    }
    ///////////////2/////////////////////
//public Payment processPayment(String username, Long courseId, double amount) {
//    // ✅ جلب بيانات الدورة من course-service
//    CourseDTO course = courseClient.getCourseById(courseId);
//
//    // ✅ تحقق من أن المبلغ يطابق السعر الحقيقي للدورة
//    if (Double.compare(course.getPrice(), amount) != 0) {
//        throw new PaymentRequiredException("المبلغ المدفوع لا يطابق سعر الدورة (" + course.getPrice() + ")");
//    }
//
//    // ✅ جلب بيانات المستخدم من user-service
//    UserDTO user = userClient.getUserByUsername(username);
//
//    // ✅ تحقق من أن الرصيد كافٍ
//    if (user.getWalletBalance() < amount) {
//        throw new PaymentRequiredException("❌ الرصيد غير كافٍ في المحفظة (" + user.getWalletBalance() + ")");
//    }
//
//    // ✅ خصم المبلغ من المحفظة
//    double newBalance = user.getWalletBalance() - amount;
//    userClient.updateWallet(username, newBalance); // Endpoint في user-service
//
//    // ✅ إنشاء وتخزين الدفع
//    Payment payment = new Payment(username, courseId, amount, true, LocalDateTime.now());
//    return repository.save(payment);
//}
public Payment processPayment(String username, Long courseId, double amount) {
    // ✅ جلب بيانات الدورة
    CourseDTO course = courseClient.getCourseById(courseId);

    // ✅ التحقق من المبلغ
    if (Double.compare(course.getPrice(), amount) != 0) {
        throw new PaymentRequiredException("المبلغ المدفوع لا يطابق سعر الدورة (" + course.getPrice() + ")");
    }

    // ✅ التحقق من الدفع السابق
    if (repository.existsByUsernameAndCourseId(username, courseId)) {
        throw new PaymentRequiredException("⚠️ لقد قمت بالدفع لهذه الدورة من قبل");
    }

    // ✅ جلب بيانات المستخدم
    UserDTO user = userClient.getUserByUsername(username);

    // ✅ التحقق من الرصيد
    if (user.getWalletBalance() < amount) {
        throw new PaymentRequiredException("❌ الرصيد غير كافٍ في المحفظة (" + user.getWalletBalance() + ")");
    }

    // ✅ خصم الرصيد
    double newBalance = user.getWalletBalance() - amount;
    userClient.updateWallet(username, newBalance);

    // ✅ إنشاء الدفع
    Payment payment = new Payment(username, courseId, amount, true, LocalDateTime.now());
    return repository.save(payment);
}

    public List<Payment> getPaymentsByUser(String username) {
        return repository.findByUsername(username);
    }

    public boolean hasUserPaidForCourse(String username, Long courseId) {
        return repository.findByUsername(username).stream()
                .anyMatch(p -> p.getCourseId().equals(courseId) && p.isSuccess());
    }

}
