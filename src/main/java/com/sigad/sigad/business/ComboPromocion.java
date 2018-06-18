/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Alexandra
 */
@Entity
public class ComboPromocion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nombre;
    private Double preciounitario;//base
    private Double preciounireal;//real
    private Integer maxDisponible;
    private String descripcion;
    private Integer numVendidos;
    private Date fechaInicio;
    private Date fechaFin;
    private Boolean activo;
    private Integer duracionDias;
    private Double volumen;
    private String imagen;
    @OneToMany(mappedBy = "combopromocion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductosCombos> productosxCombo = new HashSet<>();

    public ComboPromocion() {
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the preciounitario
     */
    public Double getPreciounitario() {
        return preciounitario;
    }

    /**
     * @param preciounitario the preciounitario to set
     */
    public void setPreciounitario(Double preciounitario) {
        this.preciounitario = preciounitario;
    }

    /**
     * @return the maxDisponible
     */
    public Integer getMaxDisponible() {
        return maxDisponible;
    }

    /**
     * @param maxDisponible the maxDisponible to set
     */
    public void setMaxDisponible(Integer maxDisponible) {
        this.maxDisponible = maxDisponible;
    }

    /**
     * @return the numVendidos
     */
    public Integer getNumVendidos() {
        return numVendidos;
    }

    /**
     * @param numVendidos the numVendidos to set
     */
    public void setNumVendidos(Integer numVendidos) {
        this.numVendidos = numVendidos;
    }

    /**
     * @return the fechaInicio
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaFin
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * @return the activo
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    /**
     * @return the duracionDias
     */
    public Integer getDuracionDias() {
        return duracionDias;
    }

    /**
     * @param duracionDias the duracionDias to set
     */
    public void setDuracionDias(Integer duracionDias) {
        this.duracionDias = duracionDias;
    }

    /**
     * @return the productosxCombo
     */
    public Set<ProductosCombos> getProductosxCombo() {
        return productosxCombo;
    }

    public ArrayList<ProductosCombos> getProductosxComboArray() {
        return new ArrayList(productosxCombo);
    }

    /**
     * @param productosxCombo the productosxCombo to set
     */
    public void setProductosxCombo(Set<ProductosCombos> productosxCombo) {
        this.productosxCombo = productosxCombo;
    }

    /**
     * @return the preciounireal
     */
    public Double getPreciounireal() {
        return preciounireal;
    }

    /**
     * @param preciounireal the preciounireal to set
     */
    public void setPreciounireal(Double preciounireal) {
        this.preciounireal = preciounireal;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the imagen
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * @param imagen the imagen to set
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * @return the volumen
     */
    public Double getVolumen() {
        return volumen;
    }

    /**
     * @param volumen the volumen to set
     */
    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }
}
