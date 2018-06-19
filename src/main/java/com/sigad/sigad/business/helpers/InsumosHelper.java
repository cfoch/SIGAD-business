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
import com.sigad.sigad.business.Tienda;
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
    
    
    public Long saveInsumo(Insumo newInsumo, List<ProveedorInsumo>  lista_proveedoresxInsumo) {
        Long id = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            newInsumo.setActivo(true);
            session.save(newInsumo);
            if(newInsumo.getId() != null) {
                id = newInsumo.getId();
            }
            for (ProveedorInsumo proveedorxInsumo : lista_proveedoresxInsumo) {
                LOGGER.log(Level.INFO, String.format("Procesando relacion entre %s y %s", proveedorxInsumo.getProveedor().getNombre(), proveedorxInsumo.getInsumo().getNombre()));
                session.save(proveedorxInsumo);
            }
            tx.commit();
            LOGGER.log(Level.FINE, "Insumo guardado con exito");
            this.errorMessage = "";
        } catch (Exception e) {
            if (tx!=null)   tx.rollback();
            this.errorMessage = e.getMessage();
            LOGGER.log(Level.SEVERE, String.format("Ocurrio un error al tratar de crear el insumo %s", newInsumo.getNombre()));
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
        }
        return id;
    }
    
    public ProveedorInsumo getInsumoProveedorUnit(Insumo insumo, Proveedor proveedor) {
        String hqlQuery = "from ProveedorInsumo PI where PI.proveedor = :prov_id and PI.insumo = :in_id and activo=true";
        try{
            List<ProveedorInsumo> busquedaResultado = session.createQuery(hqlQuery).setParameter("prov_id", proveedor).setParameter("in_id", insumo).list();
            if (!busquedaResultado.isEmpty()){
                LOGGER.log(Level.INFO, String.format("Insumo %s asociado al proveedor %s encontrado ", insumo.getNombre(), proveedor.getNombre()));
                return busquedaResultado.get(0);
            }
            else{
                LOGGER.log(Level.INFO, String.format("Insumo %s asociado al proveedor %s no encontrado ", insumo.getNombre(), proveedor.getNombre()));
                return null;
            }
        }
        catch(Exception e) {
            LOGGER.log(Level.WARNING, String.format("Error en la busqueda de los Insumos del proveedor %s", proveedor.getNombre()));
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
            return null;
        }
    }
    
    public List<ProveedorInsumo> getInsumoFromProveedor(Proveedor proveedor) {
        String hqlQuery = "from ProveedorInsumo PI where PI.proveedor = :prov_id and activo=true";
        try{
            List<ProveedorInsumo> busquedaResultado = session.createQuery(hqlQuery).setParameter("prov_id", proveedor).list();
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
    
    public Long updateInsumo(Insumo modifiedInsumo, List<ProveedorInsumo>  lista_proveedoresxInsumo) {
        Long id = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(modifiedInsumo);
            for (ProveedorInsumo proveedorInsumo : lista_proveedoresxInsumo) {
                LOGGER.log(Level.INFO, String.format("Actualizando relacion entre %s y %s", proveedorInsumo.getProveedor().getNombre(), proveedorInsumo.getInsumo().getNombre()));
                session.saveOrUpdate(proveedorInsumo);
            }
            tx.commit();
            LOGGER.log(Level.FINE, "Insumo actualizado con exito");
            this.errorMessage = "";
            id = modifiedInsumo.getId();
        }
        catch(Exception e) {
            if (tx!=null)   tx.rollback();
            LOGGER.log(Level.SEVERE, String.format("Ocurrio un error al tratar de actualizar el insumo %s", modifiedInsumo.getNombre()));
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
            this.errorMessage = e.getMessage();
        }
        return id;
    }
    
    public Boolean updateProveedorInsumo(ProveedorInsumo tOld){
        Transaction tx = null;
        boolean ok = false;
        try {          
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            ProveedorInsumo tNew = session.load(ProveedorInsumo.class, tOld.getId());
            
            tNew.setActivo(tOld.isActivo());
            tNew.setInsumo(tOld.getInsumo());
            tNew.setPrecio(tOld.getPrecio());
            tNew.setProveedor(tOld.getProveedor());

            session.update(tNew);
            session.merge(tNew);
            tx.commit();
            ok = true;
            LOGGER.log(Level.FINE, "Lote insumo guardado");
            this.errorMessage = "";
        } catch (Exception e) {
            if (tx!=null)   tx.rollback();
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return ok;
    }
    
    public Boolean saveProveedorInsumo(ProveedorInsumo pi){
        Transaction tx = null;
        boolean ok = false;
        try {
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            session.save(pi);
            tx.commit();
            ok = true;
            LOGGER.log(Level.FINE, "Lote insumo guardado");
            this.errorMessage = "";
        } catch (Exception e) {
            if (tx!=null)   tx.rollback();
            this.errorMessage = e.getMessage();
            LOGGER.log(Level.SEVERE, String.format("Ocurrio un error al tratar de crear el lote insumo"));
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
        }
        return ok;
    }
    
    public boolean updateInsumo(Insumo tOld) {
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            Insumo tNew = session.load(Insumo.class, tOld.getId());
            
            tNew.setActivo(tOld.isActivo());
            tNew.setDescripcion(tOld.getDescripcion());
            tNew.setImagen(tOld.getImagen());
            tNew.setNombre(tOld.getNombre());
            tNew.setPrecio(tOld.getPrecio());
            tNew.setStockTotalFisico(tOld.getStockTotalFisico());
            tNew.setStockTotalLogico(tOld.getStockTotalLogico());
            tNew.setTiempoVida(tOld.getTiempoVida());
            tNew.setVolumen(tOld.isVolumen());
            
            session.update(tNew);
            session.merge(tNew);
            tx.commit();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return ok;
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
    
    public Insumo getInsumo(Long id){
        Insumo insumo = null;
        Query query = null;
        try {
            query = session.createQuery("from Insumo where id=" + id);
            if(!query.list().isEmpty()){
                insumo = (Insumo) query.list().get(0);
            }
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return insumo;
        
    }
}
