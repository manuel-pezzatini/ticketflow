package com.manuel.ticketflow.dto;

import java.time.LocalDateTime;

import com.manuel.ticketflow.enums.Stato;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoricoStatoResponse {

    private Long id;
    private Stato statoPrecedente;
    private Stato statoNuovo;
    private LocalDateTime dataModifica;

}
