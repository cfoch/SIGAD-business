/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import com.sun.istack.internal.NotNull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author cfoch
 */
@Entity
public class Vehiculo {
    @Entity
    public static class Tipo {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;
        @NotNull
        private String nombre;
        private String descripcion;
        @NotNull
        private double pesoSoportado;
        private String marca;
        private String modelo;

        public Tipo(String nombre, double pesoSoportado) {
            setNombre(nombre);
            setPesoSoportado(pesoSoportado);
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
         * @return the pesoSoportado
         */
        public double getPesoSoportado() {
            return pesoSoportado;
        }

        /**
         * @param pesoSoportado the pesoSoportado to set
         */
        public void setPesoSoportado(double pesoSoportado) {
            this.pesoSoportado = pesoSoportado;
        }

        /**
         * @return the marca
         */
        public String getMarca() {
            return marca;
        }

        /**
         * @param marca the marca to set
         */
        public void setMarca(String marca) {
            this.marca = marca;
        }

        /**
         * @return the modelo
         */
        public String getModelo() {
            return modelo;
        }

        /**
         * @param modelo the modelo to set
         */
        public void setModelo(String modelo) {
            this.modelo = modelo;
        }
        
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @NotNull
    private Vehiculo.Tipo tipo;
    @NotNull
    @NotBlank
    private String placa;
    private String descripcion;
    
    public Vehiculo(Vehiculo.Tipo tipo, String placa) {
        setTipo(tipo);
        setPlaca(placa);
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
     * @return the tipo
     */
    public Vehiculo.Tipo getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Vehiculo.Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the placa
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * @param placa the placa to set
     */
    public void setPlaca(String placa) {
        this.placa = placa;
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
}
