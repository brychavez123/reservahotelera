package com.evaluacion.reservahotelera.repository;

import com.evaluacion.reservahotelera.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
	Optional<Pago> findByReservaId(Integer reservaId);
}
