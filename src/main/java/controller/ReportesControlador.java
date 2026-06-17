package controller;

import repository.Util;
import repository.ReporteDAO;
import model.PagoPorCliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la pantalla de reportes.
 * Integra las consultas de eventos originales con los nuevos reportes avanzados.
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
        try { return new ReporteDAO().obtenerServiciosPorTipo(); }
        catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
    }

    public List<Object[]> listarEventosCostosos() {
        try { return new ReporteDAO().obtenerEventosCostosos(); }
        catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
    }

    public List<Object[]> listarClientesTop() {
        try { return new ReporteDAO().obtenerClientesTop(); }
        catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
    }
}