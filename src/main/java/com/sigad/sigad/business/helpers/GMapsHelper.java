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
import java.io.InputStream;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cfoch
 */
public class GMapsHelper {
    private static final String API_KEY =
            "AIzaSyBuwWY5-875b_CNPq45NX1id5BuVVItz0A";
    private GeoApiContext context;

    public GMapsHelper() {
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

    public JSONObject reverseGeoCode(Double lat, Double lng) {
        HttpGet httpGet = new HttpGet(String.format(
                "http://maps.google.com/maps/api/geocode/json?latlng=%s,%s"
                        + "&sensor=false", lat, lng));

        httpGet.setHeader("Content-Type", "text/plain; charset=UTF-8");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static GMapsHelper getInstance() {
        return MapsHelperHolder.INSTANCE;
    }

    private static class MapsHelperHolder {

        private static final GMapsHelper INSTANCE = new GMapsHelper();
    }
}
