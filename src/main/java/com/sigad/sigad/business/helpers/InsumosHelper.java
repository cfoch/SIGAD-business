/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.Proveedor;
import com.sigad.sigad.business.ProveedorInsumo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author chrs
 */
public class InsumosHelper {
    
    Session session = null;
    private String errorMessage = "";
    private final static Logger LOGGER = Logger.getLogger(InsumosHelper.class.getName());
    
    /////////////////////////
    
    public InsumosHelper() {
        session = LoginController.serviceInit();
    }
    
    public void close(){
        session.close();
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
    
    public Long saveInsumo(Insumo newInsumo) {
        Long id = null;
        try {
            Transaction tx = session.beginTransaction();
            session.save(newInsumo);
            if(newInsumo.getId() != null) {
                id = newInsumo.getId();
            }
            session.getTransaction().commit();                        
            LOGGER.log(Level.FINE, "Insumo guardado con exito");
            this.errorMessage = "";
        } catch (Exception e) {
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
        }
        return id;
    }
    
    public List<ProveedorInsumo> getInsumoFromProveedor(Proveedor proveedor) {
        String hqlQuery = "from ProveedorInsumo PI where PI.proveedor_id = :prov_id";
        try{
            List<ProveedorInsumo> busquedaResultado = session.createQuery(hqlQuery).setParameter("prov_id", proveedor.getId()).list();
            LOGGER.log(Level.INFO, String.format("Insumos asociados al proveedor %s encontrados, total %d", proveedor.getNombre(), busquedaResultado.size()));
            return busquedaResultado;
        }
        catch(Exception e) {
            LOGGER.log(Level.WARNING, String.format("Error en la busqueda de los Insumos del proveedor %s", proveedor.getNombre()));
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
            return null;
        }
    }
    
    public Long updateInsumo(Insumo modifiedInsumo) {
        Long id = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(modifiedInsumo);
            tx.commit();
            LOGGER.log(Level.FINE, "Insumo actualizado con exito");
            this.errorMessage = "";
            id = modifiedInsumo.getId();
        }
        catch(Exception e) {
            if (tx!=null)   tx.rollback();
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
            this.errorMessage = e.getMessage();
        }
        return id;
    }
    
    public Long disableInsumo(Insumo disabledInsumo) {
        Long id = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            disabledInsumo.setActivo(false);    // se deshabilita insumo, eliminacion logica
            session.update(disabledInsumo);
            tx.commit();
            LOGGER.log(Level.FINE, "Insumo deshabilitado con exito");
            this.errorMessage = "";
            id = disabledInsumo.getId();
        }
        catch(Exception e) {
            if (tx!=null)   tx.rollback();
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
            this.errorMessage = e.getMessage();
        }
        return id;
    }
    
    public ArrayList<Insumo> getInsumos() {
        ArrayList<Insumo> insumos = null;
        Query query = null;
        try {
            query = session.createQuery("from Insumo");
            if(!query.list().isEmpty()){
                insumos = (ArrayList<Insumo>)(query.list());
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        return insumos;
    }
}
