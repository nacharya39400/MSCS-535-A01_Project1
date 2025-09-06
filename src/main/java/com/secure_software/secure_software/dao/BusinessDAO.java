package com.secure_software.secure_software.dao;

import com.secure_software.secure_software.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessDAO extends JpaRepository<Account, UUID>{
}

