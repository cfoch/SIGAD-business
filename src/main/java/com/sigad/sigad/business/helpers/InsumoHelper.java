/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Insumo;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author jorgito-stark
 */
public class InsumoHelper {
    Session session = null;
    
    public InsumoHelper() {
        session = LoginController.serviceInit();
        session.beginTransaction();

    }
    
    public ArrayList<Insumo> getInsumos(){
        ArrayList<Insumo> list = null;
        try {
            Query query  = session.createQuery("from Insumo");
            
            if(!query.list().isEmpty()){
                list= (ArrayList<Insumo>) query.list();
            }
        } catch (Exception e) {
        }
        
        return list;
    };
    
    public Insumo getInsumo(Long id){
        Insumo insumo = null;
        Query query = null;
        try {
            query = session.createQuery("from Insumo where id=" + id);
            
            if(!query.list().isEmpty()){
               insumo = (Insumo)( query.list().get(0));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally{
            return insumo;
        }        
    }

    public void close(){
        session.getTransaction().commit();
    }
    
    public class InsumoParser{
        String nombre;
        Integer cantidad;
        
        public InsumoParser(String nombre, Integer cantidad){
            this.nombre = nombre;
            this.cantidad = cantidad;
        }
    }
}
