/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.CapacidadTienda;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.Tienda;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.hibernate.Session;

/**
 *
 * @author Alexandra
 */
public class CapacidadTiendaHelper {
    static Session session = null;
    private String errorMessage = "";

    public CapacidadTiendaHelper() {
        if(this.session!= null) this.close();
        session = LoginController.serviceInit();
    }

    /*Close session*/
    public void close() {
        session.close();
    }
    
    public HashMap<Insumo, Integer> getCapacidadbyTend(Tienda tienda){
        ArrayList<CapacidadTienda> capacidades = new ArrayList(tienda.getCapacidadTiendas()); 
        HashMap<Insumo, Integer> capacidadesTienda = new HashMap<>();
        capacidades.forEach((t) -> {
            capacidadesTienda.put(t.getInsumo(), t.getCantidad());
        });
        return capacidadesTienda;
    
    }
 
}
