/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import java.io.IOException;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author cfoch
 */
public class GMapsHelper {
    private static final String API_KEY =
            "AIzaSyBuwWY5-875b_CNPq45NX1id5BuVVItz0A";
    private GeoApiContext context;

    private GMapsHelper() {
        context = new GeoApiContext.Builder().apiKey(API_KEY).build();

    }

    /**
     * Obtiene la latitud y longitud dada una dirección.
     * @param address
     * @return Una dupla de Double en caso de éxito (representando latitud y
     *  longitud). De lo contrario, null.
     * @throws ApiException
     * @throws InterruptedException
     * @throws IOException
     */
    public Pair<Double, Double> geocodeAddress(String address)
            throws ApiException, InterruptedException, IOException {
        GeocodingResult[] results =
                GeocodingApi.geocode(context, address).await();
        if (results.length == 0 || results[0].geometry == null ||
                results[0].geometry.location == null) {
            return null;
        }
        return Pair.of(results[0].geometry.location.lat,
                results[0].geometry.location.lng);
    }

    public static GMapsHelper getInstance() {
        return MapsHelperHolder.INSTANCE;
    }

    private static class MapsHelperHolder {

        private static final GMapsHelper INSTANCE = new GMapsHelper();
    }
}
