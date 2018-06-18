/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import java.text.DecimalFormat;

/**
 *
 * @author Alexandra
 */
public class GeneralHelper {

    public static Double roundTwoDecimals(double d) {

        try {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            String temp = twoDForm.format(d).replaceAll(",", ".");
            Double ret = Double.valueOf(temp);
            return ret;
        } catch (Exception e) {
            System.out.println("Error en parseo de "  +  String.valueOf(d));
            return d;
        }
        
    }

    public static boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            // s is not numeric
            return false;
        }
    }

    public static boolean isNumericDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            // s is not numeric
            return false;
        }
    }
}
