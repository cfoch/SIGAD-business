/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.grupo1.simulated_annealing.Locacion;
import com.grupo1.simulated_annealing.Pista;
import com.grupo1.simulated_annealing.VRPCostMatrix;
import com.grupo1.simulated_annealing.VRPProblem;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.Vehiculo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author cfoch
 */
public class AlgoritmoHelper extends BaseHelper {
    public Tienda getTiendaActual(Usuario usuario) {
        Tienda tienda;
        tienda = usuario.getTienda();
        return tienda;
    }

    /**
     * Obtiene pedidos a repartir. Los pedidos deben tener estado VENTA.
     * @param usuario
     * @return
     */
    public static final List<Pedido> getPedidos(final Usuario usuario) {
        ArrayList<Pedido> pedidos;
        pedidos = new ArrayList<>();
        return pedidos;
    }

    /**
     * Dado una tienda y una lista de pedidos, los convierte en un formato
     * entendido por el algoritmo VRP.
     * @param tienda
     * @param pedidos
     * @return Un array de Locacion donde el primer elemento corresponde a la
     *          tienda.
     */
    public static final Locacion [] createLocaciones(final Tienda tienda,
            final List<Pedido> pedidos) {
        int i;
        Locacion [] locaciones = new Locacion[1 + pedidos.size()];
        locaciones[0] = tienda.getLocacion();
        for (i = 0; i < pedidos.size(); i++) {
            locaciones[i + 1] = pedidos.get(i).getLocacion();
        }
        return locaciones;
    }

    /**
     * Construye un problema de VRP dadas ciertas locaciones, un tipo de
     * vehículo y una matriz de costos.
     * @param locaciones
     * @param vehiculoTipo
     * @param costMatrix
     * @return Un VRPProblem
     * @throws Exception
     */
    public static final VRPProblem buildVRPProblem(Locacion [] locaciones,
            Vehiculo.Tipo vehiculoTipo,
            double [][] costMatrix) {
        int i, j;
        Stream<Locacion> stream;
        VRPCostMatrix vrpMatrix;
        Graph<Locacion, Pista> grafo;

        try {
            vrpMatrix = new VRPCostMatrix(costMatrix, locaciones);
        } catch (Exception ex) {
            vrpMatrix = null;
        }
        grafo = new SimpleGraph<>(Pista.class);
        for (i = 0; i < locaciones.length; i++) {
            Locacion locacion;
            locacion = locaciones[i];
            locacion.setCostMatrix(vrpMatrix);
            grafo.addVertex(locacion);
        }

        for (i = 0; i < locaciones.length; i++) {
            Locacion v1;
            v1 = locaciones[i];
            for (j = 0; j < locaciones.length; j++) {
                Locacion v2;
                v2 = locaciones[j];
                if (i == j) {
                    continue;
                }
                grafo.addEdge(v1, v2);
            }
        }

        return new VRPProblem(grafo, locaciones[0], vehiculoTipo.getTipo());
    }
}
