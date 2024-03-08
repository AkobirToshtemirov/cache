package com.akobir.cache.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record StudentRequestDTO(
        @NotBlank(message = "Name cannot be empty!")
        String name,

        @Min(value = 1, message = "Age cannot be negative!")
        int age
) {
}
