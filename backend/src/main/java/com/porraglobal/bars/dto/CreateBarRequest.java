package com.porraglobal.bars.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateBarRequest(
        @NotBlank @Size(max = 100)
        String name,

        @Size(max = 255)
        String description,

        @NotBlank @Size(max = 200)
        String address,

        @Size(max = 80)
        String city,

        BigDecimal latitude,

        BigDecimal longitude,

        @Size(max = 20)
        String phone
) {
}
