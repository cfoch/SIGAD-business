/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Vehiculo;
import com.sigad.sigad.utils.ui.UIFuncs.Dialogs;
import static com.sigad.sigad.utils.ui.UIFuncs.Dialogs.showMsg;
import com.sigad.sigad.validations.SIGADValidations;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class VehiculoTipoCrearController implements Initializable {
    public static enum Modo {
        CREAR,
        EDITAR
    };
    public static final String VIEW_PATH =
            "/com/sigad/sigad/repartos/view/VehiculoTipoCrear.fxml";

    @FXML
    JFXTextField nombreTxt;
    @FXML
    JFXTextField capacidadTxt;
    @FXML
    JFXTextField marcaTxt;
    @FXML
    JFXTextField modeloTxt;
    @FXML
    JFXTextArea descripcionTxtArea;
    @FXML
    StackPane stackPane;

    private JFXButton crearButton;
    private Modo modo;
    private Long currentVehiculoId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupValidators();
    }

    private void setupValidators() {
        RequiredFieldValidator validatorRequiredNombre;
        RequiredFieldValidator validatorRequiredCapacidad;
        NumberValidator validatorNumericCapacidad;
        ReadOnlyBooleanProperty prop;

        modo = Modo.CREAR;
        setCurrentVehiculoId(null);

        validatorRequiredNombre = new RequiredFieldValidator();   
        validatorRequiredNombre.setMessage(SIGADValidations.MSG.REQUIRED);
        validatorRequiredCapacidad = new RequiredFieldValidator();
        validatorRequiredCapacidad.setMessage(SIGADValidations.MSG.REQUIRED);
        validatorNumericCapacidad = new NumberValidator();
        validatorNumericCapacidad.setMessage(SIGADValidations.MSG.NUMERIC);

        nombreTxt.getValidators().add(validatorRequiredNombre);
        capacidadTxt.getValidators().add(validatorRequiredCapacidad);
        capacidadTxt.getValidators().add(validatorNumericCapacidad);

        SIGADValidations.setupSimpleValidationListener(nombreTxt);
        SIGADValidations.setupSimpleValidationListener(capacidadTxt);
    }

    private void editar(Vehiculo.Tipo tipo) {
        tipo.setNombre(nombreTxt.getText());
        tipo.setCapacidad(Double.parseDouble(capacidadTxt.getText()));
        tipo.setMarca(marcaTxt.getText());
        tipo.setModelo(modeloTxt.getText());
        tipo.setModelo(descripcionTxtArea.getText());
    }

    private Vehiculo.Tipo nuevo() {
        Vehiculo.Tipo ret;
        ret = new Vehiculo.Tipo();
        editar(ret);
        return ret;
    }

    private boolean isValid() {
        return nombreTxt.validate() && capacidadTxt.validate();
    }

    private Vehiculo.Tipo queryData(Long id, Session session) throws Exception {
        Query query;
        String hql;
        Vehiculo.Tipo vehiculoTipo;

        hql = "from Vehiculo$Tipo where id = :id";
        if (session == null){
            session = LoginController.serviceInit();
        }
        query = session.createQuery(hql).setParameter("id", id);
        vehiculoTipo = (Vehiculo.Tipo) query.getSingleResult();
        return vehiculoTipo;
    }

    public void setData(Long id) throws Exception {
        Session session;
        Query query;
        Vehiculo.Tipo vehiculoTipo;

        setCurrentVehiculoId(id);

        vehiculoTipo = queryData(id, null);
        nombreTxt.setText(vehiculoTipo.getNombre());
        capacidadTxt.setText(Double.toString(vehiculoTipo.getCapacidad()));
        marcaTxt.setText(vehiculoTipo.getMarca());
        modeloTxt.setText(vehiculoTipo.getModelo());
        descripcionTxtArea.setText(vehiculoTipo.getDescripcion());
    }

    private void onEditarButtonClicked(ActionEvent e) {
        if (isValid()) {
            Configuration config;
            Session session;
            Transaction transaction = null;
            Vehiculo.Tipo vehiculoTipo;
            System.out.println("Updating tx");

            session = LoginController.serviceInit();
            try {
                transaction = session.beginTransaction();

                vehiculoTipo = queryData(getCurrentVehiculoId(), session);
                editar(vehiculoTipo);
                System.out.println("update vehicule tipo");
                session.update(vehiculoTipo);
                transaction.commit();
                showMsg(stackPane,
                                Dialogs.HEADINGS.EXITO,
                                Dialogs.MESSAGES.CRUD_UPDATE_SUCCESS,
                                Dialogs.BUTTON.ACEPTAR);
            } catch (Exception ex) {
                Logger.getLogger(VehiculoTipoCrearController.class.getName())
                        .log(Level.SEVERE, null, ex);
                if (transaction != null) {
                    transaction.rollback();
                }
                showMsg(stackPane,
                        Dialogs.HEADINGS.ERROR,
                        Dialogs.MESSAGES.DB_GENERIC_ERROR,
                        Dialogs.BUTTON.ACEPTAR);
            } finally {
                session.close();
            }
        }
    }

    private void onCrearButtonClicked(ActionEvent e) {
        if (isValid()) {
            Configuration config;
            Session session;
            Transaction transaction = null;
            Vehiculo.Tipo vehiculoTipo;

            session = LoginController.serviceInit();
            try {
                transaction = session.beginTransaction();

                vehiculoTipo = nuevo();
                session.save(vehiculoTipo);
                transaction.commit();
                showMsg(stackPane,
                                Dialogs.HEADINGS.EXITO,
                                Dialogs.MESSAGES.CRUD_CREATE_SUCCESS,
                                Dialogs.BUTTON.ACEPTAR);
            } catch (Exception ex) {
                if (transaction != null) {
                    transaction.rollback();
                }
                showMsg(stackPane,
                        Dialogs.HEADINGS.ERROR,
                        Dialogs.MESSAGES.DB_GENERIC_ERROR,
                        Dialogs.BUTTON.ACEPTAR);
            } finally {
                session.close();
            }
        }
    }

    /**
     * @return the crearButton
     */
    public JFXButton getCrearButton() {
        return crearButton;
    }

    /**
     * @param crearButton the crearButton to set
     */
    public void setCrearButton(JFXButton crearButton) {
        this.crearButton = crearButton;
        this.crearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (modo == Modo.CREAR) {
                    onCrearButtonClicked(e);
                } else if (modo == Modo.EDITAR) {
                    onEditarButtonClicked(e);
                }
            }
        });

    }

    /**
     * @return the modo
     */
    public Modo getModo() {
        return modo;
    }

    /**
     * @param modo the modo to set
     */
    public void setModo(Modo modo) {
        this.modo = modo;
    }

    /**
     * @return the currentVehiculoId
     */
    public Long getCurrentVehiculoId() {
        return currentVehiculoId;
    }

    /**
     * @param currentVehiculoId the currentVehiculoId to set
     */
    public void setCurrentVehiculoId(Long currentVehiculoId) {
        this.currentVehiculoId = currentVehiculoId;
    }
}
