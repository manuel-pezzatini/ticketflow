package com.manuel.ticketflow.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.manuel.ticketflow.dto.CreateTicketRequest;
import com.manuel.ticketflow.dto.StoricoStatoResponse;
import com.manuel.ticketflow.dto.TicketResponse;
import com.manuel.ticketflow.models.Categoria;
import com.manuel.ticketflow.models.StoricoStato;
import com.manuel.ticketflow.models.Ticket;
import com.manuel.ticketflow.models.Utente;
import com.manuel.ticketflow.enums.Stato;
import com.manuel.ticketflow.exceptions.ForbiddenException;
import com.manuel.ticketflow.exceptions.ResourceNotFoundException;
import com.manuel.ticketflow.repositories.CategoriaRepository;
import com.manuel.ticketflow.repositories.StoricoStatoRepository;
import com.manuel.ticketflow.repositories.TicketRepository;
import com.manuel.ticketflow.repositories.UtenteRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final CategoriaRepository categoriaRepository;
    private final UtenteRepository utenteRepository;
    private final StoricoStatoRepository storicoStatoRepository;

    public TicketService(TicketRepository ticketRepository,
                        CategoriaRepository categoriaRepository,
                        UtenteRepository utenteRepository,
                        StoricoStatoRepository storicoStatoRepository){

        this.ticketRepository = ticketRepository;
        this.categoriaRepository = categoriaRepository;
        this.utenteRepository = utenteRepository;
        this.storicoStatoRepository = storicoStatoRepository;
    }
    
    public TicketResponse creaTicket(CreateTicketRequest createTicketRequest){
        Optional<Categoria> categoriaTrovata = categoriaRepository.findById(createTicketRequest.getCategoriaId());

        if(categoriaTrovata.isEmpty()){
            throw new ResourceNotFoundException("Categoria non esistente");
        }

        Categoria categoria = categoriaTrovata.get();

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<Utente> utenteTrovato = utenteRepository.findByEmail(email);
        if(utenteTrovato.isEmpty()){
            throw new IllegalArgumentException("Utente non trovato");
        }

        Utente utente = utenteTrovato.get();

        Ticket ticket = new Ticket();
            ticket.setTitolo(createTicketRequest.getTitolo());
            ticket.setDescrizione(createTicketRequest.getDescrizione());
            ticket.setStato(Stato.OPEN);
            ticket.setPriorita(createTicketRequest.getPriorita());
            ticket.setDataCreazione(LocalDateTime.now());
            ticket.setAutore(utente);
            ticket.setCategoria(categoria);

        ticketRepository.save(ticket);

        return toResponse(ticket);
    }

    public List<TicketResponse> getMieiTicket(){
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<Utente> utenteTrovato =
            utenteRepository.findByEmail(email);

        if (utenteTrovato.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Utente utente = utenteTrovato.get();

        List<Ticket> tickets = ticketRepository.findByAutore(utente);
        List<TicketResponse> responses = new ArrayList<>();

        for(Ticket ticket : tickets){
            responses.add(toResponse(ticket));
        }

        return responses;
    }

    public List<TicketResponse> getTicketAperti(){
        List<Ticket> ticketOpen = ticketRepository.findByStato(Stato.OPEN);

        List<TicketResponse> responses = new ArrayList<>();

        for (Ticket ticket : ticketOpen) {
            responses.add(toResponse(ticket));
        }

        return responses;
    }

    @Transactional 
    public TicketResponse prendiInCarico(Long ticketId){
        Optional<Ticket> ticketTrovato = ticketRepository.findById(ticketId);

        if(ticketTrovato.isEmpty()){
            throw new ResourceNotFoundException("Errore: ticket non trovato");
        }

        Ticket ticket = ticketTrovato.get();

        Stato statoPrecedente = ticket.getStato();

        if (ticket.getStato() != Stato.OPEN ) {
            throw new IllegalArgumentException("Ticket non disponibile per la presa in carico");
        }

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<Utente> utenteTrovato =
            utenteRepository.findByEmail(email);

        if (utenteTrovato.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Utente utente = utenteTrovato.get();

        ticket.setOperatore(utente);
        ticket.setStato(Stato.IN_PROGRESS);
        ticket.setDataUltimoAggiornamento(LocalDateTime.now());
        
        Ticket ticketSalvato = ticketRepository.save(ticket);

        StoricoStato storicoStato = new StoricoStato();
        storicoStato.setTicket(ticketSalvato);
        storicoStato.setStatoPrecedente(statoPrecedente);
        storicoStato.setStatoNuovo(ticketSalvato.getStato());
        storicoStato.setDataModifica(LocalDateTime.now());
        storicoStato.setModificatoDa(utente);

        storicoStatoRepository.save(storicoStato);
        
        return toResponse(ticket);

    }

    public List<TicketResponse> getTicketOperatore(){
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<Utente> utenteTrovato =
            utenteRepository.findByEmail(email);

        if (utenteTrovato.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Utente utente = utenteTrovato.get();

        List<Ticket> ticketOperatore = ticketRepository.findByOperatore(utente);

        List<TicketResponse> ticketResponse = new ArrayList<>();
        
    
        for (Ticket ticket : ticketOperatore) {
            ticketResponse.add(toResponse(ticket));
        }

        return ticketResponse;
    }

    @Transactional
    public TicketResponse chiudiTicket(Long ticketId){
        
        Optional<Ticket> ticketTrovato = ticketRepository.findById(ticketId);

        if(ticketTrovato.isEmpty()){
            throw new ResourceNotFoundException("Errore: il ticket non è esistente");
        }

        Ticket ticket = ticketTrovato.get();

        Stato statoPrecedente = ticket.getStato();

        if(ticket.getStato() != Stato.IN_PROGRESS){
            throw new IllegalArgumentException("Errore: il ticket non può essere modificato");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Optional<Utente> utenteTrovato =
            utenteRepository.findByEmail(email);

        if (utenteTrovato.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        Utente utente = utenteTrovato.get();

        if(!ticket.getOperatore().getId().equals(utente.getId())){
            throw new ForbiddenException(
            "Errore: il ticket non è assegnato a questo operatore"
            );
        }

        ticket.setStato(Stato.CLOSED);
        ticket.setDataUltimoAggiornamento(LocalDateTime.now());

        Ticket ticketSalvato = ticketRepository.save(ticket);

        StoricoStato storicoStato = new StoricoStato();
        storicoStato.setTicket(ticketSalvato);
        storicoStato.setStatoPrecedente(statoPrecedente);
        storicoStato.setStatoNuovo(ticketSalvato.getStato());
        storicoStato.setDataModifica(LocalDateTime.now());
        storicoStato.setModificatoDa(utente);

        storicoStatoRepository.save(storicoStato);
        
        return toResponse(ticket);

    }

    public List<StoricoStatoResponse> getStoricoTicket(Long ticketId) {

        Optional<Ticket> ticketTrovato = ticketRepository.findById(ticketId);

        if(ticketTrovato.isEmpty()){
            throw new ResourceNotFoundException("Errore: Ticket non trovato");
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

        if(ticket.getOperatore() == null || !ticket.getOperatore().getId().equals(utente.getId())){
            throw new ForbiddenException(
            "Errore: il ticket non è assegnato a questo operatore"
            );
        }

        List<StoricoStato> storico = storicoStatoRepository.findByTicket(ticket);

        List<StoricoStatoResponse> storicoResponse = new ArrayList<>();

        for (StoricoStato storicoStato : storico) {

            StoricoStatoResponse response = new StoricoStatoResponse();

            response.setId(storicoStato.getId());
            response.setStatoPrecedente(storicoStato.getStatoPrecedente());
            response.setStatoNuovo(storicoStato.getStatoNuovo());
            response.setDataModifica(storicoStato.getDataModifica());

            storicoResponse.add(response);
        }

        return storicoResponse;

    }

        private TicketResponse toResponse(Ticket ticket){
            TicketResponse response = new TicketResponse();

            response.setId(ticket.getId());
            response.setTitolo(ticket.getTitolo());
            response.setDescrizione(ticket.getDescrizione());
            response.setStato(ticket.getStato());
            response.setPriorita(ticket.getPriorita());

            return response;
        }
}
