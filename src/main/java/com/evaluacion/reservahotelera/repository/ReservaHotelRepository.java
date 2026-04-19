package com.evaluacion.reservahotelera.repository;

import com.evaluacion.reservahotelera.model.Habitacion;
import com.evaluacion.reservahotelera.model.ReservaHotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaHotelRepository extends JpaRepository<ReservaHotel, Integer> {

	List<ReservaHotel> findAllByOrderByIdAsc();

	List<ReservaHotel> findAllByEstadoIgnoreCaseNotAndFechaEntradaLessThanEqualAndFechaSalidaGreaterThan(
			String estado,
			LocalDate fechaEntrada,
			LocalDate fechaSalida
	);

	boolean existsByHabitacionAndEstadoIgnoreCaseNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
			Habitacion habitacion,
			String estado,
			LocalDate fechaSalida,
			LocalDate fechaEntrada
	);

	boolean existsByHabitacionAndEstadoIgnoreCaseNotAndIdNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
			Habitacion habitacion,
			String estado,
			Integer id,
			LocalDate fechaSalida,
			LocalDate fechaEntrada
	);

	boolean existsByHabitacionAndEstadoIgnoreCaseNot(Habitacion habitacion, String estado);
}
