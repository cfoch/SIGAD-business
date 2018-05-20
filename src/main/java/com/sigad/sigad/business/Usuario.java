/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author cfoch
 */
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    private String nombres;
    @NotNull
    private String apellidoPaterno;
    @NotNull
    private String apellidoMaterno;
    @NotNull
    @ManyToOne
    private Perfil perfil;
    private String telefono;
    @NotNull
    private String dni;
    private String celular;
    @NotNull
    private boolean activo;
    private String correo;
    private String intereses;
    @OneToMany(mappedBy="usuario")
    private Set<ClienteFecha> clienteFechas = new HashSet<ClienteFecha>();
    @OneToMany(mappedBy="usuario")
    private Set<ClienteDireccion> clienteDirecciones = new HashSet<ClienteDireccion>();
    @OneToMany(mappedBy="id.insumo")
    private Set<CapacidadTienda> capacidadTiendas = new HashSet<CapacidadTienda>();
    @OneToMany(mappedBy = "cliente")
    private Set<Pedido> pedidoCliente = new HashSet<Pedido>();
    
    @ManyToMany(mappedBy = "usuarios", cascade = { CascadeType.ALL })
    private Set<ProductoDescuento> descuentos = new HashSet<ProductoDescuento>();
    
    /**
     * Constructor.
     */
    public Usuario() {
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
     * @return the nombres
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * @param nombres the nombres to set
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * @return the apellidoPaterno
     */
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    /**
     * @param apellidoPaterno the apellidoPaterno to set
     */
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    /**
     * @return the apellidoMaterno
     */
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    /**
     * @param apellidoMaterno the apellidoMaterno to set
     */
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    /**
     * @return the perfil
     */
    public Perfil getPerfil() {
        return perfil;
    }

    /**
     * @param perfil the perfil to set
     */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * @param dni the dni to set
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * @return the celular
     */
    public String getCelular() {
        return celular;
    }

    /**
     * @param celular the celular to set
     */
    public void setCelular(String celular) {
        this.celular = celular;
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
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return the intereses
     */
    public String getIntereses() {
        return intereses;
    }

    /**
     * @param intereses the intereses to set
     */
    public void setIntereses(String intereses) {
        this.intereses = intereses;
    }

    /**
     * @return the clienteFechas
     */
    public Set<ClienteFecha> getClienteFechas() {
        return clienteFechas;
    }

    /**
     * @return the clienteFechas
     */
    public void addClienteFecha(ClienteFecha clienteFecha) {
        if (clienteFecha.getUsuario() == this) {
            clienteFechas.add(clienteFecha);
        }
    }

    /**
     * @return the clienteDirecciones
     */
    public void addClienteDirecciones(ClienteDireccion clienteDireccion) {
        if (clienteDireccion.getUsuario() == this) {
            clienteDirecciones.add(clienteDireccion);
        }
    }

    /**
     * @return the capacidadTiendas
     */
    public Set<CapacidadTienda> getCapacidadTiendas() {
        return capacidadTiendas;
    }

    /**
     * @param capacidadTienda the capacidadTienda to add
     */
    public void addCapacidadTienda(CapacidadTienda capacidadTienda) {
        capacidadTiendas.add(capacidadTienda);
    }

    /**
     * @return the pedidoCliente
     */
//    public Set<Pedido> getPedidoCliente() {
//        return pedidoCliente;
//    }

    /**
     * @param pedidoCliente the pedidoCliente to set
     */
//    public void setPedidoCliente(Set<Pedido> pedidoCliente) {
//        this.pedidoCliente = pedidoCliente;
//    }

    /**
     * @return the descuentos
     */
//    public Set<ProductoDescuento> getDescuentos() {
//        return descuentos;
//    }

    /**
     * @param descuentos the descuentos to set
     */
//    public void setDescuentos(Set<ProductoDescuento> descuentos) {
//        this.descuentos = descuentos;
//    }
}
