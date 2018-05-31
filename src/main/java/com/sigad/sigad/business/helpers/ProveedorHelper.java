/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Proveedor;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author chrs
 */
public class ProveedorHelper {
    
    Session session = null;
    private String errorMessage = "";
    public ProveedorHelper() {
        session = LoginController.serviceInit();
    }
    
    public void close(){
        session.close();
    }
        
    public String getErrorMessage(){
        return errorMessage;
    }
    
    public ArrayList<Proveedor> getProveedores() {
        ArrayList<Proveedor> proveedores = null;
        Query query = null;

        try {
            query = session.createQuery("from Proveedor");
            if(!query.list().isEmpty()){
                proveedores = (ArrayList<Proveedor>) ( query.list());
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        return proveedores;
    }
    
}
