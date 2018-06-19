/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.helpers.cargaMasiva;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author paul
 */
public class CargaMasivaConstantes {
    // Tablas a cargar mediante consulta masiva
    public static final String TABLA_PRODUCTOCATEGORIA = "Categoria de Productos";
    public static final String TABLA_PERFILES = "Perfiles";
    public static final String TABLA_USUARIOS = "Usuarios";
    public static final String TABLA_PROVEEDORES = "Proveedores";
    public static final String TABLA_INSUMOS = "Insumos";
    public static final String TABLA_TIENDAS = "Tiendas";
    public static final String TABLA_TIPOMOV = "Tipo de Movimientos";
    public static final String TABLA_PERMISOS = "Permisos";
    public static final String TABLA_FRAGILIDAD = "Fragilidad de Producto";
    public static final String TABLA_PERFILXPERMISO = "Perfil x Permiso";
    public static final String TABLA_PRODUCTOS = "Productos";
    public static final String TABLA_TIPOPAGO = "Tipos de Pago";
    public static final String TABLA_PRODXINSUMO = "Producto x Insumo";
    public static final String TABLA_TIPOVEHICULOS = "Tipos de Vehiculo";
    public static final String TABLA_VEHICULOS = "Vehiculos";
    public static final String TABLA_PROVEEDORXINSUMO = "Proveedor x Insumo";
    public static final String TABLA_DESCUENTOSCATEGORIA = "Descuento de Categoria de Productos";
    public static final String TABLA_PRODUCTODESCUENTO = "Descuento de Productos";
    public static final String TABLA_DESCUENTOXUSUARIO = "Descuento de Usuarios";
    
    /* mantener esta lista ordenada de la siguiente manera : primero aquellas entidades que no tienen FK, luego aquellas que si tiene FK */
    public static ObservableList<String> getList(){
        return FXCollections.observableArrayList(
                TABLA_PRODUCTOCATEGORIA, TABLA_DESCUENTOSCATEGORIA, TABLA_TIPOPAGO,TABLA_TIENDAS,TABLA_PERFILES, TABLA_USUARIOS, TABLA_DESCUENTOXUSUARIO, TABLA_PROVEEDORES, TABLA_INSUMOS, TABLA_PROVEEDORXINSUMO,
                TABLA_TIPOMOV, TABLA_PERMISOS, TABLA_FRAGILIDAD, TABLA_PRODUCTOS, TABLA_PRODUCTODESCUENTO,TABLA_PERFILXPERMISO, TABLA_PRODXINSUMO, TABLA_TIPOVEHICULOS, TABLA_VEHICULOS
        );
    };
}
