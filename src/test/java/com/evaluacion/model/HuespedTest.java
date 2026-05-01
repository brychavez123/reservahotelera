package com.evaluacion.model;

import com.evaluacion.reservahotelera.model.Huesped;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HuespedTest {

    @Test
    void constructor_ok() {
        Huesped huesped = new Huesped(1, "Bryan");

        assertEquals(1, huesped.getId());
        assertEquals("Bryan", huesped.getNombre());
    }

    @Test
    void setters_ok() {
        Huesped huesped = new Huesped();

        huesped.setId(2);
        huesped.setNombre("Camila");

        assertEquals(2, huesped.getId());
        assertEquals("Camila", huesped.getNombre());
    }
}