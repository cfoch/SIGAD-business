/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.controller.cargaMasiva;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import com.sigad.sigad.business.*;
import com.sigad.sigad.helpers.cargaMasiva.*;
import static com.sigad.sigad.helpers.cargaMasiva.CargaMasivaHelper.generarCargaMasivaTemplate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import com.sigad.sigad.helpers.cargaMasiva.TreeObjectInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javafx.event.Event;
import javafx.scene.control.SelectionMode;

/**
 * FXML Controller class
 *
 * @author paul
 */
public class CargaMasivaViewController implements Initializable {
    public static  String viewPath = "/com/sigad/sigad/cargaMasiva/view/cargaMasivaView.fxml";
    
    private Desktop desktop = Desktop.getDesktop();
    private File loadedFile;
    
    @FXML
    private JFXButton plantillaBtn;
    @FXML
    private JFXButton cargaMasivaBtn;
    @FXML
    private JFXTextField archivoNombre;
    @FXML
    private JFXButton guardarBtn;
     @FXML
    private JFXTreeTableView<HojaReporteTree> tablaPrevia;
    @FXML
    private JFXListView<String> entidadesListView;
    private ArrayList<String> entidadesSeleccionadas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> options = CargaMasivaConstantes.getList();
        
        for (Iterator<String> iterator = options.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            entidadesListView.getItems().add(next);
        }
        
        entidadesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


         entidadesListView.setOnMouseClicked(new EventHandler<Event>() {

             @Override
             public void handle(Event event) {
                 ObservableList<String> selectedItems =  entidadesListView.getSelectionModel().getSelectedItems();
                 //entidadesSeleccionadas.clear();
                 ArrayList<String> selectedList = new ArrayList<String>();
                 for(String s : selectedItems){
                     selectedList.add(s);
                 }
                 updateList(selectedList);
             }

         });
        
        plantillaBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                downloadTemplate(event);
            }
        });
        
        cargaMasivaBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                File file = loadFile(event);
                if(file != null){
                    //openFile(file);
                    String fileName = file.getAbsolutePath();
                    archivoNombre.setText(fileName);
                    loadedFile = file;
                    initializeGrid(file);
               }
            }
        });
        
        guardarBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                String filePath = loadedFile.getAbsolutePath();
                List<HojaReporte> reporte = CargaMasivaHelper.CargaMasivaProceso(filePath);
                showReport(reporte);
            }
        });
    }
    
    private void showReport(List<HojaReporte> reporte){
        
        JFXTreeTableColumn<HojaReporteTree,String> hoja = new JFXTreeTableColumn<>("Hoja procesada");
        hoja.setPrefWidth(300);
        hoja.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HojaReporteTree, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<HojaReporteTree,String> param){
                return param.getValue().getValue().hoja;
            }
        });        
        
        JFXTreeTableColumn<HojaReporteTree,String> casosExitosos = new JFXTreeTableColumn<>("Casos Exitosos");
        casosExitosos.setPrefWidth(100);
        casosExitosos.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HojaReporteTree, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<HojaReporteTree,String> param){
                return param.getValue().getValue().casosExitosos;
            }
        });        

        JFXTreeTableColumn<HojaReporteTree,String> casosFallidos = new JFXTreeTableColumn<>("Casos Fallidos");
        casosFallidos.setPrefWidth(100);
        casosFallidos.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<HojaReporteTree, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<HojaReporteTree,String> param){
                return param.getValue().getValue().casosFallidos;
            }
        });
        
        ObservableList<HojaReporteTree> reportList = FXCollections.observableArrayList();
        
        if(reporte != null){
            reporte.forEach((p) ->{
                HojaReporte t = p;
                reportList.add(new HojaReporteTree(
                    String.valueOf(t.getNombreHoja()),
                    String.valueOf(t.getCasosExitosos()),
                    String.valueOf(t.getCasosFallidos())
                ));
            });
        }
         
        final TreeItem<HojaReporteTree> root = new RecursiveTreeItem<HojaReporteTree>(reportList, RecursiveTreeObject::getChildren);
        tablaPrevia.getColumns().setAll(hoja,casosExitosos,casosFallidos);
        tablaPrevia.setRoot(root);
        tablaPrevia.setShowRoot(false);     
    }
    
    public Window getCurrentStage(ActionEvent event){
        Node source = (Node) event.getSource();
        return source.getScene().getWindow();
    }

    public File loadFile(ActionEvent event){
        Window currentStage = getCurrentStage(event);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir archivo de carga");
        return fileChooser.showOpenDialog(currentStage);
    }
    
    public void downloadTemplate(ActionEvent event){                 
        Window currentStage = getCurrentStage(event);
        if(entidadesSeleccionadas != null){
            DirectoryChooser dirChooser = new DirectoryChooser();
            String downloadDir = dirChooser.showDialog(currentStage).getAbsolutePath() + "/template.xls";
            if(downloadDir != null){
                generarCargaMasivaTemplate(entidadesSeleccionadas,downloadDir);
            }
        }        
    }
    
    private void openFile(File file) {
        if(Desktop.isDesktopSupported()){
            new Thread(() -> {
                 try {
                    desktop.open(file);
                } catch (IOException ex) {
                    Logger.getLogger(CargaMasivaViewController.class.getName())
                            .log(Level.SEVERE, null, ex);
                }       
            }).start();
        }
    }
    
    private void updateList(ArrayList<String> options){
        this.entidadesSeleccionadas = options;
    }

    private void initializeGrid(File file){
        // Insumo
        /*
        JFXTreeTableColumn<Insumo, String>nomb = new JFXTreeTableColumn<>("Nombre");
        nomb.setPrefWidth(50);
        nomb.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Insumo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Insumo, String> param) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return param.getValue().getValue().nombre;
            }
        });
        
        JFXTreeTableColumn<Insumo, String>descp = new JFXTreeTableColumn<>("Descripción");
        descp.setPrefWidth(150);
        descp.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Insumo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Insumo, String> param) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return param.getValue().getValue().descripcion;
            }
        });        
        
        ObservableList<Insumo> insumos = FXCollections.observableArrayList();
        insumos.add(new Insumo("Insumo 1", "Este insumo es el primero", "5", "50.00", "200", "true", "15"));
        insumos.add(new Insumo("Insumo 2", "Este insumo no es el primero", "5", "50.00", "200", "true", "15"));
        insumos.add(new Insumo("Insumo 3", "Este insumo no es el primero", "5", "50.00", "200", "true", "15"));
        
        final TreeItem<Insumo> root = new RecursiveTreeItem<Insumo>(insumos, RecursiveTreeObject::getChildren);
        tablaPrevia.getColumns().setAll(nomb,descp);
        tablaPrevia.setRoot(root);
        tablaPrevia.showRoot(false);
        */
        // Fin Insumo
    }
    
    class HojaReporteTree extends RecursiveTreeObject<HojaReporteTree>{
        StringProperty hoja;
        StringProperty casosExitosos;
        StringProperty casosFallidos;
        
        public HojaReporteTree(String hoja, String casosExitosos, String casosFallidos){
            this.hoja = new SimpleStringProperty(hoja);
            this.casosExitosos = new SimpleStringProperty(casosExitosos);
            this.casosFallidos = new SimpleStringProperty(casosFallidos);
        }
    }
    
}
