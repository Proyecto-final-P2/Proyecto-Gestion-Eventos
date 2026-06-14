package repository;

import model.Administrador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO {

    public void insertar(Administrador a) throws SQLException {
        String sql = "INSERT INTO Administrador (A_NombreApellido, A_Email, A_Password) VALUES (?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNombreApellido());
            ps.setString(2, a.getEmail());
            ps.setString(3, a.getPassword());
            ps.executeUpdate();
        }
    }

    public List<Administrador> listar() throws SQLException {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT * FROM Administrador";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Administrador> buscarPorNombre(String nombre) throws SQLException {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT * FROM Administrador WHERE A_NombreApellido LIKE ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Administrador buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Administrador WHERE A_Email = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public void actualizar(Administrador a) throws SQLException {
        String sql = "UPDATE Administrador SET A_NombreApellido=?, A_Email=?, A_Password=? WHERE A_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNombreApellido());
            ps.setString(2, a.getEmail());
            ps.setString(3, a.getPassword());
            ps.setInt(4, a.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Administrador WHERE A_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Administrador mapear(ResultSet rs) throws SQLException {
        return new Administrador(
            rs.getInt("A_ID"),
            rs.getString("A_NombreApellido"),
            rs.getString("A_Email"),
            rs.getString("A_Password")
        );
    }
}
