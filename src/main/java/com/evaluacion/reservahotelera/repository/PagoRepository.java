package com.evaluacion.reservahotelera.repository;

import com.evaluacion.reservahotelera.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
	Optional<Pago> findByReservaId(Integer reservaId);

	@Modifying
	@Transactional
	@Query("delete from Pago p where p.reserva.id = :reservaId")
	int deleteByReservaId(@Param("reservaId") Integer reservaId);
}
