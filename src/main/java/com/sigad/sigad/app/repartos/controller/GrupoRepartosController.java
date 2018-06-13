/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.sigad.sigad.utils.ui.UICRUDViewListarController;
import com.sigad.sigad.utils.ui.UICRUDViewWrapperController;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author cfoch
 */
public class GrupoRepartosController extends UICRUDViewWrapperController {

    public GrupoRepartosController(Class<? extends UICRUDViewListarController> listarControllerKlass) throws InstantiationException, IllegalAccessException {
        super(listarControllerKlass);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        setTitulo("Grupo de repartos");
    }

}
