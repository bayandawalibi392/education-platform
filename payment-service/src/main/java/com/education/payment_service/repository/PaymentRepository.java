package com.education.payment_service.repository;



import com.education.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUsername(String username);
    List<Payment> findByCourseId(Long courseId);
    boolean existsByUsernameAndCourseId(String username, Long courseId);

}
