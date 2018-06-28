/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoDescuento;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class ProductoDescuentoHelper extends BaseHelper {

    public ProductoDescuentoHelper() {
        super();
    }

    public ArrayList<ProductoDescuento> getDescuentos() {
        ArrayList<ProductoDescuento> list = null;
        try {
            Query query = session.createQuery("from ProductoDescuento");
            list = (ArrayList<ProductoDescuento>) query.list();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally {
            return list;
        }
    }

    ;
    
    public ProductoDescuento getDescuentoById(Integer id) {
        ProductoDescuento descuento = null;
        Query query = null;
        try {
            query = session.createQuery("from ProductoDescuento where id='" + id + "'");

            if (!query.list().isEmpty()) {
                descuento = (ProductoDescuento) query.list().get(0);
            }
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return descuento;
    }

    ;

    public ProductoDescuento getDescuentoByProducto(Integer producto_id) {
        ProductoDescuento descuento = null;
        ArrayList<ProductoDescuento> descuentos = new ArrayList<>();
        Query query = null;
        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            query = session.createQuery("SELECT c FROM ProductoDescuento AS c WHERE c.fechaInicio <= :today AND c.fechaFin >= :today  AND producto_id='" + producto_id + "' AND activo=true ");
            query.setParameter("today", new Date());

            if (!query.list().isEmpty()) {
                descuentos = (ArrayList<ProductoDescuento>) query.list();
                descuento = descuentos.stream().max(Comparator.comparing(ProductoDescuento::getValorPct)).orElseThrow(NoSuchElementException::new);

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
    
    public List<ProductoDescuento> getDescuentosByProducto(Integer producto_id) {
        List<ProductoDescuento> descuentos = null;
        Query query = null;
        try {
            query = session.createQuery("from ProductoDescuento where producto_id='" + producto_id + "' and activo=true ");

            if (!query.list().isEmpty()) {
                descuentos = (List<ProductoDescuento>) query.list();
            }
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        } finally {
            return descuentos;
        }

    }

    ;
    
     public Long saveDescuento(ProductoDescuento p) {
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

    public boolean updateDescuento(ProductoDescuento uOld) {
        boolean ok = false;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }

            ProductoDescuento uNew = session.load(ProductoDescuento.class, uOld.getId());

            uNew.setFechaInicio(uOld.getFechaInicio());
            uNew.setFechaFin(uOld.getFechaFin());
            uNew.setValorPct(uOld.getValorPct());

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

}
