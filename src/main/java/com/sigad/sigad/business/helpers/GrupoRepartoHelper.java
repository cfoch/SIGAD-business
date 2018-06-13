/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import java.util.List;

/**
 *
 * @author cfoch
 */
public class GrupoRepartoHelper extends BaseHelper {

    public List<?> getRepartoGrupos() {
        String hql =
                "select r.tienda.id, to_char(r.fecha, 'yyyy-mm-dd') as f, r.turno "
                + "from Reparto as r "
                + "group by r.tienda.id, to_char(r.fecha, 'yyyy-mm-dd'), r.turno";
        return session.createQuery(hql).list();
    }
}
