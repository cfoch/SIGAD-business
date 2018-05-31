/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.helpers.cargaMasiva;

/**
 *
 * @author paul
 */
public class HojaReporte {
    
    private String nombreHoja;
    private int casosExitosos;
    private int casosFallidos;

    /**
     * @return the casosExitosos
     */
    public int getCasosExitosos() {
        return casosExitosos;
    }

    /**
     * @param casosExitosos the casosExitosos to set
     */
    public void setCasosExitosos(int casosExitosos) {
        this.casosExitosos = casosExitosos;
    }

    /**
     * @return the casosFallidos
     */
    public int getCasosFallidos() {
        return casosFallidos;
    }

    /**
     * @param casosFallidos the casosFallidos to set
     */
    public void setCasosFallidos(int casosFallidos) {
        this.casosFallidos = casosFallidos;
    }

    /**
     * @return the nombreHoja
     */
    public String getNombreHoja() {
        return nombreHoja;
    }

    /**
     * @param nombreHoja the nombreHoja to set
     */
    public void setNombreHoja(String nombreHoja) {
        this.nombreHoja = nombreHoja;
    }
    
}
