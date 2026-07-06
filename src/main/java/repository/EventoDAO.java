package repository;

import model.Evento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    // inserta evento en BD
    public int insertar(Evento e) throws SQLException {
        String sql = "INSERT INTO Evento (E_Fecha, E_HoraInicio, E_HoraFin, E_Tipo, E_CantInvitados, E_Estado, Cliente_C_ID, Salon_SA_ID) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(e.getFecha()));
            ps.setTime(2, Time.valueOf(e.getHoraInicio()));
            ps.setTime(3, Time.valueOf(e.getHoraFin()));
            ps.setString(4, e.getTipo());
            ps.setInt(5, e.getCantInvitados());
            ps.setString(6, e.getEstado());
            ps.setInt(7, e.getClienteId());
            ps.setInt(8, e.getSalonId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    // trae todos los eventos (usa la Vista para traer los saldos calculados)
    public List<Evento> listar() throws SQLException {
        List<Evento> lista = new ArrayList<>();
        // En vez de SELECT * FROM Evento, cruzamos con VistaDetallesEvento si es necesario, 
        // pero mapear() ahora usará un query inteligente
        String sql = "SELECT e.*, v.CostoTotal, v.SaldoPendiente FROM Evento e LEFT JOIN VistaDetallesEvento v ON e.E_ID = v.EventoID ORDER BY e.E_Fecha DESC, e.E_HoraInicio DESC";
        try (Connection con = Util.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearConSaldos(rs));
        }
        return lista;
    }

    // trae eventos de un cliente específico
    public List<Evento> listarPorCliente(int clienteId) throws SQLException {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT e.*, v.CostoTotal, v.SaldoPendiente FROM Evento e LEFT JOIN VistaDetallesEvento v ON e.E_ID = v.EventoID WHERE e.Cliente_C_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearConSaldos(rs));
            }
        }
        return lista;
    }

    // trae eventos por estado
    public List<Evento> listarPorEstado(String estado) throws SQLException {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT e.*, v.CostoTotal, v.SaldoPendiente FROM Evento e LEFT JOIN VistaDetallesEvento v ON e.E_ID = v.EventoID WHERE e.E_Estado = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearConSaldos(rs));
            }
        }
        return lista;
    }

    // actualiza evento en BD
    public void actualizar(Evento e) throws SQLException {
        String sql = "UPDATE Evento SET E_Fecha=?, E_HoraInicio=?, E_HoraFin=?, E_Tipo=?, E_CantInvitados=?, E_Estado=? WHERE E_ID=?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(e.getFecha()));
            ps.setTime(2, Time.valueOf(e.getHoraInicio()));
            ps.setTime(3, Time.valueOf(e.getHoraFin()));
            ps.setString(4, e.getTipo());
            ps.setInt(5, e.getCantInvitados());
            ps.setString(6, e.getEstado());
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

    // busca un evento por su ID exacto en la BD
    public Evento buscarPorId(int id) throws SQLException {
        String sql = "SELECT e.*, v.CostoTotal, v.SaldoPendiente FROM Evento e LEFT JOIN VistaDetallesEvento v ON e.E_ID = v.EventoID WHERE e.E_ID = ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearConSaldos(rs);
            }
        }
        return null;
    }

    // busca eventos por su tipo (coincidencia parcial)
    public List<Evento> buscarPorTipo(String tipo) throws SQLException {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT e.*, v.CostoTotal, v.SaldoPendiente FROM Evento e LEFT JOIN VistaDetallesEvento v ON e.E_ID = v.EventoID WHERE e.E_Tipo LIKE ?";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tipo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearConSaldos(rs));
            }
        }
        return lista;
    }

    // convierte una fila de la BD en un objeto Evento
    private Evento mapearConSaldos(ResultSet rs) throws SQLException {
        Evento e = new Evento();
        e.setId(rs.getInt("E_ID"));
        e.setFecha(rs.getDate("E_Fecha").toLocalDate());
        e.setHoraInicio(rs.getTime("E_HoraInicio").toLocalTime());
        e.setHoraFin(rs.getTime("E_HoraFin").toLocalTime());
        e.setTipo(rs.getString("E_Tipo"));
        e.setCantInvitados(rs.getInt("E_CantInvitados"));
        e.setEstado(rs.getString("E_Estado"));
        e.setClienteId(rs.getInt("Cliente_C_ID"));
        e.setSalonId(rs.getInt("Salon_SA_ID"));
        e.setCostoTotal(rs.getDouble("CostoTotal"));
        e.setSaldoPendiente(rs.getDouble("SaldoPendiente"));
        return e;
    }

    // Verifica si ya existe un evento en el mismo salón y fecha que se superponga
    public boolean existeSuperposicion(int salonId, java.time.LocalDate fecha, java.time.LocalTime hInicio, java.time.LocalTime hFin, int eventoIdIgnorar) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Evento WHERE Salon_SA_ID = ? AND E_Fecha = ? AND E_ID != ? AND " +
                     "((E_HoraInicio < ? AND E_HoraFin > ?) OR (E_HoraInicio >= ? AND E_HoraInicio < ?))";
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, salonId);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setInt(3, eventoIdIgnorar);
            
            ps.setTime(4, Time.valueOf(hFin));
            ps.setTime(5, Time.valueOf(hInicio));
            
            ps.setTime(6, Time.valueOf(hInicio));
            ps.setTime(7, Time.valueOf(hFin));
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Actualiza los servicios contratados de un evento
    public void actualizarServiciosContratados(int eventoId, List<model.Servicio> servicios) throws SQLException {
        try (Connection con = Util.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM Contratados WHERE Evento_E_ID = ?")) {
                ps.setInt(1, eventoId);
                ps.executeUpdate();
            }
            if (servicios != null && !servicios.isEmpty()) {
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO Contratados (Evento_E_ID, Servicios_SE_ID, CON_Precio) VALUES (?, ?, ?)")) {
                    for (model.Servicio s : servicios) {
                        ps.setInt(1, eventoId);
                        ps.setInt(2, s.getId());
                        ps.setDouble(3, s.getCosto());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
        }
    }

    // Verifica si algún servicio seleccionado ya está ocupado en la misma fecha y rango horario
    public List<String> verificarDisponibilidadServicios(List<model.Servicio> servicios, java.time.LocalDate fecha, java.time.LocalTime hInicio, java.time.LocalTime hFin, int eventoIdIgnorar) throws SQLException {
        List<String> ocupados = new ArrayList<>();
        if (servicios == null || servicios.isEmpty()) return ocupados;
        
        String sql = "SELECT s.SE_Tipo, s.SE_Proveedor FROM Contratados c " +
                     "JOIN Evento e ON c.Evento_E_ID = e.E_ID " +
                     "JOIN Servicios s ON c.Servicios_SE_ID = s.SE_ID " +
                     "WHERE c.Servicios_SE_ID = ? AND e.E_Fecha = ? AND e.E_ID != ? AND " +
                     "((e.E_HoraInicio < ? AND e.E_HoraFin > ?) OR (e.E_HoraInicio >= ? AND e.E_HoraInicio < ?))";
                     
        try (Connection con = Util.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (model.Servicio s : servicios) {
                ps.setInt(1, s.getId());
                ps.setDate(2, Date.valueOf(fecha));
                ps.setInt(3, eventoIdIgnorar);
                ps.setTime(4, Time.valueOf(hFin));
                ps.setTime(5, Time.valueOf(hInicio));
                ps.setTime(6, Time.valueOf(hInicio));
                ps.setTime(7, Time.valueOf(hFin));
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ocupados.add(rs.getString("SE_Tipo") + " - " + rs.getString("SE_Proveedor"));
                    }
                }
            }
        }
        return ocupados;
    }
}
