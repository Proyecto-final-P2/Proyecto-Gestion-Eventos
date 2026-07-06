package repository;

import model.Pago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    // inserta un pago en la BD
    public void insertar(Pago p) throws SQLException {
        String sql = "INSERT INTO Pago (P_MontoPagado, Evento_E_ID, P_Pagador, P_MetodoPago, P_FechaPago) VALUES (?,?,?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, p.getMontoPagado());
            ps.setInt(2, p.getEventoId());
            ps.setString(3, p.getPagador());
            ps.setString(4, p.getMetodoPago());
            ps.setDate(5, java.sql.Date.valueOf(p.getFechaPago() != null ? p.getFechaPago() : java.time.LocalDate.now()));
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

    // busca un pago por su ID exacto
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

    // busca pagos que contengan ese texto en el nombre del pagador
    public List<Pago> buscarPorPagador(String pagador) throws SQLException {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago WHERE P_Pagador LIKE ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + pagador + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // actualiza los datos de un pago existente
    public void actualizar(Pago p) throws SQLException {
        String sql = "UPDATE Pago SET P_MontoPagado=?, Evento_E_ID=?, P_Pagador=?, P_MetodoPago=?, P_FechaPago=? WHERE P_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, p.getMontoPagado());
            ps.setInt(2, p.getEventoId());
            ps.setString(3, p.getPagador());
            ps.setString(4, p.getMetodoPago());
            ps.setDate(5, java.sql.Date.valueOf(p.getFechaPago() != null ? p.getFechaPago() : java.time.LocalDate.now()));
            ps.setInt(6, p.getId());
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

    // registra el pago en la BD
    public String registrarPago(int eventoId, double monto) throws SQLException {
        String sql = "{CALL RegistrarPagoParaEvento(?,?,?)}";
        try (Connection con = Util.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, eventoId);
            cs.setDouble(2, monto);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3);
        }
    }

    // lista pagos de un evento específico
    public List<Pago> listarPorEvento(int eventoId) throws SQLException {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago WHERE Evento_E_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // lista pagos de un cliente específico
    public List<Pago> listarPorCliente(int clienteId) throws SQLException {
        List<Pago> lista = new ArrayList<>();
        String sql =
            "SELECT p.* FROM Pago p " +
            "JOIN Evento e ON p.Evento_E_ID = e.E_ID " +
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

    // convierte una fila de la BD en un objeto Pago
    private Pago mapear(ResultSet rs) throws SQLException {
        java.sql.Date sqlDate = rs.getDate("P_FechaPago");
        java.time.LocalDate localDate = sqlDate != null ? sqlDate.toLocalDate() : java.time.LocalDate.now();
        return new Pago(
            rs.getInt("P_ID"),
            rs.getDouble("P_MontoPagado"),
            rs.getInt("Evento_E_ID"),
            rs.getString("P_Pagador"),
            rs.getString("P_MetodoPago"),
            localDate
        );
    }
}
