package com.example.adminservice.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
//@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long enrollment_id;

    @Column(name = "student_id")
    private Long student_id;

    @Column(name = "course_id")
    private Long course_id;

    // Getters and setters
    public Long getEnrollmentId() {
        return enrollment_id;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollment_id = enrollment_id;
    }

    public Long getStudentId() {
        return student_id;
    }

    public void setStudentId(Long studentId) {
        this.student_id = student_id;
    }

    public Long getCourseId() {
        return course_id;
    }

    public void setCourseId(Long courseId) {
        this.course_id = course_id;
    }
}
