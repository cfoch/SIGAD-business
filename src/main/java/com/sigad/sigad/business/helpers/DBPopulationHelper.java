/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.business.DetalleOrdenCompra;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.LoteInsumo;
import com.sigad.sigad.business.OrdenCompra;
import com.sigad.sigad.business.Proveedor;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Usuario;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cfoch
 */
public class DBPopulationHelper extends BaseHelper {
    static final int DEFAULT_LOTE_INSUMO_STOCK_FISICO = 0;
    static final int DEFAULT_LOTE_INSUMO_STOCK_LOGICO = 100;
    static final int DEFAULT_LOTE_INSUMO_DIAS_DESDE_HOY = 15;
    static final int DEFAULT_LOTE_INSUMO_COSTO_UNITARIO_MIN = 50;
    static final int DEFAULT_LOTE_INSUMO_COSTO_UNITARIO_MAX = 100;
    static final double DEFAULT_LOTE_INSUMO_PORCENTAJE_PRECIO = 0.72;

    // Todas las órdenes generadas tendrán estado RECIBIDO por ahora.
    static final int DEFAULT_N_ORDENES_COMPRA_RECIBIDAS = 25;
    static final int DEFAULT_N_ORDENES_COMPRA = 25;

    private LoteInsumo generarLoteInsumo(Tienda tienda, Insumo insumo,
            int stockFisico, int stockLogico, int diasDesdeHoy) {
        Calendar cal;
        LoteInsumo loteInsumo;
        Date fechaVencimiento;
        double costoUnitario;

        // Calcular fecha a 'diasDesdeHoy' dias adelante.
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, diasDesdeHoy);
        fechaVencimiento = cal.getTime();

        loteInsumo = new LoteInsumo();
        loteInsumo.setCostoUnitario(100);
        loteInsumo.setFechaVencimiento(fechaVencimiento);
        loteInsumo.setInsumo(insumo);
        loteInsumo.setStockFisico(stockFisico);
        loteInsumo.setStockLogico(stockLogico);
        loteInsumo.setTienda(tienda);

        insumo.setStockTotalFisico(stockFisico + insumo.getStockTotalFisico());
        insumo.setStockTotalLogico(stockLogico + insumo.getStockTotalLogico());

        return loteInsumo;
    }

    private DetalleOrdenCompra generarDetalleOrdenCompra(
            LoteInsumo loteInsumo) {
        double precioDetalle;
        DetalleOrdenCompra detalle = new DetalleOrdenCompra();

        precioDetalle =
                loteInsumo.getCostoUnitario() * loteInsumo.getStockLogico();
        detalle.setLoteInsumo(loteInsumo);
        detalle.setPrecioDetalle(precioDetalle);
        return detalle;
    }

    private OrdenCompra generarOrdenCompra(Tienda tienda,
            Usuario usuarioAdministradorTienda, Proveedor proveedor,
            List<LoteInsumo> lotesInsumo, boolean recibido) {
        int i;
        double precioTotal = 0.0;
        OrdenCompra orden = new OrdenCompra();
        Set<DetalleOrdenCompra> detalles = new HashSet<>();

        for (i = 0; i < lotesInsumo.size(); i++) {
            LoteInsumo lote = lotesInsumo.get(i);
            DetalleOrdenCompra detalle = generarDetalleOrdenCompra(lote);
            detalle.setOrden(orden);
            lote.setDetalleOrdenCompra(detalle);
            detalles.add(detalle);
            precioTotal += detalle.getPrecioDetalle();
        }

        orden.setDetalleOrdenCompra(detalles);
        orden.setFecha(new Date());
        orden.setPrecioTotal(precioTotal);
        orden.setProveedor(proveedor);
        orden.setRecibido(recibido);
        orden.setUsuario(usuarioAdministradorTienda);

        return orden;
    }

    private Proveedor getProveedorAlAzar() throws Exception {
        Proveedor proveedor;
        try {
            proveedor = (Proveedor) session
                    .createQuery("select p from Proveedor p order by rand()")
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            throw new Exception("No se pudo obtener un proveedor. ¿Existe?");
        }
        return proveedor;
    }

    private Usuario getUsuarioOrdenCompraAlAzar() throws Exception {
        Usuario usuario = null;
        String perfil1 = "Encargado de almacen";
        try {
            usuario = (Usuario) session
                    .createQuery("select u from Usuario u where "
                            + "u.perfil.nombre = :perfil1 and "
                            + "u.tienda is not null order by rand()")
                    .setParameter("perfil1", perfil1)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            throw new Exception(String.format("No se pudo obtener un usuario "
//                    + "con perfil '%s'. ¿Existe?¿Tiene tienda asignada?",
//                    perfil1));
        }
        return usuario;
    }

    private List<Insumo> getInsumosAlAzar(Proveedor proveedor)
            throws Exception {
        List<Insumo> insumos;
        try {
            insumos = (List<Insumo>) session
                    .createQuery("select x.insumo from ProveedorInsumo x where "
                            + "x.proveedor.id = :proveedor_id and x.activo=true")
                    .setParameter("proveedor_id", proveedor.getId())
                    .list();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            throw new Exception(String.format("No se pudo seleccionar insumos. "
                    + "¿Existe insumos relacionados al proveedor con id %d?",
                    proveedor.getId()));
        }
        return insumos;
    }

    public void generarOrdenesCompra() throws Exception {
        int i, nOrdenesCompra;
        List<Proveedor> proveedores;
        Usuario usuario;
        Tienda tienda;
        List<Insumo> insumos;


        proveedores = (List<Proveedor>) session
                .createQuery("from Proveedor").list();

        try {
            session.beginTransaction();
            i = nOrdenesCompra = 0;
            while (nOrdenesCompra < DEFAULT_N_ORDENES_COMPRA &&
                    !proveedores.isEmpty()) {
                OrdenCompra ordenCompra;
                boolean recibido =
                        nOrdenesCompra < DEFAULT_N_ORDENES_COMPRA_RECIBIDAS;
                Proveedor proveedor = proveedores.get(i);
                List<LoteInsumo> lotes = new ArrayList<>();

                usuario = getUsuarioOrdenCompraAlAzar();
                tienda = usuario.getTienda();
                insumos = getInsumosAlAzar(proveedor);

                if (insumos.isEmpty()) {
                    nOrdenesCompra++;
                    continue;
                }

                for (Insumo insumo : insumos) {
                    LoteInsumo loteInsumo;
                    if (recibido) {
                        loteInsumo = this.generarLoteInsumo(tienda, insumo,
                                DEFAULT_LOTE_INSUMO_STOCK_LOGICO,
                                DEFAULT_LOTE_INSUMO_STOCK_LOGICO,
                                DEFAULT_LOTE_INSUMO_DIAS_DESDE_HOY);
                    } else {
                        loteInsumo = this.generarLoteInsumo(tienda, insumo,
                                0,
                                DEFAULT_LOTE_INSUMO_STOCK_LOGICO,
                                DEFAULT_LOTE_INSUMO_DIAS_DESDE_HOY);
                    }
                    session.save(loteInsumo);
                    session.save(insumo);
                    lotes.add(loteInsumo);
                }

                ordenCompra = this.generarOrdenCompra(tienda, usuario, proveedor,
                        lotes, recibido);

                session.save(ordenCompra);
                for (DetalleOrdenCompra detalle :
                        ordenCompra.getDetalleOrdenCompra()) {
                    session.save(detalle);
                }
                for (LoteInsumo lote : lotes) {
                    session.save(lote);
                }

                i = (i + 1) % proveedores.size();
                nOrdenesCompra++;
            }
            session.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            session.getTransaction().rollback();
        }
    }
}
