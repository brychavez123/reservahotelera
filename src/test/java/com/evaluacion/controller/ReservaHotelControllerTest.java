package com.evaluacion.controller;
import com.evaluacion.reservahotelera.GlobalExceptionHandler;
import com.evaluacion.reservahotelera.controller.ReservaHotelController;
import com.evaluacion.reservahotelera.service.ReservaHotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservaHotelControllerTest {

    private MockMvc mockMvc;
    private ReservaHotelService reservaHotelService;

    @BeforeEach
    void setUp() {
        reservaHotelService = mock(ReservaHotelService.class);
        ReservaHotelController controller = new ReservaHotelController(reservaHotelService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
        void listar_ok() throws Exception {
        when(reservaHotelService.listarReservas()).thenReturn(List.of());

        mockMvc.perform(get("/api/hoteles/reservas"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(reservaHotelService).listarReservas();
    }

    @Test
        void crear_ok() throws Exception {
        when(reservaHotelService.crearReserva(
                1,
                "Bryan",
                101,
                LocalDate.parse("2026-05-10"),
                LocalDate.parse("2026-05-12"),
                new BigDecimal("120000"),
                "TARJETA"
        )).thenReturn(Map.of("ok", true, "mensaje", "Reserva creada correctamente."));

        mockMvc.perform(post("/api/hoteles/reservas")
                        .param("id", "1")
                        .param("nombreHuesped", "Bryan")
                        .param("numeroHabitacion", "101")
                        .param("fechaEntrada", "2026-05-10")
                        .param("fechaSalida", "2026-05-12")
                        .param("montoPago", "120000")
                        .param("metodoPago", "TARJETA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));

        verify(reservaHotelService).crearReserva(
                1,
                "Bryan",
                101,
                LocalDate.parse("2026-05-10"),
                LocalDate.parse("2026-05-12"),
                new BigDecimal("120000"),
                "TARJETA"
        );
    }

    @Test
        void buscar_noExiste() throws Exception {
        when(reservaHotelService.buscarReservaPorId(999))
                .thenReturn(Map.of("ok", false, "error", "No existe una reserva con ese id"));

        mockMvc.perform(get("/api/hoteles/reservas/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.error").value("No existe una reserva con ese id"));

        verify(reservaHotelService).buscarReservaPorId(999);
    }

    @Test
        void crear_fechaInvalida() throws Exception {
        mockMvc.perform(post("/api/hoteles/reservas")
                        .param("id", "1")
                        .param("nombreHuesped", "Bryan")
                        .param("numeroHabitacion", "101")
                        .param("fechaEntrada", "10-05-2026")
                        .param("fechaSalida", "2026-05-12"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.error").value("Ponga una fecha correcta con formato yyyy-MM-dd"));

        verifyNoInteractions(reservaHotelService);
    }

    @Test
        void actualizar_fechaInvalida() throws Exception {
        mockMvc.perform(put("/api/hoteles/reservas/1")
                        .param("fechaEntrada", "2026/05/10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.error").value("Ponga una fecha correcta con formato yyyy-MM-dd"));

        verifyNoInteractions(reservaHotelService);
    }

    @Test
        void eliminar_noExiste() throws Exception {
        when(reservaHotelService.eliminarReserva(10))
                .thenReturn(Map.of("ok", false, "error", "No existe una reserva con ese id"));

        mockMvc.perform(delete("/api/hoteles/reservas/10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.error").value("No existe una reserva con ese id"));

        verify(reservaHotelService).eliminarReserva(10);
    }
}