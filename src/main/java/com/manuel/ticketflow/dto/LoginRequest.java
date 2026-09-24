package com.manuel.ticketflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "L'email è obbligatoria")
    @Email (message = "Il formato non è valido")
    private String email;
    @NotBlank(message = "La password è obbligatoria")
    private String password;
}
