package com.evaluacion.repository;

import com.evaluacion.reservahotelera.ReservahoteleraApplication;
import com.evaluacion.reservahotelera.model.Habitacion;
import com.evaluacion.reservahotelera.model.Huesped;
import com.evaluacion.reservahotelera.model.Pago;
import com.evaluacion.reservahotelera.model.ReservaHotel;
import com.evaluacion.reservahotelera.repository.HabitacionRepository;
import com.evaluacion.reservahotelera.repository.HuespedRepository;
import com.evaluacion.reservahotelera.repository.PagoRepository;
import com.evaluacion.reservahotelera.repository.ReservaHotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ReservahoteleraApplication.class)
@ActiveProfiles("test")
@Transactional
class PagoRepositoryTest {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ReservaHotelRepository reservaHotelRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @BeforeEach
    void limpiar() {
        pagoRepository.deleteAll();
        reservaHotelRepository.deleteAll();
        huespedRepository.deleteAll();
        habitacionRepository.deleteAll();
    }

    @Test
    void buscarPorReserva_ok() {
        ReservaHotel reserva = crearReserva(50, 5001);

        Pago pago = new Pago(null, new BigDecimal("100000"), "TARJETA", "PENDIENTE");
        pago.setHuesped(reserva.getHuesped());
        pago.setReserva(reserva);
        pagoRepository.save(pago);

        boolean existe = pagoRepository.findByReservaId(50).isPresent();

        assertTrue(existe);
    }

    @Test
    void deletePorReserva_ok() {
        ReservaHotel reserva = crearReserva(60, 6001);

        Pago pago = new Pago(null, new BigDecimal("80000"), "EFECTIVO", "PENDIENTE");
        pago.setHuesped(reserva.getHuesped());
        pago.setReserva(reserva);
        pagoRepository.save(pago);

        int borrados = pagoRepository.deleteByReservaId(60);

        assertEquals(1, borrados);
        assertEquals(false, pagoRepository.findByReservaId(60).isPresent());
    }

    private ReservaHotel crearReserva(int idReserva, int numeroHabitacion) {
        Huesped huesped = huespedRepository.save(new Huesped(null, "Huesped " + idReserva));
        Habitacion habitacion = habitacionRepository.save(new Habitacion(null, numeroHabitacion, 2));

        ReservaHotel reserva = new ReservaHotel(
                idReserva,
                huesped,
                habitacion,
                LocalDate.parse("2026-05-10"),
                LocalDate.parse("2026-05-12"),
                "CONFIRMADA"
        );

        return reservaHotelRepository.save(reserva);
    }
}
