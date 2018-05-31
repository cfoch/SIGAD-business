/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.Tienda;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class ProductoHelper {

    Session session = null;
    private String errorMessage = "";

    public ProductoHelper() {
        session = LoginController.serviceInit();
        session.beginTransaction();
        // Add new Employee object
//        Producto prod = new Producto("Rosas", "/images/rosa.jpg", 15, 12.0,Boolean.TRUE);
//        Producto prod2 = new Producto("Chocolates", "/images/chocolate.jpg", 15,12.0, Boolean.TRUE);
//        session.save(prod);
//        session.save(prod2);
    }

    public ArrayList<Producto> getProducts() {
        ArrayList<Producto> list = null;
        try {
            Query query = session.createQuery("from Producto");

            if (!query.list().isEmpty()) {
                list = (ArrayList<Producto>) query.list();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        }

        return list;

    }

    ;
    public ArrayList<Producto> getProductsByTends() {
        ArrayList<Producto> listTienda = null;
        try {
            Query query = session.createQuery("from Tienda where id='" + LoginController.user.getTienda().getId() + "'");
            Tienda tienda = (Tienda) query;
           
            tienda.getCapacidadTiendas().forEach((t) -> {
            });

            if (!query.list().isEmpty()) {
                //ist = (ArrayList<Tienda>) query.list();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        }
        return null;
       //return list;

    }

    ;
    public Producto getProductById(Integer id) {
        Producto product = null;
        Query query = null;
        try {
            query = session.createQuery("from Producto where id='" + id + "'");

            if (!query.list().isEmpty()) {
                product = (Producto) query.list().get(0);
            }
        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return product;
    }

    ;
    
    public void close() {
        session.getTransaction().commit();
    }

}
