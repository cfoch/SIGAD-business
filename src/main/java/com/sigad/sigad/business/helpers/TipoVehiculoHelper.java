/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.TipoMovimiento;
import com.sigad.sigad.business.Vehiculo;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author jorgito-stark
 */
public class TipoVehiculoHelper {
    Session session = null;
    private String errorMessage = "";
    private final static Logger LOGGER = Logger.getLogger(InsumosHelper.class.getName());
    
    public TipoVehiculoHelper() {
        session = LoginController.serviceInit();
    }
    
    public void close(){
        session.close();
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
    
    public Vehiculo.Tipo getTipoVehiculo(String nombre){
        Vehiculo.Tipo tipo = null;
        Query query = null;
        try {
            query = session.createQuery("from vehiculo$tipo where nombre = " + nombre);
            if(!query.list().isEmpty()){
                tipo = (Vehiculo.Tipo) query.list().get(0);
            }
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return tipo;
    }
}
