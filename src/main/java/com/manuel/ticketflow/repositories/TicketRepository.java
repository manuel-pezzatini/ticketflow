package com.manuel.ticketflow.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manuel.ticketflow.enums.Stato;
import com.manuel.ticketflow.models.Ticket;
import com.manuel.ticketflow.models.Utente;

public interface TicketRepository extends JpaRepository<Ticket, Long>{
    
    List<Ticket> findByAutore(Utente autore);
    List<Ticket> findByStato(Stato stato);

    List<Ticket> findByOperatore(Utente operatore);
}
