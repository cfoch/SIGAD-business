/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.jfoenix.controls.JFXButton;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.PerfilHelper;
import com.sigad.sigad.business.helpers.UsuarioHelper;
import com.sigad.sigad.personal.controller.PersonalController;
import static com.sigad.sigad.personal.controller.PersonalController.updateTable;
import com.sigad.sigad.utils.ui.UIFuncs;
import com.sigad.sigad.utils.ui.UIFuncs.Dialogs;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author cfoch
 */
public class RepartidoresInnerController extends PersonalController {
    public static final String viewPath =
            "/com/sigad/sigad/repartos/view/repartidores.fxml";

    @FXML
    StackPane hiddenSp;
    @FXML
    JFXButton genRutasBtn;

    @Override
    protected void getDataFromDB() {
        Perfil perfil;
        Long tiendaId;
        ArrayList<Usuario> lista;
        PerfilHelper perfilHelper = new PerfilHelper();
        UsuarioHelper userHelper = new UsuarioHelper();
        perfil = perfilHelper.getProfile("Repartidor");

        if (LoginController.user.getTienda() != null) {
            tiendaId = LoginController.user.getTienda().getId();
            lista = userHelper.getUsersByTiendaId(perfil,
                    tiendaId);
        } else {
            lista = userHelper.getUsers(perfil);
        }
        if(lista != null){
            lista.forEach((p) -> {
                updateTable(p);
            });
        }
        userHelper.close();
    }

    @Override
    protected void rowDoubleClickFunc(PersonalController.User selectedUser) {
        Pair<Node, FXMLLoader> pair;
        Node node;
        FXMLLoader loader;
        RepartosWrapperController controller;

        controller = new RepartosWrapperController(hiddenSp,
                selectedUser.getId());
        node = UIFuncs.<RepartosWrapperController>createNodeFromControllerFXML(
                controller, RepartosWrapperController.VIEW_PATH);
        UIFuncs.Dialogs.showDialog(
                hiddenSp,
                "Lista de repartos",
                node,
                new JFXButton(UIFuncs.Dialogs.BUTTON.CERRAR),
                true);
    }

    @FXML
    public void onGenRutasBtnClicked(ActionEvent e) {
        String dialogFXMLpath = RepartidoresDialogGenRutasController.viewPath;
        Node node = UIFuncs.<RepartidoresDialogGenRutasController>
                createNodeFromFXML(dialogFXMLpath);

        if (node == null) {
            return;
        }

        Dialogs.showDialog(hiddenSp,
                "Generación de rutas",
                node,
                new JFXButton(Dialogs.BUTTON.CERRAR),
                true);
    }
}
