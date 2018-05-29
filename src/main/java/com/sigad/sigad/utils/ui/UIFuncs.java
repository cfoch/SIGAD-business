/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.utils.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author cfoch
 */
public class UIFuncs {
    public static class Dialogs {

        /**
         * Utilidad para crear un menu genérico.
         * @param <K> Un Enum que implemente toString, por ejemplo CREAR,
         *            EDITAR, BORRAR.
         */
        public static class SimplePopupMenuFactory<K extends Enum<K>> {
            private ArrayList<JFXButton> buttonsSorted;
            private HashMap<K, JFXButton> buttonsMap;
            private JFXPopup popup;

            public SimplePopupMenuFactory(K [] enumValues) {
                int i;
                popup = null;
                buttonsSorted = new ArrayList<>();
                buttonsMap = new HashMap<>();

                System.out.println("Length: " + enumValues.length);
                for (i = 0; i < enumValues.length; i++) {
                    JFXButton button;
                    button = new JFXButton(enumValues[i].toString());
                    System.out.println("but:" + enumValues[i].toString());
                    buttonsSorted.add(button);
                    buttonsMap.put(enumValues[i], button);
                };
            }

            public JFXButton getButton(K enumValue) {
                return buttonsMap.get(enumValue);
            }

            public JFXPopup getPopup() {
                if (popup == null) {
                    VBox vBox;

                    vBox = new VBox();
                    buttonsSorted.forEach((button) -> {
                        vBox.getChildren().add(button);
                        button.setPadding(new Insets(20));
                        button.setPrefSize(145, 40);
                    });
                    popup = new JFXPopup();
                    popup.setPopupContent(vBox);
                }
                return popup;
            }
        }

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

        /**
         * Shows a dialog displaying a message.
         * @param pane The StackPane
         * @param heading The title.
         * @param body The text of the body.
         * @param textButton The text of the single button.
         */
        public static void showMsg(StackPane pane, String heading, String body,
                String textButton) {
            showDialog(pane, heading, new Text(body), textButton);
        }

        /**
         * Shows a dialog with a generic node.
         * @param pane The StackPane
         * @param heading The title.
         * @param nodeBody A generic node.
         * @param textButton The text of the single button.
         */
        public static void showDialog(StackPane pane, String heading,
                javafx.scene.Node nodeBody, String textButton) {
            JFXDialogLayout content;
            JFXDialog dialog;
            JFXButton button;

            content = new JFXDialogLayout();
            content.setHeading(new Text(heading));
            content.setBody(nodeBody);

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
