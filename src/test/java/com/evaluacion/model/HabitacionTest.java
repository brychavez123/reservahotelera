package com.evaluacion.model;

import com.evaluacion.reservahotelera.model.Habitacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HabitacionTest {

    @Test
    void constructor_ok() {
        Habitacion habitacion = new Habitacion(1, 101, 2);

        assertEquals(1, habitacion.getId());
        assertEquals(101, habitacion.getNumeroHabitacion());
        assertEquals(2, habitacion.getCapacidadPersonas());
    }

    @Test
    void setters_ok() {
        Habitacion habitacion = new Habitacion();

        habitacion.setId(2);
        habitacion.setNumeroHabitacion(202);
        habitacion.setCapacidadPersonas(4);

        assertEquals(2, habitacion.getId());
        assertEquals(202, habitacion.getNumeroHabitacion());
        assertEquals(4, habitacion.getCapacidadPersonas());
    }
}