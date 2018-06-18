/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.ClienteDescuento;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class ClienteDescuentoHelper {

    Session session = null;
    private String errorMessage = "";

    public ClienteDescuentoHelper() {
        session = LoginController.serviceInit();
        session.beginTransaction();
    }

    public ArrayList<ClienteDescuento> getDescuentos() {
        ArrayList<ClienteDescuento> list = null;
        try {
            Query query = session.createQuery("from ClienteDescuento");
            list = (ArrayList<ClienteDescuento>) query.list();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally {
            return list;
        }
    }

    ;
    
    public ClienteDescuento getDescuentoById(Integer id) {
        ClienteDescuento descuento = null;
        Query query = null;
        try {
            query = session.createQuery("from ClienteDescuento where id='" + id + "'");

            if (!query.list().isEmpty()) {
                descuento = (ClienteDescuento) query.list().get(0);
            }
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return descuento;
    }

    ;

    public ClienteDescuento getDescuentoByCliente(Integer cliente_id) {
        ClienteDescuento descuento = null;
        Query query = null;
        try {
            query = session.createQuery("from ClienteDescuento where producto_id='" + cliente_id + "' and activo=true");
            descuento = (ClienteDescuento) query.list().get(0);
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        } finally {
            return descuento;
        }

    }

    ;
    
    public List<ClienteDescuento> getDescuentosByClientes(Integer cliente_id) {
        List<ClienteDescuento> descuentos = null;
        Query query = null;
        try {
            query = session.createQuery("from ClienteDescuento where cliente_id='" + cliente_id + "' and activo=true");

            descuentos = (List<ClienteDescuento>) query.list();
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        } finally {
            return descuentos;
        }

    }

    ;
    
     public Long saveDescuento(ClienteDescuento p) {
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

    public boolean updateDescuento(ClienteDescuento uOld) {
        boolean ok = false;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }

            ClienteDescuento uNew = session.load(ClienteDescuento.class, uOld.getId());

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
