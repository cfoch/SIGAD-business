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
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.DetalleOrdenCompra;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.LoteInsumo;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.OrdenCompra;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.helpers.InsumosHelper;
import com.sigad.sigad.business.helpers.LoteInsumoHelper;
import com.sigad.sigad.business.helpers.MovimientoHelper;
import com.sigad.sigad.business.helpers.OrdenCompraHelper;
import com.sigad.sigad.business.helpers.TipoMovimientoHelper;
import com.sigad.sigad.deposito.helper.DepositoHelper;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
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
    JFXTreeTableColumn<OrdenCompraViewer,String> recibidoCol = new JFXTreeTableColumn<>("Estado");
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
        private SimpleStringProperty recibido;
        private SimpleDoubleProperty  precio;
        private OrdenCompra ordenCompra;
        
        public OrdenCompraViewer(String codigo,String fecha,Double precio,Boolean recibido){
            this.codigo = new SimpleStringProperty(codigo);
            this.fecha = new SimpleStringProperty(fecha);
            this.precio = new SimpleDoubleProperty(precio);
            this.recibido = new SimpleStringProperty(recibido? "Recibido": "Creado");
        }

        public OrdenCompra getOrdenCompra() {
            return ordenCompra;
        }

        public void setOrdenCompra(OrdenCompra ordenCompra) {
            this.ordenCompra = ordenCompra;
        }
        public SimpleStringProperty getRecibido() {
            return recibido;
        }

        public void setRecibido(String recibido) {
            this.recibido = new SimpleStringProperty(recibido);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ordenesList = FXCollections.observableArrayList();
        setColumns();
        addColumns();
        fillData();

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
                    try {
                        createEditOrdenDialog(false);
                    } catch (IOException ex) {
                        Logger.getLogger(ListaOrdenesCompraController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return row;
        });
        recibidoCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<OrdenCompraViewer, String> param) -> param.getValue().getValue().getRecibido()
        );
    }
    
    private void addColumns(){
        final TreeItem<OrdenCompraViewer> root = new RecursiveTreeItem<>(ordenesList,RecursiveTreeObject::getChildren);
        tblOrdenesCompra.getColumns().setAll(codCol,fechaCol,precioCol,recibidoCol);
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
        helpero.close();
    }
    public static void updateTable(OrdenCompra orden) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        OrdenCompraViewer ordev =new OrdenCompraViewer(Long.toString(orden.getId()),df.format(orden.getFecha()),orden.getPrecioTotal(),orden.isRecibido());
        ordev.setOrdenCompra(orden);
        ordenesList.add(ordev);
    }
    
    @FXML
    void handleAction(ActionEvent event) {
        if(event.getSource() == addBtn) {
            Tienda currentStore = LoginController.user.getTienda();
            if(currentStore == null){
                ErrorController error = new ErrorController();
                error.loadDialog("Error","No puede crear una orden de compra si es superAdmin","OK", hiddenSp);
                return;
            }
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
        JFXButton io = new JFXButton("Ingreso");
        JFXButton delete = new JFXButton("Eliminar");
        
        io.setOnAction((ActionEvent event) -> {
            popup.hide();
            Tienda currentStore = LoginController.user.getTienda();
            double capacidadTotal = currentStore.getCapacidad();

            double capacidadActual = 0.0;
            LoteInsumoHelper helperli = new LoteInsumoHelper();
            ArrayList<LoteInsumo> lotes = helperli.getLoteInsumos(currentStore);
            for (int i = 0; i < lotes.size(); i++) {
                LoteInsumo next = lotes.get(i);
                capacidadActual += next.getInsumo().getStockTotalFisico()* next.getInsumo().isVolumen();
            }

            OrdenCompraHelper helperoc = new OrdenCompraHelper();
            ArrayList<DetalleOrdenCompra> detalle = helperoc.getDetalles(selectedOrdenCompra.getOrdenCompra().getId());
            if(detalle!= null){
                for (int i = 0; i < detalle.size(); i++) {
                    DetalleOrdenCompra next = detalle.get(i);
                    capacidadActual += next.getLoteInsumo().getInsumo().isVolumen() * next.getLoteInsumo().getStockLogico();
                }
            }
            
            if(capacidadActual > capacidadTotal){
                ErrorController error = new ErrorController();
                error.loadDialog("Error", "No puede agregar " + (capacidadActual) + " porque supera la capacidad actual de la tienda es " + (capacidadTotal), "Ok", hiddenSp);
                return;
            }
            
            
            selectedOrdenCompra.getOrdenCompra().setRecibido(true);
            selectedOrdenCompra.recibido.set("Recibido");
            
            helperoc.updateOrdenCompra(selectedOrdenCompra.getOrdenCompra());
            helperoc.close();
            
            int index = tblOrdenesCompra.getSelectionModel().getSelectedIndex();
            ordenesList.remove(index);
            ordenesList.add(index, selectedOrdenCompra);
            
            InsumosHelper helperi = new InsumosHelper();
            OrdenCompraHelper helperoctemp = new OrdenCompraHelper();
            MovimientoHelper helpermo= new MovimientoHelper();
            TipoMovimientoHelper helpertm = new TipoMovimientoHelper();
            ArrayList<DetalleOrdenCompra> detallesOrdenes = helperoctemp.getDetalles(selectedOrdenCompra.getOrdenCompra().getId());
            if(detallesOrdenes!=null){
                for (int i = 0; i < detallesOrdenes.size(); i++) {
                
                    //stock lote
                    LoteInsumo loteNew = detallesOrdenes.get(i).getLoteInsumo();
                    loteNew.setStockFisico(loteNew.getStockLogico());
                    helperli.updateLoteInsumo(loteNew);

                    //stock insumo
                    Insumo insumoNew = helperi.getInsumo(loteNew.getInsumo().getId());
                    insumoNew.setStockTotalFisico(insumoNew.getStockTotalFisico() + loteNew.getStockLogico());
                    helperi.updateInsumo(insumoNew);
                    
                    //registrar movimiento
                    MovimientosTienda movNew = new MovimientosTienda();
                    movNew.setCantidadMovimiento(loteNew.getStockLogico());
                    movNew.setFecha(new Date());
                    movNew.setTienda(currentStore);
                    movNew.setTipoMovimiento(helpertm.getTipoMov("Ingreso"));
                    movNew.setTrabajador(LoginController.user);

                }
            }
            
            helperi.close();
            helperli.close();
            helperoc.close();
            
            
        });
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
        io.setPadding(new Insets(20));
        io.setPrefSize(145, 40);
        
        
        VBox vBox;
        if(!selectedOrdenCompra.getOrdenCompra().isRecibido()){
           vBox = new VBox(edit, io, delete);
           popup = new JFXPopup();
           popup.setPopupContent(vBox);
        }
        else {
           vBox = new VBox(edit, delete);
           popup = new JFXPopup();
           popup.setPopupContent(vBox);
        }

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
