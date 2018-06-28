/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.utils.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class UICRUDViewWrapperController extends UIBaseController
        implements Initializable {

    public static String VIEW_PATH =
             "/com/sigad/sigad/repartos/view/CRUDViewWrapper.fxml";
    @FXML
    private StackPane stackPane;
    @FXML
    private VBox box;
    @FXML
    private Label labelTitulo;

    private UICRUDViewListarController listarController;
    private Node listarNode;

    public UICRUDViewWrapperController(
            Class<? extends UICRUDViewListarController> listarControllerKlass)
            throws InstantiationException, IllegalAccessException {
        listarController = listarControllerKlass.newInstance();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listarController.setParentStackPane(getParentStackPane());
        listarNode = UIFuncs.createNodeFromControllerFXML(listarController,
                listarController.getViewPath());
        box.getChildren().add(listarNode);
    }

    /**
     * @return the listarController
     */
    public UICRUDViewListarController getListarController() {
        return listarController;
    }

    public void setTitulo(String titulo) {
        labelTitulo.setText(titulo);
    }

    @Override
    public String getViewPath() {
        return VIEW_PATH;
    }

    /**
     * @return the stackPane
     */
    public StackPane getStackPane() {
        return stackPane;
    }
}
