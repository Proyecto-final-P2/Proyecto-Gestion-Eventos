package repository;

import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // inserta cliente en BD
    public void insertar(Cliente c) throws SQLException {
        String sql = "INSERT INTO Cliente (C_DNI, C_NombreApellido, C_Email, C_Telefono) VALUES (?,?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getDni());
            ps.setString(2, c.getNombreApellido());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefono());
            ps.executeUpdate();
        }
    }

    // trae todos los clientes de la BD
    public List<Cliente> listar() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // busca un cliente por su ID exacto
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE C_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // busca clientes que contengan ese texto en su nombre
    public List<Cliente> buscarPorNombre(String nombre) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente WHERE C_NombreApellido LIKE ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // busca un cliente por su email exacto
    public Cliente buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE C_Email = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // busca un cliente por su dni exacto
    public Cliente buscarPorDni(int dni) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE C_DNI = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // actualiza los datos de un cliente existente en la BD
    public void actualizar(Cliente c) throws SQLException {
        String sql = "UPDATE Cliente SET C_DNI=?, C_NombreApellido=?, C_Email=?, C_Telefono=? WHERE C_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getDni());
            ps.setString(2, c.getNombreApellido());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefono());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        }
    }

    // borra un cliente de la BD
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE C_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // convierte una fila de la BD en un objeto Cliente que Java pueda entender
    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("C_ID"),
            rs.getInt("C_DNI"),
            rs.getString("C_NombreApellido"),
            rs.getString("C_Email"),
            rs.getString("C_Telefono")
        );
    }
}
