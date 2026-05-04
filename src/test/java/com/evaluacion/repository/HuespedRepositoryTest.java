package com.evaluacion.repository;

import com.evaluacion.reservahotelera.ReservahoteleraApplication;
import com.evaluacion.reservahotelera.model.Huesped;
import com.evaluacion.reservahotelera.repository.HuespedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ReservahoteleraApplication.class)
@ActiveProfiles("test")
@Transactional
class HuespedRepositoryTest {

    @Autowired
    private HuespedRepository huespedRepository;

    @BeforeEach
    void limpiar() {
        huespedRepository.deleteAll();
    }

    @Test
    void buscarIgnoreCase_ok() {
        huespedRepository.save(new Huesped(null, "Bryan"));

        Huesped resultado = huespedRepository.findByNombreIgnoreCase("brYaN").orElse(null);

        assertTrue(resultado != null);
        assertEquals("Bryan", resultado.getNombre());
    }

    @Test
    void buscarNoExiste_ok() {
        boolean existe = huespedRepository.findByNombreIgnoreCase("nadie").isPresent();

        assertEquals(false, existe);
    }
}
