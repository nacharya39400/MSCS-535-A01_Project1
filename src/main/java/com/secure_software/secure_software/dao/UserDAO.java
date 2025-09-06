package com.secure_software.secure_software.dao;

import com.secure_software.secure_software.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

;

/**
 * AppUserRepo
 */

public interface UserDAO extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

}
