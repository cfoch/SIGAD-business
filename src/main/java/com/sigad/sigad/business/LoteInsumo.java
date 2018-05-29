/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author jorgeespinoza
 */
@Entity
public class LoteInsumo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @ManyToOne(optional=false)
    private Insumo insumo;
    
    @NotNull
    private Integer stockLogico;
    @NotNull
    private Integer stockFisico;
    
    @NotNull
    private Date fechaVencimiento;
    @NotNull
    private double costoUnitario;
    //cascade: un lote tiene que tener un detalle orden, sino no tiene sentido
    //cascade: se guardan automaticamente el loteInsumo en la creación de detalleCompra
    //onetoone fetch default eager
    @OneToOne(mappedBy = "loteInsumo",cascade = CascadeType.ALL, fetch = FetchType.LAZY) 
    private DetalleOrdenCompra detalleOrdenCompra;
    
    /**
     * Constructor.
     */
    public LoteInsumo() {
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
     * @return the insumo
     */
    public Insumo getInsumo() {
        return insumo;
    }

    /**
     * @param insumo the insumo to set
     */
    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    /**
     * @return the fechaVencimiento
     */
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fechaVencimiento to set
     */
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the costoUnitario
     */
    public double getCostoUnitario() {
        return costoUnitario;
    }

    /**
     * @param costoUnitario the costoUnitario to set
     */
    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    /**
     * @return the detalleOrdenCompra
     */
    public DetalleOrdenCompra getDetalleOrdenCompra() {
        return detalleOrdenCompra;
    }

    /**
     * @param detalleOrdenCompra the detalleOrdenCompra to set
     */
    public void setDetalleOrdenCompra(DetalleOrdenCompra detalleOrdenCompra) {
        this.detalleOrdenCompra = detalleOrdenCompra;
    }

    /**
     * @return the stockLogico
     */
    public Integer getStockLogico() {
        return stockLogico;
    }

    /**
     * @param stockLogico the stockLogico to set
     */
    public void setStockLogico(Integer stockLogico) {
        this.stockLogico = stockLogico;
    }

    /**
     * @return the stockFisico
     */
    public Integer getStockFisico() {
        return stockFisico;
    }

    /**
     * @param stockFisico the stockFisico to set
     */
    public void setStockFisico(Integer stockFisico) {
        this.stockFisico = stockFisico;
    }
}
