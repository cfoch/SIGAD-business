package com.sigad.sigad.validations;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;
import java.util.Optional;

public class ConfirmationValidator {

    public static boolean onlyStringOrNumbers(String cad) {
        for (char c : cad.toCharArray()) {
            if (!Character.isDigit(c) && !Character.isAlphabetic(c) && c != ' ')
                return false;
        }
        return true;
    }

    public static boolean onlyFourNumbers(String cad) {
        for (char c : cad.toCharArray()) {
            if (!Character.isDigit(c) && !Character.isAlphabetic(c) && c != ' ')
                return false;
        }
        return true;
    }

    public static boolean onlyNumbers(String cad) {
        for (char c : cad.toCharArray()) {
            if (!Character.isDigit(c) && c != ' ') return false;
        }
        return true;
    }

    public static boolean onlyString(String cad) {
        for (char c : cad.toCharArray()) {
            if (!Character.isAlphabetic(c) && c != ' ') return false;
        }
        return true;
    }

    public static boolean emailFormat(String cad) {
        if (!(cad.contains("@") && cad.contains("."))
                || cad.indexOf('@') == 0
                || !onlyStringOrNumbers(cad.toString().replace('@', 'a').replace('.', 'a')))
            return false;
        else return true;
    }

    public static boolean phoneFormat(String cad) {
        for (char c : cad.toCharArray()) {
            if (!Character.isDigit(c) && c != '(' && c != ')' && c != '-' && c != ' ') return false;
        }
        return true;
    }

    public static void showDialogError(String dialogText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("¡Error!");
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.UTILITY);
        alert.setContentText(dialogText);
        Optional<ButtonType> result = alert.showAndWait();
    }


}
