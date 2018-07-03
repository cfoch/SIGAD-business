/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.business.ComboPromocion;
import com.sigad.sigad.business.Constantes;
import com.sigad.sigad.business.DetallePedido;
import com.sigad.sigad.business.NotaCredito;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoCategoriaDescuento;
import com.sigad.sigad.business.ProductoDescuento;
import com.sigad.sigad.business.helpers.GeneralHelper;
import com.sigad.sigad.business.helpers.NotaCreditoHelper;
import com.sigad.sigad.business.helpers.PdfHelper;
import com.sigad.sigad.business.helpers.PedidoHelper;
import static com.sigad.sigad.pedido.controller.SeleccionarProductosController.viewPath;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */
public class EditarEliminarPedidoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/pedido/view/EditarEliminarPedido.fxml";
    Pedido pedido;
    NotaCredito nota;
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
    private JFXButton btnGenerarDocumento;

    @FXML
    private JFXTextArea txtmensaje;

    @FXML
    private JFXTextField txtruc;

    @FXML
    private JFXTextField txtTotal;

    @FXML
    private JFXTextField txtTurno;

    @FXML
    private JFXTextField txtigv;

    @FXML
    private JFXTextField txtDescuento;

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
        columnasPedidos();
        agregarColumnasTablasPedidos();
    }

    public void initModel(Boolean isEdit, Pedido pedido, StackPane hiddenSp) {
        PedidoHelper helper = new PedidoHelper();
        this.pedido = helper.getPedidoEager(pedido.getId());
        ArrayList<DetallePedido> d = new ArrayList<>(this.pedido.getDetallePedido());
        d.forEach((t) -> {
            pedidos.add(new PedidoLista(t));
        });
        helper.close();
        txtCliente.setText(this.pedido.getCliente().toString());
        txtDireccion.setText(this.pedido.getDireccionDeEnvio());
        txtEstado.setText(this.pedido.getEstado().getNombre());
        txtmensaje.setText(this.pedido.getMensajeDescripicion());
        Double igv = this.pedido.getTotal() / (HomeController.IGV + 1) * HomeController.IGV;
        Double pctdesc = (this.pedido.getDescuentoCliente() != null) ? this.pedido.getDescuentoCliente().getValue() : 0.0;
        Double descuentoCliente = (this.pedido.getTotal() - igv) / (1 + pctdesc) * pctdesc;
        txtigv.setText(GeneralHelper.roundTwoDecimals(igv).toString());
        txtDescuento.setText(GeneralHelper.roundTwoDecimals(descuentoCliente).toString());
        txtTotal.setText(this.pedido.getTotal().toString());
        txtTurno.setText(this.pedido.getTurno());
        txtruc.setText((this.pedido.getRucFactura() == null) ? "-" : this.pedido.getRucFactura());
        setup();
    }

    public void setup() {
        if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_PENDIENTE)) {
            btnGenerarDocumento.setDisable(true);
        }
        if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_DEVOLUCION)) {
            txtmensaje.setPromptText("Motivo de la devolucion");
        } else if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_CANCELADO)) {
            btnGenerarDocumento.setDisable(true);
        } else {
            btnGenerarDocumento.setText((pedido.getRucFactura() == null) ? "Generar Boleta" : "Generar Factura");
        }

    }

    public void cancelarPedido() {

    }

    public void columnasPedidos() {

        nombrePedido.setPrefWidth(450);
        nombrePedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, String> param) -> param.getValue().getValue().nombre);

        cantidadPedido.setPrefWidth(80);
        cantidadPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, Integer> param) -> param.getValue().getValue().cantidad.asObject());

//       
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

    public Window getCurrentStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        return source.getScene().getWindow();
    }

    @FXML
    void generarBoleta(ActionEvent event) throws DocumentException {
        if (event.getSource() == btnGenerarDocumento) {
            PdfHelper helper = new PdfHelper();

            Window currentStage = getCurrentStage(event);
            DirectoryChooser dirChooser = new DirectoryChooser();
            String downloadDir = dirChooser.showDialog(currentStage).getAbsolutePath();
            if (downloadDir != null) {
                System.out.println(downloadDir);
            }
            if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_DEVOLUCION)) {
                System.out.println("Entro a esttado devolucion");
                NotaCreditoHelper helpernota = new NotaCreditoHelper();
                nota = helpernota.getNota(pedido.getId());
                helpernota.close();
                helper.crearNotaDeCredito(downloadDir, pedido, nota);
                ErrorController err = new ErrorController();
                err.loadDialog("Aviso", "Documento generado satisfactoriamente", "Ok", stackPane);
            } else if (pedido.getRucFactura() == null) {
                helper.crearBoletaVenta(downloadDir, pedido);
                ErrorController err = new ErrorController();
                err.loadDialog("Aviso", "Documento generado satisfactoriamente", "Ok", stackPane);
            } else {
                helper.crearFacturaVenta(downloadDir, pedido);
                ErrorController err = new ErrorController();
                err.loadDialog("Aviso", "Documento generado satisfactoriamente", "Ok", stackPane);
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
