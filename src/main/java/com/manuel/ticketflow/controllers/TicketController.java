package com.manuel.ticketflow.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manuel.ticketflow.dto.CommentoRequest;
import com.manuel.ticketflow.dto.CommentoResponse;
import com.manuel.ticketflow.dto.CreateTicketRequest;
import com.manuel.ticketflow.dto.StoricoStatoResponse;
import com.manuel.ticketflow.dto.TicketResponse;
import com.manuel.ticketflow.services.TicketService;

import jakarta.validation.Valid;

import com.manuel.ticketflow.services.CommentoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    
    private final CommentoService commentoService;
    private final TicketService ticketService;

    public TicketController(TicketService ticketService, CommentoService commentoService) {
        this.ticketService = ticketService;
        this.commentoService = commentoService;
    }

    @PostMapping
    public TicketResponse creaTicket(@Valid @RequestBody CreateTicketRequest createTicketRequest) {
        
        return ticketService.creaTicket(createTicketRequest);
    }

    @GetMapping("/mine")
    public List<TicketResponse> getTicket() {
        return ticketService.getMieiTicket();
    }
    
    @GetMapping("/open")
    public List<TicketResponse> getTicketOpen() {
        return ticketService.getTicketAperti();
    }
    
    @PatchMapping("/{id}/take")
    public TicketResponse prendiInCarico(@PathVariable Long id){
        return ticketService.prendiInCarico(id);
    }

    @GetMapping("/assigned")
    public List<TicketResponse> getTicketOperatore() {
        return ticketService.getTicketOperatore();
    }
    
    @PatchMapping("/{id}/close")
    public TicketResponse chiudiTicket(@PathVariable("id") Long ticketId){
        return ticketService.chiudiTicket(ticketId);
    }

    @GetMapping("/{id}/history")
    public List<StoricoStatoResponse> getstoricoStato(@PathVariable Long id) {
        return ticketService.getStoricoTicket(id);
    }

    @PostMapping("/{id}/comments")
    public CommentoResponse aggiungiCommento(@PathVariable Long id, @Valid @RequestBody CommentoRequest request) {

        return commentoService.aggiungiCommento(id, request);
    }

    @GetMapping("/{id}/comments")
    public List<CommentoResponse> getCommentiTicket(@PathVariable Long id) {
        return commentoService.getCommentiTicket(id);
    }
    
    
    
}
