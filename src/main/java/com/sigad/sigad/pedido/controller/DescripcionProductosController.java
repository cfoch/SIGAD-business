/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoDescuento;
import com.sigad.sigad.business.ProductoInsumo;
import com.sigad.sigad.business.helpers.ProductoDescuentoHelper;
import com.sigad.sigad.business.helpers.ProductoHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
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
import javafx.scene.input.MouseEvent;
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
    private JFXTreeTableView<PromocionesLista> tablaPromociones;
    private ObservableList<PromocionesLista> promociones = FXCollections.observableArrayList();
    JFXTreeTableColumn<PromocionesLista, String> promocion = new JFXTreeTableColumn<>("Promocion");
    JFXTreeTableColumn<PromocionesLista, String> tipo = new JFXTreeTableColumn<>("Tipo");
    JFXTreeTableColumn<PromocionesLista, String> descuento = new JFXTreeTableColumn<>("Descuento");
    
    @FXML
    private JFXTreeTableView<InsumosLista> tablaInsumos;
    private ObservableList<InsumosLista> insumos = FXCollections.observableArrayList();
    JFXTreeTableColumn<InsumosLista, String> nombre = new JFXTreeTableColumn<>("Nombre");
    JFXTreeTableColumn<InsumosLista, String> cantidad = new JFXTreeTableColumn<>("Cantidad");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        columnasPromociones();
        columnasInsumos();
        agregarColumnasTablasInsumos();
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
            txtCategoria.setText(producto.getCategoria().getNombre());
            txtDescripcion.setText(producto.getDescripcion());
            Image image = new Image(producto.getImagen());
            imageProducto.setImage(image);
        }
        llenarTablaDescuento();
        ArrayList<ProductoInsumo> pd = new ArrayList<>(producto.getProductoxInsumos());
        pd.forEach((t) -> {
            insumos.add(new InsumosLista(t.getInsumo(), t.getCantidad().intValue()));
        });
    }

    @FXML
    void clickCerrar(MouseEvent event) {
        SeleccionarProductosController.userDialog.close();
    }

    public void llenarTablaDescuento() {
        ProductoDescuentoHelper helper = new ProductoDescuentoHelper();

        List<ProductoDescuento> descuentos = helper.getDescuentosByProducto(idProducto);
        if (descuentos != null) {
            descuentos.forEach((t) -> {
                promociones.add(new PromocionesLista(t.getCodCupon(), "Descuento",
                        String.valueOf(t.getValorPct() * 100), "D", idProducto));
            });
        }
        helper.close();

    }

    

    public void columnasPromociones() {
        promocion.setPrefWidth(120);
        promocion.setCellValueFactory((TreeTableColumn.CellDataFeatures<PromocionesLista, String> param) -> param.getValue().getValue().promocion);
        tipo.setPrefWidth(120);
        tipo.setCellValueFactory((TreeTableColumn.CellDataFeatures<PromocionesLista, String> param) -> param.getValue().getValue().tipo);
        descuento.setPrefWidth(120);
        descuento.setCellValueFactory((TreeTableColumn.CellDataFeatures<PromocionesLista, String> param) -> param.getValue().getValue().descuento);

    }

     public void columnasInsumos() {
        nombre.setPrefWidth(120);
        nombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumosLista, String> param) -> param.getValue().getValue().nombre);
        cantidad.setPrefWidth(120);
        cantidad.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumosLista, String> param) -> param.getValue().getValue().cantidad);

    }

     public void agregarColumnasTablasInsumos() {
        final TreeItem<InsumosLista> rootPedido = new RecursiveTreeItem<>(insumos, RecursiveTreeObject::getChildren);
        tablaInsumos.setEditable(true);
        tablaInsumos.getColumns().setAll(nombre, cantidad);
        tablaInsumos.setRoot(rootPedido);
        tablaInsumos.setShowRoot(false);
    }

    public void agregarColumnasTablasPromociones() {
        final TreeItem<PromocionesLista> rootPedido = new RecursiveTreeItem<>(promociones, RecursiveTreeObject::getChildren);
        tablaPromociones.setEditable(true);
        tablaPromociones.getColumns().setAll(promocion, tipo, descuento);
        tablaPromociones.setRoot(rootPedido);
        tablaPromociones.setShowRoot(false);
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
    
    
    class InsumosLista extends RecursiveTreeObject<InsumosLista> {

        StringProperty nombre;
        StringProperty cantidad;
        Insumo insumo;

        public InsumosLista(Insumo insumo, Integer cantidad) {
            this.nombre = new SimpleStringProperty(insumo.getNombre());
            this.cantidad = new SimpleStringProperty(cantidad.toString());
            this.insumo = insumo;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof InsumosLista) {
                InsumosLista pl = (InsumosLista) o;
                return pl.insumo.equals(pl.insumo);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 23 * hash + Objects.hashCode(this.insumo);
            return hash;
        }


    }
}
