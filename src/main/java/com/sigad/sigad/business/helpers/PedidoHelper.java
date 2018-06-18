/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.PedidoEstado;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.TipoMovimiento;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class PedidoHelper {

    Session session = null;
    private String errorMessage = "";

    public PedidoHelper() {
        session = LoginController.serviceInit();
    }

    /*Close session*/
    public void close() {
        session.close();
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    
     public ArrayList<Pedido> getPedidos(){
        ArrayList<Pedido> pedidos = null;
         Query query = null;
        try {
            query = session.createQuery("from Pedido");
            pedidos = (ArrayList<Pedido>) query.list();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            errorMessage = e.getMessage();
        } finally{
            return pedidos;
        }
    };
    /*Get all the Pedidos*/
    public Long savePedido(Pedido pedido) {
        Long id = null;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }
            
            session.save(pedido);
            if (pedido.getId()== null) {
                id = pedido.getId();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        } finally {
            return id;
        }
    }

    public List<Pedido> getPedidosPorTienda(Tienda tienda, PedidoEstado estado,
            String turno, Date fecha) throws Exception {
        String hql;
        Query query;
        String hqlBase = "from Pedido where tienda_id = :tienda_id";
        List<Pedido> pedidos = new ArrayList<>();

        hql = hqlBase;
        if (turno != null) {
            hql = hql + " and turno = :turno";
        }
        if (estado != null) {
            hql = hql + " and estado_id = :estado_id";
        }
        if (fecha != null) {
            hql = hql
                    + " and fechaentregaesperada BETWEEN :today and :tomorrow";
        }

        query = session.createQuery(hql);

        if (turno != null) {
            query.setParameter("turno", turno);
        }
        if (estado != null) {
            query.setParameter("estado_id", estado.getId());
        }
        if (fecha != null) {
            Calendar calTomorrow = Calendar.getInstance();
            Date tomorrow = new Date();
            calTomorrow.setTime(tomorrow);
            calTomorrow.add(Calendar.DATE, 1);
            tomorrow = calTomorrow.getTime();
            query.setDate("today", fecha);
            query.setDate("tomorrow", tomorrow);
        }
        query.setParameter("tienda_id", tienda.getId());
        pedidos = query.list();
        return pedidos;
    }

    public boolean cambiarPedidoEstado(Pedido pedido, String estadoNombre)
            throws Exception {
        PedidoEstado estadoNuevo;
        try {
            estadoNuevo = (PedidoEstado) session
                    .createQuery("from PedidoEstado where nombre = :nombre")
                    .setParameter("nombre", estadoNombre)
                    .getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName())
                .log(Level.SEVERE, null, ex);
            throw new Exception(String.format("El estado 'Despacho' no "
                    + "está registrado."));
        }

        return cambiarPedidoEstado(pedido, estadoNuevo);
    }

    public boolean cambiarPedidoEstado(Pedido pedido,
            PedidoEstado estadoNuevo) throws Exception {
        boolean ret = false;
        PedidoEstado estadoActual = pedido.getEstado();
        String estadoActualNombre = estadoActual.getNombre();

        if (estadoActualNombre.equals("Venta") &&
                estadoNuevo.getNombre().equals("Despacho")) {
            int i;
            String tipoMovimientoNombre = "Salida Lógica";
            List<MovimientosTienda> movimientos;
            TipoMovimientoHelper tipoMovimientoHelper =
                    new TipoMovimientoHelper();
            TipoMovimiento tipoMovimientoSalidaFisica;

            tipoMovimientoSalidaFisica =
                    tipoMovimientoHelper.getTipoMov("Salida Física");
            if (tipoMovimientoSalidaFisica == null) {
                throw new Exception("No existe tipo de movimiento "
                        + "'Salida física'");
            }

            try {
                movimientos = (List<MovimientosTienda>) session
                        .createQuery("from MovimientosTienda as mov "
                                + "where mov.pedido.id = :pedido_id and "
                                + "mov.tipoMovimiento.nombre = :tipoMovimiento")
                        .setParameter("pedido_id", pedido.getId())
                        .setParameter("tipoMovimiento", tipoMovimientoNombre)
                        .list();
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName())
                    .log(Level.SEVERE, null, ex);
                throw new Exception(String.format("No existe movimiento de "
                        + "tipo '%s' para el pedido con id %d",
                        tipoMovimientoNombre, pedido.getId()));
            }
            for (i = 0; i < movimientos.size(); i++) {
                MovimientosTienda movimiento = movimientos.get(i);
                MovimientosTienda movimientoNuevo = new MovimientosTienda();
                movimientoNuevo.setCantidadMovimiento(
                        movimiento.getCantidadMovimiento());
                movimientoNuevo.setFecha(movimiento.getFecha());
                movimientoNuevo.setLoteInsumo(movimiento.getLoteInsumo());
                movimientoNuevo.setPedido(movimiento.getPedido());
                movimientoNuevo.setTienda(movimiento.getTienda());
                movimientoNuevo.setTipoMovimiento(tipoMovimientoSalidaFisica);
                movimientoNuevo.setTrabajador(movimiento.getTrabajador());
                try {
                    session.beginTransaction();
                    session.save(movimientoNuevo);
                    session.getTransaction().commit();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName())
                        .log(Level.SEVERE, null, ex);
                    session.getTransaction().rollback();
                    throw new Exception(String.format("No se pudo crear un "
                            + "movimiento de tipo 'Salida física' para el "
                            + "pedido con id %d", pedido.getId()));
                }
            }
            ret = true;
        }
        return ret;
    }

    public static String stringifyTurno(String turno) {
        String ret = "No definido";
        if (turno.equals("M")) {
            ret = "Mañana";
        } else if (turno.equals("T")) {
            ret = "Tarde";
        } else if (turno.equals("N")) {
            ret = "Noche";
        }
        return ret;
    }
}
