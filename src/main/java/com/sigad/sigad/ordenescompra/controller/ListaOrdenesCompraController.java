/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.ordenescompra.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.business.OrdenCompra;
import com.sigad.sigad.deposito.controller.FXMLAlmacenIngresoListaOrdenCompraController.OrdenCompraViewer;
import com.sigad.sigad.deposito.helper.DepositoHelper;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author chrs
 */
public class ListaOrdenesCompraController implements Initializable {

    public static final String viewPath = "/com/sigad/sigad/ordenescompra/view/ListaOrdenesCompra.fxml";
    /**
     * Initializes the controller class.
     */
    @FXML
    private StackPane hiddenSp;

    @FXML
    private JFXTreeTableView<OrdenCompraViewer> tblOrdenesCompra;

    @FXML
    private JFXButton moreBtn;

    @FXML
    private JFXButton btnAdd;
    
    public static JFXDialog ordenDialog;
    
    public static boolean isOrdenCreate;
    
    JFXTreeTableColumn<OrdenCompraViewer,String> codCol = new JFXTreeTableColumn<>("Codigo");
    JFXTreeTableColumn<OrdenCompraViewer,String> fechaCol = new JFXTreeTableColumn<>("Fecha Compra");
    JFXTreeTableColumn<OrdenCompraViewer,Number> precioCol = new JFXTreeTableColumn<>("Precio");
    static ObservableList<OrdenCompraViewer> ordenes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ordenes = FXCollections.observableArrayList();
        setColumns();
        addColumns();
        //fillData();
        // TODO        
        DepositoHelper depositoHelper = new DepositoHelper();
        ArrayList<OrdenCompra> ordenesBD= (ArrayList<OrdenCompra>)depositoHelper.getOrdenes();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
        ordenesBD.forEach((orden)->{
            OrdenCompraViewer ordenViewer = new OrdenCompraViewer(Integer.toString(orden.getId()),sdf.format(orden.getFecha()),orden.getPrecioTotal());
            ordenes.add(ordenViewer);
            System.out.println(orden.getId() + " " + orden.getPrecioTotal() + " " + orden.getFecha());
            
        });
    }
    private void setColumns() {
        codCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<OrdenCompraViewer, String> param) -> 
                param.getValue().getValue().getCodigo() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );               
        fechaCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<OrdenCompraViewer, String> param) -> 
                param.getValue().getValue().getFecha() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );        
        precioCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<OrdenCompraViewer, Number> param) -> 
                param.getValue().getValue().getPrecio() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        //Double click on row
        tblOrdenesCompra.setRowFactory(ord -> {
            JFXTreeTableRow<OrdenCompraViewer> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked((event) -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                     && event.getClickCount() == 2) {
                    OrdenCompraViewer clickedRow = row.getItem();
                    System.out.println(clickedRow.getCodigo().getValue());
//                    try {
////                        selectedOrder = clickedRow;
////                        AttendOrder(clickedRow);                    
//                        
//                    } catch (IOException ex) {
//                        Logger.getLogger(ListaOrdenesCompraController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }
                
            });
            return row;
        });
    }
    
    private void addColumns(){
        final TreeItem<OrdenCompraViewer> root = new RecursiveTreeItem<>(ordenes,RecursiveTreeObject::getChildren);
        tblOrdenesCompra.getColumns().setAll(codCol,fechaCol,precioCol);
        tblOrdenesCompra.setRoot(root);
        tblOrdenesCompra.setShowRoot(false);
        
    }
    
    public static void updateTable(OrdenCompra orden) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ordenes.add(new OrdenCompraViewer(Long.toString(orden.getId()),df.format(orden.getFecha()),orden.getPrecioTotal()));
    }
    
    @FXML
    void handleAction(ActionEvent event) {
        if(event.getSource() == btnAdd ) {
            try {
                CreateEditUserDialog(true);
            } catch (IOException ex) {
                
            }
        }
    }
    
    public void CreateEditUserDialog(boolean iscreate) throws IOException {
        isOrdenCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        
        if(isOrdenCreate ){
            content.setHeading(new Text("Crear Usuario"));
        }else{
            content.setHeading(new Text("Editar Usuario"));
            
//            UsuarioHelper helper = new UsuarioHelper();
//            Usuario u = helper.getUser(PersonalController.selectedUser.correo.getValue());
//            if(u == null){
//                ErrorController error = new ErrorController();
//                error.loadDialog("Error", "No se pudo obtener el usuario", "Ok", hiddenSp);
//                System.out.println("Error al obtener el usuario");
//                return;
//            }
        }
        
        Node node;
        node = (Node) FXMLLoader.load(ListaOrdenesCompraController.this.getClass().getResource(CrearEditarOrdenCompraController.viewPath));
        content.setBody(node);
        //content.setPrefSize(400,400);
                
        ordenDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        ordenDialog.show();
    }  
    
    
}
