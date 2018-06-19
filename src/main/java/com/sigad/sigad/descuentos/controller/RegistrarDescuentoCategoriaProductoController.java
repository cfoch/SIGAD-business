/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.descuentos.controller;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sigad.sigad.business.ProductoCategoria;
import com.sigad.sigad.business.ProductoCategoriaDescuento;
import com.sigad.sigad.business.helpers.GeneralHelper;
import com.sigad.sigad.business.helpers.ProductoCategoriaDescuentoHelper;
import com.sigad.sigad.business.helpers.ProductoCategoriaHelper;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class RegistrarDescuentoCategoriaProductoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/descuentos/view/registrarDescuentoCategoriaProducto.fxml";

    @FXML
    private JFXDatePicker txtFechaInicio;

    @FXML
    private JFXDatePicker txtFechaFin;

    @FXML
    private JFXTextField txtValuePct;

    @FXML
    private Label lblError;

    @FXML
    private Label lblerrorc;
    
    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXComboBox<ProductoCategoria> cmbCategorias;

    Boolean isEdit;

    ProductoCategoriaDescuento pc;
    ObservableList<ProductoCategoria> categoriasproductos;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO\
        setupValidations();
    }

    public void cargarCategorias() {
        ProductoCategoriaHelper helper = new ProductoCategoriaHelper();
        ArrayList<ProductoCategoria> categorias = helper.getActiveProductCategories();
        categoriasproductos = FXCollections.observableArrayList(categorias);
        cmbCategorias.setItems(categoriasproductos);
        cmbCategorias.setPromptText("Categorias");
        
//        cmbCategorias.setOnAction((event) -> {
//            pc.setCategoria(cmbCategorias.getSelectionModel().getSelectedItem());
//        });

    }

    public void initModel(Boolean isedit, ProductoCategoriaDescuento pc, StackPane st) {
        this.isEdit = isedit;
        this.pc = pc;
        cargarCategorias();
        if (isedit) {
            cargarDatos();
        } else {

            this.pc = new ProductoCategoriaDescuento();
        }

    }

    public void setupValidations() {
        RequiredFieldValidator r;

        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio y númerico");
        txtValuePct.getValidators().add(r);
        txtValuePct.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {

                if (!txtValuePct.validate()) {
                    txtValuePct.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                } else {
                    txtValuePct.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
                }
            }
        });
        txtValuePct.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {

                txtValuePct.setText(oldValue);
            } else {
                if (newValue.length() > 0) {
                    Double n = Double.valueOf(newValue);
                    System.out.println(n);
                    if (n > 100.0 || n < 0.0) {
                        txtValuePct.setText(oldValue);
                    }
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

    @FXML
    void close(MouseEvent event) {
        MantenimientoDescuentosController.descDialog.close();
    }

    public boolean validateFields() {
        if (!txtValuePct.validate() && !GeneralHelper.isNumericDouble(txtValuePct.getText()) && txtValuePct.getLength() == 0) {
            txtValuePct.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtValuePct.requestFocus();
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
        } else if (cmbCategorias.getValue() == null) {
            lblerrorc.setText("Verifique el rango de fechas");
            return false;
        }else {
            lblError.setText("");
            lblerrorc.setText("");
            return true;
        }
    }

    public void cargarDatos() {

        LocalDate localDate = pc.getFechaInicio().toLocalDate();
        txtFechaInicio.setValue(localDate);
        localDate = pc.getFechaFin().toLocalDate();
        txtFechaFin.setValue(localDate);
        Double pct = pc.getValue() * 100;
        txtValuePct.setText(pct.toString());
        cmbCategorias.getItems().clear();
        cmbCategorias.getItems().add(pc.getCategoria());
        cmbCategorias.setValue(pc.getCategoria());
    }

    public void construirDescuento() {
        pc.setCategoria(cmbCategorias.getValue());
        Date dateIni = Date.valueOf(txtFechaInicio.getValue());
        Date dateFin = Date.valueOf(txtFechaFin.getValue());
        pc.setFechaInicio(dateIni);
        pc.setFechaFin(dateFin);
        pc.setValue(Double.valueOf(txtValuePct.getText()) / 100);

    }

    @FXML
    void guardarDescuento(ActionEvent event) {
        ProductoCategoriaDescuentoHelper helper = new ProductoCategoriaDescuentoHelper();
        if (/*validarCampos() && cant == 1 &&*/validateFields() && !isEdit) {
            construirDescuento();
            helper.saveDescuento(pc);
            reloadTable();
            MantenimientoDescuentosController.descCatDialog.close();
        } else if (/*validarCampos() && cant == 1 &&*/validateFields() && isEdit) {
            construirDescuento();
            helper.updateDescuento(pc);
            reloadTable();
            MantenimientoDescuentosController.descCatDialog.close();
        }

    }

    public void reloadTable() {
        MantenimientoDescuentosController.descuentosCategorias.clear();
        ProductoCategoriaDescuentoHelper pdhelper = new ProductoCategoriaDescuentoHelper();
        ArrayList<ProductoCategoriaDescuento> ps = pdhelper.getDescuentos();
        pdhelper.close();
        ps.forEach((t) -> {
            MantenimientoDescuentosController.descuentosCategorias.add(new MantenimientoDescuentosController.DescuentosCategoriaLista(t));
        });

    }

}
