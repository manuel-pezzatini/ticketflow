package com.manuel.ticketflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manuel.ticketflow.models.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
