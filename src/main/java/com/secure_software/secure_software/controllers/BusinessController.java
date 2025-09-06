package com.secure_software.secure_software.controllers;

import com.secure_software.secure_software.dto.CreateBusinessDTO;
import com.secure_software.secure_software.dto.GetBusinessDTO;
import com.secure_software.secure_software.entities.Account;
import com.secure_software.secure_software.services.BusinessBO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BusinessController {

    @Autowired
    private BusinessBO businessBO;

    @PostMapping(value = "/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody @Valid CreateBusinessDTO createBusinessDTO) {
        try {
            Account account = this.businessBO.createAccount(createBusinessDTO);
            return ResponseEntity.ok().body(account);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An internal system error occurred while processing your request");
        }

    }

    @PostMapping(value = "/getAccount")
    public ResponseEntity<?> getAccount( @RequestBody @Valid GetBusinessDTO getBusinessDTO) {
        try {
            Account byId = this.businessBO.getById(getBusinessDTO.getAccountId());
            return ResponseEntity.ok().body(byId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An internal system error occurred while processing your request");
        }
    }
}
