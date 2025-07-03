//package com.education.user_service.controller;
//
//import com.education.user_service.model.User;
//import com.education.user_service.service.UserService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/users")
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody User user) {
//        if (user.getUsername() == null || user.getUsername().isEmpty()) {
//            return ResponseEntity.badRequest().body("Username is required");
//        }
//        if (user.getEmail() == null || user.getEmail().isEmpty()) {
//            return ResponseEntity.badRequest().body("Email is required");
//        }
//        if (user.getPassword() == null || user.getPassword().isEmpty()) {
//            return ResponseEntity.badRequest().body("Password is required");
//        }
//
//        try {
//            User registeredUser = userService.registerUser(user);
//            return ResponseEntity.ok(registeredUser);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Internal Server Error");
//        }
//    }
//}
package com.education.user_service.controller;

import com.education.user_service.model.User;
import com.education.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }

        // السماح فقط بدور STUDENT أو بدون تحديد دور
        if (user.getRole() != null && !user.getRole().equalsIgnoreCase("STUDENT")) {
            return ResponseEntity.badRequest().body("You cannot register with this role.");
        }

        // تأكيد أن دور المستخدم سيكون STUDENT دائمًا للتسجيل العام
        user.setRole("STUDENT");

        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    // endpoint خاص للأدمن لإضافة مدربين
    @PostMapping("/add-teacher")
    public ResponseEntity<?> addTeacher(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }

        // تعيين دور TEACHER فقط هنا
        user.setRole("TEACHER");

        try {
            User registeredTeacher = userService.registerUser(user);
            return ResponseEntity.ok(registeredTeacher);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }
    @PostMapping("/wallet/recharge")
    public ResponseEntity<?> rechargeWallet(@RequestParam String username, @RequestParam Double amount) {
        User user = userService.findByUsername(username);
        user.setWalletBalance(user.getWalletBalance() + amount);
        userService.save(user);
        return ResponseEntity.ok("Wallet recharged successfully.");
    }
    @PutMapping("/update-wallet")
    public ResponseEntity<?> updateWallet(@RequestParam String username, @RequestParam Double newBalance) {
        User user = userService.findByUsername(username);
        user.setWalletBalance(newBalance);
        userService.save(user);
        return ResponseEntity.ok("Wallet updated");
    }
    @GetMapping("/by-username/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }


}
