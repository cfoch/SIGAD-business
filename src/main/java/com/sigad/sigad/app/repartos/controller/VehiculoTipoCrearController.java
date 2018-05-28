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
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * FXML Controller class
 *
 * @author cfoch
 */
public class VehiculoTipoCrearController implements Initializable {
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
    JFXButton guardarBtn;
    @FXML
    StackPane stackPane;

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

    private Vehiculo.Tipo nuevo() {
        Vehiculo.Tipo ret;
        ret = new Vehiculo.Tipo();
        ret.setNombre(nombreTxt.getText());
        ret.setCapacidad(Double.parseDouble(capacidadTxt.getText()));
        ret.setMarca(marcaTxt.getText());
        ret.setModelo(modeloTxt.getText());
        ret.setModelo(descripcionTxtArea.getText());
        return ret;
    }

    private boolean isValid() {
        return nombreTxt.validate() && capacidadTxt.validate();
    }

    @FXML
    private void onGuardarButtonClicked(MouseEvent e) {
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
}
