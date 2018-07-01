/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Alexandra
 */
@Entity
public class NotaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Double monto;
    private String codigo;//8alfa
    private Date fechaVencimiento;
    private Date fechaGenerada;
    private String motivo;
    @OneToOne
    private Pedido pedidoOrigen;//Pedido devuelto que provoco la nota de credito
    @OneToOne
    private Pedido pedidoPago;//Pedido con el que se pague

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the monto
     */
    public Double getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the fechaVencimiento
     */
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fechaVencimiento to set
     */
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the fechaGenerada
     */
    public Date getFechaGenerada() {
        return fechaGenerada;
    }

    /**
     * @param fechaGenerada the fechaGenerada to set
     */
    public void setFechaGenerada(Date fechaGenerada) {
        this.fechaGenerada = fechaGenerada;
    }

    /**
     * @return the pedidoOrigen
     */
    public Pedido getPedidoOrigen() {
        return pedidoOrigen;
    }

    /**
     * @param pedidoOrigen the pedidoOrigen to set
     */
    public void setPedidoOrigen(Pedido pedidoOrigen) {
        this.pedidoOrigen = pedidoOrigen;
    }

    /**
     * @return the pedidoPago
     */
    public Pedido getPedidoPago() {
        return pedidoPago;
    }

    /**
     * @param pedidoPago the pedidoPago to set
     */
    public void setPedidoPago(Pedido pedidoPago) {
        this.pedidoPago = pedidoPago;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
