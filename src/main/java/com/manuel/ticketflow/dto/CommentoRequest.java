package com.manuel.ticketflow.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentoRequest {
    @NotBlank(message = "Il testo del commento è obbligatorio")
    private String testo;

}
