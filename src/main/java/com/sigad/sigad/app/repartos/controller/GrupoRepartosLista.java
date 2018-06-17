/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.repartos.controller;

import com.grupo1.simulated_annealing.Locacion;
import com.jfoenix.controls.JFXButton;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Reparto;
import com.sigad.sigad.business.helpers.AlgoritmoHelper;
import com.sigad.sigad.business.helpers.GrupoRepartoHelper;
import com.sigad.sigad.business.helpers.PedidoHelper;
import com.sigad.sigad.business.helpers.RepartoHelper;
import com.sigad.sigad.business.helpers.TiendaHelper;
import com.sigad.sigad.utils.ui.UICRUDViewListarController;
import com.sigad.sigad.utils.ui.UIFuncs;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 *
 * @author cfoch
 */
public class GrupoRepartosLista extends UICRUDViewListarController {
    public GrupoRepartosLista() {
        addColumnNumeric("tiendaId", "Tienda");
        addColumnString("fecha", "Fecha");
        addColumnString("turno", "Turno");
    }

    @Override
    public List<UICRUDViewListarController.Info> populate() {
        ArrayList<UICRUDViewListarController.Info> ret = new ArrayList<>();
        List<?> res;
        int i;
        GrupoRepartoHelper helper = new GrupoRepartoHelper();
        if (LoginController.user.getTienda() != null) {
            Long tiendaId = LoginController.user.getTienda().getId();
            res = helper.getRepartoGrupos(tiendaId);
        } else {
            res = helper.getRepartoGrupos(null);
        }
        System.out.println("res: " + res.size());
        for (i = 0; i < res.size(); i++) {
            Info info;
            TiendaHelper helperTienda = new TiendaHelper();
            Object[] row = (Object []) res.get(i);
            Long tiendaId = (Long) row[0];
            String fecha = (String) row[1];
            String turno = (String) row[2];
            String tiendaNombre =
                    helperTienda.getStore(tiendaId.intValue()).getDescripcion();
            helperTienda.close();
            info = new Info(tiendaId, tiendaNombre, fecha, turno);
            ret.add(info);
        }
        helper.close();
        return ret;
    }

    @Override
    protected void rowDoubleClickFunc(
            UICRUDViewListarController.Info selectedInfo) {
        GrupoRepartosLista.Info info = (GrupoRepartosLista.Info) selectedInfo;
        StackPane sp = getParentStackPane();
        if (sp != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                int i;
                RepartoMapaController controllerMapa;
                List<Reparto> repartos;
                List<List<Locacion>> solucion = new ArrayList<>();
                String marshallizedSolution;
                RepartoHelper helperReparto = new RepartoHelper();
                AlgoritmoHelper helperAlgoritmo = new AlgoritmoHelper();
                Date fecha = format.parse(info.fecha);
                Node nodeMapa;

                repartos = helperReparto.getRepartos(fecha, info.tiendaId,
                        info.turno);

                controllerMapa = new RepartoMapaController(repartos);
                nodeMapa = UIFuncs.createNodeFromControllerFXML(controllerMapa,
                        RepartoMapaController.VIEW_PATH);
                UIFuncs.Dialogs.showDialog(sp,
                        "Mapa de repatos",
                        nodeMapa,
                        new JFXButton(UIFuncs.Dialogs.BUTTON.CERRAR),
                        true);
            } catch (ParseException ex) {
                Logger.getLogger(GrupoRepartosLista.class.getName())
                        .log(Level.SEVERE, "Mal formato de fecha.", ex);
                return;
            }
        }
    }

    public static class Info extends UICRUDViewListarController.Info {
        StringProperty tiendaProp;
        StringProperty fechaProp;
        StringProperty turnoProp;

        Long tiendaId;
        String turno;
        String fecha;

        public Info(Long tiendaId, String tiendaNombre, String fecha,
                String turno) {
            super();

            this.tiendaId = tiendaId;
            this.turno = turno;
            this.fecha = fecha;

            tiendaProp = new SimpleStringProperty(tiendaNombre);
            fechaProp = new SimpleStringProperty(fecha);
            turnoProp = new SimpleStringProperty(
                    PedidoHelper.stringifyTurno(turno));
            this.addProperty("tiendaId", tiendaProp);
            this.addProperty("fecha", fechaProp);
            this.addProperty("turno", turnoProp);
        }
    }
}
