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
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.Tienda;
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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author jorgito-stark
 */
public class ProductosIndexController implements Initializable {
    public static  String viewPath = "/com/sigad/sigad/productos/view/productos.fxml";
    
    @FXML
    private JFXTreeTableView<ProductoLista> productosTabla;
    static ObservableList<ProductoLista> data;
    @FXML
    private JFXButton addBtn;
    private JFXButton editBtn;
    @FXML
    private StackPane hiddenSp;
    /*Extras*/
    public static JFXDialog productDialog;
    
    @FXML
    private JFXButton moreBtn;
    private JFXPopup popup;
    
    public static boolean isProductCreate;
    public static ProductoLista selectedProductList = null;
    public static Producto selectedProduct = null;
    @FXML
    private JFXTextField filtro;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        initializeMainTable();
        agregarFiltro();
//        initEvents();
    }
    
    public void agregarFiltro() {
        filtro.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            productosTabla.setPredicate((TreeItem<ProductoLista> t) -> {
                Boolean flag = t.getValue().nombre.getValue().contains(newValue) || t.getValue().precio.getValue().contains(newValue) || t.getValue().estado.getValue().contains(newValue) || t.getValue().descripcion.getValue().contains(newValue);
                return flag;
            });
        });
    }
    
    private static void updateTable(Producto p) {
        data.add(new ProductoLista(
                    String.valueOf(p.getId()),
                    p.getNombre(),
                    p.getDescripcion(),
                    p.isActivo()? "Activo":"Inactivo",
                    String.valueOf(p.getPrecio())
                ));
    }
    
    public static void refreshMainTable(){
        data.clear();
        ProductoHelper productoHelper = new ProductoHelper();
        
        ArrayList<Producto> productos = productoHelper.getProducts();
        
        if(productos != null){
            productos.forEach((p) ->{
                Producto t = p;
                updateTable(p);
            });
        }
        
        productoHelper.close();
    }
    
    private void getDataFromDB() {
        ProductoHelper productoHelper = new ProductoHelper();
        
        ArrayList<Producto> productos = productoHelper.getProducts();
        
        if(productos != null){
            productos.forEach((p) ->{
                Producto t = p;
                updateTable(p);
            });
        }
        
        productoHelper.close();
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
        productoNombre.setPrefWidth(200);
        productoNombre.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductoLista,String> param){
                return param.getValue().getValue().nombre;
            }
        });
        
        JFXTreeTableColumn<ProductoLista,String> productoDescripcion = new JFXTreeTableColumn<>("Descripcion");
        productoDescripcion.setPrefWidth(400);
        productoDescripcion.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ProductoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ProductoLista,String> param){
                return param.getValue().getValue().descripcion;
            }
        });        
        
        JFXTreeTableColumn<ProductoLista,String> productoActivo = new JFXTreeTableColumn<>("Estado");
        productoActivo.setPrefWidth(100);
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
        
        //DB
        getDataFromDB();
        
        //Double click on row
        productosTabla.setRowFactory(ord -> {
            JFXTreeTableRow<ProductoLista> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked((event) -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                     && event.getClickCount() == 2) {
                    ProductoLista clickedRow = row.getItem();
                    System.out.println(clickedRow.nombre);
                    
                    selectedProductList = clickedRow;
                    
                    //data.remove(selectedUser);
                    try {
                        CreateEdditProductDialog(false);
                    } catch (IOException ex) {
                        Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "initUserTbl(): CreateEdditProductDialog()", ex);
                    }
                }
            });
            return row;
        });
        /*
        JFXButton popupButton = new JFXButton("Popup");
        StackPane main = new StackPane();
        main.getChildren().add(popupButton);
        
        JFXPopup popup = new JFXPopup(productosTabla);
        popupButton.setOnAction(e -> popup.show(popupButton, PopupVPosition.TOP, PopupHPosition.LEFT));
        */
        final TreeItem<ProductoLista> root = new RecursiveTreeItem<ProductoLista>(data, RecursiveTreeObject::getChildren);
        productosTabla.getColumns().setAll(productoId,productoNombre,productoDescripcion,productoActivo,productoPrecio);
        productosTabla.setRoot(root);
        productosTabla.setShowRoot(false);        
    }
    
//    private void initEvents(){
//        addBtn.setOnMouseClicked(new EventHandler<Event>() {
//
//            @Override
//            public void handle(Event event) {
//                try {
//                    System.out.println("Go Dialog");
//                    CreateEdditProductDialog(true);
//                } catch (IOException ex) {
//                    Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
//                }
//             }
//         });
//        
//        editBtn.setOnMouseClicked(new EventHandler<Event>(){
//            @Override
//            public void handle(Event event) {
//                try {
//                    int selected  = productosTabla.getSelectionModel().getSelectedIndex();
//                    ProductoLista selectedProduct = (ProductoLista) productosTabla.getSelectionModel().getModelItem(selected).getValue();
//                    setCurrentProduct(selectedProduct);
//                    CreateEdditProductDialog(false);
//                } catch (IOException ex) {
//                    Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
//                }
//             }            
//        });
//    }
    
    //usar para botones
    @FXML
    private void handleAction(ActionEvent event) {
        if(event.getSource() == addBtn){
            Tienda currentStore = LoginController.user.getTienda();
            if(currentStore == null){
                ErrorController error = new ErrorController();
                error.loadDialog("Error","No puede crear un producto si es superAdmin","OK", hiddenSp);
                return;
            }
            else{
                try {
                    CreateEdditProductDialog(true);
                } catch (IOException ex) {
                    Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
                }   
            }
        }else if(event.getSource() == moreBtn){
            Tienda currentStore = LoginController.user.getTienda();
            if(currentStore == null){
                ErrorController error = new ErrorController();
                error.loadDialog("Error","No puede editar un producto si es superAdmin","OK", hiddenSp);
                return;
            }
            else{
                int count = productosTabla.getSelectionModel().getSelectedCells().size();
                if( count > 1){
                    ErrorController error = new ErrorController();
                    error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla", "Ok", hiddenSp);
                }else if(count<=0){
                    ErrorController error = new ErrorController();
                    error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "Ok", hiddenSp);
                }else{
                    int selected  = productosTabla.getSelectionModel().getSelectedIndex();
                    selectedProductList = (ProductoLista) productosTabla.getSelectionModel().getModelItem(selected).getValue();

                    initPopup();
                    popup.show(moreBtn, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
                }   
            }
        }
    }
    
    private void initPopup(){
        JFXButton edit = new JFXButton("Editar");
        JFXButton delete = new JFXButton("Activar/Desactivar");
        
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                try {
                    CreateEdditProductDialog(false);
                } catch (IOException ex) {
                    Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "initPopup(): CreateEdditUserDialog()", ex);
                }
            }
        });
        
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                data.remove(PersonalController.selectedUser);
//                
//                UsuarioHelper helper = new UsuarioHelper();
//                Usuario temp = helper.getUser(selected.correo.getValue());
//                temp.setActivo(!temp.isActivo());
//                helper.close();
//                
//                UsuarioHelper helper1 = new UsuarioHelper();
//                boolean ok = helper1.updateUser(temp);
//                if(!ok){
//                    ErrorController error = new ErrorController();
//                    error.loadDialog("Eror", "No se pudo actualizar el estado", "Ok", hiddenSp);
//                    return;
//                }
//                updateTable(temp);
//                
//                helper1.close();
//                popup.hide();
                
            }
        });
        
        edit.setPadding(new Insets(20));
        edit.setPrefSize(145, 40);
        delete.setPadding(new Insets(20));
        delete.setPrefSize(145, 40);
        
        VBox vBox = new VBox(edit, delete);
        
        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }
    
    public void CreateEdditProductDialog(boolean iscreate) throws IOException {
        isProductCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        if(isProductCreate){
            content.setHeading(new Text("Crear Producto"));
            selectedProduct = null;
            selectedProductList = null;
        }else{
            content.setHeading(new Text("Editar Producto"));
            if(!setCurrentProduct(selectedProductList)){
                return;
            }
        }
        Node node;
        node = (Node) FXMLLoader.load(ProductosIndexController.this.getClass().getResource(ProductosManagementController.viewPath));
        content.setBody(node);
        //content.setPrefSize(400,400);
                
        productDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        productDialog.show();
    }
    
    private boolean setCurrentProduct(ProductoLista selectedProduct) {
        Long currentId = Long.parseLong(selectedProduct.id.getValue());
        ProductoHelper helper = new ProductoHelper();
        this.selectedProduct = helper.getProducto(currentId);
        
        if(this.selectedProduct == null){
            ErrorController error = new ErrorController();
            error.loadDialog("Error", "No se logró obtener el producto", "Ok", hiddenSp);
            System.out.println("Error al obtener el usuario");
            
            helper.close();
            return false;
        }
        
        return true;
    }
    
    public static class ProductoLista extends RecursiveTreeObject<ProductoLista>{
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
