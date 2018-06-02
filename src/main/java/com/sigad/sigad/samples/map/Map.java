/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.samples.map;

import com.grupo1.simulated_annealing.Locacion;
import com.grupo1.simulated_annealing.VRPAlgorithm;
import com.grupo1.simulated_annealing.VRPProblem;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Vehiculo;
import com.sigad.sigad.business.helpers.AlgoritmoHelper;
import com.sigad.sigad.business.helpers.GraphHopperHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jgrapht.graph.GraphWalk;

/**
 *
 * @author cfoch
 */
public class Map extends Application {
    private static final String VIEW_PATH =
            "/com/sigad/sigad/samples/view/Map.fxml";
    private static final double DELTA = 1e-15;
    private static List<Pedido> pedidos;
    private static Tienda tienda;
    private static Vehiculo.Tipo vehiculoTipo;
    // Av. de la Marina 2155 Cercado de Lima 32
    private static final double [] coordenadasTienda = {-12.078418, -77.083272};
    private static final long idsPedidos[] = {
        11,
        32,
        23,
        30,
        7,
        10,
        20,
        29,
        12,
        21,
        4,
        26,
        6,
        31,
        8,
        27,
        16,
        17,
        3,
        22,
        2,
        0,
        14,
        25,
        5,
        19,
        33,
        28,
        9,
        18,
        13,
        24,
        15,
        1
    };
    private static final double [][] coordenadasPedidos = {
        // Salinar 115
        {-12.072564, -77.071882},
        // Avenida José Leguía y Meléndez 1786-1846 Pueblo Libre 15084
        {-12.074211, -77.072853},
        // Panaderia Thakrispan. Municipalidad Metropolitana de, Lima 15084
        {-12.077264, -77.074422},
        // Santa Francisca. Cercado de Lima 15088
        {-12.061329, -77.078440},
        // Av. Alejandro Bertello. Cercado de Lima 15083
        {-12.062924, -77.062839},
        // Rodríguez de Mendoza 329-241 Cercado de Lima 15084
        {-12.073089, -77.060483},
        // Calle Los Ruiseñores 120-218. Bellavista 07006
        {-12.060905, -77.095347},
        // Arenas. Callao 070010
        {-12.056830, -77.125136},
        // Los Diamantes 202-230. Callao 07001
        {-12.052427, -77.109267},
        // Unnamed Road. Bellavista 07011
        {-12.061401, -77.103749},
        // Calle Granada. Cercado de Lima 07016
        {-12.073870, -77.109851},
        // Calle Granada. Cercado de Lima 07016
        {-12.073870, -77.109851},
        // Calle Isla Del Gallo 184-186. Cercado de Lima 15088
        {-12.071876, -77.100180},
        // Hermanos Catari 206-296. San Miguel 15087
        {-12.076820, -77.096778},
        // Calle los Robles 297. Cercado de Lima 15073
        {-12.092791, -77.042839},
        // Calle Choquehuanca 1083-1003. San Isidro 15076
        {-12.097793, -77.041770},
        // Telefonica Data. San Isidro 15073
        {-12.094347, -77.038408},
        // Calle Río de la Plata 180-222. San Isidro 15046
        {-12.100524, -77.031302},
        // Gémmelas. Antero Aspillaga 993, Surquillo 15047
        {-12.111448, -77.023457},
        // Calle Gonzáles Prada 799-699. Cercado de Lima 15047
        {-12.116331, -77.022599},
        // Calle Las Palomas 397-307. Surquillo 15047
        {-12.104704, -77.025151},
        // Alejandro Deustua 224-296. Miraflores 15048
        {-12.116991, -77.012262},
        // Casuarina 283-247. Cercado de Lima 15038
        {-12.120787, -77.000274},
        // Jirón Gozzoli Nte. 311. Lima 15036
        {-12.101750, -77.006027},
        // Pq Santos Dumont. Cercado de Lima 15046
        {-12.087841, -77.026510},
        // Balconcillo. La Victoria
        {-12.081640, -77.032464},
        // Cervantes 209-129. Cercado de Lima 15046
        {-12.062478, -77.042630},
        // Prol. Lucanas 987-913 La Victoria 15018
        {-12.068136, -77.018088},
        // Jirón America 799-721. La Victoria 15018
        {-12.069190, -77.015182},
        // Torre de la Merced 277. Cercado de Lima 15034
        {-12.080400, -77.013612},
        // Jirón Saco Oliveros 197-115. Cercado de Lima 15046
        {-12.068405, -77.037561},
        // Gral. Vidal 499-405. Breña 15083
        {-12.062584, -77.049160},
        // Manuel Belgrano 191-101. Cercado de Lima 15082
        {-12.056953, -77.050372},
        //San Rafael. Lima District 15082
        {-12.051026, -77.062456}
    };
    private static final double [] volumenesPedidos = {
        99,
        97,
        343,
        155,
        382,
        23,
        222,
        374,
        65,
        385,
        139,
        392,
        270,
        280,
        276,
        189,
        240,
        105,
        101,
        348,
        381,
        26,
        90,
        182,
        237,
        385,
        190,
        89,
        120,
        382,
        243,
        50,
        189,
        189
    };

    public Map() {

    }

    public static void setupPedidos() {
        int i;
        System.out.println("Crear pedidos.");
        pedidos = new ArrayList<>();
        for (i = 0; i < coordenadasPedidos.length; i++) {
            Pedido pedido;
            pedido = new Pedido();
            pedido.setId(idsPedidos[i]);
            pedido.setCooXDireccion(coordenadasPedidos[i][0]);
            pedido.setCooYDireccion(coordenadasPedidos[i][1]);
            pedido.setDireccionDeEnvio("Direccion de prueba " + idsPedidos[i]);
            pedido.setVolumenTotal(volumenesPedidos[i]);
            pedidos.add(pedido);
        }
    }

    public static void setupTienda() {
        System.out.println("Crear tiendas.");
        tienda = new Tienda();
        tienda.setId(new Long(0));
        tienda.setDireccion("Direccion de prueba 0");
        tienda.setCooXDireccion(coordenadasTienda[0]);
        tienda.setCooYDireccion(coordenadasTienda[1]);
    }

    public static void setupVehiculo() {
        System.out.println("Crear vehículo.");
        vehiculoTipo = new Vehiculo.Tipo("Vehiculo Tipo A", 1500);
        vehiculoTipo.setId(new Long(0));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VRPProblem problem;
        VRPAlgorithm algorithm;
        ArrayList<GraphWalk> solution;
        GraphHopperHelper hopperHelper;
        Locacion [] locaciones;
        double[][] costMatrix;

        setupPedidos();
        setupTienda();
        setupVehiculo();

        System.out.println("Crear locaciones.");
        locaciones = AlgoritmoHelper.createLocaciones(tienda, pedidos);

        try {
            hopperHelper = GraphHopperHelper.getInstance();
            costMatrix = hopperHelper.getCostMatrix(locaciones, true);
            problem = AlgoritmoHelper.buildVRPProblem(locaciones, vehiculoTipo,
                    costMatrix);
            System.out.println("Create VRP problem.");
            algorithm = new VRPAlgorithm(problem);
            System.out.println("Solve VRP problem.");
            solution = algorithm.solve();
            algorithm.printSolution(solution);
            draw(args, solution);
        } catch (Exception ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void draw(String [] args, ArrayList<GraphWalk> solution) {
        MapController.solution = solution;
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root =
                FXMLLoader.load(getClass().getResource(VIEW_PATH));

        Scene scene = new Scene(root);

        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }

}
