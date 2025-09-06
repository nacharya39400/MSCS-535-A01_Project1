package com.secure_software.secure_software.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "created_by", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'SYSTEM'")
    private String createdBy;

    @Column(name = "updated_by", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'SYSTEM'")
    private String updatedBy;

    @Column(name = "created_datetime", nullable = false,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDatetime;

    @Column(name = "updated_date_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedDatetime;
}
