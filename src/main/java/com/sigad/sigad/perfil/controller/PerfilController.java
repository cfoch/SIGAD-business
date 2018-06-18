/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.perfil.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Permiso;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.PerfilHelper;
import com.sigad.sigad.business.helpers.PermisoHelper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class PerfilController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/perfil/view/perfil.fxml";
    
    static ObservableList<Profile> dataPerfilTbl;
    static ObservableList<Permission> dataPermisoTbl;
    @FXML
    private JFXTreeTableView profilesTbl, permissionTbl;
    @FXML
    private JFXButton perfilAddBtn, permisoAddBtn;
    @FXML
    private JFXButton perfilMoreBtn, permisoMoreBtn;
    @FXML
    private JFXPopup popupProfile;
    @FXML
    private JFXPopup popupPermission;
    @FXML
    private StackPane hiddenSp;
    @FXML
    public static JFXDialog profileDialog, permissionDialog;
    
    public static boolean isProfileCreate;
    public static Profile selectedProfile = null;
    
    public static boolean isPermissionCreate;
    public static Permission selectedPermission = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataPerfilTbl = FXCollections.observableArrayList();
        dataPermisoTbl = FXCollections.observableArrayList();
        initProfileTable();
    }
    
    public static void updateProfileData(Perfil p) {
        dataPerfilTbl.add(new Profile(
            new SimpleStringProperty(p.getNombre()), 
            new SimpleStringProperty(p.getDescripcion()), 
            new SimpleStringProperty(p.isActivo()? "Activo": "Inactivo"))
        );
    }
    
    public static void updatePermissionData(Permiso p) {
        dataPermisoTbl.add(new Permission(
                        new SimpleStringProperty(p.getMenu()), 
                        new SimpleStringProperty(p.getIcono()))
        );
    }
    
    @FXML
    private void handleAction(ActionEvent event) {
        if(event.getSource() == perfilAddBtn){
            try {
                CreateEdditProfileDialog(true);
            } catch (IOException ex) {
                Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
            }
        }else if(event.getSource() == perfilMoreBtn){
            int count = profilesTbl.getSelectionModel().getSelectedCells().size();
            if( count > 1){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla Perfiles", "Ok", hiddenSp);
            }else if(count<=0){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla Perfiles", "Ok", hiddenSp);
            }else{
                int selected  = profilesTbl.getSelectionModel().getSelectedIndex();
                selectedProfile = (Profile) profilesTbl.getSelectionModel().getModelItem(selected).getValue();
                profileInitPopup();
                popupProfile.show(perfilMoreBtn, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
            }
        }else if(event.getSource() == permisoAddBtn){
            int count = profilesTbl.getSelectionModel().getSelectedCells().size();
            if( count > 1){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla Perfiles", "Ok", hiddenSp);
            }else if(count<=0){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla Perfiles", "Ok", hiddenSp);
            }else{
                int selected  = profilesTbl.getSelectionModel().getSelectedIndex();
                selectedProfile = (Profile) profilesTbl.getSelectionModel().getModelItem(selected).getValue();
                
                try {
                    CreateEdditPermissionDialog(true);
                } catch (IOException ex) {
                    Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
                }
            }
            
        }else if(event.getSource() == permisoMoreBtn){
            int count = permissionTbl.getSelectionModel().getSelectedCells().size();
            if( count > 1){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla Permisos", "Ok", hiddenSp);
            }else if(count<=0){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla Permisos", "Ok", hiddenSp);
            }else{
                int selected  = permissionTbl.getSelectionModel().getSelectedIndex();
                selectedPermission = (Permission) permissionTbl.getSelectionModel().getModelItem(selected).getValue();
                permissionInitPopup();
                popupPermission.show(permisoMoreBtn, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
            }
        }
        
    }
    
    private void initProfileTable() {
        JFXTreeTableColumn<Profile, String> nombre = new JFXTreeTableColumn<>("Nombre");
        nombre.setPrefWidth(120);
        nombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<Profile, String> param) -> param.getValue().getValue().name);
        
        JFXTreeTableColumn<Profile, String> description = new JFXTreeTableColumn<>("Descripción");
        description.setPrefWidth(120);
        description.setCellValueFactory((TreeTableColumn.CellDataFeatures<Profile, String> param) -> param.getValue().getValue().description);
        
        JFXTreeTableColumn<Profile, String> active = new JFXTreeTableColumn<>("Activo");
        active.setPrefWidth(120);
        active.setCellValueFactory((TreeTableColumn.CellDataFeatures<Profile, String> param) -> param.getValue().getValue().active);
        
        //Get Data from db
        PerfilHelper usuarioHelper = new PerfilHelper();
        ArrayList<Perfil> list = usuarioHelper.getProfiles();
        if(list != null){
            list.forEach(p -> {
                System.out.println(p.getNombre());
                updateProfileData(p);
            });
        }
        
        //click on row
        profilesTbl.setRowFactory(ord -> {
            JFXTreeTableRow<Profile> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked((event) -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                     && event.getClickCount() == 1) {
//                    Profile clickedRow = row.getItem();
                    selectedProfile = row.getItem();
                    System.out.println(selectedProfile.name.getValue());
                    
                    initPermissionTable(selectedProfile.name.getValue());
                }
//                else if(! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
//                     && event.getClickCount() == 2){
//                    Profile clickedRow = row.getItem();
//                    System.out.println(clickedRow.name);
//                    
//                    try {
//                        CreateEdditProfileDialog(false);
//                    } catch (IOException ex) {
//                        Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, "initProfileTable(): Double click" , ex);
//                    }
//                }
            });
            return row;
        });
        
        final TreeItem<Profile> root = new RecursiveTreeItem<>(dataPerfilTbl, RecursiveTreeObject::getChildren);
        
        profilesTbl.setEditable(true);
        profilesTbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        profilesTbl.getColumns().setAll(nombre, description, active);
        profilesTbl.setRoot(root);
        profilesTbl.setShowRoot(false);        
    }
    
    private void profileInitPopup(){
        JFXButton edit = new JFXButton("Editar");
        JFXButton delete = new JFXButton("Activar/Desactivar");
        
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popupProfile.hide();
                try {
                    CreateEdditProfileDialog(false);
                } catch (IOException ex) {
                    Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, "initPopup(): CreateEdditProfileDialog()", ex);
                }
            }
        });
        
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popupProfile.hide();
                deleteProfileDialog();
            }
        });
        
        edit.setPadding(new Insets(20));
        delete.setPadding(new Insets(20));
        
        edit.setPrefSize(145,40);
        delete.setPrefSize(145,40);
        
        VBox vBox = new VBox(edit, delete);
        
        
        popupProfile = new JFXPopup();
        popupProfile.setPopupContent(vBox);
    }
    
    public void CreateEdditProfileDialog(boolean iscreate) throws IOException {
        isProfileCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        if(isProfileCreate){
            content.setHeading(new Text("Crear Perfil"));
        }else{
            content.setHeading(new Text("Editar Perfil"));
            
            PerfilHelper helper = new PerfilHelper();
            Perfil p = helper.getProfile(selectedProfile.name.getValue());
            if(p == null){
                ErrorController error = new ErrorController();
                error.loadDialog("Error", "No se logró obtener el perfil", "Ok", hiddenSp);
                System.out.println("Error al obtener el perfil" + selectedProfile.name.getValue());
                return;
            }
        }
        
        Node node = (Node) FXMLLoader.load(PerfilController.this.getClass().getResource(CrearEditarPerfilController.viewPath));
        content.setBody(node);
        content.setPrefSize(400,160);
                
        profileDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        profileDialog.show();
    }  
    
    private void deleteProfileDialog() {
        PerfilHelper helper = new PerfilHelper();
        Perfil p = helper.getProfile(selectedProfile.name.getValue());
        if(p!= null){
            Set<Usuario> lstUsuarios = p.getUsuarios();
            for (Usuario usuario : lstUsuarios) {
                if(usuario.isActivo()){
                    ErrorController error = new ErrorController();
                    error.loadDialog("Error", "No se puede desactivar este perfil porque cuenta con usuarios activos", "Ok", hiddenSp);
                    return;
                }
            }
            //no active users
            p.setActivo(!selectedProfile.active.getValue().equals("Activo"));
            helper.updateProfile(p);
            helper.close();

            dataPerfilTbl.remove(selectedProfile);
            selectedProfile.active = new SimpleStringProperty(p.isActivo()? "Activo": "Inactivo");
            dataPerfilTbl.add(selectedProfile);
        }else{
            ErrorController error = new ErrorController();
            error.loadDialog("Error", "No se logró desactivar este perfil", "Ok", hiddenSp);
        }
    }
    
    private void permissionInitPopup(){
        JFXButton delete = new JFXButton("Eliminar");
        
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popupPermission.hide();
                deletePermissionDialog();
            }
        });
        
        delete.setPadding(new Insets(20));
        delete.setPrefSize(145,40);
        
        VBox vBox = new VBox(delete);
        
        
        popupPermission = new JFXPopup();
        popupPermission.setPopupContent(vBox);
    }
    
    public void CreateEdditPermissionDialog(boolean iscreate) throws IOException {
        isPermissionCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        if(isPermissionCreate){
            content.setHeading(new Text("Crear Permiso"));
        }else{
            content.setHeading(new Text("Editar Permiso"));
            
            PermisoHelper helper = new PermisoHelper();
            Permiso p = helper.getPermission(selectedPermission.menu.getValue());
            if(p == null){
                ErrorController error = new ErrorController();
                error.loadDialog("Error", "No se logró obtener el permiso", "Ok", hiddenSp);
                System.out.println("Error al obtener el permiso" + selectedProfile.name.getValue());
                return;
            }
        }
        
        Node node = (Node) FXMLLoader.load(PerfilController.this.getClass().getResource(CrearEditarPermisoController.viewPath));
        content.setBody(node);
        content.setPrefSize(400,160);
                
        permissionDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        permissionDialog.show();
    }  
    
    private void deletePermissionDialog() {
        PerfilHelper helper = new PerfilHelper();
        System.out.println(selectedProfile.name.getValue());
        System.out.println(selectedPermission.menu.getValue());
        Perfil perfilTemp = helper.getProfile(selectedProfile.name.getValue());
        if(perfilTemp!= null){
            Set<Permiso> permisoList = perfilTemp.getPermisos();
            Permiso permisoToRemove = null;
            for (Permiso permiso : permisoList) {
                if(permiso.getMenu().equals(selectedPermission.menu.getValue())){
                    permisoToRemove = permiso;
                }
            }
            //permission found
            if(permisoToRemove!= null){
                perfilTemp.getPermisos().remove(permisoToRemove);
            }
            helper.updateProfile(perfilTemp);
            helper.close();
            
            dataPermisoTbl.remove(selectedPermission);
        }
        
//        JFXDialogLayout content =  new JFXDialogLayout();
//        content.setHeading(new Text("Error"));
//        content.setBody(new Text("Cuenta o contraseña incorrectas"));
//                
//        JFXDialog dialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
//        JFXButton button = new JFXButton("Okay");
//        button.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                dialog.close();
//            }
//        });
//        content.setActions(button);
//        dialog.show();
    }

    private void initPermissionTable(String profileName) {
        dataPermisoTbl.clear();
        
        JFXTreeTableColumn<Permission, String> nombre = new JFXTreeTableColumn<>("Menú");
        nombre.setPrefWidth(120);
        nombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<Permission, String> param) -> param.getValue().getValue().menu);
        
        JFXTreeTableColumn<Permission, String> description = new JFXTreeTableColumn<>("Ícono");
        description.setPrefWidth(120);
        description.setCellValueFactory((TreeTableColumn.CellDataFeatures<Permission, String> param) -> param.getValue().getValue().icono);
        
        PerfilHelper ph = new PerfilHelper();
        Perfil ptemp = ph.getProfile(profileName);
        if(ptemp == null){
            ErrorController error = new ErrorController();
            error.loadDialog("Error", "No se logró obtener el perfil seleccionado", "Ok", hiddenSp);
            return;
        }
        Set<Permiso> currPermissions= ptemp.getPermisos();
        for (Permiso currPermission : currPermissions) {
            updatePermissionData(currPermission);
        }
        
//        PermisoHelper helper = new PermisoHelper();
//        ArrayList<Permiso> list = helper.getPermissions();
//        if(list != null){
//            list.forEach(p -> {
//                currPermissions.forEach((curr)->{
//                   if(p.getMenu().equals(curr.getMenu())){
//                       updatePermissionData(p);
//                   } 
//                });
//            });
//        }
        
        final TreeItem<Permission> root = new RecursiveTreeItem<>(dataPermisoTbl, RecursiveTreeObject::getChildren);
        
        permissionTbl.setEditable(true);
        permissionTbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        permissionTbl.getColumns().setAll(nombre, description);
        permissionTbl.setRoot(root);
        permissionTbl.setShowRoot(false);
    }

    /*Classes extra*/
    public static class Profile extends RecursiveTreeObject<Profile> {

        StringProperty name;
        StringProperty description;
        StringProperty active;

        public Profile(StringProperty profileName, StringProperty profileDesc, StringProperty seleccion) {
            this.name = profileName;
            this.description = profileDesc;
            this.active = seleccion;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Profile) {
                Profile u = (Profile) o;
                return u.name.equals(this.name);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

    }
    
    public static class Permission extends RecursiveTreeObject<Permission> {        
        
        StringProperty menu;
        StringProperty icono;

        public Permission(StringProperty option, StringProperty icono) {
            this.menu = option;
            this.icono = icono;
        }
        

        @Override
        public boolean equals(Object o) {
            if (o instanceof Profile) {
                Permission u = (Permission) o;
                return u.menu.equals(menu);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
