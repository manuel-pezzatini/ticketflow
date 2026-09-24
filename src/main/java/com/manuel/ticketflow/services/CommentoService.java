package com.manuel.ticketflow.services;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.manuel.ticketflow.dto.CommentoRequest;
import com.manuel.ticketflow.dto.CommentoResponse;
import com.manuel.ticketflow.enums.Ruolo;
import com.manuel.ticketflow.exceptions.ForbiddenException;
import com.manuel.ticketflow.exceptions.ResourceNotFoundException;
import com.manuel.ticketflow.models.Commento;
import com.manuel.ticketflow.models.Ticket;
import com.manuel.ticketflow.models.Utente;
import com.manuel.ticketflow.repositories.CommentoRepository;
import com.manuel.ticketflow.repositories.TicketRepository;
import com.manuel.ticketflow.repositories.UtenteRepository;

@Service
public class CommentoService {

    private final CommentoRepository commentoRepository;
    private final TicketRepository ticketRepository;
    private final UtenteRepository utenteRepository;

    public CommentoService(
            CommentoRepository commentoRepository,
            TicketRepository ticketRepository,
            UtenteRepository utenteRepository) {

        this.commentoRepository = commentoRepository;
        this.ticketRepository = ticketRepository;
        this.utenteRepository = utenteRepository;
    }

    public CommentoResponse aggiungiCommento(
            Long ticketId,
            CommentoRequest request) {

        // 1. Cerchiamo il ticket
        Optional<Ticket> ticketTrovato =
                ticketRepository.findById(ticketId);

        if (ticketTrovato.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Errore: il ticket non è esistente"
            );
        }

        Ticket ticket = ticketTrovato.get();

        // 2. Recuperiamo l'utente autenticato
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<Utente> utenteTrovato =
                utenteRepository.findByEmail(email);

        if (utenteTrovato.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Utente utente = utenteTrovato.get();

        // 3. Se è UTENTE, il ticket deve essere suo
        if (utente.getRuolo() == Ruolo.UTENTE &&
                !ticket.getAutore().getId().equals(utente.getId())) {

            throw new ForbiddenException(
                    "Errore: non puoi commentare questo ticket"
            );
        }

        // 4. Se è OPERATORE, il ticket deve essere assegnato a lui
        if (utente.getRuolo() == Ruolo.OPERATORE &&
                (ticket.getOperatore() == null ||
                !ticket.getOperatore().getId().equals(utente.getId()))) {

            throw new ForbiddenException(
                    "Errore: non puoi commentare questo ticket"
            );
        }

        // 5. Creiamo il commento
        Commento commento = new Commento();

        commento.setTesto(request.getTesto());
        commento.setAutore(utente);
        commento.setTicket(ticket);
        commento.setDataCreazione(LocalDateTime.now());

        // 6. Salviamo il commento nel DB
        Commento commentoSalvato =
                commentoRepository.save(commento);

        // 7. Entity -> DTO
        CommentoResponse response = new CommentoResponse();

        response.setId(commentoSalvato.getId());
        response.setTesto(commentoSalvato.getTesto());
        response.setNomeAutore(
                commentoSalvato.getAutore().getNome()
        );
        response.setDataCreazione(
                commentoSalvato.getDataCreazione()
        );

        return response;
    }

    public List<CommentoResponse> getCommentiTicket(Long ticketId) {
        Optional<Ticket> ticketTrovato = ticketRepository.findById(ticketId);

        if(ticketTrovato.isEmpty()){
            throw new ResourceNotFoundException("Errore: il ticket non è stato trovato");

        }
        Ticket ticket = ticketTrovato.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<Utente> utenteTrovato =
            utenteRepository.findByEmail(email);

        if (utenteTrovato.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Utente utente = utenteTrovato.get();

        if (utente.getRuolo() == Ruolo.UTENTE &&
                !ticket.getAutore().getId().equals(utente.getId())) {

            throw new ForbiddenException(
                    "Errore: non puoi visualizzare i commenti di questo ticket"
            );
        }

        if (utente.getRuolo() == Ruolo.OPERATORE &&
                (ticket.getOperatore() == null ||
                !ticket.getOperatore().getId().equals(utente.getId()))) {

            throw new ForbiddenException(
                    "Errore: non puoi visualizzare i commenti di questo ticket"
            );
        }

        List<Commento> commenti = commentoRepository.findByTicketOrderByDataCreazioneAsc(ticket);
        List<CommentoResponse> commentiResponse = new ArrayList<>();


        for (Commento commento : commenti) {
            CommentoResponse response = new CommentoResponse();
            response.setId(commento.getId());
            response.setTesto(commento.getTesto());
            response.setNomeAutore(commento.getAutore().getNome());
            response.setDataCreazione(commento.getDataCreazione());

            commentiResponse.add(response);
            
        }
        return commentiResponse;

    }
}