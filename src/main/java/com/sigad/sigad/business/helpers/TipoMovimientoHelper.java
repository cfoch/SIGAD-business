/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.TipoMovimiento;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author chrs
 */
public class TipoMovimientoHelper {
    Session session = null;
    private String errorMessage = "";
    private final static Logger LOGGER = Logger.getLogger(InsumosHelper.class.getName());
    
    /////////////////////////
    
    public TipoMovimientoHelper() {
        session = LoginController.serviceInit();
    }
    
    public void close(){
        session.close();
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
    
    public ArrayList<TipoMovimiento> getTiposMovimientos(){
        ArrayList<TipoMovimiento> tipos = null;
        Query query = null;
        try {
            query = session.createQuery("from TipoMovimiento");
            if(!query.list().isEmpty()){
                tipos = (ArrayList<TipoMovimiento>)(query.list());
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        return tipos;
    }
    
    public TipoMovimiento getTipoMov(String nombre){
        TipoMovimiento tipoMov = null;
        Query query = null;
        try {
            query = session.createQuery("from TipoMovimiento where nombre = '" + nombre + "'");
            if(!query.list().isEmpty()){
                tipoMov = (TipoMovimiento) query.list().get(0);
            }
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return tipoMov;
    }
}
