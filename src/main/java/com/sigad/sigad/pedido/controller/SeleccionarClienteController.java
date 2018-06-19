/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.ClienteDireccion;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.PerfilHelper;
import com.sigad.sigad.business.helpers.ProductoHelper;
import com.sigad.sigad.business.helpers.UsuarioHelper;
import static com.sigad.sigad.pedido.controller.SeleccionarProductosController.userDialog;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */
public class SeleccionarClienteController implements Initializable {

    public static final String viewPath = "/com/sigad/sigad/pedido/view/SeleccionarCliente.fxml";
    Pedido pedido;
    private StackPane stackPane;
    public static Boolean isClientCreate;
    @FXML
    private JFXTreeTableView<ClientesLista> tablaClientes;

    @FXML
    public static JFXDialog userDialog;
    public static final ObservableList<ClientesLista> clientes = FXCollections.observableArrayList();
    @FXML
    JFXTreeTableColumn<ClientesLista, String> nombre = new JFXTreeTableColumn<>("Nombre");
    @FXML
    JFXTreeTableColumn<ClientesLista, String> dni = new JFXTreeTableColumn<>("DNI");
    @FXML
    JFXTreeTableColumn<ClientesLista, String> telefono = new JFXTreeTableColumn<>("Telefono");
    @FXML
    JFXTreeTableColumn<ClientesLista, String> celular = new JFXTreeTableColumn<>("Celular");

    @FXML
    private JFXTextField txtBuscar;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXButton btnRegistrar;
    @FXML
    private JFXButton btnSiguiente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        agregarColumnasClientes();
        agregarColumnasTablasClientes();
        agregarFiltro();
    }

    public void initModel(Pedido pedido, StackPane stackpane) {
        this.stackPane = stackpane;
        this.pedido = pedido;
        llenarTabla();
        isClientCreate = Boolean.FALSE;

    }

    public void llenarTabla() {
        clientes.clear();
        PerfilHelper perfilHelper = new PerfilHelper();
        Perfil perfil = perfilHelper.getProfile("Cliente");
        perfilHelper.close();
        UsuarioHelper usuarioHelper = new UsuarioHelper();
        ArrayList<Usuario> usuarios = usuarioHelper.getUsers(perfil);
        usuarioHelper.close();
        if (usuarios != null) {
            usuarios.forEach((t) -> {
                clientes.add(new ClientesLista(t.getNombres() + " " + t.getApellidoPaterno() + " " + t.getApellidoMaterno(), t.getDni(), t.getTelefono(), t.getCelular(), t.getId().intValue()));
            });
        }

    }

    public void agregarColumnasClientes() {
        nombre.setPrefWidth(200);
        nombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<ClientesLista, String> param) -> param.getValue().getValue().nombre);
        dni.setPrefWidth(120);
        dni.setCellValueFactory((TreeTableColumn.CellDataFeatures<ClientesLista, String> param) -> param.getValue().getValue().dni);
        telefono.setPrefWidth(120);
        telefono.setCellValueFactory((TreeTableColumn.CellDataFeatures<ClientesLista, String> param) -> param.getValue().getValue().telefono);
        celular.setPrefWidth(120);
        celular.setCellValueFactory((TreeTableColumn.CellDataFeatures<ClientesLista, String> param) -> param.getValue().getValue().celular);
    }

    public void agregarColumnasTablasClientes() {
        final TreeItem<ClientesLista> rootPedido = new RecursiveTreeItem<>(clientes, RecursiveTreeObject::getChildren);
        tablaClientes.setEditable(true);
        tablaClientes.getColumns().setAll(nombre, dni, telefono, celular);
        tablaClientes.setRoot(rootPedido);
        tablaClientes.setShowRoot(false);
        tablaClientes.setRowFactory(new Callback<TreeTableView<ClientesLista>, TreeTableRow<ClientesLista>>() {
            @Override
            public TreeTableRow<ClientesLista> call(TreeTableView<ClientesLista> param) {
                TreeTableRow<ClientesLista> row = new TreeTableRow<>();
                row.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        ClientesLista rowData = row.getItem();
                        editarInfoCliente(rowData.codigo);

                    }
                });
                return row; //To change body of generated lambdas, choose Tools | Templates.
            }
        });
    }

    public void editarInfoCliente(Integer codigo) {
        try {
            isClientCreate = Boolean.FALSE;
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Editar cliente"));
            Node node;
            FXMLLoader loader = new FXMLLoader(SeleccionarClienteController.this.getClass().getResource(RegistrarClienteController.viewPath));
            node = (Node) loader.load();
            RegistrarClienteController ctrl = loader.getController();
            ctrl.cargarCliente(codigo);
            content.setBody(node);
            userDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
            userDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(DescripcionProductosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarFiltro() {
        txtBuscar.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tablaClientes.setPredicate((TreeItem<ClientesLista> t) -> {
                Boolean flag = t.getValue().nombre.getValue().contains(newValue) || t.getValue().dni.getValue().contains(newValue);
                return flag;
            });
        });
    }

    @FXML
    void backPage(MouseEvent event){
        
    
    }
    
    @FXML
    void registrarCliente(MouseEvent event) {
        try {
            isClientCreate = Boolean.TRUE;
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Registrar cliente"));
            Node node;
            FXMLLoader loader = new FXMLLoader(SeleccionarClienteController.this.getClass().getResource(RegistrarClienteController.viewPath));
            node = (Node) loader.load();
            RegistrarClienteController ctrl = new RegistrarClienteController();
            ctrl.initModel(stackPane);
            content.setBody(node);
            userDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
            userDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(DescripcionProductosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goDatosPedido(MouseEvent event) {
        try {
            int count = tablaClientes.getSelectionModel().getSelectedCells().size();

            if (count == 1) {
                Integer idCliente = tablaClientes.getSelectionModel().getSelectedCells().get(0).getTreeItem().getValue().codigo;
                UsuarioHelper helper = new UsuarioHelper();
                Usuario cliente = helper.getUser(idCliente);
                helper.close();
                pedido.setCliente(cliente);
                Node node;
                FXMLLoader loader = new FXMLLoader(SeleccionarClienteController.this.getClass().getResource(DatosPedidoController.viewPath));
                node = (Node) loader.load();
                DatosPedidoController desc = loader.getController();
                desc.initModel(pedido, stackPane);
                stackPane.getChildren().setAll(node);
            } else {
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar un registro de la tabla", "Ok", stackPane);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(DescripcionProductosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class ClientesLista extends RecursiveTreeObject<ClientesLista> {

        StringProperty nombre;
        StringProperty dni;
        StringProperty telefono;
        StringProperty celular;
        Integer codigo;

        public ClientesLista(String nombre, String dni, String telefono, String celular, Integer codigo) {
            this.nombre = new SimpleStringProperty(nombre);
            this.dni = new SimpleStringProperty(dni);
            this.telefono = new SimpleStringProperty(telefono);
            this.celular = new SimpleStringProperty(celular);
            this.codigo = codigo;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ClientesLista) {
                ClientesLista cl = (ClientesLista) o;
                return cl.dni.getValue().trim().equals(this.dni.getValue().trim());
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(this.dni);
            return hash;
        }

    }

}
