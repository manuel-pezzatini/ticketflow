package com.manuel.ticketflow.models;

import java.time.LocalDateTime;

import com.manuel.ticketflow.enums.Priorita;
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
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titolo;


    private String descrizione;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato", nullable = false)
    private Stato stato;

    @Enumerated(EnumType.STRING)
    @Column(name = "priorita", nullable = false)
    private Priorita priorita;

    @Column(name = "data_creazione")
    private LocalDateTime dataCreazione;

    @Column(name = "data_ultimo_aggiornamento")
    private LocalDateTime dataUltimoAggiornamento;

    @ManyToOne
    @JoinColumn(name = "autore_id", nullable = false)
    private Utente autore;

    @ManyToOne
    @JoinColumn(name = "operatore_id")
    private Utente operatore;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

}
