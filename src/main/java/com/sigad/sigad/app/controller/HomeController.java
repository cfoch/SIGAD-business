/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXMasonryPane;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import java.awt.event.ActionListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    public static String viewPath = "/com/sigad/sigad/app/view/home.fxml";
    public static String windowName = "Home";
    @FXML
    private JFXButton profileBtn, productoBtn,offertBtn;
    @FXML
    private JFXButton workersBtn,settingsBtn, menuBtn;
    @FXML
    private AnchorPane secondPane, firstPane;
    @FXML
    private JFXMasonryPane sidebarPane;
    @FXML
    private AnchorPane menuPanel;
    @FXML
    private Label profileLbl,productLbl,offertLbl,workersLbl,settingsLbl;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        profileLbl.setVisible(false);
        productLbl.setVisible(false);
        offertLbl.setVisible(false);
        workersLbl.setVisible(false);
        settingsLbl.setVisible(false);
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == profileBtn){
            System.out.println("entro pe");
            firstPane.toFront();
            
        }else if(event.getSource() == productoBtn){
            secondPane.toFront();
        }
    }

    @FXML
    private void menuBtnClicked(MouseEvent event) {
        if(sidebarPane.isVisible()){
            sidebarPane.setVisible(false);
            
        }else{
            sidebarPane.setVisible(true);
        }
    }
    
}
