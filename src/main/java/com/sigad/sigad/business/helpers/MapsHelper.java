/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.grupo1.simulated_annealing.Locacion;
import java.io.IOException;

/**
 *
 * @author cfoch
 */
public class MapsHelper {
    private static final String API_KEY =
            "AIzaSyBuwWY5-875b_CNPq45NX1id5BuVVItz0A";
    private GeoApiContext context;

    private MapsHelper() {
        context = new GeoApiContext.Builder().apiKey(API_KEY).build();

    }

    public static String locacionToString(Locacion locacion) {
        return String.format("%f,%f", locacion.getX(), locacion.getY());
    }

    public static String [] locacionesToArrayString(Locacion [] locaciones) {
        String [] ret = new String[locaciones.length];
        int i;
        for (i = 0; i < locaciones.length; i++) {
            ret[i] = locacionToString(locaciones[i]);
        }
        return ret;
    }

    public final LatLng locacionToLatLng(final Locacion locacion) {
        LatLng st;
        st = new LatLng(locacion.getX(), locacion.getY());
        return st;
    }

    public final LatLng[] locacionesToArrayLatLng(
            final Locacion [] locaciones) {
        LatLng [] ret;
        int i;
        ret = new LatLng[locaciones.length];
        for (i = 0; i < locaciones.length; i++) {
            ret[i] = new LatLng(locaciones[i].getX(), locaciones[i].getY());
        }
        return ret;
    }

    public DistanceMatrix getDistanceMatrix(Locacion [] origins,
            Locacion [] destinations) throws ApiException, InterruptedException, IOException {
        LatLng [] originsLatLng;
        LatLng [] destinationsLatLng;
        DistanceMatrixApiRequest req;
        DistanceMatrix matrix;

        originsLatLng = locacionesToArrayLatLng(origins);
        destinationsLatLng = locacionesToArrayLatLng(destinations);
        req = DistanceMatrixApi.newRequest(context);
        req = req.origins(originsLatLng).destinations(destinationsLatLng);
        matrix = req.await();
        return matrix;
    }

    public static double[][] distanceMatrixTo2DArray(
            DistanceMatrix distanceMatrix, boolean average) {
        int i, j;
        double [][] ret = new double[distanceMatrix.rows.length][];
        for (i = 0; i < distanceMatrix.rows.length; i++) {
            ret[i] = new double[distanceMatrix.rows[i].elements.length];
            for (j = 0; j < distanceMatrix.rows[i].elements.length; j++) {
                double d1;
                d1 = distanceMatrix.rows[i].elements[j].distance.inMeters;
                if (!average) {
                    ret[i][j] = d1;
                } else {
                    double d2;
                    d2 = distanceMatrix.rows[j].elements[i].distance.inMeters;
                    ret[i][j] = (d1 + d2) / 2;
                }
            }
        }
        return ret;
    }

    public static MapsHelper getInstance() {
        return MapsHelperHolder.INSTANCE;
    }

    private static class MapsHelperHolder {

        private static final MapsHelper INSTANCE = new MapsHelper();
    }
}
