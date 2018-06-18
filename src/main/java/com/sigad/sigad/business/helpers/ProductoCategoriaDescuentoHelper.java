/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.ProductoCategoriaDescuento;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class ProductoCategoriaDescuentoHelper {

    Session session = null;
    private String errorMessage = "";

    public ProductoCategoriaDescuentoHelper() {
        session = LoginController.serviceInit();
        session.beginTransaction();
    }

    public ArrayList<ProductoCategoriaDescuento> getDescuentos() {
        ArrayList<ProductoCategoriaDescuento> list = null;
        try {
            Query query = session.createQuery("from ProductoCategoriaDescuento");
            list = (ArrayList<ProductoCategoriaDescuento>) query.list();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally {
            return list;
        }
    }

    ;
    
    public ProductoCategoriaDescuento getDescuentoById(Integer id) {
        ProductoCategoriaDescuento descuento = null;
        Query query = null;
        try {
            query = session.createQuery("from ProductoCategoriaDescuento where id='" + id + "'");

            if (!query.list().isEmpty()) {
                descuento = (ProductoCategoriaDescuento) query.list().get(0);
            }
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return descuento;
    }

    ;

    public ProductoCategoriaDescuento getDescuentoByProducto(Integer producto_id) {
        ProductoCategoriaDescuento descuento = null;
        Query query = null;
        try {
            query = session.createQuery("from ProductoCategoriaDescuento where producto_id='" + producto_id);

            if (!query.list().isEmpty()) {
                descuento = (ProductoCategoriaDescuento) query.list().get(0);
            }
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        } finally {
            return descuento;
        }

    }

    ;
    
    public List<ProductoCategoriaDescuento> getDescuentosByProducto(Integer producto_id) {
        List<ProductoCategoriaDescuento> descuentos = null;
        Query query = null;
        try {
            query = session.createQuery("from ProductoCategoriaDescuento where producto_id='" + producto_id);

            descuentos = (List<ProductoCategoriaDescuento>) query.list();
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        } finally {
            return descuentos;
        }

    }

    ;
    
     public Long saveDescuento(ProductoCategoriaDescuento p) {
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

    public boolean updateDescuento(ProductoCategoriaDescuento uOld) {
        boolean ok = false;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }

            ProductoCategoriaDescuento uNew = session.load(ProductoCategoriaDescuento.class, uOld.getId());

            uNew.setFechaInicio(uOld.getFechaInicio());
            uNew.setFechaFin(uOld.getFechaFin());
            uNew.setValue(uOld.getValue());

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
