package repository;

import model.Servicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {

// inserta servicio en la BD (SIN EL ID)
    public void insertar(Servicio s) throws SQLException {
        String sql = "INSERT INTO Servicios (SE_Tipo, SE_Proveedor, SE_Costo) VALUES (?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getTipo());
            ps.setString(2, s.getProveedor());
            ps.setDouble(3, s.getCosto());
            ps.executeUpdate();
        }
    }
    // lista servicios de la BD
    public List<Servicio> listar() throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM Servicios";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // busca servicios que contengan ese texto en el proveedor
    public List<Servicio> buscarPorProveedor(String proveedor) throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM Servicios WHERE SE_Proveedor LIKE ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + proveedor + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // busca los IDs de los servicios contratados para un evento
    public List<Integer> obtenerIdsServiciosPorEvento(int eventoId) throws SQLException {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT Servicios_SE_ID FROM Contratados WHERE Evento_E_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(rs.getInt(1));
            }
        }
        return lista;
    }

    // actualiza servicio en la BD
    public void actualizar(Servicio s) throws SQLException {
        String sql = "UPDATE Servicios SET SE_Tipo=?, SE_Proveedor=?, SE_Costo=? WHERE SE_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getTipo());
            ps.setString(2, s.getProveedor());
            ps.setDouble(3, s.getCosto());
            ps.setInt(4, s.getId());
            ps.executeUpdate();
        }
    }

    // elimina servicio de la BD
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Servicios WHERE SE_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // convierte fila de la BD en objeto Servicio
    private Servicio mapear(ResultSet rs) throws SQLException {
        return new Servicio(
            rs.getInt("SE_ID"),
            rs.getString("SE_Tipo"),
            rs.getString("SE_Proveedor"),
            rs.getDouble("SE_Costo")
        );
    }
}