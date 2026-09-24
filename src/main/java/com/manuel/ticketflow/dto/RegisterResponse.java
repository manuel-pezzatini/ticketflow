package com.manuel.ticketflow.dto;
import com.manuel.ticketflow.enums.Ruolo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String nome;
    private String cognome;
    private String email;
    private Ruolo ruolo;
}
