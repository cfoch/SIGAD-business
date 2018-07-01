/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

/**
 *
 * @author Alexandra
 */
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MovimientosTienda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer cantidadMovimiento;
    private Date fecha; 
    @ManyToOne
    private TipoMovimiento tipoMovimiento;
    @ManyToOne
    private Usuario trabajador;
    @ManyToOne
    private Tienda tienda;
    @ManyToOne
    private LoteInsumo LoteInsumo;
    @ManyToOne
    private Pedido pedido;
    public MovimientosTienda() {    
    }

    public MovimientosTienda(Integer cantidadMovimiento, Date fecha, TipoMovimiento tipoMovimiento, Usuario trabajador, Tienda tienda, LoteInsumo LoteInsumo, Pedido pedido) {
        this.cantidadMovimiento = cantidadMovimiento;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.trabajador = trabajador;
        this.tienda = tienda;
        this.LoteInsumo = LoteInsumo;
        this.pedido = pedido;
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
     * @return the cantidadMovimiento
     */
    public Integer getCantidadMovimiento() {
        return cantidadMovimiento;
    }

    /**
     * @param cantidadMovimiento the cantidadMovimiento to set
     */
    public void setCantidadMovimiento(Integer cantidadMovimiento) {
        this.cantidadMovimiento = cantidadMovimiento;
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
     * @return the tipoMovimiento
     */
    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    /**
     * @param tipoMovimiento the tipoMovimiento to set
     */
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    /**
     * @return the trabajador
     */
    public Usuario getTrabajador() {
        return trabajador;
    }

    /**
     * @param trabajador the trabajador to set
     */
    public void setTrabajador(Usuario trabajador) {
        this.trabajador = trabajador;
    }

    /**
     * @return the tienda
     */
    public Tienda getTienda() {
        return tienda;
    }

    /**
     * @param tienda the tienda to set
     */
    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    /**
     * @return the LoteTienda
     */
    public LoteInsumo getLoteInsumo() {
        return LoteInsumo;
    }

    /**
     * @param LoteTienda the LoteTienda to set
     */
    public void setLoteInsumo(LoteInsumo LoteTienda) {
        this.LoteInsumo = LoteTienda;
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
