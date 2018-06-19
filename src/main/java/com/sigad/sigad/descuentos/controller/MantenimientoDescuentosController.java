/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.descuentos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.business.ClienteDescuento;
import com.sigad.sigad.business.ComboPromocion;
import com.sigad.sigad.business.ProductoCategoriaDescuento;
import com.sigad.sigad.business.helpers.ProductoCategoriaDescuentoHelper;
import com.sigad.sigad.business.ProductoDescuento;
import com.sigad.sigad.business.helpers.ClienteDescuentoHelper;
import com.sigad.sigad.business.helpers.ComboPromocionHelper;
import com.sigad.sigad.business.helpers.ProductoDescuentoHelper;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */
public class MantenimientoDescuentosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/descuentos/view/mantenimientoDescuentos.fxml";

    @FXML
    private StackPane stackPane;

    @FXML
    private StackPane stackPaneCat;
    @FXML
    private StackPane stackPaneCli;

    @FXML
    private StackPane stackPaneCmb;
    private JFXPopup popup;
    
    public static JFXDialog descDialog;

    public static JFXDialog descCatDialog;

    public static JFXDialog descCliDialog;

    public static JFXDialog comboDialog;

    JFXTreeTableColumn<DescuentosLista, Integer> id = new JFXTreeTableColumn<>("id");
    JFXTreeTableColumn<DescuentosLista, String> producto = new JFXTreeTableColumn<>("Producto");
    JFXTreeTableColumn<DescuentosLista, String> fechaInicio = new JFXTreeTableColumn<>("F. Inicio");
    JFXTreeTableColumn<DescuentosLista, String> fechaFin = new JFXTreeTableColumn<>("F. Fin");
    JFXTreeTableColumn<DescuentosLista, Double> valorPct = new JFXTreeTableColumn<>("Valor(%)");

    JFXTreeTableColumn<DescuentosCategoriaLista, Integer> idCat = new JFXTreeTableColumn<>("id");
    JFXTreeTableColumn<DescuentosCategoriaLista, String> categoria = new JFXTreeTableColumn<>("Categoria");
    JFXTreeTableColumn<DescuentosCategoriaLista, String> fechaInicioCat = new JFXTreeTableColumn<>("F. Inicio");
    JFXTreeTableColumn<DescuentosCategoriaLista, String> fechaFinCat = new JFXTreeTableColumn<>("F. Fin");
    JFXTreeTableColumn<DescuentosCategoriaLista, Double> valorPctCat = new JFXTreeTableColumn<>("Valor(%)");

    JFXTreeTableColumn<DescuentosUsuariosLista, Integer> idCli = new JFXTreeTableColumn<>("id");
    JFXTreeTableColumn<DescuentosUsuariosLista, String> tipo = new JFXTreeTableColumn<>("Tipo");
    JFXTreeTableColumn<DescuentosUsuariosLista, String> masde = new JFXTreeTableColumn<>("Aplicable a");
    JFXTreeTableColumn<DescuentosUsuariosLista, String> fechaInicioCli = new JFXTreeTableColumn<>("F. Inicio");
    JFXTreeTableColumn<DescuentosUsuariosLista, String> fechaFinCli = new JFXTreeTableColumn<>("F. Fin");
    JFXTreeTableColumn<DescuentosUsuariosLista, Double> valorCli = new JFXTreeTableColumn<>("Valor(%)");

    JFXTreeTableColumn<CombosProductosLista, Integer> idCombo = new JFXTreeTableColumn<>("id");
    JFXTreeTableColumn<CombosProductosLista, String> descCombo = new JFXTreeTableColumn<>("Descripcion");
    JFXTreeTableColumn<CombosProductosLista, String> nombre = new JFXTreeTableColumn<>("Nombre");
    JFXTreeTableColumn<CombosProductosLista, String> fechaInicioCmb = new JFXTreeTableColumn<>("F. Inicio");
    JFXTreeTableColumn<CombosProductosLista, String> fechaFinCmb = new JFXTreeTableColumn<>("F. Fin");
    JFXTreeTableColumn<CombosProductosLista, Double> precioBase = new JFXTreeTableColumn<>("Precio(PEN)");

    @FXML
    private JFXTreeTableView<DescuentosLista> tblDescuentos;
    public static final ObservableList<DescuentosLista> descuentos = FXCollections.observableArrayList();
    private Boolean isEdit;

    @FXML
    private JFXTreeTableView<DescuentosCategoriaLista> tblDescCat;
    public static final ObservableList<DescuentosCategoriaLista> descuentosCategorias = FXCollections.observableArrayList();

    @FXML
    private JFXTreeTableView<DescuentosUsuariosLista> tblDescCliente;
    public static final ObservableList<DescuentosUsuariosLista> descuentosUsuarios = FXCollections.observableArrayList();

    @FXML
    private JFXTreeTableView<CombosProductosLista> tblCombos;
    public static final ObservableList<CombosProductosLista> descuentosCombos = FXCollections.observableArrayList();
    @FXML
    private JFXButton moreBtnProducto;
    @FXML
    private JFXTextField filtroProducto;
    @FXML
    private JFXButton btnAddDescuento;
    @FXML
    private JFXButton moreBtnCategoria;
    @FXML
    private JFXTextField filtroCategoria;
    @FXML
    private JFXButton btnAddDescCat;
    @FXML
    private JFXButton moreBtnCliente;
    @FXML
    private JFXTextField filtroCliente;
    @FXML
    private JFXButton btnAddDescCliente;
    @FXML
    private JFXButton moreBtnCombos;
    @FXML
    private JFXTextField filtroCombos;
    @FXML
    private JFXButton btnAddCombo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        isEdit = Boolean.FALSE;
        columnasDescuentos();
        agregarColumnas();
        columnasDescuentosCategorias();
        agregarColumnasCategorias();
        llenarTabla();
        llenarTablaCategoriaDescuento();

        columnadDescuentosClientes();
        agregarColumnasClientes();
        llenarTablaClientes();
        columnasCombos();
        agregarColumnasCombos();
        llenarTablaCombos();
        agregarFiltros();
    }

    public void agregarFiltros() {
        filtroProducto.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tblDescuentos.setPredicate((TreeItem<DescuentosLista> t) -> {
                Boolean flag = t.getValue().id.getValue().toString().contains(newValue);
                return flag;
            });
        });
        
        filtroCategoria.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tblDescCat.setPredicate((TreeItem<DescuentosCategoriaLista> t) -> {
                Boolean flag = t.getValue().id.getValue().toString().contains(newValue);
                return flag;
            });
        });
        
        filtroCliente.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tblDescCliente.setPredicate((TreeItem<DescuentosUsuariosLista> t) -> {
                Boolean flag = t.getValue().id.getValue().toString().contains(newValue);
                return flag;
            });
        });
        
        filtroCombos.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tblCombos.setPredicate((TreeItem<CombosProductosLista> t) -> {
                Boolean flag = t.getValue().id.getValue().toString().contains(newValue);
                return flag;
            });
        });
    }
    
    public void llenarTabla() {
        descuentos.clear();
        ProductoDescuentoHelper pdhelper = new ProductoDescuentoHelper();
        ArrayList<ProductoDescuento> pd = pdhelper.getDescuentos();
        pdhelper.close();
        pd.forEach((t) -> {
            descuentos.add(new DescuentosLista(t));
        });

    }

    public void llenarTablaCategoriaDescuento() {
        descuentosCategorias.clear();
        ProductoCategoriaDescuentoHelper pdhelper = new ProductoCategoriaDescuentoHelper();
        ArrayList<ProductoCategoriaDescuento> pd = pdhelper.getDescuentos();
        pdhelper.close();
        pd.forEach((t) -> {
            descuentosCategorias.add(new DescuentosCategoriaLista(t));
        });

    }

    public void llenarTablaClientes() {
        descuentosUsuarios.clear();
        ClienteDescuentoHelper pdhelper = new ClienteDescuentoHelper();
        ArrayList<ClienteDescuento> pd = pdhelper.getDescuentos();
        pdhelper.close();
        pd.forEach((t) -> {
            descuentosUsuarios.add(new DescuentosUsuariosLista(t));
        });

    }

    public void llenarTablaCombos() {
        descuentosCombos.clear();
        ComboPromocionHelper pdhelper = new ComboPromocionHelper();
        ArrayList<ComboPromocion> pd = pdhelper.getCombos();
        pdhelper.close();
        pd.forEach((t) -> {
            descuentosCombos.add(new CombosProductosLista(t));
        });

    }

    public void columnasDescuentos() {
        id.setPrefWidth(120);
        id.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosLista, Integer> param) -> param.getValue().getValue().id.asObject());

        fechaFin.setPrefWidth(120);
        fechaFin.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosLista, String> param) -> param.getValue().getValue().fechaFin);

        fechaInicio.setPrefWidth(120);
        fechaInicio.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosLista, String> param) -> param.getValue().getValue().fechaInicio);

        valorPct.setPrefWidth(120);
        valorPct.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosLista, Double> param) -> param.getValue().getValue().valorPct.asObject());

        producto.setPrefWidth(120);
        producto.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosLista, String> param) -> param.getValue().getValue().producto);

    }

    public void columnasDescuentosCategorias() {
        idCat.setPrefWidth(120);
        idCat.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosCategoriaLista, Integer> param) -> param.getValue().getValue().id.asObject());

        fechaFinCat.setPrefWidth(120);
        fechaFinCat.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosCategoriaLista, String> param) -> param.getValue().getValue().fechaFin);

        fechaInicioCat.setPrefWidth(120);
        fechaInicioCat.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosCategoriaLista, String> param) -> param.getValue().getValue().fechaInicio);

        valorPctCat.setPrefWidth(120);
        valorPctCat.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosCategoriaLista, Double> param) -> param.getValue().getValue().valorPct.asObject());

        categoria.setPrefWidth(120);
        categoria.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosCategoriaLista, String> param) -> param.getValue().getValue().categoria);

    }

    public void columnasCombos() {
        idCombo.setPrefWidth(120);
        idCombo.setCellValueFactory((TreeTableColumn.CellDataFeatures<CombosProductosLista, Integer> param) -> param.getValue().getValue().id.asObject());

        fechaFinCmb.setPrefWidth(120);
        fechaFinCmb.setCellValueFactory((TreeTableColumn.CellDataFeatures<CombosProductosLista, String> param) -> param.getValue().getValue().fechaFin);

        fechaInicioCmb.setPrefWidth(120);
        fechaInicioCmb.setCellValueFactory((TreeTableColumn.CellDataFeatures<CombosProductosLista, String> param) -> param.getValue().getValue().fechaInicio);

        precioBase.setPrefWidth(120);
        precioBase.setCellValueFactory((TreeTableColumn.CellDataFeatures<CombosProductosLista, Double> param) -> param.getValue().getValue().valor.asObject());

        descCombo.setPrefWidth(120);
        descCombo.setCellValueFactory((TreeTableColumn.CellDataFeatures<CombosProductosLista, String> param) -> param.getValue().getValue().descripcion);
        nombre.setPrefWidth(120);
        nombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<CombosProductosLista, String> param) -> param.getValue().getValue().nombre);
    }

    public void columnadDescuentosClientes() {
        idCli.setPrefWidth(120);
        idCli.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosUsuariosLista, Integer> param) -> param.getValue().getValue().id.asObject());

        fechaFinCli.setPrefWidth(120);
        fechaFinCli.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosUsuariosLista, String> param) -> param.getValue().getValue().fechaFin);

        fechaInicioCli.setPrefWidth(120);
        fechaInicioCli.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosUsuariosLista, String> param) -> param.getValue().getValue().fechaInicio);

        valorCli.setPrefWidth(120);
        valorCli.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosUsuariosLista, Double> param) -> param.getValue().getValue().valorPct.asObject());

        tipo.setPrefWidth(120);
        tipo.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosUsuariosLista, String> param) -> param.getValue().getValue().tipo);

        masde.setPrefWidth(120);
        masde.setCellValueFactory((TreeTableColumn.CellDataFeatures<DescuentosUsuariosLista, String> param) -> param.getValue().getValue().masde);

    }

    public void agregarColumnas() {
        final TreeItem<DescuentosLista> rootPedido = new RecursiveTreeItem<>(descuentos, RecursiveTreeObject::getChildren);
        tblDescuentos.setEditable(true);
        tblDescuentos.getColumns().setAll(id, fechaInicio, fechaFin, valorPct, producto);
        tblDescuentos.setRoot(rootPedido);
        tblDescuentos.setShowRoot(false);
        tblDescuentos.setRowFactory(new Callback<TreeTableView<DescuentosLista>, TreeTableRow<DescuentosLista>>() {
            @Override
            public TreeTableRow<DescuentosLista> call(TreeTableView<DescuentosLista> param) {
                TreeTableRow<DescuentosLista> row = new TreeTableRow<>();
                row.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        DescuentosLista rowData = row.getItem();
                        isEdit = Boolean.TRUE;
                        editRegistrarDescuento(rowData.descuentoObj);
                    }
                });
                return row; //To change body of generated lambdas, choose Tools | Templates.
            }
        });
    }

    
    public void agregarColumnasCombos() {
        final TreeItem<CombosProductosLista> rootPedido = new RecursiveTreeItem<>(descuentosCombos, RecursiveTreeObject::getChildren);
        tblCombos.setEditable(true);
        tblCombos.getColumns().setAll(idCombo, nombre, descCombo, fechaInicioCmb, fechaFinCmb, precioBase);
        tblCombos.setRoot(rootPedido);
        tblCombos.setShowRoot(false);
        tblCombos.setRowFactory(new Callback<TreeTableView<CombosProductosLista>, TreeTableRow<CombosProductosLista>>() {
            @Override
            public TreeTableRow<CombosProductosLista> call(TreeTableView<CombosProductosLista> param) {
                TreeTableRow<CombosProductosLista> row = new TreeTableRow<>();
                row.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        CombosProductosLista rowData = row.getItem();
                        isEdit = Boolean.TRUE;
                        editRegistrarComboPromocion(rowData.descuentoObj);
                    }
                });
                return row; //To change body of generated lambdas, choose Tools | Templates.
            }
        });
    }
    public void agregarColumnasCategorias() {
        final TreeItem<DescuentosCategoriaLista> rootPedido = new RecursiveTreeItem<>(descuentosCategorias, RecursiveTreeObject::getChildren);
        tblDescCat.setEditable(true);
        tblDescCat.getColumns().setAll(idCat, fechaInicioCat, fechaFinCat, valorPctCat, categoria);
        tblDescCat.setRoot(rootPedido);
        tblDescCat.setShowRoot(false);
        tblDescCat.setRowFactory(new Callback<TreeTableView<DescuentosCategoriaLista>, TreeTableRow<DescuentosCategoriaLista>>() {
            @Override
            public TreeTableRow<DescuentosCategoriaLista> call(TreeTableView<DescuentosCategoriaLista> param) {
                TreeTableRow<DescuentosCategoriaLista> row = new TreeTableRow<>();
                row.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        DescuentosCategoriaLista rowData = row.getItem();
                        isEdit = Boolean.TRUE;
                        editRegistrarDescuentoCategoria(rowData.descuentoObj);
                    }
                });
                return row; //To change body of generated lambdas, choose Tools | Templates.
            }
        });
    }

    public void agregarColumnasClientes() {
        final TreeItem<DescuentosUsuariosLista> rootPedido = new RecursiveTreeItem<>(descuentosUsuarios, RecursiveTreeObject::getChildren);
        tblDescCliente.setEditable(true);
        tblDescCliente.getColumns().setAll(idCli, fechaInicioCli, fechaFinCli, valorCli, tipo, masde);
        tblDescCliente.setRoot(rootPedido);
        tblDescCliente.setShowRoot(false);
        tblDescCliente.setRowFactory(new Callback<TreeTableView<DescuentosUsuariosLista>, TreeTableRow<DescuentosUsuariosLista>>() {
            @Override
            public TreeTableRow<DescuentosUsuariosLista> call(TreeTableView<DescuentosUsuariosLista> param) {
                TreeTableRow<DescuentosUsuariosLista> row = new TreeTableRow<>();
                row.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        DescuentosUsuariosLista rowData = row.getItem();
                        isEdit = Boolean.TRUE;
                        editRegistrarDescuentoCliente(rowData.descuentoObj);
                    }
                });
                return row; //To change body of generated lambdas, choose Tools | Templates.
            }
        });
    }

    @FXML
    void handleAction(ActionEvent event) {
        JFXButton edit = new JFXButton("Editar");
        JFXButton delete = new JFXButton("Eliminar");
        
        edit.setOnAction((ActionEvent event1) -> {
            popup.hide();
            if(event.getSource() == moreBtnProducto){
            }else if(event.getSource() == moreBtnCliente){
            }else if(event.getSource() == moreBtnCategoria){
            }else if(event.getSource() == moreBtnCombos){
            }

        });

        delete.setOnAction((ActionEvent event2) -> {
            popup.hide();
            if(event.getSource() == moreBtnProducto){
            }else if(event.getSource() == moreBtnCliente){
            }else if(event.getSource() == moreBtnCategoria){
            }else if(event.getSource() == moreBtnCombos){
            }
        });
        
        edit.setPadding(new Insets(20));
        edit.setPrefSize(145, 40);
        delete.setPadding(new Insets(20));
        delete.setPrefSize(145, 40);
        
        VBox vBox = new VBox(edit, delete);
        
        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }
    
    public void editRegistrarDescuentoCliente(ClienteDescuento pd) {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Agregar descuento por Clientes"));
            Node node;
            FXMLLoader loader = new FXMLLoader(MantenimientoDescuentosController.this.getClass().getResource(RegistrarDescuentoClientesController.viewPath));
            node = (Node) loader.load();
            RegistrarDescuentoClientesController desc = loader.getController();
            desc.initModel(isEdit, pd, stackPaneCli);
            content.setBody(node);
            descCliDialog = new JFXDialog(stackPaneCli, content, JFXDialog.DialogTransition.CENTER);
            descCliDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoDescuentosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editRegistrarDescuento(ProductoDescuento pd) {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Agregar descuento de producto"));
            Node node;
            FXMLLoader loader = new FXMLLoader(MantenimientoDescuentosController.this.getClass().getResource(RegistrarDescuentoProductoController.viewPath));
            node = (Node) loader.load();
            RegistrarDescuentoProductoController desc = loader.getController();
            desc.initModel(isEdit, pd, stackPane);
            content.setBody(node);
            descDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
            descDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoDescuentosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editRegistrarDescuentoCategoria(ProductoCategoriaDescuento pd) {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Agregar descuento de categoria"));
            Node node;
            FXMLLoader loader = new FXMLLoader(MantenimientoDescuentosController.this.getClass().getResource(RegistrarDescuentoCategoriaProductoController.viewPath));
            node = (Node) loader.load();
            RegistrarDescuentoCategoriaProductoController desc = loader.getController();
            desc.initModel(isEdit, pd, stackPaneCat);
            content.setBody(node);
            descCatDialog = new JFXDialog(stackPaneCat, content, JFXDialog.DialogTransition.CENTER);
            descCatDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoDescuentosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editRegistrarComboPromocion(ComboPromocion pd) {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Agregar combo"));
            Node node;
            FXMLLoader loader = new FXMLLoader(MantenimientoDescuentosController.this.getClass().getResource(RegistrarComboProductosController.viewPath));
            node = (Node) loader.load();
            RegistrarComboProductosController desc = loader.getController();
            desc.initModel(isEdit, pd, stackPaneCmb);
            content.setBody(node);
            comboDialog = new JFXDialog(stackPaneCmb, content, JFXDialog.DialogTransition.CENTER);
            comboDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoDescuentosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void gotAgregarDescuento(MouseEvent event) {
        isEdit = Boolean.FALSE;
        editRegistrarDescuento(null);
    }

    void gotAgregarDescuentoCategoria(MouseEvent event) {
        isEdit = Boolean.FALSE;
        editRegistrarDescuentoCategoria(null);
    }

    void gotAgregarDescuentoCliente(MouseEvent event) {
        isEdit = Boolean.FALSE;
        editRegistrarDescuentoCliente(null);
    }

    void gotAgregarCombo(MouseEvent event) {
        isEdit = Boolean.FALSE;
        editRegistrarComboPromocion(null);

    }

    public static class DescuentosLista extends RecursiveTreeObject<DescuentosLista> {

        IntegerProperty id;
        StringProperty fechaInicio;
        StringProperty fechaFin;
        DoubleProperty valorPct;
        StringProperty producto;
        ProductoDescuento descuentoObj;

        public DescuentosLista(ProductoDescuento descuento) {
            this.id = new SimpleIntegerProperty(descuento.getId().intValue());
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaFin = new SimpleStringProperty(f.format(descuento.getFechaFin()));
            this.fechaInicio = new SimpleStringProperty(f.format(descuento.getFechaInicio()));
            this.valorPct = new SimpleDoubleProperty(descuento.getValorPct() * 100);
            this.producto = new SimpleStringProperty(descuento.getProducto().getNombre());
            this.descuentoObj = descuento;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DescuentosLista) {
                DescuentosLista dl = (DescuentosLista) o;
                return dl.id.equals(this.id);

            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + Objects.hashCode(this.id);
            return hash;
        }

    }

    public static class DescuentosCategoriaLista extends RecursiveTreeObject<DescuentosCategoriaLista> {

        IntegerProperty id;
        StringProperty fechaInicio;
        StringProperty fechaFin;
        DoubleProperty valorPct;
        StringProperty categoria;
        ProductoCategoriaDescuento descuentoObj;

        public DescuentosCategoriaLista(ProductoCategoriaDescuento descuento) {
            this.id = new SimpleIntegerProperty(descuento.getId().intValue());
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaFin = new SimpleStringProperty(f.format(descuento.getFechaFin()));
            this.fechaInicio = new SimpleStringProperty(f.format(descuento.getFechaInicio()));
            this.valorPct = new SimpleDoubleProperty(descuento.getValue() * 100);
            this.categoria = new SimpleStringProperty(descuento.getCategoria().getNombre());
            this.descuentoObj = descuento;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DescuentosCategoriaLista) {
                DescuentosCategoriaLista dl = (DescuentosCategoriaLista) o;
                return dl.id.equals(this.id);

            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + Objects.hashCode(this.id);
            return hash;
        }

    }

    public static class DescuentosUsuariosLista extends RecursiveTreeObject<DescuentosUsuariosLista> {

        IntegerProperty id;
        StringProperty fechaInicio;
        StringProperty fechaFin;
        DoubleProperty valorPct;
        StringProperty tipo;
        StringProperty masde;
        ClienteDescuento descuentoObj;

        public DescuentosUsuariosLista(ClienteDescuento descuento) {
            this.id = new SimpleIntegerProperty(descuento.getId().intValue());
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaFin = new SimpleStringProperty(f.format(descuento.getFechaFin()));
            this.fechaInicio = new SimpleStringProperty(f.format(descuento.getFechaInicio()));
            this.valorPct = new SimpleDoubleProperty(descuento.getValue() * 100);
            this.tipo = new SimpleStringProperty(descuento.getTipo());
            this.masde = new SimpleStringProperty(descuento.getCondicion().toString());
            this.descuentoObj = descuento;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DescuentosUsuariosLista) {
                DescuentosUsuariosLista dl = (DescuentosUsuariosLista) o;
                return dl.id.equals(this.id);

            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + Objects.hashCode(this.id);
            return hash;
        }

    }

    public static class CombosProductosLista extends RecursiveTreeObject<CombosProductosLista> {

        IntegerProperty id;
        StringProperty fechaInicio;
        StringProperty descripcion;
        StringProperty fechaFin;
        StringProperty stockMaximo;
        StringProperty consumidos;
        StringProperty nombre;
        DoubleProperty valor;
        ComboPromocion descuentoObj;

        public CombosProductosLista(ComboPromocion combo) {
            this.id = new SimpleIntegerProperty(combo.getId().intValue());
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            this.fechaFin = new SimpleStringProperty(f.format(combo.getFechaFin()));
            this.nombre = new SimpleStringProperty(combo.getNombre());
            this.fechaInicio = new SimpleStringProperty(f.format(combo.getFechaInicio()));
            this.valor = new SimpleDoubleProperty(combo.getPreciounitario());
            //this.consumidos = new SimpleStringProperty(combo.getNumVendidos().toString());
            //this.stockMaximo = new SimpleStringProperty(combo.getMaxDisponible().toString());
            this.descripcion = new SimpleStringProperty(combo.getDescripcion());
            this.descuentoObj = combo;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof CombosProductosLista) {
                CombosProductosLista dl = (CombosProductosLista) o;
                return dl.id.equals(this.id);

            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + Objects.hashCode(this.id);
            return hash;
        }

    }
}
