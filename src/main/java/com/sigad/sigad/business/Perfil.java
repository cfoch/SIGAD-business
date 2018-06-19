/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import javax.persistence.Column;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.validation.constraints.NotNull;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author cfoch
 */
@Entity
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String nombre;
    private Boolean editable;
    private String descripcion;
    @NotNull
    private boolean activo;
    @ManyToMany
    private Set<Permiso> permisos = new HashSet<>();
    @OneToMany(mappedBy = "perfil",fetch = FetchType.LAZY)
    private Set<Usuario> usuarios = new HashSet<>();
    /**
     * Constructor.
     */
    public Perfil() {
        setEditable(true);
    }
    
    public Perfil(boolean editable) {
        setEditable(editable);
    }

    public Perfil(String nombre, String descripcion, boolean activo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }
    
    public Perfil(String nombre, String descripcion, boolean activo, Set<Permiso> permisos, boolean editable) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
        this.permisos = permisos;
        this.editable = editable;
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
     * @return the permisos
     */
    
    public Set<Permiso> getPermisos() {
        return permisos;
    }

    /**
     * @param permisos
     */
    public void setPermisos(Set<Permiso> permisos) {
        this.permisos = permisos;
    }

     
    public void addPermiso(Permiso permiso) {
        this.permisos.add(permiso);
    }

    /**
     * @return the usuarios
     */
    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    /**
     * @return the editable
     */
    public Boolean isEditable() {
        return editable;
    }

    /**
     * @param editable the editable to set
     */
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
   
    
}
