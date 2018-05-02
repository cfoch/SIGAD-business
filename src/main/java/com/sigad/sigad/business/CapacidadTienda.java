/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import com.sigad.sigad.business.ids.CapacidadTiendaId;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 *
 * @author cfoch
 */
@Entity
public class CapacidadTienda {
    @EmbeddedId
    private CapacidadTiendaId id = new CapacidadTiendaId();
    private int cantidad;

    /**
     * Constructor.
     */
    public CapacidadTienda() {
    }
    /**
     * @return the id
     */
    public CapacidadTiendaId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(CapacidadTiendaId id) {
        this.id = id;
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the Tienda
     */
    @Transient
    public Tienda getTienda() {
        return id.getTienda();
    }


    /**
     * @param tienda the Tienda to set
     */
    public void setTienda(Tienda tienda) {
        id.setTienda(tienda);
    }
}
