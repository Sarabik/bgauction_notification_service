package com.bgauction.notificationservice.feign;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Game {
    @NotNull
    @Positive
    private Long id;

    @NotBlank
    @Size(min = 2)
    private String title;
}
