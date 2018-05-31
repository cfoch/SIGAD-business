/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.insumos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.helpers.InsumosHelper;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author chrs
 */
public class CrearEditarInsumoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    public static String viewPath = "/com/sigad/sigad/insumos/view/crearEditarInsumo.fxml";
    
    @FXML
    private StackPane hiddenSp;

    @FXML
    private JFXTextField nombreTxt;

    @FXML
    private JFXTextField tiempoTxt;

    @FXML
    private JFXTextField volumenTxt;

    @FXML
    private JFXTextArea descripcionTxt;
    
    @FXML
    private JFXTextField precioTxt;


    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnCancelar;
    
    public static Insumo insumo = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(!ListaInsumoController.isInsumoCreate) {
            System.out.println(ListaInsumoController.selectedInsumo);
            loadFields();
        }
        insumo = new Insumo();
        
        
    }
    
//    private void addDialogBtns() {
//        JFXButton save = new JFXButton("Guardar");
//        save.setPrefSize(80, 25);
//        AnchorPane.setBottomAnchor(save, -20.0);
//        AnchorPane.setRightAnchor(save, 0.0);
//        save.setOnAction((ActionEvent event) -> {
//            if(validateFields()){
//                System.out.println("VALIDADO ALL FIELDS");
////                int indexProfile = getSelectedIndex(profilesListView, "Perfiles");
////                if(indexProfile<0)return;
////                
////                int indexStore = getSelectedIndex(storesListView, "Tiendas");
////                if(indexStore<0) return;
////                updateFields();
//                
//                Insumo helper = new Insumo();
//                //edicion
//                if(!ListaInsumoController.isInsumoCreate){
//                    //boolean ok = helper.updateUser(user);
////                    if(ok){
////                        PersonalController.data.remove(PersonalController.selectedUser);
////                        PersonalController.updateTable(user);
////                        PersonalController.userDialog.close();
////                    }else{
////                        ErrorController error = new ErrorController();
////                        error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
////                    }
//                }
//                //creacion
//                else{
////                    Long id = helper.saveUser(user);
////                    if(id != null){
////                        PersonalController.updateTable(user);
////                        PersonalController.userDialog.close();
////                    }else{
////                        ErrorController error = new ErrorController();
////                        error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
////                    }
//                }
//            }
//        });
//        
//        JFXButton cancel = new JFXButton("Cancelar");
//        cancel.setPrefSize(80, 25);
//        AnchorPane.setBottomAnchor(cancel, -20.0);
//        AnchorPane.setRightAnchor(cancel, 85.0);
//        cancel.setOnAction((ActionEvent event) -> {
//            PersonalController.userDialog.close();
//            //PersonalController.getDataFromDB();
//        });
//        
//        containerPane.getChildren().add(save);
//        containerPane.getChildren().add(cancel);
    //}
    @FXML
    private void handleAction(ActionEvent event) {
        if(event.getSource() == btnCancelar ) {
            ListaInsumoController.insumoDialog.close();
        }
        if(event.getSource() == btnGuardar) {
            //validar
            
            //crear nuevo objeto
            fillFields();
            
            InsumosHelper helper = new InsumosHelper();
            Long id = helper.saveInsumo(insumo);
            if( id!=null){
                ListaInsumoController.updateTable(insumo);
            }
            helper.close();
            ListaInsumoController.insumoDialog.close();
        }
    }
    
    private void loadFields(){
        InsumosHelper helper = new InsumosHelper();
        
        
    }
    private void fillFields(){
        insumo.setNombre(nombreTxt.getText());
        insumo.setTiempoVida(Integer.parseInt(tiempoTxt.getText()));
        insumo.setPrecio(Double.parseDouble(precioTxt.getText()));
        insumo.setActivo(true);
        insumo.setStockTotalFisico(0);
        insumo.setStockTotalLogico(0);
        insumo.setVolumen(Double.parseDouble(volumenTxt.getText()));
        insumo.setDescripcion(descripcionTxt.getText());
    }
    
    
    
}
