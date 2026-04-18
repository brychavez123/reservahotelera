package com.evaluacion.reservahotelera.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;

@Entity
@Table(name = "PAGO")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MONTO", precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "METODO", length = 50)
    private String metodo;

    @Column(name = "ESTADO", length = 20)
    private String estado;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HUESPED_ID", nullable = false)
    private Huesped huesped;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESERVA_ID", nullable = false, unique = true)
    private ReservaHotel reserva;

    public Pago() {
    }

    public Pago(Integer id, BigDecimal monto, String metodo, String estado) {
        this.id = id;
        this.monto = monto;
        this.metodo = metodo;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
    }

    public ReservaHotel getReserva() {
        return reserva;
    }

    public void setReserva(ReservaHotel reserva) {
        this.reserva = reserva;
    }

    @Transient
    public Integer getHuespedId() {
        return huesped != null ? huesped.getId() : null;
    }

    @Transient
    public Integer getReservaId() {
        return reserva != null ? reserva.getId() : null;
    }
}
