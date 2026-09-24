package com.manuel.ticketflow.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manuel.ticketflow.models.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long>{
    boolean existsByEmail(String email);
    Optional<Utente> findByEmail(String email);
}
