package com.evaluacion.reservahotelera.repository;

import com.evaluacion.reservahotelera.model.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HuespedRepository extends JpaRepository<Huesped, Integer> {
    Optional<Huesped> findByNombreIgnoreCase(String nombre);
}
