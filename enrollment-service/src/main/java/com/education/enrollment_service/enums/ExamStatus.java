package com.education.enrollment_service.enums;

public enum ExamStatus {
    NOT_ATTEMPTED("لم يتقدّم للاختبار بعد"),
    FAILED("لم يجتز جميع الاختبارات"),
    PASSED("اجتاز الاختبارات");

    private final String label;

    ExamStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
