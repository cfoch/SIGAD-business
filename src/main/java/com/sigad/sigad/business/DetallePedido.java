/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
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
    @ManyToOne
    private Producto producto;
    
    
    
    @ManyToOne
    private ProductoDescuento descuento;
    
    @ManyToOne
    private ComboPromocion combo;
    
    @ManyToOne
    private ProductoCategoriaDescuento descuentoCategoria;
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

    public DetallePedido( boolean activo, Integer cantidad, Double precioUnitario, Integer numEntregados, Producto producto, Pedido pedido) {
        this.activo = activo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.numEntregados = numEntregados;
        this.producto = producto;
        this.pedido = pedido;
    }
    
    public DetallePedido( boolean activo, Integer cantidad, Double precioUnitario, Integer numEntregados, Producto producto, Pedido pedido, ProductoDescuento descuento) {
        this.activo = activo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.numEntregados = numEntregados;
        this.producto = producto;
        this.pedido = pedido;
        this.descuento = descuento;
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

    /**
     * @return the descuento
     */
    public ProductoDescuento getDescuentoProducto() {
        return descuento;
    }

    /**
     * @param descuento the descuento to set
     */
    public void setDescuentoProducto(ProductoDescuento descuento) {
        this.descuento = descuento;
    }

    /**
     * @return the combo
     */
    public ComboPromocion getCombo() {
        return combo;
    }

    /**
     * @param combo the combo to set
     */
    public void setCombo(ComboPromocion combo) {
        this.combo = combo;
    }

    /**
     * @return the descuentoCategoria
     */
    public ProductoCategoriaDescuento getDescuentoCategoria() {
        return descuentoCategoria;
    }

    /**
     * @param descuentoCategoria the descuentoCategoria to set
     */
    public void setDescuentoCategoria(ProductoCategoriaDescuento descuentoCategoria) {
        this.descuentoCategoria = descuentoCategoria;
    }

    /**
     * @return the pedido
     */
    public Pedido getPedido() {
        return pedido;
    }

    /**
     * @param pedido the pedido to set
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

  
    
}
