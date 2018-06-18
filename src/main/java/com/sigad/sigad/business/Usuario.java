/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

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
    @Email(message = "{user.email.invalid}")
    @Column(unique = true)
    private String correo;
    @Column(nullable = false)
    private String password;
    private String intereses;
    @OneToMany(mappedBy = "usuario")
    private Set<ClienteFecha> clienteFechas = new HashSet<ClienteFecha>();
    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClienteDireccion> clienteDirecciones = new HashSet<ClienteDireccion>(); 
    @OneToMany(mappedBy = "insumo")
    private Set<CapacidadTienda> capacidadTiendas = new HashSet<CapacidadTienda>();
    @OneToMany(mappedBy = "cliente")
    private Set<Pedido> pedidoCliente = new HashSet<Pedido>();
    @ManyToMany(mappedBy = "usuarios", cascade = {CascadeType.ALL})
    private Set<Producto> favoritos = new HashSet<>();
    @ManyToMany(mappedBy = "clientes", cascade = {CascadeType.ALL})
    private Set<ClienteDescuento> descuentoCliente = new HashSet<ClienteDescuento>();
            
    @ManyToOne
    private Tienda tienda;

    /**
     * Constructor.
     */
    public Usuario() {
    }

    public Usuario(String nombres, String apellidoPaterno,
            String apellidoMaterno, Perfil perfil, String telefono,
            String dni, String celular, boolean activo, String correo,
            String password, String intereses) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.perfil = perfil;
        this.telefono = telefono;
        this.dni = dni;
        this.celular = celular;
        this.activo = activo;
        this.correo = correo;
        this.password = password;
        this.intereses = intereses;

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Usuario) {
            Usuario u = (Usuario) o;
            return u.getDni().trim().equals(this.getDni().trim());

        }
        return super.equals(o); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.dni);
        return hash;
    }

    @Override
    public String toString() {
        String s = this.nombres + " " + this.apellidoPaterno + " " + this.apellidoMaterno;
        return s; //To change body of generated methods, choose Tools | Templates.
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

    public ArrayList<ClienteDireccion> getClienteDirecciones() {
        ArrayList<ClienteDireccion> direcciones = new ArrayList(clienteDirecciones);
        return direcciones;
    }
    
    public Set<ClienteDireccion> getClienteDireccionesSet() {
        return clienteDirecciones;
    }

    /**
     * @param clienteDirecciones the clienteDirecciones to set
     */
    public void setClienteDirecciones(Set<ClienteDireccion> clienteDirecciones) {
        this.clienteDirecciones.clear();;
        this.clienteDirecciones.addAll(clienteDirecciones);
    }

    public void cleanClienteDirecciones(){
        clienteDirecciones.clear();
    }
    
    /**
     * @return the clienteDirecciones
     */
    public void addClienteDirecciones(ClienteDireccion clienteDireccion) {
        if (clienteDirecciones == null) {
            clienteDirecciones = new HashSet<>();
        }
        clienteDirecciones.add(clienteDireccion);
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @return the pedidoCliente
     */
    public Set<Pedido> getPedidoCliente() {
        return pedidoCliente;
    }

    /**
     * @param pedidoCliente the pedidoCliente to set
     */
    public void setPedidoCliente(Set<Pedido> pedidoCliente) {
        this.pedidoCliente = pedidoCliente;
    }

    /**
     * @return the favoritos
     */
    public Set<Producto> getFavoritos() {
        return favoritos;
    }

    /**
     * @param favoritos the favoritos to set
     */
    public void setFavoritos(Set<Producto> favoritos) {
        this.favoritos = favoritos;
    }

    /**
     * @return the descuentoCliente
     */
    public Set<ClienteDescuento> getDescuentoCliente() {
        return descuentoCliente;
    }

    /**
     * @param descuentoCliente the descuentoCliente to set
     */
    public void setDescuentoCliente(Set<ClienteDescuento> descuentoCliente) {
        this.descuentoCliente = descuentoCliente;
    }
}
