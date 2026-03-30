package com.evaluacion.reservahotelera.controller;

import com.evaluacion.reservahotelera.service.ReservaHotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/hoteles")
public class ReservaHotelController {

    private final ReservaHotelService reservaHotelService;

    public ReservaHotelController(ReservaHotelService reservaHotelService) {
        this.reservaHotelService = reservaHotelService;
    }

    @GetMapping("/reservas")
    public ResponseEntity<?> listarReservas() {
        return ResponseEntity.ok(reservaHotelService.listarReservas());
    }

    @GetMapping("/reservas/crear")
    public ResponseEntity<?> crearReserva(
            @RequestParam int id,
            @RequestParam String nombreHuesped,
            @RequestParam int numeroHabitacion,
            @RequestParam String fechaEntrada,
            @RequestParam String fechaSalida
    ) {
        LocalDate fechaEntradaParseada;
        LocalDate fechaSalidaParseada;
        try {
            fechaEntradaParseada = LocalDate.parse(fechaEntrada);
            fechaSalidaParseada = LocalDate.parse(fechaSalida);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "ok", false,
                    "error", "Ponga una fecha correcta con formato yyyy-MM-dd"
            ));
        }

        Map<String, Object> resultado = reservaHotelService.crearReserva(id, nombreHuesped, numeroHabitacion, fechaEntradaParseada, fechaSalidaParseada);
        if (Boolean.FALSE.equals(resultado.get("ok"))) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/reservas/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable int id) {
        Map<String, Object> resultado = reservaHotelService.cancelarReserva(id);
        if (Boolean.FALSE.equals(resultado.get("ok"))) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<?> consultarDisponibilidad(
            @RequestParam String fecha
    ) {
        LocalDate fechaParseada;
        try {
            fechaParseada = LocalDate.parse(fecha);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "ok", false,
                    "error", "Ponga una fecha correcta con formato yyyy-MM-dd"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "ok", true,
                "fecha", fechaParseada,
                "habitaciones", reservaHotelService.consultarDisponibilidad(fechaParseada)
        ));
    }
}
