/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import com.sigad.sigad.business.ids.LoteTiendaId;
import javax.validation.constraints.NotNull;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 *
 * @author jorgeespinoza
 */
@Entity
public class LoteTienda {
    @EmbeddedId
    private LoteTiendaId id = new LoteTiendaId();
    @NotNull
    private int stock;

    /**
     * Constructor.
     */
    public LoteTienda() {
    }
    
    /**
     * @return the id
     */
    public LoteTiendaId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(LoteTiendaId id) {
        this.id = id;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
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
