package repository;

import model.Pago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    // registra el pago en la BD
    public String registrarPago(int reservaId, double monto) throws SQLException {
        String sql = "{CALL RegistrarPagoParaReserva(?,?,?)}";
        try (Connection con = Util.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, reservaId);
            cs.setDouble(2, monto);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3);
        }
    }

    // lista pagos de una reserva específica
    public List<Pago> listarPorReserva(int reservaId) throws SQLException {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago WHERE Reserva_R_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reservaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // convierte una fila de la BD en un objeto Pago
    private Pago mapear(ResultSet rs) throws SQLException {
        return new Pago(
            rs.getInt("P_ID"),
            rs.getDouble("P_MontoPagado"),
            rs.getInt("Reserva_R_ID")
        );
    }
}
