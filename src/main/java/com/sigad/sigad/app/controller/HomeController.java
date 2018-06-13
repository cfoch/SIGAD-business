/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.sigad.sigad.app.repartos.controller.RepartosController;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Permiso;
import com.sigad.sigad.controller.cargaMasiva.CargaMasivaViewController;
import com.sigad.sigad.descuentos.controller.MantenimientoDescuentosController;
import com.sigad.sigad.estadisticas.controller.ReportesController;
import com.sigad.sigad.insumos.controller.ListaInsumoController;
import com.sigad.sigad.movimientos.controller.MovimientosController;
import com.sigad.sigad.ordenescompra.controller.ListaOrdenesCompraController;
import com.sigad.sigad.pedido.controller.MantenimientoPedidosController;
import com.sigad.sigad.perfil.controller.PerfilController;
import com.sigad.sigad.personal.controller.PersonalController;
import com.sigad.sigad.productos.controller.ProductosIndexController;
import com.sigad.sigad.tienda.controller.TiendaController;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
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
import javafx.scene.layout.StackPane;
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
    public static final Double IGV = 0.18;
    public static String windowName = "Home";
    @FXML
    private JFXButton profileBtn, productoBtn,offertBtn, descuentosBtn;
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
    private StackPane stackPane;
    @FXML
    private AnchorPane sidebarPane;
    @FXML
    private AnchorPane menuPanel;
    @FXML
    private JFXPopup popup;
    public static final int SIDEBAR_BUTTON_HEIGHT = 50;
    public static final int SIDEBAR_BUTTON_GAP = 4;
    public static final Pos SIDEBAR_BUTTON_ALIGNMENT = Pos.BASELINE_LEFT;
    public static final String SIDEBAR_BUTTON_ICON_SIZE = "30";
 
    private Color mainColor = new Color(0.27, 0.31, 0.42, 1);
    private double  baseTop = 60.0;

    private enum Container{
        SIDEBAR, MENU, PANEL
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initsidebar();
        menuProfile.setText(LoginController.user.getNombres() + " " + LoginController.user.getApellidoPaterno());
    }   
    
    private void initsidebar(){
        // TODO: Encontrar una manera más al estilo Java de hacer esto.
        HomeController self = this;
        Perfil perfil = LoginController.user.getPerfil();
        Set<Permiso> permisos = perfil.getPermisos();
        if(permisos != null){
            List<Permiso> permisosSorted = permisos.stream().collect(Collectors.toList());
            Collections.sort(permisosSorted, (o1, o2) -> o1.getOrden()-o2.getOrden());
            
            Iterator<Permiso> itr = permisosSorted.iterator();
            int i = 0;
            while(itr.hasNext()){
                Permiso p = itr.next();
                sidebarBtns.add(new JFXButton(p.getMenu()));
                sidebarIcons.add(matchIconText(p.getIcono()));
                dinamicSideMenu(i, sidebarBtns.get(i), SIDEBAR_BUTTON_HEIGHT, SIDEBAR_BUTTON_GAP, SIDEBAR_BUTTON_ALIGNMENT,
                    sidebarIcons.get(i), SIDEBAR_BUTTON_ICON_SIZE);
                i++;
            }

            Iterator<Permiso> itr1 = permisosSorted.iterator();
            int j = 0;
            while(itr1.hasNext()){
                String name = itr1.next().getMenu();

                sidebarBtns.get(j).setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                Node node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(PersonalController.viewPath));;
                                if(name.equals("Productos")){
                                    node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(ProductosIndexController.viewPath));
                                }else if(name.equals("Ventas")){
                                    node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(MantenimientoPedidosController.viewPath));
                                }else if(name.equals("Insumos")){
                                    node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(ListaInsumoController.viewPath));
                                }else if(name.equals("Descuentos")){
                                    node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(MantenimientoDescuentosController.viewPath));
                                }else if(name.equals("Personal")){
                                    node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(PersonalController.viewPath));
                                }else if(name.equals("Repartos")){
                                    URL resource;
                                    String resourcePath;
                                    FXMLLoader loader;
                                    RepartosController controller;

                                    resourcePath =
                                            RepartosController.VIEW_PATH;
                                    resource = getClass().getResource(resourcePath);

                                    loader = new FXMLLoader(resource);

                                    node = (Node) loader.load();
                                    controller = loader
                                            .<RepartosController>getController();
                                    controller.setHomeController(self);

                                    getFirstPanel().getChildren().setAll(node);
                                }else if(name.equals("Pedidos")){
                                    node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(ListaOrdenesCompraController.viewPath));
                                }else if(name.equals("Tiendas")){
                                    node = (Node) FXMLLoader.load(getClass().getResource(TiendaController.viewPath));
                                }else if(name.equals("Perfiles")){
                                    node= (Node) FXMLLoader.load(HomeController.this.getClass().getResource(PerfilController.viewPath));
                                }else if(name.equals("Estadísticas")){
                                    node = (Node) FXMLLoader.load(HomeController.this.getClass().getResource(ReportesController.viewPath));
                                }else if(name.equals("Carga Masiva")){
                                    node = (Node) FXMLLoader.load(getClass().getResource(CargaMasivaViewController.viewPath));
                                }else if(name.equals("Configuraciones")){
                                    
                                }else if(name.equals("Movimientos")){
                                    node = (Node) FXMLLoader.load(getClass().getResource(MovimientosController.viewPath));
                                }

                                firstPanel.getChildren().setAll(node);
                            }catch (IOException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "sidebarBtns.get().setOnAction", ex);
                            }
                        }
                    });
                j++;
            }
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
        
        AnchorPane.setTopAnchor(
                newButton, baseTop + SIDEBAR_BUTTON_HEIGHT * (count));
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
            AnchorPane.setLeftAnchor(getFirstPanel(), 200.00);
            
//            addClassToContainer(Container.SIDEBAR,"active");
        }else{
            sidebarPane.setPrefWidth(50);
            AnchorPane.setLeftAnchor(menuPanel, 50.00);
            AnchorPane.setLeftAnchor(getFirstPanel(), 50.00);
            
//            removeClassFromContainer(Container.SIDEBAR,"active");
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
                descuentosBtn.getStyleClass().add(clase);
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
                descuentosBtn.getStyleClass().removeAll(clase);
                break;
            case MENU:
                break;
            case PANEL:
                break;
        }
    }
    
    private void initPopup(){
//        JFXButton perfil = new JFXButton("Mi perfil");
        JFXButton configurations = new JFXButton("Configuraciones");
        JFXButton logout = new JFXButton("Logout");
        
//        perfil.setPadding(new Insets(20));
        configurations.setPadding(new Insets(20));
        logout.setPadding(new Insets(20));
        
//        perfil.setPrefSize(150.0,40.0);
        configurations.setPrefSize(150.0,40.0);
        logout.setPrefSize(150.0,40.0);
        
        logout.setOnAction((ActionEvent event) -> {
            
            if(LoginController.serviceEnd()){
                LoginController.stage.close();
                Platform.setImplicitExit(true);
            }
        });
        
        VBox vBox = new VBox(configurations, logout);
        
        
        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }

    /**
     * @return the firstPanel
     */
    public AnchorPane getFirstPanel() {
        return firstPanel;
    }

    /**
     * @param firstPanel the firstPanel to set
     */
    public void setFirstPanel(AnchorPane firstPanel) {
        this.firstPanel = firstPanel;
    }
    
//    public static void changeChildren(Node node){
//        firstPanel.getChildren().setAll(node);
//    }

    /**
     * @return the stackPane
     */
    public StackPane getStackPane() {
        return stackPane;
    }
    
    
}