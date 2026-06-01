package repository;

import model.Invitado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvitadoDAO {

    public void insertar(Invitado inv) throws SQLException {
        String sql = "INSERT INTO Invitado (IN_DNI, IN_NombreApellido, IN_Email, IN_Telefono, IN_Asistencia, IN_PreferenciaMenu) VALUES (?,?,?,?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, inv.getDni());
            ps.setString(2, inv.getNombreApellido());
            ps.setString(3, inv.getEmail());
            ps.setString(4, inv.getTelefono());
            ps.setString(5, inv.getAsistencia());
            ps.setString(6, inv.getPreferenciaMenu());
            ps.executeUpdate();
        }
    }

    public List<Invitado> listarPorEvento(int eventoId) throws SQLException {
        List<Invitado> lista = new ArrayList<>();
        String sql = "SELECT i.* FROM Invitado i JOIN Asiste a ON i.IN_ID = a.Invitado_IN_ID WHERE a.Evento_E_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void actualizar(Invitado inv) throws SQLException {
        String sql = "UPDATE Invitado SET IN_Asistencia=?, IN_PreferenciaMenu=? WHERE IN_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, inv.getAsistencia());
            ps.setString(2, inv.getPreferenciaMenu());
            ps.setInt(3, inv.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Invitado WHERE IN_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Invitado mapear(ResultSet rs) throws SQLException {
        return new Invitado(
            rs.getInt("IN_ID"),
            rs.getInt("IN_DNI"),
            rs.getString("IN_NombreApellido"),
            rs.getString("IN_Email"),
            rs.getString("IN_Telefono"),
            rs.getString("IN_Asistencia"),
            rs.getString("IN_PreferenciaMenu")
        );
    }
}
