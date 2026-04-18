package com.evaluacion.reservahotelera.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDate;

@Entity
@Table(name = "RESERVA_HOTEL")
public class ReservaHotel {

    @Id
    @Column(name = "ID")
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HUESPED_ID")
    private Huesped huesped;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HABITACION_ID")
    private Habitacion habitacion;

    @Column(name = "FECHA_ENTRADA")
    private LocalDate fechaEntrada;

    @Column(name = "FECHA_SALIDA")
    private LocalDate fechaSalida;

    @Column(name = "ESTADO")
    private String estado;

    @JsonIgnore
    @OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
    private Pago pago;

    public ReservaHotel() {
    }

    public ReservaHotel(Integer id, Huesped huesped, Habitacion habitacion, LocalDate fechaEntrada, LocalDate fechaSalida, String estado) {
        this.id = id;
        this.huesped = huesped;
        this.habitacion = habitacion;
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

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
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

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    @Transient
    public String getNombreHuesped() {
        return huesped != null ? huesped.getNombre() : null;
    }

    @Transient
    public Integer getNumeroHabitacion() {
        return habitacion != null ? habitacion.getNumeroHabitacion() : null;
    }

    @Transient
    public Integer getHuespedId() {
        return huesped != null ? huesped.getId() : null;
    }

    @Transient
    public Integer getHabitacionId() {
        return habitacion != null ? habitacion.getId() : null;
    }

    @Transient
    public Integer getPagoId() {
        return pago != null ? pago.getId() : null;
    }
}
