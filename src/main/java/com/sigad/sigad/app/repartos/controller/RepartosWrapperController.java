/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXButton;
import com.sigad.sigad.utils.ui.UIFuncs;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class RepartosWrapperController implements Initializable {
    public static final String VIEW_PATH =
            "/com/sigad/sigad/repartos/view/repartosWrapper.fxml";

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton menuBtn;
    @FXML
    private VBox box;
    private StackPane parentStackPane;

    private long repartidorId;

    public RepartosWrapperController(StackPane stackPane,
            long repartidorId) {
        this.parentStackPane = stackPane;
        setRepartidorId(repartidorId);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setListarNode();
        } catch (IOException ex) {
            Logger.getLogger(VehiculoTipoController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    private void setListarNode() throws IOException {
        RepartosListaController controller =
                new RepartosListaController(stackPane, repartidorId);
        Node node =
                UIFuncs.<RepartosListaController>createNodeFromControllerFXML(
                        controller,
                        RepartosListaController.VIEW_PATH);
        box.getChildren().setAll(node);
    }

    /**
     * @return the repartidorId
     */
    public long getRepartidorId() {
        return repartidorId;
    }

    /**
     * @param repartidorId the repartidorId to set
     */
    public void setRepartidorId(long repartidorId) {
        this.repartidorId = repartidorId;
    }
}
