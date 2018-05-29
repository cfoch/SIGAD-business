/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import javax.validation.constraints.NotNull;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author Alexandra
 */
@Entity
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotNull
    private boolean activo;
    @NotNull
    private Integer cantidad;
    @NotNull
    private Double precioUnitario;
    @NotNull
    private Integer numEntregados;
    
    //Fk
    @OneToOne( cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Producto producto;
    
    @ManyToOne(optional = false)
    private Pedido pedido;
    
    //Campos de auditoria
    @Column (name = "`timestamp`")
    @CreationTimestamp
    private Date timestamp;
    @Column(name = "updated_on")
    @UpdateTimestamp
    private Date updatedOn;

    public DetallePedido() {
    }

    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
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
     * @return the cantidad
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the precioUnitario
     */
    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * @param precioUnitario the precioUnitario to set
     */
    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * @return the numEntregados
     */
    public Integer getNumEntregados() {
        return numEntregados;
    }

    /**
     * @param numEntregados the numEntregados to set
     */
    public void setNumEntregados(Integer numEntregados) {
        this.numEntregados = numEntregados;
    }

//    /**
//     * @return the pedidoVenta
//     */
//    public Pedido getPedidoVenta() {
//        return pedidoVenta;
//    }
//
//    /**
//     * @param pedidoVenta the pedidoVenta to set
//     */
//    public void setPedidoVenta(Pedido pedidoVenta) {
//        this.pedidoVenta = pedidoVenta;
//    }

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
