/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.business.ComboPromocion;
import com.sigad.sigad.business.DetallePedido;
import com.sigad.sigad.business.Producto;
import java.util.ArrayList;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class DetallePedidoHelper extends BaseHelper{
         public DetallePedidoHelper() {
    }
        
    /*Get all*/
    public ArrayList<DetallePedido> getDetallesPedido(int pedido_id){
        ArrayList<DetallePedido> detalles = null;
        Query query = null;
        try {
            query = session.createQuery("from DetallePedido where pedido_id = " + String.valueOf(pedido_id)  + "activo = 'true'");
            
            if(!query.list().isEmpty()){
               detalles = (ArrayList<DetallePedido>)( query.list());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally{
            return detalles;
        }
    };
    
    /*Get profile by id, if nothin then null*/
    public DetallePedido getDetallePedido(int id){
        DetallePedido p = null;
        Query query = null;
        try {
            query = session.createQuery("from DetallePedido where id=" + Integer.toString(id));
            
            if(!query.list().isEmpty()){
                p = (DetallePedido) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally{
            return p;
        }
    }
    
    /*Get profile by pedido_id and producto, if nothin then null*/
    public DetallePedido getDetallePedido(int pedido_id, Producto producto){
        DetallePedido d = null;
        Query query = null;
        try {
            query = session.createQuery("from DetallePedido where producto_id=" + producto.getId() + "pedido_id=" + pedido_id);
            
            if(!query.list().isEmpty()){
                d = (DetallePedido) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally{
            return d;
        }
    }
    
     /*Get detalle by combo and pedido_id, if nothin then null*/
    public DetallePedido getDetallePedido(int pedido_id, ComboPromocion combo){
        DetallePedido d = null;
        Query query = null;
        try {
            query = session.createQuery("from DetallePedido where combo_id=" + combo.getId() + "pedido_id=" + pedido_id);
            
            if(!query.list().isEmpty()){
                d = (DetallePedido) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally{
            return d;
        }
    }
    
    public Long saveProfile(DetallePedido p){
        Long id = null;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            session.save(p);
            if(p.getId() != null){
                id = p.getId();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return id;
    }
    
    public boolean updateDetallePedido(DetallePedido pOld){
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            DetallePedido pNew = session.load(DetallePedido.class, pOld.getId());
            
            pNew.setActivo(pOld.isActivo());
            
            session.merge(pNew);
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
