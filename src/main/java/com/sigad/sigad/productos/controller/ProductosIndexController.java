/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.productos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import com.sigad.sigad.business.helpers.ProductoHelper;
import com.sigad.sigad.business.helpers.UsuarioHelper;
import com.sigad.sigad.personal.controller.CrearEditarUsuarioController;
import com.sigad.sigad.personal.controller.PersonalController;
import static com.sigad.sigad.personal.controller.PersonalController.userDialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author jorgito-stark
 */
public class ProductosIndexController implements Initializable {
    public static  String viewPath = "/com/sigad/sigad/productos/view/ProductosIndex.fxml";
    
    @FXML
    private JFXTreeTableView<ProductoLista> productosTabla;
    @FXML
    private JFXButton addBtn;
    @FXML
    private JFXButton deleteBtn;
    @FXML
    private JFXButton editBtn;
    @FXML
    private StackPane hiddenSp;
    /*Extras*/
    @FXML
    public static JFXDialog productDialog;
    public static Producto selectedProduct = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeMainTable();
        initEvents();
    }
    
    private void initializeMainTable(){
        JFXTreeTableColumn<ProductoLista,String> productoId = new JFXTreeTableColumn<>("ID");
        productoId.setPrefWidth(50);
        productoId.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductoLista,String> param){
                return param.getValue().getValue().id;
            }
        });        
        
        JFXTreeTableColumn<ProductoLista,String> productoNombre = new JFXTreeTableColumn<>("Nombre");
        productoNombre.setPrefWidth(300);
        productoNombre.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductoLista,String> param){
                return param.getValue().getValue().nombre;
            }
        });
        
        JFXTreeTableColumn<ProductoLista,String> productoDescripcion = new JFXTreeTableColumn<>("Descripcion");
        productoDescripcion.setPrefWidth(500);
        productoDescripcion.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductoLista,String> param){
                return param.getValue().getValue().descripcion;
            }
        });        
        
        JFXTreeTableColumn<ProductoLista,String> productoActivo = new JFXTreeTableColumn<>("Estado");
        productoActivo.setPrefWidth(50);
        productoActivo.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductoLista,String> param){
                return param.getValue().getValue().estado;
            }
        });        
        
        JFXTreeTableColumn<ProductoLista,String> productoPrecio = new JFXTreeTableColumn<>("Precio");
        productoPrecio.setPrefWidth(100);
        productoPrecio.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductoLista,String> param){
                return param.getValue().getValue().precio;
            }
        });
        
        ObservableList<ProductoLista> productosList = FXCollections.observableArrayList();
        ProductoHelper productoHelper = new ProductoHelper();
        
        ArrayList<Producto> productos = productoHelper.getProducts();
        
        if(productos != null){
            productos.forEach((p) ->{
                Producto t = p;
                productosList.add(new ProductoLista(
                    String.valueOf(t.getId()),
                    t.getNombre(),
                    t.getDescripcion(),
                    t.isActivo()? "Activo":"Inactivo",
                    String.valueOf(t.getPrecio())
                ));
            });
        }
        
        productoHelper.close();
        /*
        JFXButton popupButton = new JFXButton("Popup");
        StackPane main = new StackPane();
        main.getChildren().add(popupButton);
        
        JFXPopup popup = new JFXPopup(productosTabla);
        popupButton.setOnAction(e -> popup.show(popupButton, PopupVPosition.TOP, PopupHPosition.LEFT));
        */
        final TreeItem<ProductoLista> root = new RecursiveTreeItem<ProductoLista>(productosList, RecursiveTreeObject::getChildren);
        productosTabla.getColumns().setAll(productoId,productoNombre,productoDescripcion,productoActivo,productoPrecio);
        productosTabla.setRoot(root);
        productosTabla.setShowRoot(false);        
    }
    
    private void initEvents(){
        addBtn.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                try {
                    System.out.println("Go Dialog");
                    ManageProduct(true);
                } catch (IOException ex) {
                    Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
                }
             }
         });
        
        editBtn.setOnMouseClicked(new EventHandler<Event>(){
            @Override
            public void handle(Event event) {
                try {
                    int selected  = productosTabla.getSelectionModel().getSelectedIndex();
                    ProductoLista selectedProduct = (ProductoLista) productosTabla.getSelectionModel().getModelItem(selected).getValue();
                    setCurrentProduct(selectedProduct);
                    ManageProduct(false);
                } catch (IOException ex) {
                    Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
                }
             }            
        });
    }
    
    public void ManageProduct(boolean iscreate) throws IOException {
        JFXDialogLayout content =  new JFXDialogLayout();
        
        if(iscreate){
            content.setHeading(new Text("Crear Producto"));
        }else{
            content.setHeading(new Text("Editar Producto"));
            /*
            UsuarioHelper helper = new UsuarioHelper();
            Usuario u = helper.getUser(PersonalController.selectedStore.correo.getValue());
            if(u == null){
                ErrorController error = new ErrorController();
                error.loadDialog("Error", "No se pudo obtener el usuario", "Ok", hiddenSp);
                System.out.println("Error al obtener el usuario");
                return;
            }
            */
        }
        Node node;
        node = (Node) FXMLLoader.load(ProductosIndexController.this.getClass().getResource(ProductosManagementController.viewPath));
        content.setBody(node);
        //content.setPrefSize(400,400);
                
        productDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        productDialog.show();
    }
    
    private void setCurrentProduct(ProductoLista selectedProduct) {
        Long currentId = Long.parseLong(selectedProduct.id.getValue());
        ProductoHelper helper = new ProductoHelper();
        this.selectedProduct = helper.getProducto(currentId);
        helper.close();
    }
    
    class ProductoLista extends RecursiveTreeObject<ProductoLista>{
        StringProperty id;
        StringProperty nombre;
        StringProperty descripcion;
        StringProperty estado;
        StringProperty precio;
        
        
        public ProductoLista(String id, String nombre, String descripcion, String estado, String precio){
            this.id = new SimpleStringProperty(id);
            this.nombre = new SimpleStringProperty(nombre);
            this.descripcion = new SimpleStringProperty(descripcion);
            this.estado = new SimpleStringProperty(estado);
            this.precio = new SimpleStringProperty(precio);
        }

    }
    
}
