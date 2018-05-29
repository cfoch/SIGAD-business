/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPopup;
import com.sigad.sigad.utils.ui.UIFuncs.Dialogs;
import com.sigad.sigad.utils.ui.UIFuncs.Dialogs.SimplePopupMenuFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class VehiculoTipoController implements Initializable {

    public static enum Modo {
        EDITAR,
        CREAR,
        BORRAR
    };

    public static final String VIEW_PATH =
            "/com/sigad/sigad/repartos/view/vehiculoTipo.fxml";

    private VehiculoTipoController.Modo modo;

    //@FXML
    private JFXButton borrarBtn;
    //@FXML
    private JFXButton editarBtn;
    //@FXML
    private JFXButton nuevoBtn;
    //@FXML
    private JFXButton listarBtn;
    @FXML
    private JFXButton menuBtn;
    @FXML
    private VBox box;
    @FXML
    private StackPane stackPane;

    private SimplePopupMenuFactory<Modo> menuFactory;

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
        menuFactory = new SimplePopupMenuFactory<Modo>(Modo.values());

        resetMenuBtnVisibility();

        getMenuFactory().getButton(Modo.CREAR).setOnAction(
                new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    JFXDialog dialog;
                    Node node;
                    VehiculoTipoCrearController controller;
                    JFXButton button = new JFXButton(Dialogs.BUTTON.CREAR);
                    FXMLLoader loader = getCrearLoader();
                    node = (Node) loader.load();
                    controller = loader.<VehiculoTipoCrearController>getController();
                    controller.setCrearButton(button);
                    dialog = Dialogs.buildDialog(stackPane,
                            Dialogs.HEADINGS.EMPTY, node, button, false);
                    dialog.setOnDialogClosed(closeEvent -> {
                        try {
                            setListarNode();
                            resetMenuBtnVisibility();
                        } catch (IOException ex) {
                            Logger.getLogger(VehiculoTipoController.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    });
                    dialog.show();

                } catch (IOException ex) {
                    Logger.getLogger(VehiculoTipoController.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    private void onMenuBtnClicked(MouseEvent e) {
        JFXPopup popup;
        popup = getMenuFactory().getPopup();
        popup.show(menuBtn);
    }

    /**
     * @return the modo
     */
    public VehiculoTipoController.Modo getModo() {
        return modo;
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
        controller.setMainController(this);
        box.getChildren().setAll(node);
    }

    private FXMLLoader getCrearLoader() throws IOException {
        Node node;
        URL resource;
        String resourcePath;
        FXMLLoader loader;
        VehiculoTipoCrearController controller;

        resourcePath = VehiculoTipoCrearController.VIEW_PATH;
        resource = getClass().getResource(resourcePath);

        loader = new FXMLLoader(resource);

        return loader;
    }

    /**
     * @return the menuFactory
     */
    public SimplePopupMenuFactory<Modo> getMenuFactory() {
        return menuFactory;
    }

    public void resetMenuBtnVisibility() {
        getMenuFactory().getButton(Modo.EDITAR).setDisable(true);
        getMenuFactory().getButton(Modo.BORRAR).setDisable(true);
    }
}