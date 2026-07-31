package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfService {

    public static boolean generarPdfEvento(String[] columnNames, Object[] rowData, File destino, List<Object[]> servicios, List<Object[]> invitados) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(destino));
            document.open();
            
            // Título: "Reporte de Evento Confirmado"
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Reporte de Evento Confirmado", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);
            document.add(titulo);
            
            // Fecha de generación
            Font fechaFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String fechaStr = "Generado el: " + ahora.format(formatter);
            Paragraph fecha = new Paragraph(fechaStr, fechaFont);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            fecha.setSpacingAfter(20);
            document.add(fecha);
            
            // Tabla principal del evento
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingAfter(20);
            table.setWidths(new float[]{4f, 6f});
            
            Font negritaFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            
            for (int i = 0; i < columnNames.length; i++) {
                PdfPCell cellLeft = new PdfPCell(new Phrase(columnNames[i], negritaFont));
                cellLeft.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellLeft.setPadding(8);
                table.addCell(cellLeft);
                
                Object valObj = (i < rowData.length) ? rowData[i] : null;
                String valStr = (valObj != null) ? valObj.toString() : "—";
                PdfPCell cellRight = new PdfPCell(new Phrase(valStr, normalFont));
                cellRight.setBackgroundColor(BaseColor.WHITE);
                cellRight.setPadding(8);
                table.addCell(cellRight);
            }
            document.add(table);

            // Agregar Servicios Adicionales
            if (servicios != null && !servicios.isEmpty()) {
                Paragraph subtitle = new Paragraph("Servicios Adicionales Contratados", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
                subtitle.setSpacingBefore(15);
                subtitle.setSpacingAfter(10);
                document.add(subtitle);
                
                PdfPTable servTable = new PdfPTable(3);
                servTable.setWidthPercentage(100);
                servTable.setSpacingAfter(20);
                servTable.setWidths(new float[]{4f, 4f, 2f});
                
                Font headFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
                String[] cabecerasServ = {"Servicio", "Proveedor", "Precio"};
                for (String c : cabecerasServ) {
                    PdfPCell h = new PdfPCell(new Phrase(c, headFont));
                    h.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    h.setPadding(6);
                    servTable.addCell(h);
                }
                
                Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
                for (Object[] serv : servicios) {
                    PdfPCell c1 = new PdfPCell(new Phrase(serv[0].toString(), cellFont));
                    c1.setPadding(6);
                    servTable.addCell(c1);
                    
                    PdfPCell c2 = new PdfPCell(new Phrase(serv[1].toString(), cellFont));
                    c2.setPadding(6);
                    servTable.addCell(c2);
                    
                    PdfPCell c3 = new PdfPCell(new Phrase("$" + String.format("%.2f", (Double)serv[2]), cellFont));
                    c3.setPadding(6);
                    servTable.addCell(c3);
                }
                document.add(servTable);
            }

            // Agregar Lista de Invitados
            if (invitados != null && !invitados.isEmpty()) {
                Paragraph subtitle = new Paragraph("Lista de Invitados", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
                subtitle.setSpacingBefore(15);
                subtitle.setSpacingAfter(10);
                document.add(subtitle);
                
                PdfPTable invTable = new PdfPTable(4);
                invTable.setWidthPercentage(100);
                invTable.setSpacingAfter(20);
                invTable.setWidths(new float[]{3f, 2f, 3f, 2f});
                
                Font headFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
                String[] cabecerasInv = {"Nombre", "DNI", "Preferencia Menú", "Asistencia"};
                for (String c : cabecerasInv) {
                    PdfPCell h = new PdfPCell(new Phrase(c, headFont));
                    h.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    h.setPadding(6);
                    invTable.addCell(h);
                }
                
                Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
                for (Object[] inv : invitados) {
                    PdfPCell c1 = new PdfPCell(new Phrase(inv[1].toString(), cellFont)); // Nombre
                    c1.setPadding(6);
                    invTable.addCell(c1);
                    
                    PdfPCell c2 = new PdfPCell(new Phrase(inv[0].toString(), cellFont)); // DNI
                    c2.setPadding(6);
                    invTable.addCell(c2);
                    
                    PdfPCell c3 = new PdfPCell(new Phrase(inv[3].toString(), cellFont)); // Menú
                    c3.setPadding(6);
                    invTable.addCell(c3);
                    
                    PdfPCell c4 = new PdfPCell(new Phrase(inv[4].toString(), cellFont)); // Asistencia
                    c4.setPadding(6);
                    invTable.addCell(c4);
                }
                document.add(invTable);
            }
            
            // Pie de página
            Font pieFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY);
            Paragraph pie = new Paragraph("Sistema de Gestión de Eventos", pieFont);
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);
            
            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (document.isOpen()) {
                document.close();
            }
            return false;
        }
    }

    public static boolean generarPdfReporteMultiTabla(String tituloPrincipal, List<String> subtitulos, List<String[]> listaColumnas, List<List<Object[]>> listaFilas, File destino) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(destino));
            document.open();
            
            // Título Principal
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph titulo = new Paragraph(tituloPrincipal, tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);
            document.add(titulo);
            
            // Fecha
            Font fechaFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            Paragraph fecha = new Paragraph("Generado el: " + ahora.format(formatter), fechaFont);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            fecha.setSpacingAfter(20);
            document.add(fecha);
            
            Font subFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font headFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
            
            for (int t = 0; t < subtitulos.size(); t++) {
                Paragraph sub = new Paragraph(subtitulos.get(t), subFont);
                sub.setSpacingBefore(10);
                sub.setSpacingAfter(10);
                document.add(sub);
                
                String[] columnas = listaColumnas.get(t);
                List<Object[]> filas = listaFilas.get(t);
                
                PdfPTable table = new PdfPTable(columnas.length);
                table.setWidthPercentage(100);
                table.setSpacingAfter(20);
                
                for (String c : columnas) {
                    PdfPCell h = new PdfPCell(new Phrase(c, headFont));
                    h.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    h.setPadding(6);
                    table.addCell(h);
                }
                
                for (Object[] fila : filas) {
                    for (Object valor : fila) {
                        String valStr = (valor != null) ? valor.toString() : "—";
                        PdfPCell c = new PdfPCell(new Phrase(valStr, cellFont));
                        c.setPadding(6);
                        table.addCell(c);
                    }
                }
                document.add(table);
            }
            
            // Pie
            Font pieFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY);
            Paragraph pie = new Paragraph("Sistema de Gestión de Eventos", pieFont);
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);
            
            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (document.isOpen()) {
                document.close();
            }
            return false;
        }
    }
}
