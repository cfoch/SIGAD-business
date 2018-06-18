/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.*;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.ClienteDireccion;
import com.sigad.sigad.business.DetallePedido;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.LoteInsumo;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.PedidoEstado;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoInsumo;
import com.sigad.sigad.business.helpers.LoteInsumoHelper;
import com.sigad.sigad.business.helpers.PedidoHelper;
import com.sigad.sigad.deposito.helper.PedidoEstadoHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */
public class DatosPedidoController implements Initializable {

    Pedido pedido;
    StackPane stackPane;

    @FXML
    private JFXRadioButton btnBoleta;

    @FXML
    private ToggleGroup genero;

    @FXML
    private JFXRadioButton btnFactura;

    @FXML
    private JFXRadioButton btnEfectivo;

    @FXML
    private ToggleGroup pago;

    @FXML
    private JFXTextField txtMonto;

    @FXML
    private JFXRadioButton btnPOS;

    @FXML
    private JFXTextArea cmbDedicatoria;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXButton btnRegistrar;

    @FXML
    private JFXDatePicker dpFechaEntrega;

    @FXML
    private JFXComboBox<String> cmbInicio;
    private final ObservableList<String> horasInicio = FXCollections.observableArrayList();

    @FXML
    private JFXComboBox<ClienteDireccion> cmbDireccion;
    private final ObservableList<ClienteDireccion> direcciones = FXCollections.observableArrayList();

    @FXML
    private JFXComboBox<String> cmbTarjeta;
    private final ObservableList<String> tarjetas = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/pedido/view/datosPedido.fxml";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        dpFechaEntrega.setDayCellFactory(picker -> new DateCell(){
//           @Override
//            public void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                setDisable(empty || date.getDayOfWeek() == DayOfWeek.MONDAY);
//            }
//        });

    }

    public void llenarComboBox() {
        cmbDireccion.setItems(direcciones);
        pedido.getCliente().getClienteDirecciones().forEach((t) -> {
            direcciones.add(t);
        });
        cmbTarjeta.getItems().addAll("Visa", "Mastercard");
        cmbInicio.getItems().addAll("M", "T", "N");

    }

    public void initModel(Pedido pedido, StackPane stackPane) {
        this.pedido = pedido;
        this.stackPane = stackPane;
        txtMonto.setText(pedido.getTotal().toString());
        txtMonto.setDisable(true);
        llenarComboBox();

    }

    public void isValid() {

    }

    @FXML
    void registrarPedido(MouseEvent event) {
        try {
            pedido.setDireccionDeEnvio(cmbDireccion.getValue().getDireccionCliente());
            pedido.setCooXDireccion(cmbDireccion.getValue().getCooXDireccion());
            pedido.setCooYDireccion(cmbDireccion.getValue().getCooXDireccion());
            pedido.setMensajeDescripicion(cmbDedicatoria.getText());
            Date date = Date.from(dpFechaEntrega.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            Timestamp timeStamp = new Timestamp(date.getTime());
            pedido.setFechaVenta(timeStamp);
            pedido.setVendedor(LoginController.user);
            PedidoEstadoHelper hp = new PedidoEstadoHelper();
            pedido.setTurno(cmbInicio.getValue());
            PedidoEstado estado = hp.getEstadoByName("venta");
            pedido.addEstado(estado);
            pedido.setEstado(estado);
            hp.close();
            HashMap<Insumo, Integer> insumos = new HashMap<>();
            for (DetallePedido dp : pedido.getDetallePedido()) {
                Producto p = dp.getProducto();
                ArrayList<ProductoInsumo> pxi = new ArrayList(p.getProductoxInsumos());
                for (ProductoInsumo productoInsumo : pxi) {
                    if (insumos.get(productoInsumo.getInsumo()) != null) {
                        insumos.put(productoInsumo.getInsumo(), productoInsumo.getCantidad().intValue() * dp.getCantidad() + insumos.get(productoInsumo.getInsumo()));
                    } else {
                        insumos.put(productoInsumo.getInsumo(), productoInsumo.getCantidad().intValue() * dp.getCantidad());
                    }
                }
            }

            LoteInsumoHelper lihelper = new LoteInsumoHelper();
            Boolean ok = lihelper.descontarInsumos(insumos, pedido.getTienda());
            if (ok) {
                PedidoHelper helper = new PedidoHelper();
                helper.savePedido(pedido);
                helper.close();
            }else{
                ErrorController err = new ErrorController() ;
                err.loadDialog("Alerta", "No hay insumos", "ok", stackPane);
            }
            gotoInicio();

        } catch (Exception ex) {
        }
    }

    void gotoInicio() {
        try {
            Node node;
            FXMLLoader loader = new FXMLLoader(DatosPedidoController.this.getClass().getResource(MantenimientoPedidosController.viewPath));
            node = (Node) loader.load();
            MantenimientoPedidosController desc = loader.getController();
            stackPane.getChildren().setAll(node);
        } catch (IOException ex) {
            Logger.getLogger(DatosPedidoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
