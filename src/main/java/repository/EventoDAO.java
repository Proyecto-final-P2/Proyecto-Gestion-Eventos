package repository;

import model.Evento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    // inserta evento en BD
    public void insertar(Evento e) throws SQLException {
        String sql = "INSERT INTO Evento (E_Fecha, E_Horario, E_Tipo, E_CantInvitados, E_Estado, E_CostoFinal, Cliente_C_ID, Salon_SA_ID) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(e.getFecha()));
            ps.setTime(2, Time.valueOf(e.getHorario()));
            ps.setString(3, e.getTipo());
            ps.setInt(4, e.getCantInvitados());
            ps.setString(5, e.getEstado());
            ps.setDouble(6, e.getCostoFinal());
            ps.setInt(7, e.getClienteId());
            ps.setInt(8, e.getSalonId());
            ps.executeUpdate();
        }
    }

    // trae todos los eventos de la BD
    public List<Evento> listar() throws SQLException {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Evento";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // trae eventos de un cliente específico
    public List<Evento> listarPorCliente(int clienteId) throws SQLException {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Evento WHERE Cliente_C_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // trae eventos por estado
    public List<Evento> listarPorEstado(String estado) throws SQLException {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Evento WHERE E_Estado = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // actualiza evento en BD
    public void actualizar(Evento e) throws SQLException {
        String sql = "UPDATE Evento SET E_Fecha=?, E_Horario=?, E_Tipo=?, E_CantInvitados=?, E_Estado=?, E_CostoFinal=? WHERE E_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(e.getFecha()));
            ps.setTime(2, Time.valueOf(e.getHorario()));
            ps.setString(3, e.getTipo());
            ps.setInt(4, e.getCantInvitados());
            ps.setString(5, e.getEstado());
            ps.setDouble(6, e.getCostoFinal());
            ps.setInt(7, e.getId());
            ps.executeUpdate();
        }
    }

    // elimina evento de la BD
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Evento WHERE E_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // convierte una fila de la BD en un objeto Evento
    private Evento mapear(ResultSet rs) throws SQLException {
        Evento e = new Evento();
        e.setId(rs.getInt("E_ID"));
        e.setFecha(rs.getDate("E_Fecha").toLocalDate());
        e.setHorario(rs.getTime("E_Horario").toLocalTime());
        e.setTipo(rs.getString("E_Tipo"));
        e.setCantInvitados(rs.getInt("E_CantInvitados"));
        e.setEstado(rs.getString("E_Estado"));
        e.setCostoFinal(rs.getDouble("E_CostoFinal"));
        e.setClienteId(rs.getInt("Cliente_C_ID"));
        e.setSalonId(rs.getInt("Salon_SA_ID"));
        return e;
    }
}
