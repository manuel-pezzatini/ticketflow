package com.manuel.ticketflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manuel.ticketflow.models.StoricoStato;
import com.manuel.ticketflow.models.Ticket;

import java.util.List;


public interface StoricoStatoRepository extends JpaRepository<StoricoStato, Long>{

    List<StoricoStato> findByTicket(Ticket ticket);

}
