/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.helpers.ProductoHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */
public class DescripcionProductosController implements Initializable {

    public static final String viewPath = "/com/sigad/sigad/pedido/view/descripcionProductos.fxml";
    /**
     * Initializes the controller class.
     */
    private Integer idProducto;

    @FXML
    private ImageView imageProducto;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtPrecioBase;

    @FXML
    private JFXTextField txtCategoria;

    @FXML
    private JFXTextField txtFragilidad;

    @FXML
    private JFXTextArea txtDescripcion;

    

    @FXML
    private JFXButton btnBack;
    
    @FXML
    private JFXTreeTableView<StockLista> tablaStock;
    private ObservableList<StockLista> stocks  = FXCollections.observableArrayList(); ;
    JFXTreeTableColumn<StockLista, String> local = new JFXTreeTableColumn<>("Local");
    JFXTreeTableColumn<StockLista, String> stock = new JFXTreeTableColumn<>("Stock");
    
    @FXML
    private JFXTreeTableView<PromocionesLista> tablaPromociones;
    private ObservableList<PromocionesLista> promociones  = FXCollections.observableArrayList();;
    JFXTreeTableColumn<PromocionesLista, String> promocion = new JFXTreeTableColumn<>("Promocion");
    JFXTreeTableColumn<PromocionesLista, String> tipo = new JFXTreeTableColumn<>("Tipo");
    JFXTreeTableColumn<PromocionesLista, String> descuento = new JFXTreeTableColumn<>("Descuento");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        columnasPromociones();
        columnasStockLista();
        agregarColumnasTablaStock();
        agregarColumnasTablasPromociones();
        System.out.println(idProducto);
    }

    /**
     * @param idProducto the idProducto to set
     */
    public void initModel(Integer idProducto) {
        this.idProducto = idProducto;
        ProductoHelper helper = new ProductoHelper();
        Producto producto = helper.getProductById(idProducto);
        helper.close();
        if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtPrecioBase.setText(producto.getPrecio().toString());
            //txtCategoria.setText(producto.getCategoria().getNombre());
            txtDescripcion.setText(producto.getDescripcion());
            Image image = new Image(producto.getImagen());
            imageProducto.setImage(image);
        }

        System.out.println(idProducto);
    }
    
    
    public void columnasStockLista() {
        local.setPrefWidth(120);
        local.setCellValueFactory((TreeTableColumn.CellDataFeatures<StockLista, String> param) -> param.getValue().getValue().local);
        stock.setPrefWidth(120);
        stock.setCellValueFactory((TreeTableColumn.CellDataFeatures<StockLista, String> param) -> param.getValue().getValue().stock);

    }

    public void columnasPromociones() {
        promocion.setPrefWidth(120);
        promocion.setCellValueFactory((TreeTableColumn.CellDataFeatures<PromocionesLista, String> param) -> param.getValue().getValue().promocion);
        tipo.setPrefWidth(120);
        tipo.setCellValueFactory((TreeTableColumn.CellDataFeatures<PromocionesLista, String> param) -> param.getValue().getValue().tipo);
        descuento.setPrefWidth(120);
        descuento.setCellValueFactory((TreeTableColumn.CellDataFeatures<PromocionesLista, String> param) -> param.getValue().getValue().descuento);

    }
    public void agregarColumnasTablaStock() {
        final TreeItem<StockLista> rootPedido = new RecursiveTreeItem<>(stocks, RecursiveTreeObject::getChildren);
        tablaStock.setEditable(true);
        tablaStock.getColumns().setAll(local, stock);
        tablaStock.setRoot(rootPedido);
        tablaStock.setShowRoot(false);
    }
    public void agregarColumnasTablasPromociones() {
        final TreeItem<PromocionesLista> rootPedido = new RecursiveTreeItem<>(promociones, RecursiveTreeObject::getChildren);
        tablaPromociones.setEditable(true);
        tablaPromociones.getColumns().setAll(promocion, tipo, descuento);
        tablaPromociones.setRoot(rootPedido);
        tablaPromociones.setShowRoot(false);
    }

    class StockLista extends RecursiveTreeObject<StockLista> {

        StringProperty local;
        StringProperty stock;
        Integer codigo;

        public StockLista(String local, String stock, Integer codigo) {
            this.local = new SimpleStringProperty(local);
            this.codigo = codigo;
            this.stock = new SimpleStringProperty(stock);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof StockLista) {
                StockLista pl = (StockLista) o;
                return pl.codigo.equals(codigo);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.codigo);
            return hash;
        }

    }

    class PromocionesLista extends RecursiveTreeObject<PromocionesLista> {

        StringProperty promocion;
        StringProperty tipo;
        StringProperty descuento;
        StringProperty descripcion;
        Integer codigo;

        public PromocionesLista(String promocion, String tipo, String descuento, String descripcion, Integer codigo) {
            this.promocion = new SimpleStringProperty(promocion);
            this.codigo = codigo;
            this.tipo = new SimpleStringProperty(tipo);
            this.descuento = new SimpleStringProperty(descuento);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PromocionesLista) {
                PromocionesLista pl = (PromocionesLista) o;
                return pl.codigo.equals(codigo);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.codigo);
            return hash;
        }

    }
}
