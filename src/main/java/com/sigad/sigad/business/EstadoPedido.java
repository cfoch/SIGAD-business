/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.sql.Time;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Alexandra
 */
@Entity
public class EstadoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotNull
    private Timestamp hora;
    
    @ManyToOne
    private Pedido pedido;
    
    @ManyToOne
    private PedidoEstado estado;

    public EstadoPedido() {
    }

    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the hora
     */
    public Timestamp getHora() {
        return hora;
    }

    /**
     * @param hora the hora to set
     */
    public void setHora(Timestamp hora) {
        this.hora = hora;
    }

    /**
     * @return the pedido
     */
    public Pedido getPedido() {
        return pedido;
    }

    /**
     * @param pedido the pedido to set
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * @return the estado
     */
    public PedidoEstado getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(PedidoEstado estado) {
        this.estado = estado;
    }
    
}
