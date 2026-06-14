package repository;

import model.Invitado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvitadoDAO {

    // inserta invitado en BD y lo vincula al evento en la tabla Asiste
    public void insertar(Invitado inv) throws SQLException {
        String sqlInvitado = "INSERT INTO Invitado (IN_DNI, IN_NombreApellido, IN_Email, IN_Telefono, IN_Asistencia, IN_PreferenciaMenu) VALUES (?,?,?,?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlInvitado, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, inv.getDni());
            ps.setString(2, inv.getNombreApellido());
            ps.setString(3, inv.getEmail());
            ps.setString(4, inv.getTelefono());
            ps.setString(5, inv.getAsistencia());
            ps.setString(6, inv.getPreferenciaMenu());
            ps.executeUpdate();

            // obtiene el ID generado y lo inserta en Asiste
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int nuevoId = keys.getInt(1);
                    String sqlAsiste = "INSERT INTO Asiste (Invitado_IN_ID, Evento_E_ID) VALUES (?, ?)";
                    try (PreparedStatement psAsiste = con.prepareStatement(sqlAsiste)) {
                        psAsiste.setInt(1, nuevoId);
                        psAsiste.setInt(2, inv.getEventoId());
                        psAsiste.executeUpdate();
                    }
                }
            }
        }
    }

    // lista todos los invitados sin filtro
    public List<Invitado> listar() throws SQLException {
        List<Invitado> lista = new ArrayList<>();
        String sql = "SELECT * FROM Invitado";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // lista invitados de un evento específico usando JOIN con Asiste
    public List<Invitado> listarPorEvento(int eventoId) throws SQLException {
        List<Invitado> lista = new ArrayList<>();
        String sql = "SELECT i.* FROM Invitado i JOIN Asiste a ON i.IN_ID = a.Invitado_IN_ID WHERE a.Evento_E_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Invitado inv = mapear(rs);
                    inv.setEventoId(eventoId);
                    lista.add(inv);
                }
            }
        }
        return lista;
    }

    // lista invitados de un cliente específico (método existente, no se toca)
    public List<Invitado> listarPorCliente(int clienteId) throws SQLException {
        List<Invitado> lista = new ArrayList<>();
        String sql =
            "SELECT i.* FROM Invitado i " +
            "JOIN Asiste a ON i.IN_ID = a.Invitado_IN_ID " +
            "JOIN Evento e ON a.Evento_E_ID = e.E_ID " +
            "WHERE e.Cliente_C_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // actualiza todos los campos editables del invitado
    public void actualizar(Invitado inv) throws SQLException {
        String sql = "UPDATE Invitado SET IN_DNI=?, IN_NombreApellido=?, IN_Email=?, IN_Telefono=?, IN_Asistencia=?, IN_PreferenciaMenu=? WHERE IN_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, inv.getDni());
            ps.setString(2, inv.getNombreApellido());
            ps.setString(3, inv.getEmail());
            ps.setString(4, inv.getTelefono());
            ps.setString(5, inv.getAsistencia());
            ps.setString(6, inv.getPreferenciaMenu());
            ps.setInt(7, inv.getId());
            ps.executeUpdate();
        }
    }

    // elimina el invitado: primero borra de Asiste (FK) y luego de Invitado
    public void eliminar(int id) throws SQLException {
        try (Connection con = Util.getConnection()) {
            // 1. borrar de la tabla intermedia para respetar la FK
            String sqlAsiste = "DELETE FROM Asiste WHERE Invitado_IN_ID = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlAsiste)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            // 2. ahora sí borrar el invitado
            String sqlInvitado = "DELETE FROM Invitado WHERE IN_ID = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlInvitado)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }

    // convierte una fila de la BD en un objeto Invitado
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
