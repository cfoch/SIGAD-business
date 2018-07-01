/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.PedidoEstado;
import com.sigad.sigad.business.ProductoInsumo;
import com.sigad.sigad.business.ProductosCombos;
import com.sigad.sigad.business.Tienda;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Alexandra
 */
public class PedidoHelper extends BaseHelper {

    public PedidoHelper() {
        super();
    }


    public String getPedidoEstado(Long id) {
        String estado;
        try {
            List<Object[]> rows;
            Object [] row;
            Object res;
            res = session
                    .createQuery(
                            "select p.estado.nombre from Pedido p where id = :id")
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getSingleResult();
            estado = (String) res;
        } catch (Exception ex) {
            ex.printStackTrace();
            estado = null;
        }
        return estado;
    }
    
    public ArrayList<Pedido> getPedidos(){
        ArrayList<Pedido> pedidos = null;
        Query query = null;
        try {
            query = session.createQuery("from Pedido");
            pedidos = (ArrayList<Pedido>) query.list();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            errorMessage = e.getMessage();
        } finally {
            return pedidos;
        }
    }

    ;
    
    public Pedido getPedido(Long pedido_id) {
        Pedido pedido = null;
        Query query = null;
        try {
            query = session.createQuery("from Pedido where id = " + pedido_id);
            pedido = (Pedido) query.list().get(0);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            errorMessage = e.getMessage();
        } finally {
            return pedido;
        }
    }

    public Pedido getPedidoEager(Long pedido_id) {
        Pedido pedido = null;
        Query query = null;
        try {
            query = session.createQuery("from Pedido where id = " + pedido_id);
            pedido = (Pedido) query.list().get(0);
            pedido.getEstadosPedido().size();
            pedido.getDetallePedido().forEach((t) -> {
                t.getDescuentoCategoria();
                t.getDescuentoProducto();
                if (t.getProducto() != null) {
                    t.getProducto().getCategoria();
                    ArrayList<ProductoInsumo> pd = new ArrayList(t.getProducto().getProductoxInsumos());
                    pd.forEach((p) -> {
                        p.getInsumo();
                    });
                } else if (t.getCombo() != null) {
                    ArrayList<ProductosCombos> pd = new ArrayList(t.getCombo().getProductosxCombo());
                    pd.forEach((p) -> {
                        p.getProducto();
                        ArrayList<ProductoInsumo> pi = new ArrayList(p.getProducto().getProductoxInsumos());
                        pi.forEach((m) -> {
                            m.getCantidad();
                            m.getInsumo();
                        });
                    });
                }
            });
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            return pedido;
        }
    }

    ;
    
    public boolean updatePedido(Pedido tOld){
        boolean ok = false;
        try {
            Transaction tx;
            if(session.getTransaction().isActive()){
                tx = session.getTransaction();
            }else{
                tx = session.beginTransaction();
            }
            
            Pedido tNew = session.load(Pedido.class, tOld.getId());
            
            tNew.setActivo(tOld.isActivo());
            tNew.setDescuentoCliente(tOld.getDescuentoCliente());
            tNew.setDetallePedido(tOld.getDetallePedido());
            tNew.setDocumentos(tOld.getDocumentos());
            tNew.setFechaEntregaEsperada(tOld.getFechaEntregaEsperada());
            tNew.setFechaVenta(tOld.getFechaVenta());
            tNew.setMensajeDescripicion(tOld.getMensajeDescripicion());
            tNew.setNombreEmpresa(tOld.getNombreEmpresa());
            tNew.setRucFactura(tOld.getRucFactura());
            tNew.setTipoPago(tOld.getTipoPago());
            tNew.setTotal(tOld.getTotal());
            tNew.setTurno(tOld.getTurno());
            tNew.setVolumenTotal(tOld.getVolumenTotal());
            session.merge(tNew);
            tx.commit();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
            this.errorMessage = e.getMessage();
        }
        return ok;
    }
    
    public ArrayList<Pedido> getPedidosCliente(Long userId) {
        ArrayList<Pedido> pedidos = null;
        Query query = null;
        try {
            query = session.createQuery("from Pedido where cliente_id = " + userId);
            pedidos = (ArrayList<Pedido>) query.list();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            errorMessage = e.getMessage();
        } finally {
            return pedidos;
        }
    }

    public ArrayList<Pedido> getPedidosVendedor(Long userId) {
        ArrayList<Pedido> pedidos = null;
        Query query = null;
        try {
            query = session.createQuery("from Pedido where vendedor_id = " + userId);
            pedidos = (ArrayList<Pedido>) query.list();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            session.getTransaction().rollback();
            errorMessage = e.getMessage();
        } finally {
            return pedidos;
        }
    }

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
            if (pedido.getId() == null) {
                id = pedido.getId();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
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
