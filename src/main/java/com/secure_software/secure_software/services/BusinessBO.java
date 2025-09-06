package com.secure_software.secure_software.services;

import com.secure_software.secure_software.dao.BusinessDAO;
import com.secure_software.secure_software.dto.CreateBusinessDTO;
import com.secure_software.secure_software.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BusinessBO {

    private final BusinessDAO businessDAO;

    @Autowired
    public BusinessBO(BusinessDAO businessDAO) {
        this.businessDAO = businessDAO;
    }

    //--- Insert new account from DTO ---//
    public Account createAccount(CreateBusinessDTO dto) {
        Account account = Account.builder()
                .accountName(dto.getAccountName())
                .accountOwner(dto.getAccountOwner())
                .accountEmail(dto.getAccountEmail())
                .accountBalance(dto.getAccountBalance())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .createdDatetime(LocalDateTime.now())
                .updatedDatetime(LocalDateTime.now())
                .build();

        return businessDAO.save(account);
    }

    //--- Get account by id ---//
    public Account getById(UUID id) {
        return businessDAO.findById(id).orElse(null);
    }
}
