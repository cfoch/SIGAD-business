/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.utils.ui;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.tuple.Pair;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class UICRUDViewListarController extends UIBaseController
        implements Initializable {
    public static final String VIEW_PATH =
            "/com/sigad/sigad/repartos/view/CRUDViewListar.fxml";

    private static final int DEFAULT_PREF_WIDTH = 150;

    @FXML
    private JFXTreeTableView treeView;
    private HashMap<String, Pair<String, JFXTreeTableColumn<Info, ?>>> columns;
    ObservableList<Info> infos;

    public UICRUDViewListarController() {
        columns = new HashMap<>();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<String> columnsArrayList;
        infos = FXCollections.observableArrayList();
        final TreeItem<Info> root =
                new RecursiveTreeItem<Info>(infos,
                        RecursiveTreeObject::getChildren);
        columnsArrayList = new ArrayList<String>(columns.keySet());
        showColumns(columnsArrayList.stream().toArray(String[]::new));
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        infos.addAll(populate());

        //Double click on row
        treeView.setRowFactory(ord -> {
            JFXTreeTableRow<Info> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked((event) -> {
                onRowClicked(event);
            });
            return row;
        });

    }

    public List<Info> populate() {
        return new ArrayList<>();
    }

    private void onRowClicked(MouseEvent event) {
        JFXTreeTableRow<Info> row;
        row = (JFXTreeTableRow<Info>) event.getSource();
        if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
             && event.getClickCount() == 2) {
            Info clickedRow = row.getItem();
            rowDoubleClickFunc(clickedRow);
        }
    }

    protected void rowDoubleClickFunc(Info selectedInfo) {
    }

    public void showColumns(String [] columnNames) {
        int i;
        for (i = 0; i < columnNames.length; i++) {
            System.out.println("show " + columnNames[i]);
            treeView.getColumns().add(columns.get(columnNames[i]).getRight());
        }
    }

    public <T> void addColumn(String columnName, String title) {
        JFXTreeTableColumn<Info, T> col;
        Pair pair;
        col = new JFXTreeTableColumn<>(title);
        col.setPrefWidth(DEFAULT_PREF_WIDTH);
        col.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Info, T> param) ->
                        param.getValue().getValue().getProperty(columnName));
        pair = Pair.of(title, col);
        columns.put(columnName, pair);
    }

    public <T> JFXTreeTableColumn<Info, T> getColumn(
            String columnName) {
        return (JFXTreeTableColumn<Info, T>) columns.get(columnName).getRight();
    }

    public void addColumnNumeric(String columnName, String title) {
        this.<Number>addColumn(columnName, title);
    }

    public JFXTreeTableColumn<Info, Number> getColumnNumeric(
            String columnName) {
        return getColumn(columnName);
    }

    public void addColumnString(String columnName, String title) {
        this.<String>addColumn(columnName, title);
    }

    public JFXTreeTableColumn<Info, String> getColumnString(
            String columnName) {
        return getColumn(columnName);
    }

    @Override
    public String getViewPath() {
        return VIEW_PATH;
    }

    public static class Info extends RecursiveTreeObject<Info> {
        public HashMap<String, ObservableValue> columns;

        public Info() {
            columns = new HashMap<>();
        }

        public void addProperty(String columnName, ObservableValue prop) {
            columns.put(columnName, prop);
        }

        public ObservableValue getProperty(String columName) {
            return columns.get(columName);
        }
    }
}
