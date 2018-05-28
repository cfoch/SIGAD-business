/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.sigad.sigad.business.Permiso;
import com.sigad.sigad.business.helpers.PermisoHelper;
import com.sigad.sigad.controller.cargaMasiva.CargaMasivaViewController;
import com.sigad.sigad.personal.controller.PersonalController;
import com.sigad.sigad.pedido.controller.SeleccionarProductosController;
import com.sigad.sigad.deposito.controller.FXMLAlmacenIngresoListaOrdenCompraController;
import com.sigad.sigad.perfil.controller.PerfilController;
import com.sigad.sigad.tienda.controller.TiendaController;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    public static final String viewPath = "/com/sigad/sigad/app/view/home.fxml";
    public static String windowName = "Home";
    @FXML
    private JFXButton profileBtn, productoBtn,offertBtn;
    @FXML
    private ArrayList<MaterialDesignIcon> sidebarIcons = new ArrayList<MaterialDesignIcon>();
    private ArrayList<JFXButton> sidebarBtns = new ArrayList<JFXButton>();
    @FXML
    private JFXButton workersBtn,refundBtn,statisticBtn,settingsBtn;
    @FXML
    private JFXButton menuBtn,menuProfile;
    @FXML
    private AnchorPane containerPane, firstPanel;
    @FXML
    private AnchorPane sidebarPane;
    @FXML
    private AnchorPane menuPanel;
    @FXML
    private JFXPopup popup;
    public static final int SIDEBAR_BUTTON_HEIGHT = 70;
    public static final int SIDEBAR_BUTTON_GAP = 20;
    public static final Pos SIDEBAR_BUTTON_ALIGNMENT = Pos.BASELINE_LEFT;
    public static final String SIDEBAR_BUTTON_ICON_SIZE = "30";
 
    private Color mainColor = new Color(0.27, 0.31, 0.42, 1);
    private double  baseTop = 80.0;

    private enum Container{
        SIDEBAR, MENU, PANEL
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initsidebar();
    }   
    
    private void initsidebar(){
        
        PermisoHelper helper = new PermisoHelper();
        ArrayList<Permiso> list = helper.getPermissions();
        
        if(list != null){
            for (int i = 0; i < list.size(); i++) {
                sidebarBtns.add(new JFXButton(list.get(i).getMenu()));
                sidebarIcons.add(matchIconText(list.get(i).getIcono()));
                dinamicSideMenu(i, sidebarBtns.get(i), SIDEBAR_BUTTON_HEIGHT, SIDEBAR_BUTTON_GAP, SIDEBAR_BUTTON_ALIGNMENT,
                    sidebarIcons.get(i), SIDEBAR_BUTTON_ICON_SIZE);
            }            
        }
        
        for (int i = 0; i < list.size() && list != null; i++) {
            String name = list.get(i).getMenu();
            
            sidebarBtns.get(i).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            Node node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(PersonalController.viewPath));;
                            if(name.equals("Productos")){
                                node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(SeleccionarProductosController.viewPath));
                            }else if(name.equals("Insumos")){
                                
                            }else if(name.equals("Personal")){
                                node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(PersonalController.viewPath));
                            }else if(name.equals("Repartos")){
                
                            }else if(name.equals("Pedidos")){
                                node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(FXMLAlmacenIngresoListaOrdenCompraController.viewPath));
                            }else if(name.equals("Tiendas")){
                                node = (Node) FXMLLoader.load(getClass().getResource(TiendaController.viewPath));
                            }else if(name.equals("Perfiles")){
                                node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(PerfilController.viewPath));
                            }else if(name.equals("Estadísticas")){
                            }else if(name.equals("Carga Masiva")){
                                node = (Node) FXMLLoader.load(getClass().getResource(CargaMasivaViewController.viewPath));
                            }else if(name.equals("Configuraciones")){
                                
                            }
                            
                            firstPanel.getChildren().setAll(node);
                        }catch (IOException ex) {
                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "sidebarBtns.get().setOnAction", ex);
                        }
                    }
                });
            
        }
                
    }
    
    private MaterialDesignIcon matchIconText(String search) {
        for (MaterialDesignIcon test : MaterialDesignIcon.values()){
            if(test.name() == null ? search == null : test.name().equals(search)){
                return test;
            }
        }
        return MaterialDesignIcon.BRIEFCASE_CHECK;
    }
    
    private void dinamicSideMenu(
            int count, JFXButton newButton, int height, int gap,
            Pos pos, MaterialDesignIcon iconType, String size) {
        //sidebarBtns creation
        newButton.setPrefHeight(height);
        newButton.setGraphicTextGap(gap);
        newButton.setAlignment(pos);
        newButton.getStyleClass().add("sidebarBtn");
        
        //icon
        MaterialDesignIconView icon;
        icon = new MaterialDesignIconView(iconType);
        icon.setSize(size);
	icon.setFill(mainColor);
        
        newButton.setGraphic(icon);
        
        sidebarPane.getChildren().add(newButton);   
        
        AnchorPane.setTopAnchor(newButton, baseTop + 70*(count));
        AnchorPane.setLeftAnchor(newButton, 0.0);
        AnchorPane.setRightAnchor(newButton, 0.0);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        
        if(event.getSource() == profileBtn){
            //firstPanel.toFront();
        }if (event.getSource() == sidebarBtns.get(1)) {
            System.err.println(">>>>");
            
        }
        
        System.out.println(event.getEventType());
    }

    @FXML
    private void menuBtnClicked(MouseEvent event) {
        if(sidebarPane.getPrefWidth()==50){
            sidebarPane.setPrefWidth(200);
            AnchorPane.setLeftAnchor(menuPanel, 200.00);
            AnchorPane.setLeftAnchor(firstPanel, 200.00);
            
            addClassToContainer(Container.SIDEBAR,"active");
        }else{
            sidebarPane.setPrefWidth(50);
            AnchorPane.setLeftAnchor(menuPanel, 50.00);
            AnchorPane.setLeftAnchor(firstPanel, 50.00);
            
            removeClassFromContainer(Container.SIDEBAR,"active");
        }
    }
    @FXML
    private void menuProfileClicked(MouseEvent event) {
        initPopup();
        popup.show(menuProfile, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
    }
    
    private void addClassToContainer(Container container, String clase){
        switch (container){
            case SIDEBAR:
                profileBtn.getStyleClass().add(clase);
                productoBtn.getStyleClass().add(clase);
                offertBtn.getStyleClass().add(clase);
                workersBtn.getStyleClass().add(clase);
                refundBtn.getStyleClass().add(clase);
                statisticBtn.getStyleClass().add(clase);
                settingsBtn.getStyleClass().add(clase);
                break;
            case MENU:
                break;
            case PANEL:
                break;
        }
    }
    
    private void removeClassFromContainer(Container container, String clase){
        switch (container){
            case SIDEBAR:
                profileBtn.getStyleClass().removeAll(clase);
                productoBtn.getStyleClass().removeAll(clase);
                offertBtn.getStyleClass().removeAll(clase);
                workersBtn.getStyleClass().removeAll(clase);
                refundBtn.getStyleClass().removeAll(clase);
                statisticBtn.getStyleClass().removeAll(clase);
                settingsBtn.getStyleClass().removeAll(clase);
                break;
            case MENU:
                break;
            case PANEL:
                break;
        }
    }
    
    private void initPopup(){
        JFXButton bl = new JFXButton("Mi perfil");
        JFXButton bl1 = new JFXButton("Configuraciones");
        JFXButton bl2 = new JFXButton("Logout");
        
        bl.setPadding(new Insets(20));
        bl1.setPadding(new Insets(20));
        bl2.setPadding(new Insets(20));
        
        bl.setPrefHeight(40);
        bl1.setPrefHeight(40);
        bl2.setPrefHeight(40);
        
        bl.setPrefWidth(145);
        bl1.setPrefWidth(145);
        bl2.setPrefWidth(145);
        
        VBox vBox = new VBox(bl, bl1, bl2);
        
        
        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }
    
//    public static void changeChildren(Node node){
//        firstPanel.getChildren().setAll(node);
//    }
    
    
}
