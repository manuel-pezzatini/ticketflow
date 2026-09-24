package com.manuel.ticketflow.services;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.manuel.ticketflow.models.Utente;
import com.manuel.ticketflow.repositories.UtenteRepository;
import com.manuel.ticketflow.dto.LoginResponse;
import com.manuel.ticketflow.dto.RegisterRequest;
import com.manuel.ticketflow.dto.RegisterResponse;
import com.manuel.ticketflow.enums.Ruolo;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder){
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }




    public RegisterResponse registraUtente(RegisterRequest registerRequest){

        if(utenteRepository.existsByEmail(registerRequest.getEmail())){
            throw new IllegalArgumentException("Email già esistente.");
        }
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        Utente nuovoUtente = new Utente();

        nuovoUtente.setNome(registerRequest.getNome());
        nuovoUtente.setCognome(registerRequest.getCognome());
        nuovoUtente.setEmail(registerRequest.getEmail());
        nuovoUtente.setPassword(hashedPassword);
        nuovoUtente.setRuolo(Ruolo.UTENTE);
        nuovoUtente.setDataRegistrazione(LocalDateTime.now());

        Utente utenteSalvato = utenteRepository.save(nuovoUtente);

        RegisterResponse registerResponse = new RegisterResponse(
            utenteSalvato.getNome(),
            utenteSalvato.getCognome(),
            utenteSalvato.getEmail(),
            utenteSalvato.getRuolo()
        );

        return registerResponse;
    }




    public LoginResponse getLoginResponse(String email){
        Utente utente = utenteRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Utente non trovato."));
        
        return new LoginResponse(
        utente.getId(),
        utente.getNome(),
        utente.getCognome(),
        utente.getEmail(),
        utente.getRuolo(),
        null
        );
    }
}
