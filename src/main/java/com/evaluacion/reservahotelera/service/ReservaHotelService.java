package com.evaluacion.reservahotelera.service;

import com.evaluacion.reservahotelera.model.Habitacion;
import com.evaluacion.reservahotelera.model.Huesped;
import com.evaluacion.reservahotelera.model.Pago;
import com.evaluacion.reservahotelera.model.ReservaHotel;
import com.evaluacion.reservahotelera.repository.HabitacionRepository;
import com.evaluacion.reservahotelera.repository.HuespedRepository;
import com.evaluacion.reservahotelera.repository.PagoRepository;
import com.evaluacion.reservahotelera.repository.ReservaHotelRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ReservaHotelService {

    private final ReservaHotelRepository reservaHotelRepository;
    private final HuespedRepository huespedRepository;
    private final HabitacionRepository habitacionRepository;
    private final PagoRepository pagoRepository;

    public ReservaHotelService(
            ReservaHotelRepository reservaHotelRepository,
            HuespedRepository huespedRepository,
            HabitacionRepository habitacionRepository,
            PagoRepository pagoRepository
    ) {
        this.reservaHotelRepository = reservaHotelRepository;
        this.huespedRepository = huespedRepository;
        this.habitacionRepository = habitacionRepository;
        this.pagoRepository = pagoRepository;
    }

    public List<ReservaHotel> listarReservas() {
        return reservaHotelRepository.findAllByOrderByIdAsc();
    }

    public Map<String, Object> crearReserva(
            int id,
            String nombreHuesped,
            int numeroHabitacion,
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            BigDecimal montoPago,
            String metodoPago
    ) {
        if (nombreHuesped == null || nombreHuesped.isBlank()) {
            return Map.of("ok", false, "error", "El nombre del huesped es obligatorio");
        }
        if (numeroHabitacion <= 0) {
            return Map.of("ok", false, "error", "El numero de habitacion debe ser mayor a 0");
        }
        if (fechaEntrada == null || fechaSalida == null) {
            return Map.of("ok", false, "error", "Las fechas de entrada y salida son obligatorias");
        }
        if (!fechaSalida.isAfter(fechaEntrada)) {
            return Map.of("ok", false, "error", "La fecha de salida debe ser posterior a la fecha de entrada");
        }
        if (reservaHotelRepository.existsById(id)) {
            return Map.of("ok", false, "error", "Ya existe una reserva con ese id");
        }

        String nombreNormalizado = nombreHuesped.trim();

        Huesped huesped = huespedRepository.findByNombreIgnoreCase(nombreNormalizado)
            .orElseGet(() -> huespedRepository.save(new Huesped(null, nombreNormalizado)));

        Habitacion habitacion = habitacionRepository.findByNumeroHabitacion(numeroHabitacion)
            .orElse(null);

        if (habitacion == null) {
            return Map.of(
                    "ok", false,
                    "error", "La habitacion no existe en el mantenedor. Registrela primero."
            );
        }

        boolean habitacionOcupadaEnRango = reservaHotelRepository
            .existsByHabitacionAndEstadoIgnoreCaseNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
                habitacion,
                "CANCELADA",
                fechaSalida,
                fechaEntrada
            );

        if (habitacionOcupadaEnRango) {
            return Map.of("ok", false, "error", "La habitacion ya esta reservada para ese rango de fechas");
        }

        ReservaHotel reserva = new ReservaHotel(id, huesped, habitacion, fechaEntrada, fechaSalida, "CONFIRMADA");
        reserva.setNombreHuesped(nombreNormalizado);
        reserva.setNumeroHabitacion(habitacion.getNumeroHabitacion());
        ReservaHotel guardada = reservaHotelRepository.save(reserva);

        BigDecimal monto = montoPago != null ? montoPago : BigDecimal.ZERO;
        String metodo = (metodoPago == null || metodoPago.isBlank()) ? "SIN_DEFINIR" : metodoPago.trim();

        Pago pago = new Pago();
        pago.setMonto(monto);
        pago.setMetodo(metodo);
        pago.setEstado("PENDIENTE");
        pago.setHuesped(huesped);
        pago.setReserva(guardada);
        Pago pagoGuardado = pagoRepository.save(pago);

        guardada.setPago(pagoGuardado);

        return Map.of(
                "ok", true,
                "modalidad", "Online",
                "mensaje", "Reserva creada correctamente.",
            "data", guardada,
            "pago", pagoGuardado
        );
    }

    public Map<String, Object> actualizarReserva(
            int id,
            Integer numeroHabitacion,
            LocalDate fechaEntrada,
            LocalDate fechaSalida
    ) {
        ReservaHotel reserva = reservaHotelRepository.findById(id).orElse(null);
        if (reserva == null) {
            return Map.of("ok", false, "error", "No existe una reserva con ese id");
        }
        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            return Map.of("ok", false, "error", "No se puede modificar una reserva cancelada");
        }
        if (numeroHabitacion == null && fechaEntrada == null && fechaSalida == null) {
            return Map.of("ok", false, "error", "Debe enviar al menos un dato para actualizar");
        }

        Habitacion habitacionFinal = reserva.getHabitacion();
        if (numeroHabitacion != null) {
            if (numeroHabitacion <= 0) {
                return Map.of("ok", false, "error", "El numero de habitacion debe ser mayor a 0");
            }

            Habitacion habitacionBuscada = habitacionRepository.findByNumeroHabitacion(numeroHabitacion)
                    .orElse(null);

            if (habitacionBuscada == null) {
                return Map.of("ok", false, "error", "La habitacion no existe en el mantenedor");
            }
            habitacionFinal = habitacionBuscada;
        }

        if (habitacionFinal == null) {
            return Map.of(
                    "ok", false,
                    "error", "La reserva no tiene habitacion asociada. Envie numeroHabitacion para corregirla"
            );
        }

        LocalDate fechaEntradaFinal = fechaEntrada != null ? fechaEntrada : reserva.getFechaEntrada();
        LocalDate fechaSalidaFinal = fechaSalida != null ? fechaSalida : reserva.getFechaSalida();

        if (fechaEntradaFinal == null || fechaSalidaFinal == null) {
            return Map.of("ok", false, "error", "La reserva debe tener fecha de entrada y salida");
        }
        if (!fechaSalidaFinal.isAfter(fechaEntradaFinal)) {
            return Map.of("ok", false, "error", "La fecha de salida debe ser posterior a la fecha de entrada");
        }

        boolean habitacionOcupadaEnRango = reservaHotelRepository
                .existsByHabitacionAndEstadoIgnoreCaseNotAndIdNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
                        habitacionFinal,
                        "CANCELADA",
                        id,
                        fechaSalidaFinal,
                        fechaEntradaFinal
                );

        if (habitacionOcupadaEnRango) {
            return Map.of("ok", false, "error", "La habitacion ya esta reservada para ese rango de fechas");
        }

        reserva.setHabitacion(habitacionFinal);
        reserva.setNumeroHabitacion(habitacionFinal.getNumeroHabitacion());
        reserva.setFechaEntrada(fechaEntradaFinal);
        reserva.setFechaSalida(fechaSalidaFinal);

        ReservaHotel actualizada = reservaHotelRepository.save(reserva);
        return Map.of(
                "ok", true,
                "mensaje", "Reserva actualizada correctamente.",
                "data", actualizada
        );
    }

    public Map<String, Object> cancelarReserva(int id) {
        ReservaHotel reserva = reservaHotelRepository.findById(id).orElse(null);
        if (reserva == null) {
            return Map.of("ok", false, "error", "No existe una reserva con ese id");
        }
        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            return Map.of("ok", false, "error", "La reserva ya se encuentra cancelada");
        }

        reserva.setEstado("CANCELADA");
        ReservaHotel actualizada = reservaHotelRepository.save(reserva);

        pagoRepository.findByReservaId(actualizada.getId()).ifPresent(pago -> {
            pago.setEstado("ANULADO");
            pagoRepository.save(pago);
            actualizada.setPago(pago);
        });

        return Map.of("ok", true, "data", actualizada);
    }

    public List<Map<String, Object>> consultarDisponibilidad(LocalDate fecha) {
        List<Habitacion> habitacionesRegistradas = habitacionRepository.findAllByOrderByNumeroHabitacionAsc();
        List<ReservaHotel> reservasOcupadas = reservaHotelRepository
                .findAllByEstadoIgnoreCaseNotAndFechaEntradaLessThanEqualAndFechaSalidaGreaterThan(
                        "CANCELADA",
                        fecha,
                        fecha
                );

        Set<Integer> habitaciones = new LinkedHashSet<>();
        for (Habitacion habitacion : habitacionesRegistradas) {
            if (habitacion.getNumeroHabitacion() != null) {
                habitaciones.add(habitacion.getNumeroHabitacion());
            }
        }

        if (habitaciones.isEmpty()) {
            List<ReservaHotel> reservas = reservaHotelRepository.findAllByOrderByIdAsc();
            for (ReservaHotel reserva : reservas) {
                if (reserva.getNumeroHabitacion() != null) {
                    habitaciones.add(reserva.getNumeroHabitacion());
                }
            }
        }

        Set<Integer> habitacionesOcupadas = new LinkedHashSet<>();
        for (ReservaHotel reserva : reservasOcupadas) {
            if (reserva.getNumeroHabitacion() != null) {
                habitacionesOcupadas.add(reserva.getNumeroHabitacion());
            }
        }

        List<Map<String, Object>> estadoHabitaciones = new ArrayList<>();
        for (Integer habitacion : habitaciones) {
            boolean ocupada = habitacionesOcupadas.contains(habitacion);
            estadoHabitaciones.add(Map.of(
                    "numeroHabitacion", habitacion,
                    "estado", ocupada ? "NO_DISPONIBLE" : "DISPONIBLE"
            ));
        }
        return estadoHabitaciones;
    }
}
