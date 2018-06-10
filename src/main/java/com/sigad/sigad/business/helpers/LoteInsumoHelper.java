/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.LoteInsumo;
import com.sigad.sigad.business.Tienda;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author chrs
 */
public class LoteInsumoHelper {

    Session session = null;
    private String errorMessage = "";

    public LoteInsumoHelper() {
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
    
    public ArrayList<LoteInsumo> getLoteInsumosEspecific(Tienda tienda,Insumo insumo){
        ArrayList<LoteInsumo> listaLotes = null;
        Query query = null;
        try {
            query = session.createQuery("from LoteInsumo where insumo_id = " + insumo.getId().toString() + " and tienda_id = "  + tienda.getId().toString());
            if (!query.list().isEmpty()) {
                listaLotes = (ArrayList<LoteInsumo>) query.list();
            }
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return listaLotes;
    }
    
    
            
    public ArrayList<LoteInsumo> getLoteInsumosEspecificPositive(Tienda tienda,Insumo insumo){
        ArrayList<LoteInsumo> listaLotes = null;
        Query query = null;
        try {
            query = session.createQuery("from LoteInsumo where insumo_id = " + insumo.getId().toString() + " and tienda_id = "  + tienda.getId().toString() +" and stockfisico > 0");
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

    public Boolean descontarInsumos(HashMap<Insumo, Integer> insumosHaConsumir, Tienda tienda) {
        Boolean ok = Boolean.FALSE;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }
            ArrayList<LoteInsumo> loteinsumos = getLoteInsumos(tienda);
            Collections.sort(loteinsumos, new Comparator<LoteInsumo>() {
                @Override
                public int compare(LoteInsumo s1, LoteInsumo s2) {
                    Long i =  s1.getFechaVencimiento().getTime() - s1.getFechaVencimiento().getTime();
                    return i.intValue();
                }
            });
            loteinsumos.forEach((t) -> {
                System.out.println(t.getFechaVencimiento().toString());
                System.out.println(t.getInsumo().getNombre());
                System.out.println(t.getStockLogico());
                System.out.println("----------------------------------");
            });
            ArrayList<LoteInsumo> seleccionados = new ArrayList<>();
            for (Map.Entry<Insumo, Integer> entry : insumosHaConsumir.entrySet()) {
                Insumo t = entry.getKey();
                Integer u = entry.getValue();
                Date fecha = new Date();
                Long maximo = Long.MAX_VALUE;
                LoteInsumo loteSeleccionado = null;
                for (LoteInsumo loteinsumo : loteinsumos) {
                    if (loteinsumo.getInsumo().equals(t) && loteinsumo.getStockLogico() >= u) {
                        if (loteinsumo.getFechaVencimiento().getTime() - fecha.getTime() < maximo) {
                            loteSeleccionado = loteinsumo;
                            maximo = loteinsumo.getFechaVencimiento().getTime() - fecha.getTime();
                        }
                    }
                }
                if (loteSeleccionado == null) {
                    return Boolean.FALSE;
                }
                loteSeleccionado.setStockLogico(loteSeleccionado.getStockLogico() - u);
                seleccionados.add(loteSeleccionado);
            }

            seleccionados.forEach((t) -> {
                updateLoteInsumo(t);
            });
            ok = Boolean.TRUE;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.errorMessage = e.getMessage();
        }
        return ok;
    }
    
    public LoteInsumo getMostRecentLoteInsumo(Tienda tienda){
        Boolean ok = Boolean.FALSE;
        try {
            Transaction tx;
            if (session.getTransaction().isActive()) {
                tx = session.getTransaction();
            } else {
                tx = session.beginTransaction();
            }
            
            
            ArrayList<LoteInsumo> loteinsumos = getLoteInsumos(tienda);
            LoteInsumo lowestDateInsumo = loteinsumos.get(0);
            for (int i = 1; i < loteinsumos.size(); i++) {
                if(lowestDateInsumo.getFechaVencimiento().compareTo(loteinsumos.get(i).getFechaVencimiento())>0){
                    lowestDateInsumo = loteinsumos.get(i);
                }
            }
            return lowestDateInsumo;
        } catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
        return null;
    }
    
    public Double getUsedCapacity(Tienda tienda){
        ArrayList<LoteInsumo> listaLotes = null;
        Query query = null;
        Double totalUsed = 0.0;
        try {
            query = session.createQuery("from LoteInsumo where tienda_id = "  + tienda.getId().toString());
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
}
