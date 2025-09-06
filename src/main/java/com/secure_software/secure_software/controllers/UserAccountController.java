package com.secure_software.secure_software.controllers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.secure_software.secure_software.dto.CreateUserDTO;
import com.secure_software.secure_software.entities.User;
import com.secure_software.secure_software.services.UserBO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserAccountController {

    @Autowired
    private UserBO userBO;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/singUp")
    public ResponseEntity<?> createAccount(@RequestBody @Valid CreateUserDTO createUserDTO) {
        try {
            User user = this.userBO.createUser(createUserDTO);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An internal system error occurred while processing your request");
        }
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody @Valid LoginRequestDTO loginRequestDTO,
                                               HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password());

        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        // Save into SecurityContextHolder
        SecurityContextHolder.setContext(context);

        // Persist into session
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return ResponseEntity.ok("Success");
    }

    @GetMapping(value="/signOut")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("You have been logged out successfully");
    }



    public record LoginRequestDTO(
            @Expose
            @SerializedName("email")
            @NotNull(message = "{login.email.notnull}")
            String email,
            @Expose
            @SerializedName("password")
            @NotNull(message = "{login.password.notnull}")
            String password
    ) {}
}
