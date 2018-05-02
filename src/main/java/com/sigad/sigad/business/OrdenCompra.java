/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author jorgeespinoza
 */
@Entity
public class OrdenCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @NotNull
    private double precioTotal;
    @NotNull
    private Date fecha; 
    //onetomany fetch defult lazy
    @OneToMany(mappedBy="orden")
    private Set<DetalleOrdenCompra> detallesOrden = new HashSet<DetalleOrdenCompra>();
    //manytoone fetch default eager -> lazy para evitar la carga inmediata del proveedor
    @ManyToOne(fetch = FetchType.LAZY) 
    private Proveedor proveedor;
    @ManyToOne(fetch = FetchType.LAZY) 
    private Usuario usuario;
    
    /**
     * Constructor.
     */
    public OrdenCompra(){
        
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the precioTotal
     */
    public double getPrecioTotal() {
        return precioTotal;
    }

    /**
     * @param precioTotal the precioTotal to set
     */
    public void setPrecioTotal(double precioTotal) {
        this.setPrecioTotal(precioTotal);
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.setFecha(fecha);
    }

    /**
     * @return the detalleOrdenCompra
     */
    public Set<DetalleOrdenCompra> getDetalleOrdenCompra() {
        return detallesOrden;
    }

    /**
     * @param detalleOrdenCompra the detalleOrdenCompra to set
     */
    public void setDetalleOrdenCompra(Set<DetalleOrdenCompra> detallesOrden) {
        this.detallesOrden = detallesOrden;
    }

    /**
     * @return the proveedor
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
    
    
}
