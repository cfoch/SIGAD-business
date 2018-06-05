/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Alexandra
 */
@Entity
public class ProveedorInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private Proveedor proveedor;
    @ManyToOne
    private Insumo insumo;
    private Double precio;

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
     * @return the proveedor
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * @return the insumo
     */
    public Insumo getInsumo() {
        return insumo;
    }

    /**
     * @param insumo the insumo to set
     */
    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    /**
     * @return the precio
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

}
