package com.evaluacion.reservahotelera.repository;

import com.evaluacion.reservahotelera.model.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {
    Optional<Habitacion> findByNumeroHabitacion(Integer numeroHabitacion);

    List<Habitacion> findAllByOrderByNumeroHabitacionAsc();
}
