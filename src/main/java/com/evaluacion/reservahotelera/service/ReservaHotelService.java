package com.evaluacion.reservahotelera.service;

import com.evaluacion.reservahotelera.model.ReservaHotel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReservaHotelService {

    private final List<ReservaHotel> reservas = new ArrayList<>();
    private final List<Integer> habitaciones = new ArrayList<>();
    private final List<Integer> habitacionesOcupadasFijas = new ArrayList<>();

    public ReservaHotelService() {
        habitaciones.add(101);
        habitaciones.add(102);
        habitaciones.add(103);
        habitaciones.add(104);
        habitaciones.add(105);
        habitaciones.add(106);
        habitaciones.add(107);
        habitaciones.add(108);
        habitaciones.add(109);
        habitaciones.add(110);
        habitaciones.add(111);
        habitaciones.add(112);
        habitaciones.add(113);
        habitaciones.add(114);
        habitaciones.add(115);

        reservas.add(new ReservaHotel(1, "Ana Lopez", 101, LocalDate.of(2026, 3, 26), LocalDate.of(2026, 3, 29), "CONFIRMADA"));
        reservas.add(new ReservaHotel(2, "Carlos Mena", 102, LocalDate.of(2026, 3, 27), LocalDate.of(2026, 3, 28), "PENDIENTE"));
        reservas.add(new ReservaHotel(3, "Daniela Rojas", 103, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 3), "CANCELADA"));
        reservas.add(new ReservaHotel(4, "Marco Diaz", 104, LocalDate.of(2026, 4, 2), LocalDate.of(2026, 4, 5), "CONFIRMADA"));
        reservas.add(new ReservaHotel(5, "Camila Ortiz", 105, LocalDate.of(2026, 4, 6), LocalDate.of(2026, 4, 8), "PENDIENTE"));
        reservas.add(new ReservaHotel(6, "Javiera Soto", 106, LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 11), "CONFIRMADA"));
        reservas.add(new ReservaHotel(7, "Luis Perez", 107, LocalDate.of(2026, 4, 12), LocalDate.of(2026, 4, 15), "CONFIRMADA"));
        reservas.add(new ReservaHotel(8, "Rocio Mora", 108, LocalDate.of(2026, 4, 16), LocalDate.of(2026, 4, 18), "PENDIENTE"));

        habitacionesOcupadasFijas.add(109);
        habitacionesOcupadasFijas.add(110);
        habitacionesOcupadasFijas.add(115);
    }

    public List<ReservaHotel> listarReservas() {
        return reservas;
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
        ReservaHotel reservaSimulada = new ReservaHotel(id, nombreHuesped, numeroHabitacion, fechaEntrada, fechaSalida, "CONFIRMADA");
        return Map.of(
                "ok", true,
                "modalidad", "Online",
                "mensaje", "Reserva creada correctamente.",
                "data", reservaSimulada
        );
    }

    public Map<String, Object> cancelarReserva(int id) {
        ReservaHotel reserva = null;
        for (ReservaHotel item : reservas) {
            if (item.getId() == id) {
                reserva = item;
                break;
            }
        }

        if (reserva == null) {
            return Map.of("ok", false, "error", "No existe una reserva con ese id");
        }
        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            return Map.of("ok", false, "error", "La reserva ya se encuentra cancelada");
        }

        reserva.setEstado("CANCELADA");
        return Map.of("ok", true, "data", reserva);
    }

    public List<Map<String, Object>> consultarDisponibilidad(LocalDate fecha) {
        List<Integer> habitacionesOcupadas = new ArrayList<>(habitacionesOcupadasFijas);
        for (ReservaHotel reserva : reservas) {
            if (!"CANCELADA".equalsIgnoreCase(reserva.getEstado())
                    && !fecha.isBefore(reserva.getFechaEntrada())
                    && !fecha.isAfter(reserva.getFechaSalida().minusDays(1))
                    && !habitacionesOcupadas.contains(reserva.getNumeroHabitacion())) {
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
