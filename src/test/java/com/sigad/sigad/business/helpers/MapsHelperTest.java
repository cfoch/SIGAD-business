/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.grupo1.simulated_annealing.Locacion;
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
public class MapsHelperTest {
    private static final double DELTA = 1e-15;
    private Locacion [] locaciones;
    private final double [][] coordenadas = {
        // Salinar 115
        {-12.072564, -77.071882},
        // Avenida José Leguía y Meléndez 1786-1846 Pueblo Libre 15084
        {-12.074211, -77.072853},
        // Panaderia Thakrispan. Municipalidad Metropolitana de, Lima 15084
        {-12.077264, -77.074422},
        // Santa Francisca. Cercado de Lima 15088
        {-12.061329, -77.078440}
    };

    public MapsHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        int i;
        // Inicializar locaciones.
        locaciones = new Locacion[coordenadas.length];
        for (i = 0; i < coordenadas.length; i++) {
            Locacion locacion;
            locacion = new Locacion(i, "Locacion-" + i, Locacion.Tipo.OTRO,
                    coordenadas[i][0], coordenadas[i][1]);
            locaciones[i] = locacion;
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getDistanceMatrix method, of class MapsHelper.
     */
    @Test
    public void testGetDistanceMatrix() throws Exception {
        System.out.println("getDistanceMatrix");
        int i, j;
        MapsHelper helper;
        helper = MapsHelper.getInstance();
        DistanceMatrix result = helper.getDistanceMatrix(locaciones,
                locaciones);
        Assert.assertNotNull(result);
        Assert.assertEquals(locaciones.length, result.rows.length);
        for (i = 0; i < result.rows.length; i++) {
            Assert.assertEquals(locaciones.length,
                    result.rows[i].elements.length);
            for (j = 0; j < result.rows[i].elements.length; j++) {
                Distance distance = result.rows[i].elements[j].distance;
                if (i == j && distance != null) {
                    Assert.assertEquals(0, distance.inMeters);
                }
            }
        }
    }

    /**
     * Test of distanceMatrixTo2DArray method, of class MapsHelper.
     */
    @Test
    public void testDistanceMatrixTo2DArray() {
        System.out.println("distanceMatrixTo2DArray");
        int i, j;
        DistanceMatrix distanceMatrix;
        DistanceMatrixRow [] rows;
        final long [][] costMatrix = {
            {123, 456, 789, 101},
            {121, 314, 156, 178},
            {192, 212, 232, 425},
            {262, 728, 293, 313}
        };
        double [][] resultNoAvg;
        double [][] resultAvg;
        String dummy[] = { "dummy" };

        rows = new DistanceMatrixRow[costMatrix.length];
        for (i = 0; i < costMatrix.length; i++) {
            DistanceMatrixRow row = new DistanceMatrixRow();
            DistanceMatrixElement [] elements =
                    new DistanceMatrixElement[costMatrix.length];
            row.elements = elements;

            for (j = 0; j < costMatrix.length; j++) {
                row.elements[j] = new DistanceMatrixElement();
                row.elements[j].distance = new Distance();
                row.elements[j].distance.inMeters = costMatrix[i][j];
            }
            rows[i] = row;
        }
        distanceMatrix = new DistanceMatrix(dummy, dummy, rows);

        resultNoAvg = MapsHelper.distanceMatrixTo2DArray(distanceMatrix, false);
        resultAvg = MapsHelper.distanceMatrixTo2DArray(distanceMatrix, true);

        Assert.assertEquals(costMatrix.length, resultNoAvg.length);
        Assert.assertEquals(costMatrix.length, resultAvg.length);

        for (i = 0; i < resultNoAvg.length; i++) {
            Assert.assertEquals(costMatrix.length, resultNoAvg[i].length);
            Assert.assertEquals(costMatrix.length, resultAvg[i].length);
            for (j = 0; j < resultNoAvg.length; j++) {
                double distAvgExpected;
                Assert.assertEquals(costMatrix[i][j], resultNoAvg[i][j], DELTA);
                distAvgExpected = ((double) costMatrix[i][j] +
                        (double)costMatrix[j][i]) / 2;
                Assert.assertEquals(distAvgExpected, resultAvg[i][j], DELTA);
            }
        }
    }
}
