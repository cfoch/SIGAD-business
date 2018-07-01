/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Constantes;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.TipoMovimiento;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author chrs
 */
public class MovimientoHelper extends BaseHelper{
    
    private final static Logger LOGGER = Logger.getLogger(InsumosHelper.class.getName());
    
    /////////////////////////    
    public MovimientoHelper() {
        super();
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
    
    
    /*Get all the movements*/
    public ArrayList<MovimientosTienda> getLogicMovements(Pedido pedido){
        ArrayList<MovimientosTienda> movements = null;
        TipoMovimientoHelper tipomov = new TipoMovimientoHelper();
        TipoMovimiento tipo=tipomov.getTipoMov(Constantes.TIPO_MOVIMIENTO_SALIDA_LOGICA);
        System.out.println(tipo.getNombre());
        Query query = null;
        try {
            query = session.createQuery("from MovimientosTienda where pedido_id=" + pedido.getId() + "and tipomovimiento_id=" + tipo.getId() );
            if(!query.list().isEmpty()){
               movements = (ArrayList<MovimientosTienda>)( query.list());
            }
            movements.forEach((t) -> {
                t.getLoteInsumo().getInsumo();
            });
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
    
    public ArrayList<MovimientosTienda> getMovements(Pedido pedido){
        ArrayList<MovimientosTienda> movements = null;
        Query query = null;
        try {
            query = session.createQuery("from MovimientosTienda where pedido_id =" + pedido.getId().toString());
            
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
    
    public MovimientosTienda getMovement(Long movement_id){
        MovimientosTienda movement = null;
        Query query = null;
        try {
            query = session.createQuery("from MovimientosTienda where id =" + movement_id);
            
            if(!query.list().isEmpty()){
               movement = (MovimientosTienda)( query.list().get(0));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally{
            return movement;
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
    
    public boolean updateMovement(MovimientosTienda tOld){
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            MovimientosTienda tNew = session.load(MovimientosTienda.class, tOld.getId());
            
            tNew.setCantidadMovimiento(tOld.getCantidadMovimiento());
            tNew.setLoteInsumo(tOld.getLoteInsumo());
            tNew.setTienda(tOld.getTienda());
            tNew.setPedido(tOld.getPedido());
          
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
    
    
    public boolean deleteMovement(MovimientosTienda tOld){
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            MovimientosTienda tNew = session.load(MovimientosTienda.class, tOld.getId());
            tNew.setId(tOld.getId());
            session.delete(tNew);
            tx.commit();
            session.close();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        }
        return ok;
    }
    
}
