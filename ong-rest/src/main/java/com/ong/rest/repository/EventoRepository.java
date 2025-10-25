package com.ong.rest.repository;

import com.ong.rest.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    // Si querés métodos custom, después se agregan
}
