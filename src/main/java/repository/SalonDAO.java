package repository;

import model.Salon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalonDAO {

    // inserta salón en la BD
    public void insertar(Salon s) throws SQLException {
        String sql = "INSERT INTO Salon (SA_Direccion, SA_Nombre, SA_Capacidad, SA_CantSillas, SA_CantMesas, SA_Costo) VALUES (?,?,?,?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getDireccion());
            ps.setString(2, s.getNombre());
            ps.setInt(3, s.getCapacidad());
            ps.setInt(4, s.getCantSillas());
            ps.setInt(5, s.getCantMesas());
            ps.setDouble(6, s.getCosto());
            ps.executeUpdate();
        }
    }

    // lista salones de la BD
    public List<Salon> listar() throws SQLException {
        List<Salon> lista = new ArrayList<>();
        String sql = "SELECT * FROM Salon";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // busca salón por id en la BD
    public Salon buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Salon WHERE SA_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // actualiza salón en la BD
    public void actualizar(Salon s) throws SQLException {
        String sql = "UPDATE Salon SET SA_Direccion=?, SA_Nombre=?, SA_Capacidad=?, SA_CantSillas=?, SA_CantMesas=?, SA_Costo=? WHERE SA_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getDireccion());
            ps.setString(2, s.getNombre());
            ps.setInt(3, s.getCapacidad());
            ps.setInt(4, s.getCantSillas());
            ps.setInt(5, s.getCantMesas());
            ps.setDouble(6, s.getCosto());
            ps.setInt(7, s.getId());
            ps.executeUpdate();
        }
    }

    // elimina salón de la BD
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Salon WHERE SA_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // busca salones que contengan ese texto en su nombre
    public List<Salon> buscarPorNombre(String nombre) throws SQLException {
        List<Salon> lista = new ArrayList<>();
        String sql = "SELECT * FROM Salon WHERE SA_Nombre LIKE ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // convierte fila de la BD en objeto Salon
    private Salon mapear(ResultSet rs) throws SQLException {
        return new Salon(
            rs.getInt("SA_ID"),
            rs.getString("SA_Direccion"),
            rs.getString("SA_Nombre"),
            rs.getInt("SA_Capacidad"),
            rs.getInt("SA_CantSillas"),
            rs.getInt("SA_CantMesas"),
            rs.getDouble("SA_Costo")
        );
    }
}
