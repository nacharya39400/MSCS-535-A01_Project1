package com.secure_software.secure_software.services;

import com.secure_software.secure_software.dao.UserDAO;
import com.secure_software.secure_software.dto.CreateUserDTO;
import com.secure_software.secure_software.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserBO {

    private UserDAO userDAO;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserBO(UserDAO userDAO, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDAO = userDAO;
        this.bCryptPasswordEncoder =bCryptPasswordEncoder;
    }

    //--- Insert new user from DTO ---//
    public User createUser(CreateUserDTO dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(this.bCryptPasswordEncoder.encode(dto.getPassword()))   // make sure this is encoded later!
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .createdDatetime(LocalDateTime.now())
                .updatedDatetime(LocalDateTime.now())
                .build();

        return this.userDAO.save(user);
    }
}
