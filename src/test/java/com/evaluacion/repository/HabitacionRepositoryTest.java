package com.evaluacion.repository;

import com.evaluacion.reservahotelera.ReservahoteleraApplication;
import com.evaluacion.reservahotelera.model.Habitacion;
import com.evaluacion.reservahotelera.repository.HabitacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ReservahoteleraApplication.class)
@ActiveProfiles("test")
@Transactional
class HabitacionRepositoryTest {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @BeforeEach
    void limpiar() {
        habitacionRepository.deleteAll();
    }

    @Test
    void buscarPorNumero_ok() {
        habitacionRepository.save(new Habitacion(null, 1001, 2));

        boolean existe = habitacionRepository.findByNumeroHabitacion(1001).isPresent();

        assertTrue(existe);
    }

    @Test
    void ordenar_ok() {
        habitacionRepository.save(new Habitacion(null, 303, 2));
        habitacionRepository.save(new Habitacion(null, 101, 2));
        habitacionRepository.save(new Habitacion(null, 202, 2));

        List<Habitacion> resultado = habitacionRepository.findAllByOrderByNumeroHabitacionAsc();

        assertEquals(3, resultado.size());
        assertEquals(101, resultado.get(0).getNumeroHabitacion());
        assertEquals(202, resultado.get(1).getNumeroHabitacion());
        assertEquals(303, resultado.get(2).getNumeroHabitacion());
    }
}
