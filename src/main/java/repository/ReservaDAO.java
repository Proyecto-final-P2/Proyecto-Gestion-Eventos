package repository;

import model.Reserva;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para acceso a datos de la tabla Reserva.
 */
public class ReservaDAO {

    // Lista todas las reservas mapeadas a objetos Reserva, vinculando Cliente y Salon
    public List<Reserva> listar() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.*, c.C_NombreApellido, s.SA_Nombre " +
                     "FROM Reserva r " +
                     "JOIN Cliente c ON r.R_ClienteID = c.C_ID " +
                     "JOIN Salon s ON r.R_SalonID = s.SA_ID";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // Inserta una nueva reserva con relación a Cliente y Salon
    public void insertar(Reserva r) throws SQLException {
        String sql = "INSERT INTO Reserva (R_Fecha, R_HoraInicio, R_HoraFin, R_Monto, R_ClienteID, R_SalonID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(r.getR_Fecha()));
            ps.setTime(2, Time.valueOf(r.getR_HoraInicio()));
            ps.setTime(3, Time.valueOf(r.getR_HoraFin()));
            ps.setDouble(4, r.getR_Monto());
            ps.setInt(5, r.getR_ClienteID());
            ps.setInt(6, r.getR_SalonID());
            ps.executeUpdate();
        }
    }

    // Elimina una reserva por ID
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Reserva WHERE R_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Busca una reserva por ID
    public Reserva buscarPorId(int id) throws SQLException {
        String sql = "SELECT r.*, c.C_NombreApellido, s.SA_Nombre " +
                     "FROM Reserva r " +
                     "JOIN Cliente c ON r.R_ClienteID = c.C_ID " +
                     "JOIN Salon s ON r.R_SalonID = s.SA_ID " +
                     "WHERE r.R_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // Mapea una fila de ResultSet a un objeto Reserva completo
    private Reserva mapear(ResultSet rs) throws SQLException {
        return new Reserva(
            rs.getInt("R_ID"),
            rs.getDate("R_Fecha").toLocalDate(),
            rs.getTime("R_HoraInicio").toLocalTime(),
            rs.getTime("R_HoraFin").toLocalTime(),
            rs.getDouble("R_Monto"),
            rs.getInt("R_ClienteID"),
            rs.getInt("R_SalonID"),
            rs.getString("C_NombreApellido"),
            rs.getString("SA_Nombre")
        );
    }

    // Actualiza los datos de una reserva existente
    public void actualizar(Reserva r) throws SQLException {
        String sql = "UPDATE Reserva SET R_Fecha = ?, R_HoraInicio = ?, R_HoraFin = ?, R_Monto = ?, R_ClienteID = ?, R_SalonID = ? WHERE R_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(r.getR_Fecha()));
            ps.setTime(2, Time.valueOf(r.getR_HoraInicio()));
            ps.setTime(3, Time.valueOf(r.getR_HoraFin()));
            ps.setDouble(4, r.getR_Monto());
            ps.setInt(5, r.getR_ClienteID());
            ps.setInt(6, r.getR_SalonID());
            ps.setInt(7, r.getR_ID());
            ps.executeUpdate();
        }
    }

    // Verifica si existe superposición horaria para una reserva en un salón y fecha específicos
    public boolean existeSuperposicion(int salonId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int reservaIdIgnorar) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Reserva WHERE R_SalonID = ? AND R_Fecha = ? AND R_ID != ? AND R_HoraInicio < ? AND R_HoraFin > ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, salonId);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setInt(3, reservaIdIgnorar);
            ps.setTime(4, Time.valueOf(horaFin));
            ps.setTime(5, Time.valueOf(horaInicio));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
