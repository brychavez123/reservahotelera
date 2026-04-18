package com.evaluacion.reservahotelera.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "RESERVA_HOTEL")
public class ReservaHotel {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NOMBRE_HUESPED")
    private String nombreHuesped;

    @Column(name = "NUMERO_HABITACION")
    private Integer numeroHabitacion;

    @Column(name = "FECHA_ENTRADA")
    private LocalDate fechaEntrada;

    @Column(name = "FECHA_SALIDA")
    private LocalDate fechaSalida;

    @Column(name = "ESTADO")
    private String estado;

    public ReservaHotel() {
    }

    public ReservaHotel(Integer id, String nombreHuesped, Integer numeroHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida, String estado) {
        this.id = id;
        this.nombreHuesped = nombreHuesped;
        this.numeroHabitacion = numeroHabitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreHuesped() {
        return nombreHuesped;
    }

    public void setNombreHuesped(String nombreHuesped) {
        this.nombreHuesped = nombreHuesped;
    }

    public Integer getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(Integer numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
