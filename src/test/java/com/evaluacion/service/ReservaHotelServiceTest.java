package com.evaluacion.service;

import com.evaluacion.reservahotelera.model.Habitacion;
import com.evaluacion.reservahotelera.model.Huesped;
import com.evaluacion.reservahotelera.model.Pago;
import com.evaluacion.reservahotelera.model.ReservaHotel;
import com.evaluacion.reservahotelera.repository.HabitacionRepository;
import com.evaluacion.reservahotelera.repository.HuespedRepository;
import com.evaluacion.reservahotelera.repository.PagoRepository;
import com.evaluacion.reservahotelera.repository.ReservaHotelRepository;
import com.evaluacion.reservahotelera.service.ReservaHotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservaHotelServiceTest {

    private ReservaHotelRepository reservaHotelRepository;
    private HuespedRepository huespedRepository;
    private HabitacionRepository habitacionRepository;
    private PagoRepository pagoRepository;
    private ReservaHotelService service;

    @BeforeEach
    void setUp() {
        reservaHotelRepository = mock(ReservaHotelRepository.class);
        huespedRepository = mock(HuespedRepository.class);
        habitacionRepository = mock(HabitacionRepository.class);
        pagoRepository = mock(PagoRepository.class);

        service = new ReservaHotelService(
                reservaHotelRepository,
                huespedRepository,
                habitacionRepository,
                pagoRepository
        );
    }

    @Test
    void listar_ok() {
        List<ReservaHotel> esperado = List.of(new ReservaHotel());
        when(reservaHotelRepository.findAllByOrderByIdAsc()).thenReturn(esperado);

        List<ReservaHotel> resultado = service.listarReservas();

        assertEquals(esperado, resultado);
    }

    @Test
    void crear_ok() {
        LocalDate entrada = LocalDate.parse("2026-05-10");
        LocalDate salida = LocalDate.parse("2026-05-12");

        Huesped huesped = new Huesped(1, "Bryan");
        Habitacion habitacion = new Habitacion(2, 101, 2);
        ReservaHotel guardada = new ReservaHotel(10, huesped, habitacion, entrada, salida, "CONFIRMADA");

        when(reservaHotelRepository.existsById(10)).thenReturn(false);
        when(huespedRepository.findByNombreIgnoreCase("Bryan")).thenReturn(Optional.of(huesped));
        when(habitacionRepository.findByNumeroHabitacion(101)).thenReturn(Optional.of(habitacion));
        when(reservaHotelRepository.existsByHabitacionAndEstadoIgnoreCaseNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
                habitacion, "CANCELADA", salida, entrada)).thenReturn(false);
        when(reservaHotelRepository.save(any(ReservaHotel.class))).thenReturn(guardada);
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> resultado = service.crearReserva(
                10,
                "Bryan",
                101,
                entrada,
                salida,
                null,
                ""
        );

        assertEquals(true, resultado.get("ok"));
        assertEquals("Online", resultado.get("modalidad"));

        ArgumentCaptor<Pago> captor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoRepository).save(captor.capture());
        assertEquals(BigDecimal.ZERO, captor.getValue().getMonto());
        assertEquals("SIN_DEFINIR", captor.getValue().getMetodo());
    }

    @Test
    void actualizar_ok() {
        Huesped huesped = new Huesped(1, "Ana");
        Habitacion antigua = new Habitacion(1, 101, 2);
        Habitacion nueva = new Habitacion(2, 202, 2);
        ReservaHotel reserva = new ReservaHotel(
                30,
                huesped,
                antigua,
                LocalDate.parse("2026-06-01"),
                LocalDate.parse("2026-06-03"),
                "CONFIRMADA"
        );

        when(reservaHotelRepository.findById(30)).thenReturn(Optional.of(reserva));
        when(habitacionRepository.findByNumeroHabitacion(202)).thenReturn(Optional.of(nueva));
        when(reservaHotelRepository.existsByHabitacionAndEstadoIgnoreCaseNotAndIdNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
                nueva,
                "CANCELADA",
                30,
                LocalDate.parse("2026-06-05"),
                LocalDate.parse("2026-06-02")
        )).thenReturn(false);
        when(reservaHotelRepository.save(any(ReservaHotel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> resultado = service.actualizarReserva(
                30,
                202,
                LocalDate.parse("2026-06-02"),
                LocalDate.parse("2026-06-05")
        );

        assertEquals(true, resultado.get("ok"));
        assertEquals(202, reserva.getNumeroHabitacion());
    }

    @Test
    void eliminar_ok() {
        when(reservaHotelRepository.existsById(40)).thenReturn(true);
        when(pagoRepository.deleteByReservaId(40)).thenReturn(1);
        when(reservaHotelRepository.deleteDirectById(40)).thenReturn(1);

        Map<String, Object> resultado = service.eliminarReserva(40);

        assertEquals(true, resultado.get("ok"));
        verify(pagoRepository).deleteByReservaId(40);
        verify(reservaHotelRepository).deleteDirectById(40);
    }

    @Test
    void cancelar_ok() {
        Huesped huesped = new Huesped(1, "Luis");
        Habitacion habitacion = new Habitacion(1, 111, 2);
        ReservaHotel reserva = new ReservaHotel(
                80,
                huesped,
                habitacion,
                LocalDate.parse("2026-07-10"),
                LocalDate.parse("2026-07-12"),
                "CONFIRMADA"
        );
        Pago pago = new Pago(7, new BigDecimal("50000"), "TARJETA", "PENDIENTE");

        when(reservaHotelRepository.findById(80)).thenReturn(Optional.of(reserva));
        when(reservaHotelRepository.save(any(ReservaHotel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(pagoRepository.findByReservaId(80)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> resultado = service.cancelarReserva(80);

        assertEquals(true, resultado.get("ok"));
        assertEquals("CANCELADA", reserva.getEstado());
        assertEquals("ANULADO", pago.getEstado());
    }

    @Test
    void disponibilidad_ok() {
        LocalDate fecha = LocalDate.parse("2026-10-10");

        Habitacion h1 = new Habitacion(1, 101, 2);
        Habitacion h2 = new Habitacion(2, 102, 2);

        Huesped huesped = new Huesped(1, "Bryan");
        ReservaHotel ocupada = new ReservaHotel(
                90,
                huesped,
                h1,
                LocalDate.parse("2026-10-09"),
                LocalDate.parse("2026-10-11"),
                "CONFIRMADA"
        );

        when(habitacionRepository.findAllByOrderByNumeroHabitacionAsc()).thenReturn(List.of(h1, h2));
        when(reservaHotelRepository.findAllByEstadoIgnoreCaseNotAndFechaEntradaLessThanEqualAndFechaSalidaGreaterThan(
                "CANCELADA", fecha, fecha)).thenReturn(List.of(ocupada));

        List<Map<String, Object>> resultado = service.consultarDisponibilidad(fecha);

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(m -> m.get("numeroHabitacion").equals(101) && m.get("estado").equals("NO_DISPONIBLE")));
        assertTrue(resultado.stream().anyMatch(m -> m.get("numeroHabitacion").equals(102) && m.get("estado").equals("DISPONIBLE")));
    }

    @Test
    void buscar_noExiste() {
        when(reservaHotelRepository.findById(1)).thenReturn(Optional.empty());

        Map<String, Object> resultado = service.buscarReservaPorId(1);

        assertEquals(false, resultado.get("ok"));
    }

    @Test
    void crear_nombreVacio() {
        Map<String, Object> resultado = service.crearReserva(
                1,
                " ",
                101,
                LocalDate.parse("2026-05-10"),
                LocalDate.parse("2026-05-12"),
                null,
                null
        );

        assertEquals(false, resultado.get("ok"));
    }

    @Test
    void actualizar_noExiste() {
        when(reservaHotelRepository.findById(99)).thenReturn(Optional.empty());

        Map<String, Object> resultado = service.actualizarReserva(99, null, null, null);

        assertEquals(false, resultado.get("ok"));
    }
}
