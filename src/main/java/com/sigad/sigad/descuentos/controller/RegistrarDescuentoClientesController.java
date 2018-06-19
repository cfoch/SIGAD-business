/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.descuentos.controller;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sigad.sigad.business.ClienteDescuento;
import com.sigad.sigad.business.Constantes;
import com.sigad.sigad.business.helpers.ClienteDescuentoHelper;
import com.sigad.sigad.business.helpers.GeneralHelper;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */
public class RegistrarDescuentoClientesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label lblError;

    @FXML
    private Label lblError1;

    @FXML
    private JFXDatePicker txtFechaInicio;

    @FXML
    private JFXDatePicker txtFechaFin;

    @FXML
    private JFXTextField txtValue;

    @FXML
    private JFXTextField txtCondicion;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private Label lblunit;

    @FXML
    private JFXComboBox<String> cmbTiposDescuento;

    private Boolean isEdit;

    private ClienteDescuento pc;

    public static final String viewPath = "/com/sigad/sigad/descuentos/view/registrarDescuentoClientes.fxml";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setuValidations();
    }

    public void cargarTipoDescuento() {
        cmbTiposDescuento.getItems().addAll(Constantes.TIPO_DCTO_USUARIO_X_MONTO, Constantes.TIPO_DCTO_USUARIO_X_NPEDIDOS);
    }

    public void cargarDatos() {

        LocalDate localDate = pc.getFechaInicio().toLocalDate();
        txtFechaInicio.setValue(localDate);
        localDate = pc.getFechaFin().toLocalDate();
        txtFechaFin.setValue(localDate);
        txtCondicion.setText(pc.getCondicion().toString());
        Double pct = pc.getValue() * 100;
        if (pc.getTipo().equals(Constantes.TIPO_DCTO_USUARIO_X_MONTO)) {
            txtValue.setText(pct.toString());
            lblunit.setText("PEN");
        } else {
            txtValue.setText(pct.toString());
            lblunit.setText("ped.");
        }

        cmbTiposDescuento.getItems().clear();
        cmbTiposDescuento.getItems().add(pc.getTipo());
        cmbTiposDescuento.setValue(pc.getTipo());
    }

    public void initModel(Boolean isedit, ClienteDescuento pc, StackPane st) {
        this.isEdit = isedit;
        this.pc = pc;
        cargarTipoDescuento();
        if (isedit) {
            cargarDatos();
        } else {
            this.pc = new ClienteDescuento();
        }

    }

    public void setuValidations() {
        RequiredFieldValidator r;

        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        txtValue.getValidators().add(r);
        txtValue.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {

                if (!txtValue.validate()) {
                    txtValue.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                } else {
                    txtValue.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
                }
            }
        });
        txtValue.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                txtValue.setText(oldValue);
            } else {
                if (newValue.length() > 0) {
                    Double n = Double.valueOf(newValue);
                    System.out.println(n);
                    if (n > 100.0 || n < 0.0) {
                        txtValue.setText(oldValue);
                    }
                }
            }
        });

        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        txtCondicion.getValidators().add(r);
        txtCondicion.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {

                if (!txtCondicion.validate()) {
                    txtCondicion.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                } else {
                    txtCondicion.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
                }
            }
        });

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
        txtFechaInicio.setDayCellFactory(dayCellFactory);
        txtFechaFin.setDayCellFactory(dayCellFactory);

        txtFechaInicio.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {

            try {

                if (newValue.length() == 10) {
                    LocalDate newDate = LocalDate.parse(newValue, formatter);
                    System.out.println("Valor actual de END FIELD " + txtFechaFin.getValue());
                    System.out.println("Valor actual de START FIELD " + txtFechaInicio.getValue());

                    if (txtFechaFin.getValue() != null && txtFechaFin.getValue().isBefore(newDate)) {
                        lblError.setText("La fecha de inicio debe ser menor a fecha fin");
                    }
                    lblError.setText("");

                }
            } catch (Exception e) {
                System.out.println("Entre al catch");
                System.err.println(e.getLocalizedMessage());
            }
        });

        txtFechaFin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {

            try {

                if (newValue.length() == 10) {
                    LocalDate newDate = LocalDate.parse(newValue, formatter);

                    if (newDate.isBefore(txtFechaInicio.getValue())) {
                        txtFechaFin.getEditor().textProperty().setValue("");
                        lblError.setText("Verifique el rango de fechas");
                    }
                    lblError.setText("");
                }

            } catch (Exception e) {
                System.out.println("Entre al catch");
                System.err.println(e.getLocalizedMessage());
            }
        });
    }

    public void construirDescuento() {
        pc.setTipo(cmbTiposDescuento.getValue());
        Date dateIni = Date.valueOf(txtFechaInicio.getValue());
        Date dateFin = Date.valueOf(txtFechaFin.getValue());
        pc.setFechaInicio(dateIni);
        pc.setFechaFin(dateFin);
        pc.setValue(Double.valueOf(txtValue.getText()) / 100);
        pc.setActivo(Boolean.TRUE);
        pc.setCondicion(Double.valueOf(txtCondicion.getText()));

    }

    public boolean validateFields() {
        if (!txtValue.validate() && !GeneralHelper.isNumericDouble(txtValue.getText())) {
            txtValue.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtValue.requestFocus();
            return false;
        } else if (txtCondicion.validate() && !GeneralHelper.isNumericDouble(txtValue.getText())) {
            lblError.setText("Verifique el rango de fechas");
            return false;
        } else if (txtFechaInicio.getValue().isAfter(txtFechaFin.getValue())) {
            lblError.setText("Verifique el rango de fechas");
            return false;
        } else if (txtFechaInicio.getValue() == null) {
            lblError.setText("Verifique el rango de fechas");
            return false;
        } else if (txtFechaFin.getValue() == null) {
            lblError.setText("Verifique el rango de fechas");
            return false;
        } else if (cmbTiposDescuento.getValue() == null) {
            lblError1.setText("El combo no puede ser null");
            return false;
        } else {
            lblError.setText("");
            lblError1.setText("");
            return true;
        }
    }

    @FXML
    void guardarDescuento(ActionEvent event) {
        ClienteDescuentoHelper helper = new ClienteDescuentoHelper();
        if (/*validarCampos() && cant == 1 &&*/validateFields() && !isEdit) {
            construirDescuento();
            helper.saveDescuento(pc);
            reloadTable();
            MantenimientoDescuentosController.descCliDialog.close();
        } else if (/*validarCampos() && cant == 1 &&*/validateFields() && isEdit) {
            construirDescuento();
            helper.updateDescuento(pc);
            reloadTable();
            MantenimientoDescuentosController.descCliDialog.close();
        }

    }

    @FXML
    void handleAction(ActionEvent event) {
        if (cmbTiposDescuento.getValue().equals(Constantes.TIPO_DCTO_USUARIO_X_MONTO)) {
            txtCondicion.setPromptText("Monto a superar");
            lblunit.setText("PEN");
        } else {
            txtCondicion.setPromptText("Minimo comprado");
            lblunit.setText("Unit.");
        }
    }

    public void reloadTable() {
        MantenimientoDescuentosController.descuentosUsuarios.clear();
        ClienteDescuentoHelper pdhelper = new ClienteDescuentoHelper();
        ArrayList<ClienteDescuento> ps = pdhelper.getDescuentos();
        pdhelper.close();
        ps.forEach((t) -> {
            MantenimientoDescuentosController.descuentosUsuarios.add(new MantenimientoDescuentosController.DescuentosUsuariosLista(t));
        });

    }

    @FXML
    void close(MouseEvent event) {
        MantenimientoDescuentosController.descCliDialog.close();
    }

}
