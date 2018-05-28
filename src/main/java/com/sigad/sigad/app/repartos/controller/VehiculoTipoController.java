/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXButton;
import static com.sigad.sigad.app.repartos.controller.VehiculoTipoController.Modo.LISTAR;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class VehiculoTipoController implements Initializable {

    static public enum Modo {
        EDITAR,
        CREAR,
        LISTAR,
        BORRAR
    };

    public static final String VIEW_PATH =
            "/com/sigad/sigad/repartos/view/vehiculoTipo.fxml";

    private VehiculoTipoController.Modo modo;

    @FXML
    private JFXButton borrarBtn;
    @FXML
    private JFXButton editarBtn;
    @FXML
    private JFXButton nuevoBtn;
    @FXML
    private JFXButton listarBtn;
    @FXML
    private VBox box;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setModo(Modo.LISTAR);
        } catch (IOException ex) {
            Logger.getLogger(VehiculoTipoController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onNuevoButtonClicked(MouseEvent e) {
        try {
            setModo(Modo.CREAR);
        } catch (IOException ex) {
            Logger.getLogger(VehiculoTipoController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onEditarButtonClicked(MouseEvent e) {
        try {
            setModo(Modo.EDITAR);
        } catch (IOException ex) {
            Logger.getLogger(VehiculoTipoController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onBorrarButtonClicked(MouseEvent e) {
        try {
            setModo(Modo.BORRAR);
        } catch (IOException ex) {
            Logger.getLogger(VehiculoTipoController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onListarButtonClicked(MouseEvent e) {
        try {
            setModo(Modo.LISTAR);
        } catch (IOException ex) {
            Logger.getLogger(VehiculoTipoController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the modo
     */
    public VehiculoTipoController.Modo getModo() {
        return modo;
    }

    /**
     * @param modo the modo to set
     */
    public void setModo(VehiculoTipoController.Modo modo) throws IOException {
        this.modo = modo;
        switch (modo) {
            case EDITAR:
                break;
            case CREAR:
                break;
            case LISTAR:
                setListarNode();
                break;
            case BORRAR:
                break;
        }
    }

    private void setListarNode() throws IOException {
        Node node;
        URL resource;
        String resourcePath;
        FXMLLoader loader;
        VehiculoTipoListaController controller;

        resourcePath = VehiculoTipoListaController.VIEW_PATH;
        resource = getClass().getResource(resourcePath);

        loader = new FXMLLoader(resource);

        node = (Node) loader.load();
        controller = loader.<VehiculoTipoListaController>getController();
        box.getChildren().setAll(node);
    }
}
