package com.evaluacion.reservahotelera;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> manejarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "ok", false,
                "error", "Parametro invalido: revise id, numeroHabitacion o formato de fecha"
        ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> manejarParametroFaltante(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "ok", false,
                "error", "Falta un parametro requerido: " + ex.getParameterName()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> manejarIntegridadDatos(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "ok", false,
                "error", "No se pudo completar la operacion por restricciones de integridad en base de datos"
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> manejarErrorGeneral(Exception ex) {
        return ResponseEntity.internalServerError().body(Map.of(
                "ok", false,
                "error", "Ocurrio un error inesperado"
        ));
    }
}
