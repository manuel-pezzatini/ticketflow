package com.manuel.ticketflow.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manuel.ticketflow.models.Commento;
import com.manuel.ticketflow.models.Ticket;

public interface CommentoRepository extends JpaRepository<Commento, Long>{

    List<Commento> findByTicketOrderByDataCreazioneAsc(Ticket ticket);

}
