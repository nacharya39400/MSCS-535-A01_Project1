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
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_owner", nullable = false)
    private String accountOwner;

    @Column(name = "account_email", nullable = false, unique = true)
    private String accountEmail;

    @Column(name = "account_balance", nullable = false)
    private Long accountBalance;

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
