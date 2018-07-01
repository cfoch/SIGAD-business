/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.business.DetalleOrdenCompra;
import com.sigad.sigad.business.LoteInsumo;
import com.sigad.sigad.business.OrdenCompra;
import com.sigad.sigad.business.Tienda;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author chrs
 */
public class OrdenCompraHelper extends BaseHelper {
    private final static Logger LOGGER = Logger.getLogger(OrdenCompraHelper.class.getName());
    public OrdenCompraHelper(){
        super();
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
            for (LoteInsumo lotexInsumo : lista_lotexInsumo) {
                lotexInsumo.getInsumo().setStockTotalLogico(lotexInsumo.getInsumo().getStockTotalLogico() + lotexInsumo.getStockLogico());
                session.save(lotexInsumo);
                LOGGER.log(Level.INFO, String.format("El insumo %s fue registrado como lote para la orden de compra", lotexInsumo.getInsumo().getNombre()));
                // aqui registrar el DetalleOrdenCompra por cada lotexInsumo
                DetalleOrdenCompra det  = new DetalleOrdenCompra();
                det.setLoteInsumo(lotexInsumo);
                det.setOrden(newOrden);
                det.setPrecioDetalle(lotexInsumo.getStockLogico() * lotexInsumo.getCostoUnitario());
                session.save(det);                               
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
    public ArrayList<OrdenCompra> getOrdenesUser(Long id){
        ArrayList<OrdenCompra> ordenes = null;
        Query query = null;
        try {
            query = session.createQuery("from OrdenCompra where usuario_id = " + id);
            if(!query.list().isEmpty()){
                ordenes = (ArrayList<OrdenCompra>)query.list();
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        return ordenes;
    }
    public ArrayList<OrdenCompra> getOrdenesStore(Tienda tienda){
        ArrayList<OrdenCompra> ordenes = null;
        Query query = null;
        try {
            query = session.createQuery("SELECT ORD from Usuario US INNER JOIN OrdenCompra ORD on US.id = ORD.usuario where US.tienda = :tiendaId");
            query.setParameter("tiendaId",tienda);
            if(!query.list().isEmpty()){
                ordenes = (ArrayList<OrdenCompra>)query.list();
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        return ordenes;
        
    }
    public ArrayList<DetalleOrdenCompra> getDetalles(Integer id){
        ArrayList<DetalleOrdenCompra> ordenes = null;
        Query query = null;
        try {
            query = session.createQuery("from DetalleOrdenCompra where orden_id =" + id.toString());
            if(!query.list().isEmpty()){
                ordenes = (ArrayList<DetalleOrdenCompra>)query.list();
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        return ordenes;
    }
    public boolean updateOrdenCompra(OrdenCompra tOld){
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            OrdenCompra tNew = session.load(OrdenCompra.class, tOld.getId());
            
            tNew.setDetalleOrdenCompra(tOld.getDetalleOrdenCompra());
            tNew.setFecha(tOld.getFecha());
            tNew.setPrecioTotal(tOld.getPrecioTotal());
            tNew.setProveedor(tOld.getProveedor());
            tNew.setRecibido(tOld.isRecibido());
            tNew.setUsuario(tOld.getUsuario());
            
            session.merge(tNew);
            tx.commit();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return ok;
    }
    public OrdenCompra getOrdenCompra(Integer id){
        OrdenCompra orden = null;
        Query query = null;
        try {
            query = session.createQuery("from OrdenCompra where id=" + id);
            if(!query.list().isEmpty()){
                orden = (OrdenCompra) query.list().get(0);
            }
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return orden;
    }
}
