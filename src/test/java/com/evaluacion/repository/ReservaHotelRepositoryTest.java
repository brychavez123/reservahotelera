package com.evaluacion.repository;

import com.evaluacion.reservahotelera.ReservahoteleraApplication;
import com.evaluacion.reservahotelera.model.Habitacion;
import com.evaluacion.reservahotelera.model.Huesped;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ReservahoteleraApplication.class)
@ActiveProfiles("test")
@Transactional
class ReservaHotelRepositoryTest {

    @Autowired
    private ReservaHotelRepository reservaHotelRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @BeforeEach
    void limpiar() {
        pagoRepository.deleteAll();
        reservaHotelRepository.deleteAll();
        huespedRepository.deleteAll();
        habitacionRepository.deleteAll();
    }

    @Test
    void listarOrdenado_ok() {
        crearReserva(2, 7002, LocalDate.parse("2026-07-10"), LocalDate.parse("2026-07-12"));
        crearReserva(1, 7001, LocalDate.parse("2026-07-01"), LocalDate.parse("2026-07-03"));

        List<ReservaHotel> reservas = reservaHotelRepository.findAllByOrderByIdAsc();

        assertEquals(2, reservas.size());
        assertEquals(1, reservas.get(0).getId());
        assertEquals(2, reservas.get(1).getId());
    }

    @Test
    void existeTraslape_ok() {
        ReservaHotel existente = crearReserva(10, 7010,
                LocalDate.parse("2026-08-10"), LocalDate.parse("2026-08-15"));

        boolean existe = reservaHotelRepository
                .existsByHabitacionAndEstadoIgnoreCaseNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
                        existente.getHabitacion(),
                        "CANCELADA",
                        LocalDate.parse("2026-08-12"),
                        LocalDate.parse("2026-08-11")
                );

        assertTrue(existe);
    }

    @Test
    void deleteDirecto_ok() {
        crearReserva(20, 7020, LocalDate.parse("2026-09-01"), LocalDate.parse("2026-09-03"));

        int borrados = reservaHotelRepository.deleteDirectById(20);

        assertEquals(1, borrados);
        assertEquals(false, reservaHotelRepository.existsById(20));
    }

    private ReservaHotel crearReserva(int idReserva, int numeroHabitacion, LocalDate entrada, LocalDate salida) {
        Huesped huesped = huespedRepository.save(new Huesped(null, "Huesped " + idReserva));
        Habitacion habitacion = habitacionRepository.save(new Habitacion(null, numeroHabitacion, 2));

        ReservaHotel reserva = new ReservaHotel(idReserva, huesped, habitacion, entrada, salida, "CONFIRMADA");
        return reservaHotelRepository.save(reserva);
    }
}
