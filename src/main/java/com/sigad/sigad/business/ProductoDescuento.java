/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Alexandra
 */
@Entity
public class ProductoDescuento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    
    @NotNull
    private Date fechaInicio ;
    @NotNull
    private Date fechaFin;
    private Double valorPct;
    private Boolean activo;
    private String codCupon;
    private Double valorDescuento;
    private Integer duracionDias;
    
    //Fk
    @ManyToOne
    private Producto producto;
    @ManyToMany
    private Set<Usuario> usuarios = new HashSet<>();

    public ProductoDescuento() {
    }

    
    /**
     * @return the id
     */
    public long getId() {
        return id;
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
     * @return the valorPct
     */
    public Double getValorPct() {
        return valorPct;
    }

    /**
     * @param valorPct the valorPct to set
     */
    public void setValorPct(Double valorPct) {
        this.valorPct = valorPct;
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
     * @return the codCupon
     */
    public String getCodCupon() {
        return codCupon;
    }

    /**
     * @param codCupon the codCupon to set
     */
    public void setCodCupon(String codCupon) {
        this.codCupon = codCupon;
    }

    /**
     * @return the valorDescuento
     */
    public Double getValorDescuento() {
        return valorDescuento;
    }

    /**
     * @param valorDescuento the valorDescuento to set
     */
    public void setValorDescuento(Double valorDescuento) {
        this.valorDescuento = valorDescuento;
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
     * @return the producto
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

}
