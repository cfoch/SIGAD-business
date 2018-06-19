/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.movimientos.controller;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.helpers.MovimientoHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class MovimientosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/movimientos/view/movimientos.fxml";
    public static String windowName = "Movimientos de Tienda";
    
    @FXML
    private JFXTreeTableView<Movement> movimientosTbl;
    static ObservableList<Movement> data;
    
    /*Extras*/
    @FXML
    public static StackPane movementDialog;
    @FXML
    private JFXTextField filtro;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        initMovementTbl();
    }    
    
    public void agregarFiltro() {
        filtro.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            movimientosTbl.setPredicate((TreeItem<Movement> t) -> {
                Boolean flag = t.getValue().id.getValue().contains(newValue) || t.getValue().fecha.getValue().contains(newValue) || t.getValue().cantidadMovimiento.getValue().contains(newValue) || t.getValue().dirección.getValue().contains(newValue) || t.getValue().idLoteInsumo.getValue().contains(newValue) || t.getValue().idTienda.getValue().contains(newValue) || t.getValue().insumo.getValue().contains(newValue) || t.getValue().movimiento.getValue().contains(newValue) || t.getValue().usuario.getValue().contains(newValue);
                return flag;
            });
        });
    }
    private void initMovementTbl() {
        JFXTreeTableColumn<Movement, String> id = new JFXTreeTableColumn<>("Id");
        id.setPrefWidth(70);
        id.setCellValueFactory((TreeTableColumn.CellDataFeatures<Movement, String> param) -> param.getValue().getValue().id);
        
        JFXTreeTableColumn<Movement, String> cantMovimiento = new JFXTreeTableColumn<>("Cantidad Movimiento");
        cantMovimiento.setPrefWidth(70);
        cantMovimiento.setCellValueFactory((TreeTableColumn.CellDataFeatures<Movement, String> param) -> param.getValue().getValue().cantidadMovimiento);
        
        JFXTreeTableColumn<Movement, String> fecha = new JFXTreeTableColumn<>("Fecha");
        fecha.setPrefWidth(70);
        fecha.setCellValueFactory((TreeTableColumn.CellDataFeatures<Movement, String> param) -> param.getValue().getValue().fecha);
        
        JFXTreeTableColumn<Movement, String> idLoteInsumo = new JFXTreeTableColumn<>("Id LoteInsumo");
        idLoteInsumo.setPrefWidth(70);
        idLoteInsumo.setCellValueFactory((TreeTableColumn.CellDataFeatures<Movement, String> param) -> param.getValue().getValue().idLoteInsumo);
        
        JFXTreeTableColumn<Movement, String> insumo = new JFXTreeTableColumn<>("Insumo");
        insumo.setPrefWidth(70);
        insumo.setCellValueFactory((TreeTableColumn.CellDataFeatures<Movement, String> param) -> param.getValue().getValue().insumo);
        
        JFXTreeTableColumn<Movement, String> idTienda = new JFXTreeTableColumn<>("Id Tienda");
        idTienda.setPrefWidth(70);
        idTienda.setCellValueFactory((TreeTableColumn.CellDataFeatures<Movement, String> param) -> param.getValue().getValue().idTienda);
        
        JFXTreeTableColumn<Movement, String> tienda = new JFXTreeTableColumn<>("Tienda");
        tienda.setPrefWidth(70);
        tienda.setCellValueFactory((TreeTableColumn.CellDataFeatures<Movement, String> param) -> param.getValue().getValue().dirección);
        
        JFXTreeTableColumn<Movement, String> tipoMovimiento = new JFXTreeTableColumn<>("Tipo Movimiento");
        tipoMovimiento.setPrefWidth(70);
        tipoMovimiento.setCellValueFactory((TreeTableColumn.CellDataFeatures<Movement, String> param) -> param.getValue().getValue().movimiento);
        
        
        JFXTreeTableColumn<Movement, String> usuario = new JFXTreeTableColumn<>("Usuario");
        usuario.setPrefWidth(70);
        usuario.setCellValueFactory((TreeTableColumn.CellDataFeatures<Movement, String> param) -> param.getValue().getValue().usuario);
        
        movimientosTbl.getColumns().add(id);
        movimientosTbl.getColumns().add(cantMovimiento);
        movimientosTbl.getColumns().add(fecha);
        movimientosTbl.getColumns().add(idLoteInsumo);
        movimientosTbl.getColumns().add(insumo);
        movimientosTbl.getColumns().add(idTienda);
        movimientosTbl.getColumns().add(tienda);
        movimientosTbl.getColumns().add(tipoMovimiento);
        movimientosTbl.getColumns().add(usuario);
        
        //DB
        getDataFromDB();
        
        final TreeItem<Movement> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        
        movimientosTbl.setEditable(true);
        movimientosTbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);        
        movimientosTbl.getColumns().setAll(id,cantMovimiento,fecha, idLoteInsumo, insumo, idTienda, tienda, tipoMovimiento, usuario);
        movimientosTbl.setRoot(root);
        movimientosTbl.setShowRoot(false);
    }

    private void getDataFromDB() {
        MovimientoHelper helper;
        if(LoginController.user.getTienda() == null){
            helper = new MovimientoHelper();
        
            ArrayList<MovimientosTienda> lista = helper.getMovements();
            if(lista != null){
                lista.forEach((p) -> {
                    updateTable(p);
                });
            }
        }else{
            helper = new MovimientoHelper();
        
            ArrayList<MovimientosTienda> lista = helper.getMovements(LoginController.user.getTienda());
            if(lista != null){
                lista.forEach((p) -> {
                    updateTable(p);
                });
            }
        }
        helper.close();
    }

    private void updateTable(MovimientosTienda p) {
        String fullname = p.getTrabajador().getNombres() + " " + p.getTrabajador().getApellidoPaterno() + " " + p.getTrabajador().getApellidoMaterno();
        data.add(
                new Movement(
                        new SimpleStringProperty(p.getId().toString()),
                        new SimpleStringProperty(p.getCantidadMovimiento().toString()),
                        new SimpleStringProperty(p.getFecha().toString()),
                        new SimpleStringProperty(p.getLoteInsumo().getId().toString()),
                        new SimpleStringProperty(p.getLoteInsumo().getInsumo().getNombre()),
                        new SimpleStringProperty(p.getTienda().getId().toString()),
                        new SimpleStringProperty(p.getTienda().getDireccion()),
                        new SimpleStringProperty(p.getTipoMovimiento().getNombre()),
                        new SimpleStringProperty(fullname)
                ));
    }

    /*Class for table*/
    private static class Movement extends RecursiveTreeObject<Movement>{
        StringProperty id;
        StringProperty cantidadMovimiento;
        StringProperty fecha;
        StringProperty idLoteInsumo;
        StringProperty insumo;
        StringProperty idTienda;
        StringProperty dirección;
        StringProperty movimiento;
        StringProperty usuario;

        public Movement(StringProperty id, StringProperty cantidadMovimiento, StringProperty fecha, StringProperty idLoteInsumo, StringProperty insumo, StringProperty idTienda, StringProperty dirección, StringProperty movimiento, StringProperty usuario) {
            this.id = id;
            this.cantidadMovimiento = cantidadMovimiento;
            this.fecha = fecha;
            this.idLoteInsumo = idLoteInsumo;
            this.insumo = insumo;
            this.idTienda = idTienda;
            this.dirección = dirección;
            this.movimiento = movimiento;
            this.usuario = usuario;
        }
        
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof Movement) {
                Movement u = (Movement) o;
                return u.id.equals(id);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.id);
            return hash;
        }
    }
    
}
