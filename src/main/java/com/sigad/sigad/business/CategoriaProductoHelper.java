/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import com.sigad.sigad.app.controller.LoginController;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class CategoriaProductoHelper {

    Session session = null;
    private static String errorMessage = "";

    public CategoriaProductoHelper() {
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

    /*Get all*/
    public ArrayList<ProductoCategoria> getCategorias() {
        ArrayList<ProductoCategoria> categorias = null;
        Query query = null;
        try {
            query = session.createQuery("from ProductoCategoria");
            categorias = (ArrayList<ProductoCategoria>) (query.list());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally {
            return categorias;
        }
    }

    ;
    
    /*Get profile by id, if nothin then null*/
    public ProductoCategoria getCategoria(int id) {
        ProductoCategoria p = null;
        Query query = null;
        try {
            query = session.createQuery("from ProductoCategoria where activo='true' and id=" + Integer.toString(id));

            p = (ProductoCategoria) query.list().get(0);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            return p;
        }
    }

    /*Get profile by name, if nothin then null*/
    public Perfil getCategoria(String nombre) {
        Perfil p = null;
        Query query = null;
        try {
            query = session.createQuery("from ProductoCategoria where nombre='" + nombre + "' and activo='true' ");
            p = (Perfil) query.list().get(0);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            return p;
        }
    }

    public Long saveCategoria(ProductoCategoria p) {
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

    
}
