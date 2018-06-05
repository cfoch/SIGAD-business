/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.helpers.AlgoritmoHelper;
import com.sigad.sigad.db.DBPopulator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
/**
 *
 * @author cfoch
 */

public class SIGADBusinessMain extends Application{
//    public static void main(String[] args) throws NoSuchAlgorithmException {
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
//        Usuario u= new Usuario();
//        MessageDigest digest=MessageDigest.getInstance("MD5");
//        String pass="test";
//        digest.update(pass.getBytes());
//        String hash=digest.digest().toString();
//        u.setPassword(hash);
//        session.save(u);
//
//        session.getTransaction().commit();
//        
//        session.close();
//        sessionFactory.close();
//    }
     @Override
	public void start(Stage stage) {
            try {
                    AlgoritmoHelper helperAlgo = new AlgoritmoHelper();
                    DBPopulator.populateVehiculoTipo();
                    DBPopulator.populateVehiculo();
                    DBPopulator.populateTienda();
                    DBPopulator.populatePermisos();
                    DBPopulator.populatePerfiles();
                    DBPopulator.populateUsuario();
                    DBPopulator.populatePedidoEstado();
                    DBPopulator.populateProductoCategoria();
                    DBPopulator.populateProductoFragilidad();
                    DBPopulator.populateInsumo();
                    DBPopulator.populateProducto();
                    DBPopulator.populatePedido();

                    Session session = LoginController.serviceInit();
                    List<Tienda> tiendas = (List<Tienda>) session
                            .createQuery("from Tienda").list();
                    for (int i = 0; i < tiendas.size(); i++) {
                        try {
                            helperAlgo.autogenerarRepartos(tiendas.get(i), "T");
                        } catch (Exception ex) {
                            Logger.getLogger(SIGADBusinessMain.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                        i++;
                        System.out.println("Tienda " + i + "/" + tiendas.size());
                    };


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
