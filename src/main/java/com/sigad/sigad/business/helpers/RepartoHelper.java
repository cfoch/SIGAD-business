/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.sigad.sigad.business.Reparto;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.query.Query;

/**
 *
 * @author cfoch
 */
public class RepartoHelper extends BaseHelper {
    public List<Reparto> getRepartos(long repartidorId) {
        List<Reparto> repartos;
        repartos = (List<Reparto>) session
                .createQuery("from Reparto where repartidor_id = :repar_id "
                        + "order by fecha desc")
                .setParameter("repar_id", repartidorId)
                .list();
        return repartos;
    }

    public boolean esPosibleGenerarReparto(long tiendaId, String turno) throws Exception {
        Query query;
        Long count;
        Date today = new Date();
        Date tomorrow = new Date();
        Calendar calTomorrow = Calendar.getInstance();

        calTomorrow.setTime(tomorrow);
        calTomorrow.add(Calendar.DATE, 1);
        tomorrow = calTomorrow.getTime();
        query = session
                .createQuery("select count(*) from Reparto where turno = :turno"
                        + " and tienda_id = :tienda_id"
                        + " and fecha BETWEEN :today and :tomorrow")
                .setParameter("turno", turno)
                .setParameter("tienda_id", tiendaId)
                .setDate("today", today)
                .setDate("tomorrow", tomorrow);
        count = (Long) query.uniqueResult();
        return count == 0;
    }
}
