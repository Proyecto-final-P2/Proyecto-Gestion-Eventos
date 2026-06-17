package repository;

import model.PagoPorCliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    // --- REPORTE ORIGINAL (Sprint anterior) ---
    public List<PagoPorCliente> obtenerPagosPorCliente() throws SQLException {
        List<PagoPorCliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM VistaPagosPorCliente";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                PagoPorCliente reporte = new PagoPorCliente(
                    rs.getString(1),
                    rs.getDouble(2)
                );
                lista.add(reporte);
            }
        }
        return lista;
    }

    // --- 1. SERVICIOS CONTRATADOS POR TIPO DE EVENTO ---
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

    // --- 2. EVENTOS CON COSTO TOTAL > $3000 ---
    public List<Object[]> obtenerEventosCostosos() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT Evento.E_ID, Evento.E_Tipo, SUM(Contratados.CON_Precio) AS CostoTotalServicios " +
                     "FROM Evento JOIN Contratados ON Evento.E_ID = Contratados.Evento_E_ID " +
                     "GROUP BY Evento.E_ID, Evento.E_Tipo HAVING SUM(Contratados.CON_Precio) > 3000";
        try (Connection con = Util.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getDouble(3)});
            }
        }
        return lista;
    }

    // --- 3. CLIENTES CON GASTO TOTAL > $10000 ---
    public List<Object[]> obtenerClientesTop() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT Cliente_C_ID, SUM(E_CostoFinal) AS CostoTotal " +
                     "FROM Evento GROUP BY Cliente_C_ID HAVING SUM(E_CostoFinal) > 10000";
        try (Connection con = Util.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt(1), rs.getDouble(2)});
            }
        }
        return lista;
    }
}