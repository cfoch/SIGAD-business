/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author cfoch
 */
@Entity
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String nombre;
    private String imagen;
    private String descripcion;
    private int tiempoVida; //dias
    private double precio; //ojo que esto puede variar
    @NotNull
    private int stockTotalLogico;
    @NotNull
    private int stockTotalFisico;
    @NotNull
    private boolean activo;
    private Double volumen;

    @OneToMany(mappedBy = "insumo")
    private Set<LoteInsumo> lotesInsumo = new HashSet<LoteInsumo>();

    @OneToMany(mappedBy = "insumo")
    private Set<ProductoInsumo> productos = new HashSet<ProductoInsumo>();

    /**
     * Constructor.
     */
    public Insumo() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Insumo) {
            Insumo ins = (Insumo) obj;
            return ins.getId().equals(getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

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
     * @return the tiempoVida
     */
    public int getTiempoVida() {
        return tiempoVida;
    }

    /**
     * @param tiempoVida the tiempoVida to set
     */
    public void setTiempoVida(int tiempoVida) {
        this.tiempoVida = tiempoVida;
    }

    /**
     * @return the precio
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * @return the activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * @return the volumen
     */
    public Double isVolumen() {
        return volumen;
    }

    /**
     * @return the volumen
     */
    public double getVolumen() {
        return volumen;
    }

    /**
     * @param volumen the volumen to set
     */
    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }

    /**
     * @return the stockTotalLogico
     */
    public int getStockTotalLogico() {
        return stockTotalLogico;
    }

    /**
     * @param stockTotalLogico the stockTotalLogico to set
     */
    public void setStockTotalLogico(int stockTotalLogico) {
        this.stockTotalLogico = stockTotalLogico;
    }

    /**
     * @return the stockTotalFisico
     */
    public int getStockTotalFisico() {
        return stockTotalFisico;
    }

    /**
     * @param stockTotalFisico the stockTotalFisico to set
     */
    public void setStockTotalFisico(int stockTotalFisico) {
        this.stockTotalFisico = stockTotalFisico;
    }

    /**
     * @return the productos
     */
    public Set<ProductoInsumo> getProductos() {
        return productos;
    }

    /**
     * @param productos the productos to set
     */
    public void setProductos(Set<ProductoInsumo> productos) {
        this.productos = productos;
    }

}
