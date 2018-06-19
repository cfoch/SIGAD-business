/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.tienda.controller;

import com.google.maps.errors.ApiException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.validation.DoubleValidator;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.GMapsHelper;
import com.sigad.sigad.business.helpers.TiendaHelper;
import com.sigad.sigad.business.helpers.UsuarioHelper;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.tuple.Pair;

/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class CrearEditarTiendaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/tienda/view/crearEditarTienda.fxml";
    public static Tienda tienda = null;
    @FXML
    private StackPane hiddenSp;
    @FXML
    private AnchorPane containerPane;
    @FXML
    private JFXToggleButton isActiveBtn;
    @FXML
    private JFXTextArea descripcionTxt;
    @FXML
    private JFXButton generateBtn;
    @FXML
    private JFXTextField direccionTxt,coordYText,capacidadTxt;
    @FXML
    private JFXTextField coordXText;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initElements();
        //Add the buttons of the static dialog
        addDialogBtns();
        
        //validate edit or create option in order to set user
        if(!TiendaController.isStoreCreate){
            System.out.println(TiendaController.selectedStore.dirección);
            loadFields();
        }else{
            tienda = new Tienda();
        }
        
        //Inmediate validations
        initValidator();
    }    

    private ArrayList<Usuario> deactivateStore(){
        UsuarioHelper helperu = new UsuarioHelper();
        ArrayList<Usuario> listaUs = helperu.getUsersStore(tienda);
        if(listaUs == null){
            ErrorController error = new ErrorController();
            error.loadDialog("Error", "No se puede desactivar tienda con usuarios", "Ok", hiddenSp);
        }
        helperu.close();
        return listaUs;
    }
    private void initElements(){
        isActiveBtn.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                //verify to deactivate
                if(deactivateStore() != null){
                    isActiveBtn.setSelected(true);
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
                updateFields();
                
                TiendaHelper helper = new TiendaHelper();
                if(!TiendaController.isStoreCreate){
                    boolean ok = helper.updateStore(tienda);
                    if(ok){
                        TiendaController.data.remove(TiendaController.selectedStore);
                        TiendaController.updateTable(tienda);
                        TiendaController.storeDialog.close();
                    }else{
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
                    }
                }else{
                    Long id = helper.saveStore(tienda);
                    if(id != null){
                        TiendaController.updateTable(tienda);
                        TiendaController.storeDialog.close();
                    }else{
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
                    }
                    
                }
            }
        });
        
        JFXButton cancel = new JFXButton("Cancelar");
        cancel.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(cancel, -20.0);
        AnchorPane.setRightAnchor(cancel, 85.0);
        cancel.setOnAction((ActionEvent event) -> {
            TiendaController.storeDialog.close();
            //PersonalController.getDataFromDB();
        });
        
        containerPane.getChildren().add(save);
        containerPane.getChildren().add(cancel);
    }

    private boolean validateFields() {
        if(!direccionTxt.validate()){
            direccionTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            direccionTxt.requestFocus();
            return false;
        }if(!capacidadTxt.validate()){
            direccionTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            direccionTxt.requestFocus();
            return false;
        }else return true;
    }

    private void updateFields() {
        tienda.setActivo(isActiveBtn.isSelected());
        tienda.setDireccion(direccionTxt.getText());
        tienda.setCapacidad(Double.parseDouble(capacidadTxt.getText()));
        if(coordXText.getText().isEmpty()){
            tienda.setCooXDireccion(0.0);
        }else{
            tienda.setCooXDireccion(Double.parseDouble(coordXText.getText()));
        }
        if(coordXText.getText().isEmpty()){
            tienda.setCooYDireccion(0.0);
        }else{
            tienda.setCooYDireccion(Double.parseDouble(coordYText.getText()));
        }
        tienda.setDescripcion(descripcionTxt.getText());
    }

    private void loadFields() {
        TiendaHelper helper = new TiendaHelper();
        tienda = helper.getStore(TiendaController.selectedStore.dirección.getValue());
        
        if(tienda != null){
            isActiveBtn.setSelected(tienda.isActivo());
            direccionTxt.setText(tienda.getDireccion());
            capacidadTxt.setText(Double.toString(tienda.getCapacidad()));
            coordXText.setText(Double.toString(tienda.getCooXDireccion()));
            coordYText.setText(Double.toString(tienda.getCooYDireccion()));
            descripcionTxt.setText(tienda.getDescripcion());
        }
    }

    private void initValidator() {
        RequiredFieldValidator r;
        DoubleValidator n;
                
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");  
        direccionTxt.getValidators().add(r);
        direccionTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!direccionTxt.validate()){
                    direccionTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else direccionTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
        
        n = new DoubleValidator();
        n.setMessage("Campo numérico");
        n.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        capacidadTxt.getValidators().add(n);
        
        capacidadTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!capacidadTxt.validate()){
                    capacidadTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else capacidadTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
    }

    @FXML
    private void generateLatLng(MouseEvent event){
        GMapsHelper helper = GMapsHelper.getInstance();
        
        try {
            Pair<Double, Double> pair = helper.geocodeAddress(direccionTxt.getText());
            
            if(pair==null){
                ErrorController error = new ErrorController();
                error.loadDialog("Error", "No se pudo obtener la lotitud y longitud para esta dirección", "Ok", hiddenSp);
            }else{
                Double lat = pair.getKey();
                Double lng = pair.getValue();
                
                coordXText.setText(lat.toString());
                coordYText.setText(lng.toString());
            }
            
        } catch (InterruptedException | IOException | ApiException ex) {
            Logger.getLogger(CrearEditarTiendaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
