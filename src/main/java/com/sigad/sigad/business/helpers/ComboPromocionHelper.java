/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.ComboPromocion;
import com.sigad.sigad.business.Producto;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class ComboPromocionHelper {

    Session session = null;
    private String errorMessage = "";

    public ComboPromocionHelper() {
        session = LoginController.serviceInit();
        session.beginTransaction();
    }

    public ArrayList<ComboPromocion> getCombos() {
        ArrayList<ComboPromocion> list = null;
        try {
            Query query = session.createQuery("from ComboPromocion");
            list = (ArrayList<ComboPromocion>) query.list();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally {
            return list;
        }
    }

    ;
    
    public ComboPromocion getComboById(Integer id) {
        ComboPromocion descuento = null;
        Query query = null;
        try {
            query = session.createQuery("from ComboPromocion where id='" + id + "'");

            if (!query.list().isEmpty()) {
                descuento = (ComboPromocion) query.list().get(0);
            }
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return descuento;
    }

    ;

    
    
    public List<ComboPromocion> getCombosByProducto(Integer producto_id) {
        ComboPromocion combo = null;
        Producto producto = null;
        List<ComboPromocion> combos = new ArrayList<>();
        Query query = null;
        try {
            query = session.createQuery("from Producto where producto_id='" + producto_id + "' and activo=true");

            producto = (Producto) query.list().get(0);
            if (producto != null) {
                combos = new ArrayList(producto.getCombos());
            }

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        } finally {
            return combos;
        }

    }

    ;
    
     public Long saveCombo(ComboPromocion p) {
        Long id = null;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }

            session.save(p);
            if (p.getId() != null) {
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

    public boolean updateCombo(ComboPromocion uOld) {
        boolean ok = false;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }

            ComboPromocion uNew = session.load(ComboPromocion.class, uOld.getId());

            uNew.setFechaInicio(uOld.getFechaInicio());
            uNew.setFechaFin(uOld.getFechaFin());
            uNew.setMaxDisponible(uOld.getMaxDisponible());
            uNew.setActivo(uOld.getActivo());
            uNew.setNombre(uOld.getNombre());
            uNew.setDescripcion(uOld.getDescripcion());
            uNew.setDuracionDias(uOld.getDuracionDias());
            uNew.setPreciounitario(uOld.getPreciounitario());//Aunq no deberia, si es que la cantidad de vendidos es mayos que 0

            session.merge(uNew);
            tx.commit();
            session.close();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return ok;
    }

    public void close() {
        session.getTransaction().commit();
    }
}
