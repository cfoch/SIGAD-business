/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.*;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.ClienteDescuento;
import com.sigad.sigad.business.ComboPromocion;
import com.sigad.sigad.business.Constantes;
import com.sigad.sigad.business.DetallePedido;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.PedidoEstado;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoInsumo;
import com.sigad.sigad.business.ProductosCombos;
import com.sigad.sigad.business.TipoPago;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

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
    private ToggleGroup pago;

    @FXML
    private JFXRadioButton btnFactura;

    @FXML
    private JFXRadioButton btnEfectivo;

    @FXML
    private JFXRadioButton btnDeposito;

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

    @FXML
    private JFXTextField txtdestino;

    @FXML
    private JFXTextField txtTotal;

    @FXML
    private JFXTextField txtDescuento;

    @FXML
    private JFXTextField txtTotalPago;

    @FXML
    private JFXTextField txtCliente;

    @FXML
    private JFXTextField txtEmpresa;

    @FXML
    private JFXTextField txtdoc;

    @FXML
    private JFXTextField txtcorreo;

    @FXML
    private JFXTextField txtTipo;

    @FXML
    private JFXTextField txtDireccion;

    @FXML
    private JFXTextField txtigv;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private HashMap<Insumo, Integer> insumos = new HashMap<>();
    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/pedido/view/datosPedido.fxml";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbInicio.getItems().addAll("M", "T", "N");
        setuValidations();
    }

    public void initModel(Pedido pedido, StackPane stackPane) {
        this.pedido = pedido;
        this.stackPane = stackPane;

        if (pedido.getId() != null) {
            LocalDate localDate = pedido.getFechaEntregaEsperada().toLocalDate();
            dpFechaEntrega.setValue(localDate);
            cmbInicio.setValue(pedido.getTurno());
            cmbDedicatoria.setText(pedido.getMensajeDescripicion());
            btnBoleta.setSelected((pedido.getRucFactura()==null));
            btnFactura.setSelected((pedido.getRucFactura()!=null));
            btnBoleta.setDisable(true);
            btnFactura.setDisable(true);
        }
        txtTotal.setText(pedido.getTotal().toString());
        txtTotal.setDisable(true);
        txtdestino.setText(pedido.getDireccionDeEnvio());
        txtCliente.setText(pedido.getCliente().toString());
        txtcorreo.setText(pedido.getCliente().getCorreo());

        obtenerDescuentos();

    }

    @FXML
    void clickBoleta(MouseEvent event) {
        txtdoc.setPromptText("DNI");
        txtdoc.setText(pedido.getCliente().getDni());
        txtdoc.setDisable(true);
        txtDireccion.setDisable(true);
        txtEmpresa.setDisable(true);
    }

    @FXML
    void clickFactura(MouseEvent event) {
        txtdoc.setPromptText("RUC");
        txtdoc.setText("");
        txtdoc.setDisable(false);
        txtDireccion.setDisable(false);
        txtEmpresa.setDisable(false);
    }

    public void setuValidations() {

        JFXDatePicker minDate = new JFXDatePicker();
        minDate.setValue(LocalDate.now(ZoneId.systemDefault())); // colocar la fecha de hoy como el minimo

        final Callback<DatePicker, DateCell> dayCellFactory;

        dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isBefore(minDate.getValue())) {
                    setDisable(true);
                    setVisible(false);

                } else {
                    setVisible(true);
                    setDisable(false);
                }
            }

        };
        dpFechaEntrega.setDayCellFactory(dayCellFactory);

        dpFechaEntrega.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {

            try {

                if (newValue.length() == 10) {
                }
            } catch (Exception e) {
                System.out.println("Entre al catch");
                System.err.println(e.getLocalizedMessage());
            }
        });

    }

    public void obtenerDescuentos() {
//        try {
        ClienteDescuentoHelper helper = new ClienteDescuentoHelper();
        ArrayList<ClienteDescuento> descuentos = helper.getDescuentosVigentes();
        helper.close();
        System.out.println(descuentos.size());
        UsuarioHelper us = new UsuarioHelper();
        Usuario u = us.getUser(pedido.getCliente().getId().intValue());
        Integer numPedidos = u.getPedidoCliente().size();
        for (Iterator<ClienteDescuento> iterator = descuentos.iterator(); iterator.hasNext();) {
            ClienteDescuento t = iterator.next();
            System.out.println(t.getTipo() + t.getFechaInicio() + t.getValue());
            if (t.getTipo().equals(Constantes.TIPO_DCTO_USUARIO_X_MONTO)) {
                System.out.println("++++->" + pedido.getTotal() + "---" + t.getCondicion());
                if (pedido.getTotal() < t.getCondicion()) {
                    System.out.println("--->" + pedido.getTotal() + "---" + t.getCondicion());
                    iterator.remove();
                }
            } else if (t.getTipo().equals(Constantes.TIPO_DCTO_USUARIO_X_NPEDIDOS)) {
                if (numPedidos < t.getCondicion()) {
                    iterator.remove();
                }
            }
        }
        descuentos.forEach((t) -> {
            System.out.println(t.getCondicion().toString() + t.getFechaInicio() + t.getFechaFin());
        });
        ClienteDescuento descuentoCliente = null;
        if (descuentos.size() > 0) {
            descuentoCliente = descuentos.stream().max(Comparator.comparing(ClienteDescuento::getValue)).orElseThrow(NoSuchElementException::new);
        }
        if (descuentoCliente != null) {
            pedido.setTotal(pedido.getTotal() * (1 - descuentoCliente.getValue()));
            pedido.setDescuentoCliente(descuentoCliente);
            txtTipo.setText("Por superar " + descuentoCliente.getCondicion().toString() + " en " + descuentoCliente.getTipo());
            txtDescuento.setText(String.valueOf(descuentoCliente.getValue() * 100) + " %");
            txtTotalPago.setText(pedido.getTotal().toString());
        } else {
            txtDescuento.setText("No aplica");
            Double igv = GeneralHelper.roundTwoDecimals(pedido.getTotal() * HomeController.IGV);
            txtigv.setText(igv.toString());
            txtTotalPago.setText(GeneralHelper.roundTwoDecimals(pedido.getTotal() + igv).toString());

        }

//        } catch (Exception e) {
//
//        }
    }

    public Boolean validateFields() {
        if (btnFactura.isSelected() && !txtDireccion.validate() && txtDireccion.getLength() == 0) {
            txtDireccion.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtDireccion.requestFocus();
            return false;
        } else if (btnFactura.isSelected() && !txtEmpresa.validate() && txtEmpresa.getLength() == 0) {
            txtEmpresa.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtEmpresa.requestFocus();
            return false;
        } else if (btnFactura.isSelected() && txtdoc.getLength() != 11) {
            txtdoc.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtdoc.requestFocus();
            return false;
        } else if (!btnFactura.isSelected() && !btnBoleta.isSelected()) {
            ErrorController err = new ErrorController();
            err.loadDialog("Aviso", "Selecciona un documento legal", "ok", stackPane);
            return false;
        } else if (!btnDeposito.isSelected() && !btnEfectivo.isSelected()) {
            ErrorController err = new ErrorController();
            err.loadDialog("Aviso", "Selecciona un tipo de pago", "ok", stackPane);
            return false;
        } else if (dpFechaEntrega.getValue() == null) {
            ErrorController err = new ErrorController();
            err.loadDialog("Aviso", "Seleccione una fecha del pedido", "ok", stackPane);
            return false;
        } else if (cmbInicio.getValue() == null) {
            ErrorController err = new ErrorController();
            err.loadDialog("Aviso", "Seleccione un turno de entrega", "ok", stackPane);
            return false;
        } else {

            return true;
        }
    }

    public void construirPedido() {
        pedido.setMensajeDescripicion(cmbDedicatoria.getText());
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        pedido.setFechaVenta(timeStamp);
        java.sql.Date fechaentrega = java.sql.Date.valueOf(dpFechaEntrega.getValue());
        pedido.setFechaEntregaEsperada(fechaentrega);
        pedido.setVendedor(LoginController.user);
        TipoPagoHelper tpagohelper = new TipoPagoHelper();
        if (btnDeposito.isSelected()) {
            TipoPago tipo = tpagohelper.getTipoPago(Constantes.TIPO_PAGO_DEPOSITO);
            pedido.setTipoPago(tipo);
            tpagohelper.close();
            pedido.setTurno(cmbInicio.getValue());
            PedidoEstadoHelper hp = new PedidoEstadoHelper();
            PedidoEstado estado = hp.getEstadoByName(Constantes.ESTADO_PENDIENTE);
            pedido.addEstado(estado);
            pedido.setEstado(estado);
            hp.close();
        } else if (btnEfectivo.isSelected()) {
            TipoPago tipo = tpagohelper.getTipoPago(Constantes.TIPO_PAGO_EFECTIVO);
            pedido.setTipoPago(tipo);
            tpagohelper.close();
            pedido.setTurno(cmbInicio.getValue());
            PedidoEstadoHelper hp = new PedidoEstadoHelper();
            PedidoEstado estado = hp.getEstadoByName(Constantes.ESTADO_VENTA);
            pedido.addEstado(estado);
            pedido.setEstado(estado);
            hp.close();
        }
        if (btnFactura.isSelected()) {
            pedido.setNombreEmpresa(txtEmpresa.getText());
            pedido.setRucFactura(txtdoc.getText());
        }
        

    }
    void calcularInsumos(Producto p, Integer cantidad) {
        ArrayList<ProductoInsumo> pxi = new ArrayList(p.getProductoxInsumos());
        for (ProductoInsumo productoInsumo : pxi) {
            if (insumos.get(productoInsumo.getInsumo()) != null) {
                insumos.put(productoInsumo.getInsumo(), productoInsumo.getCantidad().intValue() * cantidad + insumos.get(productoInsumo.getInsumo()));
            } else {
                insumos.put(productoInsumo.getInsumo(), productoInsumo.getCantidad().intValue() * cantidad);
            }
        }
    }

    @FXML
    void registrarPedido(MouseEvent event) {
        try {
            if (!validateFields()) {
                return;
            }
            construirPedido();

            for (DetallePedido dp : pedido.getDetallePedido()) {
                if (dp.getProducto() != null) {
                    ProductoHelper helperProducto = new ProductoHelper();
                    Producto producto = helperProducto.getProductById(dp.getProducto().getId().intValue());
                    calcularInsumos(producto, dp.getCantidad());
                    helperProducto.close();
                } else if (dp.getCombo() != null) {
                    ComboPromocion p = dp.getCombo();
                    for (ProductosCombos productosCombos : p.getProductosxComboArray()) {
                        calcularInsumos(productosCombos.getProducto(), productosCombos.getCantidad() * dp.getCantidad());
                    }
                }
            }

            LoteInsumoHelper lihelper = new LoteInsumoHelper();
            Boolean ok = lihelper.descontarInsumos(insumos, pedido.getTienda(), pedido);
            if (ok) {
                ErrorController err = new ErrorController();
                err.loadDialog("Alerta", "El pedido fue guardado satisfactoriamente", "ok", stackPane);
            } else {
                ErrorController err = new ErrorController();
                err.loadDialog("Alerta", "No hay insumos", "ok", stackPane);
            }
            lihelper.close();
            gotoInicio();

        } catch (Exception ex) {
            Logger.getLogger(DatosPedidoController.class.getName())
                    .log(Level.SEVERE, null, ex);
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
