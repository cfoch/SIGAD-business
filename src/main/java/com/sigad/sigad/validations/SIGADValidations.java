/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.validations;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ObservableValue;

/**
 * Methods to validate JFX input fields.
 * @author cfoch
 */
public class SIGADValidations {
    public class MSG {
        public final static String REQUIRED =
                "Este campo no puede estar vacío.";
        public final static String NUMERIC =
                "Este campo debe contener un número.";
    }
    
    public interface IValidatorFunc<T> {
        public void run(T input, Boolean oldValue, Boolean newValue);
    }

    private static void setupSimpleValidationFunc(JFXTextField inputField,
            Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            inputField.validate();
        }
    }

    private static void setupSimpleValidationFunc(JFXTextArea inputField,
            Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            inputField.validate();
        }
    }
    
    private static void setupValidationListener(JFXTextField inputField,
            IValidatorFunc validationFunc) {
        ReadOnlyBooleanProperty prop;
        prop = inputField.focusedProperty();
        prop.addListener((ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) -> {
            validationFunc.run(inputField, oldValue, newValue);
        });
    }

    private static void setupValidationListener(JFXTextArea inputField,
            IValidatorFunc validationFunc) {
        ReadOnlyBooleanProperty prop;
        prop = inputField.focusedProperty();
        prop.addListener((ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) -> {
            validationFunc.run(inputField, oldValue, newValue);
        });
    }

    public static void setupSimpleValidationListener(JFXTextField inputField) {
        setupValidationListener(inputField, (IValidatorFunc<JFXTextField>) (
                JFXTextField input, Boolean oldValue, Boolean newValue) -> {
            setupSimpleValidationFunc(input, oldValue, newValue);
        });
    }

    public static void setupSimpleValidationListener(JFXTextArea inputField) {
        setupValidationListener(inputField, (IValidatorFunc<JFXTextArea>) (
                JFXTextArea input, Boolean oldValue, Boolean newValue) -> {
            setupSimpleValidationFunc(input, oldValue, newValue);
        });
    }
}
