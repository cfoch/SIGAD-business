/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
        private double capacidad;
        private String marca;
        private String modelo;

        public Tipo() {
        }

        public Tipo(String nombre, double pesoSoportado) {
            setNombre(nombre);
            //setPesoSoportado(pesoSoportado);
            setCapacidad(pesoSoportado);
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
         * @return the capacidad
         */
        public double getCapacidad() {
            return capacidad;
        }

        /**
         * @param capacidad the capacidad to set
         */
        public void setCapacidad(double capacidad) {
            this.capacidad = capacidad;
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

        /**
         * Obtiene un formato entendido por simulated_annealing.
         * @return un tipo de simulated_annealing.
         */
        public com.grupo1.simulated_annealing.Vehiculo.Tipo getTipo() {
            com.grupo1.simulated_annealing.Vehiculo.Tipo tipo;
            tipo = new com.grupo1.simulated_annealing.Vehiculo.Tipo(
                    id.toString(), (int) capacidad);
            return tipo;
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
    private String nombre;
    private String descripcion;

    public Vehiculo() {
    }

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
}
