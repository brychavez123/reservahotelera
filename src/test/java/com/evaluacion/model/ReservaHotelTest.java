package com.evaluacion.model;

import com.evaluacion.reservahotelera.model.Habitacion;
import com.evaluacion.reservahotelera.model.Huesped;
import com.evaluacion.reservahotelera.model.Pago;
import com.evaluacion.reservahotelera.model.ReservaHotel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReservaHotelTest {

    @Test
    void constructor_ok() {
        Huesped huesped = new Huesped(1, "Bryan");
        Habitacion habitacion = new Habitacion(2, 101, 2);

        ReservaHotel reserva = new ReservaHotel(
                10,
                huesped,
                habitacion,
                LocalDate.parse("2026-05-10"),
                LocalDate.parse("2026-05-12"),
                "CONFIRMADA"
        );

        assertEquals(10, reserva.getId());
        assertEquals(huesped, reserva.getHuesped());
        assertEquals(habitacion, reserva.getHabitacion());
        assertEquals(LocalDate.parse("2026-05-10"), reserva.getFechaEntrada());
        assertEquals(LocalDate.parse("2026-05-12"), reserva.getFechaSalida());
        assertEquals("Bryan", reserva.getNombreHuesped());
        assertEquals(101, reserva.getNumeroHabitacion());
        assertEquals("CONFIRMADA", reserva.getEstado());
    }

    @Test
    void constructor_conNulls_ok() {
        ReservaHotel reserva = new ReservaHotel(
                11,
                null,
                null,
                LocalDate.parse("2026-07-01"),
                LocalDate.parse("2026-07-03"),
                "CONFIRMADA"
        );

        assertNull(reserva.getNombreHuesped());
        assertNull(reserva.getNumeroHabitacion());
    }

    @Test
    void setters_ok() {
        ReservaHotel reserva = new ReservaHotel();
        Huesped huesped = new Huesped(4, "Ana");
        Habitacion habitacion = new Habitacion(8, 202, 3);
        Pago pago = new Pago(6, new BigDecimal("90000"), "EFECTIVO", "PENDIENTE");

        reserva.setId(20);
        reserva.setHuesped(huesped);
        reserva.setHabitacion(habitacion);
        reserva.setFechaEntrada(LocalDate.parse("2026-08-10"));
        reserva.setFechaSalida(LocalDate.parse("2026-08-12"));
        reserva.setNombreHuesped("Ana");
        reserva.setNumeroHabitacion(202);
        reserva.setEstado("CONFIRMADA");
        reserva.setPago(pago);

        assertEquals(20, reserva.getId());
        assertEquals(huesped, reserva.getHuesped());
        assertEquals(habitacion, reserva.getHabitacion());
        assertEquals(LocalDate.parse("2026-08-10"), reserva.getFechaEntrada());
        assertEquals(LocalDate.parse("2026-08-12"), reserva.getFechaSalida());
        assertEquals("Ana", reserva.getNombreHuesped());
        assertEquals(202, reserva.getNumeroHabitacion());
        assertEquals("CONFIRMADA", reserva.getEstado());
        assertEquals(pago, reserva.getPago());
    }

    @Test
    void idsTransient_ok() {
        ReservaHotel reserva = new ReservaHotel();

        assertNull(reserva.getHuespedId());
        assertNull(reserva.getHabitacionId());
        assertNull(reserva.getPagoId());

        Huesped huesped = new Huesped(9, "Luis");
        Habitacion habitacion = new Habitacion(10, 505, 2);
        Pago pago = new Pago(12, new BigDecimal("150000"), "TARJETA", "PENDIENTE");

        reserva.setHuesped(huesped);
        reserva.setHabitacion(habitacion);
        reserva.setPago(pago);

        assertEquals(9, reserva.getHuespedId());
        assertEquals(10, reserva.getHabitacionId());
        assertEquals(12, reserva.getPagoId());
    }
}