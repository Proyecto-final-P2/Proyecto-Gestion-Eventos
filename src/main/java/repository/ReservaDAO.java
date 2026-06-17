package repository;

import model.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para acceso a datos de la tabla Reserva.
 */
public class ReservaDAO {

    // Lista todas las reservas mapeadas a objetos Reserva
    public List<Reserva> listar() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reserva";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // Inserta una nueva reserva en la base de datos (R_ID autoincremental no se envía)
    public void insertar(Reserva r) throws SQLException {
        String sql = "INSERT INTO Reserva (R_Fecha, R_HoraInicio, R_HoraFin, R_Monto) VALUES (?, ?, ?, ?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(r.getR_Fecha()));
            ps.setTime(2, Time.valueOf(r.getR_HoraInicio()));
            ps.setTime(3, Time.valueOf(r.getR_HoraFin()));
            ps.setDouble(4, r.getR_Monto());
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

    // Mapea una fila de ResultSet a un objeto Reserva
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
