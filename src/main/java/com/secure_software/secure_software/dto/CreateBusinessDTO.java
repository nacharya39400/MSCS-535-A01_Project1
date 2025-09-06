package com.secure_software.secure_software.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBusinessDTO {

    @Expose
    @SerializedName("accountName")
    @NotNull(message = "{account.name.notnull}")
    private String accountName;

    @Expose
    @SerializedName("accountOwner")
    @NotNull(message = "{account.owner.notnull}")
    private String accountOwner;

    @Expose
    @SerializedName("accountEmail")
    @NotNull(message = "{account.email.notnull}")
    private String accountEmail;

    @Expose
    @SerializedName("accountBalance")
    @NotNull(message = "{account.balance.not.null}")
    private Long accountBalance;

    @Expose
    @SerializedName("address")
    @NotNull(message = "{address.notnull}")
    private String address;

    @Expose
    @SerializedName("phoneNumber")
    @NotNull(message = "{phone.number.notnull}")
    private String phoneNumber;
}
