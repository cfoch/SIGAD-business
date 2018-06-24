/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.tienda.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Vehiculo;
import com.sigad.sigad.business.helpers.TiendaHelper;
import com.sigad.sigad.utils.ui.UIFuncs;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.hibernate.Session;


/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class TiendaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/tienda/view/tienda.fxml";
    @FXML
    private JFXTreeTableView<Store> storeTbl;
    static ObservableList<Store> data;
    @FXML
    private StackPane hiddenSp;
    @FXML
    private JFXButton moreBtn;
    @FXML
    private JFXButton addBtn;
    /*Extras*/
    public static JFXDialog storeDialog;
    private JFXPopup popup;
    
    public static boolean isStoreCreate;
    public static Store selectedStore = null;
    @FXML
    private JFXTextField filtro;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        initStoreTbl();
        agregarFiltro();
    }   
    
    public void agregarFiltro() {
        filtro.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            storeTbl.setPredicate((TreeItem<Store> t) -> {
                Boolean flag = t.getValue().capacidad.getValue().contains(newValue) || t.getValue().dirección.getValue().contains(newValue) || t.getValue().descripcion.getValue().contains(newValue) || t.getValue().activo.getValue().contains(newValue);
                return flag;
            });
        });
    }
    public static void updateTable(Tienda u) {
        data.add(
                new Store(
                        u.getId(),
                        new SimpleStringProperty(String.valueOf(u.getCapacidad())),
                        new SimpleStringProperty(String.valueOf(u.getCooXDireccion())),
                        new SimpleStringProperty(String.valueOf(u.getCooYDireccion())),
                        new SimpleStringProperty(u.getDescripcion()),
                        new SimpleStringProperty(u.getDireccion()),
                        new SimpleStringProperty(u.isActivo()? "Activo": "Inactivo")
                ));
    }
    
    private void getDataFromDB() {
        TiendaHelper helper = new TiendaHelper();
        
        ArrayList<Tienda> lista = helper.getStores();
        if(lista != null){
            lista.forEach((p) -> {
                updateTable(p);
            });
        }
        helper.close();
    }
    
    @FXML
    void handleAction(ActionEvent event) {
        if(event.getSource() == addBtn){
            try {
                CreateEdditStoreDialog(true);
            } catch (IOException ex) {
                Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
            }
        }else if(event.getSource() == moreBtn){
            int count = storeTbl.getSelectionModel().getSelectedCells().size();
            if( count > 1){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla", "Ok", hiddenSp);
            }else if(count<=0){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "Ok", hiddenSp);
            }else{
                int selected  = storeTbl.getSelectionModel().getSelectedIndex();
                selectedStore = (Store) storeTbl.getSelectionModel().getModelItem(selected).getValue();
                initPopup();
                popup.show(moreBtn, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
            }
            
        }
    }
    
    private void initPopup(){
        JFXButton edit = new JFXButton("Editar");
        JFXButton delete = new JFXButton("Eliminar");
        JFXButton verVehiculos = new JFXButton("Ver vehículos");
        
        edit.setOnAction((ActionEvent event) -> {
            popup.hide();
            try {
                CreateEdditStoreDialog(false);
            } catch (IOException ex) {
                Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, "initPopup(): CreateEdditStoreDialog()", ex);
            }
        });
        
        delete.setOnAction((ActionEvent event) -> {
            popup.hide();
            deleteUserDialog();
        });

        verVehiculos.setOnAction((event) -> {
            JFXListView<Label> listView = new JFXListView<Label>();
            Tienda tienda;
            Session session;
            popup.hide();

            if (selectedStore == null) {
                UIFuncs.Dialogs.showMsg(
                        hiddenSp,
                        UIFuncs.Dialogs.HEADINGS.ERROR,
                        "Debe seleccionar una tienda.",
                        UIFuncs.Dialogs.BUTTON.CERRAR);
                return;
            }

            session = LoginController.serviceInit();
            try {
                tienda = (Tienda) session
                        .createQuery("from Tienda where id = :idTienda")
                        .setParameter("idTienda", selectedStore.id)
                        .getSingleResult();
                for (Vehiculo vehiculo : tienda.getVehiculos()) {
                    Label label = new Label(String.format("%s - %s",
                            vehiculo.getPlaca(), vehiculo.getNombre()));
                    listView.getItems().add(label);
                }
                session.close();
                UIFuncs.Dialogs.showDialog(hiddenSp,
                        "Vehículos",
                        listView,
                        new JFXButton("Cerrar"),
                        true);
            } catch (Exception ex) {
                try {
                    session.close();
                } catch (Exception ex1) {
                }
                UIFuncs.Dialogs.showMsg(
                        hiddenSp,
                        UIFuncs.Dialogs.HEADINGS.ERROR,
                        "Hubo un error al mostrar las tiendas.",
                        UIFuncs.Dialogs.BUTTON.CERRAR);
                return;
            }
        });
        
        edit.setPadding(new Insets(20));
        edit.setPrefSize(145, 40);
        delete.setPadding(new Insets(20));
        delete.setPrefSize(145, 40);
        verVehiculos.setPadding(new Insets(20));
        verVehiculos.setPrefSize(145, 40);
        
        VBox vBox = new VBox(edit, delete,verVehiculos);
        
        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }

    public void CreateEdditStoreDialog(boolean iscreate) throws IOException {
        isStoreCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        if(isStoreCreate){
            content.setHeading(new Text("Crear Tienda"));
        }else{
            content.setHeading(new Text("Editar Tienda"));
            
            TiendaHelper helper = new TiendaHelper();
            Tienda u = helper.getStore(Double.valueOf(selectedStore.cooxdireccion.getValue()), Double.valueOf(selectedStore.cooydireccion.getValue()));
            if(u == null){
                ErrorController error = new ErrorController();
                error.loadDialog("Error", "No se pudo obtener la tienda", "Ok", hiddenSp);
                System.out.println("Error al obtener la tienda");
                return;
            }
        }
        
        Node node;
        node = (Node) FXMLLoader.load(TiendaController.this.getClass().getResource(CrearEditarTiendaController.viewPath));
        content.setBody(node);
        //content.setPrefSize(400,400);
                
        storeDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        storeDialog.show();
    }  
    
    private void deleteUserDialog() {
        JFXDialogLayout content =  new JFXDialogLayout();
        content.setHeading(new Text("Error"));
        content.setBody(new Text("Cuenta o contraseña incorrectas"));
                
        JFXDialog dialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        content.setActions(button);
        dialog.show();
    }

    private void initStoreTbl() {
        JFXTreeTableColumn<TiendaController.Store, String> capacidad = new JFXTreeTableColumn<>("Capacidad");
        capacidad.setPrefWidth(100);
        capacidad.setCellValueFactory((TreeTableColumn.CellDataFeatures<TiendaController.Store, String> param) -> param.getValue().getValue().capacidad);
        
        JFXTreeTableColumn<TiendaController.Store, String> coorxdireccion = new JFXTreeTableColumn<>("Coordenada X");
        coorxdireccion.setPrefWidth(100);
        coorxdireccion.setCellValueFactory((TreeTableColumn.CellDataFeatures<TiendaController.Store, String> param) -> param.getValue().getValue().cooxdireccion);
        
        JFXTreeTableColumn<TiendaController.Store, String> coorydireccion = new JFXTreeTableColumn<>("Coordenada Y");
        coorydireccion.setPrefWidth(100);
        coorydireccion.setCellValueFactory((TreeTableColumn.CellDataFeatures<TiendaController.Store, String> param) -> param.getValue().getValue().cooydireccion);
        
        JFXTreeTableColumn<TiendaController.Store, String> direccion = new JFXTreeTableColumn<>("Dirección");
        direccion.setPrefWidth(300);
        direccion.setCellValueFactory((TreeTableColumn.CellDataFeatures<TiendaController.Store, String> param) -> param.getValue().getValue().dirección);
        
        JFXTreeTableColumn<TiendaController.Store, String> descripcion = new JFXTreeTableColumn<>("Descripción");
        descripcion.setPrefWidth(200);
        descripcion.setCellValueFactory((TreeTableColumn.CellDataFeatures<TiendaController.Store, String> param) -> param.getValue().getValue().descripcion);
        
        JFXTreeTableColumn<TiendaController.Store, String> active = new JFXTreeTableColumn<>("Activo");
        active.setPrefWidth(80);
        active.setCellValueFactory((TreeTableColumn.CellDataFeatures<TiendaController.Store, String> param) -> param.getValue().getValue().activo);
        
        storeTbl.getColumns().add(capacidad);
        storeTbl.getColumns().add(coorxdireccion);
        storeTbl.getColumns().add(coorydireccion);
        storeTbl.getColumns().add(direccion);
        storeTbl.getColumns().add(descripcion);
        storeTbl.getColumns().add(active);
        
        //DB
        getDataFromDB();
        
        //Double click on row
        storeTbl.setRowFactory(ord -> {
            JFXTreeTableRow<Store> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked((event) -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                     && event.getClickCount() == 2) {
                    Store clickedRow = row.getItem();
                    System.out.println(clickedRow.dirección);
                    
                    selectedStore = clickedRow;
                    //data.remove(selectedUser);
                    try {
                        CreateEdditStoreDialog(false);
                    } catch (IOException ex) {
                        Logger.getLogger(TiendaController.class.getName()).log(Level.SEVERE, "initUserTbl(): CreateEdditUserDialog()", ex);
                    }
                }
            });
            return row;
        });
        
        final TreeItem<TiendaController.Store> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        
        storeTbl.setEditable(false);
        storeTbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);        
        storeTbl.getColumns().setAll(direccion, capacidad,coorxdireccion, coorydireccion, descripcion, active);
        storeTbl.setRoot(root);
        storeTbl.setShowRoot(false);
    }
    
    public static class Store  extends RecursiveTreeObject<Store> {
        Long id;
        StringProperty capacidad;
        StringProperty cooxdireccion;
        StringProperty cooydireccion;
        StringProperty descripcion;
        StringProperty dirección;
        StringProperty activo;

        public Store(Long id, StringProperty capacidad, StringProperty cooxdireccion, StringProperty cooydireccion, StringProperty descripcion, StringProperty dirección, StringProperty activo) {
            this.id = id;
            this.capacidad = capacidad;
            this.cooxdireccion = cooxdireccion;
            this.cooydireccion = cooydireccion;
            this.descripcion = descripcion;
            this.dirección = dirección;
            this.activo = activo;
        }
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof Store) {
                Store s = (Store) o;
                return s.cooxdireccion== this.cooxdireccion && s.cooydireccion==this.cooydireccion;
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    
    
    
}