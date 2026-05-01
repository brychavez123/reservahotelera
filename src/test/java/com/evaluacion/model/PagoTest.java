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

class PagoTest {

    @Test
    void constructor_ok() {
        Pago pago = new Pago(1, new BigDecimal("120000"), "TARJETA", "PENDIENTE");

        assertEquals(1, pago.getId());
        assertEquals(new BigDecimal("120000"), pago.getMonto());
        assertEquals("TARJETA", pago.getMetodo());
        assertEquals("PENDIENTE", pago.getEstado());
    }

    @Test
    void setters_ok() {
        Pago pago = new Pago();

        Huesped huesped = new Huesped(10, "Bryan");
        Habitacion habitacion = new Habitacion(7, 301, 2);
        ReservaHotel reserva = new ReservaHotel(20, huesped, habitacion,
                LocalDate.parse("2026-05-10"), LocalDate.parse("2026-05-12"), "CONFIRMADA");

        pago.setId(2);
        pago.setMonto(new BigDecimal("80000"));
        pago.setMetodo("TRANSFERENCIA");
        pago.setEstado("ANULADO");
        pago.setHuesped(huesped);
        pago.setReserva(reserva);

        assertEquals(2, pago.getId());
        assertEquals(new BigDecimal("80000"), pago.getMonto());
        assertEquals("TRANSFERENCIA", pago.getMetodo());
        assertEquals("ANULADO", pago.getEstado());
        assertEquals(huesped, pago.getHuesped());
        assertEquals(reserva, pago.getReserva());
    }

    @Test
    void idsTransient_ok() {
        Pago pago = new Pago();

        assertNull(pago.getHuespedId());
        assertNull(pago.getReservaId());

        Huesped huesped = new Huesped(3, "Ana");
        Habitacion habitacion = new Habitacion(9, 401, 3);
        ReservaHotel reserva = new ReservaHotel(15, huesped, habitacion,
                LocalDate.parse("2026-06-01"), LocalDate.parse("2026-06-03"), "CONFIRMADA");

        pago.setHuesped(huesped);
        pago.setReserva(reserva);

        assertEquals(3, pago.getHuespedId());
        assertEquals(15, pago.getReservaId());
    }
}