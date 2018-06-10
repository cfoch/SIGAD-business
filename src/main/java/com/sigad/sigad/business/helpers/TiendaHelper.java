/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Tienda;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author jorgeespinoza
 */
public class TiendaHelper {
    
    Session session = null;
    private String errorMessage = "";
    
    public TiendaHelper() {
        session = LoginController.serviceInit();
    }
    
    /*Close session*/
    public void close(){
        session.close();
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /*Get all stores*/
    public ArrayList<Tienda> getStores(){
        ArrayList<Tienda> stores = new ArrayList<>();
        Query query = null;
        try {
            query = session.createQuery("from Tienda");
            
            if(!query.list().isEmpty()){
               stores = (ArrayList<Tienda>)( query.list());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally{
            return stores;
        }
    }
    
    /*Get store by id, if nothin then null*/
    public Tienda getStore(int id){
        Tienda t = null;
        Query query = null;
        try {
            query = session.createQuery("from Tienda where id=" + Integer.toString(id));
            
            if(!query.list().isEmpty()){
                t = (Tienda) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            this.errorMessage = e.getMessage();
        } finally{
            return t;
        }
    }
    
    /*Get store by id, if nothin then null*/
    public Tienda getStore(String direction){
        Tienda t = null;
        Query query = null;
        try {
            query = session.createQuery("from Tienda where direccion='" + direction +"'");
            
            if(!query.list().isEmpty()){
                t = (Tienda) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            this.errorMessage = e.getMessage();
        } finally{
            return t;
        }
    }
    
    /*Get store by coordinate x and y, if nothin then null*/
    public Tienda getStore(double cooxdireccion, double cooydireccion){
        Tienda t = null;
        Query query = null;
        try {
            query = session.createQuery("from Tienda where cooxdireccion='" + Double.toString(cooxdireccion) + "' and cooydireccion='" + Double.toString(cooydireccion) + "'");
            
            if(!query.list().isEmpty()){
                t = (Tienda) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            this.errorMessage = e.getMessage();
        } finally{
            return t;
        }
    }
    
    public Long saveStore(Tienda t){
        Long id = null;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            session.save(t);
            
            if(t.getId() != null){
                id = t.getId();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return id;
    }
    
    public boolean updateStore(Tienda tOld){
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            Tienda tNew = session.load(Tienda.class, tOld.getId());
            
            tNew.setCapacidad(tOld.getCapacidad());
            tNew.setCooXDireccion(tOld.getCooXDireccion());
            tNew.setCooYDireccion(tOld.getCooYDireccion());
            tNew.setDescripcion(tOld.getDescripcion());
            tNew.setDireccion(tOld.getDireccion());
            tNew.setActivo(tOld.isActivo());
            
            session.merge(tNew);
            tx.commit();
            session.close();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return ok;
    }
    
   
}
