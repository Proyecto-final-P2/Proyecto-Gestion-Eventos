package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    // --- 1. PAGOS POR EVENTO Y PAGADOR ---
    public List<Object[]> obtenerPagosPorEvento() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT e.E_ID, e.E_Tipo, p.P_Pagador, SUM(p.P_MontoPagado) AS TotalPagado, " +
                     "(s.SA_Costo + COALESCE((SELECT SUM(CON_Precio) FROM Contratados WHERE Evento_E_ID = e.E_ID), 0)) AS CostoTotal " +
                     "FROM Pago p " +
                     "JOIN Evento e ON p.Evento_E_ID = e.E_ID " +
                     "JOIN Salon s ON e.Salon_SA_ID = s.SA_ID " +
                     "GROUP BY e.E_ID, e.E_Tipo, p.P_Pagador, CostoTotal " +
                     "ORDER BY e.E_ID DESC";
        try (Connection con = Util.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5)});
            }
        }
        return lista;
    }

    // --- 2. SERVICIOS CONTRATADOS POR TIPO DE EVENTO ---
    public List<Object[]> obtenerServiciosPorTipo() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT Evento.E_Tipo, COUNT(Contratados.Servicios_SE_ID) AS ServiciosContratados " +
                     "FROM Evento JOIN Contratados ON Evento.E_ID = Contratados.Evento_E_ID " +
                     "GROUP BY Evento.E_Tipo";
        try (Connection con = Util.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getString(1), rs.getInt(2)});
            }
        }
        return lista;
    }

    // --- 3. EVENTOS CON COSTO TOTAL > $3000 ---
    public List<Object[]> obtenerEventosCostosos() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT e.E_ID, e.E_Tipo, c.C_NombreApellido, s.SA_Costo, " +
                     "COALESCE(SUM(con.CON_Precio), 0) AS MontoServicios, " +
                     "(s.SA_Costo + COALESCE(SUM(con.CON_Precio), 0)) AS CostoTotal " +
                     "FROM Evento e " +
                     "JOIN Cliente c ON e.Cliente_C_ID = c.C_ID " +
                     "JOIN Salon s ON e.Salon_SA_ID = s.SA_ID " +
                     "LEFT JOIN Contratados con ON e.E_ID = con.Evento_E_ID " +
                     "GROUP BY e.E_ID, e.E_Tipo, c.C_NombreApellido, s.SA_Costo " +
                     "HAVING CostoTotal > 3000 " +
                     "ORDER BY CostoTotal DESC";
        try (Connection con = Util.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6)});
            }
        }
        return lista;
    }

    // --- 4. TOP 5 EVENTOS MAS CAROS ---
    public List<Object[]> obtenerTop5EventosCaros() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT e.E_ID, e.E_Tipo, c.C_NombreApellido, " +
                     "(s.SA_Costo + COALESCE(SUM(con.CON_Precio), 0)) AS CostoTotal " +
                     "FROM Evento e " +
                     "JOIN Cliente c ON e.Cliente_C_ID = c.C_ID " +
                     "JOIN Salon s ON e.Salon_SA_ID = s.SA_ID " +
                     "LEFT JOIN Contratados con ON e.E_ID = con.Evento_E_ID " +
                     "GROUP BY e.E_ID, e.E_Tipo, c.C_NombreApellido, s.SA_Costo " +
                     "ORDER BY CostoTotal DESC LIMIT 5";
        try (Connection con = Util.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4)});
            }
        }
        return lista;
    }

    // --- REPORTES OPERATIVOS (Movidos desde ReportesControlador) ---

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
                    rows.add(new Object[]{
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
                    rows.add(new Object[]{
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
}