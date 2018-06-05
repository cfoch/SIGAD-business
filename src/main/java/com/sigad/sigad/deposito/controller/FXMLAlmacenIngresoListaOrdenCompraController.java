/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.deposito.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.business.OrdenCompra;
import com.sigad.sigad.deposito.helper.DepositoHelper;
import com.sigad.sigad.ordenescompra.controller.ListaOrdenesCompraController.OrdenCompraViewer;
import java.beans.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author chrs
 */
public class FXMLAlmacenIngresoListaOrdenCompraController implements Initializable {
    /**
     * Initializes the controller class.
     */
    
    public static final String viewPath="/com/sigad/sigad/deposito/view/FXMLAlmacenIngresoListaOrdenCompra.fxml";
    @FXML
    private JFXTreeTableView<OrdenCompraViewer> tblAlmacenOrdenesCompra;
    @FXML
    private JFXButton moreBtn;
    @FXML
    private JFXPopup popup;
    @FXML
    private StackPane hiddenSp;
    @FXML
    public static JFXDialog attendOrder;
    
    public static OrdenCompraViewer selectedOrder = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        JFXTreeTableColumn<OrdenCompraViewer,String> codCol = new JFXTreeTableColumn<>("Codigo");
        codCol.setPrefWidth(425);
        codCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<OrdenCompraViewer, String> param) -> param.getValue().getValue().getCodigo() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        
        JFXTreeTableColumn<OrdenCompraViewer,String> fechaCol = new JFXTreeTableColumn<>("Fecha Compra");
        fechaCol.setPrefWidth(200);
        fechaCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<OrdenCompraViewer, String> param) -> param.getValue().getValue().getFecha() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        JFXTreeTableColumn<OrdenCompraViewer,Number> precioCol = new JFXTreeTableColumn<>("Precio");
        precioCol.setPrefWidth(200);
        precioCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<OrdenCompraViewer, Number> param) -> param.getValue().getValue().getPrecio() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        
        //Double click on row
        tblAlmacenOrdenesCompra.setRowFactory(ord -> {
            JFXTreeTableRow<OrdenCompraViewer> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked((event) -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                     && event.getClickCount() == 2) {
                    OrdenCompraViewer clickedRow = row.getItem();
                    System.out.println(clickedRow.getCodigo().getValue());
                    try {
                        selectedOrder = clickedRow;
                        AttendOrder(clickedRow);                    
                        
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLAlmacenIngresoListaOrdenCompraController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return row;
        });        
        
        ObservableList<OrdenCompraViewer> ordenes = FXCollections.observableArrayList();
           
        final TreeItem<OrdenCompraViewer> root = new RecursiveTreeItem<>(ordenes,RecursiveTreeObject::getChildren);
        tblAlmacenOrdenesCompra.getColumns().setAll(codCol,fechaCol,precioCol);
        tblAlmacenOrdenesCompra.setRoot(root);
        tblAlmacenOrdenesCompra.setShowRoot(false);
        DepositoHelper depositoHelper = new DepositoHelper();
        List<OrdenCompra> ordenesBD= depositoHelper.getOrdenes();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
        ordenesBD.forEach((orden)->{
            OrdenCompraViewer ordenViewer = new OrdenCompraViewer(Integer.toString(orden.getId()),sdf.format(orden.getFecha()),orden.getPrecioTotal());
            ordenes.add(ordenViewer);
            System.out.println(orden.getId() + " " + orden.getPrecioTotal() + " " + orden.getFecha());
            
        });
    }
    
    private void AttendOrder(OrdenCompraViewer ord) throws IOException {

        DepositoHelper dephelper = new DepositoHelper();
        //System.out.println(selectedOrder.getCodigo().getValue());
        OrdenCompra orden = dephelper.getOrden(Integer.parseInt(selectedOrder.getCodigo().getValue()));
        //System.out.println(orden.getId()+ " " + orden.getProveedor().getNombre());
        
        JFXDialogLayout content =  new JFXDialogLayout();
        content.setHeading(new Text("Orden de compra" + orden.getId()));
        
        FXMLAlmacenIngresoDetalleOrdenCompraController.ordenElegida = orden;
        Node node = (Node) FXMLLoader.load(FXMLAlmacenIngresoListaOrdenCompraController.this.getClass().getResource(FXMLAlmacenIngresoDetalleOrdenCompraController.viewPath));
        content.setBody(node);

        attendOrder = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        attendOrder.show();
                       
    } 
    
}
