package com.evaluacion.reservahotelera.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "HOTEL_HABITACION")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NUMERO_HABITACION", nullable = false, unique = true)
    private Integer numeroHabitacion;

    @Column(name = "CAPACIDAD_PERSONAS")
    private Integer capacidadPersonas;

    public Habitacion() {
    }

    public Habitacion(Integer id, Integer numeroHabitacion, Integer capacidadPersonas) {
        this.id = id;
        this.numeroHabitacion = numeroHabitacion;
        this.capacidadPersonas = capacidadPersonas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(Integer numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public Integer getCapacidadPersonas() {
        return capacidadPersonas;
    }

    public void setCapacidadPersonas(Integer capacidadPersonas) {
        this.capacidadPersonas = capacidadPersonas;
    }
}
