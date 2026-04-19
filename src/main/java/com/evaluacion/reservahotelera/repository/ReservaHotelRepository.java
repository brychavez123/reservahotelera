package com.evaluacion.reservahotelera.repository;

import com.evaluacion.reservahotelera.model.Habitacion;
import com.evaluacion.reservahotelera.model.ReservaHotel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

	@Modifying
	@Transactional
	@Query("delete from ReservaHotel r where r.id = :id")
	int deleteDirectById(@Param("id") Integer id);
}
