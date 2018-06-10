/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.grupo1.simulated_annealing.Locacion;
import com.grupo1.simulated_annealing.Pista;
import com.grupo1.simulated_annealing.VRPAlgorithm;
import com.grupo1.simulated_annealing.VRPCostMatrix;
import com.grupo1.simulated_annealing.VRPProblem;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.PedidoEstado;
import com.sigad.sigad.business.Reparto;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.Vehiculo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jgrapht.Graph;
import org.jgrapht.graph.GraphWalk;
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

    public void autogenerarRepartos(Tienda tienda,
            String turno) throws Exception {
        int i, j;
        try (Session session = LoginController.serviceInit()) {
            PedidoHelper helperPedido = new PedidoHelper();
            List<Pedido> pedidos;
            List<List<Pedido>> pedidosSublists;
            List<PedidoEstado> estados;
            PedidoEstado estado;
            String hql;
            Double totalCapacidadTipo;
            double [] proportions;
            int iPedido, jPedido;

            tienda = (Tienda) session.createQuery("from Tienda where id = :id")
                    .setParameter("id", tienda.getId())
                    .getSingleResult();
            estados = (List<PedidoEstado>) session.createQuery(
                    "from PedidoEstado where nombre='Venta'").list();
            if (estados.isEmpty()) {
                throw new Exception("PedidoEstado[nombre='Venta'] no existe");
            }
            estado = estados.get(0);

            List<Vehiculo> vehiculos = tienda.getVehiculos();
            pedidos = helperPedido.getPedidosPorTienda(tienda, estado,
                    turno);
            // FIXME
            // Si no hubiera vehiculos asignados para la tienda, se rompe.
            generarRepartos(tienda, pedidos,
                    vehiculos.get(0).getTipo(), vehiculos, turno);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void generarRepartos(Tienda tienda, List<Pedido> pedidos,
            Vehiculo.Tipo vehiculoTipo, List<Vehiculo> vehiculos, String turno)
            throws Exception {
        GraphHopperHelper hopperHelper;
        VRPProblem problem;
        VRPAlgorithm algorithm;
        ArrayList<GraphWalk> solution;
        double [][] costMatrix;
        Locacion [] locaciones;
        List<Usuario> repartidores;
        Usuario repartidor;
        Random rand = new Random();
        int i, j;

        // Resolver problema VRP
        locaciones = createLocaciones(tienda, pedidos);
        hopperHelper = GraphHopperHelper.getInstance();
        costMatrix = hopperHelper.getCostMatrix(locaciones, true);
        problem = AlgoritmoHelper.buildVRPProblem(locaciones,
                vehiculoTipo, costMatrix);
        algorithm = new VRPAlgorithm(problem);
        solution = algorithm.solve();
        algorithm.printSolution(solution);

        // Generar repartos
        repartidores = (List<Usuario>) session
                .createQuery("from Usuario where tienda_id = :tienda_id")
                .setParameter("tienda_id", tienda.getId())
                .list();
        //repartidores.addAll();
        try {
            PedidoEstado estadoPedido;
            estadoPedido = (PedidoEstado) session
                    .createQuery("from PedidoEstado where nombre='Despacho'")
                    .getSingleResult();
            for (i = 0; i < solution.size(); i++) {
                Vehiculo vehiculo = vehiculos.get(i % vehiculos.size());
                List<Pedido> subpedidos;
                List<Locacion> ruta = solution.get(i).getVertexList();

                subpedidos = locacionesToPedidos(ruta);
                repartidor = repartidores.get(rand.nextInt(repartidores.size()));

                session.getTransaction().begin();
                Reparto reparto = new Reparto();
                reparto.setVehiculo(vehiculo);
                reparto.setPedidos(subpedidos);
                reparto.setFecha(new Date());
                reparto.setRepartidor(repartidor);
                reparto.setTurno(turno);
                reparto.setTienda(repartidor.getTienda());
                session.save(reparto);
                for (j = 0; j < subpedidos.size(); j++) {
                    Pedido subpedido = subpedidos.get(j);
                    subpedido.setReparto(reparto);
                    subpedido.setEstado(estadoPedido);
                    subpedido.setSecuenciaReparto(j);
                    session.save(subpedido);
                }
                session.getTransaction().commit();
            }
        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw ex;
        }
    }

    public List<Pedido> locacionesToPedidos(
            List<Locacion> locaciones) {
        Query query;
        List<Pedido> pedidos;
        long [] idPedidosTmp;
        Long [] idPedidos;
        try {
            idPedidosTmp = locaciones.stream()
                    .filter(l -> l.getServicio() != null)
                    .mapToLong(l -> l.getId())
                    .toArray();
            idPedidos = ArrayUtils.toObject(idPedidosTmp);
            query = session.createQuery("from Pedido p where p.id in (:ids)")
                    .setParameterList("ids", idPedidos);
            pedidos = (List<Pedido>) query.list();
        } catch (Exception ex) {
            pedidos = new ArrayList<>();
        }
        return pedidos;
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
