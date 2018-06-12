/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.insumos.controller;

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
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.LoteInsumo;
import com.sigad.sigad.business.helpers.InsumosHelper;
import com.sigad.sigad.business.helpers.LoteInsumoHelper;
import com.sigad.sigad.personal.controller.PersonalController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class ListaInsumoController implements Initializable {
    
    /**
     * Initializes the controller class.
     */
    
    public static String viewPath = "/com/sigad/sigad/insumos/view/ListaInsumo.fxml";
    
    @FXML
    private StackPane hiddenSp;

    @FXML
    private JFXTreeTableView<InsumoViewer> tblInsumos;
    
    @FXML
    private JFXButton moreBtn;

    @FXML
    private JFXButton addBtn;

    @FXML
    public static JFXDialog insumoDialog;
    
    @FXML
    private JFXPopup popup;
    
    public static boolean isInsumoCreate;
    
    
    public static InsumoViewer selectedInsumo = null;
    // table elements;
    
    JFXTreeTableColumn<InsumoViewer,Boolean> selectCol = new JFXTreeTableColumn<>("Seleccionar");
    JFXTreeTableColumn<InsumoViewer,String> nombreCol = new JFXTreeTableColumn<>("Nombre");
    JFXTreeTableColumn<InsumoViewer,String> stockLCol = new JFXTreeTableColumn<>("Stock Total Logico");
    JFXTreeTableColumn<InsumoViewer,String> stockFCol = new JFXTreeTableColumn<>("Stock Total Fisico");
    JFXTreeTableColumn<InsumoViewer,String> volumenCol = new JFXTreeTableColumn<>("Volumen");
    static ObservableList<InsumoViewer> insumosList;
    
    public static class InsumoViewer extends RecursiveTreeObject<InsumoViewer>{

        public SimpleStringProperty getStockTotalLogico() {
            return stockTotalLogico;
        }

        public SimpleStringProperty getStockTotalFisico() {
            return stockTotalFisico;
        }

        public void setStockTotalLogico(String stockTotalLogico) {
            this.stockTotalLogico = new SimpleStringProperty(stockTotalLogico);
        }

        public void setStockTotalFisico(String stockTotalFisico) {
            this.stockTotalFisico = new SimpleStringProperty(stockTotalFisico);
        }

        public SimpleStringProperty getNombre() {
            return nombre;
        }

        public SimpleStringProperty getDescripcion() {
            return descripcion;
        }

        public SimpleStringProperty getTiempoVida() {
            return tiempoVida;
        }

        public SimpleBooleanProperty getActivo() {
            return activo;
        }

        public SimpleStringProperty getVolumen() {
            return volumen;
        }

        public BooleanProperty getSeleccion() {
            return seleccion;
        }

        public void setNombre(String nombre) {
            this.nombre = new SimpleStringProperty(nombre);
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = new SimpleStringProperty(descripcion);
        }

        public void setTiempoVida(String tiempoVida) {
            this.tiempoVida = new SimpleStringProperty(tiempoVida);
        }

        public void setActivo(Boolean activo) {
            this.activo = new SimpleBooleanProperty(activo);
        }

        public void setVolumen(String volumen) {
            this.volumen = new SimpleStringProperty(volumen);
        }

        public void setSeleccion(BooleanProperty seleccion) {
            this.seleccion = seleccion;
        }

        private SimpleStringProperty nombre;
        private SimpleStringProperty descripcion;
        private SimpleStringProperty tiempoVida;
        private SimpleStringProperty stockTotalLogico;
        private SimpleStringProperty stockTotalFisico;
        private SimpleBooleanProperty activo;
        private SimpleStringProperty volumen;
        private BooleanProperty seleccion;
        private SimpleIntegerProperty cantidad;
        ImageView imagen;
        private Long id;
        private Double precio;
        
        public InsumoViewer(String nombre,String descripcion, String tiempoVida,String stockTotalLogico, String stockTotalFisico, Boolean activo, String volumen ,String imagePath, Integer cantidad,Long id,Double precio) {
            this.nombre = new SimpleStringProperty(nombre);
            this.descripcion = new SimpleStringProperty(descripcion);
            this.tiempoVida = new SimpleStringProperty(tiempoVida);
            this.stockTotalLogico = new SimpleStringProperty(stockTotalLogico);
            this.stockTotalFisico = new SimpleStringProperty(stockTotalFisico);
            this.activo = new SimpleBooleanProperty(activo);
            this.volumen = new SimpleStringProperty(volumen);
            this.cantidad = new SimpleIntegerProperty(cantidad);
            this.id = id;
            this.precio = precio;
        }

        public SimpleIntegerProperty getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = new SimpleIntegerProperty(cantidad);
        }

        public Long getId() {
            return id;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        insumosList = FXCollections.observableArrayList();
        setColumns();
        addColumns();
        fillData();
    }    
    
    private void setColumns(){
        nombreCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getNombre() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        stockLCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getStockTotalLogico()//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        stockFCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getStockTotalFisico()//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        volumenCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getVolumen() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        //Double click on row
        tblInsumos.setRowFactory(ord -> {
            JFXTreeTableRow<InsumoViewer> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked((event) -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                     && event.getClickCount() == 2) {
                    InsumoViewer clickedRow = row.getItem();
                    System.out.println("Selected nombre" + clickedRow.nombre);           
                    selectedInsumo = clickedRow;
                    try {
                        createEditInsumoDialog(false);
                    } catch (IOException e) {
                        Logger.getLogger(ListaInsumoController.class.getName()).log(Level.SEVERE,"createEditInsumoDialog()",e);
                    }
                }
            });
            return row;
        });
       
    }
    
    private void addColumns(){
        final TreeItem<InsumoViewer> rootInsumo = new RecursiveTreeItem<>(insumosList,RecursiveTreeObject::getChildren);
        tblInsumos.setEditable(true);
        tblInsumos.getColumns().setAll(nombreCol,stockLCol,stockFCol,volumenCol);
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
    }
    
    public static void updateTable(Insumo insumo){
        LoteInsumoHelper helperli = new LoteInsumoHelper();
        Integer stockFisico = 0;
        Integer stockLogico = 0;
        ArrayList<LoteInsumo> li = helperli.getLoteInsumosEspecific(LoginController.user.getTienda(), insumo);
        
        if(li != null){
            for (LoteInsumo li1 : li) {
                stockFisico += li1.getStockFisico();
                stockLogico += li1.getStockLogico();
            }
        }
        
        insumosList.add(new InsumoViewer(insumo.getNombre(),
                                         insumo.getDescripcion(),
                                         Integer.toString(insumo.getTiempoVida()),
                                         stockLogico.toString(),
                                         stockFisico.toString(),
                                         insumo.isActivo(),
                                         insumo.isVolumen().toString(),
                                         insumo.getImagen(),0,insumo.getId(),insumo.getPrecio()));
    }
    
    
    //botones
    @FXML
    private void handleAction(ActionEvent event) {
        if(event.getSource() == addBtn ) {
            try {
                createEditInsumoDialog(true);
            } catch (IOException ex) {
                Logger.getLogger(ListaInsumoController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
            }
        }
        else if (event.getSource() == moreBtn){
            int count = tblInsumos.getSelectionModel().getSelectedCells().size();
            if( count > 1) {
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla", "OK", hiddenSp);
            }else if(count <=0){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "OK", hiddenSp);
            }else{
                int selected =tblInsumos.getSelectionModel().getSelectedIndex();
                selectedInsumo = (InsumoViewer)  tblInsumos.getSelectionModel().getModelItem(selected).getValue();
                showOptions();
                popup.show(moreBtn,JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
            }
        }
    }
    
    //dialogos
    private void showOptions(){
        JFXButton edit = new JFXButton("Editar");
        JFXButton delete = new JFXButton("Desactivar");
        JFXButton io = new JFXButton("I/O");
        
        edit.setOnAction((ActionEvent event) -> {
            popup.hide();
            try {
                createEditInsumoDialog(false);
            } catch (IOException ex) {
                Logger.getLogger(ListaInsumoController.class.getName()).log(Level.SEVERE, "initPopup(): CreateEdditUserDialog()", ex);
            }
        });
        
        delete.setOnAction((ActionEvent event) -> {
            popup.hide();
            deleteInsumosDialog();
        });
        
        io.setOnAction((ActionEvent event) ->{ 
            popup.hide();
            try {
                registraringresoSalidaDialog();
            } catch (IOException ex) {
                Logger.getLogger(ListaInsumoController.class.getName()).log(Level.SEVERE, "initPopup(): registraringresoSalidaDialog()", ex);
            }
        });
        
        edit.setPadding(new Insets(20));
        edit.setPrefSize(145, 40);
        delete.setPadding(new Insets(20));
        delete.setPrefSize(145, 40);
        io.setPadding(new Insets(20));
        io.setPrefSize(145, 40);
        
        VBox vBox = new VBox(io,delete);
        
        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }
    
    private void deleteInsumosDialog() {
        //delete if stock 0 validacion falta
        if((Integer.parseInt(selectedInsumo.getStockTotalLogico().getValue()) >= 0)|| (Integer.parseInt(selectedInsumo.getStockTotalFisico().getValue()) >= 0)) {
            ErrorController error = new ErrorController();
            error.loadDialog("Atención", "No puede desactivar un insumo con stock", "OK", hiddenSp);
        }
        else {
            JFXDialogLayout content =  new JFXDialogLayout();
            content.setHeading(new Text("Desactivar Insumo"));
            content.setBody(new Text("¿Seguro que desea desactivar el insumo seleccionado?"));

            InsumosHelper helperi = new InsumosHelper();
            JFXDialog dialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
            JFXButton button = new JFXButton("Okay");
            System.out.println(helperi.getInsumo(selectedInsumo.getId()));
            button.setOnAction((ActionEvent event) -> {
                Insumo ins= helperi.getInsumo(selectedInsumo.getId());
                helperi.disableInsumo(ins);
                dialog.close();
                helperi.close();
            });
            content.setActions(button);
            dialog.show();
        }

    }
    
    public void createEditInsumoDialog(boolean iscreate) throws IOException {
        isInsumoCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        if(isInsumoCreate){
            content.setHeading(new Text("Crear Insumo"));
        }else{
            content.setHeading(new Text("Editar Insumo"));
        }
        
        Node node;
        node = (Node) FXMLLoader.load(ListaInsumoController.this.getClass().getResource(CrearEditarInsumoController.viewPath));
        content.setBody(node);
        
        insumoDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        insumoDialog.show();
    }  
    
    // ingresos por hallazgo y salidas por accidente
    private void registraringresoSalidaDialog() throws IOException{
        JFXDialogLayout content = new JFXDialogLayout();
        
        content.setHeading(new Text("Registrar Ingreso o salida"));
        
        Node node;
        node = (Node) FXMLLoader.load(ListaInsumoController.this.getClass().getResource(RegistrarIngresoSalidaInsumoController.viewPath));
        content.setBody(node);
        
        insumoDialog = new JFXDialog(hiddenSp,content,JFXDialog.DialogTransition.CENTER);
        insumoDialog.show();
    }
}
