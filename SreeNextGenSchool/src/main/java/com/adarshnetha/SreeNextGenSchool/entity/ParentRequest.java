package com.adarshnetha.SreeNextGenSchool.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "parent_requests",
    uniqueConstraints = @UniqueConstraint(
        name = "UK_parent_requests_mobile_number",
        columnNames = "mobile_number"
    )
)
public class ParentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String parentName;

    @Column(name = "mobile_number", nullable = false)
    private String phoneWhatsApp;

    @Column(nullable = true)
    private String classApplyingFor;

    @Column(columnDefinition = "text")
    private String message;

    @Column(name = "additional_children_json", columnDefinition = "text")
    private String additionalChildrenJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @PrePersist
    void prePersist() {
        if (status == null) {
            status = RequestStatus.REQUESTED_FOR_ADMISSION;
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPhoneWhatsApp() {
        return phoneWhatsApp;
    }

    public void setPhoneWhatsApp(String phoneWhatsApp) {
        this.phoneWhatsApp = phoneWhatsApp;
    }

    public String getClassApplyingFor() {
        return classApplyingFor;
    }

    public void setClassApplyingFor(String classApplyingFor) {
        this.classApplyingFor = classApplyingFor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAdditionalChildrenJson() {
        return additionalChildrenJson;
    }

    public void setAdditionalChildrenJson(String additionalChildrenJson) {
        this.additionalChildrenJson = additionalChildrenJson;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
