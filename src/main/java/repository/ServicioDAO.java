package repository;

import model.Servicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {

    public void insertar(Servicio s) throws SQLException {
        String sql = "INSERT INTO Servicios (SE_Tipo, SE_Proveedor, SE_Costo, SE_Cantidad, SE_Estado) VALUES (?,?,?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getTipo());
            ps.setString(2, s.getProveedor());
            ps.setDouble(3, s.getCosto());
            ps.setInt(4, s.getCantidad());
            ps.setString(5, s.getEstado());
            ps.executeUpdate();
        }
    }

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

    public void actualizar(Servicio s) throws SQLException {
        String sql = "UPDATE Servicios SET SE_Tipo=?, SE_Proveedor=?, SE_Costo=?, SE_Cantidad=?, SE_Estado=? WHERE SE_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getTipo());
            ps.setString(2, s.getProveedor());
            ps.setDouble(3, s.getCosto());
            ps.setInt(4, s.getCantidad());
            ps.setString(5, s.getEstado());
            ps.setInt(6, s.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Servicios WHERE SE_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Servicio mapear(ResultSet rs) throws SQLException {
        return new Servicio(
            rs.getInt("SE_ID"),
            rs.getString("SE_Tipo"),
            rs.getString("SE_Proveedor"),
            rs.getDouble("SE_Costo"),
            rs.getInt("SE_Cantidad"),
            rs.getString("SE_Estado")
        );
    }
}
