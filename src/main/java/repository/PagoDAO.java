package repository;

import model.Pago;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    // inserta un nuevo pago en la BD
    // la fecha se asigna automáticamente con LocalDate.now()
    public void insertar(Pago p) throws SQLException {
        String sql = "INSERT INTO Pago (P_MontoPagado, Reserva_R_ID, P_Pagador, P_MetodoPago, P_FechaPago) VALUES (?,?,?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, p.getMontoPagado());
            ps.setInt(2, p.getReservaId());
            ps.setString(3, p.getPagador());
            ps.setString(4, p.getMetodoPago());
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
        }
    }

    // trae todos los pagos de la BD
    public List<Pago> listar() throws SQLException {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // busca un pago por su ID
    public Pago buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Pago WHERE P_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // actualiza los datos de un pago existente (NO modifica la fecha original)
    public void actualizar(Pago p) throws SQLException {
        String sql = "UPDATE Pago SET P_MontoPagado=?, Reserva_R_ID=?, P_Pagador=?, P_MetodoPago=? WHERE P_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, p.getMontoPagado());
            ps.setInt(2, p.getReservaId());
            ps.setString(3, p.getPagador());
            ps.setString(4, p.getMetodoPago());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        }
    }

    // borra un pago de la BD
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Pago WHERE P_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // convierte una fila de la BD en un objeto Pago
    private Pago mapear(ResultSet rs) throws SQLException {
        return new Pago(
            rs.getInt("P_ID"),
            rs.getDouble("P_MontoPagado"),
            rs.getInt("Reserva_R_ID"),
            rs.getString("P_Pagador"),
            rs.getString("P_MetodoPago"),
            rs.getDate("P_FechaPago") != null ? rs.getDate("P_FechaPago").toLocalDate() : null
        );
    }
}
