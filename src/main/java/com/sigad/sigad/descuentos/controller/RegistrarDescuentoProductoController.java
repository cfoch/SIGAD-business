/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.descuentos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoDescuento;
import com.sigad.sigad.business.helpers.GeneralHelper;
import com.sigad.sigad.business.helpers.ProductoDescuentoHelper;
import com.sigad.sigad.business.helpers.ProductoHelper;
import static com.sigad.sigad.descuentos.controller.MantenimientoDescuentosController.descuentos;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */
public class RegistrarDescuentoProductoController implements Initializable {

    public static final String viewPath = "/com/sigad/sigad/descuentos/view/registrarDescuentoProducto.fxml";
    /**
     * Initializes the controller class.
     */
    public AnchorPane container;
    StackPane stackpane;

    @FXML
    private Label lblError;

    @FXML
    private JFXDatePicker txtFechaInicio;

    @FXML
    private JFXDatePicker txtFechaFin;

    @FXML
    private JFXTreeTableView<ProductoLista> tblProductos;

    @FXML
    private JFXTextField txtPrecio;

    @FXML
    private JFXTextField txtNuevoPrecio;

    @FXML
    private JFXTextField txtDescuentopct;

    @FXML
    private JFXTextField filtro;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnCancelar;

    private Boolean isEdit;
    private ProductoDescuento pd;
    @FXML
    JFXTreeTableColumn<ProductoLista, Integer> id = new JFXTreeTableColumn<>("ID");
    @FXML
    JFXTreeTableColumn<ProductoLista, String> nombre = new JFXTreeTableColumn<>("Nombre");
    @FXML
    JFXTreeTableColumn<ProductoLista, String> categoria = new JFXTreeTableColumn<>("Categoria");

    private final ObservableList<ProductoLista> prod = FXCollections.observableArrayList();
    private Paint colorStd;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        columnas();
        agregarColumnas();
        cargarDatos();
        setuValidations();
        txtDescuentopct.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {

                txtDescuentopct.setText(oldValue);
            } else {
                if (newValue.length() > 0) {
                    Double n = Double.valueOf(newValue);
                    System.out.println(n);
                    if (n > 100.0 || n < 0.0) {
                        txtDescuentopct.setText(oldValue);
                    }
                }
            }

            if (GeneralHelper.isNumericDouble(txtDescuentopct.getText()) && GeneralHelper.isNumericDouble(txtPrecio.getText())) {
                Double pct = Double.valueOf(txtDescuentopct.getText()) / 100;
                Double nprecio = (1.0 - pct) * Double.valueOf(txtPrecio.getText());
                txtNuevoPrecio.setText(GeneralHelper.roundTwoDecimals(nprecio).toString());

            } else {
                txtNuevoPrecio.clear();
            }
        });
        colorStd = txtPrecio.getFocusColor();
    }

    public void setuValidations() {
        RequiredFieldValidator r;

        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");

        txtDescuentopct.getValidators().add(r);
        txtDescuentopct.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {

                if (!txtDescuentopct.validate() && GeneralHelper.isNumericDouble(txtDescuentopct.getText())) {
                    Double t = Double.valueOf(txtDescuentopct.getText());
                    if (t <= 100 & t > 0) {
                        txtDescuentopct.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                    }
                } else {
                    txtDescuentopct.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
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
            JFXSnackbar snackbar = new JFXSnackbar(container);

            try {

                if (newValue.length() == 10) {
                    LocalDate newDate = LocalDate.parse(newValue, formatter);
                    System.out.println("Valor actual de END FIELD " + txtFechaFin.getValue());
                    System.out.println("Valor actual de START FIELD " + txtFechaInicio.getValue());

                    if (txtFechaFin.getValue() != null && txtFechaFin.getValue().isBefore(newDate)) {
                        snackbar.show("Fecha Inicio debe ser menor a Fecha Fin", 2000);
                        lblError.setText("La fecha de inicio debe ser menor a fecha fin");
                    }
                    lblError.setText("");

                }
            } catch (Exception e) {
                System.out.println("Entre al catch");
                System.err.println(e.getLocalizedMessage());
                snackbar.show("Formato de Fecha Inicial Incorrecto", 20000);
            }
        });

        txtFechaFin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            JFXSnackbar snackbar = new JFXSnackbar(container);

            try {

                if (newValue.length() == 10) {
                    LocalDate newDate = LocalDate.parse(newValue, formatter);

                    if (newDate.isBefore(txtFechaInicio.getValue())) {
                        txtFechaFin.getEditor().textProperty().setValue("");
                        snackbar.show("Verifique el rango de fechas", 2000);
                        lblError.setText("Verifique el rango de fechas");
                    }
                    lblError.setText("");
                }

            } catch (Exception e) {
                System.out.println("Entre al catch");
                System.err.println(e.getLocalizedMessage());
                snackbar.show("Formato de Fecha Final Incorrecto", 20000);
            }
        });

    }

    public void agregarFiltro() {
        filtro.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tblProductos.setPredicate((TreeItem<ProductoLista> t) -> {
                Boolean flag = t.getValue().nombre.getValue().contains(newValue) || t.getValue().categoria.getValue().contains(newValue);
                return flag;
            });
        });
    }

    public void initModel(Boolean isedit, ProductoDescuento pd, StackPane stackpane) {

        this.isEdit = isedit;
        this.stackpane = stackpane;
        agregarFiltro();
        this.pd = pd;
        if (isedit) {
            filtro.setText(pd.getProducto().getNombre());
            Instant instant = pd.getFechaInicio().toInstant();
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            txtFechaInicio.setValue(localDate);
            instant = pd.getFechaFin().toInstant();
            localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            txtFechaFin.setValue(localDate);
            Double pct = pd.getValorPct() * 100;
            txtPrecio.setDisable(true);
            txtNuevoPrecio.setText(String.valueOf(GeneralHelper.roundTwoDecimals(pd.getValorPct() * pd.getProducto().getPrecio())));

            txtNuevoPrecio.setDisable(true);
            txtDescuentopct.setText(pct.toString());

        }
    }

    public void reloadTable() {
        descuentos.clear();
        ProductoDescuentoHelper pdhelper = new ProductoDescuentoHelper();
        ArrayList<ProductoDescuento> ps = pdhelper.getDescuentos();
        pdhelper.close();
        ps.forEach((t) -> {
            descuentos.add(new MantenimientoDescuentosController.DescuentosLista(t));
        });

    }

    public void cargarDatos() {
        ProductoHelper helper = new ProductoHelper();
        ArrayList<Producto> productos = helper.getProducts();
        helper.close();
        productos.forEach((t) -> {
            prod.add(new ProductoLista(t));
        });
    }

    public void agregarColumnas() {
        final TreeItem<ProductoLista> rootPedido = new RecursiveTreeItem<>(prod, RecursiveTreeObject::getChildren);
        tblProductos.setEditable(true);
        tblProductos.getColumns().setAll(id, nombre, categoria);
        tblProductos.setRoot(rootPedido);
        tblProductos.setShowRoot(false);
        tblProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtPrecio.setText(newSelection.getValue().precio.getValue());
            }
        });

    }

    public Boolean validarCampos() {
        return txtFechaFin.getValue().toString() != "" && txtFechaFin.getValue().toString() != "" && txtDescuentopct.getText() != "";

    }

    public boolean validateFields() {
        if (!txtDescuentopct.validate() && !GeneralHelper.isNumericDouble(txtDescuentopct.getText())) {
            txtDescuentopct.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtDescuentopct.requestFocus();
            return false;
        }  else if (txtFechaInicio.getValue() == null) {
            lblError.setText("Fecha inicio incorrecta");
            return false;
        } else if (txtFechaInicio.getValue().isAfter(txtFechaFin.getValue())) {
            lblError.setText("Verifique el rango de fechas");
            return false;
        } else if (txtFechaFin.getValue() == null) {
            lblError.setText("Fecha fin incorrecta");
            return false;
        } else {

            return true;
        }
    }

    public void construirDescuento(ProductoDescuento nuevo) {

        nuevo.setActivo(Boolean.TRUE);
        Date dateIni = Date.valueOf(txtFechaInicio.getValue());
        Date dateFin = Date.valueOf(txtFechaFin.getValue());
        nuevo.setFechaInicio(dateIni);
        nuevo.setFechaFin(dateFin);
        nuevo.setValorPct(Double.valueOf(txtDescuentopct.getText()) / 100.0);
        nuevo.setProducto(tblProductos.getSelectionModel().getSelectedItem().getValue().producto);
    }

    @FXML
    void guardarDescuento(ActionEvent event) {
        Integer cant = tblProductos.getSelectionModel().getSelectedItems().size();
        ProductoDescuentoHelper helper = new ProductoDescuentoHelper();
        if (validateFields() && cant == 1 && !isEdit) {
            ProductoDescuento nuevo = new ProductoDescuento();
            nuevo.setActivo(Boolean.TRUE);
            construirDescuento(nuevo);
            helper.saveDescuento(nuevo);
            reloadTable();
            MantenimientoDescuentosController.descDialog.close();
        } else if (validateFields() && cant == 1 && isEdit) {
            construirDescuento(pd);
            helper.updateDescuento(pd);
            reloadTable();
            MantenimientoDescuentosController.descDialog.close();
        }

    }

    @FXML
    void close(MouseEvent event) {
        MantenimientoDescuentosController.descDialog.close();
    }

    public void seleccionStock() {


    }

    @FXML
    void calcularNuevoPrecio(InputMethodEvent event) {
//        if (GeneralHelper.isNumeric(txtDescuentopct.getText())) {
//            Double pct = Double.valueOf(txtDescuentopct.getText()) / 100;
//            Double nprecio = (1.0 - pct) * Double.valueOf(txtPrecio.getText());
//            txtNuevoPrecio.setText(GeneralHelper.roundTwoDecimals(nprecio).toString());
//        } else {
//            txtDescuentopct.clear();
//        }

    }

    public void columnas() {
        id.setPrefWidth(80);
        id.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, Integer> param) -> param.getValue().getValue().id.asObject());

        nombre.setPrefWidth(80);
        nombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().nombre);

        categoria.setPrefWidth(80);
        categoria.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().categoria);
    }

    class ProductoLista extends RecursiveTreeObject<ProductoLista> {

        private IntegerProperty id;
        private StringProperty nombre;
        private StringProperty precio;
        private StringProperty categoria;
        private Producto producto;

        public ProductoLista(Producto producto) {
            this.id = new SimpleIntegerProperty(producto.getId().intValue());
            this.nombre = new SimpleStringProperty(producto.getNombre());
            this.precio = new SimpleStringProperty(producto.getPrecio().toString());
            this.categoria = new SimpleStringProperty(producto.getCategoria().getNombre());
            this.producto = producto;
        }
    }

}
