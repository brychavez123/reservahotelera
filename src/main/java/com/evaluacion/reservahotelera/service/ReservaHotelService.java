package com.evaluacion.reservahotelera.service;

import com.evaluacion.reservahotelera.model.ReservaHotel;
import com.evaluacion.reservahotelera.repository.ReservaHotelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ReservaHotelService {

    private final ReservaHotelRepository reservaHotelRepository;

    public ReservaHotelService(ReservaHotelRepository reservaHotelRepository) {
        this.reservaHotelRepository = reservaHotelRepository;
    }

    public List<ReservaHotel> listarReservas() {
        return reservaHotelRepository.findAllByOrderByIdAsc();
    }

    public Map<String, Object> crearReserva(int id, String nombreHuesped, int numeroHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
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

        ReservaHotel reserva = new ReservaHotel(id, nombreHuesped, numeroHabitacion, fechaEntrada, fechaSalida, "CONFIRMADA");
        ReservaHotel guardada = reservaHotelRepository.save(reserva);
        return Map.of(
                "ok", true,
                "modalidad", "Online",
                "mensaje", "Reserva creada correctamente.",
                "data", guardada
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
        return Map.of("ok", true, "data", actualizada);
    }

    public List<Map<String, Object>> consultarDisponibilidad(LocalDate fecha) {
        List<ReservaHotel> todasLasReservasOrdenadas = reservaHotelRepository.findAllByOrderByNumeroHabitacionAsc();
        List<ReservaHotel> reservasOcupadas = reservaHotelRepository
                .findAllByEstadoIgnoreCaseNotAndFechaEntradaLessThanEqualAndFechaSalidaGreaterThanOrderByNumeroHabitacionAsc(
                        "CANCELADA",
                        fecha,
                        fecha
                );

        Set<Integer> habitaciones = new LinkedHashSet<>();
        for (ReservaHotel reserva : todasLasReservasOrdenadas) {
            habitaciones.add(reserva.getNumeroHabitacion());
        }

        Set<Integer> habitacionesOcupadas = new LinkedHashSet<>();
        for (ReservaHotel reserva : reservasOcupadas) {
            habitacionesOcupadas.add(reserva.getNumeroHabitacion());
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
