/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.personal.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.pedido.controller.SeleccionarProductosController;
import com.sigad.sigad.usuarios.helper.UsuariosHelper;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class PersonalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/personal/view/personal.fxml";
    public static String windowName = "Cuentas";
    
    @FXML
    private JFXTreeTableView userTbl;

    ObservableList<User> data = FXCollections.observableArrayList();
    
    @FXML
    private JFXButton addBtn;
    @FXML
    private JFXButton moreBtn;
    @FXML
    private JFXPopup popup;
    @FXML
    private StackPane hiddenSp;
    @FXML
    public static JFXDialog dialog;
    
    public static boolean isCreate;
    public static User selectedUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXTreeTableColumn name = new JFXTreeTableColumn("Nombre");
        name.setPrefWidth(50);
        name.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<PersonalController.User, String> p) {
                return p.getValue().getValue().nombres;
            }
        });
        
        JFXTreeTableColumn apellidoMaterno = new JFXTreeTableColumn("Appelido Materno");
        apellidoMaterno.setPrefWidth(50);
        apellidoMaterno.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PersonalController.User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<PersonalController.User, String> p) {
                return p.getValue().getValue().apellidoMaterno;
            }
        });
        
        JFXTreeTableColumn apellidoPaterno = new JFXTreeTableColumn("Appelido Paterno");
        apellidoPaterno.setPrefWidth(50);
        apellidoPaterno.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PersonalController.User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<PersonalController.User, String> p) {
                return p.getValue().getValue().apellidoPaterno;
            }
        });
        
        JFXTreeTableColumn dni = new JFXTreeTableColumn("DNI");
        dni.setPrefWidth(50);
        dni.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PersonalController.User, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<PersonalController.User, String> p) {
                return p.getValue().getValue().dni;
            }
        });
        
        JFXTreeTableColumn telephone = new JFXTreeTableColumn("Teléfono");
        telephone.setPrefWidth(50);
        telephone.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PersonalController.User, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<PersonalController.User, String> p) {
                return p.getValue().getValue().telefono;
            }
        });
        
        JFXTreeTableColumn cellphone = new JFXTreeTableColumn("Celular");
        cellphone.setPrefWidth(50);
        cellphone.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PersonalController.User, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<PersonalController.User, String> p) {
                return p.getValue().getValue().celular;
            }
        });
        
        JFXTreeTableColumn profile = new JFXTreeTableColumn("Perfil");
        profile.setPrefWidth(50);
        profile.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PersonalController.User, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<PersonalController.User, String> p) {
                return p.getValue().getValue().profileName;
            }
        });
        
        JFXTreeTableColumn profileDesc = new JFXTreeTableColumn("Descripción");
        profileDesc.setPrefWidth(50);
        profileDesc.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PersonalController.User, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<PersonalController.User, String> p) {
                return p.getValue().getValue().profileDesc;
            }
        });
        
        userTbl.getColumns().add(name);
        userTbl.getColumns().add(apellidoMaterno);
        userTbl.getColumns().add(apellidoPaterno);
        userTbl.getColumns().add(dni);
        userTbl.getColumns().add(telephone);
        userTbl.getColumns().add(cellphone);
        userTbl.getColumns().add(profile);
        userTbl.getColumns().add(profileDesc);
        
        //DB
        getDataFromDB();
        
        final TreeItem<PersonalController.User> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        
        userTbl.setEditable(true);
        userTbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);        
        userTbl.getColumns().setAll(name,apellidoMaterno,apellidoPaterno, dni, telephone, cellphone, profile, profileDesc);
        userTbl.setRoot(root);
        userTbl.setShowRoot(false);
        
    }    

    private void getDataFromDB() {
        UsuariosHelper userHelper = new UsuariosHelper();
        
        ArrayList<?> lista = userHelper.getUsers(-1);
        lista.forEach((p) -> {
            Usuario u = (Usuario) p;
            //System.out.println(u.getNombres() + u.getApellidoMaterno() + u.getApellidoPaterno());
            data.add(
                new User(
                        new SimpleStringProperty(u.getNombres()),
                        new SimpleStringProperty(u.getApellidoMaterno()),
                        new SimpleStringProperty(u.getApellidoPaterno()),
                        new SimpleStringProperty(u.getCorreo()),
                        new SimpleStringProperty(u.getDni()),
                        new SimpleStringProperty(u.getTelefono()),
                        new SimpleStringProperty(u.getCelular()),
                        new SimpleStringProperty(u.getPerfil().getNombre()),
                        new SimpleStringProperty(u.getPerfil().getDescripcion()),
                        true
                ));
        });
        userHelper.close();
    }

    @FXML
    private void handleAction(ActionEvent event) {
        if(event.getSource() == addBtn){
            try {
                CreateEdditUserDialog(true);
            } catch (IOException ex) {
                Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
            }
        }else if(event.getSource() == moreBtn){
            int count = userTbl.getSelectionModel().getSelectedCells().size();
            if( count > 1){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla", "Ok", hiddenSp);
            }else if(count<=0){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "Ok", hiddenSp);
            }else{
                int selected  = userTbl.getSelectionModel().getSelectedIndex();
                selectedUser = (User) userTbl.getSelectionModel().getModelItem(selected).getValue();
                initPopup();
                popup.show(moreBtn, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
            }
            
        }
    }
    
    private void initPopup(){
        JFXButton edit = new JFXButton("Editar");
        JFXButton delete = new JFXButton("Eliminar");
        
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                try {
                    CreateEdditUserDialog(false);
                } catch (IOException ex) {
                    Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "initPopup(): CreateEdditUserDialog()", ex);
                }
            }
        });
        
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                deleteUserDialog();
            }
        });
        
        edit.setPadding(new Insets(20));
        delete.setPadding(new Insets(20));
        
        edit.setPrefHeight(40);
        delete.setPrefHeight(40);
        
        edit.setPrefWidth(145);
        delete.setPrefWidth(145);
        
        VBox vBox = new VBox(edit, delete);
        
        
        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }
    
    public void CreateEdditUserDialog(boolean iscreate) throws IOException {
        isCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        if(isCreate){
            content.setHeading(new Text("Crear Usuario"));
        }else{
            content.setHeading(new Text("Editar Usuario"));
            
            UsuariosHelper helper = new UsuariosHelper();
            if(!helper.existUserEmail(PersonalController.selectedUser.correo.getValue())){
                ErrorController error = new ErrorController();
                error.loadDialog("Error", "No se pudo obtener el usuario", "Ok", hiddenSp);
                System.out.println("Error al obtener el usuario");
                return;
            }
        }
        
        Node node;
        node = (Node) FXMLLoader.load(PersonalController.this.getClass().getResource(EditarForm.viewPath));
        content.setBody(node);
        content.setPrefSize(400,400);
                
        dialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        dialog.show();
    }  
    
    private void deleteUserDialog() {
        JFXDialogLayout content =  new JFXDialogLayout();
        content.setHeading(new Text("Error"));
        content.setBody(new Text("Cuenta o contraseña incorrectas"));
                
        JFXDialog dialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        content.setActions(button);
        dialog.show();
    }
    
    /*Class for table*/
    public class User extends RecursiveTreeObject<User> {

        StringProperty nombres;
        StringProperty apellidoMaterno;
        StringProperty apellidoPaterno;
        StringProperty correo;
        StringProperty dni;
        StringProperty telefono;
        StringProperty celular;
        StringProperty profileName;
        StringProperty profileDesc;
        boolean activo;

        public User(StringProperty nombres, StringProperty apellidoMaterno, StringProperty apellidoPaterno, StringProperty correo, StringProperty dni, StringProperty telefono, StringProperty celular, StringProperty profileName, StringProperty profileDesc, boolean activo) {
            this.nombres = nombres;
            this.apellidoMaterno = apellidoMaterno;
            this.apellidoPaterno = apellidoPaterno;
            this.correo = correo;
            this.dni = dni;
            this.telefono = telefono;
            this.celular = celular;
            this.profileName = profileName;
            this.profileDesc = profileDesc;
            this.activo = activo;
        }

        

        

        @Override
        public boolean equals(Object o) {
            if (o instanceof User) {
                User u = (User) o;
                return u.dni.equals(dni);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.dni);
            return hash;
        }

    }
}
