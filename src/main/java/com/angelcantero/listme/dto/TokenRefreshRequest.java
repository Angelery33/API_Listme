package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshRequest {
    @NotBlank(message = "El token de refresco es obligatorio")
    private String refreshToken;
}
