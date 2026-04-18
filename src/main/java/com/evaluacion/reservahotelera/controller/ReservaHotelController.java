package com.evaluacion.reservahotelera.controller;

import com.evaluacion.reservahotelera.service.ReservaHotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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

    @PostMapping("/reservas")
    public ResponseEntity<?> crearReserva(
            @RequestParam int id,
            @RequestParam String nombreHuesped,
            @RequestParam int numeroHabitacion,
            @RequestParam String fechaEntrada,
            @RequestParam String fechaSalida,
            @RequestParam(required = false) BigDecimal montoPago,
            @RequestParam(required = false) String metodoPago
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

        Map<String, Object> resultado = reservaHotelService.crearReserva(
            id,
            nombreHuesped,
            numeroHabitacion,
            fechaEntradaParseada,
            fechaSalidaParseada,
            montoPago,
            metodoPago
        );
        if (Boolean.FALSE.equals(resultado.get("ok"))) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }
}
