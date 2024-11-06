package com.bgauction.notificationservice.feign;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class User {
    @NotNull
    @Positive
    private Long id;

    @NotBlank
    @Email
    private String email;
}
