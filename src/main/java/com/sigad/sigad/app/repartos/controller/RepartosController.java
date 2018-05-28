/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXListView;
import com.sigad.sigad.app.controller.HomeController;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class RepartosController implements Initializable {

    /**
     * @return the homeController
     */
    public HomeController getHomeController() {
        return homeController;
    }

    /**
     * @param homeController the homeController to set
     */
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public static enum SIGADRepartosMainMenuType {
        REPARTIDORES {
            @Override
            public final String toString() {
                return "Repartidores";
            }
        },
        VEHICULOS_TIPO {
            @Override
            public final String toString() {
                return "Tipos de vehículos";
            }
        },
        VEHICULOS {
            @Override
            public final String toString() {
                return "Vehículos";
            }
        }
    }

    public static String VIEW_PATH =
            "/com/sigad/sigad/repartos/view/repartos.fxml";

    private HomeController homeController;

    private Map<Label, SIGADRepartosMainMenuType> mainMenu;

    @FXML
    private JFXListView<Label> mainListView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initializeMainMenu();
    }

    private void initializeMainMenu() {
        mainMenu = new HashMap<>();
        for (SIGADRepartosMainMenuType item :
                SIGADRepartosMainMenuType.values()) {
            Label label;
            label = new Label(item.toString());
            mainMenu.put(label, item);
            mainListView.getItems().add(label);
        }
        // UI settings.
        mainListView.setExpanded(true);
        mainListView.setDepth(1);

        connectMainMenuClickActions();
    }

    private void connectMainMenuClickActions() {
        ReadOnlyObjectProperty<Label> selectedItemProp;
        selectedItemProp =
                mainListView.getSelectionModel().selectedItemProperty();
        selectedItemProp.addListener((ObservableValue<? extends Label> obs,
                Label oldValue, Label selectedLabel) -> {
            SIGADRepartosMainMenuType menuType;
            menuType = mainMenu.get(selectedLabel);
            switch (menuType) {
                case REPARTIDORES: {
                    break;
                }
                case VEHICULOS_TIPO: {
                    try {
                        Node nd;
                        URL resource;
                        String resourcePath;
                        FXMLLoader loader;

                        resourcePath = VehiculoTipoController.VIEW_PATH;
                        resource = getClass().getResource(resourcePath);

                        loader = new FXMLLoader();

                        nd = (Node) loader.load(resource);

                        homeController.getFirstPanel().getChildren().setAll(nd);
                    } catch (IOException ex) {
                        Logger.getLogger(HomeController.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case VEHICULOS:
                    break;
            }
        });
    }
}
