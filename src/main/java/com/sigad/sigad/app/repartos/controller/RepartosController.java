/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Vehiculo;
import com.sigad.sigad.business.helpers.TiendaHelper;
import com.sigad.sigad.business.helpers.VehiculoHelper;
import com.sigad.sigad.utils.ui.UICRUDViewWrapperController;
import com.sigad.sigad.utils.ui.UIFuncs;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import com.sigad.sigad.utils.ui.UIFuncs.Dialogs.SimplePopupMenuFactory;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.text.Text;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class RepartosController implements Initializable {

    @FXML
    private JFXTabPane tabPane;

    public static String VIEW_PATH =
            "/com/sigad/sigad/repartos/view/repartos.fxml";

    private HomeController homeController;

    private Map<Label, SIGADRepartosMainMenuType> mainMenu;

    private JFXListView<Label> mainListView;
    @FXML
    private AnchorPane hiddenSpVehiculos;
    @FXML
    private AnchorPane hiddenSpRepartidores;
    @FXML
    private AnchorPane hiddenSpRepartos;
 
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
//        VEHICULOS_TIPO {
//            @Override
//            public final String toString() {
//                return "Tipos de vehículos";
//            }
//        },
        REPARTOS {
            @Override
            public final String toString() {
                return "Repartos";
            }
        },
        VEHICULOS {
            @Override
            public final String toString() {
                return "Vehículos";
            }
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Repartos
//        initializeMainMenu();
        connectMainMenuClickActions();
        
        initFirstMenu();
    }
    
    private void initFirstMenu() {
        try {
            System.out.println("gg");
            Node nd;
            URL resource;
            String resourcePath;
            FXMLLoader loader;

            resourcePath = VehiculoController.viewPath;
            resource = getClass().getResource(resourcePath);

            loader = new FXMLLoader();

            nd = (Node) loader.load(resource);
            
            hiddenSpVehiculos.getChildren().setAll(nd);
        } catch (IOException ex) {
            Logger.getLogger(RepartosController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
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
//        ReadOnlyObjectProperty<Label> selectedItemProp;

        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab oldValue, Tab newValue) {
                System.out.println("Changed oldValue:" + oldValue.getText() + " newValue:"+ newValue.getText());
                SIGADRepartosMainMenuType menuType;
                switch (newValue.getText()){
                    case "Vehiculos":
                        menuType = SIGADRepartosMainMenuType.VEHICULOS;
                        break;
                    case "Repartidores":
                        menuType = SIGADRepartosMainMenuType.REPARTIDORES;
                        break;
                    case "Repartos":
                        menuType = SIGADRepartosMainMenuType.REPARTOS;
                        break;
                    default:
                        menuType = SIGADRepartosMainMenuType.VEHICULOS;
                }
                switch (menuType) {
                case REPARTIDORES: {
                    try {
                        Node nd;
                        URL resource;
                        String resourcePath;
                        FXMLLoader loader;

                        resourcePath = RepartidoresInnerController.viewPath;
                        resource = getClass().getResource(resourcePath);

                        loader = new FXMLLoader();

                        nd = (Node) loader.load(resource);

//                        homeController.getFirstPanel().getChildren().setAll(nd);
                        hiddenSpRepartidores.getChildren().setAll(nd);
                    } catch (IOException ex) {
                        Logger.getLogger(HomeController.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case VEHICULOS:

                    try {
                        Node nd;
                        URL resource;
                        String resourcePath;
                        FXMLLoader loader;

                        resourcePath = VehiculoController.viewPath;
                        resource = getClass().getResource(resourcePath);

                        loader = new FXMLLoader();

                        nd = (Node) loader.load(resource);

//                        homeController.getFirstPanel().getChildren().setAll(nd);
                        hiddenSpVehiculos.getChildren().setAll(nd);
                    } catch (IOException ex) {
                        Logger.getLogger(HomeController.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                    break;
                case REPARTOS:
                {
                    try {
                        Node nodeWrapper;

                        System.out.println("cls: " + UICRUDViewWrapperController.VIEW_PATH);

                        GrupoRepartosController controllerWrapper =
                            new GrupoRepartosController(
                                    GrupoRepartosLista.class);

                        nodeWrapper = UIFuncs.<GrupoRepartosController>
                                createNodeFromControllerFXML(controllerWrapper,
                                        GrupoRepartosController.VIEW_PATH);
                        controllerWrapper.getListarController()
                                .setParentStackPane(
                                        controllerWrapper.getStackPane());
//                        homeController.getFirstPanel().getChildren().setAll(nodeWrapper);
                        hiddenSpRepartos.getChildren().setAll(nodeWrapper);
                    } catch (InstantiationException | IllegalAccessException ex) {
                        Logger.getLogger(RepartosController.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
            }
        }); 
    }
}
