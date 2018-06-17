/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import java.util.List;
import org.hibernate.query.Query;

/**
 *
 * @author cfoch
 */
public class GrupoRepartoHelper extends BaseHelper {

    public List<?> getRepartoGrupos(Long tiendaId) {
        String hql;
        Query query;

        hql = "select r.tienda.id, to_char(r.fecha, 'yyyy-mm-dd') as f, r.turno"
                    + " from Reparto as r ";
        if (tiendaId != null) {
            hql += "where r.tienda.id = :tienda_id ";
        }
        hql += "group by r.tienda.id, to_char(r.fecha, 'yyyy-mm-dd'), r.turno";

        query = session.createQuery(hql);
        if (tiendaId != null) {
            query = query.setParameter("tienda_id", tiendaId);
        }
        return query.list();
    }
}
