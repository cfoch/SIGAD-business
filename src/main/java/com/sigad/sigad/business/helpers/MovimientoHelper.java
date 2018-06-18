/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.Tienda;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
    
    /*Get all the movements*/
    public ArrayList<MovimientosTienda> getMovements(){
        ArrayList<MovimientosTienda> movements = null;
        Query query = null;
        try {
            query = session.createQuery("from MovimientosTienda");
            
            if(!query.list().isEmpty()){
               movements = (ArrayList<MovimientosTienda>)( query.list());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally{
            return movements;
        }
    }
    
    /*Get all the movements filtered by store*/
    public ArrayList<MovimientosTienda> getMovements(Tienda tienda){
        ArrayList<MovimientosTienda> movements = null;
        Query query = null;
        try {
            query = session.createQuery("from MovimientosTienda where tienda_id =" + tienda.getId().toString());
            
            if(!query.list().isEmpty()){
               movements = (ArrayList<MovimientosTienda>)( query.list());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally{
            return movements;
        }
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
