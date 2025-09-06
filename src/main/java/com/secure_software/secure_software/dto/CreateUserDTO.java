package com.secure_software.secure_software.dto;

import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {

    @Expose
    @NotNull(message = "{user.firstName.notnull}")
    @NotBlank(message = "{user.firstName.notblank}")
    private String firstName;

    @Expose
    @NotNull(message = "{user.lastName.notnull}")
    @NotBlank(message = "{user.lastName.notblank}")
    private String lastName;

    @Expose
    @NotNull(message = "{user.email.notnull}")
    @NotBlank(message = "{user.email.notblank}")
    @Email(message = "{user.email.invalid}")
    private String email;

    @Expose
    @NotNull(message = "{user.password.notnull}")
    @NotBlank(message = "{user.password.notblank}")
    private String password;

    @Expose
    @NotNull(message = "{user.address.notnull}")
    @NotBlank(message = "{user.address.notblank}")
    private String address;

    @Expose
    @NotNull(message = "{user.phoneNumber.notnull}")
    @NotBlank(message = "{user.phoneNumber.notblank}")
    private String phoneNumber;
}
