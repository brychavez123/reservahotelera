package com.evaluacion.reservahotelera.repository;

import com.evaluacion.reservahotelera.model.ReservaHotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaHotelRepository extends JpaRepository<ReservaHotel, Integer> {

	List<ReservaHotel> findAllByOrderByIdAsc();

	List<ReservaHotel> findAllByOrderByNumeroHabitacionAsc();

	List<ReservaHotel> findAllByEstadoIgnoreCaseNotAndFechaEntradaLessThanEqualAndFechaSalidaGreaterThanOrderByNumeroHabitacionAsc(
			String estado,
			LocalDate fechaEntrada,
			LocalDate fechaSalida
	);
}
