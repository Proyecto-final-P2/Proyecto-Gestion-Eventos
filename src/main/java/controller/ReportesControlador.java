package controller;

import repository.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la pantalla de reportes de Eventos Confirmados.
 * Se encarga del acceso a datos consultando directamente la vista SQL VistasEventosConfirmados.
 */
public class ReportesControlador {

    /**
     * Retorna todos los eventos confirmados.
     * @return una lista de filas de eventos confirmados
     */
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

    /**
     * Retorna eventos confirmados filtrados por salón.
     * @param salon nombre del salón a filtrar
     * @return una lista de filas filtradas
     */
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

    /**
     * Retorna los nombres únicos de salones para cargar el JComboBox.
     * @return lista de nombres de salones
     */
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

    /**
     * Retorna los nombres de columnas de la vista (para el header de la JTable).
     * @return un arreglo con los nombres de las columnas
     */
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
}
