/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.personal.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.PedidoHelper;
import com.sigad.sigad.business.helpers.PerfilHelper;
import com.sigad.sigad.business.helpers.TiendaHelper;
import com.sigad.sigad.business.helpers.UsuarioHelper;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

/**
 *
 * @author jorgeespinoza
 */
public class CrearEditarUsuarioController implements Initializable {

    public static final String viewPath = "/com/sigad/sigad/personal/view/crearEditarUsuario.fxml";
    public static Usuario user = null;
    @FXML
    private JFXTextField nameTxt,appTxt,apmTxt,dniTxt,telephoneTxt,cellphoneTxt,emailTxt;
    @FXML 
    private JFXPasswordField passwordTxt;
    @FXML
    private StackPane hiddenSp;
    @FXML
    private AnchorPane containerPane;
    @FXML
    private JFXToggleButton isActiveBtn;
    @FXML
    private JFXScrollPane profilePane, storePane;
    @FXML
    private JFXListView<Label> storesListView,profilesListView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //initElements
        initElements();
        
        //Add the buttons of the static dialog
        addDialogBtns();
        
        //validate edit or create option in order to set user
        if(!PersonalController.isUserCreate){
            System.out.println(PersonalController.selectedUser.nombres);
            loadFields();
        }else{
            user = new Usuario();
        }
        
        //Inmediate validations
        initValidator();
        
        //initProfileTable();
        initProfilePicker();
        
        //initStoresTable()
        initStorePicker();
        
    }

    private ArrayList<Pedido> deactivateClient(){
        PedidoHelper helperp = new PedidoHelper();
        ArrayList<Pedido> listaPedidos = helperp.getPedidosCliente(user.getId());
        if(listaPedidos==null){
            ErrorController error = new ErrorController();
            error.loadDialog("Error", "No se puede desactivar cliente con pedidos asignados", "Ok", hiddenSp);
        }
        helperp.close();
        return listaPedidos;
    }
    private ArrayList<Pedido> deactivateRepartidor(){
        PedidoHelper helperp = new PedidoHelper();
        ArrayList<Pedido> listaPedidos = helperp.getPedidosVendedor(user.getId());
        if(listaPedidos==null){
            ErrorController error = new ErrorController();
            error.loadDialog("Error", "No se puede desactivar repartidor con pedidos asignados", "Ok", hiddenSp);          
        }
        helperp.close();
        return listaPedidos;
    }
    private void initElements(){
        isActiveBtn.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                //verify to deactivate
                switch (user.getPerfil().getNombre()){
                    case "Cliente":
                        if(deactivateClient() == null){
                            isActiveBtn.setSelected(true);
                        }
                        break;
                    case "Repartidor":
                        if(deactivateRepartidor()== null){
                            isActiveBtn.setSelected(true);
                        }
                        break;                   
                }
            }
            else {
                //verify to activate
                if(!PersonalController.isUserCreate){
                    if(!user.getPerfil().isActivo()){
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", "No se puede activar este usuario, su perfil esta desactivado", "Ok", hiddenSp); 
                        isActiveBtn.setSelected(true);
                    }
                }
            }
        }));
    }
    private void addDialogBtns() {
        JFXButton save = new JFXButton("Guardar");
        save.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(save, -20.0);
        AnchorPane.setRightAnchor(save, 0.0);
        save.setOnAction((ActionEvent event) -> {
            if(validateFields()){
                System.out.println("VALIDADO ALL FIELDS");
                int indexProfile = getSelectedIndex(profilesListView, "Perfiles");
                if(indexProfile<0)return;
                
                int indexStore = getSelectedIndex(storesListView, "Tiendas");
                if(indexStore<0) return;
                updateFields();
                
                UsuarioHelper helper = new UsuarioHelper();
                if(!PersonalController.isUserCreate){
                    boolean ok = helper.updateUser(user);
                    if(ok){
                        PersonalController.data.remove(PersonalController.selectedUser);
                        PersonalController.updateTable(user);
                        PersonalController.userDialog.close();
                    }else{
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
                    }
                }else{
                    
                    Long id = helper.saveUser(user);
                    if(id != null){
                        PersonalController.updateTable(user);
                        PersonalController.userDialog.close();
                        
                    }else{
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
                    }
                    
                }
                helper.close();
            }
        });
        
        JFXButton cancel = new JFXButton("Cancelar");
        cancel.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(cancel, -20.0);
        AnchorPane.setRightAnchor(cancel, 85.0);
        cancel.setOnAction((ActionEvent event) -> {
            PersonalController.userDialog.close();
            //PersonalController.getDataFromDB();
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
        
        UsuarioHelper usuarioHelper = new UsuarioHelper();
        user = usuarioHelper.getUser(PersonalController.selectedUser.correo.getValue());
        
        if(user != null){
            nameTxt.setText(user.getNombres());
            appTxt.setText(user.getApellidoPaterno());
            apmTxt.setText(user.getApellidoMaterno());
            dniTxt.setText(user.getDni());
            telephoneTxt.setText(user.getTelefono());
            cellphoneTxt.setText(user.getCelular());
            emailTxt.setText(user.getCorreo());
            passwordTxt.setText(LoginController.decrypt(user.getPassword()));
            isActiveBtn.setSelected(user.isActivo());
            //styles
            //passwordTxt.setEditable(false);
            //passwordTxt.setOpacity(0.5);
        }
    }
    
    public boolean validateFields() {
        if(!nameTxt.validate()){
            nameTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            nameTxt.requestFocus();
            return false;
        }else if(!appTxt.validate()){
            appTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            appTxt.requestFocus();
            return false;
        }else if(!apmTxt.validate()){
            apmTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            apmTxt.requestFocus();
            return false;
        }else if(!dniTxt.validate()){
            dniTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            dniTxt.requestFocus();
            return false;
        }else if(dniTxt.getText().length()!=8){
            ErrorController r= new ErrorController();
            r.loadDialog("Error", "Debe el dni debe tener 8 dígitos", "Ok", hiddenSp);
            dniTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            dniTxt.requestFocus();
            return false;
        }else if(!telephoneTxt.validate()){
            cellphoneTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            cellphoneTxt.requestFocus();
            return false;
        }else if(!cellphoneTxt.validate()){
            cellphoneTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            cellphoneTxt.requestFocus();
            return false;
        }else if(cellphoneTxt.getText().length()<9){
            ErrorController r= new ErrorController();
            r.loadDialog("Error", "El celular debe tener 9 dígitos", "Ok", hiddenSp);
            cellphoneTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            cellphoneTxt.requestFocus();
            return false;
        }else if(!emailTxt.validate()){
            emailTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            emailTxt.requestFocus();
            return false;
        }else if(!emailTxt.getText().endsWith("sigad.net")){
            ErrorController r= new ErrorController();
            r.loadDialog("Error", "El correo debe terminar con @sigad.net", "Ok", hiddenSp);
            emailTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            emailTxt.requestFocus();
            return false;
        }else if(!passwordTxt.validate()){
            passwordTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            passwordTxt.requestFocus();
            return false;
        }else return true;
    }

    public void updateFields() {
        user.setNombres(nameTxt.getText());
        user.setApellidoPaterno(appTxt.getText());
        user.setApellidoMaterno(apmTxt.getText());
        user.setDni(dniTxt.getText());
        user.setTelefono(telephoneTxt.getText());
        user.setCelular(cellphoneTxt.getText());
        user.setCorreo(emailTxt.getText());
        user.setPassword(passwordTxt.getText());
        user.setActivo(isActiveBtn.isSelected());
        
        int indexProfile = profilesListView.getSelectionModel().getSelectedIndex();
        int indexStore = storesListView.getSelectionModel().getSelectedIndex();
        
        if(indexProfile>=0){
            String profileName = profilesListView.getItems().get(indexProfile).getText();
            PerfilHelper helper = new PerfilHelper();
            Perfil p = helper.getProfile(profileName);
            if(p!= null){
                user.setPerfil(p);
            }
        }
        
        if(indexStore>=0){
            String storeDirection = storesListView.getItems().get(indexStore).getText();
            TiendaHelper helper1 = new TiendaHelper();
            Tienda t = helper1.getStore(storeDirection);
            if(t!= null){
                user.setTienda(t);
            }
        }
    }

    private void initValidator() {
        RequiredFieldValidator r;
        NumberValidator n;
                
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
        
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");  
        appTxt.getValidators().add(r);
        appTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!appTxt.validate()){
                    appTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else appTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
        
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        apmTxt.getValidators().add(r);
        apmTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!apmTxt.validate()){
                    apmTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else apmTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
        
        /*DNI*/
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        dniTxt.getValidators().add(r);
        
        n = new NumberValidator();
        n.setMessage("Campo numérico");
        n.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        dniTxt.getValidators().add(n);
        
        dniTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!dniTxt.validate()){
                    dniTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else dniTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
        
        /**/
        
        /*TELEPHONE*/
        n = new NumberValidator();
        n.setMessage("Campo numérico");
        n.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        telephoneTxt.getValidators().add(n);
        
        telephoneTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!telephoneTxt.validate()){
                    telephoneTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else telephoneTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
        
        /**/
        
        /*CELLPHONE*/
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        cellphoneTxt.getValidators().add(r);
        
        
        n = new NumberValidator();
        n.setMessage("Campo numérico");
        n.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        cellphoneTxt.getValidators().add(n);
        
        cellphoneTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!cellphoneTxt.validate()){
                    cellphoneTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else cellphoneTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
        
        /**/
        
        /*EMAIL*/
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        emailTxt.getValidators().add(r);
        emailTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!emailTxt.validate()){
                    emailTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else emailTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
        /**/
        
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        passwordTxt.getValidators().add(r);
        passwordTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!passwordTxt.validate()){
                    passwordTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else passwordTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });

    }

    private void initProfilePicker() {
        profilesListView = new JFXListView<>();
        
        //load profiles
        PerfilHelper helper = new PerfilHelper();
        ArrayList<Perfil> perfilList = helper.getProfiles();
        
        if(perfilList != null){
            for (int i = 0; i < perfilList.size(); i++) {
                String profile = perfilList.get(i).getNombre();
                Label lbl = new Label(profile);
                lbl.setPrefSize(200, 30);
                profilesListView.getItems().add(lbl);
                
                if(user!= null && user.getPerfil() != null && user.getPerfil().getNombre().equals(profile)){
                    profilesListView.getSelectionModel().select(i);
                }
            }
        }
        
        profilesListView.getStyleClass().add("mylistview");
        profilesListView.setStyle("-fx-background-color:WHITE");
        
        profilePane.getChildren().add(profilesListView);
    }

    private void initStorePicker() {
        storesListView = new JFXListView<>();
        
        //load stores
        TiendaHelper helper = new TiendaHelper();
        ArrayList<Tienda> tiendaList = helper.getStores();
        
        if(tiendaList != null){
            for (int i = 0; i < tiendaList.size(); i++) {
                String direction = tiendaList.get(i).getDireccion();
                Label lbl = new Label(direction);
                lbl.setPrefSize(200, 30);
                storesListView.getItems().add(lbl);
                
                if(user.getTienda()!=null){
                    if(user.getTienda().getDireccion().equals(direction)){
                        storesListView.getSelectionModel().select(i);
                    }
                }
                
            }
        }
        
        storesListView.getStyleClass().add("mylistview");
        storesListView.setStyle("-fx-background-color:WHITE");
        
        storePane.getChildren().add(storesListView);
    }

}
