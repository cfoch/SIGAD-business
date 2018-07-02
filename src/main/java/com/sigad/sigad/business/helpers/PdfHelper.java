/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.business.Constantes;
import com.sigad.sigad.business.DetallePedido;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.pedido.controller.DatosPedidoController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexandra
 */
public class PdfHelper {

    private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 26, Font.BOLDITALIC);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);

    private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subcategoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    public PdfHelper() {

    }

    public void createPDF(File pdfNewFile) {
        // Aquí introduciremos el código para crear el PDF.

    }

    public void crearBoletaVenta(Pedido pedido) throws DocumentException {

        FileOutputStream fichero = null;
        try {
            Document document = new Document();
            fichero = new FileOutputStream("boleta.pdf");
            PdfWriter writter = PdfWriter.getInstance(document, fichero);
            document.open();
            //Metadata
            document.addTitle("Boleta de venta");
            document.addSubject("MaragaritaTel");
            //Primeros parrafos
            Chunk chunk = new Chunk("Boleta de venta", chapterFont);
            chunk.setBackground(BaseColor.WHITE);
            Chunk chunk2 = new Chunk("MargaritaTel - RUC: " + Constantes.RUC, smallBold);
            Chapter chapter = new Chapter(new Paragraph(chunk), 0);
            chapter.setNumberDepth(0);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            Section paragraphMoreS = chapter.addSection(new Paragraph());
            paragraphMoreS.add(chunk2);
            List list = new List(List.UNORDERED);
            list.setListSymbol("");
            ListItem item = new ListItem("Fecha de emisión : " + reportDate);
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            item = new ListItem("Señor(es) : " + pedido.getCliente().toString());
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            item = new ListItem("D.N.I : " + pedido.getCliente().getDni());
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            item = new ListItem("------------------------------------------------------------------------------------------------------------------------------");
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            paragraphMoreS.add(list);

            Integer numColumns = 7;
            PdfPTable table = new PdfPTable(numColumns);
            PdfPCell columnHeader;
            table.setWidths(new float[]{1, 3, 4, 8, 3, 3, 3});
            columnHeader = new PdfPCell(new Phrase("No"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Cantidad"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Nombre"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Descripcion"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Precio Unitario"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Descuento"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Importe"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            ArrayList<DetallePedido> detalle = new ArrayList(pedido.getDetallePedido());
            Integer j = 0;
            Double total = 0.0;
            table.setWidthPercentage(100);
            for (DetallePedido d : detalle) {
                table.addCell(String.valueOf(j + 1));
                table.addCell(d.getCantidad().toString());
                if (d.getProducto() != null) {
                    table.addCell(d.getProducto().getNombre());
                    table.addCell(d.getProducto().getDescripcion());
                    table.addCell(d.getProducto().getPrecio().toString());
                    Double desc = 0.0;
                    if (d.getDescuentoCategoria() != null) {
                        desc = GeneralHelper.roundTwoDecimals(d.getDescuentoCategoria().getValue() * d.getProducto().getPrecio());
                        table.addCell(desc.toString());
                    } else if (d.getDescuentoProducto() != null) {
                        desc = GeneralHelper.roundTwoDecimals(d.getDescuentoProducto().getValorPct() * d.getProducto().getPrecio());
                        table.addCell(desc.toString());
                    } else {
                        table.addCell("0.00");
                    }
                    Double subtotal = GeneralHelper.roundTwoDecimals(d.getCantidad() * d.getProducto().getPrecio() - desc);
                    total = total + subtotal;
                    table.addCell(subtotal.toString());

                } else {
                    table.addCell(d.getCombo().getNombre());
                    table.addCell(d.getCombo().getDescripcion());
                    table.addCell(d.getCombo().getPreciounireal().toString());
                    table.addCell("0.00");
                    Double subtotal = GeneralHelper.roundTwoDecimals(d.getCantidad() * d.getCombo().getPreciounireal());
                    total = total + subtotal;
                    table.addCell(subtotal.toString());

                }
                j = j + 1;
            }
            paragraphMoreS.add(table);
            numColumns = 2;
            PdfPTable table2 = new PdfPTable(numColumns);
            table2.setWidthPercentage(24);
            table2.setWidths(new float[]{3, 3});
            total = GeneralHelper.roundTwoDecimals(total);
            table2.addCell("Subtotal");
            table2.addCell(total.toString());
            Double descuento = 0.0;
            if (pedido.getDescuentoCliente() != null) {
                descuento = GeneralHelper.roundTwoDecimals(pedido.getDescuentoCliente().getValue() * total);
            }
            table2.addCell("Dcto.");
            table2.addCell(descuento.toString());
            Double impuesto = GeneralHelper.roundTwoDecimals((total - descuento) * HomeController.IGV);
            table2.addCell("I.G.V.");
            table2.addCell(impuesto.toString());
            Double totalReal = GeneralHelper.roundTwoDecimals(impuesto + total);
            table2.addCell("Total");
            table2.addCell(totalReal.toString());
            table2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paragraphMoreS.add(table2);
            document.add(chapter);
            document.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatosPedidoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fichero.close();
            } catch (IOException ex) {
                Logger.getLogger(DatosPedidoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void crearFacturaVenta(Pedido pedido) throws DocumentException {

        FileOutputStream fichero = null;
        try {
            
            Document document = new Document();
            fichero = new FileOutputStream("factura.pdf");
            
            PdfWriter writter = PdfWriter.getInstance(document, fichero);
            document.open();
            //Metadata
            document.addTitle("Factura");
            document.addSubject("MaragaritaTel");
            //Primeros parrafos
            Chunk chunk = new Chunk("Factura de venta", chapterFont);
            chunk.setBackground(BaseColor.WHITE);
            Chunk chunk2 = new Chunk("MargaritaTel - RUC: " + Constantes.RUC, smallBold);
            Chapter chapter = new Chapter(new Paragraph(chunk), 0);
            chapter.setNumberDepth(0);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);
            Section paragraphMoreS = chapter.addSection(new Paragraph());
            paragraphMoreS.add(chunk2);
            List list = new List(List.UNORDERED);
            list.setListSymbol("");
            ListItem item = new ListItem("Fecha de emisión : " + reportDate);
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            item = new ListItem("Señor(es) : " + pedido.getCliente().toString());
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            item = new ListItem("D.N.I : " + pedido.getCliente().getDni());
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            item = new ListItem("Empresa : " + pedido.getNombreEmpresa());
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            item = new ListItem("R.U.C : " + pedido.getRucFactura());
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            item = new ListItem("------------------------------------------------------------------------------------------------------------------------------");
            item.setAlignment(Element.ALIGN_JUSTIFIED);
            list.add(item);
            paragraphMoreS.add(list);

            Integer numColumns = 7;
            PdfPTable table = new PdfPTable(numColumns);
            PdfPCell columnHeader;
            table.setWidths(new float[]{1, 3, 4, 8, 3, 3, 3});
            columnHeader = new PdfPCell(new Phrase("No"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Cantidad"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Nombre"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Descripcion"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Precio Unitario"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Descuento"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            columnHeader = new PdfPCell(new Phrase("Importe"));
            columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(columnHeader);

            ArrayList<DetallePedido> detalle = new ArrayList(pedido.getDetallePedido());
            Integer j = 0;
            Double total = 0.0;
            table.setWidthPercentage(100);
            for (DetallePedido d : detalle) {
                table.addCell(String.valueOf(j + 1));
                table.addCell(d.getCantidad().toString());
                if (d.getProducto() != null) {
                    table.addCell(d.getProducto().getNombre());
                    table.addCell(d.getProducto().getDescripcion());
                    table.addCell(d.getProducto().getPrecio().toString());
                    Double desc = 0.0;
                    if (d.getDescuentoCategoria() != null) {
                        desc = GeneralHelper.roundTwoDecimals(d.getDescuentoCategoria().getValue() * d.getProducto().getPrecio());
                        table.addCell(desc.toString());
                    } else if (d.getDescuentoProducto() != null) {
                        desc = GeneralHelper.roundTwoDecimals(d.getDescuentoProducto().getValorPct() * d.getProducto().getPrecio());
                        table.addCell(desc.toString());
                    } else {
                        table.addCell("0.00");
                    }
                    Double subtotal = GeneralHelper.roundTwoDecimals(d.getCantidad() * d.getProducto().getPrecio() - desc);
                    total = total + subtotal;
                    table.addCell(subtotal.toString());

                } else {
                    table.addCell(d.getCombo().getNombre());
                    table.addCell(d.getCombo().getDescripcion());
                    table.addCell(d.getCombo().getPreciounireal().toString());
                    table.addCell("0.00");
                    Double subtotal = GeneralHelper.roundTwoDecimals(d.getCantidad() * d.getCombo().getPreciounireal());
                    total = total + subtotal;
                    table.addCell(subtotal.toString());

                }
                j = j + 1;
            }
            paragraphMoreS.add(table);
            numColumns = 2;
            PdfPTable table2 = new PdfPTable(numColumns);
            table2.setWidthPercentage(24);
            table2.setWidths(new float[]{3, 3});
            total = GeneralHelper.roundTwoDecimals(total);
            table2.addCell("Subtotal");
            table2.addCell(total.toString());
            Double descuento = 0.0;
            if (pedido.getDescuentoCliente() != null) {
                descuento = GeneralHelper.roundTwoDecimals(pedido.getDescuentoCliente().getValue() * total);
            }
            table2.addCell("Dcto.");
            table2.addCell(descuento.toString());
            Double impuesto = GeneralHelper.roundTwoDecimals((total - descuento) * HomeController.IGV);
            table2.addCell("I.G.V.");
            table2.addCell(impuesto.toString());
            Double totalReal = GeneralHelper.roundTwoDecimals(impuesto + total);
            table2.addCell("Total");
            table2.addCell(totalReal.toString());
            table2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paragraphMoreS.add(table2);
            document.add(chapter);
            document.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatosPedidoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fichero.close();
            } catch (IOException ex) {
                Logger.getLogger(DatosPedidoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
