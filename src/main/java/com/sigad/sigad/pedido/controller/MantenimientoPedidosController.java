/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Constantes;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.helpers.PedidoHelper;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
public class MantenimientoPedidosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    JFXPopup popup;
    @FXML
    JFXButton moreBtn;
    @FXML
    StackPane hiddenSp;
    JFXDialog direccionDialog;
    public static JFXDialog viewDialog;
    public static JFXDialog payDialog;
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, Integer> id = new JFXTreeTableColumn<>("ID");
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, String> cliente = new JFXTreeTableColumn<>("Cliente");
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, String> destino = new JFXTreeTableColumn<>("Dirección destino");
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, String> fecha = new JFXTreeTableColumn<>("Fecha");
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, String> estado = new JFXTreeTableColumn<>("Estado");
    @FXML
    JFXTreeTableColumn<PedidoOrdenLista, String> tipopago = new JFXTreeTableColumn<>("Tipo pago");
    @FXML
    private JFXTreeTableView<PedidoOrdenLista> tablaPedidos;
    Pedido pedido = new Pedido();

    public static final ObservableList<PedidoOrdenLista> pedidos = FXCollections.observableArrayList();
    public static final String viewPath = "/com/sigad/sigad/pedido/view/mantenimientoPedidos.fxml";
    private Boolean isEdit;

    @FXML
    private JFXTextField filtro;
    @FXML
    private JFXButton btnNuevo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        columnas();
        agregarColumnas();
        cargarDatos();
    }

    public void agregarFiltro() {
        filtro.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tablaPedidos.setPredicate((TreeItem<PedidoOrdenLista> t) -> {
                Boolean flag = t.getValue().id.getValue().toString().contains(newValue) || t.getValue().cliente.getValue().contains(newValue) || t.getValue().direccion.getValue().contains(newValue) || t.getValue().fecha.getValue().contains(newValue);
                return flag;
            });
        });
    }

    public void columnas() {
        id.setPrefWidth(70);
        id.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, Integer> param) -> param.getValue().getValue().id.asObject());

        cliente.setPrefWidth(300);
        cliente.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, String> param) -> param.getValue().getValue().cliente);

        destino.setPrefWidth(350);
        destino.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, String> param) -> param.getValue().getValue().direccion);

        fecha.setPrefWidth(100);
        fecha.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, String> param) -> param.getValue().getValue().fecha);

        estado.setPrefWidth(70);
        estado.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, String> param) -> param.getValue().getValue().estado);

        tipopago.setPrefWidth(70);
        tipopago.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoOrdenLista, String> param) -> param.getValue().getValue().tipopago);
    }

    public void cargarDatos() {
        pedidos.clear();
        PedidoHelper helper = new PedidoHelper();
        ArrayList<Pedido> peds = helper.getPedidos();
        peds.forEach((t) -> {
            pedidos.add(new PedidoOrdenLista(t));
        });
    }

    public void agregarColumnas() {
        final TreeItem<PedidoOrdenLista> rootPedido = new RecursiveTreeItem<>(pedidos, RecursiveTreeObject::getChildren);
        tablaPedidos.setEditable(true);
        tablaPedidos.getColumns().setAll(id, cliente, destino, fecha, estado, tipopago);
        tablaPedidos.setRoot(rootPedido);
        tablaPedidos.setShowRoot(false);
        tablaPedidos.setRowFactory(new Callback<TreeTableView<PedidoOrdenLista>, TreeTableRow<PedidoOrdenLista>>() {
            @Override
            public TreeTableRow<PedidoOrdenLista> call(TreeTableView<PedidoOrdenLista> param) {
                TreeTableRow<PedidoOrdenLista> row = new TreeTableRow<>();
                row.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        PedidoOrdenLista rowData = row.getItem();
                        pedido = rowData.pedido;
                        verPedido();

                    }
                });
                return row; //To change body of generated lambdas, choose Tools | Templates.
            }
        });

    }

    public void verPedido() {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Ver Pedido"));
            Node node;
            FXMLLoader loader = new FXMLLoader(MantenimientoPedidosController.this.getClass().getResource(EditarEliminarPedidoController.viewPath));
            node = (Node) loader.load();
            EditarEliminarPedidoController el = loader.getController();
            el.initModel(isEdit, pedido, hiddenSp);
            content.setBody(node);
            viewDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
            viewDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoPedidosController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void editarPedido() {
        try {
            Node node;
            FXMLLoader loader = new FXMLLoader(MantenimientoPedidosController.this.getClass().getResource(SeleccionarProductosController.viewPath));
            node = (Node) loader.load();
            SeleccionarProductosController el = loader.getController();
            el.initModel(pedido, hiddenSp);
            hiddenSp.getChildren().setAll(node);
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoPedidosController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void pagarPedido() {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Pagar Pedido"));
            Node node;
            FXMLLoader loader = new FXMLLoader(MantenimientoPedidosController.this.getClass().getResource(PagoPedidoController.viewPath));
            node = (Node) loader.load();
            PagoPedidoController el = loader.getController();
            el.initModel(pedido);
            content.setBody(node);
            payDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
            payDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoPedidosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void verDevolucion() {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Devolucion"));
            Node node;
            System.out.println(DevolucionPedidoController.viewPath + EditarEliminarPedidoController.viewPath);
            FXMLLoader loader = new FXMLLoader(MantenimientoPedidosController.this.getClass().getResource(DevolucionPedidoController.viewPath));
            node = (Node) loader.load();
            DevolucionPedidoController el = loader.getController();
            el.initModel(isEdit, pedido, hiddenSp);
            content.setBody(node);
            viewDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
            viewDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoPedidosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void reloadTable() {
        MantenimientoPedidosController.pedidos.clear();
        PedidoHelper pdhelper = new PedidoHelper();
        ArrayList<Pedido> ps = pdhelper.getPedidos();
        pdhelper.close();
        ps.forEach((t) -> {
            MantenimientoPedidosController.pedidos.add(new MantenimientoPedidosController.PedidoOrdenLista(t));
        });

    }

    public void initPopup() {

        JFXButton ver = new JFXButton("Ver");
        JFXButton edit = new JFXButton("Editar");
        JFXButton eliminar = new JFXButton("Cancelar");
        JFXButton pago = new JFXButton("Pagar");
        JFXButton devolucion = new JFXButton("Devolucion");

        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                try {
                    editarPedido();
                } catch (Exception ex) {
                    Logger.getLogger(MantenimientoPedidosController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        ver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                try {
                    verPedido();
                } catch (Exception ex) {

                }
            }
        });

        eliminar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                try {
                    
                } catch (Exception ex) {

                }
            }
        });

        pago.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                try {
                    pagarPedido();

                } catch (Exception ex) {

                }
            }
        });

        devolucion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                try {
                    verDevolucion();
                } catch (Exception ex) {

                }
            }
        });

        edit.setPadding(new Insets(20));
        edit.setPrefSize(145, 40);

        ver.setPadding(new Insets(20));
        ver.setPrefSize(145, 40);

        eliminar.setPadding(new Insets(20));
        eliminar.setPrefSize(145, 40);

        pago.setPadding(new Insets(20));
        pago.setPrefSize(145, 40);
        
        devolucion.setPadding(new Insets(20));
        devolucion.setPrefSize(145, 40);

        VBox vBox = new VBox();
        if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_PENDIENTE)) {
            vBox.getChildren().add(edit);
            vBox.getChildren().add(pago);
            vBox.getChildren().add(ver);
            vBox.getChildren().add(eliminar);
            
        }
        if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_VENTA) && pedido.getTipoPago().getDescripcion().equals(Constantes.TIPO_PAGO_DEPOSITO)) {
            vBox.getChildren().add(ver);
            vBox.getChildren().add(eliminar);
        }

        if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_VENTA) && pedido.getTipoPago().getDescripcion().equals(Constantes.TIPO_PAGO_EFECTIVO)) {
            vBox.getChildren().add(edit);
            vBox.getChildren().add(ver);
            vBox.getChildren().add(eliminar);
        }

        if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_DESPACHO)) {
            vBox.getChildren().add(ver);
            vBox.getChildren().add(eliminar);
        }

        if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_FINALIZADO)) {
            vBox.getChildren().add(ver);
            vBox.getChildren().add(devolucion);
        }
        if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_CANCELADO)) {
            vBox.getChildren().add(ver);
        }
        if (pedido.getEstado().getNombre().equals(Constantes.ESTADO_DEVOLUCION)) {
            vBox.getChildren().add(ver);
        }

        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }

    @FXML
    void crearPedido(MouseEvent event) {
        try {
            if (verificarTienda()) {
//                Node node;
//                FXMLLoader loader = new FXMLLoader(MantenimientoPedidosController.this.getClass().getResource(SeleccionarProductosController.viewPath));
//                node = (Node) loader.load();
//                SeleccionarProductosController sel = loader.getController();
//                sel.initModel(pedido, hiddenSp);
//                hiddenSp.getChildren().setAll(node);
            }
        } catch (Exception ex) {
            Logger.getLogger(MantenimientoPedidosController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Boolean verificarTienda() {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Encontrar tienda mas cercana"));
            Node node;
            FXMLLoader loader = new FXMLLoader(MantenimientoPedidosController.this.getClass().getResource(SolicitarDireccionController.viewPath));
            node = (Node) loader.load();
            SolicitarDireccionController dir = loader.getController();
            Tienda tienda = new Tienda();
            dir.initModel(isEdit, tienda, hiddenSp);
            content.setBody(node);
            direccionDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
            direccionDialog.show();
            //return Boolean.TRUE;
        } catch (IOException ex) {
            Logger.getLogger(MantenimientoPedidosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Boolean.FALSE;
    }

    @FXML
    public void handleAction(Event event) {

        if (event.getSource() == moreBtn) {
            int count = tablaPedidos.getSelectionModel().getSelectedCells().size();
            if (count > 1) {
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla", "Ok", hiddenSp);
            } else if (count <= 0) {
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "Ok", hiddenSp);
            } else {
                int selected = tablaPedidos.getSelectionModel().getSelectedIndex();
                pedido = tablaPedidos.getSelectionModel().getModelItem(selected).getValue().pedido;
                initPopup();
                popup.show(moreBtn, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
            }

        }
    }

    public static class PedidoOrdenLista extends RecursiveTreeObject<PedidoOrdenLista> {

        IntegerProperty id;
        StringProperty cliente;
        StringProperty direccion;
        StringProperty fecha;
        StringProperty estado;
        StringProperty tipopago;
        Pedido pedido;

        public PedidoOrdenLista(Pedido pedido) {
            this.pedido = pedido;
            this.id = new SimpleIntegerProperty(pedido.getId().intValue());
            this.cliente = new SimpleStringProperty(pedido.getCliente().toString());
            this.direccion = new SimpleStringProperty(pedido.getDireccionDeEnvio());
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            this.fecha = new SimpleStringProperty(f.format(pedido.getFechaVenta()));
            this.estado = new SimpleStringProperty(pedido.getEstado().getNombre());
            this.tipopago = new SimpleStringProperty(pedido.getTipoPago().getDescripcion());

        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PedidoOrdenLista) {
                PedidoOrdenLista pl = (PedidoOrdenLista) o;
                return pl.pedido.getId().equals(this.pedido.getId());
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 89 * hash + Objects.hashCode(this.pedido.getId());
            return hash;
        }

    }

}