/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.helpers.cargaMasiva;

import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.ClienteDescuento;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.PedidoEstado;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Permiso;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoCategoria;
import com.sigad.sigad.business.ProductoCategoriaDescuento;
import com.sigad.sigad.business.ProductoDescuento;
import com.sigad.sigad.business.ProductoFragilidad;
import com.sigad.sigad.business.ProductoInsumo;
import com.sigad.sigad.business.Proveedor;
import com.sigad.sigad.business.ProveedorInsumo;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.TipoMovimiento;
import com.sigad.sigad.business.TipoPago;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.Vehiculo;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author paul
 */
public class CargaMasivaHelper {
    
    private final static Logger LOGGER = Logger.getLogger(CargaMasivaHelper.class.getName());
    
    /* Incio : Metodos disponibles */
    
    /* forma de consumo : se pasa como parametros la lista des tablas de las cuales se quieren generar sus plantillas
    y la ruta donde se quiere guardar el archivo, cabe senialar que la ruta debe contener el nombre del archivo con el formato xls (Excel)*/
    public static void generarCargaMasivaTemplate(ArrayList<String> listaTablaCarga, String destinoTemplate) {
        if (listaTablaCarga != null && !listaTablaCarga.isEmpty()) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            int rowIndex;
            for (String tablaCarga : listaTablaCarga) {
                HSSFSheet sheet = workbook.createSheet(tablaCarga);
                HSSFRow rowhead = sheet.createRow(0);
                rowIndex = 0;
                // Definimos las cabeceras
                switch(tablaCarga) {
                    case CargaMasivaConstantes.TABLA_DESCUENTOXUSUARIO:
                        rowhead.createCell(rowIndex).setCellValue("Tipo de Descuento Cliente");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Condicion");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Valor de Descuento");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Fecha Inicio(dd/mm/yyyy)");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Fecha Fin(dd/mm/yyyy)");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Ingrese correo de los clientes separados por comas");
                        break;
                    case CargaMasivaConstantes.TABLA_PRODUCTODESCUENTO:
                        rowhead.createCell(rowIndex).setCellValue("Nombre del Producto");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descuento(%)");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Stockmaximo");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Fecha Inicio(dd/mm/yyyy)");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Fecha Fin(dd/mm/yyyy)");
                        break;
                    case CargaMasivaConstantes.TABLA_DESCUENTOSCATEGORIA:
                        rowhead.createCell(rowIndex).setCellValue("Nombre de Categoria de Producto");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descuento(%)");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Fecha Inicio(dd/mm/yyyy)");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Fecha Fin(dd/mm/yyyy)");
                        break;
                    case CargaMasivaConstantes.TABLA_PROVEEDORXINSUMO:
                        rowhead.createCell(rowIndex).setCellValue("Nombre de Proveedor");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Nombre de Insumo");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Precio de Insumo");
                        break;
                    case CargaMasivaConstantes.TABLA_PRODUCTOCATEGORIA:
                        rowhead.createCell(rowIndex).setCellValue("Nombre");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        break;
                    case CargaMasivaConstantes.TABLA_PERFILES:
                        rowhead.createCell(rowIndex).setCellValue("Nombre");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Editable (S/N)");
                        break;
                    case CargaMasivaConstantes.TABLA_USUARIOS:
                        rowhead.createCell(rowIndex).setCellValue("Nombre(s)");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Apellido Paterno");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Apellido Materno");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Perfil");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Telefono Fijo");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("DNI");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Celular");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Correo Electronico");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Contraseña");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Intereses");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Tienda");
                        break;
                    case CargaMasivaConstantes.TABLA_PROVEEDORES:
                        rowhead.createCell(rowIndex).setCellValue("Nombre");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Ruc");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        break;
                    case CargaMasivaConstantes.TABLA_INSUMOS:
                        rowhead.createCell(rowIndex).setCellValue("Nombre");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Precio");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Tiempo de Vida (dias)");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Volumen por Unidad");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Imagen (Direccion Web)");
                        break;
                    case CargaMasivaConstantes.TABLA_TIENDAS:
                        rowhead.createCell(rowIndex).setCellValue("Direccion");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Ubicacion, Eje X");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Ubicacion, Eje Y");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Capacidad en peso");
                        break;
                    case CargaMasivaConstantes.TABLA_TIPOMOV:
                        rowhead.createCell(rowIndex).setCellValue("Nombre");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        break;
                    case CargaMasivaConstantes.TABLA_PERMISOS:
                        rowhead.createCell(rowIndex).setCellValue("Menu");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Icono");
                        break;
                    case CargaMasivaConstantes.TABLA_PERFILXPERMISO:
                        rowhead.createCell(rowIndex).setCellValue("Nombre de Perfil");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Menu e Icono (separados por coma)");
                        break;
                    case CargaMasivaConstantes.TABLA_FRAGILIDAD:
                        rowhead.createCell(rowIndex).setCellValue("Valor de Fragilidad");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        break;
                    case CargaMasivaConstantes.TABLA_PRODUCTOS:
                        rowhead.createCell(rowIndex).setCellValue("Nombre");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Precio Unitario");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Peso");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Imagen (Direccion Web)");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Categoria");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Nivel de Fragilidad");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Volumen");
                        break;
                    case CargaMasivaConstantes.TABLA_PRODXINSUMO:
                        rowhead.createCell(rowIndex).setCellValue("Nombre de Producto");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Nombre de Insumo");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Cantidad");
                        break;
                    case CargaMasivaConstantes.TABLA_TIPOPAGO:
                        rowhead.createCell(rowIndex).setCellValue("Descripcion del Tipo de Pago");
                        break;
                    case CargaMasivaConstantes.TABLA_PEDIDOESTADO:
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Nombre");
                        break;
                    case CargaMasivaConstantes.TABLA_TIPOVEHICULOS:
                        rowhead.createCell(rowIndex).setCellValue("Capacidad");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Marca");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Modelo");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Nombre");
                        break;
                    case CargaMasivaConstantes.TABLA_VEHICULOS:
                        rowhead.createCell(rowIndex).setCellValue("Descripcion");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Nombre");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Placa");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Tipo");
                        rowIndex++;
                        rowhead.createCell(rowIndex).setCellValue("Direccion Tienda asignada (opcional)");
                        break;
                    // agregar aqui el resto de casos
                    default:
                        LOGGER.log(Level.WARNING, "Tabla no reconocida, abortando ....");
                        return;
                }
                // dar formato a celdas
                for (int i=0;i<=rowIndex;i++)
                    sheet.autoSizeColumn(i);
            }
            try {
                FileOutputStream fileOut = new FileOutputStream(destinoTemplate);
                workbook.write(fileOut);
                fileOut.close();
                LOGGER.log(Level.INFO, "Plantilla(s) creada(s) con exito");
                }
            catch(Exception ex) {
                LOGGER.log(Level.SEVERE, "Error al generar la(s) plantilla(s)");
                System.out.print(ex);
            }
        }
    }
    
    /* forma de consumo : se pasa como unico parametro la ruta del archivo, el metodo identificara las hojas del archivo e iniciara la carga masiva */
    public static List<HojaReporte> CargaMasivaProceso(String archivoRuta) {
        try {
            // Declarando e inicializando variables a utilizar
            DataFormatter dataFormatter = new DataFormatter();
            Workbook workbook = WorkbookFactory.create(new File(archivoRuta));
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            Sheet sheet;
            String sheetName;
            Row row;
            Iterator<Row> rowIterator;
            //Iterator<Cell> cellIterator;
            int casosExitosos, casosFallidos;
            int hojasReconocidas = 0;
            Session session = LoginController.serviceInit();
            LOGGER.log(Level.INFO, "Se procede a inspeccionar archivo ...");
            // se itera sobre la prioridad establecida en CargaMasivaConstantes
            List<HojaReporte> reporteFinal = new ArrayList<>();
            for (String tablaEnCola : CargaMasivaConstantes.getList()) {
                sheet = workbook.getSheet(tablaEnCola);
                if (sheet!=null) {
                    sheetName = sheet.getSheetName();
                    LOGGER.log(Level.INFO, String.format("Hoja %s reconocida, iniciando proceso ...", sheetName));
                    hojasReconocidas++;
                    rowIterator = sheet.rowIterator();
                    rowIterator.next(); // nos saltamos la cabecera
                    // inicializamos contadores
                    casosExitosos = 0;
                    casosFallidos = 0;
                    // a este punto ya estamos en la fila inicial para comenzar a leer y cargar datos
                    while (rowIterator.hasNext()) {
                        row = rowIterator.next();
                        if (CargaMasivaHelper.SubirRegistroBD(sheetName, row, dataFormatter, session)) casosExitosos++;
                        else casosFallidos++;
                    }
                    LOGGER.log(Level.INFO, String.format("Hoja %s finalizada, reporte :", sheetName));
                    LOGGER.log(Level.INFO, String.format("Casos Exitosos : %d", casosExitosos));
                    LOGGER.log(Level.INFO, String.format("Casos Fallidos : %d", casosFallidos));
                    HojaReporte hojaReporte = new HojaReporte();
                    hojaReporte.setNombreHoja(sheetName);
                    hojaReporte.setCasosExitosos(casosExitosos);
                    hojaReporte.setCasosFallidos(casosFallidos);
                    reporteFinal.add(hojaReporte);
                }
            }
            // Cerrando conexion a Base de Datos
            session.close();
            workbook.close();
            LOGGER.log(Level.INFO, "Procesamiento Finalizado, reporte final :");
            LOGGER.log(Level.INFO, String.format("Cantidad de Hojas Procesadas : %s", hojasReconocidas));
            return reporteFinal;
        }
        catch(Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar masivamente, revisar la ruta del archivo");
            System.out.print(ex);
            return null;
        }
    }
    
    /* Fin : Metodos disponibles */
    
    /* Inicio : Metodos de Apoyo */
    
    // solo retorna el primero que encuentra
    private static Object busquedaGeneralInt(Session session, String nombreEntidad, String [] condiciones, int [] valoresCondiciones) {
        String hqlQuery = String.format("from %s where %s = %d", nombreEntidad, condiciones[0], valoresCondiciones[0]);
        for (int i=1;i<condiciones.length;i++)
            hqlQuery += String.format(" and %s = %d", condiciones[i], valoresCondiciones[i]);
        try{
            List<Object> resultadoBusqueda = session.createQuery(hqlQuery).list();
            return resultadoBusqueda.get(0);
        }
        catch(Exception e) {
            LOGGER.log(Level.WARNING, String.format("La busqueda de la entidad %s fallo", nombreEntidad));
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
            return null;
        }
    }
    
    // solo retornara el primero que encuentre
    private static Object busquedaGeneralString(Session session, String nombreEntidad, String [] condiciones, String [] valoresCondiciones) {
        String hqlQuery = String.format("from %s where %s='%s'", nombreEntidad, condiciones[0], valoresCondiciones[0]);
        for (int i=1;i<condiciones.length;i++)
            hqlQuery += String.format(" and %s='%s'", condiciones[i], valoresCondiciones[i]);
        try{
            List<Object> resultadoBusqueda = session.createQuery(hqlQuery).list();
            return resultadoBusqueda.get(0);
        }
        catch(Exception e) {
            LOGGER.log(Level.WARNING, String.format("La busqueda de la entidad %s fallo", nombreEntidad));
            System.out.println("====================================================================");
            System.out.println(e);
            System.out.println("====================================================================");
            return null;
        }
    }
    
    // implementacion de logica para cada tipo de tabla a cargar en bd, por cada registro a escanear
    private static boolean SubirRegistroBD(String tablaCarga, Row row, DataFormatter dataFormatter, Session session) {
        int index = 0;
        switch(tablaCarga) {
            case CargaMasivaConstantes.TABLA_DESCUENTOXUSUARIO:
                String tipoDscto = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                index++;
                Double condicionDscto = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (condicionDscto==null)
                    return false;
                index++;
                Double valorDscto = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (valorDscto==null)
                    return false;
                index++;
                try {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    Date fechaInicio = df.parse(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                    index++;
                    Date fechaFin = df.parse(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                    index++;
                    String clientesCorreos = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                    String [] correosFiltrados = clientesCorreos.split(",");
                    // guardamos el descuento de usuario
                    ClienteDescuento nuevoClieDesc = new ClienteDescuento();
                    nuevoClieDesc.setActivo(true);
                    nuevoClieDesc.setTipo(tipoDscto);
                    nuevoClieDesc.setCondicion(condicionDscto);
                    nuevoClieDesc.setValue(valorDscto);
                    nuevoClieDesc.setFechaInicio(new java.sql.Date(fechaInicio.getTime()));
                    nuevoClieDesc.setFechaFin(new java.sql.Date(fechaFin.getTime()));
                    Set<Usuario> listaUsuarioClientes = new HashSet<>();
                    for (int i=0;i<correosFiltrados.length;i++) {
                        Usuario usuarioCliente = (Usuario) CargaMasivaHelper.busquedaGeneralString(session, "Usuario", new String[] {"correo"}, new String [] {correosFiltrados[i]});
                        listaUsuarioClientes.add(usuarioCliente);
                    }
                    nuevoClieDesc.setClientes(listaUsuarioClientes);
                    return CargaMasivaHelper.guardarObjeto(nuevoClieDesc, session);
                }
                catch(Exception e) {
                    LOGGER.log(Level.SEVERE, "No se pudo convertir fechas de inicio  o fin");
                    return false;
                }
            case CargaMasivaConstantes.TABLA_PRODUCTODESCUENTO:
                String nombreProd = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                index++;
                Double dsctoProd = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (dsctoProd==null)
                    return false;
                index++;
                Integer stockmaximo = (Integer) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), true);
                if (stockmaximo==null)
                    return false;
                try {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    index++;
                    Date fechaInicio = df.parse(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                    index++;
                    Date fechaFin = df.parse(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                    if (dsctoProd!=null && dsctoProd>0.0) {
                        Producto prod = (Producto) CargaMasivaHelper.busquedaGeneralString(session, "Producto", new String[] {"nombre"}, new String [] {nombreProd});
                        if (prod!=null) {
                            ProductoDescuento nuevoDescProd = new ProductoDescuento();
                            nuevoDescProd.setActivo(true);
                            nuevoDescProd.setCodCupon(null);
                            nuevoDescProd.setDuracionDias(null);
                            nuevoDescProd.setProducto(prod);
                            nuevoDescProd.setValorPct(dsctoProd);
                            nuevoDescProd.setFechaInicio(fechaInicio);
                            nuevoDescProd.setFechaFin(fechaFin);
                            return CargaMasivaHelper.guardarObjeto(nuevoDescProd, session);
                        }
                        LOGGER.log(Level.WARNING, "No se encontro producto referenciada");
                    }
                    else
                        LOGGER.log(Level.WARNING, "Las fechas que se indican no tiene validaciones entre ellas");
                    return false;
                }
                catch(Exception e) {
                    LOGGER.log(Level.SEVERE, "No se pudo convertir fechas de inicio  o fin");
                    return false;
                }
            case CargaMasivaConstantes.TABLA_DESCUENTOSCATEGORIA:
                String nombreCategoriaProd = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                System.out.println(nombreCategoriaProd);
                index++;
                Double dsctoCategoria = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (dsctoCategoria==null)
                    return false;
                index++;
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    java.util.Date fechaInicio = df.parse(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                    index++;
                    java.util.Date fechaFin = df.parse(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                    if (dsctoCategoria>0.0) {
                        LOGGER.log(Level.INFO, "Orden de fechas correcto");
                        ProductoCategoria pc = (ProductoCategoria) CargaMasivaHelper.busquedaGeneralString(session, "ProductoCategoria", new String[] {"nombre"}, new String [] {nombreCategoriaProd});
                        if (pc!=null) {
                            ProductoCategoriaDescuento nuevoPCD = new ProductoCategoriaDescuento();
                            nuevoPCD.setCategoria(pc);
                            nuevoPCD.setValue(dsctoCategoria);
                            nuevoPCD.setFechaInicio(new java.sql.Date(fechaInicio.getTime()));
                            nuevoPCD.setFechaFin(new java.sql.Date(fechaFin.getTime()));
                            nuevoPCD.setActivo(true);
                            return CargaMasivaHelper.guardarObjeto(nuevoPCD, session);
                        }
                        LOGGER.log(Level.WARNING, "No se encontro a la categoria referenciada");
                    }
                    else
                        LOGGER.log(Level.WARNING, "Las fechas que se indican no tiene validaciones entre ellas");
                    return false;
                }
                catch(Exception e) {
                    LOGGER.log(Level.SEVERE, "No se pudo convertir fechas de inicio  o fin");
                    return false;
                }
            case CargaMasivaConstantes.TABLA_PROVEEDORXINSUMO:
                String nombreProveedor = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                index++;
                String nombreInsumo = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                index++;
                Double precioInsumoProveedor = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (precioInsumoProveedor==null)
                    return false;
                if (precioInsumoProveedor>0.0) {
                    Proveedor provee = (Proveedor) CargaMasivaHelper.busquedaGeneralString(session, "Proveedor", new String [] {"nombre"}, new String [] {nombreProveedor});
                    Insumo insumo = (Insumo) CargaMasivaHelper.busquedaGeneralString(session, "Insumo", new String [] {"nombre"}, new String [] {nombreInsumo});
                    if (provee!=null && insumo!=null) {
                        ProveedorInsumo proveedorxinsumo = new ProveedorInsumo();
                        proveedorxinsumo.setActivo(true);
                        proveedorxinsumo.setProveedor(provee);
                        proveedorxinsumo.setInsumo(insumo);
                        proveedorxinsumo.setPrecio(precioInsumoProveedor);
                        return CargaMasivaHelper.guardarObjeto(proveedorxinsumo, session);
                    }
                    else
                        LOGGER.log(Level.SEVERE, "El proveedor o insumo indicado son invalidos");
                }
                else
                    LOGGER.log(Level.SEVERE, "El precio indicado no es valido");
                return false;
            case CargaMasivaConstantes.TABLA_PRODUCTOCATEGORIA:
                ProductoCategoria nuevoProdCat = new ProductoCategoria();
                nuevoProdCat.setActivo(true);   // logica de negocio
                String nombreProdCat = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(nombreProdCat))
                    nuevoProdCat.setNombre(nombreProdCat);
                else {
                    LOGGER.log(Level.SEVERE, "El nombre de una categoria de producto es un campo obligatorio");
                    return false;
                }
                index++;
                nuevoProdCat.setDescripcion(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                return CargaMasivaHelper.guardarObjeto(nuevoProdCat, session);
            case CargaMasivaConstantes.TABLA_PERFILES:
                Perfil nuevoPerfil = new Perfil();
                nuevoPerfil.setActivo(true);    // logica de negocio
                String nombrePerfil = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(nombrePerfil))
                    nuevoPerfil.setNombre(nombrePerfil);
                else {
                    LOGGER.log(Level.SEVERE, "El nombre de un perfil es un campo obligatorio");
                    return false;
                }
                index++;
                nuevoPerfil.setDescripcion(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                String editableTxtPerfil = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                Boolean editable = false;
                if(StringUtils.equals(editableTxtPerfil, "S"))
                    editable = true;
                nuevoPerfil.setEditable(editable);
                return CargaMasivaHelper.guardarObjeto(nuevoPerfil, session);
            case CargaMasivaConstantes.TABLA_USUARIOS:
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setActivo(true);   // logica de negocio
                String nombreUsuario = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(nombreUsuario))
                    nuevoUsuario.setNombres(nombreUsuario);
                else {
                    LOGGER.log(Level.SEVERE, "El nombre de usuario es un campo obligatorio");
                    return false;
                }
                index++;
                String apPaternoUsuario = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(apPaternoUsuario))
                    nuevoUsuario.setApellidoPaterno(apPaternoUsuario);
                else {
                    LOGGER.log(Level.SEVERE, "El apellido paterno es un campo obligatorio");
                    return false;
                }
                index++;
                String apMaternoUsuario = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(apMaternoUsuario))
                    nuevoUsuario.setApellidoMaterno(apMaternoUsuario);
                else {
                    LOGGER.log(Level.SEVERE, "El apellido materno es un campo obligatorio");
                    return false;
                }
                index++;
                String perfilNombre = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                // Buscando el perfil elegido
                Perfil perfilBuscado = (Perfil) CargaMasivaHelper.busquedaGeneralString(session, "Perfil", new String [] {"nombre"}, new String [] {perfilNombre});
                if (perfilBuscado!=null)
                    LOGGER.log(Level.INFO, String.format("Perfil %s encontrado con exito", perfilNombre));
                else {
                    LOGGER.log(Level.SEVERE, String.format("Perfil %s no encontrado, cancelando operacion", perfilNombre));
                    return false;
                }
                nuevoUsuario.setPerfil(perfilBuscado);
                index++;
                nuevoUsuario.setTelefono(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                String dniUsuario = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(dniUsuario))
                    nuevoUsuario.setDni(dniUsuario);
                else {
                    LOGGER.log(Level.SEVERE, "El DNI es un campo obligatorio");
                    return false;
                }
                index++;
                nuevoUsuario.setCelular(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                // otener correo
                String correoUsuario = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(correoUsuario))
                    nuevoUsuario.setCorreo(correoUsuario);
                else {
                    LOGGER.log(Level.SEVERE, String.format("No se introdujo un correo valido para el usuario %s", nuevoUsuario.getNombres()));
                    return false;
                }
                index++;
                // obtener contrasenia
                String constraseniaUsuario = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(constraseniaUsuario))
                    nuevoUsuario.setPassword(LoginController.encrypt(constraseniaUsuario));
                else {
                    LOGGER.log(Level.SEVERE, String.format("No se introdujo una contraseña valida para el correo %s", correoUsuario));
                    return false;
                }
                index++;
                nuevoUsuario.setIntereses(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                String tiendaUsuario = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if(StringUtils.isNotBlank(tiendaUsuario)){
                    Tienda tiendaBuscada = (Tienda) CargaMasivaHelper.busquedaGeneralString(session, "Tienda", new String [] {"direccion"}, new String [] {tiendaUsuario});    
                    if(tiendaBuscada != null){
                        nuevoUsuario.setTienda(tiendaBuscada);
                    }
                    else{
                        LOGGER.log(Level.SEVERE, String.format("No se introdujo una tienda existente"));
                        //return false;
                        nuevoUsuario.setTienda(null);
                    }
                }
                else{
                    LOGGER.log(Level.SEVERE, String.format("No se introdujo una tienda para el usuario"));
                    return false;
                }
                return CargaMasivaHelper.guardarObjeto(nuevoUsuario, session);
            case CargaMasivaConstantes.TABLA_PROVEEDORES:
                Proveedor nuevoProv = new Proveedor();
                nuevoProv.setNombre(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                String rucProved = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (rucProved!=null)
                    nuevoProv.setRuc(rucProved);
                else
                    return false;
                index++;
                nuevoProv.setDescripcion(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                return CargaMasivaHelper.guardarObjeto(nuevoProv, session);
            case CargaMasivaConstantes.TABLA_INSUMOS:
                Insumo nuevoInsumo = new Insumo();
                nuevoInsumo.setActivo(true);    // logica de negocio
                nuevoInsumo.setStockTotalFisico(0);        // logica de negocio, se inicializa nuevo insumo
                nuevoInsumo.setStockTotalLogico(0);
                String nombreInsumoAux = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(nombreInsumoAux))
                    nuevoInsumo.setNombre(nombreInsumoAux);
                else {
                    LOGGER.log(Level.SEVERE, "El nombre de insumo es un campo obligatorio");
                    return false;
                }
                index++;
                nuevoInsumo.setDescripcion(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                // se trata de obtener el precio del insumo
                Double precioInsumo = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (precioInsumo!=null)
                    nuevoInsumo.setPrecio(precioInsumo);
                else
                    return false;
                index++;
                Integer tiempoVidaInsumo = (Integer) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), true);
                if (tiempoVidaInsumo!=null)
                    nuevoInsumo.setTiempoVida(tiempoVidaInsumo);
                else
                    return false;
                index++;
                Double volumenInsumo = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                nuevoInsumo.setVolumen(volumenInsumo);
                index++;
                nuevoInsumo.setImagen(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                return CargaMasivaHelper.guardarObjeto(nuevoInsumo, session);
            case CargaMasivaConstantes.TABLA_TIENDAS:
                Tienda nuevaTienda = new Tienda();
                nuevaTienda.setActivo(true);
                nuevaTienda.setDireccion(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                Double codX = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (codX!=null)
                    nuevaTienda.setCooXDireccion(codX);
                else
                    return false;
                index++;
                Double codY = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (codY!=null)
                    nuevaTienda.setCooYDireccion(codY);
                else
                    return false;
                index++;
                nuevaTienda.setDescripcion(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                Double capacidadTienda = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (capacidadTienda!=null && capacidadTienda >= 0.0)
                    nuevaTienda.setCapacidad(capacidadTienda);
                else
                    return false;
                return CargaMasivaHelper.guardarObjeto(nuevaTienda, session);
            case CargaMasivaConstantes.TABLA_TIPOMOV:
                TipoMovimiento nuevoTipoMov = new TipoMovimiento();
                String nombreTipoMov = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(nombreTipoMov))
                    nuevoTipoMov.setNombre(nombreTipoMov);
                else {
                    LOGGER.log(Level.SEVERE, "El nombre del tipo de movimiento es un campo obligatorio");
                    return false;
                }
                index++;
                nuevoTipoMov.setDescripcion(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                return CargaMasivaHelper.guardarObjeto(nuevoTipoMov, session);
            case CargaMasivaConstantes.TABLA_PERMISOS:
                Permiso nuevoPermiso = new Permiso();
                String menuPermiso = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(menuPermiso))
                    nuevoPermiso.setMenu(menuPermiso);
                else {
                    LOGGER.log(Level.SEVERE, "El menu de un permiso es un campo obligatorio");
                    return false;
                }
                index++;
                String iconoPermiso = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(iconoPermiso))
                    nuevoPermiso.setIcono(iconoPermiso);
                else {
                    LOGGER.log(Level.SEVERE, "El icono de un permiso es un campo obligatorio");
                    return false;
                }
                return CargaMasivaHelper.guardarObjeto(nuevoPermiso, session);
            case CargaMasivaConstantes.TABLA_PERFILXPERMISO:
                String perfilNombreAux = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                Perfil perfilAsociado = null;
                if (StringUtils.isNotBlank(perfilNombreAux))
                    perfilAsociado = (Perfil) CargaMasivaHelper.busquedaGeneralString(session, "Perfil", new String [] {"nombre"}, new String [] {perfilNombreAux});
                else {
                    LOGGER.log(Level.WARNING, "No se identifico algun nombre de perfil valido");
                    return false;
                }
                if (perfilAsociado!=null) { // si el perfil mencionado fue encontrado entonces se continua con el proceso
                    LOGGER.log(Level.INFO, String.format("Perfil %s encontrado con exito", perfilNombreAux));
                    index++;
                    String permisoMenuxIcono = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                    if (StringUtils.isBlank(permisoMenuxIcono)) {
                        LOGGER.log(Level.WARNING, "No se encontro un permiso valido, abortando");
                        return false;
                    }
                    // identificamos la opcion y el icono en la variable permisoOpcionxIcono
                    String [] permisoAux = permisoMenuxIcono.split(",");
                    if ((permisoAux.length == 2) && (StringUtils.isNotBlank(permisoAux[0]) && StringUtils.isNotBlank(permisoAux[1]))){
                        Permiso permisoAsociado = (Permiso) CargaMasivaHelper.busquedaGeneralString(session, "Permiso", new String [] {"menu","icono"}, new String [] {StringUtils.trimToEmpty(permisoAux[0]), StringUtils.trimToEmpty(permisoAux[1])});
                        if (permisoAsociado!=null) {
                            LOGGER.log(Level.INFO, String.format("Permiso %s encontrado con exito", permisoMenuxIcono));
                            perfilAsociado.getPermisos().add(permisoAsociado);
                            return CargaMasivaHelper.actualizarObjeto(perfilAsociado, session);
                        }
                        else {
                            LOGGER.log(Level.WARNING, String.format("Permiso %s no encontrado, este permiso no sera considerado", permisoMenuxIcono));
                            return false;
                        }
                    }
                    else {
                        LOGGER.log(Level.WARNING, String.format("No se encontro Menu o Icono"));
                        return false;
                    }
                }
                else {
                    LOGGER.log(Level.SEVERE, String.format("Perfil %s no encontrado, cancelando operacion", perfilNombreAux));
                    return false;
                }
            case CargaMasivaConstantes.TABLA_FRAGILIDAD:
                ProductoFragilidad nuevaFrag = new ProductoFragilidad();
                String valorCandea = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                Integer valorParseado = (Integer) CargaMasivaHelper.validarParsing(valorCandea, true);
                if (valorParseado!=null && valorParseado>0)
                    nuevaFrag.setValor(valorParseado);
                else {
                    LOGGER.log(Level.SEVERE, "El valor de fragilidad no es valido");
                    return false;
                }
                index++;
                nuevaFrag.setDescripcion(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                return CargaMasivaHelper.guardarObjeto(nuevaFrag, session);
            case CargaMasivaConstantes.TABLA_PRODUCTOS:
                Producto nuevoProd = new Producto();
                nuevoProd.setActivo(true);  // logica de negocio
                nuevoProd.setStockFisico(0);  // logica de negocio
                nuevoProd.setStockLogico(0);
                nuevoProd.setNombre(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                nuevoProd.setPrecio((Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false));
                index++;
                Double pesoProd = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (pesoProd!=null)
                    nuevoProd.setPeso(pesoProd);
                else
                    return false;
                index++;
                nuevoProd.setImagen(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                nuevoProd.setDescripcion(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))));
                index++;
                String nombreCategoriaAsociada = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(nombreCategoriaAsociada)) {
                    ProductoCategoria categoriaAsociada = (ProductoCategoria) CargaMasivaHelper.busquedaGeneralString(session, "ProductoCategoria", new String[] {"nombre"}, new String[] {nombreCategoriaAsociada});
                    if (categoriaAsociada!=null){
                        LOGGER.log(Level.INFO, String.format("Categoria %s encontrado con exito", nombreCategoriaAsociada));
                        nuevoProd.setCategoria(categoriaAsociada);
                    }
                    else
                        LOGGER.log(Level.SEVERE, String.format("Categoria %s no encontrada, no se tendra en consideracion", nombreCategoriaAsociada));
                }
                else
                    LOGGER.log(Level.WARNING, String.format("No se especifico categoria para producto %s, se continua el proceso", nuevoProd.getNombre()));
                index++;
                String fragilidadAsociada = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(fragilidadAsociada)) {
                    if (fragilidadAsociada != null) {
                            ProductoFragilidad fragilidadProducto = (ProductoFragilidad) CargaMasivaHelper.busquedaGeneralString(session, "ProductoFragilidad", new String [] {"descripcion"}, new String [] {fragilidadAsociada});
                        if (fragilidadProducto!=null) {
                            LOGGER.log(Level.INFO, String.format("Fragilidad %s encontrada con exito", fragilidadProducto));
                            nuevoProd.setFragilidad(fragilidadProducto);
                        }
                        else
                            LOGGER.log(Level.SEVERE, String.format("Fragilidad %s no encontrada, no se tendra en consideracion", fragilidadProducto));
                    }
                }
                else
                    LOGGER.log(Level.WARNING, String.format("No se especifico fragilidad para producto %s, se continua el proceso", nuevoProd.getNombre()));
                index++;
                String volumenCadena = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                Double volumenValor = (Double) CargaMasivaHelper.validarParsing(volumenCadena, false);
                if (volumenValor!=null)
                    nuevoProd.setVolumen(volumenValor);
                else
                    return false;
                return CargaMasivaHelper.guardarObjeto(nuevoProd, session);
            case CargaMasivaConstantes.TABLA_PRODXINSUMO:
                String nombreProductoAsociado = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(nombreProductoAsociado)) {
                    Producto prodAsociado = (Producto) CargaMasivaHelper.busquedaGeneralString(session, "Producto", new String[] {"nombre"}, new String[] {nombreProductoAsociado});
                    if (prodAsociado!=null) {
                        LOGGER.log(Level.INFO, String.format("Producto %s encontrado con exito", nombreProductoAsociado));
                        index++;
                        String nombreInsumoAsociado = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                        if (StringUtils.isNotBlank(nombreInsumoAsociado)) {
                            Insumo insumoAsociado = (Insumo) CargaMasivaHelper.busquedaGeneralString(session, "Insumo", new String[] {"nombre"}, new String[] {nombreInsumoAsociado});
                            if (insumoAsociado!=null) {
                                LOGGER.log(Level.INFO, String.format("Insumo %s encontrado con exito", nombreInsumoAsociado));
                                index++;
                                Double cantidadAsociada = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                                if (cantidadAsociada!=null) {
                                    ProductoInsumo nuevoProdxInsumo = new ProductoInsumo();
                                    nuevoProdxInsumo.setProducto(prodAsociado);
                                    nuevoProdxInsumo.setInsumo(insumoAsociado);
                                    nuevoProdxInsumo.setCantidad(cantidadAsociada);
                                    return CargaMasivaHelper.guardarObjeto(nuevoProdxInsumo, session);
                                }
                                else 
                                    return false;
                            }
                            else {
                                LOGGER.log(Level.SEVERE, String.format("Insumo %s no encontrado, cancelando operacion", nombreInsumoAsociado));
                                return false;
                            }
                        }
                        else {
                            LOGGER.log(Level.SEVERE, String.format("Insumo %s no detectado", nombreInsumoAsociado));
                            return false;
                        }
                    }
                    else {
                        LOGGER.log(Level.SEVERE, String.format("Producto %s no encontrado, cancelando operacion", nombreProductoAsociado));
                        return false;
                    }
                }
                else {
                    LOGGER.log(Level.SEVERE, String.format("Producto %s no detectado", nombreProductoAsociado));
                    return false;
                }
            case CargaMasivaConstantes.TABLA_TIPOPAGO:
                String descripTipoPago = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(descripTipoPago)) {
                    TipoPago nuevoTipoPago = new TipoPago();
                    nuevoTipoPago.setDescripcion(descripTipoPago);
                    return CargaMasivaHelper.guardarObjeto(nuevoTipoPago, session);
                }
                else {
                    LOGGER.log(Level.SEVERE, "No se identifica una descripcion valida de tipo de pago");
                    return false;
                }
            case CargaMasivaConstantes.TABLA_PEDIDOESTADO:
                PedidoEstado nuevoPedidoEstado = new PedidoEstado();
                String descripPedidoEstado = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(descripPedidoEstado))
                    nuevoPedidoEstado.setDescripcion(descripPedidoEstado);
                index++;
                String nombPedidoEstado = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isNotBlank(nombPedidoEstado))
                    nuevoPedidoEstado.setNombre(nombPedidoEstado);
                else{
                    LOGGER.log(Level.SEVERE, "El nombre de un estado de pedido es un campo obligatorio");
                    return false;
                }
                return CargaMasivaHelper.guardarObjeto(nuevoPedidoEstado, session);
            case CargaMasivaConstantes.TABLA_TIPOVEHICULOS:
                Double capacidadTipoVehiculo = (Double) CargaMasivaHelper.validarParsing(StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index))), false);
                if (capacidadTipoVehiculo != null){
                    index++;
                    String descripcionTipoVehiculo = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                    if(StringUtils.isNotBlank(descripcionTipoVehiculo)){
                        index++;
                        String marcaTipoVehiculo = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                        index++;
                        String modeloTipoVehiculo = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                        index++;
                        String nombreTipoVehiculo = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                        if(StringUtils.isNotBlank(nombreTipoVehiculo)){
                            Vehiculo.Tipo nuevoTipoVehiculo = new Vehiculo.Tipo();
                            nuevoTipoVehiculo.setCapacidad(capacidadTipoVehiculo);
                            nuevoTipoVehiculo.setDescripcion(descripcionTipoVehiculo);
                            nuevoTipoVehiculo.setMarca(marcaTipoVehiculo);
                            nuevoTipoVehiculo.setModelo(modeloTipoVehiculo);
                            nuevoTipoVehiculo.setNombre(nombreTipoVehiculo);
                            return CargaMasivaHelper.guardarObjeto(nuevoTipoVehiculo, session);
                        }
                        else{
                            LOGGER.log(Level.SEVERE, "No se identifica un nombre valido de tipo de vehiculo");
                            return false;    
                        }
                    }
                    else{
                        LOGGER.log(Level.SEVERE, "No se identifica una descripcion valida de tipo de vehiculo");
                        return false;
                    }
                }
                else{
                    LOGGER.log(Level.SEVERE, "No se identifica una capacidad valida de tipo de vehiculo");
                    return false;
                }
            case CargaMasivaConstantes.TABLA_VEHICULOS:
                Vehiculo.Tipo tipoVehiculoAsociado = null;
                String descripcionVehiculo = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                index++;
                String nombreVehiculo = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                index++;
                String placaVehiculo = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if (StringUtils.isBlank(placaVehiculo)) {
                    LOGGER.log(Level.SEVERE, "No se identifica una placa correcta para el vehiculo");
                    return false;
                }
                index++;
                String nombreTipoVehiculoAsociado = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                if(StringUtils.isBlank(nombreTipoVehiculoAsociado)){
                    LOGGER.log(Level.SEVERE, "No se identifica un nombre de tipo de vehiculo para el vehiculo");
                    return false;
                }
                tipoVehiculoAsociado = (Vehiculo.Tipo) CargaMasivaHelper.busquedaGeneralString(session, Vehiculo.Tipo.class.getName(), new String[] {"nombre"}, new String[] {nombreTipoVehiculoAsociado});
                if (tipoVehiculoAsociado == null) {
                    LOGGER.log(Level.SEVERE, "No se identifica un nombre de tipo de vehiculo para el vehiculo");
                    return false;
                }
                index++;
                String tiendaRelacionada = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                Tienda tiendaAsociada = null;
                if (StringUtils.isBlank(tiendaRelacionada))
                    LOGGER.log(Level.WARNING, "No se indica una tienda para este vehículo");
                else {
                    tiendaAsociada = (Tienda) CargaMasivaHelper.busquedaGeneralString(session, "Tienda", new String[] {"direccion"}, new String[] {tiendaRelacionada});
                    if (tiendaAsociada==null) {
                        LOGGER.log(Level.SEVERE, String.format("Tienda %s no encontrada", tiendaRelacionada));
                        return false;
                    }
                }
                Vehiculo nuevoVehiculo = new Vehiculo();
                nuevoVehiculo.setDescripcion(descripcionVehiculo);
                nuevoVehiculo.setNombre(nombreVehiculo);
                nuevoVehiculo.setPlaca(placaVehiculo);
                nuevoVehiculo.setTienda(tiendaAsociada);
                nuevoVehiculo.setTipo(tipoVehiculoAsociado);
                return CargaMasivaHelper.guardarObjeto(nuevoVehiculo, session);
                /*
                if(StringUtils.isNotBlank(placaVehiculo)){
                    index++;
                    String nombreTipoVehiculoAsociado = StringUtils.trimToEmpty(dataFormatter.formatCellValue(row.getCell(index)));
                    if(StringUtils.isNotBlank(nombreTipoVehiculoAsociado)){
                        Vehiculo.Tipo tipoVehiculoAsociado = (Vehiculo.Tipo) CargaMasivaHelper.busquedaGeneralString(session, "Vehiculo$Tipo", new String[] {"nombre"}, new String[] {nombreTipoVehiculoAsociado});    
                        if(tipoVehiculoAsociado != null){
                            Vehiculo nuevoVehiculo = new Vehiculo(tipoVehiculoAsociado, placaVehiculo);
                            nuevoVehiculo.setNombre(nombreVehiculo);
                            nuevoVehiculo.setDescripcion(descripcionVehiculo);
                            return CargaMasivaHelper.guardarObjeto(nuevoVehiculo, session);
                        }
                        else{
                            LOGGER.log(Level.SEVERE, "No se identifica un tipo de vehiculo valido para el vehiculo");
                            return false;    
                        }
                    }
                    else{
                        LOGGER.log(Level.SEVERE, "No se identifica un nombre de tipo de vehiculo para el vehiculo");
                        return false;
                    }
                }
                else{
                    LOGGER.log(Level.SEVERE, "No se identifica una placa correcta para el vehiculo");
                    return false;
                }
                */
            // colocar aqui los demas casos para el resto de tablas de carga masiva
            default:
                return false;
        }
    }
    
    public static Object validarParsing(String numero, boolean esInteger) {
        try {
            numero = numero.replaceAll(",", ".");
            System.out.println(String.format("Valor a ser parseado %s", numero));
            if (esInteger)
                return Integer.valueOf(numero);
            else
                return Double.valueOf(numero);
        }
        catch(Exception e) {
            LOGGER.log(Level.SEVERE, String.format("Error en el parseo, revisar el valor : %s", numero));
            return null;
        }
    }
    
    public static java.sql.Timestamp parseDate(String dateStr){
        SimpleDateFormat dateFormatter = new SimpleDateFormat ("yyyy-MM-dd");
        try{
            java.util.Date date = dateFormatter.parse(dateStr);
            return new java.sql.Timestamp(date.getTime());
        }
        catch(Exception e){
            return null;
        }
    }
    
    public static java.sql.Time parseTime(String timeStr){
        SimpleDateFormat dateFormatter = new SimpleDateFormat ("HH:mm");
        try{
            java.util.Date date = dateFormatter.parse(timeStr);
            return new java.sql.Time(date.getTime());
        }
        catch(Exception e){
            return null;
        }
    }
    
    public static boolean actualizarObjeto(Object object, Session session) {
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(object);
            tx.commit();
            LOGGER.log(Level.FINE, "Objeto actualizado con exito");
            return true;
        }
        catch(Exception he) {
            if (tx!=null)   tx.rollback();
            LOGGER.log(Level.SEVERE, "Error al intentar actualizar objeto");
            System.out.println("====================================================================");
            System.out.println(he);
            System.out.println("====================================================================");
            return false;
        }
    }
    
    public static boolean guardarObjeto(Object object, Session session) {
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(object);
            tx.commit();
            LOGGER.log(Level.FINE, "Objeto guardado con exito");
            return true;
        }
        catch(Exception he) {
            if (tx!=null)   tx.rollback();
            LOGGER.log(Level.SEVERE, "Error al intentar guardar objeto");
            System.out.println("====================================================================");
            System.out.println(he);
            System.out.println("====================================================================");
            return false;
        }
    }
    
    /* Fin : Metodos de Apoyo */
    
}