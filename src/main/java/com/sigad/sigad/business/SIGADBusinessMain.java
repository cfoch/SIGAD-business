/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.insumos.controller.InsumoController;
/**
 *
 * @author cfoch
 */
public class SIGADBusinessMain extends Application{
//    public static void main(String[] args) {
//        Configuration config;
//        SessionFactory sessionFactory;
//        Session session;
//
//        config = new Configuration();
//        config.configure("hibernate.cfg.xml");
//        sessionFactory = config.buildSessionFactory();
//        session = sessionFactory.openSession();
//
//        session.beginTransaction();
//
//        
//        Perfil perfil1 = new Perfil();
//        perfil1.setNombre("cliente");
//        perfil1.setDescripcion("Perfil de cliente");
//        perfil1.setActivo(true);
//
//        session.save(perfil1);
//
//        session.getTransaction().commit();
//        
//        session.close();
//        sessionFactory.close();
//    }
    
    @Override
	public void start(Stage stage) {
            try {
                    Parent root = FXMLLoader.load(getClass().getResource(LoginController.viewPath));
                    Scene scene = new Scene(root);
                    //scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
                    stage.setScene(scene);
                    stage.show();
            } catch(Exception e) {
                    e.printStackTrace();
            }
	}
	
	public static void main(String[] args) {
            launch(args);
	}
}
