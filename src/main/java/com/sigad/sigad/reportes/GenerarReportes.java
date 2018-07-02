/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.reportes;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.Constantes;
import com.sigad.sigad.business.helpers.BaseHelper;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

/**
 *
 * @author paul
 */
public class GenerarReportes {
    
    private Session session = null;
    private Connection conn = null;
    private final static Logger LOGGER = Logger.getLogger(GenerarReportes.class.getName());
    
    // metodo para iniciar conexion
    private void iniciarConexion() {
        try{
            session = LoginController.serviceInit();
            LOGGER.log(Level.INFO, "Habriendo conexion para reportes");
            //conn = DriverManager.getConnection(Constantes.BD_CONNECTION_STRING, Constantes.BD_USER, Constantes.BD_PASS);
            conn = session.getSessionFactory().getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class).getConnection();
            LOGGER.log(Level.INFO, "Conexion abierta con exito");
        }
        catch(SQLException e) {
            LOGGER.log(Level.SEVERE, "Conexion fallo al intentar abrirse");
        }
    }
    
    // rutaFinal se refiere a la ruta donde se guardara el archivo, no incluya el nombre del archivo ni la extension
    public void reporte(String rutaFinal, String fileName, String reportName){
        try {
            //JasperReport report = (JasperReport) JRLoader.loadObjectFromFile("Insumos.jasper");
            File f = new File(fileName);
            JasperReport report = JasperCompileManager.compileReport(f.getAbsolutePath());
            Map parameters = new HashMap();
            if (session==null)
                iniciarConexion();
            JasperPrint jprint = JasperFillManager.fillReport(report, parameters, conn);
            JasperViewer jv = new JasperViewer(jprint, false);
            jv.setTitle(reportName);
            jv.setVisible(true);
            //conn.close();
            // exportar PDF
            generarPDF(jprint, rutaFinal, reportName);
            LOGGER.log(Level.INFO, "Reporte de insumo creado con exito");
        }
        catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurrio un error al intentar abrir el reporte %s en pantalla", reportName);
            JOptionPane.showMessageDialog(null, "Error al mostrar el reporte : " + e);
        }
    }
    
    public void reporteVentas(String rutaFinal, String fileName, String reportName, Long tiendaId){
        try {
            //JasperReport report = (JasperReport) JRLoader.loadObjectFromFile("Insumos.jasper");
            File f = new File(fileName);
            JasperReport report = JasperCompileManager.compileReport(f.getAbsolutePath());
            Map parameters = new HashMap();
            parameters.put("idTienda", tiendaId);
            iniciarConexion();
            JasperPrint jprint = JasperFillManager.fillReport(report, parameters, conn);
            JasperViewer jv = new JasperViewer(jprint, false);
            jv.setTitle(reportName);
            jv.setVisible(true);
            //conn.close();
            // exportar PDF
            generarPDF(jprint, rutaFinal, reportName);
            LOGGER.log(Level.INFO, "Reporte de ventas creado con exito");
        }
        catch(Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurrio un error al intentar abrir el reporte %s en pantalla", reportName);
            JOptionPane.showMessageDialog(null, "Error al mostrar el reporte : " + e);
        }
    }
    
    private void generarPDF(JasperPrint jasperPrint, String rutaFinal, String tipoReporte) {
        try {
            File directorioSalida = new File(rutaFinal);
            directorioSalida.mkdirs();
            JasperExportManager.exportReportToPdfFile(jasperPrint, rutaFinal + "/" + tipoReporte + ".pdf");
        }
        catch(Exception e) {
            LOGGER.log(Level.SEVERE, String.format("Algo salio mal al intentar crear el archivo %s", tipoReporte));
        }
    }
    
}
