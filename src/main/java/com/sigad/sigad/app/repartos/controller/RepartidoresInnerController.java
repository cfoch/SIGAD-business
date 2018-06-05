/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.PerfilHelper;
import com.sigad.sigad.business.helpers.UsuarioHelper;
import com.sigad.sigad.personal.controller.PersonalController;
import static com.sigad.sigad.personal.controller.PersonalController.updateTable;
import java.util.ArrayList;

/**
 *
 * @author cfoch
 */
public class RepartidoresInnerController extends PersonalController {
    public static final String viewPath =
            "/com/sigad/sigad/repartos/view/repartidores.fxml";

    @Override
    protected void getDataFromDB() {
        Perfil perfil;
        PerfilHelper perfilHelper = new PerfilHelper();
        UsuarioHelper userHelper = new UsuarioHelper();
        perfil = perfilHelper.getProfile("Repartidor");

        ArrayList<Usuario> lista = userHelper.getUsers(perfil);
        if(lista != null){
            lista.forEach((p) -> {
                updateTable(p);
            });
        }
        userHelper.close();
    }
}
