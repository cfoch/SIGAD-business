/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.helpers.AlgoritmoHelper;
import com.sigad.sigad.business.helpers.RepartoHelper;
import com.sigad.sigad.business.helpers.TiendaHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class RepartidoresDialogGenRutasController implements Initializable {
    public static final String viewPath =
            "/com/sigad/sigad/repartos/view/repartidoresDialogGenRutas.fxml";

    @FXML
    private JFXButton morningBtn;
    @FXML
    private JFXButton afternoonBtn;
    @FXML
    private JFXButton nightBtn;
    @FXML
    private JFXListView<Label> tiendasListView;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Label msgLabel;

    List<Tienda> tiendas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TiendaHelper helperTienda;
        int i;

        helperTienda = new TiendaHelper();
        if (LoginController.user.getTienda() == null) {
            tiendas = helperTienda.getStores();
        } else {
            tiendas = new ArrayList<>();
            tiendas.add(LoginController.user.getTienda());
        }
        helperTienda.close();

        for (i = 0; i < tiendas.size(); i++) {
            Tienda tienda = tiendas.get(i);
            Label label;
            label = new Label(tienda.getDescripcion());
            tiendasListView.getItems().add(label);
        }

        ReadOnlyObjectProperty prop =
                tiendasListView.getSelectionModel().selectedItemProperty();

        prop.addListener((obs, oldSelection, newSelection) -> {
            Tienda tienda;
            int index =
                    tiendasListView.getSelectionModel().getSelectedIndex();
            if (tiendasListView.getSelectionModel()
                    .getSelectedItems().isEmpty()) {
                return;
            }
            tienda = tiendas.get(index);

            try {
                resetButtonVisibility(tienda);
            } catch (Exception ex) {
                Logger.getLogger(RepartidoresDialogGenRutasController
                        .class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        progress.setVisible(false);
    }

    public void setLoadingState(boolean isLoading) {
        if (isLoading) {
            morningBtn.setDisable(true);
            afternoonBtn.setDisable(true);
            nightBtn.setDisable(true);
        }
        progress.setVisible(isLoading);
        tiendasListView.setDisable(isLoading);
    }

    public void runVRPAlgorithm(String turno) throws Exception {
        boolean ret = true;
        AlgoritmoHelper helperAlgo;
        int index = tiendasListView.getSelectionModel().getSelectedIndex();
        Tienda tienda = tiendas.get(index);
        helperAlgo = new AlgoritmoHelper();
        helperAlgo.autogenerarRepartos(tienda, turno);
    }

    public void onTurnoBtnClicked(JFXButton button, String turno) {
        Thread disableButtonsThread;
        Thread algoThread;
        Thread handlerThread;
        Tienda tienda;
        boolean res;
        int index = tiendasListView.getSelectionModel().getSelectedIndex();
        if (tiendasListView.getSelectionModel().getSelectedItems().isEmpty()) {
            return;
        }
        tienda = tiendas.get(index);

        setLoadingState(true);
        Task<Void> taskRunAlgorithm = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                runVRPAlgorithm(turno);
                return null;
            }
        };
        taskRunAlgorithm.setOnFailed(e -> {
            if (taskRunAlgorithm.getException() != null) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null,
                        taskRunAlgorithm.getException());
                msgLabel.setVisible(true);
                msgLabel.setText(taskRunAlgorithm.getException().getMessage());
            }
            setLoadingState(false);
        });
        taskRunAlgorithm.setOnSucceeded(e -> {
            try {
                resetButtonVisibility(tienda);
            } catch (Exception ex) {
                Logger.getLogger(RepartidoresDialogGenRutasController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            setLoadingState(false);
        });
        new Thread(taskRunAlgorithm).start();
    }

    public void resetButtonVisibility(Tienda tienda) throws Exception {
        int i;
        String []turnos = {"M", "T", "N"};
        RepartoHelper helper = new RepartoHelper();
        for (i = 0; i < turnos.length; i++) {
            JFXButton button;
            String turno = turnos[i];
            boolean posible = helper.esPosibleGenerarReparto(tienda.getId(),
                    turno);
            if (turno.equals("M")) {
                button = morningBtn;
            } else if (turno.equals("T")) {
                button = afternoonBtn;
            } else {
                button = nightBtn;
            }
            if (posible) {
                button.setDisable(false);
            } else {
                button.setDisable(true);
            }
        }
        helper.close();
    }

    @FXML
    public void onMorningBtnClicked(ActionEvent e) {
        onTurnoBtnClicked(morningBtn, "M");
    }

    @FXML
    public void onAfternoonBtnClicked(ActionEvent e) {
        onTurnoBtnClicked(afternoonBtn, "T");
    }

    @FXML
    public void onNightBtnClicked(ActionEvent e) {
        onTurnoBtnClicked(nightBtn, "N");
    }
}
