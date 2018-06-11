/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.MovimientosTienda;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author chrs
 */
public class MovimientoHelper {
    
    Session session = null;
    private String errorMessage = "";
    private final static Logger LOGGER = Logger.getLogger(InsumosHelper.class.getName());
    
    /////////////////////////
    
    public MovimientoHelper() {
        session = LoginController.serviceInit();
    }
    
    public void close(){
        session.close();
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
    
    public Boolean saveMovement(MovimientosTienda newMov){
        Boolean ok = false;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(newMov);
            tx.commit();
            ok = true;
            LOGGER.log(Level.FINE, "Movimiento registrado con exito");
            this.errorMessage = "";
        } catch (Exception e) {
            if (tx!=null)   tx.rollback();
            this.errorMessage = e.getMessage();
            LOGGER.log(Level.SEVERE, String.format("Ocurrio un error al tratar de crear el insumo %s", newMov.getId()));
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
        }
        return true;
    }
    
}
