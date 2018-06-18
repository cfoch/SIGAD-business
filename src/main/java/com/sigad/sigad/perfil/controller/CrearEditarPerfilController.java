/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.perfil.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.helpers.PerfilHelper;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
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
public class CrearEditarPerfilController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/perfil/view/crearEditarPerfil.fxml";
    public static Perfil perfil = null;
    @FXML
    private JFXTextField nameTxt;
    @FXML
    private JFXTextArea descriptionTXt;
    @FXML
    private AnchorPane containerPane;
    @FXML
    private StackPane hiddenSp;
    @FXML
    private JFXToggleButton activeBtn;
    @FXML
    private Label messageLbl;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Add the buttons of the static dialog
        addDialogBtns();
        
        //validate edit or create option in order to set user
        if(!PerfilController.isProfileCreate){
            System.out.println(PerfilController.selectedProfile.name);
            loadFields();
        }else{
            perfil = new Perfil();
            messageLbl.setVisible(false);
        }
        
        //Inmediate validations
        initValidator();
        
    }

    private void addDialogBtns() {
        JFXButton save = new JFXButton("Guardar");
        save.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(save, 0.0);
        AnchorPane.setRightAnchor(save, 0.0);
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(validateFields()){
                    updateFields();
                    
                    PerfilHelper helper = new PerfilHelper();
                    if(!PerfilController.isProfileCreate){
                        boolean ok = helper.updateProfile(perfil);
                        if(ok){
                            PerfilController.dataPerfilTbl.remove(PerfilController.selectedProfile);
                            PerfilController.updateProfileData(perfil);
                            PerfilController.profileDialog.close();
                        }else{
                            ErrorController error = new ErrorController();
                            error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
                        }
                    }else{
                        for (PerfilController.Profile profile : PerfilController.dataPerfilTbl) {
                            if(profile.name.getValue().equals(nameTxt.getText())){
                                ErrorController error = new ErrorController();
                                error.loadDialog("Erro", "No se puede crear este perfil porque ya existe este nombre", "Ok", hiddenSp);

                                nameTxt.requestFocus();

                                return;
                            }
                        }
                        Long id = helper.saveProfile(CrearEditarPerfilController.perfil);
                        if(id != null){
                            PerfilController.updateProfileData(CrearEditarPerfilController.perfil);
                            PerfilController.profileDialog.close();
                        }else{
                            ErrorController error = new ErrorController();
                            error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
                        }
                    }
                    
                }
            }
        });
        
        JFXButton cancel = new JFXButton("Cancelar");
        cancel.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(cancel, 0.0);
        AnchorPane.setRightAnchor(cancel, 85.0);
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PerfilController.profileDialog.close();
            }
        });
        
        containerPane.getChildren().add(save);
        containerPane.getChildren().add(cancel);
    }
    
    public boolean validateFields() {
        if(!nameTxt.validate()){
            nameTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            nameTxt.requestFocus();
            return false;
        }else{
            return true;
        }
    }
    
    public void updateFields() {
        perfil.setNombre(nameTxt.getText());
        perfil.setDescripcion(descriptionTXt.getText());
        perfil.setActivo(activeBtn.isSelected());
    }

    private void loadFields() {
        PerfilHelper helper = new PerfilHelper();
        Perfil temp = helper.getProfile(PerfilController.selectedProfile.name.getValue());
        
        if(temp != null){
            perfil = new Perfil();
            perfil.setId(temp.getId());
            perfil.setNombre(temp.getNombre());
            perfil.setDescripcion(temp.getDescripcion());
            perfil.setPermisos(temp.getPermisos());
            perfil.setActivo(temp.isActivo());
            
            if(temp.getEditable()!=null && !temp.getEditable()){
                messageLbl.setVisible(true);
                nameTxt.setEditable(false);
                descriptionTXt.setEditable(false);
                activeBtn.setDisable(true);
            }else{
                messageLbl.setVisible(false);
            }
            nameTxt.setText(perfil.getNombre());
            descriptionTXt.setText(perfil.getDescripcion());
            activeBtn.setSelected(perfil.isActivo());
        }
        
        helper.close();
    }

    private void initValidator() {
        RequiredFieldValidator r;
        
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");  
        nameTxt.getValidators().add(r);
        nameTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!nameTxt.validate()){
                    nameTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else nameTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
    }
    
    
}
