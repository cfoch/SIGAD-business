/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.PathWrapper;
import com.graphhopper.api.GraphHopperWeb;
import com.graphhopper.util.shapes.GHPoint;
import com.grupo1.simulated_annealing.Locacion;

/**
 *
 * @author cfoch
 */
public class GraphHopperHelper {
    private static String URI = "http://localhost:8989/route";
    private GraphHopperWeb gh;

    private GraphHopperHelper() {
        gh = new GraphHopperWeb(URI);
    }

    public double requestDistance(GHPoint origin, GHPoint destination)
            throws Exception {
        GHRequest req;
        GHResponse fullRes;
        PathWrapper res;
        req = new GHRequest().addPoint(origin).addPoint(destination);
        req.setVehicle("car");
        req.getHints().put("elevation", false);
        req.getHints().put("instructions", false);
        req.getHints().put("calc_points", false);
        fullRes = gh.route(req);
        if (fullRes.hasErrors()) {
            throw new Exception("Error en el servidor u origen o destino fuera"
                    + "de límite.");
        }
        res = fullRes.getBest();
        return res.getDistance();
    }

    public double[][] getCostMatrix(Locacion [] locaciones,
            boolean average) throws Exception {
        int i, j;
        double[][] costMatrix =
                new double[locaciones.length][locaciones.length];
        for (i = 0; i < locaciones.length; i++) {
            for (j = 0; j < locaciones.length; j++) {
                Locacion lOrigin, lDestination;
                GHPoint pOrigin, pDestination;
                double distance1;
                lOrigin = locaciones[i];
                lDestination = locaciones[j];
                pOrigin = new GHPoint(lOrigin.getX(), lOrigin.getY());
                pDestination =
                        new GHPoint(lDestination.getX(), lDestination.getY());
                distance1 = requestDistance(pOrigin, pDestination);
                if (average) {
                    double distance2 = requestDistance(pDestination, pOrigin);
                    costMatrix[i][j] = (distance1 + distance2) / 2;
                } else {
                    costMatrix[i][j] = distance1;
                }
            }
        }
        return costMatrix;
    }

    public static GraphHopperHelper getInstance() {
        return GraphHopperHelperHolder.INSTANCE;
    }

    private static class GraphHopperHelperHolder {

        private static final GraphHopperHelper INSTANCE = new GraphHopperHelper();
    }
}
