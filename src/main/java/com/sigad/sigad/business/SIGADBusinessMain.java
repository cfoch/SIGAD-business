/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author cfoch
 */
public class SIGADBusinessMain {
    public static void main(String[] args) {
        Configuration config;
        SessionFactory sessionFactory;
        Session session;

        config = new Configuration();
        config.configure("hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        session = sessionFactory.openSession();

        session.beginTransaction();

        
        Perfil perfil1 = new Perfil();
        perfil1.setNombre("cliente");
        perfil1.setDescripcion("Perfil de cliente");
        perfil1.setActivo(true);

        session.save(perfil1);

        session.getTransaction().commit();
        
        session.close();
        sessionFactory.close();
    }
}
