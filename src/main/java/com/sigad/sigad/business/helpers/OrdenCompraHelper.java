/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.LoteInsumo;
import com.sigad.sigad.business.OrdenCompra;
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
public class OrdenCompraHelper {
    private final static Logger LOGGER = Logger.getLogger(OrdenCompraHelper.class.getName());
    Session session = null;
    private String errorMessage = "";
    public OrdenCompraHelper(){
        session = LoginController.serviceInit();
    }
    public void close(){
        session.close();
    }
    public String getErrorMessage(){
        return errorMessage;
    }
    public OrdenCompra getOrden(Integer id){
        session.beginTransaction();
        OrdenCompra orden =  (OrdenCompra)session.get(OrdenCompra.class, id);
        session.getTransaction().commit();
        return orden;
    }
    
    /* mandar el objeto de orden de compra ya con todos los campos llenos y una lista de lotexInsumo con sus datos llenos obtenidos de la pantalla de front */
    public Integer saveOrden(OrdenCompra newOrden, List<LoteInsumo> lista_lotexInsumo){
        Integer id = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(newOrden);
            LOGGER.log(Level.INFO, "Orden de Compra generada con exito");
            if(newOrden.getId() != 0) {
                id = newOrden.getId();
            }
            session.getTransaction().commit();
            for (LoteInsumo lotexInsumo : lista_lotexInsumo) {
                session.save(lotexInsumo);
                LOGGER.log(Level.INFO, String.format("El insumo %s fue registrado como lote para la orden de compra", lotexInsumo.getInsumo().getNombre()));
                // aqui registrar el DetalleOrdenCompra por cada lotexInsumo
            }
            tx.commit();
        } catch (Exception e) {
            if (tx!=null)   tx.rollback();
            LOGGER.log(Level.SEVERE, "Error al intentar realizar la transaccion de orden de compra");
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
            this.errorMessage = e.getMessage();
        }
        return id;
    }
    
    
    public ArrayList<OrdenCompra> getOrdenes(){
        ArrayList<OrdenCompra> ordenes = null;
        Query query = null;
        try {
            query = session.createQuery("from OrdenCompra");
            if(!query.list().isEmpty()){
                ordenes = (ArrayList<OrdenCompra>)query.list();
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        return ordenes;
    }
}
