package repository;

import model.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    // registrar reserva en BD
    public String crearReserva(int id, java.time.LocalDate fecha,
                                java.time.LocalTime inicio, java.time.LocalTime fin,
                                double monto) throws SQLException {
        String sql = "{CALL CrearReservaConelID(?,?,?,?,?,?)}";
        try (Connection con = Util.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, id);
            cs.setDate(2, Date.valueOf(fecha));
            cs.setTime(3, Time.valueOf(inicio));
            cs.setTime(4, Time.valueOf(fin));
            cs.setDouble(5, monto);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.execute();
            return cs.getString(6); // "Reserva creada exitosamente." o error
        }
    }

    // lista todas las reservas de la BD
    public List<Reserva> listar() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reserva";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // elimina reserva de la BD
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Reserva WHERE R_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

<<<<<<< Updated upstream
    // convierte fila de la BD en objeto Reserva
=======
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
>>>>>>> Stashed changes
    private Reserva mapear(ResultSet rs) throws SQLException {
        return new Reserva(
            rs.getInt("R_ID"),
            rs.getDate("R_Fecha").toLocalDate(),
            rs.getTime("R_HoraInicio").toLocalTime(),
            rs.getTime("R_HoraFin").toLocalTime(),
            rs.getDouble("R_Monto")
        );
    }
}
