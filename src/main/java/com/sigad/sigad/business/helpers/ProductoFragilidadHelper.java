/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.ProductoFragilidad;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author jorgito-stark
 */
public class ProductoFragilidadHelper {
        Session session = null;
    
    public ProductoFragilidadHelper() {
        session = LoginController.serviceInit();
        session.beginTransaction();

    }
    
    public ArrayList<ProductoFragilidad> getProductFragilities(){
        ArrayList<ProductoFragilidad> list = null;
        try {
            Query query  = session.createQuery("from ProductoFragilidad");
            
            if(!query.list().isEmpty()){
                list= (ArrayList<ProductoFragilidad>) query.list();
            }
        } catch (Exception e) {
        }
        
        return list;
    };
    
    public ProductoFragilidad getProductFragilityByDescription(String description){
        ProductoFragilidad fragility = null;
        Query query = null;
        try {
            query = session.createQuery("from ProductoFragilidad where descripcion='" + description + "'");
            
            if(!query.list().isEmpty()){
               fragility = (ProductoFragilidad)( query.list().get(0));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally{
            return fragility;
        }        
    }
      

    public void close(){
        session.getTransaction().commit();
    }  
}
