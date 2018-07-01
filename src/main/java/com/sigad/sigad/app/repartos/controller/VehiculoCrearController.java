/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.ValidationFacade;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Vehiculo;
import com.sigad.sigad.business.helpers.TipoVehiculoHelper;
import com.sigad.sigad.business.helpers.VehiculoHelper;
import com.sigad.sigad.validations.SIGADValidations;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class VehiculoCrearController implements Initializable {
    public static enum Modo {
        CREAR,
        EDITAR
    };
    public static final String viewPath =
            "/com/sigad/sigad/repartos/view/VehiculoCrear.fxml";

    
    @FXML
    private StackPane hiddenSp;

    @FXML
    private AnchorPane containerPane;
    
    @FXML
    JFXTextField nombreTxt;
    @FXML
    JFXTextField placaTxt;
    @FXML
    JFXTextArea descripcionTxtArea;

    
    @FXML
    private JFXTreeTableView<TipoVehiculoViewer> tblTipoVehiculo;
    
    @FXML
    JFXListView<Label> tipoListView;

    @FXML
    StackPane stackPane;

    private JFXButton crearButton;
    private Modo modo;
    private Long currentVehiculoId;

    private Map<Label, Vehiculo.Tipo> tipoListViewItems;
    
    public static Vehiculo vehiculo = null;
    
    JFXTreeTableColumn<TipoVehiculoViewer,String> marcaCol = new JFXTreeTableColumn<>("Marca");
    JFXTreeTableColumn<TipoVehiculoViewer,String> modeloCol = new JFXTreeTableColumn<>("Modelo");

    static ObservableList<TipoVehiculoViewer> listaVehiculo;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaVehiculo = FXCollections.observableArrayList();
        setColumns();
        addColumns();
        if(!VehiculoController.isVehiculoCreate){
            //tblTipoVehiculo.setDisable(true);
            placaTxt.setDisable(true);
            loadFields();
        }
        else{
            vehiculo = new Vehiculo();
        }
        fillData();
        addDialogBtns();
        
    }
    
    
    
    public class TipoVehiculoViewer extends RecursiveTreeObject<TipoVehiculoViewer>{

        public SimpleStringProperty getMarca() {
            return marca;
        }

        public SimpleStringProperty getModelo() {
            return modelo;
        }

        public Vehiculo.Tipo getVeh() {
            return veh;
        }

        public void setMarca(String marca) {
            this.marca = new SimpleStringProperty(marca);
        }

        public void setModelo(String modelo) {
            this.modelo = new  SimpleStringProperty(modelo);
        }

        public void setVeh(Vehiculo.Tipo veh) {
            this.veh = veh;
        }
        private SimpleStringProperty marca;
        private SimpleStringProperty modelo;
        private Vehiculo.Tipo veh;
        
        public TipoVehiculoViewer(String marca,String modelo){
            this.marca = new SimpleStringProperty(marca);
            this.modelo = new SimpleStringProperty(modelo);
        }
    }
    
    
    private void addDialogBtns() {
        JFXButton save = new JFXButton("Guardar");
        save.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(save, -20.0);
        AnchorPane.setRightAnchor(save, 0.0);
        save.setOnAction((ActionEvent event) -> {
            if(validateFields()){
                System.out.println("VALIDADO ALL FIELDS");
                fillFields();
                VehiculoHelper helperv = new VehiculoHelper();    
                //edicion
                if(!VehiculoController.isVehiculoCreate){
                    Boolean ok = helperv.updateVehiculo(vehiculo);
                    if(ok){
                        VehiculoController.vehiculosList.remove(VehiculoController.selectedVehiculo);
                        VehiculoController.updateTable(vehiculo);
                        VehiculoController.vehiculoDialog.close();
                    }else{
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", helperv.getErrorMessage(), "Ok", hiddenSp);
                    }
                }
                //creacion
                else{
                    Long id = helperv.saveVehiculo(vehiculo);
                    if(id != null){
                        VehiculoController.updateTable(vehiculo);
                        VehiculoController.vehiculoDialog.close();
                    }else {
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", helperv.getErrorMessage(), "Ok", hiddenSp);
                    }
                }
                helperv.close();
            }
        });
        
        JFXButton cancel = new JFXButton("Cancelar");
        cancel.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(cancel, -20.0);
        AnchorPane.setRightAnchor(cancel, 85.0);
        cancel.setOnAction((ActionEvent event) -> {
            VehiculoController.vehiculoDialog.close();
        });
        
        containerPane.getChildren().add(save);
        containerPane.getChildren().add(cancel);
    }
    
    public boolean validateFields() {
//        if(!nombreTxt.validate()){
//            nombreTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
//            nombreTxt.requestFocus();
//            return false;
//        }else if(!tiempoTxt.validate()){
//            tiempoTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
//            tiempoTxt.requestFocus();
//            return false;
//        }else if(!descripcionTxt.validate()){
//            descripcionTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
//            descripcionTxt.requestFocus();
//            return false;
//        }
//        else if(!volumenTxt.validate()){
//            volumenTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
//            volumenTxt.requestFocus();
//            return false;
//        }
//        else 
        if(tblTipoVehiculo.getSelectionModel().getSelectedIndex()<0){
            return false;
        }
            return true;
    }
    
    private void loadFields(){
        
        VehiculoHelper helperv = new VehiculoHelper();
        vehiculo = helperv.getVehiculo(VehiculoController.selectedVehiculo.getId());
        
        if(vehiculo != null) {
            nombreTxt.setText(vehiculo.getNombre());
            nombreTxt.setDisable(true);
            placaTxt.setText(vehiculo.getPlaca());
            descripcionTxtArea.setText(vehiculo.getDescripcion());
        }
        helperv.close();
    }
    private void fillFields(){
        vehiculo.setNombre(nombreTxt.getText());
        vehiculo.setPlaca(placaTxt.getText());
        vehiculo.setDescripcion(descripcionTxtArea.getText());
        vehiculo.setTipo(tblTipoVehiculo.getSelectionModel().getSelectedItem().getValue().getVeh());
        vehiculo.setTienda(LoginController.user.getTienda());
    }
    public void setColumns(){
        marcaCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<TipoVehiculoViewer, String> param) -> param.getValue().getValue().getMarca()
        );
        modeloCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<TipoVehiculoViewer, String> param) -> param.getValue().getValue().getModelo()
        );
    }
    
    public void addColumns(){
        final TreeItem<TipoVehiculoViewer> rootTipoVehiculo = new RecursiveTreeItem<>(listaVehiculo,RecursiveTreeObject::getChildren);
        tblTipoVehiculo.getColumns().setAll(marcaCol,modeloCol);
        tblTipoVehiculo.setRoot(rootTipoVehiculo);
        tblTipoVehiculo.setShowRoot(false);
        
    }
    public void fillData(){
        TipoVehiculoHelper helpertv = new TipoVehiculoHelper();
        ArrayList<Vehiculo.Tipo> listaVehiculos = null;
        
        listaVehiculos = helpertv.getTiposVehiculo();
        
        if(listaVehiculos != null){
            listaVehiculos.forEach((i)->{
                updateTable(i);
            });
        }
        helpertv.close();
    }
    
    public void updateTable(Vehiculo.Tipo tipo){
        TipoVehiculoViewer newTipo = new TipoVehiculoViewer(tipo.getMarca(),tipo.getModelo());
        newTipo.setVeh(tipo);
        listaVehiculo.add(newTipo);
    }
}
