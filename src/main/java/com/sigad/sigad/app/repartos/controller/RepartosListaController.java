/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Reparto;
import com.sigad.sigad.business.helpers.PedidoHelper;
import com.sigad.sigad.business.helpers.RepartoHelper;
import com.sigad.sigad.utils.ui.UIFuncs;
import com.sigad.sigad.utils.ui.UIFuncs.Dialogs;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class RepartosListaController implements Initializable {
    public static final String VIEW_PATH =
            "/com/sigad/sigad/repartos/view/repartosLista.fxml";

    @FXML
    private JFXTreeTableView treeView;
    @FXML
    private StackPane stackPane;

    private long repartidorId;
    ObservableList<RepartoInfo> infos;

    public RepartosListaController(StackPane stackPane, long repartidorId) {
        setStackPane(stackPane);
        setRepartidorId(repartidorId);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXTreeTableColumn<RepartoInfo, Number> colId;
        JFXTreeTableColumn<RepartoInfo, String> colFecha;
        JFXTreeTableColumn<RepartoInfo, String> colTurno;
        JFXTreeTableColumn<RepartoInfo, Number> colNPedidos;
        JFXTreeTableColumn<RepartoInfo, Number> colNPedidosRepartidos;
        JFXTreeTableColumn<RepartoInfo, Number> colNPedidosPorRepartir;

        colId = new JFXTreeTableColumn<>("Código");
        colId.setPrefWidth(150);
        colId.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<RepartoInfo, Number> param) ->
                        param.getValue().getValue().id);
        colFecha = new JFXTreeTableColumn<>("Fecha");
        colFecha.setPrefWidth(150);
        colFecha.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<RepartoInfo, String> param) ->
                        param.getValue().getValue().fecha);
        colTurno = new JFXTreeTableColumn<>("Turno");
        colTurno.setPrefWidth(150);
        colTurno.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<RepartoInfo, String> param) ->
                        param.getValue().getValue().turno);
        colNPedidosRepartidos =
                new JFXTreeTableColumn<>("N° Pedidos repartidos");
        colNPedidosRepartidos.setPrefWidth(150);
        colNPedidosRepartidos.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<RepartoInfo, Number> param) ->
                        param.getValue().getValue().nPedidosRepartidos);
        colNPedidosPorRepartir =
                new JFXTreeTableColumn<>("N° Pedidos por repartir");
        colNPedidosPorRepartir.setPrefWidth(150);
        colNPedidosPorRepartir.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<RepartoInfo, Number> param) ->
                        param.getValue().getValue().nPedidosPorRepartir);
        colNPedidos = new JFXTreeTableColumn<>("N°  Pedidos");
        colNPedidos.setPrefWidth(150);
        colNPedidos.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<RepartoInfo, Number> param) ->
                        param.getValue().getValue().nPedidos);

        infos = FXCollections.observableArrayList();
        final TreeItem<RepartoInfo> root =
                new RecursiveTreeItem<RepartoInfo>(infos,
                        RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(
                colId,
                colFecha,
                colTurno,
                colNPedidosRepartidos,
                colNPedidosPorRepartir,
                colNPedidos
        );
        treeView.setRoot(root);
        treeView.setShowRoot(false);

        populate();
    }

    private void populate() {
        List<Reparto> repartos;
        RepartoHelper helperReparto = new RepartoHelper();

        repartos = helperReparto.getRepartos(repartidorId);

        repartos.forEach((reparto) -> {
            List<Pedido> pedidos;
            RepartoInfo info;
            Integer nPedidosRepartidos, nPedidosPorRepartir;

            pedidos = reparto.getPedidos();
            pedidos = pedidos.stream()
                    .sorted((a, b) -> a.getSecuenciaReparto() - b.getSecuenciaReparto())
                    .collect(Collectors.toList());
            nPedidosRepartidos = pedidos.stream()
                    .filter((p) -> p.getEstado().equals("Despacho") || p.getEstado().equals("Finalizado"))
                    .collect(Collectors.toList()).size();
            nPedidosPorRepartir = pedidos.size() - nPedidosRepartidos;
            info = new RepartoInfo(reparto, nPedidosRepartidos,
                    nPedidosPorRepartir);
            infos.add(info);
        });
        helperReparto.close();
    }

    /**
     * @return the repartidorId
     */
    public long getRepartidorId() {
        return repartidorId;
    }

    /**
     * @param repartidorId the repartidorId to set
     */
    public void setRepartidorId(long repartidorId) {
        this.repartidorId = repartidorId;
    }

    /**
     * @return the stackPane
     */
    public StackPane getStackPane() {
        return stackPane;
    }

    /**
     * @param stackPane the stackPane to set
     */
    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public static class RepartoInfo extends RecursiveTreeObject<RepartoInfo> {
        IntegerProperty id;
        StringProperty fecha;
        StringProperty turno;
        IntegerProperty nPedidos;
        IntegerProperty nPedidosRepartidos;
        IntegerProperty nPedidosPorRepartir;

        public RepartoInfo(Reparto reparto, int nRepartidos,
                int nNoRepartidos) {
            Format formatter = new SimpleDateFormat("dd-MM-yyyy");
            id = new SimpleIntegerProperty(reparto.getId().intValue());
            fecha = new SimpleStringProperty(
                    formatter.format(reparto.getFecha()));
            turno = new SimpleStringProperty(
                    PedidoHelper.stringifyTurno(reparto.getTurno()));
            nPedidos = new SimpleIntegerProperty(reparto.getPedidos().size());
            nPedidosRepartidos = new SimpleIntegerProperty(nRepartidos);
            nPedidosPorRepartir = new SimpleIntegerProperty(nNoRepartidos);
        }
    }
}
