/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.ordenescompra.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.OrdenCompra;
import com.sigad.sigad.business.helpers.OrdenCompraHelper;
import com.sigad.sigad.deposito.helper.DepositoHelper;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private JFXButton addBtn;
    
    @FXML
    public static JFXDialog ordenDialog;
    
    @FXML
    private JFXPopup popup;
    
    public static boolean isOrdenCreate;
    
    public static OrdenCompraViewer selectedOrdenCompra = null;
    
    JFXTreeTableColumn<OrdenCompraViewer,String> codCol = new JFXTreeTableColumn<>("Codigo");
    JFXTreeTableColumn<OrdenCompraViewer,String> fechaCol = new JFXTreeTableColumn<>("Fecha Compra");
    JFXTreeTableColumn<OrdenCompraViewer,Number> precioCol = new JFXTreeTableColumn<>("Precio");
    static ObservableList<OrdenCompraViewer> ordenesList;
    
    public static class OrdenCompraViewer extends RecursiveTreeObject<OrdenCompraViewer>{

        /**
         * @return the codigo
         */
        public SimpleStringProperty getCodigo() {
            return codigo;
        }

        /**
         * @param codigo the codigo to set
         */
        public void setCodigo(SimpleStringProperty codigo) {
            this.codigo = codigo;
        }

        /**
         * @return the fecha
         */
        public SimpleStringProperty getFecha() {
            return fecha;
        }

        /**
         * @param fecha the fecha to set
         */
        public void setFecha(SimpleStringProperty fecha) {
            this.fecha = fecha;
        }

        /**
         * @return the precio
         */
        public SimpleDoubleProperty getPrecio() {
            return precio;
        }

        /**
         * @param precio the precio to set
         */
        public void setPrecio(Double precio) {
            this.precio = new SimpleDoubleProperty(precio);
        }
        private SimpleStringProperty codigo;
        private SimpleStringProperty fecha;
        private SimpleDoubleProperty  precio;
        
        public OrdenCompraViewer(String codigo,String fecha,Double precio){
            this.codigo = new SimpleStringProperty(codigo);
            this.fecha = new SimpleStringProperty(fecha);
            this.precio = new SimpleDoubleProperty(precio);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ordenesList = FXCollections.observableArrayList();
        setColumns();
        addColumns();
        fillData();
        // TODO        
        OrdenCompraHelper helpero = new OrdenCompraHelper();
        ArrayList<OrdenCompra> ordenesBD= (ArrayList<OrdenCompra>) helpero.getOrdenes();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
//        ordenesBD.forEach((orden)->{
//            OrdenCompraViewer ordenViewer = new OrdenCompraViewer(Integer.toString(orden.getId()),sdf.format(orden.getFecha()),orden.getPrecioTotal());
//            ordenes.add(ordenViewer);
//            System.out.println(orden.getId() + " " + orden.getPrecioTotal() + " " + orden.getFecha());
//            
//        });
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
        final TreeItem<OrdenCompraViewer> root = new RecursiveTreeItem<>(ordenesList,RecursiveTreeObject::getChildren);
        tblOrdenesCompra.getColumns().setAll(codCol,fechaCol,precioCol);
        tblOrdenesCompra.setRoot(root);
        tblOrdenesCompra.setShowRoot(false);
        
    }
    
    private void fillData() {
        OrdenCompraHelper helpero = new OrdenCompraHelper();
        ArrayList<OrdenCompra> listaOrdenes= helpero.getOrdenes();
        if(listaOrdenes != null){
            listaOrdenes.forEach((i)->{
                updateTable(i);
            });
        }
    }
    public static void updateTable(OrdenCompra orden) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ordenesList.add(new OrdenCompraViewer(Long.toString(orden.getId()),df.format(orden.getFecha()),orden.getPrecioTotal()));
    }
    
    @FXML
    void handleAction(ActionEvent event) {
        if(event.getSource() == addBtn ) {
            try {
                createEditOrdenDialog(true);
            } catch (IOException ex) {
                Logger.getLogger(ListaOrdenesCompraController.class.getName()).log(Level.SEVERE,"CreateEditUserDialog()",ex);
            }
        }
        else if(event.getSource() == moreBtn) {
            int count = tblOrdenesCompra.getSelectionModel().getSelectedCells().size();
            if( count > 1) {
                ErrorController error = new  ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla", "OK", hiddenSp);
            }
            else if( count <= 0 ) {
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "OK", hiddenSp);
            }
            else {
                int selected = tblOrdenesCompra.getSelectionModel().getSelectedIndex();
                selectedOrdenCompra = (OrdenCompraViewer) tblOrdenesCompra.getSelectionModel().getModelItem(selected).getValue();
                showOptions();
                popup.show(moreBtn,JFXPopup.PopupVPosition.TOP,JFXPopup.PopupHPosition.RIGHT);
            }
        }
    }
    
    //dialogos
    private void showOptions(){
        JFXButton edit = new JFXButton("Editar");
        JFXButton delete = new JFXButton("Eliminar");
        
        edit.setOnAction((ActionEvent event) -> {
            popup.hide();
            try {
                createEditOrdenDialog(false);
            } catch (IOException ex) {
                Logger.getLogger(ListaOrdenesCompraController.class.getName()).log(Level.SEVERE, "initPopup(): createEditOrdenDialog()", ex);
            }
        });
        
        delete.setOnAction((ActionEvent event) -> {
            popup.hide();
            ////VALIDAR QUE NO SE PUEDA ELIMINAR SI YA ESTA EN ESTADO DESPACHADA
            deleteOrdenesDialog();
        });
        
        edit.setPadding(new Insets(20));
        edit.setPrefSize(145, 40);
        delete.setPadding(new Insets(20));
        delete.setPrefSize(145, 40);
        
        VBox vBox = new VBox(edit, delete);
        
        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }
    
    private void deleteOrdenesDialog() {
        JFXDialogLayout content =  new JFXDialogLayout();
        content.setHeading(new Text("Eliminar Orden"));
        content.setBody(new Text("¿Seguro que desea eliminar la orden seleccionada?"));
                
        JFXDialog dialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        content.setActions(button);
        dialog.show();
    }
    
    public void createEditOrdenDialog(boolean iscreate) throws IOException {
        isOrdenCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        
        if(isOrdenCreate){
            content.setHeading(new Text("Crear Orden"));
        }else{
            content.setHeading(new Text("Editar Orden"));
        }
        
        Node node;
        node = (Node) FXMLLoader.load(ListaOrdenesCompraController.this.getClass().getResource(CrearEditarOrdenCompraController.viewPath));
        content.setBody(node);
        
        ordenDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        ordenDialog.show();
    }  
    
    
}
