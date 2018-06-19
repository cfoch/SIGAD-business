/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.estadisticas.controller;

import com.jfoenix.controls.JFXButton;
import static com.sigad.sigad.helpers.cargaMasiva.CargaMasivaHelper.generarCargaMasivaTemplate;
import com.sigad.sigad.reportes.GenerarReportes;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

/**
 *
 * @author jorgito-stark
 */
public class ReportesController implements Initializable {
    public static  String viewPath = "/com/sigad/sigad/estadisticas/view/Reportes.fxml";
    
    @FXML
    private JFXButton reporteVentasBtn;

    @FXML
    private JFXButton reporteInsumosBtn;

    @FXML
    private JFXButton reporteFavoritosBtn;
    
    private GenerarReportes reporteGenerator = new GenerarReportes();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeEvents();
    }
    
    private void initializeEvents(){
        reporteVentasBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                ventasGenerate(event);
            }
        });
        reporteInsumosBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                insumosGenerate(event);
            }
        });
        reporteFavoritosBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                favoritosGenerate(event);
            }
        });
    }

    private void ventasGenerate(ActionEvent event){
        Window currentStage = getCurrentStage(event);
        DirectoryChooser dirChooser = new DirectoryChooser();
        String downloadDir = dirChooser.showDialog(currentStage).getAbsolutePath() + "/template.xls";
        if(downloadDir != null){
            reporteGenerator.reporte(downloadDir, "Ventas.jrxml","Reporte de Ventas");
        }
    }
    
    private void insumosGenerate(ActionEvent event){
        Window currentStage = getCurrentStage(event);
        DirectoryChooser dirChooser = new DirectoryChooser();
        String downloadDir = dirChooser.showDialog(currentStage).getAbsolutePath() + "/template.xls";
        if(downloadDir != null){
            reporteGenerator.reporte(downloadDir, "Insumos.jrxml","Reporte de Insumos");
        }
    }
    
    private void favoritosGenerate(ActionEvent event){
        Window currentStage = getCurrentStage(event);
        DirectoryChooser dirChooser = new DirectoryChooser();
        String downloadDir = dirChooser.showDialog(currentStage).getAbsolutePath() + "/template.xls";
        if(downloadDir != null){
            reporteGenerator.reporte(downloadDir, "Favoritos.jrxml","Reporte de Productos Favoritos");
        }
    }
    
    public Window getCurrentStage(ActionEvent event){
        Node source = (Node) event.getSource();
        return source.getScene().getWindow();
    }
}
