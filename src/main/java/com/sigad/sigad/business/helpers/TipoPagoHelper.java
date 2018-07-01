/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.business.TipoPago;
import java.util.ArrayList;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class TipoPagoHelper extends BaseHelper {

    public TipoPagoHelper() {
        super();
    }

    /*Get all*/
    public ArrayList<TipoPago> getTipoPagos() {
        ArrayList<TipoPago> profiles = null;
        Query query = null;
        try {
            query = session.createQuery("from TipoPago");
            profiles = (ArrayList<TipoPago>) (query.list());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            errorMessage = e.getMessage();
        } finally {
            return profiles;
        }
    }

    ;
    
    /*Get profile by id, if nothin then null*/
    public TipoPago getTipoPago(int id) {
        TipoPago p = null;
        Query query = null;
        try {
            query = session.createQuery("from TipoPago where id=" + Integer.toString(id));

            if (!query.list().isEmpty()) {
                p = (TipoPago) query.list().get(0);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            return p;
        }
    }

    /*Get profile by name, if nothin then null*/
    public TipoPago getTipoPago(String descripcion) {
        TipoPago p = null;
        Query query = null;
        try {
            query = session.createQuery("from TipoPago where descripcion='" + descripcion + "'");
            p = (TipoPago) query.list().get(0);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            return p;
        }
    }

    public Long saveTipoPago(TipoPago p) {
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
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        }
        return id;
    }

    public boolean updateTipoPago(TipoPago pOld) {
        boolean ok = false;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }

            TipoPago pNew = session.load(TipoPago.class, pOld.getId());

            pNew.setDescripcion(pOld.getDescripcion());
            session.merge(pNew);
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
