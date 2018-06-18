/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author jorgeespinoza
 */
@Entity
public class Producto {

  
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    private String nombre;
    private double peso;
    private String imagen;
    private String descripcion;
    @NotNull
    private int stockLogico;
    @NotNull
    private int stockFisico;
    @ManyToOne
    private ProductoCategoria categoria;
    @NotNull
    private boolean activo;
    @ManyToOne
    private ProductoFragilidad fragilidad;
    private double volumen;
    private Double precio;///venta
    private Double precioCompra;//costo

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductosCombos> combos = new HashSet<>();

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductoInsumo> insumos = new HashSet<>();
    
    @ManyToMany
    private Set<Usuario> usuarios = new HashSet<>();

    /**
     * Constructor.
     */
    public Producto() {
    }

    public Producto(String nombre, String imagen, Integer stock, Double precio, Boolean activo) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.stockLogico = stock;
        this.stockFisico = stock;
        this.activo = activo;
        this.precio = precio;
    }

    public boolean equals(Object o) {
        if (o instanceof Producto) {
            Producto pl = (Producto) o;
            return pl.id.equals(this.id);
        }
        return super.equals(o); //To change body of generated methods, choose Tools | Templates.
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
     * @return the precios
     */
//    public Set<ProductoPrecio> getPrecios() {
//        return precios;
//    }
    /**
     * @param precios the precios to set
     */
//    public void setPrecios(Set<ProductoPrecio> precios) {
//        this.precios = precios;
//    }
    /**
     * @return the peso
     */
    public double getPeso() {
        return peso;
    }

    /**
     * @param peso the peso to set
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * @return the imagen
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * @param imagen the imagen to set
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
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
     * @return the categoria
     */
    public ProductoCategoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(ProductoCategoria categoria) {
        this.categoria = categoria;
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
     * @return the fragilidad
     */
    public ProductoFragilidad getFragilidad() {
        return fragilidad;
    }

    /**
     * @param fragilidad the fragilidad to set
     */
    public void setFragilidad(ProductoFragilidad fragilidad) {
        this.fragilidad = fragilidad;
    }

    /**
     * @return the volumen
     */
    public double getVolumen() {
        return volumen;
    }

    /**
     * @param volumen the volumen to set
     */
    public void setVolumen(double volumen) {
        this.volumen = volumen;
    }

    /**
     * @return the precio
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * @return the stockLogico
     */
    public int getStockLogico() {
        return stockLogico;
    }

    /**
     * @param stockLogico the stockLogico to set
     */
    public void setStockLogico(int stockLogico) {
        this.stockLogico = stockLogico;
    }

    /**
     * @return the stockFisico
     */
    public int getStockFisico() {
        return stockFisico;
    }

    /**
     * @param stockFisico the stockFisico to set
     */
    public void setStockFisico(int stockFisico) {
        this.stockFisico = stockFisico;
    }

    /**
     * @return the insumos
     */
    public Set<Insumo> getInsumos() {
        Set<Insumo> insumosOb = new HashSet<>();
        insumos.forEach((t) -> {
            insumosOb.add(t.getInsumo());
        });
        return insumosOb;
    }

    public Set<ProductoInsumo> getProductoxInsumos() {
        return insumos;
    }
    
    
    public Boolean hasInsumo(Insumo insumo) {
        return getInsumos().contains(insumo);
    }
    /**
     * @param insumos the insumos to set
     */
    public void setInsumos(Set<ProductoInsumo> insumos) {
        this.insumos = insumos;
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
<<<<<<< HEAD
     * @return the combos
     */
    public Set<ProductosCombos> getCombos() {
        return combos;
    }

    /**
     * @param combos the combos to set
     */
    public void setCombos(Set<ProductosCombos> combos) {
        this.combos = combos;
    }
    /**
=======
>>>>>>> 7f3200619b35784707a29e1624d000e9da5bc458
     * @return the precioCompra
     */
    public Double getPrecioCompra() {
        return precioCompra;
    }

    /**
     * @param precioCompra the precioCompra to set
     */
    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    
}
