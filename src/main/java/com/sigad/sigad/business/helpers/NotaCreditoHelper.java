/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.business.NotaCredito;
import com.sigad.sigad.business.Pedido;
import java.util.ArrayList;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class NotaCreditoHelper extends BaseHelper {

    public NotaCreditoHelper() {
    }

    /*Get all*/
    public ArrayList<NotaCredito> getNotasDeCredito() {
        ArrayList<NotaCredito> notas = null;
        Query query = null;
        try {
            query = session.createQuery("from NotaCredito");

            notas = (ArrayList<NotaCredito>) (query.list());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            errorMessage = e.getMessage();
        } finally {
            return notas;
        }
    }

    ;
    
    /*Get profile by id, if nothin then null*/
    public NotaCredito getNota(int id) {
        NotaCredito p = null;
        Query query = null;
        try {
            query = session.createQuery("from NotaCredito where id=" + Integer.toString(id));

            p = (NotaCredito) query.list().get(0);
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            return p;
        }
    }

    /*Get profile by name, if nothin then null*/
    public NotaCredito getNota(String codigo) {
        NotaCredito p = null;
        Query query = null;
        try {
            query = session.createQuery("from NotaCredito where codigo='" + codigo + "'");
            p = (NotaCredito) query.list().get(0);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            return p;
        }
    }

    public Long saveNotaCredito(NotaCredito p) {
        Long id = null;
        Transaction tx;
        if (session.getTransaction().isActive()) {
            tx = session.getTransaction();
        } else {
            tx = session.beginTransaction();
        }
        try {
            session.save(p);
            if (p.getId() != null) {
                id = p.getId();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
            this.errorMessage = e.getMessage();
        }
        return id;
    }

    public boolean updateNotaCredito(NotaCredito pOld) {
        boolean ok = false;
        Transaction tx;
        if (session.getTransaction().isActive()) {
            tx = session.getTransaction();
        } else {
            tx = session.beginTransaction();
        }

        try {

            NotaCredito pNew = session.load(NotaCredito.class, pOld.getId());

            pNew.setMonto(pOld.getMonto());
            pNew.setMotivo(pOld.getMotivo());

            session.merge(pNew);
            tx.commit();
            session.close();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
            this.errorMessage = e.getMessage();
        }
        return ok;
    }
}
