/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.google.maps.errors.ApiException;
import com.jfoenix.controls.*;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.helpers.GMapsHelper;
import com.sigad.sigad.business.helpers.GeneralHelper;
import com.sigad.sigad.business.helpers.TiendaHelper;
import com.sigad.sigad.fx.widgets.MapPicker;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */
public class SolicitarDireccionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/pedido/view/solicitarDireccion.fxml";
    private Tienda tienda;
    private ArrayList<Tienda> tiendas;
    private Double distancia;
    private StackPane sc;
    private Double x;
    private Double y;
    @FXML
    private JFXButton btnBuscarTienda;

    @FXML
    private JFXButton btnContinuar;

    @FXML
    private JFXTextField txtdireccion;

    @FXML
    private JFXTextField txtTiendaDireccion;

    @FXML
    private AnchorPane mapBox;

    private MapPicker mapPicker;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mapPicker = new MapPicker();
        x = y = null;
        distancia = Double.MAX_VALUE;
        mapBox.getChildren().add(mapPicker);
        mapPicker.addMarkerAddedPropListener((evt) -> {
            System.out.println(evt.getPropertyName());
            if (evt.getPropertyName().equals("marker-added")) {
                try {
                    Double[] latlng = (Double[]) evt.getNewValue();
                    GMapsHelper helper = new GMapsHelper();
                    JSONObject ret
                            = helper.reverseGeoCode(latlng[0], latlng[1]);
                    JSONObject location;
                    String formatedAddress;
                    location = ret.getJSONArray("results").getJSONObject(0);
                    formatedAddress = location.getString("formatted_address");
                    // Encode to UTF-8.
                    byte ptext[] = formatedAddress.getBytes("ISO-8859-1");
                    formatedAddress = new String(ptext, "UTF-8");
                    // Update entry text.
                    txtdireccion.setText(formatedAddress);
                    // Set latitude && longitude.
                    x = latlng[0];
                    y = latlng[1];
                } catch (Exception ex) {
                    x = y = null;
                    txtdireccion.setText("");
                    ex.printStackTrace();
                }
            }
        });
        mapPicker.getWebView().setPrefSize(400, 400);
        txtdireccion.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 0 || txtTiendaDireccion.getText().length() == 0) {
                btnContinuar.setDisable(true);
            } else {
                btnContinuar.setDisable(false);
            }
        });
    }

    public void initModel(Boolean isEdit, Tienda tienda, StackPane sc) {
        this.sc = sc;
        this.tienda = tienda;

    }

    @FXML
    void encontrarTiendaCercana(MouseEvent event) {
        try {
            if (txtdireccion.getText().length() <= 0) {
                return;
            }

            GMapsHelper helper = GMapsHelper.getInstance();
            Pair<Double, Double> pair = helper.geocodeAddress(txtdireccion.getText());

            x = pair.getLeft();
            y = pair.getRight();
            System.out.println("x->" + x + "y->" + y);
            mapPicker.setMarkerAt(x, y);
            tienda = null;
            distancia = Double.MAX_VALUE;
            TiendaHelper helpertienda = new TiendaHelper();
            helpertienda.getStores().forEach((t) -> {
                Double d = GeneralHelper.distanceBetweenTwoPoints(x, t.getCooXDireccion(), y, t.getCooYDireccion());
                if (d < distancia) {
                    tienda = t;
                    distancia = d;
                }
            });
            if (tienda != null) {
                txtTiendaDireccion.setText(tienda.getDireccion());
                btnContinuar.setDisable(false);

            } else {
                txtTiendaDireccion.setText("");
            }

        } catch (ApiException | InterruptedException | IOException ex) {
            Logger.getLogger(SolicitarDireccionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SolicitarDireccionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void crearPedido(MouseEvent event) {
        try {
            Node node;
            FXMLLoader loader = new FXMLLoader(SolicitarDireccionController.this.getClass().getResource(SeleccionarProductosController.viewPath));
            node = (Node) loader.load();
            SeleccionarProductosController sel = loader.getController();
            Pedido pedido = new Pedido();
            pedido.setTienda(tienda);
            pedido.setDireccionDeEnvio(txtdireccion.getText());
            pedido.setCooXDireccion(x);
            pedido.setCooYDireccion(y);
            sel.initModel(pedido, sc);
            sc.getChildren().setAll(node);

        } catch (Exception ex) {
            Logger.getLogger(SolicitarDireccionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
