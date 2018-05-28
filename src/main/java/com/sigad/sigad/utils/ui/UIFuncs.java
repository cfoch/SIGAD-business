/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.utils.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 * @author cfoch
 */
public class UIFuncs {
    public static class Dialogs {
        public static class HEADINGS {
            public static final String EXITO = "Éxito";
            public static final String ERROR = "Error";
        }

        public static class BUTTON {
            public static final String ACEPTAR = "Aceptar";
            public static final String CANCELAR = "Cancelar";
            public static final String CERRAR = "Cerrar";
        }

        public static class MESSAGES {
            public static final String DB_GENERIC_ERROR =
                    "Lo sentimos. Hubo un problema con la conexión.";
            public static final String CRUD_CREATE_SUCCESS =
                    "Creación exitosa.";
        }

        public static void showMsg(StackPane pane, String heading, String body,
                String textButton) {
            JFXDialogLayout content;
            JFXDialog dialog;
            JFXButton button;

            content = new JFXDialogLayout();
            content.setHeading(new Text(heading));
            content.setBody(new Text(body));

            dialog = new JFXDialog(pane, content,
                    JFXDialog.DialogTransition.CENTER);
            button = new JFXButton(textButton);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dialog.close();
                }
            });
            content.setActions(button);
            dialog.show();
        }
    }
}
