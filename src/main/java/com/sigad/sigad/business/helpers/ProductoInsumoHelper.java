/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import org.hibernate.Session;
import com.sigad.sigad.business.ProductoInsumo;
import java.util.ArrayList;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author jorgito-stark
 */
public class ProductoInsumoHelper {
    Session session = null;
    
    public ProductoInsumoHelper() {
        session = LoginController.serviceInit();
        session.beginTransaction();

    }
    
    public Long saveProductoInsumo(ProductoInsumo newProductoInsumo){
        Long id = null;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }

            session.save(newProductoInsumo);
            if(newProductoInsumo.getId() != null){
                id = newProductoInsumo.getId();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return id;
        }
    }   
    
    public ArrayList<ProductoInsumo> getProductoInsumos(Long id){
        ArrayList<ProductoInsumo> list = null;
        try {
            Query query  = session.createQuery("from ProductoInsumo where producto_id=" + id);
            
            if(!query.list().isEmpty()){
                list= (ArrayList<ProductoInsumo>) query.list();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return list;   
    }
    
    public boolean updateProductoInsumo(ProductoInsumo item){
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            session.merge(item);
            tx.commit();
            session.close();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ok;
    }    

    public void close(){
        session.getTransaction().commit();
    }     
}
