/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.ProductoCategoria;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author jorgito-stark
 */
public class ProductoCategoriaHelper {

    Session session = null;

    public ProductoCategoriaHelper() {
        session = LoginController.serviceInit();
        session.beginTransaction();

    }

    public ArrayList<ProductoCategoria> getProductCategories() {
        ArrayList<ProductoCategoria> list = null;
        try {
            Query query = session.createQuery("from ProductoCategoria");

            if (!query.list().isEmpty()) {
                list = (ArrayList<ProductoCategoria>) query.list();
            }
        } catch (Exception e) {
        }

        return list;
    }

    ;
    
    public ArrayList<ProductoCategoria> getActiveProductCategories() {
        ArrayList<ProductoCategoria> list = null;
        try {
            Query query = session.createQuery("from ProductoCategoria where activo = true");
            list = (ArrayList<ProductoCategoria>) query.list();
        } catch (Exception e) {
        }

        return list;
    }

    ;
    
    public ProductoCategoria getProductCategoryByName(String name) {
        ProductoCategoria category = null;
        Query query = null;
        try {
            query = session.createQuery("from ProductoCategoria where nombre='" + name + "'");

            if (!query.list().isEmpty()) {
                category = (ProductoCategoria) (query.list().get(0));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            return category;
        }
    }

    public void close() {
        session.getTransaction().commit();
    }
}
