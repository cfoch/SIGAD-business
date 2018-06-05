/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.productos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoCategoria;
import com.sigad.sigad.business.ProductoFragilidad;
import com.sigad.sigad.business.ProductoInsumo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import com.sigad.sigad.business.helpers.ProductoHelper;
import com.sigad.sigad.business.helpers.InsumoHelper;
import com.sigad.sigad.business.helpers.InsumoHelper.InsumoParser;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;
import com.sigad.sigad.business.helpers.ProductoCategoriaHelper;
import com.sigad.sigad.business.helpers.ProductoFragilidadHelper;
import com.sigad.sigad.business.helpers.ProductoInsumoHelper;
import javafx.event.ActionEvent;

/**
 * FXML Controller class
 *
 * @author jorgito-stark
 */
public class ProductosManagementController implements Initializable {
    public static  String viewPath = "/com/sigad/sigad/productos/view/ProductosManagement.fxml";
    
    @FXML
    private JFXTreeTableView<InsumoLista> insumoTable;
    @FXML
    private JFXTextField finalPrice;    
    private ArrayList<InsumoLista> insumosSeleccionados;
    @FXML
    private JFXTextField txtNombre;
    @FXML
    private ImageView imageProducto;
    @FXML
    private JFXTextArea txtDescripcion;
    @FXML
    private JFXComboBox<String> selectCategoria;
    @FXML
    private JFXComboBox<String> selectFragilidad;
    @FXML
    private JFXTextField txtPeso;
    @FXML
    private JFXButton saveBtn;
    private static Producto producto = null;
    private static Boolean update = false;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeResources();
        if(ProductosIndexController.selectedProduct != null){
            this.update = true;
            loadProduct(ProductosIndexController.selectedProduct);
        }
        
        initializeMainTable();
        
        saveBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                saveProduct();
            }
        });
    }
    
    public void initializeMainTable(){
        JFXTreeTableColumn<InsumoLista,String> insumoId = new JFXTreeTableColumn<>("ID");
        insumoId.setPrefWidth(100);
        insumoId.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InsumoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InsumoLista,String> param){
                return param.getValue().getValue().id;
            }
        });
        
        JFXTreeTableColumn<InsumoLista,String> insumoNombre = new JFXTreeTableColumn<>("Nombre");
        insumoNombre.setPrefWidth(400);
        insumoNombre.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InsumoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InsumoLista,String> param){
                return param.getValue().getValue().nombre;
            }
        });
        
        JFXTreeTableColumn<InsumoLista,String> insumoPrecio = new JFXTreeTableColumn<>("Precio");
        insumoPrecio.setPrefWidth(100);
        insumoPrecio.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InsumoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InsumoLista,String> param){
                return param.getValue().getValue().precio;
            }
        });        
        
        JFXTreeTableColumn<InsumoLista,String> insumoCantidad = new JFXTreeTableColumn<>("Cantidad");
        insumoCantidad.setPrefWidth(100);
        insumoCantidad.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<InsumoLista, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<InsumoLista,String> param){
                return param.getValue().getValue().cantidad;
            }
        }); 
        
        ObservableList<InsumoLista> insumosList = FXCollections.observableArrayList();
        InsumoHelper insumoHelper = new InsumoHelper();
        
        ArrayList<Insumo> insumos = insumoHelper.getInsumos();
        
        if(insumos != null){
            if(this.producto == null){
                insumos.forEach((p) ->{
                    Insumo t = p;
                    insumosList.add(new ProductosManagementController.InsumoLista(
                        String.valueOf(t.getId()),
                        t.getNombre(),
                        String.valueOf(t.getPrecio())
                    ));
                });   
            }
            else{
                ArrayList<ProductoInsumo> savedRegisters = productSupplies();
                insumos.forEach((p) ->{
                    Insumo t = p;
                    Double ammount = null;
                    for (ProductoInsumo savedRegister : savedRegisters) {
                        if(savedRegister.getInsumo().getId() == t.getId())
                            ammount = savedRegister.getCantidad();
                    }
                    if(ammount != null){
                        insumosList.add(new ProductosManagementController.InsumoLista(
                            String.valueOf(t.getId()),
                            t.getNombre(),
                            String.valueOf(t.getPrecio()),
                            String.valueOf(ammount.intValue())
                        ));                           
                    }
                    else{
                        insumosList.add(new ProductosManagementController.InsumoLista(
                            String.valueOf(t.getId()),
                            t.getNombre(),
                            String.valueOf(t.getPrecio())
                        ));   
                    }
                });   
            }
        }
        
        insumoHelper.close();
        
        final TreeItem<InsumoLista> root = new RecursiveTreeItem<InsumoLista>(insumosList, RecursiveTreeObject::getChildren);
        
        insumoCantidad.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

        
        insumoTable.getColumns().setAll(insumoId,insumoNombre,insumoPrecio,insumoCantidad);
        insumoTable.setEditable(true);
        insumoTable.setRoot(root);
        insumoTable.setShowRoot(false); 
        
        insumoCantidad.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<InsumoLista, String>>(){
            @Override
            public void handle(TreeTableColumn.CellEditEvent<InsumoLista, String> event){
                TreeItem<InsumoLista> currentRow = insumoTable.getTreeItem(event.getTreeTablePosition().getRow());
                if(StringUtils.isNumeric(event.getNewValue())){
                    currentRow.getValue().setCantidad(event.getNewValue());   
                }
                else{
                    currentRow.getValue().setCantidad("0");   
                }
                calculatePrice();
            }
        });     
    }
    
    private void initializeResources(){
        ProductoCategoriaHelper productoCategoriaHelper = new ProductoCategoriaHelper();
        ArrayList<ProductoCategoria> categories = productoCategoriaHelper.getActiveProductCategories();
        if(categories != null){
            for (ProductoCategoria category : categories) {
                if(category != null)
                    selectCategoria.getItems().add(category.getNombre());
            }
        }
        productoCategoriaHelper.close();
        
        ProductoFragilidadHelper productoFagilidadHelper = new ProductoFragilidadHelper();
        ArrayList<ProductoFragilidad> fragilities = productoFagilidadHelper.getProductFragilities();
        if(categories != null){
            for (ProductoFragilidad fragility : fragilities) {
                if(fragility != null)
                    selectFragilidad.getItems().add(fragility.getDescripcion());
            }
        }
        productoFagilidadHelper.close();        
    }
    
    private void loadProduct(Producto product){
        this.producto = product;
        
        this.txtNombre.setText(this.producto.getNombre());
        this.selectCategoria.getSelectionModel().select(this.producto.getCategoria().getNombre());
        this.selectFragilidad.getSelectionModel().select(this.producto.getFragilidad().getDescripcion());
        this.txtDescripcion.setText(this.producto.getDescripcion());
        this.txtPeso.setText(String.valueOf(this.producto.getPeso()));
        this.finalPrice.setText(String.valueOf(this.producto.getPrecio()));
        //productSupplies();
    }
    
    private ArrayList<ProductoInsumo> productSupplies(){
        ProductoInsumoHelper helper = new ProductoInsumoHelper();
        ArrayList<ProductoInsumo> insumos = helper.getProductoInsumos(this.producto.getId());
        helper.close();
        return insumos;
        /*
        Integer size = insumoTable.getCurrentItemsCount();
        for (int i = 0; i < size; i++) {
            InsumoLista currentSupply = (InsumoLista) insumoTable.getSelectionModel().getModelItem(i).getValue();
            
        }
        */
    }
    
    private void fillList(JFXComboBox<String> list, ObservableList<String> options){
        list.getItems().addAll(options); 
    }
    
    private void calculatePrice(){
        double total = 0.0;
        for (int i = 0; i < insumoTable.getExpandedItemCount(); i++) {
            TreeItem<InsumoLista> currentRow = insumoTable.getTreeItem(i);
            String price = currentRow.getValue().getPrecio().getValue();
            String ammount = currentRow.getValue().getCantidad().getValue();
            total += (Double.parseDouble(price) * Integer.parseInt(ammount));
        }
        finalPrice.setText(String.valueOf(total));
    }
    
    private ArrayList<InsumoLista> getSelectedItems(){
        ArrayList<InsumoLista> options = new ArrayList<InsumoLista>();
        for (int i = 0; i < insumoTable.getExpandedItemCount(); i++) {
            TreeItem<InsumoLista> currentRow = insumoTable.getTreeItem(i);
            
            String ammountString = currentRow.getValue().getCantidad().getValue();
            Integer ammount = Integer.parseInt(ammountString);
            if(ammount > 0){
                String id = currentRow.getValue().getId().getValue();
                InsumoLista actualInsumo = new InsumoLista(id,ammountString);
                options.add(actualInsumo);
            }
            /*
            String ammountString = currentRow.getValue().getCantidad().getValue();
            Integer ammount = Integer.parseInt(ammountString);
            if(ammount > 0){
                InsumoHelper helper = new InsumoHelper();
                InsumoHelper.InsumoParser insumoActual = helper.new InsumoParser(name,ammount);
                insumosSeleccionados.add(insumoActual);
            }
            */
        }
        return options;
    }
    
    private void saveProduct(){
        this.insumosSeleccionados = getSelectedItems();
        // WIP Validations
        Producto nuevoProducto = new Producto(); 
        if(this.producto != null){
         nuevoProducto = this.producto;
        }
        
        nuevoProducto.setNombre(txtNombre.getText());
        nuevoProducto.setPeso(Double.parseDouble(txtPeso.getText()));
        nuevoProducto.setDescripcion(txtDescripcion.getText());
        nuevoProducto.setPrecio(Double.parseDouble(finalPrice.getText()));
        
        ProductoHelper productoHelper = new ProductoHelper();
        ProductoCategoriaHelper categoriaHelper = new ProductoCategoriaHelper();
        ProductoFragilidadHelper fragilidadHelper = new ProductoFragilidadHelper();
        InsumoHelper insumoHelper = new InsumoHelper();
        
        ProductoCategoria categoria = categoriaHelper.getProductCategoryByName(selectCategoria.getValue());
        ProductoFragilidad fragilidad = fragilidadHelper.getProductFragilityByDescription(selectFragilidad.getValue());
        
        nuevoProducto.setActivo(true);
        nuevoProducto.setCategoria(categoria);
        nuevoProducto.setFragilidad(fragilidad);
        
        //ProductoInsumoHelper prodcutoInsumoHelper = new ProductoInsumoHelper();
        
        if(this.update){
            Boolean updated = productoHelper.updateProduct(nuevoProducto);
            if(updated){
                ProductoInsumoHelper prodcutoInsumoHelper = new ProductoInsumoHelper();
                ArrayList<ProductoInsumo> insumos = prodcutoInsumoHelper.getProductoInsumos(nuevoProducto.getId());
                
                for (InsumoLista insumosSeleccionado : this.insumosSeleccionados) {
                    Boolean existing = false;
                    ProductoInsumo prodIns = new ProductoInsumo();
                    ProductoInsumoHelper transactionHelper = new ProductoInsumoHelper();
                    for (ProductoInsumo insumoProducto : insumos) {
                        Long selectedId = Long.valueOf(insumosSeleccionado.getId().getValue());
                        Long itemId = insumoProducto.getInsumo().getId();
                        if(selectedId == itemId){
                            existing = true;
                            prodIns = insumoProducto;
                            prodIns.setCantidad(Double.parseDouble(insumosSeleccionado.getCantidad().getValue()));
                            break;
                        }
                    }
                    if(existing){
                        transactionHelper.updateProductoInsumo(prodIns);
                    }
                    else{
                        ProductoInsumo newRecord = new ProductoInsumo();
                        newRecord.setProducto(nuevoProducto);
                        newRecord.setInsumo(insumoHelper.getInsumo(Long.parseLong(insumosSeleccionado.getId().getValue())));
                        newRecord.setCantidad(Double.parseDouble(insumosSeleccionado.getCantidad().getValue()));                            
                        transactionHelper.saveProductoInsumo(newRecord);                        
                    }
                    //prodcutoInsumoHelper.close();
                }
            }
        }
        else{
            Long id = productoHelper.saveProduct(nuevoProducto);

            if(id != null){
                for (InsumoLista insumosSeleccionado : insumosSeleccionados) {
                    ProductoInsumoHelper prodcutoInsumoHelper = new ProductoInsumoHelper();
                    ProductoInsumo newRecord = new ProductoInsumo();
                    newRecord.setProducto(nuevoProducto);
                    newRecord.setInsumo(insumoHelper.getInsumo(Long.parseLong(insumosSeleccionado.getId().getValue())));
                    newRecord.setCantidad(Double.parseDouble(insumosSeleccionado.getCantidad().getValue()));
                    prodcutoInsumoHelper.saveProductoInsumo(newRecord);
//                    prodcutoInsumoHelper.close();
                }
            }            
        }
        
        //prodcutoInsumoHelper.close();
        //productoHelper.close();
        //categoriaHelper.close();
        //fragilidadHelper.close();
        //insumoHelper.close();
    }
    
    class InsumoLista extends RecursiveTreeObject<InsumoLista>{
        StringProperty id;
        StringProperty nombre;
        StringProperty precio;
        StringProperty cantidad;
        
        public InsumoLista(String id, String nombre, String precio){
            this.id = new SimpleStringProperty(id);
            this.nombre = new SimpleStringProperty(nombre);
            this.precio = new SimpleStringProperty(precio);
            this.cantidad = new SimpleStringProperty("0");
        }
        
        public InsumoLista(String id, String nombre, String precio, String cantidad){
            this.id = new SimpleStringProperty(id);
            this.nombre = new SimpleStringProperty(nombre);
            this.precio = new SimpleStringProperty(precio);
            this.cantidad = new SimpleStringProperty(cantidad);
        }
        
        public InsumoLista(String id, String cantidad){
            this.id = new SimpleStringProperty(id);
            this.cantidad = new SimpleStringProperty(cantidad);
        }
        
        public StringProperty getId(){
            return id;
        }    
        public StringProperty getNombre(){
            return nombre;
        }
        public StringProperty getPrecio(){
            return precio;
        }
        public StringProperty getCantidad(){
            return cantidad;
        }
        public void setCantidad(String cantidad){
            this.cantidad = new SimpleStringProperty(cantidad);
        }
    }
    
}
