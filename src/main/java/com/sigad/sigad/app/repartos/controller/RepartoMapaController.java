/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.grupo1.simulated_annealing.Locacion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.sigad.sigad.business.Reparto;
import com.sigad.sigad.business.helpers.AlgoritmoHelper;
import com.sigad.sigad.fx.utils.Utils;
import com.sigad.sigad.fx.widgets.VRPMapView;
import com.sigad.sigad.utils.ui.UIBaseController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import org.jgrapht.graph.GraphWalk;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class RepartoMapaController extends UIBaseController
        implements Initializable{
    public static final String VIEW_PATH =
            "/com/sigad/sigad/repartos/view/repartoMapa.fxml";
    public static final int NO_SELECTED_INDEX = -1;
    public static ArrayList<GraphWalk> solution;



    @FXML
    private AnchorPane pane;
    @FXML
    private JFXListView<Label> listView;
    @FXML
    private JFXListView<Label> routesListView;
    @FXML
    private Label costLabel;
    @FXML
    private JFXButton button;
    VRPMapView browser;
    private int selectedIndex = NO_SELECTED_INDEX;

    private List<Reparto> repartos;
    private List<List<Locacion>> solucion;

    public RepartoMapaController(List<Reparto> repartos) {
        this.repartos = repartos;
        this.solucion = repartos2solution(repartos);
    }

    List<List<Locacion>> repartos2solution(List<Reparto> repartos) {
        int i;
        String marshallizedSolution;
        AlgoritmoHelper helperAlgoritmo = new AlgoritmoHelper(false);
        List<List<Locacion>> solucion = new ArrayList<>();
        for (i = 0; i < repartos.size(); i++) {
            List<Locacion> ruta;
            Reparto reparto = repartos.get(i);
            ruta = helperAlgoritmo.repartoToRoute(reparto);
            solucion.add(ruta);
        }
        return solucion;
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        List<Reparto> uniqueReparto = new ArrayList<>();
        List<List<Locacion>> selectedSolution;
        boolean validSolution;
        if (selectedIndex == NO_SELECTED_INDEX) {
            return;
        }
        resetRoutesListView(true);
        uniqueReparto.add(repartos.get(selectedIndex));
        selectedSolution = repartos2solution(uniqueReparto);
        System.out.println("Marshallize solutions");

        validSolution =
                browser.setSolution(
                        Utils.marshallLocationSolution(selectedSolution));
        System.out.println("solution: " + validSolution);
        if (!validSolution) {
            return;
        }
        System.out.println("Calling javascript rest funcs");
        browser.createMarkers();
        browser.drawMarkers();
        browser.drawRoute(0, true);
    }

    public void resetRoutesListView(boolean showCost) {
        List<Locacion> locaciones;
        int i;
        double cost = 0;
        routesListView.getItems().clear();
        if (selectedIndex == NO_SELECTED_INDEX) {
            return;
        }
        locaciones = solucion.get(selectedIndex);
        System.out.println("Solution route size: " + locaciones.size());
        for (i = 0; i < locaciones.size(); i++) {
            Label label;
            String text;
            System.out.println(locaciones.get(i).getNombre());
            if (locaciones.get(i).getTipo() == Locacion.Tipo.DEPOSITO) {
                text = String.format("Tienda #%d", locaciones.get(i).getId());
            } else {
                text = String.format("Pedido #%d", locaciones.get(i).getId());
            }
            label = new Label(text);
            label.setTooltip(new Tooltip(locaciones.get(i).getNombre()));
            routesListView.getItems().add(label);
            if (showCost && locaciones.get(i).getServicio() != null) {
                cost += locaciones.get(i).getServicio().getDemanda();
            }
        }
        costLabel.setText("Costo: " + cost);
    }

    public void resetListView() {
        int i;
        ReadOnlyObjectProperty prop;
        if (listView == null) {
            selectedIndex = NO_SELECTED_INDEX;
            return;
        }
        for (i = 0; i < repartos.size(); i++) {
            Reparto reparto = repartos.get(i);
            Label label;
            label = new Label(String.format("Reparto #%d", reparto.getId()));
            listView.getItems().add(label);
        }
        prop = listView.getSelectionModel().selectedItemProperty();
        prop.addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable,
                    Label oldValue, Label newValue) {
                selectedIndex = listView.getSelectionModel().getSelectedIndex();
            }
        });
    }

    /**
     * TODO.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        boolean validSolution;
        browser = new VRPMapView();
        browser.getWebView().setPrefSize(400, 400);
        pane.getChildren().add(browser);
//
        resetListView();
    }
    @Override
    public String getViewPath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
