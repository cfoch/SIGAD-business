package com.sigad.sigad.samples.map;

import com.grupo1.simulated_annealing.Locacion;
import com.jfoenix.controls.JFXListView;
import com.sigad.sigad.fx.utils.Utils;
import com.sigad.sigad.fx.widgets.VRPMapView;
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
 * TODO.
 * @author cfoch
 */
public class MapController implements Initializable {
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
    VRPMapView browser;
    private int selectedIndex = NO_SELECTED_INDEX;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        boolean validSolution;
        if (selectedIndex == NO_SELECTED_INDEX) {
            return;
        }
        resetRoutesListView(true);
        validSolution =
                browser.setSolution(Utils.marshallVRPSolution(solution));
        System.out.println("solution: " + validSolution);
        if (!validSolution) {
            return;
        }
        browser.createMarkers();
        browser.drawMarkers();
        browser.drawRoute(selectedIndex, true);
    }

    public void resetRoutesListView(boolean showCost) {
        List<Locacion> locaciones;
        int i;
        double cost = 0;
        routesListView.getItems().clear();
        if (selectedIndex == NO_SELECTED_INDEX) {
            return;
        }
        locaciones = solution.get(selectedIndex).getVertexList();
        System.out.println("Solution route size: " + locaciones.size());
        for (i = 0; i < locaciones.size(); i++) {
            Label label;
            System.out.println(locaciones.get(i).getNombre());
            label = new Label(locaciones.get(i).getNombre());
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
        for (i = 0; i < solution.size(); i++) {
            Label label;
            label = new Label(String.format("Solución %d", i + 1));
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
        pane.getChildren().add(browser);

        resetListView();
    }
}
