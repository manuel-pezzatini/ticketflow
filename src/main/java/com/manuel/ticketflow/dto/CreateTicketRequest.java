package com.manuel.ticketflow.dto;

import com.manuel.ticketflow.enums.Priorita;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequest {
    @NotBlank(message = "Il titolo è obbligatorio")
    private String titolo;
    @NotBlank(message = "La descrizione è obbligatoria")
    private String descrizione;
    @NotNull(message = "La priorità è obbligatoria")
    private Priorita priorita;
    @NotNull(message = "La categoria è obbligatoria")
    private Long categoriaId;
}
