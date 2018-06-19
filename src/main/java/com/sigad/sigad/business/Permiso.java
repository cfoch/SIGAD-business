/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author Alexandra
 */
@Entity
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer orden;
    private String menu;
    private String icono;
    @ManyToMany(mappedBy = "permisos", cascade = { CascadeType.ALL })
    private Set<Perfil> perfiles = new HashSet<>();

    public Permiso() {
    }

    public Permiso(int orden, String menu, String icono) {
        this.orden = orden;
        this.menu = menu;
        this.icono = icono;
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
     * @return the opcionmany
     */
    public String getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(String menu) {
        this.menu = menu;
    }

    /**
     * @return the icono
     */
    public String getIcono() {
        return icono;
    }

    /**
     * @param icono the icono to set
     */
    public void setIcono(String icono) {
        this.icono = icono;
    }
    
    /**
     * @return the perfil
     */
    public Set<Perfil> getPerfil() {
        return perfiles;
    }

    /**
     * @param perfil the perfil to set
     */
    public void setPerfil(Set<Perfil> perfil) {
        this.perfiles = perfil;
    }

    /**
     * @param perfil the perfil to set
     */
    public void addPerfil(Perfil perfil) {
        this.perfiles.add(perfil);
    }

    /**
     * @return the orden
     */
    public int getOrden() {
        return orden;
    }

    /**
     * @param orden the orden to set
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }
   
}
