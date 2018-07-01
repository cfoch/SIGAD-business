/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Constantes;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.LoteInsumo;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.TipoMovimiento;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author chrs
 */
public class LoteInsumoHelper extends BaseHelper {

    public LoteInsumoHelper() {
        super();
    }

    /*Get all stores*/
    public ArrayList<LoteInsumo> getLoteInsumos() {
        ArrayList<LoteInsumo> lotesInsumos = null;
        Query query = null;
        try {
            query = session.createQuery("from LoteInsumo");

            if (!query.list().isEmpty()) {
                lotesInsumos = (ArrayList<LoteInsumo>) (query.list());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally {
            return lotesInsumos;
        }
    }

    public ArrayList<LoteInsumo> getLoteInsumos(Tienda currentStore) {
        ArrayList<LoteInsumo> lotesInsumos = null;
        Query query = null;
        try {
            query = session.createQuery("from LoteInsumo where tienda_id = " + currentStore.getId().toString());

            if (!query.list().isEmpty()) {
                lotesInsumos = (ArrayList<LoteInsumo>) (query.list());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally {
            return lotesInsumos;
        }
    }

    public ArrayList<LoteInsumo> getLoteInsumosRecibidos(Tienda currentStore) {
        ArrayList<LoteInsumo> lotesInsumos = null;
        Query query = null;
        try {
            query = session.createQuery("from LoteInsumo where tienda_id = " + currentStore.getId().toString());

            if (!query.list().isEmpty()) {
                lotesInsumos = (ArrayList<LoteInsumo>) (query.list());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally {
            return lotesInsumos;
        }
    }

    public LoteInsumo getLoteInsumo(Long id) {
        LoteInsumo insumo = null;
        Query query = null;
        try {
            query = session.createQuery("from LoteInsumo where id=" + id);
            if (!query.list().isEmpty()) {
                insumo = (LoteInsumo) query.list().get(0);
            }
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return insumo;
    }

    public ArrayList<LoteInsumo> getLoteInsumosEspecific(Tienda tienda, Insumo insumo) {
        ArrayList<LoteInsumo> listaLotes = null;
        Query query = null;
        try {
            query = session.createQuery("from LoteInsumo where insumo_id = " + insumo.getId().toString() + " and tienda_id = " + tienda.getId().toString());
            if (!query.list().isEmpty()) {
                listaLotes = (ArrayList<LoteInsumo>) query.list();
            }
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return listaLotes;
    }

    public ArrayList<LoteInsumo> getLoteInsumosEspecificPositive(Tienda tienda, Insumo insumo) {
        ArrayList<LoteInsumo> listaLotes = null;
        Query query = null;
        try {
            query = session.createQuery("from LoteInsumo where insumo_id = " + insumo.getId().toString() + " and tienda_id = " + tienda.getId().toString() + " and stockfisico > 0");
            if (!query.list().isEmpty()) {
                listaLotes = (ArrayList<LoteInsumo>) query.list();
            }
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return listaLotes;
    }

    public boolean updateLoteInsumo(LoteInsumo tOld) {
        boolean ok = false;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }
            LoteInsumo tNew = session.load(LoteInsumo.class, tOld.getId());

            tNew.setStockLogico(tOld.getStockLogico());
            tNew.setStockFisico(tOld.getStockFisico());
            tNew.setCostoUnitario(tOld.getCostoUnitario());
            tNew.setDetalleOrdenCompra(tOld.getDetalleOrdenCompra());
            tNew.setFechaVencimiento(tOld.getFechaVencimiento());
            tNew.setInsumo(tOld.getInsumo());
            tNew.setTienda(tOld.getTienda());

            session.merge(tNew);
            tx.commit();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return ok;
    }

    public void devolverInsumosEditar(HashMap<Insumo, Integer> insumosADevolver, Pedido pedido, ArrayList<MovimientosTienda> movimientosLogicos) {
        Collections.sort(movimientosLogicos, (MovimientosTienda s1, MovimientosTienda s2) -> {
            return s1.getLoteInsumo().getFechaVencimiento().compareTo(s2.getLoteInsumo().getFechaVencimiento());
        });
        movimientosLogicos.forEach((t) -> {
            LoteInsumo lote = t.getLoteInsumo();
            Integer cantidad = insumosADevolver.get(lote.getInsumo());
            Integer devolucion = 0;
            if (cantidad != null) {
                if (cantidad != 0) {
                    if (t.getCantidadMovimiento() <= cantidad) {//En caso en la que la cantidad que quiero devolver es mayor de lo que consumi en el lote 
                        lote.setStockLogico(lote.getStockLogico() + t.getCantidadMovimiento());
                        devolucion = t.getCantidadMovimiento();
                        cantidad = cantidad - t.getCantidadMovimiento();
                        t.setCantidadMovimiento(0);
                        insumosADevolver.put(lote.getInsumo(), cantidad);
                        MovimientoHelper mov = new MovimientoHelper();
                        mov.deleteMovement(t);
                    } else if (t.getCantidadMovimiento() > cantidad) {//En caso en la que cantidad que devuelvo es menor que lo que ocnsumi en el lote
                        lote.setStockLogico(lote.getStockLogico() + cantidad);
                        devolucion = cantidad;
                        t.setCantidadMovimiento(t.getCantidadMovimiento() - cantidad);
                        cantidad = 0;
                        insumosADevolver.put(lote.getInsumo(), cantidad);
                        MovimientoHelper mov = new MovimientoHelper();
                        mov.updateMovement(t);
                    }
                }
            }
            if (session != null) {
                session.close();
            }
            session = LoginController.serviceInit();
            updateLoteInsumo(lote);
            InsumosHelper h = new InsumosHelper();
            Insumo i = h.getInsumo(lote.getInsumo().getId());
            i.setStockTotalLogico(i.getStockTotalLogico() + devolucion);
            h.updateInsumo(i);
        });

    }

    public void devolverInsumos(HashMap<Insumo, Integer> insumosADevolver, Pedido pedido, ArrayList<MovimientosTienda> movimientosLogicos) {
        Collections.sort(movimientosLogicos, (MovimientosTienda s1, MovimientosTienda s2) -> {
            return s1.getLoteInsumo().getFechaVencimiento().compareTo(s2.getLoteInsumo().getFechaVencimiento());
        });
        TipoMovimientoHelper tipohelper = new TipoMovimientoHelper();
        TipoMovimiento tipomovLogico = tipohelper.getTipoMov(Constantes.TIPO_MOVIMIENTO_ENTRADA_LOGICA);
        TipoMovimiento tipomovFisico = tipohelper.getTipoMov(Constantes.TIPO_MOVIMIENTO_SALIDA_FISICA);
        tipohelper.close();
        movimientosLogicos.forEach((t) -> {
            LoteInsumo lote = t.getLoteInsumo();
            Integer cantidad = insumosADevolver.get(lote.getInsumo());
            Integer devolucion = 0;
            if (cantidad != null) {
                if (cantidad != 0) {
                    if (t.getCantidadMovimiento() <= cantidad) {//En caso en la que la cantidad que quiero devolver es mayor de lo que consumi en el lote 
                        lote.setStockLogico(lote.getStockLogico() + t.getCantidadMovimiento());
                        devolucion = t.getCantidadMovimiento();
                        cantidad = cantidad - t.getCantidadMovimiento();
                        t.setCantidadMovimiento(0);
                        insumosADevolver.put(lote.getInsumo(), cantidad);
                    } else if (t.getCantidadMovimiento() > cantidad) {//En caso en la que cantidad que devuelvo es menor que lo que ocnsumi en el lote
                        lote.setStockLogico(lote.getStockLogico() + cantidad);
                        devolucion = cantidad;
                        t.setCantidadMovimiento(t.getCantidadMovimiento() - cantidad);
                        cantidad = 0;
                        insumosADevolver.put(lote.getInsumo(), cantidad);
                    }
                    MovimientoHelper mov = new MovimientoHelper();
                    MovimientosTienda newMovLogico = new MovimientosTienda(cantidad, new Date(), tipomovLogico, LoginController.user, pedido.getTienda(), lote, pedido);
                    MovimientosTienda newMovFisico = new MovimientosTienda(cantidad, new Date(), tipomovFisico, LoginController.user, pedido.getTienda(), lote, pedido);
                    mov.saveMovement(newMovLogico);
                    mov = new MovimientoHelper();
                    mov.saveMovement(newMovFisico);
                }
            }
            if (session != null) {
                session.close();
            }
            session = LoginController.serviceInit();
            updateLoteInsumo(lote);
            InsumosHelper h = new InsumosHelper();
            Insumo i = h.getInsumo(lote.getInsumo().getId());
            i.setStockTotalLogico(i.getStockTotalLogico() + devolucion);
            h.updateInsumo(i);
        });

    }

    public Boolean descontarInsumos(HashMap<Insumo, Integer> insumosHaConsumir, Tienda tienda, Pedido pedido) {
        Boolean ok = Boolean.FALSE;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }
            ArrayList<LoteInsumo> loteinsumos = getLoteInsumos(tienda);
            Collections.sort(loteinsumos, (LoteInsumo s1, LoteInsumo s2) -> {
                return s1.getFechaVencimiento().compareTo(s2.getFechaVencimiento());

            });
            ArrayList<LoteInsumo> seleccionados = new ArrayList<>();
            ArrayList<Integer> cantidadConsumida = new ArrayList<>();
            Date hoy = new Date();
            for (Map.Entry<Insumo, Integer> entry : insumosHaConsumir.entrySet()) {
                Insumo t = entry.getKey();//Insumo
                Integer u = entry.getValue();//Cantidad requerida del insumo
                Integer queda = u;
                for (LoteInsumo loteinsumo : loteinsumos) {
                    if (!loteinsumo.getFechaVencimiento().before(hoy) && loteinsumo.getInsumo().equals(t) && loteinsumo.getStockLogico() > 0 && queda > 0) {
                        Integer p = queda - loteinsumo.getStockLogico();//Queda por descontar
                        Integer descuento = (p <= 0) ? queda : loteinsumo.getStockLogico();
                        loteinsumo.setStockLogico(loteinsumo.getStockLogico() - descuento);
                        queda = queda - descuento;
                        Integer i = seleccionados.indexOf(loteinsumo);
                        if (i >= 0) {
                            seleccionados.set(i, loteinsumo);
                            cantidadConsumida.set(i, descuento);
                        } else {
                            seleccionados.add(loteinsumo);
                            cantidadConsumida.add(descuento);
                        }

                    }
                }
                if (queda > 0) {
                    return Boolean.FALSE;
                }

            }
            PedidoHelper helper = new PedidoHelper();
            if (pedido.getId() == null) {
                helper.savePedido(pedido);
            } else {
                System.out.println("Esta actualizando...");
                helper.updatePedido(pedido);
            }
            for (int i = 0; i < seleccionados.size(); i++) {
                LoteInsumo get = seleccionados.get(i);
                if (session != null) {
                    session.close();
                }
                session = LoginController.serviceInit();
                updateLoteInsumo(get);
                TipoMovimientoHelper tipomovhelper = new TipoMovimientoHelper();
                TipoMovimiento tipoMovimiento = tipomovhelper.getTipoMov(Constantes.TIPO_MOVIMIENTO_SALIDA_LOGICA);
                MovimientoHelper movhelper = new MovimientoHelper();
                MovimientosTienda m = new MovimientosTienda();
                m.setPedido(pedido);
                m.setTienda(tienda);// falta el tipo de movimiento
                m.setTipoMovimiento(tipoMovimiento);
                m.setFecha(new Date());
                m.setLoteInsumo(get);
                m.setTrabajador(LoginController.user);
                m.setCantidadMovimiento(cantidadConsumida.get(i));
                movhelper.saveMovement(m);
            }

//            seleccionados.forEach((t) -> {
//                updateLoteInsumo(t);
//
//            });
            for (Map.Entry<Insumo, Integer> entry : insumosHaConsumir.entrySet()) {
                InsumosHelper h = new InsumosHelper();
                Insumo key = entry.getKey();
                Integer value = entry.getValue();
                Insumo i = h.getInsumo(key.getId());
                System.out.println(i.getNombre());
                i.setStockTotalLogico(i.getStockTotalLogico() - value);
                System.out.println(i.getStockTotalLogico());
                h.updateInsumo(i);
            }

            ok = Boolean.TRUE;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return ok;
    }

    public LoteInsumo getMostRecentLoteInsumo(Tienda tienda, Insumo insumo) {
        Boolean ok = Boolean.FALSE;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }

            ArrayList<LoteInsumo> loteinsumos = getLoteInsumosEspecific(tienda, insumo);
            LoteInsumo lowestDateInsumo = loteinsumos.get(0);
            for (int i = 1; i < loteinsumos.size(); i++) {
                if (lowestDateInsumo.getFechaVencimiento().compareTo(loteinsumos.get(i).getFechaVencimiento()) > 0) {
                    lowestDateInsumo = loteinsumos.get(i);
                }
            }
            return lowestDateInsumo;
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return null;
    }

    public Double getUsedCapacity(Tienda tienda) {
        ArrayList<LoteInsumo> listaLotes = null;
        Query query = null;
        Double totalUsed = 0.0;
        try {
            query = session.createQuery("from LoteInsumo where tienda_id = " + tienda.getId().toString());
            if (!query.list().isEmpty()) {
                listaLotes = (ArrayList<LoteInsumo>) query.list();
                for (int i = 0; i < listaLotes.size(); i++) {
                    totalUsed += listaLotes.get(i).getStockLogico() * listaLotes.get(i).getInsumo().isVolumen();
                }
            }
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return totalUsed;
    }

    public Long saveLoteInsumo(LoteInsumo newLote) {
        Long id = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(newLote);
            if (newLote.getId() != null) {
                id = newLote.getId();
            }
            tx.commit();
//            LOGGER.log(Level.FINE, "Insumo guardado con exito");
            this.errorMessage = "";
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            this.errorMessage = e.getMessage();
//            LOGGER.log(Level.SEVERE, String.format("Ocurrio un error al tratar de crear el loteinsumo %s", newLote.getId()));
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
        }
        return id;
    }

}
