package com.manuel.ticketflow.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.manuel.ticketflow.models.Utente;
import com.manuel.ticketflow.repositories.UtenteRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UtenteRepository utenteRepository;

    public CustomUserDetailsService(UtenteRepository utenteRepository){
        this.utenteRepository = utenteRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utente> utenteTrovato = utenteRepository.findByEmail(username);
        if(utenteTrovato.isEmpty()){
            throw new UsernameNotFoundException("Utente non trovato");
        }

        Utente utenteFinale = utenteTrovato.get();

        UserDetails userDetails = User
            .withUsername(utenteFinale.getEmail())
            .password(utenteFinale.getPassword())
            .roles(utenteFinale.getRuolo().name())
            .build();

        return userDetails;
    }

}
