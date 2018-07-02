/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.business.ComboPromocion;
import com.sigad.sigad.business.DetallePedido;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoCategoriaDescuento;
import com.sigad.sigad.business.ProductoDescuento;
import com.sigad.sigad.business.ProductoInsumo;
import com.sigad.sigad.business.ProductosCombos;
import com.sigad.sigad.business.helpers.GeneralHelper;
import com.sigad.sigad.business.helpers.LoteInsumoHelper;
import com.sigad.sigad.business.helpers.MovimientoHelper;
import com.sigad.sigad.business.helpers.PedidoHelper;
import com.sigad.sigad.business.helpers.ProductoHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author chrs
 */
public class DevolucionPedidoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/pedido/view/DevolucionPedido.fxml";

    Pedido pedido;
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTreeTableView<PedidoLista> tblpedido;

    @FXML
    private JFXTextField txtCliente;

    @FXML
    private JFXTextField txtEstado;

    @FXML
    private JFXTextField txtDireccion;

    @FXML
    private JFXButton btnDevolucion;

    @FXML
    private JFXTextArea txtMotivo;

    @FXML
    JFXTreeTableColumn<PedidoLista, String> nombrePedido = new JFXTreeTableColumn<>("Nombre");
    @FXML
    JFXTreeTableColumn<PedidoLista, String> precioPedido = new JFXTreeTableColumn<>("Precio");
    @FXML
    JFXTreeTableColumn<PedidoLista, Double> subTotalPedido = new JFXTreeTableColumn<>("SubTotal");
    @FXML
    JFXTreeTableColumn<PedidoLista, Integer> cantidadPedido = new JFXTreeTableColumn<>("Cantidad");
    @FXML
    JFXTreeTableColumn<PedidoLista, Double> descuentoPedido = new JFXTreeTableColumn<>("Descuento(%)");
    @FXML
    JFXTreeTableColumn<PedidoLista, Integer> entregados = new JFXTreeTableColumn<>("Entregados");
    private final ObservableList<PedidoLista> pedidos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // TODO
        columnasPedidos();
        agregarColumnasTablasPedidos();

    }

    public void initModel(Pedido pedido, StackPane hiddenSp) {
        PedidoHelper helper = new PedidoHelper();
        this.pedido = helper.getPedidoEager(pedido.getId());
        ArrayList<DetallePedido> d = new ArrayList<>(this.pedido.getDetallePedido());
        d.forEach((t) -> {
            pedidos.add(new PedidoLista(t));
        });
        txtCliente.setText(pedido.getCliente().toString());
        txtDireccion.setText(pedido.getDireccionDeEnvio());
        txtEstado.setText(pedido.getEstado().getNombre());
    }

    public void columnasPedidos() {

        nombrePedido.setPrefWidth(80);
        nombrePedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, String> param) -> param.getValue().getValue().nombre);

        cantidadPedido.setPrefWidth(80);
        cantidadPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, Integer> param) -> param.getValue().getValue().cantidad.asObject());

        precioPedido.setPrefWidth(80);
        precioPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, String> param) -> param.getValue().getValue().precio);

        subTotalPedido.setPrefWidth(80);
        subTotalPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, Double> param) -> param.getValue().getValue().subtotal.asObject());

        descuentoPedido.setPrefWidth(80);
        descuentoPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, Double> param) -> param.getValue().getValue().descuento.asObject());

        entregados.setPrefWidth(80);
        entregados.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, Integer> param) -> param.getValue().getValue().entregados.asObject());

    }

    public void agregarColumnasTablasPedidos() {
        final TreeItem<PedidoLista> rootPedido = new RecursiveTreeItem<>(pedidos, RecursiveTreeObject::getChildren);
        tblpedido.setEditable(true);
        tblpedido.getColumns().setAll(nombrePedido, precioPedido, cantidadPedido, descuentoPedido, subTotalPedido);
        tblpedido.setRoot(rootPedido);
        tblpedido.setShowRoot(false);
    }

    @FXML
    void devolucion(MouseEvent event) {
        HashMap<Insumo, Integer> insumos = new HashMap<>();
        pedidos.forEach((t) -> {
            if (t.combo != null) {
                DetallePedido detalle = new DetallePedido(true, t.cantidad.getValue(), Double.valueOf(t.precio.getValue()), 0, t.combo, pedido);
                ComboPromocion p = t.combo;
                for (ProductosCombos productosCombos : p.getProductosxComboArray()) {
                    calcularInsumos(productosCombos.getProducto(), productosCombos.getCantidad() * t.cantidad.getValue(), insumos);
                }
            } else if (t.producto != null) {
                DetallePedido detalle = new DetallePedido(true, t.cantidad.getValue(), Double.valueOf(t.precio.getValue()), 0, t.producto, pedido, t.descuentoProducto, t.descuentoCategoria);
                calcularInsumos(detalle.getProducto(), t.cantidad.getValue(), insumos);
            } else {
                System.out.println("Error, no es ni combo ni producto");
            }

        });

        MovimientoHelper movHelper = new MovimientoHelper();
        ArrayList<MovimientosTienda> movimientos = movHelper.getLogicMovements(pedido);
        movHelper.close();
        LoteInsumoHelper lihelper = new LoteInsumoHelper();
        lihelper.devolverInsumos(insumos, pedido, movimientos);
    }

    void calcularInsumos(Producto p, Integer cantidad, HashMap<Insumo, Integer> insumosConsumidos) {
        ProductoHelper helper = new ProductoHelper();
        p = helper.getProducto(p.getId());
        ArrayList<ProductoInsumo> pxi = new ArrayList(p.getProductoxInsumos());
        for (ProductoInsumo productoInsumo : pxi) {
            if (insumosConsumidos.get(productoInsumo.getInsumo()) != null) {
                insumosConsumidos.put(productoInsumo.getInsumo(), productoInsumo.getCantidad().intValue() * cantidad + insumosConsumidos.get(productoInsumo.getInsumo()));
            } else {
                insumosConsumidos.put(productoInsumo.getInsumo(), productoInsumo.getCantidad().intValue() * cantidad);
            }
        }
    }

    class PedidoLista extends RecursiveTreeObject<PedidoLista> {

        StringProperty nombre;
        StringProperty precio;
        IntegerProperty cantidad;
        DoubleProperty subtotal;
        DoubleProperty descuento;
        IntegerProperty entregados;
        Producto producto;
        ComboPromocion combo;
        ProductoDescuento descuentoProducto;
        ProductoCategoriaDescuento descuentoCategoria;

        public PedidoLista(DetallePedido detalle) {
            if (detalle.getProducto() != null) {
                this.nombre = new SimpleStringProperty(detalle.getProducto().getNombre());
                this.precio = new SimpleStringProperty(detalle.getPrecioUnitario().toString());
                this.cantidad = new SimpleIntegerProperty(detalle.getCantidad());
                this.entregados = new SimpleIntegerProperty(detalle.getNumEntregados());
                if (detalle.getDescuentoCategoria() != null) {
                    this.descuento = new SimpleDoubleProperty(detalle.getDescuentoCategoria().getValue() * 100);
                    Double s = detalle.getCantidad() * detalle.getPrecioUnitario() * (1 - detalle.getDescuentoCategoria().getValue());
                    this.subtotal = new SimpleDoubleProperty(GeneralHelper.roundTwoDecimals(s));
                    this.descuentoCategoria = detalle.getDescuentoCategoria();
                    this.descuentoProducto = null;
                } else if (detalle.getDescuentoProducto() != null) {
                    this.descuento = new SimpleDoubleProperty(detalle.getDescuentoProducto().getValorPct() * 100);
                    Double s = detalle.getCantidad() * detalle.getPrecioUnitario() * (1 - detalle.getDescuentoProducto().getValorPct());
                    this.subtotal = new SimpleDoubleProperty(GeneralHelper.roundTwoDecimals(s));
                    this.descuentoProducto = detalle.getDescuentoProducto();
                    this.descuentoCategoria = null;
                } else {
                    Double s = detalle.getCantidad() * detalle.getPrecioUnitario();
                    this.subtotal = new SimpleDoubleProperty(GeneralHelper.roundTwoDecimals(s));
                    this.descuento = new SimpleDoubleProperty(0.0);
                    this.descuentoCategoria = null;
                    this.descuentoProducto = null;
                }

                this.producto = detalle.getProducto();
                this.combo = null;

            } else if (detalle.getCombo() != null) {
                this.nombre = new SimpleStringProperty(detalle.getCombo().getNombre());
                this.precio = new SimpleStringProperty(detalle.getPrecioUnitario().toString());
                this.cantidad = new SimpleIntegerProperty(detalle.getCantidad());
                this.entregados = new SimpleIntegerProperty(detalle.getNumEntregados());
                this.subtotal = new SimpleDoubleProperty(detalle.getPrecioUnitario() * detalle.getCantidad());
                this.descuento = new SimpleDoubleProperty(0.0);
                this.descuentoCategoria = null;
                this.descuentoProducto = null;

                this.producto = null;
                this.combo = detalle.getCombo();

            }

        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PedidoLista) {
                PedidoLista pl = (PedidoLista) o;
                return nombre.getValue().equals(pl.nombre.getValue());
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + Objects.hashCode(this.nombre);

            return hash;
        }

    }

}
