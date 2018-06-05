/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.PedidoEstado;
import com.sigad.sigad.business.Tienda;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class PedidoHelper {

    Session session = null;
    private String errorMessage = "";

    public PedidoHelper() {
        session = LoginController.serviceInit();
    }

    /*Close session*/
    public void close() {
        session.close();
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /*Get all the users*/
    public Long savePedido(Pedido pedido) {
        Long id = null;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }
            
            session.save(pedido);
            if (pedido.getId()== null) {
                id = pedido.getId();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        } finally {
            return id;
        }
    }

    public List<Pedido> getPedidosPorTienda(Tienda tienda, PedidoEstado estado,
            String turno) throws Exception {
        String hql;
        Query query;
        String hqlBase = "from Pedido where tienda_id = :tienda_id";
        List<Pedido> pedidos = new ArrayList<>();

        hql = hqlBase;
        if (turno != null) {
            hql = hql + " and turno = :turno";
        }
        if (estado != null) {
            hql = hql + " and estado_id = :estado_id";
        }

        query = session.createQuery(hql);

        if (turno != null) {
            query.setParameter("turno", turno);
        }
        if (estado != null) {
            query.setParameter("estado_id", estado.getId());
        }
        query.setParameter("tienda_id", tienda.getId());
        pedidos = query.list();
        return pedidos;
    }

}