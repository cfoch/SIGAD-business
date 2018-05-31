/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.grupo1.simulated_annealing.Locacion;
import com.grupo1.simulated_annealing.VRPProblem;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Vehiculo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cfoch
 */
public class AlgoritmoHelperTest {
    private static final double DELTA = 1e-15;
    private List<Pedido> pedidos;
    private Tienda tienda;
    private Vehiculo.Tipo vehiculoTipo;
    // Av. de la Marina 2155 Cercado de Lima 32
    private final double [] coordenadasTienda = {-12.078418, -77.083272};
    private final long idsPedidos[] = {
        0,
        1,
        5,
        9
    };
    private final double [][] coordenadasPedidos = {
        // Salinar 115
        {-12.072564, -77.071882},
        // Avenida José Leguía y Meléndez 1786-1846 Pueblo Libre 15084
        {-12.074211, -77.072853},
        // Panaderia Thakrispan. Municipalidad Metropolitana de, Lima 15084
        {-12.077264, -77.074422},
        // Santa Francisca. Cercado de Lima 15088
        {-12.061329, -77.078440}
    };
    private final double [] volumenesPedidos = {
        102,
        345,
        444,
        233
    };
    private final double[][] costMatrix = {
        {23, 54, 54, 45, 23},
        {23, 42, 42, 43, 42},
        {42, 75, 56, 34, 42},
        {96, 65, 53, 44, 43},
        {43, 43, 43, 64, 54}
    };

    public AlgoritmoHelperTest() {
        int i;
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
        tienda = new Tienda();
        tienda.setId(new Long(0));
        tienda.setDireccion("Direccion de prueba 0");
        tienda.setCooXDireccion(coordenadasTienda[0]);
        tienda.setCooYDireccion(coordenadasTienda[1]);
        vehiculoTipo = new Vehiculo.Tipo("Vehiculo Tipo A", 500);
        vehiculoTipo.setId(new Long(0));
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of buildVRPProblem method, of class AlgoritmoHelper.
     */
    @Test
    public void testCreateLocaciones() {
        System.out.println("buildVRPProblem");
        Locacion [] locaciones;
        int i;
        locaciones = AlgoritmoHelper.createLocaciones(tienda, pedidos);
        // Verificar tienda.
        Assert.assertEquals(Locacion.Tipo.DEPOSITO, locaciones[0].getTipo());
        Assert.assertEquals(0, locaciones[0].getId());
        Assert.assertEquals(coordenadasTienda[0], locaciones[0].getX(), DELTA);
        Assert.assertEquals(coordenadasTienda[1], locaciones[0].getY(), DELTA);
        Assert.assertEquals("Direccion de prueba 0", locaciones[0].getNombre());
        // Verificar pedidos.
        for (i = 0; i < pedidos.size(); i++) {
            Locacion locacion;
            Pedido pedido = pedidos.get(i);
            Long idExpected = pedido.getId();
            locacion =  locaciones[i + 1];
            Assert.assertEquals(pedido.getCooXDireccion(), locacion.getX(),
                    DELTA);
            Assert.assertEquals(pedido.getCooYDireccion(), locacion.getY(),
                    DELTA);
            Assert.assertEquals(pedido.getDireccionDeEnvio(),
                    locacion.getNombre());
            Assert.assertEquals(volumenesPedidos[i],
                    locacion.getServicio().getDemanda(), DELTA);
            Assert.assertEquals(Locacion.Tipo.OTRO, locacion.getTipo());
        }
    }

    /**
     * Test of buildVRPProblem method, of class AlgoritmoHelper.
     */
    @Test
    public void testBuildVRPProblem() throws Exception {
        System.out.println("buildVRPProblem");
        Locacion [] locaciones;
        Set<Locacion> setLocaciones;
        int i;
        boolean sameLocaciones;
        AlgoritmoHelper instance = new AlgoritmoHelper();

        locaciones = AlgoritmoHelper.createLocaciones(tienda, pedidos);
        VRPProblem problem = instance.buildVRPProblem(locaciones, vehiculoTipo,
                costMatrix);

        setLocaciones = problem.getGrafo().vertexSet();
        Assert.assertTrue(setLocaciones.containsAll(Arrays.asList(locaciones)));
        Assert.assertEquals(locaciones.length, setLocaciones.size());

        for (i = 0; i < locaciones.length; i++) {
            Assert.assertArrayEquals(costMatrix,
                    locaciones[i].getCostMatrix().getCostMatrix());
        }
    }

}
