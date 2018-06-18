/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.perfil.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTextField;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Permiso;
import com.sigad.sigad.business.helpers.PerfilHelper;
import com.sigad.sigad.business.helpers.PermisoHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class CrearEditarPermisoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/perfil/view/crearEditarPermiso.fxml";
    private static Permiso permiso = null;
    private static Perfil perfil  = null;
    @FXML
    private AnchorPane containerPane;
    @FXML
    private StackPane hiddenSp;
    private JFXTextField iconNameTxt;
    private JFXListView<Label> permissionListView;
    @FXML
    private Label profileName;
    @FXML
    private JFXScrollPane permissionPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Add the buttons of the static dialog
        addDialogBtns();
        
        //validate edit or create menu in order to set user
        if(!PerfilController.isPermissionCreate){
            System.out.println(PerfilController.selectedPermission.menu);
            loadFields();
        }else{
            permiso = new Permiso();
            loadFields();
        }
        
        //Inmediate validations
        //initValidator();
        
        //initProfileTable();
        initPermissionPicker();
    }    
    
    private void addDialogBtns() {
        JFXButton save = new JFXButton("Guardar");
        save.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(save, -20.0);
        AnchorPane.setRightAnchor(save, 0.0);
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                if(permissionListView.getItems().size()==0){
                    PerfilController.permissionDialog.close();
                }
                
                int indexPermission = getSelectedIndex(permissionListView, "Permisos");
                if(indexPermission<0){
                    ErrorController error = new ErrorController();
                    error.loadDialog("Error", "Debe seleccionar al menos un registro de la tabla Permisos", "Ok", hiddenSp);
                    return;
                }
                
                PermisoHelper helper = new PermisoHelper();
                Permiso p = helper.getPermission(permissionListView.getItems().get(indexPermission).getText());
                
                PerfilHelper perfilHelper = new PerfilHelper();
                Perfil temp = perfilHelper.getProfile(perfil.getNombre());
                if(p != null){
                    Set<Permiso> permisos = temp.getPermisos();
                    permisos.add(p);
                    perfil.setPermisos(permisos);
                    
                    boolean ok = perfilHelper.updateProfile(perfil);
                    
                    if(ok){
                        PerfilController.updatePermissionData(p);
                        PerfilController.permissionDialog.close();
                    }else{
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", perfilHelper.getErrorMessage(), "Ok", hiddenSp);
                    }
                    
                }else{
                    ErrorController error = new ErrorController();
                    error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
                }

            }
        });
        
        JFXButton cancel = new JFXButton("Cancelar");
        cancel.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(cancel, -20.0);
        AnchorPane.setRightAnchor(cancel, 85.0);
        cancel.setOnAction((ActionEvent event) -> {
            PerfilController.permissionDialog.close();
        });
        
        containerPane.getChildren().add(save);
        containerPane.getChildren().add(cancel);
    }
    
    private int getSelectedIndex(JFXListView<Label> listView, String tableName) {
        int selected = -1;
        int count = listView.getSelectionModel().getSelectedItems().size();
        if( count > 1){
            ErrorController error = new ErrorController();
            error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla" + tableName, "Ok", hiddenSp);
        }else if(count<=0){
            ErrorController error = new ErrorController();
            error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla" + tableName, "Ok", hiddenSp);
        }else{
            selected  = listView.getSelectionModel().getSelectedIndex();
        }
        return selected;
    }
    
    private void loadFields() {
        PerfilHelper helper = new PerfilHelper();
        Perfil temp = helper.getProfile(PerfilController.selectedProfile.name.getValue());
        
        if(temp!= null){
            perfil = new Perfil();
            perfil.setId(temp.getId());
            perfil.setNombre(temp.getNombre());
            perfil.setDescripcion(temp.getDescripcion());
            perfil.setPermisos(temp.getPermisos());
            perfil.setActivo(temp.isActivo());
            
            profileName.setText("Selecciona un permiso para " + perfil.getNombre());
        }
        helper.close();
    }

    private void initPermissionPicker() {
        PerfilHelper ph = new PerfilHelper();
        Perfil ptemp = ph.getProfile(PerfilController.selectedProfile.name.getValue());
        if(ptemp == null){
            ErrorController error = new ErrorController();
            error.loadDialog("Error", "No se pudo obtener el perfil seleccionado", "Ok", hiddenSp);
            return;
        }
        permissionListView = new JFXListView<>();
        
        Set<Permiso> currPermissions= ptemp.getPermisos();
        ArrayList<Permiso> permisoActualList = new ArrayList<Permiso>();
        currPermissions.forEach((currPermission) -> {
            permisoActualList.add(new Permiso(currPermission.getOrden(), currPermission.getMenu(), currPermission.getIcono()));
        });
        
        //load profiles
        PermisoHelper helper = new PermisoHelper();
        ArrayList<Permiso> permisoList = helper.getPermissions();
        
        if(permisoList != null){
            if(permisoActualList.isEmpty()){
                permisoList.forEach((p)-> {
                    Label lbl = new Label(p.getMenu());
                    lbl.setPrefSize(200, 30);
                    permissionListView.getItems().add(lbl);
                });
            }else{
                permisoList.forEach((p)-> {
                    if(!ExistInSet(p, permisoActualList)){
                        Label lbl = new Label(p.getMenu());
                        lbl.setPrefSize(200, 30);
                        permissionListView.getItems().add(lbl);
                    }
                });
            }            
        }
        
        permissionListView.getStyleClass().add("mylistview");
        permissionListView.setStyle("-fx-background-color:WHITE");
        
        permissionPane.getChildren().add(permissionListView);
    }

    private boolean ExistInSet(Permiso p, ArrayList<Permiso> currPermissions) {
        for (Permiso curr : currPermissions) {
            if(curr.getMenu() == null ? p.getMenu() == null : curr.getMenu().equals(p.getMenu())){
                return true;
            }
        }
        return false;
    }
}
