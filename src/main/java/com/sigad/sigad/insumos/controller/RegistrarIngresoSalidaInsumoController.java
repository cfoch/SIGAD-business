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
import com.sigad.sigad.business.Constantes;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
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
    JFXTreeTableColumn<LoteInsumoViewer,String> vencimientoCol = new JFXTreeTableColumn<>("Estado");
    
    
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
        
        public SimpleStringProperty getVencido() {
            return vencido;
        }

        public void setVencido(String vencido) {
            this.vencido = new SimpleStringProperty(vencido);
        }
        
        private SimpleStringProperty codigo;
        private SimpleStringProperty fechaVencimiento;
        private LoteInsumo lote;
        private SimpleStringProperty stock;
        private SimpleStringProperty vencido;
        
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
        initValidator();
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
       
        ArrayList<TipoMovimiento> tipos;
        tipos = helpertm.getTiposMovimientos();
        helpertm.close();
        LoteInsumoHelper helperli = new LoteInsumoHelper();
        ArrayList<LoteInsumo> insumosSpecific = helperli.getLoteInsumosEspecificPositive(LoginController.user.getTienda(),insumo);
        helperli.close();
        
        if(tipos!= null) {
            tipos.forEach((i)->{
//                if(!(i.getNombre().equals(Constantes.TIPO_MOVIMIENTO_SALIDA_FISICA) && insumosSpecific == null) && i.getNombre().equals(Constantes.TIPO_MOVIMIENTO_SALIDA_LOGICA)
//                            && !i.getNombre().equals(Constantes.TIPO_MOVIMIENTO_ENTRADA_LOGICA)){
//                    
//                }
                if((i.getNombre().equals(Constantes.TIPO_MOVIMIENTO_SALIDA_FISICA) && insumosSpecific != null)|| i.getNombre().equals(Constantes.TIPO_MOVIMIENTO_ENTRADA_FISICA) ){
                    cbxTipo.getItems().add(i);
                }
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
        
        cbxTipo.valueProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            TipoMovimiento li = (TipoMovimiento) newValue;
            if(li.getNombre().equals(Constantes.TIPO_MOVIMIENTO_ENTRADA_FISICA)){
                tblLotes.setDisable(true);
            }
            else if(li.getNombre().equals(Constantes.TIPO_MOVIMIENTO_SALIDA_FISICA)){
                tblLotes.setDisable(false);
            }
        });
        
        if(insumosSpecific !=null){
            insumosSpecific.forEach((i)->{
                updateTable(i);
            });
        }
        Date inputDate = new Date();
        LocalDate date = inputDate .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        pckDate.setValue(date);      
    }
    private void setColumns(){
        codigoCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<LoteInsumoViewer, String> param) -> param.getValue().getValue().getCodigo()
        );
        fechaCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<LoteInsumoViewer, String> param) -> param.getValue().getValue().getFechaVencimiento()
        );        
        stockCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<LoteInsumoViewer, String> param) -> param.getValue().getValue().getStock()
        );
        vencimientoCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<LoteInsumoViewer, String> param) -> param.getValue().getValue().getVencido()
        );
    }
    private void addColumns(){
        final TreeItem<LoteInsumoViewer> rootInsumo = new RecursiveTreeItem<>(lotesList,RecursiveTreeObject::getChildren);
        tblLotes.setEditable(false);
        tblLotes.getColumns().setAll(codigoCol, fechaCol,stockCol,vencimientoCol);
        tblLotes.setRoot(rootInsumo);
        tblLotes.setShowRoot(false);
        
    }
    public void updateTable(LoteInsumo lote){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        LoteInsumoViewer loteinsumoviewer = new LoteInsumoViewer(lote.getId().toString(),df.format(lote.getFechaVencimiento()),lote.getStockFisico().toString());
        Date d = new Date();
        if(lote.getFechaVencimiento().compareTo(d) < 0){
            loteinsumoviewer.setVencido("Vencido");
        }else {
            loteinsumoviewer.setVencido("Vigente");
        }
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
                Integer newCantidad = Integer.parseInt(cantidadTxt.getText());
                double capacidadTotal = currentStore.getCapacidad();
                double capacidadActual = 0.0;
                
                LoteInsumoHelper helperli = new LoteInsumoHelper();
                ArrayList<LoteInsumo> lotesInsumo = helperli.getLoteInsumosEspecific(currentStore, insumo);
                
                if(cbxTipo.getValue().getNombre().equals(Constantes.TIPO_MOVIMIENTO_ENTRADA_FISICA)){

                    if(lotesInsumo !=null) {
                        for (int i = 0; i < lotesInsumo.size(); i++) {
                            LoteInsumo next = lotesInsumo.get(i);
                            capacidadActual += next.getInsumo().getStockTotalLogico()* next.getInsumo().isVolumen();
                        }
                        capacidadActual += movimiento.getCantidadMovimiento()*insumo.isVolumen();
                        if(capacidadActual > capacidadTotal){
                            ErrorController error = new ErrorController();
                            error.loadDialog("Error", "No puede agregar " + (capacidadActual) + " porque supera la capacidad actual de la tienda es " + (capacidadTotal), "Ok", hiddenSp);
                            return;
                        }
                        
                        //siempre existira porque la lista no es vacia
                        LoteInsumo recentLote = helperli.getMostRecentLoteInsumo(currentStore,insumo);
                        //realizar ingreso seleccionando el lote y  validar la cantidad que ingresa.
                        recentLote.setStockFisico(recentLote.getStockFisico() + newCantidad);
                        recentLote.setStockLogico(recentLote.getStockLogico() + newCantidad);
                        //stock de insumos de todas las tiendas
                        insumo.setStockTotalFisico(insumo.getStockTotalFisico() + newCantidad );
                        insumo.setStockTotalLogico(insumo.getStockTotalLogico() + newCantidad);
                        movimiento.setLoteInsumo(recentLote);
                        
                        Boolean saveLote = helperli.updateLoteInsumo(recentLote);
                        if(!saveLote){
                            ErrorController error = new ErrorController();
                            error.loadDialog("Error", helperli.getErrorMessage(), "Ok", hiddenSp);
                        }
                        helperli.close();
                        
                        
                        InsumosHelper helperi = new InsumosHelper();
                        Boolean saveInsumo = helperi.updateInsumo(insumo);
                        if(!saveInsumo){
                            ErrorController error = new ErrorController();
                            error.loadDialog("Error", helperi.getErrorMessage(), "Ok", hiddenSp);
                        }
                        helperi.close();
                        
                        
                        MovimientoHelper helpermo = new MovimientoHelper();
                        Boolean saveMov = helpermo.saveMovement(movimiento);
                        if(!saveMov){
                            ErrorController error = new ErrorController();
                            error.loadDialog("Error", helperli.getErrorMessage(), "Ok", hiddenSp);
                        }
                        helpermo.close();
                        
                        if(saveLote && saveInsumo && saveMov){
                            ListaInsumoController.insumosList.remove(ListaInsumoController.selectedInsumo);                                   
                            ListaInsumoController.updateTable(insumo);
                            ListaInsumoController.insumoDialog.close();
                        }
                    }
                    else {
                        //crear lote default
                        LoteInsumo loteDefault = new LoteInsumo();
                        loteDefault.setCostoUnitario(0.0);
                        Date date =  new Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        
                        c.add(Calendar.DATE, insumo.getTiempoVida());
                        Date currentDatePlus = c.getTime();
                        
                        loteDefault.setFechaVencimiento(currentDatePlus);
                        loteDefault.setInsumo(insumo);
                        loteDefault.setStockFisico(newCantidad);
                        loteDefault.setStockLogico(newCantidad);
                        loteDefault.setTienda(currentStore);
                        
                        insumo.setStockTotalFisico(insumo.getStockTotalFisico() + newCantidad);
                        insumo.setStockTotalLogico(insumo.getStockTotalLogico() + newCantidad);
                        movimiento.setLoteInsumo(loteDefault);
                        
                        Long saveLote = helperli.saveLoteInsumo(loteDefault);
                        if(saveLote!=null){
                            ErrorController error = new ErrorController();
                            error.loadDialog("Error", helperli.getErrorMessage(), "Ok", hiddenSp);
                        }                       
                        
                        InsumosHelper helperi = new InsumosHelper();
                        Boolean saveInsumo = helperi.updateInsumo(insumo);
                        if(!saveInsumo){
                            ErrorController error = new ErrorController();
                            error.loadDialog("Error", helperi.getErrorMessage(), "Ok", hiddenSp);
                        }
                        
                        MovimientoHelper helpermo = new MovimientoHelper();
                        Boolean saveMov = helpermo.saveMovement(movimiento);
                        if(!saveMov){
                            ErrorController error = new ErrorController();
                            error.loadDialog("Error", helpermo.getErrorMessage(), "Ok", hiddenSp);
                        }
                        
                        if(saveLote!=null && saveInsumo && saveMov){
                            ListaInsumoController.insumosList.remove(ListaInsumoController.selectedInsumo);                                   
                            ListaInsumoController.updateTable(insumo);
                            ListaInsumoController.insumoDialog.close();
                        }
                    }
                }
                else if(cbxTipo.getValue().getNombre().equals(Constantes.TIPO_MOVIMIENTO_SALIDA_FISICA)){
                    
                    if(lotesInsumo !=null){
                        //validar que se debe selecionar un lote insumo(falta)
                        LoteInsumo loteSelected = tblLotes.getSelectionModel().getSelectedItem().getValue().getLote();
                        Integer stockFisico = loteSelected.getStockFisico();

                        if((stockFisico - newCantidad)<0){
                            //show error message
                            ErrorController error = new ErrorController();
                            error.loadDialog("Error", "Cantidad supera el stock", "Ok", hiddenSp);
                            return;
                        }
                        else {
                            loteSelected.setStockFisico(loteSelected.getStockFisico() - newCantidad);
                            loteSelected.setStockLogico(loteSelected.getStockLogico() - newCantidad);
                            //stock de insumos de todas las tiendas
                            insumo.setStockTotalFisico(insumo.getStockTotalFisico() - newCantidad );
                            insumo.setStockTotalLogico(insumo.getStockTotalLogico() - newCantidad);
                            movimiento.setLoteInsumo(loteSelected);                        

                            Boolean saveLote = helperli.updateLoteInsumo(loteSelected);
                            if(!saveLote){
                                ErrorController error = new ErrorController();
                                error.loadDialog("Error", helperli.getErrorMessage(), "Ok", hiddenSp);
                            }
                            helperli.close();


                            InsumosHelper helperi = new InsumosHelper();
                            Boolean saveInsumo = helperi.updateInsumo(insumo);
                            if(!saveInsumo){
                                ErrorController error = new ErrorController();
                                error.loadDialog("Error", helperi.getErrorMessage(), "Ok", hiddenSp);
                            }
                            helperi.close();


                            MovimientoHelper helpermo = new MovimientoHelper();
                            Boolean saveMov = helpermo.saveMovement(movimiento);
                            if(!saveMov){
                                ErrorController error = new ErrorController();
                                error.loadDialog("Error", helperli.getErrorMessage(), "Ok", hiddenSp);
                            }
                            helpermo.close();

                            if(saveLote && saveInsumo && saveMov){
                                ListaInsumoController.insumosList.remove(ListaInsumoController.selectedInsumo);                                   
                                ListaInsumoController.updateTable(insumo);
                                ListaInsumoController.insumoDialog.close();
                            }
                        }
                    }
                    else {
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", "No puede realizar una salida, no existe ningun lote del insumo", "Ok", hiddenSp);
                    }
                }
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
        if(cbxTipo.getValue() == null){
            ErrorController error = new ErrorController();
            error.loadDialog("Error","Debe seleccionar un tipo de movimiento", "Ok", hiddenSp);
            return false;
        }
        else if(cbxTipo.getValue().getNombre().equals(Constantes.TIPO_MOVIMIENTO_SALIDA_FISICA) && tblLotes.getSelectionModel().getSelectedItem() == null){
            ErrorController error = new ErrorController();
            error.loadDialog("Error","Debe seleccionar un lote de insumos", "Ok", hiddenSp);
            return false;
        }
        else if(!cantidadTxt.validate()){
            cantidadTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
            cantidadTxt.requestFocus();
            return false;
        }
        else{
            return true;
        } 
    }
    private void initValidator() {
        RequiredFieldValidator r;
        NumberValidator n;
        DoubleValidator d;
                
        r = new RequiredFieldValidator();
        r.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        r.setMessage("Campo obligatorio"); 
        cantidadTxt.getValidators().add(r);
        
        n = new NumberValidator();
        n.setMessage("Campo numérico");
        n.setIcon(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_CIRCLE));
        cantidadTxt.getValidators().add(n);
        cantidadTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if(!cantidadTxt.validate()){
                    cantidadTxt.setFocusColor(new Color(0.58, 0.34, 0.09, 1));
                }
                else cantidadTxt.setFocusColor(new Color(0.30,0.47,0.23, 1));
            }
        });
    }
    public void fillFields(){
        
        movimiento.setCantidadMovimiento(Integer.parseInt(cantidadTxt.getText()));
        Instant instant = pckDate.getValue().atTime(LocalTime.now()).atZone(ZoneId.systemDefault()).toInstant();
        movimiento.setFecha(Date.from(instant));
        System.out.println("Fecha" + movimiento.getFecha());
        movimiento.setTienda(LoginController.user.getTienda());
        movimiento.setTipoMovimiento(cbxTipo.getValue());
        movimiento.setTrabajador(LoginController.user);
        
    }
}
