/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.ordenescompra.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Constantes;
import com.sigad.sigad.business.DetalleOrdenCompra;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.LoteInsumo;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.OrdenCompra;
import com.sigad.sigad.business.Proveedor;
import com.sigad.sigad.business.ProveedorInsumo;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.helpers.InsumosHelper;
import com.sigad.sigad.business.helpers.LoteInsumoHelper;
import com.sigad.sigad.business.helpers.MovimientoHelper;
import com.sigad.sigad.business.helpers.OrdenCompraHelper;
import com.sigad.sigad.business.helpers.ProveedorHelper;
import com.sigad.sigad.business.helpers.TiendaHelper;
import com.sigad.sigad.business.helpers.TipoMovimientoHelper;
import com.sigad.sigad.insumos.controller.ListaInsumoController.InsumoViewer;
import java.net.URL;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author chrs
 */
public class CrearEditarOrdenCompraController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    public static String viewPath = "/com/sigad/sigad/ordenescompra/view/crearEditarOrdenCompra.fxml";
    
    @FXML
    private StackPane hiddenSp;
    
    @FXML
    private AnchorPane containerPane;

    @FXML
    private JFXComboBox<Proveedor> cbxProv;

    @FXML
    private JFXDatePicker pckDate;

    @FXML
    private JFXTreeTableView<InsumoViewerOrden> tblInsumos;
    
    
    @FXML
    private JFXTextField txtProveedor;
    
    public static OrdenCompra orden = null;
    static ObservableList<InsumoViewerOrden> insumosList;

    JFXTreeTableColumn<InsumoViewerOrden,String> nombreCol = new JFXTreeTableColumn<>("Nombre");
    JFXTreeTableColumn<InsumoViewerOrden,String> volumenCol = new JFXTreeTableColumn<>("Volumen");
    JFXTreeTableColumn<InsumoViewerOrden,String> precioCol = new JFXTreeTableColumn<>("Precio");
    JFXTreeTableColumn<InsumoViewerOrden,String> cantidadCol = new JFXTreeTableColumn<>("Cantidad");
    JFXTreeTableColumn<InsumoViewerOrden,String> subtotalCol = new JFXTreeTableColumn<>("Subtotal");
    
    
    public static class InsumoViewerOrden extends RecursiveTreeObject<InsumoViewerOrden>{

        public SimpleStringProperty getFechaVencimiento() {
            return fechaVencimiento;
        }

        public void setFechaVencimiento(String fechaVencimiento) {
            this.fechaVencimiento = new SimpleStringProperty(fechaVencimiento);
        }

        public SimpleStringProperty getNombre() {
            return nombre;
        }

        public SimpleStringProperty getVolumen() {
            return volumen;
        }

        public SimpleStringProperty getPrecio() {
            return precio;
        }

        public SimpleStringProperty getCantidad() {
            return cantidad;
        }

        public SimpleStringProperty getSubTotal() {
            return subTotal;
        }

        public Insumo getInsumoLocal() {
            return insumoLocal;
        }

        public void setNombre(SimpleStringProperty nombre) {
            this.nombre = nombre;
        }

        public void setVolumen(SimpleStringProperty volumen) {
            this.volumen = volumen;
        }

        public void setPrecio(SimpleStringProperty precio) {
            this.precio = precio;
        }

        public void setCantidad(String cantidad) {
            this.cantidad = new SimpleStringProperty(cantidad);
        }

        public void setSubTotal(String subtotal) {
            this.subTotal = new SimpleStringProperty(subtotal);
        }

        public void setInsumoLocal(Insumo insumoLocal) {
            this.insumoLocal = insumoLocal;
        }
        
        

        private SimpleStringProperty nombre;
        private SimpleStringProperty volumen;
        private SimpleStringProperty precio;
        private SimpleStringProperty cantidad;
        private SimpleStringProperty subTotal;
        private Insumo insumoLocal;
        private SimpleStringProperty recibido;
        private SimpleStringProperty fechaVencimiento;
        
        public InsumoViewerOrden(String nombre,String volumen,String precio,String cantidad,String subtotal) {
            this.nombre = new SimpleStringProperty(nombre);
            this.volumen = new SimpleStringProperty(volumen);            
            this.precio = new SimpleStringProperty(precio);
            this.cantidad = new SimpleStringProperty(cantidad);
            this.subTotal = new SimpleStringProperty(subtotal);
            //this.fechaVencimiento = new SimpleStringProperty(fecha);
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        insumosList = FXCollections.observableArrayList();
        setColumns();
        addColumns();
        if(!ListaOrdenesCompraController.isOrdenCreate){
            cbxProv.setVisible(false);
            txtProveedor.setVisible(true);
            tblInsumos.setDisable(true);
            loadFields();
        }
        else{
            cbxProv.setVisible(true);
            txtProveedor.setVisible(false);
            orden = new OrdenCompra();
            addDialogBtns();
        }
        fillData();
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
                
                Tienda currentStore = LoginController.user.getTienda();
                double capacidadTotal = currentStore.getCapacidad();
                
                double capacidadActual = 0.0;
                LoteInsumoHelper helperli = new LoteInsumoHelper();
                ArrayList<LoteInsumo> lotes = helperli.getLoteInsumos(currentStore);
                if(lotes!=null){
                    for (int i = 0; i < lotes.size(); i++) {
                        LoteInsumo next = lotes.get(i);
                        capacidadActual += next.getInsumo().getStockTotalFisico()* next.getInsumo().isVolumen();
                    }
                }


                for (int i = 0; i < insumosList.size(); i++) {
                    if(!insumosList.get(i).getCantidad().getValue().equals("")){
                        capacidadActual += Double.parseDouble(insumosList.get(i).getVolumen().getValue())* Double.parseDouble(insumosList.get(i).getCantidad().getValue());
                    }
                    
                }
                
                if(capacidadActual > capacidadTotal){
                    ErrorController error = new ErrorController();
                    error.loadDialog("Error", "No puede agregar " + (capacidadActual) + " porque supera la capacidad actual de la tienda:  " + (capacidadTotal), "Ok", hiddenSp);
                    return;
                }
                ArrayList <LoteInsumo> listaLotes = new ArrayList<>();
                if(listaLotes!= null){
                    insumosList.forEach((i)-> {
                        if(!i.getCantidad().getValue().equals("")){
                            LoteInsumo li = new LoteInsumo();
                            //setear datos lote insumo
                            li.setInsumo(i.insumoLocal);
                            
                            Date date =  new Date();
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);

                            c.add(Calendar.DATE, i.getInsumoLocal().getTiempoVida());
                            Date currentDatePlusOne = c.getTime();

                            li.setFechaVencimiento(currentDatePlusOne);
                            li.setCostoUnitario(Double.parseDouble(i.getPrecio().getValue()));
                            li.setStockFisico(0);
                            li.setStockLogico(Integer.parseInt(i.getCantidad().getValue()));
                            li.setTienda(currentStore);
                            listaLotes.add(li);
                        }
                    });
                }
                
                
                OrdenCompraHelper helper = new OrdenCompraHelper();
                //creacion
                if(ListaOrdenesCompraController.isOrdenCreate){                   
                    Integer id = helper.saveOrden(orden, listaLotes);
                    
                    MovimientoHelper helpermo= new MovimientoHelper();
                    TipoMovimientoHelper helpertm = new TipoMovimientoHelper();
                    OrdenCompraHelper helperoctemp = new OrdenCompraHelper();
                    ArrayList<DetalleOrdenCompra> detallesOrdenes = helperoctemp.getDetalles(id);
                    if(detallesOrdenes!=null){
                        for (int i = 0; i < detallesOrdenes.size(); i++) {

                            //registrar movimiento
                            MovimientosTienda movNew = new MovimientosTienda();
                            movNew.setCantidadMovimiento(detallesOrdenes.get(i).getLoteInsumo().getStockLogico());
                            movNew.setFecha(new Date());
                            movNew.setTienda(currentStore);
                            movNew.setTipoMovimiento(helpertm.getTipoMov(Constantes.TIPO_MOVIMIENTO_ENTRADA_LOGICA));
                            movNew.setTrabajador(LoginController.user);
                            movNew.setLoteInsumo(detallesOrdenes.get(i).getLoteInsumo());
                            helpermo.saveMovement(movNew);

                        }
                    }
                    
                    if(id != null){
                        ListaOrdenesCompraController.updateTable(orden);
                        ListaOrdenesCompraController.ordenDialog.close();
                    }else {
                        ErrorController error = new ErrorController();
                        error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
                    }
//                    Long id = helper.updateInsumo(insumo,null);
//                    if(id != null){
//                        ListaInsumoController.insumosList.remove(ListaInsumoController.selectedInsumo);
//                        ListaInsumoController.updateTable(insumo);
//                        ListaInsumoController.insumoDialog.close();
//                    }else{
//                        ErrorController error = new ErrorController();
//                        error.loadDialog("Error", helper.getErrorMessage(), "Ok", hiddenSp);
//                    }
                }
                helper.close();
            }
        });
        
        JFXButton cancel = new JFXButton("Cancelar");
        cancel.setPrefSize(80, 25);
        AnchorPane.setBottomAnchor(cancel, -20.0);
        AnchorPane.setRightAnchor(cancel, 85.0);
        cancel.setOnAction((ActionEvent event) -> {
            ListaOrdenesCompraController.ordenDialog.close();
            //PersonalController.getDataFromDB();
        });
        
        containerPane.getChildren().add(save);
        containerPane.getChildren().add(cancel);
    }
    
    private void loadFields(){
        
        OrdenCompraHelper helperor = new OrdenCompraHelper();
        orden = helperor.getOrden(Integer.parseInt(ListaOrdenesCompraController.selectedOrdenCompra.getCodigo().getValue()));
        
        if(orden != null) {
            txtProveedor.setText(orden.getProveedor().getNombre());
            pckDate.setValue(orden.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        helperor.close();
    }
    
    private void setColumns(){
        nombreCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewerOrden, String> param) -> param.getValue().getValue().getNombre() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        volumenCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewerOrden, String> param) -> param.getValue().getValue().getVolumen() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        precioCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewerOrden, String> param) -> param.getValue().getValue().getPrecio()
        );
        cantidadCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewerOrden, String> param) -> param.getValue().getValue().getCantidad() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        cantidadCol.setCellFactory((TreeTableColumn<InsumoViewerOrden, String> param) -> new EditingCell());
        
        cantidadCol.setOnEditCommit((TreeTableColumn.CellEditEvent<InsumoViewerOrden, String> event) -> {
            Integer i = insumosList.indexOf(event.getRowValue().getValue());
            insumosList.get(i).setCantidad(event.getNewValue());
            
//            //Double sub = Double.parseDouble(event.getRowValue().getValue().getPrecio().getValue())*Double.parseDouble(event.getNewValue());//                           
//            InsumoViewerOrden nuevo = new InsumoViewerOrden("gg", event.getRowValue().getValue().getVolumen().getValue(), 
//                    event.getRowValue().getValue().getPrecio().getValue(), event.getNewValue(), sub.toString());
//            
//            Integer index = insumosList.indexOf(event.getRowValue().getValue());
//            insumosList.remove(event.getRowValue().getValue());
//            insumosList.add(nuevo);
            String a = event.getRowValue().getValue().getPrecio().getValue();
            String d = event.getNewValue();
            Double sub = Double.parseDouble(a)*Double.parseDouble(d);
            insumosList.get(i).setSubTotal(sub.toString());
            
//            insumosList.forEach((o) -> {
//                        if(!o.getCantidad().getValue().equals("")){
//                            String a = event.getRowValue().getValue().getPrecio().getValue();
//                            String d = event.getNewValue();
//                            Double sub = Double.parseDouble(a)*Double.parseDouble(d);
//                            //System.out.println("gg" + sub);
//                            o.setSubTotal(sub.toString());
//                        }
//                        else {
//                            o.setSubTotal("");
//                        }
//                        System.out.println(o.getCantidad().getValue());
//                    });
        });
        
        subtotalCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewerOrden, String> param) -> param.getValue().getValue().getSubTotal()
        );
        

    }
    
    private void addColumns(){
        final TreeItem<InsumoViewerOrden> rootInsumo = new RecursiveTreeItem<>(insumosList,RecursiveTreeObject::getChildren);
        tblInsumos.setEditable(true);
        tblInsumos.getColumns().setAll(nombreCol,volumenCol,precioCol,cantidadCol,subtotalCol);
        tblInsumos.setRoot(rootInsumo);
        tblInsumos.setShowRoot(false);
    }
    
    private void fillData(){
                
        if(!ListaOrdenesCompraController.isOrdenCreate){
            OrdenCompraHelper helperor = new OrdenCompraHelper();
            
            ArrayList<DetalleOrdenCompra> listaDets = helperor.getDetalles(orden.getId());
            if(listaDets != null){
                listaDets.forEach((i)->{
                    Double pd = (Double) i.getPrecioDetalle();
                    updateTableView(i.getLoteInsumo(), pd.toString());
                });
            }
            helperor.close();
        }
        else{
            ProveedorHelper helperp = new ProveedorHelper();
            ArrayList<Proveedor> listaprov = helperp.getProveedores();
            cbxProv.setPromptText("Proveedores");
            cbxProv.setConverter(new StringConverter<Proveedor>() {
                Long id = null;
                String des = null;
                String ruc = null;
                @Override
                public String toString(Proveedor object) {
                    id = object.getId();
                    des = object.getDescripcion();
                    ruc = object.getRuc();
                    return object==null? "" : object.getNombre();
                }

                @Override
                public Proveedor fromString(String string) {
                    Proveedor pr= new Proveedor();
                    pr.setNombre(string);
                    pr.setId(id);
                    pr.setDescripcion(des);
                    pr.setRuc(ruc);
                    return pr;
                }
            });

            cbxProv.valueProperty().addListener((ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
                InsumosHelper helper = new InsumosHelper();
                Proveedor p = (Proveedor) newValue;
                ArrayList<ProveedorInsumo> pi = (ArrayList<ProveedorInsumo>) helper.getInsumoFromProveedor(p);

                if(pi!= null){
                    insumosList.clear();
                    pi.forEach((i)->{
                        updateTable(i,"");
                    });
                    helper.close();
                }
            });
            if(listaprov != null) {
                listaprov.forEach((p)-> {
                    cbxProv.getItems().add(p);
                });                
            }
            helperp.close();

            Date inputDate = new Date();
            LocalDate date = inputDate .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            pckDate.setValue(date);
        }
    }
    
    public static void updateTableView(LoteInsumo lote,String subtotal){
        Double cost = lote.getCostoUnitario();
        InsumoViewerOrden insumoOrd = new InsumoViewerOrden(lote.getInsumo().getNombre(),lote.getInsumo().isVolumen().toString(),
                                                            cost.toString(), lote.getStockLogico().toString(),subtotal);
        insumoOrd.setInsumoLocal(lote.getInsumo());
        insumosList.add(insumoOrd);
    }
    public static void updateTable(ProveedorInsumo provinsumo,String cant){
        String subtotal;
        
        if(!cant.equals("")) {
            Double sub = (provinsumo.getPrecio() * Double.parseDouble(cant));
            subtotal = sub.toString();
        }
        else {
            subtotal = "0";
        }
        
        InsumoViewerOrden insumoOrd = new InsumoViewerOrden(provinsumo.getInsumo().getNombre(),provinsumo.getInsumo().isVolumen().toString(), 
                                           provinsumo.getPrecio().toString(), cant, subtotal);
        insumoOrd.setInsumoLocal(provinsumo.getInsumo());
        insumosList.add(insumoOrd);
    }
    

    private void fillFields(){
        orden.setRecibido(false);
        orden.setUsuario(LoginController.user);
        orden.setProveedor(cbxProv.getValue());
        orden.setFecha(new Date());
        double total = 0;
        int size = insumosList.size();
        for (int i = 0; i < size; i++) {
            total += Double.parseDouble(insumosList.get(i).getSubTotal().getValue());
        }
        
        orden.setPrecioTotal(total);
        
        //anadir cantidad de insumos por
    }
    
    public boolean validateFields() {
        // Validar que exista al menos un proveedor seleccionado
        Integer count = 0;
        for (int i = 0; i < insumosList.size(); i++) {
            if(!insumosList.get(i).getCantidad().getValue().equals("")){
                count += 1;
            }
        }
        if(count == 0){
            ErrorController error = new ErrorController();
            error.loadDialog("Error", "Debe seleccionar al menos un insumo", "Ok", hiddenSp);
            return false;
        }
        else 
            return true;
    }
    
    class EditingCell extends TreeTableCell<InsumoViewerOrden, String> {

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
            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        if (isNumeric(textField.getText())) {
                            commitEdit(textField.getText());
                        }

                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
    
    class EditingCellText extends TreeTableCell<InsumoViewerOrden, String> {

        private JFXTextField textField;

        public EditingCellText() {
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
                    NumberValidator n = new NumberValidator();
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
                    commitEdit(textField.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
    
    public boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            // s is not numeric
            return false;
        }
    }
    
}
