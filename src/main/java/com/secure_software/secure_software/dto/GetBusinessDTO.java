package com.secure_software.secure_software.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBusinessDTO {

    @Expose
    @SerializedName("accountId")
    @NotNull(message = "{account.id.notnull}")
    private UUID accountId;
}
