package com.ong.kafka_producer.repository.transferencia_donacion;

import com.ong.kafka_producer.entity.transferencia_donacion.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    Optional<Inventario> findByCategoriaAndDescripcion(Inventario.Categoria categoria, String descripcion);
}




