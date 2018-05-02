/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import javax.validation.constraints.NotNull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author jorgeespinoza
 */
@Entity
public class DetalleOrdenCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @NotNull
    private double precioDetalle;
    
    //debe existir una orden de compra
    @ManyToOne(optional = false)
    private OrdenCompra orden;
    @OneToOne
    private LoteInsumo loteInsumo;

    /**
     * Constructor.
     */
    public DetalleOrdenCompra() {
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the precioDetalle
     */
    public double getPrecioDetalle() {
        return precioDetalle;
    }

    /**
     * @param precioDetalle the precioDetalle to set
     */
    public void setPrecioDetalle(double precioDetalle) {
        this.precioDetalle = precioDetalle;
    }

    /**
     * @return the orden
     */
    public OrdenCompra getOrden() {
        return orden;
    }

    /**
     * @param orden the orden to set
     */
    public void setOrden(OrdenCompra orden) {
        this.orden = orden;
    }

    /**
     * @return the loteInsumo
     */
    public LoteInsumo getLoteInsumo() {
        return loteInsumo;
    }

    /**
     * @param loteInsumo the loteInsumo to set
     */
    public void setLoteInsumo(LoteInsumo loteInsumo) {
        this.loteInsumo = loteInsumo;
    }
    
}
