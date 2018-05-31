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
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.DetalleOrdenCompra;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.OrdenCompra;
import com.sigad.sigad.business.Proveedor;
import com.sigad.sigad.business.helpers.InsumosHelper;
import com.sigad.sigad.business.helpers.OrdenCompraHelper;
import com.sigad.sigad.business.helpers.ProveedorHelper;
import com.sigad.sigad.insumos.controller.ListaInsumoController.InsumoViewer;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
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
    private JFXComboBox<Proveedor> provCbx;

    @FXML
    private JFXDatePicker datePick;

    @FXML
    private JFXTreeTableView<InsumoViewer> tblInsumos;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXTextField totalTxt;
    
    public static OrdenCompra orden = null;
    static ObservableList<InsumoViewer> insumosList;

    JFXTreeTableColumn<InsumoViewer,Boolean> selectCol = new JFXTreeTableColumn<>("Seleccionar");
    JFXTreeTableColumn<InsumoViewer,String> nombreCol = new JFXTreeTableColumn<>("Nombre");
    JFXTreeTableColumn<InsumoViewer,String> stockCol = new JFXTreeTableColumn<>("Stock Total");
    JFXTreeTableColumn<InsumoViewer,String> volumenCol = new JFXTreeTableColumn<>("Volumen");
    JFXTreeTableColumn<InsumoViewer,Integer> cantidadOrden = new JFXTreeTableColumn<>("Cantidad");
    
    Date inputDate = new Date();
    LocalDate date = inputDate .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        insumosList = FXCollections.observableArrayList();
        orden = new OrdenCompra();
        setColumns();
        addColumns();
        fillData();
        
    }
    
    private void setColumns(){
        selectCol.setPrefWidth(80);
        selectCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, Boolean> param) -> param.getValue().getValue().getSeleccion() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        selectCol.setCellFactory((TreeTableColumn<InsumoViewer,Boolean>param) -> {
            CheckBoxTreeTableCell<InsumoViewer,Boolean> cell = new CheckBoxTreeTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell; //To change body of generated lambdas, choose Tools | Templates.
        });
        nombreCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getNombre() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        stockCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getStockTotal() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        volumenCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getVolumen() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        cantidadOrden.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, Integer> param) -> param.getValue().getValue().getCantidad().asObject() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        cantidadOrden.setCellFactory((TreeTableColumn<InsumoViewer, Integer> param) -> new EditingCell());
        
        cantidadOrden.setOnEditCommit((TreeTableColumn.CellEditEvent<InsumoViewer, Integer> event) -> {
            Integer i = insumosList.indexOf(event.getRowValue().getValue());
            insumosList.get(i).setCantidad(event.getNewValue());
        });
       
    }
    
    private void addColumns(){
        final TreeItem<InsumoViewer> rootInsumo = new RecursiveTreeItem<>(insumosList,RecursiveTreeObject::getChildren);
        tblInsumos.setEditable(true);
        tblInsumos.getColumns().setAll(nombreCol,stockCol,volumenCol,cantidadOrden);
        tblInsumos.setRoot(rootInsumo);
        tblInsumos.setShowRoot(false);
    }
    
    private void fillData(){
        InsumosHelper helper = new InsumosHelper();
        ArrayList<Insumo> listaInsumos = helper.getInsumos();
        if(listaInsumos != null){
            listaInsumos.forEach((i)-> {
                updateTable(i);
            });
        }
        helper.close();
        
        ProveedorHelper helperp = new ProveedorHelper();
        ArrayList<Proveedor> listaprov = helperp.getProveedores();
        if(listaprov != null) {
            listaprov.forEach((p)-> {
                provCbx.getItems().add(p);
            });
            provCbx.setPromptText("Seleccionar proveedor");
            provCbx.setConverter(new StringConverter<Proveedor>() {
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
        }
        helper.close();
        
        datePick.setValue(date);
    }
    
    public static void updateTable(Insumo insumo){
        insumosList.add(new InsumoViewer(insumo.getNombre(),
                                         insumo.getDescripcion(),
                                         Integer.toString(insumo.getTiempoVida()),
                                         Integer.toString(insumo.getStockTotalFisico()),
                                         insumo.isActivo(),
                                         Double.toString(insumo.isVolumen()),
                                         insumo.getImagen(),0));
    }
    
    @FXML
    void handleAction(ActionEvent event) {
        if(event.getSource() == btnCancelar){
            ListaOrdenesCompraController.ordenDialog.close();
        }
        if(event.getSource() == btnGuardar) {
            OrdenCompraHelper helpero = new OrdenCompraHelper();
            OrdenCompra newOrd = new OrdenCompra();
            newOrd.setFecha(inputDate);
            newOrd.setProveedor(provCbx.getSelectionModel().getSelectedItem());
            newOrd.setUsuario(LoginController.user);
            /*Integer id  = helpero.saveOrden(newOrd);
            if( id!=null){
                ListaOrdenesCompraController.updateTable(newOrd);
            }*/
            helpero.close();
            ListaOrdenesCompraController.ordenDialog.close();
        }
    }

    private void fillFields(){
        orden.setFecha(Date.from(datePick.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        orden.setPrecioTotal(0);
        orden.setUsuario(LoginController.user);
        
        //anadir cantidad de insumos por
        
    }
    
    class EditingCell extends TreeTableCell<InsumoViewer, Integer> {

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
        public void updateItem(Integer item, boolean empty) {
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
                            commitEdit(Integer.parseInt(textField.getText()));
                            insumosList.forEach((o) -> {
                                System.out.println(o.getCantidad().getValue());
                            });
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
