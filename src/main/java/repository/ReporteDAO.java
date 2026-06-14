package repository;

import model.PagoPorCliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    public List<PagoPorCliente> obtenerPagosPorCliente() throws SQLException {
        List<PagoPorCliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM VistaPagosPorCliente";
        
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                // Columna 1: Cliente, Columna 2: Total Pagado
                PagoPorCliente reporte = new PagoPorCliente(
                    rs.getString(1),
                    rs.getDouble(2)
                );
                lista.add(reporte);
            }
        }
        return lista;
    }
}