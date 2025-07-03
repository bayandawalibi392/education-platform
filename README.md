#  Education Platform - نظام تعليم إلكتروني
مشروع نظام تعليم إلكتروني مبني باستخدام **Spring Boot Microservices Architecture**.  
يسمح هذا النظام بإدارة المستخدمين، الكورسات، عمليات الدفع، التسجيل، والاختبارات ضمن بيئة موزّعة وآمنة.
---
 مكونات النظام (Microservices)
- `user-service`: إدارة المستخدمين وتسجيل الدخول
- `course-service`: إدارة الكورسات
- `payment-service`: الدفع والمحفظة
- `enrollment-service`: تسجيل الطلاب في الدورات
- `exam-service`: إنشاء الاختبارات
- `api_gateway`: نقطة دخول موحدة
- `discovery-server`: خادم الاكتشاف (Eureka)
---

##  خطوات التشغيل المحلي

> **المتطلبات:** Java 17+, Maven, Spring Cloud

1. شغّل أولاً خدمة `discovery-server`
2. ثم شغّل `api_gateway`
3. شغّل الخدمات الأخرى: `user-service`, `course-service`, `payment-service`, `enrollment-service`, `exam-service`
4. جميع الخدمات تتصل عبر Eureka وتستخدم JWT للتوثيق
---
##  الأمان
- النظام يستخدم **JWT** لتوثيق المستخدمين.
- جميع الاتصالات تمر من خلال `api_gateway`.
- يتم التحقق من الرول (مثل `STUDENT`, `ADMIN`) في كل خدمة على حدة.
---
##  الدفع
- عند تسجيل طالب في دورة، يتم خصم المبلغ من محفظته (Wallet) عبر `payment-service`.
- ثم يتم إرسال طلب تسجيل إلى `enrollment-service`.

---
##  أدوات ومكتبات مستخدمة
- Spring Boot
- Spring Cloud Gateway
- Spring Security + JWT
- Eureka Discovery Server
- OpenFeign (للتواصل بين الخدمات)
- H2   لقواعد البيانات
---
