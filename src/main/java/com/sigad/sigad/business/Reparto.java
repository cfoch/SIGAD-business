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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Alexandra
 */
@Entity
public class Reparto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @NotNull
    private Date fecha;
    @NotNull
    private String ruta;
    //fk
    @ManyToOne
    private Usuario repartidor;
    
    @ManyToOne
    private Vehiculo vehiculo;
    
    @OneToMany(mappedBy = "reparto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DetalleReparto> detallesReparto = new HashSet<>();

    public Reparto() {
    }
    
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
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
        this.fecha = fecha;
    }

    /**
     * @return the ruta
     */
    public String getRuta() {
        return ruta;
    }

    /**
     * @param ruta the ruta to set
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    /**
     * @return the repartidor
     */
    public Usuario getRepartidor() {
        return repartidor;
    }

    /**
     * @param repartidor the repartidor to set
     */
    public void setRepartidor(Usuario repartidor) {
        this.repartidor = repartidor;
    }

    /**
     * @return the vehiculo
     */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /**
     * @param vehiculo the vehiculo to set
     */
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    /**
     * @return the detallesReparto
     */
    public Set<DetalleReparto> getDetallesReparto() {
        return detallesReparto;
    }

    /**
     * @param detallesReparto the detallesReparto to set
     */
    public void setDetallesReparto(Set<DetalleReparto> detallesReparto) {
        this.detallesReparto = detallesReparto;
    }
}
