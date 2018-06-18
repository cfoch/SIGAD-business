/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.*;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.ClienteDireccion;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.PerfilHelper;
import com.sigad.sigad.business.helpers.UsuarioHelper;
import static com.sigad.sigad.pedido.controller.SeleccionarClienteController.clientes;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */
public class RegistrarClienteController implements Initializable {

    public static final String viewPath = "/com/sigad/sigad/pedido/view/registrarCliente.fxml";
    private StackPane stackPane;
    @FXML
    private JFXTreeTableView<DireccionesLista> tablaDirecciones;
    private final ObservableList<DireccionesLista> direcciones = FXCollections.observableArrayList();
    @FXML
    JFXTreeTableColumn<DireccionesLista, String> direccion = new JFXTreeTableColumn<>("Direccion");
    @FXML
    JFXTreeTableColumn<DireccionesLista, String> nombre = new JFXTreeTableColumn<>("Nombre");

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtApp;

    @FXML
    private StackPane hiddenSp;

    @FXML
    private JFXTextField txtApm;

    @FXML
    private JFXTextField txtdni;

    @FXML
    private JFXTextField txttel;

    @FXML
    private JFXTextField txtcel;

    @FXML
    private JFXTextField txtcorreo;
    @FXML
    private JFXTextField txtnuevaDir;

    @FXML
    private JFXTextField txtnuevoNom;

    @FXML
    private JFXButton btnRegistrar;

    @FXML
    private JFXButton btnAgregarDireccion;
    @FXML
    private Usuario cliente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        agregarColumnasDireccion();
        agregarColumnasTablasClientes();
        initValidator();
    }

    public void cargarCliente(Integer codigo) {
        UsuarioHelper cli = new UsuarioHelper();
        cliente = cli.getUser(codigo);
        cli.close();
        txtNombre.setText(cliente.getNombres());
        txtApp.setText(cliente.getApellidoPaterno());
        txtApm.setText(cliente.getApellidoMaterno());
        txttel.setText(cliente.getTelefono());
        txtdni.setText(cliente.getDni());
        txtcel.setText(cliente.getCelular());
        txtcorreo.setText(cliente.getCorreo());
        cliente.getClienteDirecciones().forEach((t) -> {
            direcciones.add(new DireccionesLista(t.getDireccionCliente(), t.getNombreDireccion(), t.getId().intValue()));
        });

    }

    private void initValidator() {
        RequiredFieldValidator r;
        NumberValidator n;

        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        txtNombre.getValidators().add(r);
        txtNombre.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txtNombre.validate()) {
                    txtNombre.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                } else {
                    txtNombre.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
                }
            }
        });

        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        txtApp.getValidators().add(r);
        txtApp.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txtApp.validate()) {
                    txtApp.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                } else {
                    txtApp.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
                }
            }
        });

        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        txtApm.getValidators().add(r);
        txtApm.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txtApm.validate()) {
                    txtApm.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                } else {
                    txtApm.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
                }
            }
        });

        /*DNI*/
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        txtdni.getValidators().add(r);

        n = new NumberValidator();
        n.setMessage("Campo numérico");
        n.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        txtdni.getValidators().add(n);

        txtdni.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txtdni.validate()) {
                    txtdni.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                } else {
                    txtdni.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
                }
            }
        });

        /**/
 /*TELEPHONE*/
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        txttel.getValidators().add(r);

        n = new NumberValidator();
        n.setMessage("Campo numérico");
        n.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        txttel.getValidators().add(n);

        txttel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txttel.validate()) {
                    txttel.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                } else {
                    txttel.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
                }
            }
        });

        /**/
 /*CELLPHONE*/
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        txtcel.getValidators().add(r);

        n = new NumberValidator();
        n.setMessage("Campo numérico");
        n.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        txtcel.getValidators().add(n);

        txtcel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txtcel.validate()) {
                    txtcel.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                } else {
                    txtcel.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
                }
            }
        });

        /**/
 /*EMAIL*/
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        txtcorreo.getValidators().add(r);
        txtcorreo.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if (!txtcorreo.validate()) {
                    txtcorreo.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                } else {
                    txtcorreo.setFocusColor(new Color(0.30, 0.47, 0.23, 1));
                }
            }
        });
        /**/
        
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Agrega una direccion");
        txtnuevaDir.getValidators().add(r);
        
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Agrega una direccion");
        txtnuevoNom.getValidators().add(r);

    }

    public boolean validateFields() {
        if (!txtNombre.validate()) {
            txtNombre.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtNombre.requestFocus();
            return false;
        } else if (!txtApp.validate()) {
            txtApp.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtApp.requestFocus();
            return false;
        } else if (!txtApm.validate()) {
            txtApm.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtApm.requestFocus();
            return false;
        } else if (!txtdni.validate()) {
            txtdni.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtdni.requestFocus();
            return false;
        } else if (txtdni.getText().length() < 8) {
            ErrorController r = new ErrorController();
            r.loadDialog("Error", "Debe el dni debe tener 8 dígitos", "Ok", hiddenSp);
            txtdni.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtdni.requestFocus();
            return false;
        } else if (!txttel.validate()) {
            txttel.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txttel.requestFocus();
            return false;
        } else if (!txtcel.validate()) {
            txtcel.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtcel.requestFocus();
            return false;
        } else if (txtcel.getText().length() < 9) {
            ErrorController r = new ErrorController();
            r.loadDialog("Error", "El celular debe tener 9 dígitos", "Ok", hiddenSp);
            txtcel.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtcel.requestFocus();
            return false;
        } else if (!txtcorreo.validate()) {
            txtcorreo.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtcorreo.requestFocus();
            return false;
        } else if (!txtcorreo.getText().contains("@")) {
            ErrorController r = new ErrorController();
            r.loadDialog("Error", "El correo no es válido", "Ok", hiddenSp);
            txtcorreo.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtcorreo.requestFocus();
            return false;
        } else if (direcciones.size() <= 0) {
            ErrorController r = new ErrorController();
            r.loadDialog("Error", "Ingrese al menos una dirección", "Ok", hiddenSp);
            txtnuevaDir.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtnuevaDir.requestFocus();
            txtnuevoNom.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            txtnuevoNom.requestFocus();
            return false;
        }
        return true;
    }

    public void agregarColumnasDireccion() {
        nombre.setPrefWidth(90);
        nombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<DireccionesLista, String> param) -> param.getValue().getValue().getNombre());
        direccion.setPrefWidth(180);
        direccion.setCellValueFactory((TreeTableColumn.CellDataFeatures<DireccionesLista, String> param) -> param.getValue().getValue().getDireccion());
    }

    public void crearUsuario() {
        PerfilHelper perfilHeler = new PerfilHelper();
        Perfil perfil = perfilHeler.getProfile("Cliente");
        perfilHeler.close();
        if (!validateFields()) {
            ErrorController err = new ErrorController();
            err.loadDialog("Alerta", "Complete los campos", "Ok", stackPane);
        } else {
            if (perfil != null) {
                cliente = new Usuario(txtNombre.getText(), txtApp.getText(), txtApm.getText(),
                        perfil, txttel.getText(), txtdni.getText(), txtcel.getText(), true, txtcorreo.getText(), txtdni.getText(), "");
                direcciones.forEach((t) -> {
                    System.out.println(t.getDireccion().getValue());
                    cliente.addClienteDirecciones(new ClienteDireccion(t.getDireccion().getValue(), t.getNombre().getValue(), Boolean.FALSE, cliente));

                });
            }
        }
    }

    public Boolean isValido() {
        return !txtNombre.getText().equals("") && !txtApp.getText().equals("") && !txtApm.getText().equals("") && !txttel.getText().equals("")
                && !txtdni.getText().equals("") && !txtcel.getText().equals("") && !txtcorreo.getText().equals("") && !txtdni.getText().equals("") && direcciones.size() > 0;
    }

    public void actualizarUsuario() {
        cliente.setNombres(txtNombre.getText());
        cliente.setApellidoPaterno(txtApp.getText());
        cliente.setApellidoMaterno(txtApm.getText());
        cliente.setTelefono(txttel.getText());
        cliente.setDni(txtdni.getText());
        cliente.setCelular(txtcel.getText());
        cliente.setCorreo(txtcorreo.getText());
        cliente.getClienteDireccionesSet().clear();
        cliente.cleanClienteDirecciones();
        direcciones.forEach((t) -> {
            cliente.addClienteDirecciones(new ClienteDireccion(t.direccion.getValue(), t.nombre.getValue(), Boolean.FALSE, cliente));
        });

    }

    public void agregarColumnasTablasClientes() {
        final TreeItem<DireccionesLista> rootPedido = new RecursiveTreeItem<>(direcciones, RecursiveTreeObject::getChildren);
        tablaDirecciones.setEditable(true);
        tablaDirecciones.getColumns().setAll(direccion, nombre);
        tablaDirecciones.setRoot(rootPedido);
        tablaDirecciones.setShowRoot(false);
    }

    @FXML
    void guardarUsuario(ActionEvent event) {
        if (!validateFields()) {
            return;
        }
        if (SeleccionarClienteController.isClientCreate) {
            crearUsuario();
            UsuarioHelper usuariohelper = new UsuarioHelper();
            usuariohelper.saveUser(cliente);
            usuariohelper.close();
            SeleccionarClienteController.userDialog.close();
            actualizarTabla();
        } else {
            actualizarUsuario();
            UsuarioHelper usuariohelper = new UsuarioHelper();
            usuariohelper.updateUser(cliente);
            usuariohelper.close();
            SeleccionarClienteController.userDialog.close();
            actualizarTabla();
        }

    }

    void actualizarTabla() {
        clientes.clear();
        PerfilHelper perfilHelper = new PerfilHelper();
        Perfil perfil = perfilHelper.getProfile("Cliente");
        perfilHelper.close();
        UsuarioHelper usuarioHelper = new UsuarioHelper();
        ArrayList<Usuario> usuarios = usuarioHelper.getUsers(perfil);
        usuarioHelper.close();
        if (usuarios != null) {
            usuarios.forEach((t) -> {
                clientes.add(new SeleccionarClienteController.ClientesLista(t.getNombres() + " " + t.getApellidoPaterno() + " " + t.getApellidoMaterno(), t.getDni(), t.getTelefono(), t.getCelular(), t.getId().intValue()));
            });
        }
    }

    public void initModel(StackPane stackPane) {

        this.stackPane = stackPane;
    }

    @FXML
    void agregarDireccion(ActionEvent event) {
        if (txtnuevaDir.getText().length() > 0 && txtnuevoNom.getText().length() > 0) {
            direcciones.add(new DireccionesLista(txtnuevaDir.getText(), txtnuevoNom.getText(), null));
            txtnuevaDir.clear();
            txtnuevoNom.clear();
        } else {
            ErrorController error = new ErrorController();
            error.loadDialog("Atención", "Los campos se encuentran vacios", "Ok", stackPane);
        }
    }

    @FXML
    void close(ActionEvent event) {
        SeleccionarClienteController.userDialog.close();
    }

    public class DireccionesLista extends RecursiveTreeObject<DireccionesLista> {

        private StringProperty direccion;
        private StringProperty nombre;
        private Integer codigo;

        public DireccionesLista(String direccion, String nombre, Integer codigo) {
            this.nombre = new SimpleStringProperty(nombre);
            this.direccion = new SimpleStringProperty(direccion);
            this.codigo = codigo;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DireccionesLista) {
                DireccionesLista cl = (DireccionesLista) o;
                return cl.getDireccion().getValue().trim().equals(this.getDireccion().getValue().trim());
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 71 * hash + Objects.hashCode(this.getCodigo());
            return hash;
        }

        /**
         * @return the direccion
         */
        public StringProperty getDireccion() {
            return direccion;
        }

        /**
         * @param direccion the direccion to set
         */
        public void setDireccion(StringProperty direccion) {
            this.direccion = direccion;
        }

        /**
         * @return the nombre
         */
        public StringProperty getNombre() {
            return nombre;
        }

        /**
         * @param nombre the nombre to set
         */
        public void setNombre(StringProperty nombre) {
            this.nombre = nombre;
        }

        /**
         * @return the codigo
         */
        public Integer getCodigo() {
            return codigo;
        }

        /**
         * @param codigo the codigo to set
         */
        public void setCodigo(Integer codigo) {
            this.codigo = codigo;
        }

    }

}
