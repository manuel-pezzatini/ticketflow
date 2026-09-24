package com.manuel.ticketflow.dto;
import com.manuel.ticketflow.enums.Priorita;
import com.manuel.ticketflow.enums.Stato;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    
    private Long id;
    private String titolo;
    private String descrizione;
    private Stato stato;
    private Priorita priorita;
}
