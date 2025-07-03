package com.education.user_service.config;

import com.education.user_service.model.User;
import com.education.user_service.repository.UserRepository;
import com.education.user_service.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UserService userService, UserRepository userRepository) {
        return args -> {
            // تحقق إذا الأدمن موجود أصلاً
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword("admin123");  // كلمة المرور ستشفّر داخل UserService
                admin.setRole("ADMIN");
                userService.registerUser(admin);
                System.out.println("تم إنشاء المستخدم أدمن تلقائياً: admin / admin123");
            } else {
                System.out.println("الأدمن موجود مسبقاً، لن يتم إنشاء واحد جديد.");
            }
        };
    }
}
