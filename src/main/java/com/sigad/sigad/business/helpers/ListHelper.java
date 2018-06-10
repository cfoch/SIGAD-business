/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cfoch
 */
public class ListHelper {
    /**
     * Generar una lista de sublistas siguiendo las proporciones
     * dictadas por la variable @proportions.
     * @param <T>
     * @param list
     * @param proportions
     * @return lista de sublistas
     */
    public static <T> List<List<T>> sublist(List<T> list,
            double[] proportions) {
        List<T> sub;
        int i, start, end;
        int nElements = 0;
        List<List<T>> ret = new ArrayList<List<T>>();
        end = 0;
        for (i = 0; i < proportions.length; i++) {
            int sz;
            start = end;
            sz = (int) (proportions[i] * list.size());
            if (sz == 0) {
                break;
            }
            end = min(start + sz, list.size());
            sub = list.subList(start, end);
            ret.add(sub);
            nElements += sub.size();
        }

        if (nElements != list.size()) {
            sub = list.subList(end, list.size());
            ret.add(sub);
        }
        return ret;
    }
}
