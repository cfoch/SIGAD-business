/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Vehiculo;
import com.sigad.sigad.business.helpers.TiendaHelper;
import com.sigad.sigad.business.helpers.VehiculoHelper;
import com.sigad.sigad.utils.ui.UIFuncs;
import com.sigad.sigad.utils.ui.UIFuncs.Dialogs.SimplePopupMenuFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.hibernate.Session;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class VehiculoController implements Initializable {

    public static enum Modo {
        EDITAR,
        CREAR,
        BORRAR
    };

    public static final String viewPath =
            "/com/sigad/sigad/repartos/view/vehiculo.fxml";
    

    private VehiculoController.Modo modo;

    //@FXML
    private JFXButton borrarBtn;
    //@FXML
    private JFXButton editarBtn;
    //@FXML
    private JFXButton nuevoBtn;
    //@FXML
    private JFXButton listarBtn;
    @FXML
    private JFXButton menuBtn;
    @FXML
    private VBox box;
    @FXML
    private StackPane stackPane;

    private SimplePopupMenuFactory<Modo> menuFactory;
    private VehiculoListaController listarController;
    
    
    @FXML
    private StackPane hiddenSp;
    
    @FXML
    private JFXTreeTableView<VehiculoViewer> tblVehiculos;
    
    @FXML
    private JFXButton moreBtn;

    @FXML
    private JFXButton addBtn;
    
    
    @FXML
    public static JFXDialog vehiculoDialog;
    
    @FXML
    private JFXPopup popup;
    
    public static boolean isVehiculoCreate;
    
    static ObservableList<VehiculoViewer> vehiculosList;
    
    public static VehiculoViewer selectedVehiculo = null;

    JFXTreeTableColumn<VehiculoViewer, String> colPlaca = new JFXTreeTableColumn<>("Placa");
    JFXTreeTableColumn<VehiculoViewer, String> colTipo = new JFXTreeTableColumn<>("Tipo");
    JFXTreeTableColumn<VehiculoViewer, String> colNombre = new JFXTreeTableColumn<>("Nombre");
    JFXTreeTableColumn<VehiculoViewer, String> colTienda = new JFXTreeTableColumn<>("Tienda");
    
    
    public static class VehiculoViewer extends RecursiveTreeObject<VehiculoViewer>{

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Vehiculo getVehiculo() {
            return vehiculo;
        }

        public void setVehiculo(Vehiculo vehiculo) {
            this.vehiculo = vehiculo;
        }

        public SimpleStringProperty getPlaca() {
            return placa;
        }

        public SimpleStringProperty getTipo() {
            return tipo;
        }

        public SimpleStringProperty getNombre() {
            return nombre;
        }

        public SimpleStringProperty getTienda() {
            return tienda;
        }

        public void setPlaca(String placa) {
            this.placa = new SimpleStringProperty(placa);
        }

        public void setTipo(String tipo) {
            this.tipo = new SimpleStringProperty(tipo);
        }

        public void setNombre(String nombre) {
            this.nombre = new SimpleStringProperty(nombre);
        }

        public void setTienda(String tienda) {
            this.tienda = new SimpleStringProperty(tienda);
        }

        private SimpleStringProperty placa;
        private SimpleStringProperty tipo;
        private SimpleStringProperty nombre;
        private SimpleStringProperty tienda;
        private Long id;
        private Vehiculo vehiculo;
        
        public VehiculoViewer(String placa,String tipo, String nombre, Long id){
            this.placa = new SimpleStringProperty(placa);
            this.tipo = new SimpleStringProperty(tipo);
            this.nombre = new SimpleStringProperty(nombre);
            this.tienda = new SimpleStringProperty();
            this.id = id;
        }
        
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vehiculosList = FXCollections.observableArrayList();
        setColumns();
        addColumns();
        fillData();
        
    }

    private void setColumns(){
        colPlaca.setCellValueFactory((TreeTableColumn.CellDataFeatures<VehiculoViewer, String> param) -> param.getValue().getValue().getPlaca() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        colTipo.setCellValueFactory((TreeTableColumn.CellDataFeatures<VehiculoViewer, String> param) -> param.getValue().getValue().getTipo() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        colNombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<VehiculoViewer, String> param) -> param.getValue().getValue().getNombre() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        colTienda.setCellValueFactory((TreeTableColumn.CellDataFeatures<VehiculoViewer, String> param) -> param.getValue().getValue().getTienda() //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        );
        //Double click on row
        tblVehiculos.setRowFactory(ord -> {
            JFXTreeTableRow<VehiculoViewer> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked((event) -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                     && event.getClickCount() == 2) {
                    VehiculoViewer clickedRow = row.getItem();
                    System.out.println("Selected nombre" + clickedRow.nombre);           
                    selectedVehiculo = clickedRow;
                    try {
                        createEditVehiculoDialog(false);
                    } catch (IOException e) {
                        Logger.getLogger(VehiculoController.class.getName()).log(Level.SEVERE,"createEditVehiculoDialog()",e);
                    }
                }
            });
            return row;
        });
        
    }
    
    private void addColumns(){
        final TreeItem<VehiculoViewer> rootInsumo = new RecursiveTreeItem<>(vehiculosList,RecursiveTreeObject::getChildren);
        tblVehiculos.setEditable(true);
        tblVehiculos.getColumns().setAll(colPlaca,colTipo,colNombre,colTienda);
        tblVehiculos.setRoot(rootInsumo);
        tblVehiculos.setShowRoot(false);
    }
    
    private void fillData() {
        
        VehiculoHelper helperv = new VehiculoHelper();
        ArrayList<Vehiculo> listaVehiculos = helperv.getVehiculos();
        if(listaVehiculos!= null){
            listaVehiculos.forEach((i)->{
                updateTable(i);
            });
        }
        
        
    }
    
    public static void updateTable(Vehiculo vehiculo) {
        String nombre = vehiculo.getTipo().getMarca() + vehiculo.getTipo().getModelo();
        VehiculoViewer veh = new VehiculoViewer(vehiculo.getPlaca(),nombre,vehiculo.getNombre(),vehiculo.getId());
        veh.setVehiculo(vehiculo);
        if (vehiculo.getTienda() != null) {
            veh.setTienda(vehiculo.getTienda().getDireccion());
        }
        vehiculosList.add(veh);
    }
    
    
    @FXML
    void handleAction(ActionEvent event) {
        if(event.getSource() == addBtn) {
            try {
                createEditVehiculoDialog(true);
            } catch (IOException ex) {
                Logger.getLogger(VehiculoController.class.getName()).log(Level.SEVERE,"createEditVehiculoDialog()",ex);
            }
        }
        else if(event.getSource() == moreBtn) {
            int count = tblVehiculos.getSelectionModel().getSelectedCells().size();
            if( count > 1) {
                ErrorController error = new  ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla", "OK", hiddenSp);
            }
            else if( count <= 0 ) {
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "OK", hiddenSp);
            }
            else {
                int selected = tblVehiculos.getSelectionModel().getSelectedIndex();
                selectedVehiculo = (VehiculoViewer) tblVehiculos.getSelectionModel().getModelItem(selected).getValue();
                showOptions();
                popup.show(moreBtn,JFXPopup.PopupVPosition.TOP,JFXPopup.PopupHPosition.RIGHT);
            }
        }
    }
    
    private void showOptions(){
        JFXButton edit = new JFXButton("Editar");
        JFXButton delete = new JFXButton("Desactivar");
        JFXButton asignarTienda = new JFXButton("Asignar tienda");
        
        edit.setOnAction((ActionEvent event) -> {
            popup.hide();
            try {
                createEditVehiculoDialog(false);
            } catch (IOException ex) {
                Logger.getLogger(VehiculoController.class.getName()).log(Level.SEVERE, "initPopup(): CreateEdditUserDialog()", ex);
            }
        });
        
        delete.setOnAction((ActionEvent event) -> {
            popup.hide();
            //deleteInsumosDialog();
        });
        asignarTienda.setOnAction((ActionEvent event) -> {
            JFXListView<Label> listView = new JFXListView<Label>();
            VehiculoHelper helperVehiculo = new VehiculoHelper();
            TiendaHelper helperTienda = new TiendaHelper();
            List<Tienda> tiendas;
            Vehiculo vehiculoSeleccionado;
            VBox box = new VBox();
            JFXButton asignarBtn = new JFXButton("Asignar");

            popup.hide();

            // TODO: Handle error selectedVehiculo == null
            vehiculoSeleccionado =
                    helperVehiculo.getVehiculo(selectedVehiculo.getId());
            // TODO: Handle error vehiculoSeleccionado == null

            tiendas = helperTienda.getStores();
            for (Tienda tienda : tiendas) {
                Tienda vehiculoTienda = vehiculoSeleccionado.getTienda();
                Label label = new Label(tienda.getDescripcion());
                listView.getItems().add(label);
                if (vehiculoTienda != null &&
                        vehiculoTienda.getId() == tienda.getId()) {
                    listView.getSelectionModel().selectLast();
                }
            }
            box.getChildren().add(listView);

            asignarBtn.setOnAction((evt) -> {
                //System.out.println("HOLAaaaaaaaa");
                List<Integer> selection =
                        listView.getSelectionModel().getSelectedIndices();
                if (selection.isEmpty()) {
                    UIFuncs.Dialogs.showMsg(
                            hiddenSp,
                            UIFuncs.Dialogs.HEADINGS.ERROR,
                            "Debe seleccionar al menos una tienda.",
                            UIFuncs.Dialogs.BUTTON.ACEPTAR);
                } else {
                    Tienda tienda;
                    Integer i = selection.get(0);
                    Session session = LoginController.serviceInit();

                    tienda = tiendas.get(i);
                    vehiculoSeleccionado.setTienda(tienda);

                    session.beginTransaction();
                    session.update(vehiculoSeleccionado);
                    session.getTransaction().commit();

                    selectedVehiculo.setVehiculo(vehiculoSeleccionado);
                    selectedVehiculo.setTienda(tienda.getDescripcion());
                    tblVehiculos.refresh();
                }
            });

            UIFuncs.Dialogs.showDialog(
                    hiddenSp,
                    "Asignar tienda",
                    box,
                    asignarBtn,
                    false);
        });
        
        edit.setPadding(new Insets(20));
        edit.setPrefSize(145, 40);
        delete.setPadding(new Insets(40));
        delete.setPrefSize(145, 40);
        asignarTienda.setPadding(new Insets(40));
        asignarTienda.setPrefSize(145, 40);
        
        VBox vBox = new VBox();
        vBox.getChildren().add(edit);
        vBox.getChildren().add(asignarTienda);
        
        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }
    
    public void createEditVehiculoDialog(boolean iscreate) throws IOException {
        isVehiculoCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        if(isVehiculoCreate){
            content.setHeading(new Text("Crear Vehiculo"));
        }else{
            content.setHeading(new Text("Editar Vehiculo"));
        }
        
        Node node;
        node = (Node) FXMLLoader.load(VehiculoController.this.getClass().getResource(VehiculoCrearController.viewPath));
        content.setBody(node);
        
        vehiculoDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        vehiculoDialog.show();
    }  
}