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
import com.jfoenix.controls.JFXTextField;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

    public static JFXDialog insumoDialog;
    
    private JFXPopup popup;
    
    public static boolean isInsumoCreate;
    
    
    public static InsumoViewer selectedInsumo = null;
    
    JFXTreeTableColumn<InsumoViewer,Boolean> selectCol = new JFXTreeTableColumn<>("Seleccionar");
    JFXTreeTableColumn<InsumoViewer,String> nombreCol = new JFXTreeTableColumn<>("Nombre");
    JFXTreeTableColumn<InsumoViewer,String> stockLCol = new JFXTreeTableColumn<>("Stock Total Logico");
    JFXTreeTableColumn<InsumoViewer,String> stockFCol = new JFXTreeTableColumn<>("Stock Total Fisico");
    JFXTreeTableColumn<InsumoViewer,String> volumenCol = new JFXTreeTableColumn<>("Volumen");
    JFXTreeTableColumn<InsumoViewer,String> estadoCol = new JFXTreeTableColumn<>("Estado");
    static ObservableList<InsumoViewer> insumosList;
    @FXML
    private JFXTextField filtro;
    
    public static class InsumoViewer extends RecursiveTreeObject<InsumoViewer>{

        public Insumo getInsumo() {
            return insumo;
        }

        public void setInsumo(Insumo insumo) {
            this.insumo = insumo;
        }

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

        public SimpleStringProperty getActivo() {
            if(activo.getValue()){
                return new SimpleStringProperty("Activo");
            }
            return new SimpleStringProperty("No Activo");
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
        
        public SimpleIntegerProperty getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = new SimpleIntegerProperty(cantidad);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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
        private Long id;
        private Insumo insumo;
        
        public InsumoViewer(String nombre,String descripcion, String tiempoVida,String stockTotalLogico, String stockTotalFisico, Boolean activo, String volumen, Integer cantidad,Long id) {
            this.nombre = new SimpleStringProperty(nombre);
            this.descripcion = new SimpleStringProperty(descripcion);
            this.tiempoVida = new SimpleStringProperty(tiempoVida);
            this.stockTotalLogico = new SimpleStringProperty(stockTotalLogico);
            this.stockTotalFisico = new SimpleStringProperty(stockTotalFisico);
            this.activo = new SimpleBooleanProperty(activo);
            this.volumen = new SimpleStringProperty(volumen);
            this.cantidad = new SimpleIntegerProperty(cantidad);
            this.id = id;
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        insumosList = FXCollections.observableArrayList();
        setColumns();
        addColumns();
        fillData();
        agregarFiltro();
    }    
    
    public void agregarFiltro() {
        filtro.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tblInsumos.setPredicate((TreeItem<InsumoViewer> t) -> {
                Boolean flag = t.getValue().nombre.getValue().contains(newValue) || t.getValue().volumen.getValue().contains(newValue) || t.getValue().stockTotalLogico.getValue().contains(newValue) || t.getValue().stockTotalFisico.getValue().contains(newValue) || t.getValue().activo.getValue().toString().contains(newValue);
                return flag;
            });
        });
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
        estadoCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<InsumoViewer, String> param) -> param.getValue().getValue().getActivo()//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
       
    }
    private void addColumns(){
        final TreeItem<InsumoViewer> rootInsumo = new RecursiveTreeItem<>(insumosList,RecursiveTreeObject::getChildren);
        tblInsumos.setEditable(true);
        tblInsumos.getColumns().setAll(nombreCol,stockLCol,stockFCol,volumenCol,estadoCol);
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
        
        InsumoViewer newinsumov = new InsumoViewer(insumo.getNombre(),
                                         insumo.getDescripcion(),
                                         Integer.toString(insumo.getTiempoVida()),
                                         stockLogico.toString(),
                                         stockFisico.toString(),
                                         insumo.isActivo(),
                                         insumo.isVolumen().toString(),
                                         0,
                                         insumo.getId());
        newinsumov.setInsumo(insumo);
        
        insumosList.add(newinsumov);
    }
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
        
        VBox vBox;
        if(selectedInsumo.getInsumo().isActivo()){
           vBox = new VBox(io,edit,delete);
           popup = new JFXPopup();
           popup.setPopupContent(vBox);
        }
        else {
           vBox = new VBox(edit);
           popup = new JFXPopup();
           popup.setPopupContent(vBox);
        }
        
    }
    private void deleteInsumosDialog() {
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
