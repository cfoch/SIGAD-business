/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.insumos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.DoubleValidator;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.Proveedor;
import com.sigad.sigad.business.ProveedorInsumo;
import com.sigad.sigad.business.helpers.InsumosHelper;
import com.sigad.sigad.business.helpers.ProveedorHelper;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

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
    private AnchorPane containerPane;

//    @FXML
//    private JFXTextField precioTxt;

    @FXML
    private JFXTextField nombreTxt;


    @FXML
    private JFXTextField volumenTxt;

    @FXML
    private JFXTextField tiempoTxt;

    @FXML
    private JFXTextArea descripcionTxt;

    @FXML
    private JFXTreeTableView<ProveedorViewer> tblProveedor;
    
    @FXML
    private JFXToggleButton tglActive;
    
    public static Insumo insumo = null;
    
    JFXTreeTableColumn<ProveedorViewer,String> nombreCol = new JFXTreeTableColumn<>("Nombre");
    JFXTreeTableColumn<ProveedorViewer,String> cantidadCol = new JFXTreeTableColumn<>("Precio");
    static ObservableList<ProveedorViewer> listaProv;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        listaProv = FXCollections.observableArrayList();
        setColumns();
        addColumns();
        if(!ListaInsumoController.isInsumoCreate) {
            System.out.println(ListaInsumoController.selectedInsumo);
            loadFields();
        }
        else {
            insumo = new Insumo();
        }
        fillData();
        addDialogBtns();

        
        initValidator();
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
                InsumosHelper helper = new InsumosHelper();    
                ArrayList<ProveedorInsumo> listaInsumoProv = new ArrayList<>();
                listaProv.forEach((p)->{
                    if(!p.getPrecio().getValue().equals("")){
//                       ProveedorInsumo provin = helper.getInsumoProveedorUnit(insumo, p.getProv());
//                        if(provin != null){
//                            System.out.println("ya esxite relacion con proveedor");
//                            System.out.println("id insumo" + provin.getInsumo().getId());
//                            System.out.println("id insumo" + provin.getProveedor().getId());
//                            listaInsumoProv.add(provin);
//                        }
//                        else {
//                            
                            ProveedorInsumo pr = new ProveedorInsumo();
                            pr.setInsumo(insumo);
                            pr.setProveedor(p.getProv());
                            pr.setPrecio(Double.parseDouble(p.getPrecio().getValue()));
                            pr.setActivo(true);
                            listaInsumoProv.add(pr);
//                        }
                    }
                });
                //edicion
                if(!ListaInsumoController.isInsumoCreate){
                    Long id = helper.updateInsumo(insumo,null);
                    if(id != null){
                        ListaInsumoController.insumosList.remove(ListaInsumoController.selectedInsumo);
                        ListaInsumoController.updateTable(insumo);
                        ListaInsumoController.insumoDialog.close();
                    }else{
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
                    }
                }
                //creacion
                else{
                    Long id = helper.saveInsumo(insumo, listaInsumoProv);
                    if(id != null){
                        ListaInsumoController.updateTable(insumo);
                        ListaInsumoController.insumoDialog.close();
                    }else {
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
            ListaInsumoController.insumoDialog.close();
        });
        
        containerPane.getChildren().add(save);
        containerPane.getChildren().add(cancel);
    }
    
    private void loadFields(){
        
        InsumosHelper helper = new InsumosHelper();
        insumo = helper.getInsumo(ListaInsumoController.selectedInsumo.getId());
        
        if(insumo != null) {
            nombreTxt.setText(insumo.getNombre());
            nombreTxt.setDisable(true);
            tiempoTxt.setText(insumo.getTiempoVida() + "");
            descripcionTxt.setText(insumo.getDescripcion());
            volumenTxt.setText(insumo.isVolumen()+"");
            tglActive.setSelected(insumo.isActivo());
        }
        helper.close();
    }
    private void fillFields(){
        insumo.setNombre(nombreTxt.getText());
        insumo.setTiempoVida(Integer.parseInt(tiempoTxt.getText()));
        insumo.setVolumen(Double.parseDouble(volumenTxt.getText()));
        insumo.setActivo(true);
        insumo.setStockTotalFisico(0);
        insumo.setStockTotalLogico(0);
        insumo.setDescripcion(descripcionTxt.getText());
        insumo.setActivo(tglActive.isSelected());
    }
    
    public boolean validateFields() {
        if(!nombreTxt.validate()){
            nombreTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            nombreTxt.requestFocus();
            return false;
        }else if(!tiempoTxt.validate()){
            tiempoTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            tiempoTxt.requestFocus();
            return false;
        }else if(!descripcionTxt.validate()){
            descripcionTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            descripcionTxt.requestFocus();
            return false;
        }
        else if(!volumenTxt.validate()){
            volumenTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            volumenTxt.requestFocus();
            return false;
        }
        else return true;
    }
    
    private void initValidator() {
        RequiredFieldValidator r;
        NumberValidator n;
        DoubleValidator d;
                
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");  
        nombreTxt.getValidators().add(r);
        nombreTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!nombreTxt.validate()){
                    nombreTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else nombreTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
        
        /*Tiempo de vida*/
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        tiempoTxt.getValidators().add(r);
        
        n = new NumberValidator();
        n.setMessage("Campo numérico");
        n.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        tiempoTxt.getValidators().add(n);
        
        tiempoTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!tiempoTxt.validate()){
                    tiempoTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else tiempoTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });

        
        /*Volumen insumo*/
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio");
        volumenTxt.getValidators().add(r);
        
        
        d = new DoubleValidator();
        d.setMessage("Campo numérico");
        d.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        volumenTxt.getValidators().add(d);
        
        volumenTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!volumenTxt.validate()){
                    volumenTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else volumenTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
    }
    
    
    public class ProveedorViewer extends RecursiveTreeObject<ProveedorViewer>{

        public Proveedor getProv() {
            return prov;
        }

        public void setProv(Proveedor prov) {
            this.prov = prov;
        }
        
        public SimpleStringProperty getNombre() {
            return nombre;
        }

        public SimpleStringProperty getPrecio() {
            return precio;
        }

        public void setNombre(String nombre) {
            this.nombre = new SimpleStringProperty(nombre);
        }

        public void setPrecio(String precio) {
            this.precio = new SimpleStringProperty(precio);
        }

        private SimpleStringProperty nombre;
        private SimpleStringProperty ruc;
        private SimpleStringProperty id;
        private SimpleStringProperty precio;
        private Proveedor prov;


        public ProveedorViewer(String nombre,String precio) {
            this.nombre = new SimpleStringProperty(nombre);
            this.precio = new SimpleStringProperty(precio);
        }
    }
    
    
    private void setColumns(){
        nombreCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProveedorViewer, String> param) -> param.getValue().getValue().getNombre()//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        cantidadCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProveedorViewer, String> param) -> param.getValue().getValue().getPrecio() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        cantidadCol.setCellFactory((TreeTableColumn<ProveedorViewer,String> param) -> new EditingCell());
        cantidadCol.setOnEditCommit((TreeTableColumn.CellEditEvent<ProveedorViewer, String> event) -> {
            Integer i = listaProv.indexOf(event.getRowValue().getValue());
            listaProv.get(i).setPrecio(event.getNewValue());
        });
    }
    private void addColumns(){
        final TreeItem<ProveedorViewer> rootProveedor = new RecursiveTreeItem<>(listaProv,RecursiveTreeObject::getChildren);
        tblProveedor.setEditable(true);
        tblProveedor.getColumns().setAll(nombreCol,cantidadCol);
        tblProveedor.setRoot(rootProveedor);
        tblProveedor.setShowRoot(false);
    }
    private void fillData(){
        ProveedorHelper helperp = new ProveedorHelper();
        InsumosHelper helperi = new InsumosHelper();
        
        ArrayList<Proveedor> listaProvGet = null;
        
        if(!ListaInsumoController.isInsumoCreate){
            
            listaProvGet = helperp.getProveedores();
            if(listaProvGet != null) {
                listaProvGet.forEach((i)->{
                    ProveedorInsumo a = helperi.getInsumoProveedorUnit(insumo, i);
                    if(a != null) {
                       updateTable(i,a.getPrecio().toString());
                    }
                    else {
                        updateTable(i,"");
                    }
                });
            }
        }
        else {
            listaProvGet = helperp.getProveedores();
            if(listaProvGet != null) {
                listaProvGet.forEach((i)->{
                    updateTable(i,"");
                });
            }
        }
        
        helperp.close();
        helperi.close();
    }
    
    public void updateTable(Proveedor p,String cant){
        ProveedorViewer newprov = new ProveedorViewer(p.getNombre(), cant);
        newprov.setProv(p);
        listaProv.add(newprov);
    }
    
    class EditingCell extends TreeTableCell<ProveedorViewer, String> {

        private JFXTextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }

            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }

        private void createTextField() {
            textField = new JFXTextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnKeyPressed((KeyEvent t) -> {
                if (t.getCode() == KeyCode.ENTER) {
                    DoubleValidator n = new DoubleValidator();
                    textField.getValidators().add(n);
                    if(textField.validate()){
                        if(Double.parseDouble(textField.getText()) <0){
                            ErrorController error = new ErrorController();
                            error.loadDialog("Atención", "Debe escribir un valor positivo", "OK", hiddenSp);
                        }
                        else {
                            commitEdit(textField.getText());
                        }
                    }
                    else {
                        textField.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                        textField.requestFocus();
                        ErrorController error = new ErrorController();
                        error.loadDialog("Atención", "Debe escribir un valor numerico", "OK", hiddenSp);
                    }

                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
    
       
}
