package com.ong.rest.repository;

import com.ong.rest.entity.FiltroEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiltroEventoRepository extends JpaRepository<FiltroEvento, Integer> {

    List<FiltroEvento> findByUsuarioId(Integer usuarioId);

    boolean existsByNombreAndUsuarioId(String nombre, Integer usuarioId);
}


