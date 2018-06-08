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
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.DoubleValidator;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.LoteInsumo;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.TipoMovimiento;
import com.sigad.sigad.business.helpers.InsumosHelper;
import com.sigad.sigad.business.helpers.LoteInsumoHelper;
import com.sigad.sigad.business.helpers.MovimientoHelper;
import com.sigad.sigad.business.helpers.TipoMovimientoHelper;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
    
    
    @FXML
    private JFXTreeTableView<LoteInsumoViewer> tblLotes;
    
    //variable insumo
    public static Insumo insumo = null;
    
    //variable movimiento
    public static MovimientosTienda movimiento= null;
    
    static ObservableList<LoteInsumoViewer> lotesList;
    
    JFXTreeTableColumn<LoteInsumoViewer,String> codigoCol = new JFXTreeTableColumn<>("Codigo");
    JFXTreeTableColumn<LoteInsumoViewer,String> fechaCol = new JFXTreeTableColumn<>("Fecha Vencimiento");
    JFXTreeTableColumn<LoteInsumoViewer,String> stockCol = new JFXTreeTableColumn<>("Stock Fisico");
    
    
    public static class LoteInsumoViewer extends RecursiveTreeObject<LoteInsumoViewer>{

        public SimpleStringProperty getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = new SimpleStringProperty(stock);
        }

        public SimpleStringProperty getCodigo() {
            return codigo;
        }

        public SimpleStringProperty getFechaVencimiento() {
            return fechaVencimiento;
        }

        public void setCodigo(String codigo) {
            this.codigo = new SimpleStringProperty(codigo);
        }

        public void setFechaVencimiento(String fechaVencimiento) {
            this.fechaVencimiento = new SimpleStringProperty(fechaVencimiento);
        }
        
        public LoteInsumo getLote() {
            return lote;
        }

        public void setLote(LoteInsumo lote) {
            this.lote = lote;
        }
        
        private SimpleStringProperty codigo;
        private SimpleStringProperty fechaVencimiento;
        private LoteInsumo lote;
        private SimpleStringProperty stock;
        
        public LoteInsumoViewer(String codigo,String fechaVencimiento,String stock){
            this.codigo = new SimpleStringProperty(codigo);
            this.fechaVencimiento = new SimpleStringProperty(fechaVencimiento);
            this.stock = new SimpleStringProperty(stock);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tblLotes.setDisable(true);
        lotesList = FXCollections.observableArrayList();
        movimiento = new MovimientosTienda();
        setColumns();
        addColumns();
        loadInsumo();
        fillData();
        addDialogBtns();
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
        helpertm.close();
        
        cbxTipo.valueProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            TipoMovimiento li = (TipoMovimiento) newValue;
            if(li.getNombre().equals("Ingreso")){
                tblLotes.setDisable(true);
            }
            else if(li.getNombre().equals("Salida")){
                tblLotes.setDisable(false);
            }
        });
        
        LoteInsumoHelper helperli = new LoteInsumoHelper();
        ArrayList<LoteInsumo> insumosSpecific = helperli.getLoteInsumosEspecific(LoginController.user.getTienda(),insumo);
        if(insumosSpecific !=null){
            insumosSpecific.forEach((i)->{
                updateTable(i);
            });
        }
        
        helperli.close();
    }
    
    private void setColumns(){
        codigoCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<LoteInsumoViewer, String> param) -> param.getValue().getValue().getCodigo() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        fechaCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<LoteInsumoViewer, String> param) -> param.getValue().getValue().getFechaVencimiento() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );        
        stockCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<LoteInsumoViewer, String> param) -> param.getValue().getValue().getStock() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );        
    }
    
    private void addColumns(){
        final TreeItem<LoteInsumoViewer> rootInsumo = new RecursiveTreeItem<>(lotesList,RecursiveTreeObject::getChildren);
        tblLotes.setEditable(false);
        tblLotes.getColumns().setAll(codigoCol, fechaCol,stockCol);
        tblLotes.setRoot(rootInsumo);
        tblLotes.setShowRoot(false);
        
    }
    
    public void updateTable(LoteInsumo lote){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        LoteInsumoViewer loteinsumoviewer = new LoteInsumoViewer(lote.getId().toString(),df.format(lote.getFechaVencimiento()),lote.getStockFisico().toString());
        loteinsumoviewer.setLote(lote);
        lotesList.add(loteinsumoviewer);
        
    }
    private void addDialogBtns() {
        JFXButton save = new JFXButton("Registrar");
        save.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(save, -20.0);
        AnchorPane.setRightAnchor(save, 0.0);
        
        save.setOnAction((ActionEvent event) -> {
            if(validateFields()){
                System.out.println("VALIDADO ALL FIELDS");
                fillFields();
                
                Tienda currentStore = LoginController.user.getTienda();
                double capacidadTotal = currentStore.getCapacidad();
                double capacidadActual = 0.0;
                
                LoteInsumoHelper helperli = new LoteInsumoHelper();
                MovimientoHelper helpermo = new MovimientoHelper();
                
                LoteInsumo recentLote = helperli.getMostRecentLoteInsumo(currentStore);
                ArrayList<LoteInsumo> lotes = helperli.getLoteInsumos(currentStore);
                if(recentLote != null){
                    
                    //validate if insumo can enter to almacen
                    if(lotes!=null){
                        for (int i = 0; i < lotes.size(); i++) {
                            LoteInsumo next = lotes.get(i);
                            capacidadActual += next.getInsumo().getStockTotalFisico()* next.getInsumo().isVolumen();
                        }
                    }
                    capacidadActual += movimiento.getCantidadMovimiento()*insumo.isVolumen();
                    if(capacidadActual > capacidadTotal){
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", "No puede agregar " + (capacidadActual) + " porque supera la capacidad actual de la tienda es " + (capacidadTotal), "Ok", hiddenSp);
                        return;
                    }
                    
                    //ajustar vencimiento(en lote) FALTAAAA
                    Integer newCantidad = Integer.parseInt(cantidadTxt.getText());
                    //ingreso
                    if(cbxTipo.getValue().getNombre().equals("Ingreso")){
                        
                        //realizar ingreso seleccionando el lote y  validar la cantidad que ingresa.
                        recentLote.setStockFisico(recentLote.getStockFisico() + newCantidad);
                        recentLote.setStockLogico(recentLote.getStockLogico() + newCantidad);
                        helperli.updateLoteInsumo(recentLote);
                        movimiento.setLoteInsumo(recentLote);
                    }
                    //salida
                    else if(cbxTipo.getValue().getNombre().equals("Salida")) {
                        LoteInsumo loteSelected = tblLotes.getSelectionModel().getSelectedItem().getValue().getLote();
                        Integer stockFisico = loteSelected.getStockFisico();
                        
                        if((stockFisico - newCantidad)<0){
                            //show error message
                        }
                        else {
                            loteSelected.setStockFisico(loteSelected.getStockFisico() - newCantidad);
                            loteSelected.setStockLogico(loteSelected.getStockLogico() - newCantidad);
                            helperli.updateLoteInsumo(loteSelected);
                            movimiento.setLoteInsumo(loteSelected); 
                        }
                        //validar que se debe selecionar un lote insumo(falta)
                        
                    }
                    helpermo.saveMovement(movimiento);
                }else {
                    ErrorController error = new ErrorController();
                    error.loadDialog("Error", helperli.getErrorMessage(), "Ok", hiddenSp);
                }
                helperli.close();
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
            return true;
    }
    
    private void initValidator() {
        RequiredFieldValidator r;
        NumberValidator n;
        DoubleValidator d;
                
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio"); 
//        nombreTxt.getValidators().add(r);
//        nombreTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//            if (!newValue) {
//                if(!nombreTxt.validate()){
//                    nombreTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
//                }
//                else nombreTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
//            }
//        });
//        
//        /*Tiempo de vida*/
//        r = new RequiredFieldValidator();
//        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
//        r.setMessage("Campo obligatorio");
//        tiempoTxt.getValidators().add(r);
//        
//        n = new NumberValidator();
//        n.setMessage("Campo numérico");
//        n.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
//        tiempoTxt.getValidators().add(n);
//        
//        tiempoTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//            if (!newValue) {
//                if(!tiempoTxt.validate()){
//                    tiempoTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
//                }
//                else tiempoTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
//            }
//        });
//
//        
//        /*Volumen insumo*/
//        r = new RequiredFieldValidator();
//        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
//        r.setMessage("Campo obligatorio");
//        volumenTxt.getValidators().add(r);
//        
//        
//        d = new DoubleValidator();
//        d.setMessage("Campo numérico");
//        d.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
//        volumenTxt.getValidators().add(d);
//        
//        volumenTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//            if (!newValue) {
//                if(!volumenTxt.validate()){
//                    volumenTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
//                }
//                else volumenTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
//            }
//        });
    }
    
    public void fillFields(){
        
        movimiento.setCantidadMovimiento(Integer.parseInt(cantidadTxt.getText()));
        Instant instant = pckDate.getValue().atTime(LocalTime.now()).atZone(ZoneId.systemDefault()).toInstant();
        movimiento.setFecha(Date.from(instant));
        System.out.println("Fecha" + movimiento.getFecha());
        movimiento.setTienda(LoginController.user.getTienda());
        movimiento.setTipoMovimiento(cbxTipo.getValue());
        movimiento.setTrabajador(LoginController.user);
        
        //validar que se debe seleccionar un lote si se esta produciendo una salida
        
        // ingreso por existencia añadir al lote mas reciente?
        //movimiento.setsetLoteInsumo(LoteTienda);
        
        // salida por accidente añadir sacar de ? lote
    }
}
