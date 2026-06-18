package controller;

import repository.Util;
import repository.ReporteDAO;
import model.PagoPorCliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controlador para la pantalla de reportes.
 * Integra las consultas de eventos originales con los nuevos reportes
 * avanzados.
 */
public class ReportesControlador {

    // --- REPORTES ORIGINALES (Eventos y Salones) ---

    public List<Object[]> getEventosConfirmados() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT * FROM VistasEventosConfirmados";
        try (Connection con = Util.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                rows.add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rows;
    }

    public List<Object[]> getEventosPorSalon(String salon) {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT * FROM VistasEventosConfirmados WHERE Salon = ?";
        try (Connection con = Util.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, salon);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int columnCount = md.getColumnCount();
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    rows.add(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rows;
    }

    public List<String> getSalones() {
        List<String> salones = new ArrayList<>();
        String sql = "SELECT DISTINCT Salon FROM VistasEventosConfirmados ORDER BY Salon";
        try (Connection con = Util.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String salon = rs.getString("Salon");
                if (salon != null) {
                    salones.add(salon);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return salones;
    }

    public String[] getColumnNames() {
        String sql = "SELECT * FROM VistasEventosConfirmados WHERE 1=0";
        try (Connection con = Util.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData md = rs.getMetaData();
            int count = md.getColumnCount();
            String[] cols = new String[count];
            for (int i = 1; i <= count; i++) {
                cols[i - 1] = md.getColumnLabel(i);
            }
            return cols;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new String[0];
        }
    }

    // --- REPORTES INTEGRADOS (Pagos y Reportes Avanzados) ---

    public List<PagoPorCliente> listarPagosPorCliente() {
        try {
            return new ReporteDAO().obtenerPagosPorCliente();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Object[]> listarServiciosPorTipo() {
        try {
            return new ReporteDAO().obtenerServiciosPorTipo();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Object[]> listarEventosCostosos() {
        try {
            return new ReporteDAO().obtenerEventosCostosos();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Object[]> listarClientesTop() {
        try {
            return new ReporteDAO().obtenerClientesTop();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Object[]> getServiciosContratados(int eventoId) {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT s.SE_Tipo, s.SE_Proveedor, c.CON_Precio " +
                "FROM Contratados c " +
                "JOIN Servicios s ON c.Servicios_SE_ID = s.SE_ID " +
                "WHERE c.Evento_E_ID = ?";
        try (Connection con = repository.Util.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getString(1),
                            rs.getString(2),
                            rs.getDouble(3)
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rows;
    }

    public List<Object[]> getInvitadosEvento(int eventoId) {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT i.IN_DNI, i.IN_NombreApellido, i.IN_Email, i.IN_PreferenciaMenu, i.IN_Asistencia " +
                "FROM Asiste a " +
                "JOIN Invitado i ON a.Invitado_IN_ID = i.IN_ID " +
                "WHERE a.Evento_E_ID = ?";
        try (Connection con = repository.Util.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5)
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rows;
    }

    public boolean generarPdfEvento(String[] columnNames, Object[] rowData, File destino) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(destino));
            document.open();

            // Título: "Reporte de Evento Confirmado" — fuente grande, negrita, centrado.
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Reporte de Evento Confirmado", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);
            document.add(titulo);

            // Fecha de generación: "Generado el: [fecha y hora actual]" — fuente pequeña,
            // alineado a la derecha.
            Font fechaFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String fechaStr = "Generado el: " + ahora.format(formatter);
            Paragraph fecha = new Paragraph(fechaStr, fechaFont);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            fecha.setSpacingAfter(20);
            document.add(fecha);

            // Tabla de dos columnas con todos los campos del evento
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingAfter(20);

            // Definir anchos relativos de las columnas (ej: 40% y 60%)
            table.setWidths(new float[] { 4f, 6f });

            Font negritaFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            for (int i = 0; i < columnNames.length; i++) {
                // Columna izquierda: nombre del campo — fondo gris claro, negrita
                PdfPCell cellLeft = new PdfPCell(new Phrase(columnNames[i], negritaFont));
                cellLeft.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellLeft.setPadding(8);
                table.addCell(cellLeft);

                // Columna derecha: valor del campo — fondo blanco
                Object valObj = (i < rowData.length) ? rowData[i] : null;
                String valStr = (valObj != null) ? valObj.toString() : "—";
                PdfPCell cellRight = new PdfPCell(new Phrase(valStr, normalFont));
                cellRight.setBackgroundColor(BaseColor.WHITE);
                cellRight.setPadding(8);
                table.addCell(cellRight);
            }
            document.add(table);

            // Agregar Servicios Adicionales si existen
            int eventoId = -1;
            try {
                if (rowData.length > 0 && rowData[0] != null) {
                    eventoId = Integer.parseInt(rowData[0].toString());
                }
            } catch (Exception ex) {
                // Fallback silencioso si no se puede parsear el ID
            }

            if (eventoId != -1) {
                List<Object[]> servicios = getServiciosContratados(eventoId);
                if (!servicios.isEmpty()) {
                    Paragraph subtitle = new Paragraph("Servicios Adicionales Contratados",
                            new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
                    subtitle.setSpacingBefore(15);
                    subtitle.setSpacingAfter(10);
                    document.add(subtitle);

                    PdfPTable servTable = new PdfPTable(3);
                    servTable.setWidthPercentage(100);
                    servTable.setSpacingAfter(20);
                    servTable.setWidths(new float[] { 4f, 4f, 2f });

                    // Cabeceras de la tabla de servicios
                    Font headFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
                    PdfPCell h1 = new PdfPCell(new Phrase("Servicio", headFont));
                    h1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    h1.setPadding(6);
                    servTable.addCell(h1);

                    PdfPCell h2 = new PdfPCell(new Phrase("Proveedor", headFont));
                    h2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    h2.setPadding(6);
                    servTable.addCell(h2);

                    PdfPCell h3 = new PdfPCell(new Phrase("Precio", headFont));
                    h3.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    h3.setPadding(6);
                    servTable.addCell(h3);

                    Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
                    for (Object[] serv : servicios) {
                        PdfPCell c1 = new PdfPCell(new Phrase(serv[0].toString(), cellFont));
                        c1.setPadding(6);
                        servTable.addCell(c1);

                        PdfPCell c2 = new PdfPCell(new Phrase(serv[1].toString(), cellFont));
                        c2.setPadding(6);
                        servTable.addCell(c2);

                        PdfPCell c3 = new PdfPCell(new Phrase("$" + String.format("%.2f", (Double) serv[2]), cellFont));
                        c3.setPadding(6);
                        servTable.addCell(c3);
                    }
                    document.add(servTable);
                }
            }

            // Agregar Lista de Invitados si existen
            if (eventoId != -1) {
                List<Object[]> invitados = getInvitadosEvento(eventoId);
                if (!invitados.isEmpty()) {
                    Paragraph subtitle = new Paragraph("Lista de Invitados",
                            new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
                    subtitle.setSpacingBefore(15);
                    subtitle.setSpacingAfter(10);
                    document.add(subtitle);

                    PdfPTable invTable = new PdfPTable(4);
                    invTable.setWidthPercentage(100);
                    invTable.setSpacingAfter(20);
                    invTable.setWidths(new float[] { 3f, 2f, 3f, 2f }); // Nombre, DNI, Menú, Asistencia

                    // Cabeceras de la tabla de invitados
                    Font headFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
                    PdfPCell h1 = new PdfPCell(new Phrase("Nombre", headFont));
                    h1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    h1.setPadding(6);
                    invTable.addCell(h1);

                    PdfPCell h2 = new PdfPCell(new Phrase("DNI", headFont));
                    h2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    h2.setPadding(6);
                    invTable.addCell(h2);

                    PdfPCell h3 = new PdfPCell(new Phrase("Preferencia Menú", headFont));
                    h3.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    h3.setPadding(6);
                    invTable.addCell(h3);

                    PdfPCell h4 = new PdfPCell(new Phrase("Asistencia", headFont));
                    h4.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    h4.setPadding(6);
                    invTable.addCell(h4);

                    Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
                    for (Object[] inv : invitados) {
                        PdfPCell c1 = new PdfPCell(new Phrase(inv[1].toString(), cellFont)); // Nombre
                        c1.setPadding(6);
                        invTable.addCell(c1);

                        PdfPCell c2 = new PdfPCell(new Phrase(inv[0].toString(), cellFont)); // DNI
                        c2.setPadding(6);
                        invTable.addCell(c2);

                        PdfPCell c3 = new PdfPCell(new Phrase(inv[3].toString(), cellFont)); // PreferenciaMenu
                        c3.setPadding(6);
                        invTable.addCell(c3);

                        PdfPCell c4 = new PdfPCell(new Phrase(inv[4].toString(), cellFont)); // Asistencia
                        c4.setPadding(6);
                        invTable.addCell(c4);
                    }
                    document.add(invTable);
                }
            }

            // Pie de página: "Sistema de Gestión de Eventos" — centrado, fuente pequeña,
            // gris.
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