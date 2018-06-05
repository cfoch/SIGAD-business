/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.insumos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.TipoMovimiento;
import com.sigad.sigad.business.helpers.InsumosHelper;
import com.sigad.sigad.business.helpers.TipoMovimientoHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author chrs
 */
public class RegistrarIngresoSalidaInsumoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    public static String viewPath = "/com/sigad/sigad/insumos/view/RegistrarIngresoSalidaInsumo.fxml";
    
    @FXML
    private StackPane hiddenSp;

    @FXML
    private AnchorPane containerPane;

    @FXML
    private JFXComboBox<TipoMovimiento> cbxTipo;

    @FXML
    private JFXDatePicker pckDate;

    @FXML
    private JFXTextField cantidadTxt;
    
    @FXML
    private JFXTextField nombreTxt;
    
    public static Insumo insumo = null;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        loadInsumo();
        fillData();
    }
    
    public void loadInsumo(){
        InsumosHelper helper = new InsumosHelper();
        insumo = helper.getInsumo(ListaInsumoController.selectedInsumo.getId());
        
        if(insumo != null) {
            nombreTxt.setText(insumo.getNombre());
            nombreTxt.setDisable(true);
            
        }
        helper.close();
    }
    
    public void fillData(){
        TipoMovimientoHelper helpertm = new TipoMovimientoHelper();
       
        ArrayList<TipoMovimiento> tipos = null;
        tipos = helpertm.getTiposMovimientos();
        if(tipos!= null) {
            tipos.forEach((i)->{
                cbxTipo.getItems().add(i);
            });
            cbxTipo.setPromptText("Tipo Movimiento");
            cbxTipo.setConverter(new StringConverter<TipoMovimiento>() {
                
                Long id = null;
                String des = null;
                
                @Override
                public String toString(TipoMovimiento object) {
                    id =object.getId();
                    des = object.getDescripcion();
                    return object==null? "" : object.getNombre();
                }

                @Override
                public TipoMovimiento fromString(String string) {
                    TipoMovimiento t = new TipoMovimiento();
                    t.setNombre(string);
                    t.setId(id);
                    t.setDescripcion(des);
                    return t;
                }
            });
        }
    }
    
    private void addDialogBtns() {
        JFXButton save = new JFXButton("Guardar");
        save.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(save, -20.0);
        AnchorPane.setRightAnchor(save, 0.0);
//        save.setOnAction((ActionEvent event) -> {
//            if(validateFields()){
//                System.out.println("VALIDADO ALL FIELDS");
//                fillFields();
//                //ingreso
//                    Long id = helper.saveInsumo(insumo, listaInsumoProv);
//                    if(id != null){
//                        ListaInsumoController.updateTable(insumo);
//                        ListaInsumoController.insumoDialog.close();
//                    }else {
//                        ErrorController error = new ErrorController();
//                        error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
//                    }
//               
//                helper.close();
//            }
//        });
        
        JFXButton cancel = new JFXButton("Cancelar");
        cancel.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(cancel, -20.0);
        AnchorPane.setRightAnchor(cancel, 85.0);
        cancel.setOnAction((ActionEvent event) -> {
            ListaInsumoController.insumoDialog.close();
        });
        
        containerPane.getChildren().add(save);
        containerPane.getChildren().add(cancel);
    }
    
    
    
    
    
}
