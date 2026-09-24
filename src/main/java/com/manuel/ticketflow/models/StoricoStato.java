package com.manuel.ticketflow.models;

import java.time.LocalDateTime;

import com.manuel.ticketflow.enums.Stato;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "storico_stato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoricoStato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato_precedente")
    private Stato statoPrecedente;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato_nuovo")
    private Stato statoNuovo;

    @Column(name = "data_modifica")
    private LocalDateTime dataModifica;


    @ManyToOne
    @JoinColumn(name = "modificato_da", nullable = false)
    private Utente modificatoDa;
}
