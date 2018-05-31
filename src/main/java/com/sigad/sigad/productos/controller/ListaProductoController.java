/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.productos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author chrs
 */
public class ListaProductoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private StackPane hiddenSp;

    @FXML
    private JFXTreeTableView<?> tblProductos;

    @FXML
    private JFXButton moreBtn;

    @FXML
    private JFXButton btnAdd;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
