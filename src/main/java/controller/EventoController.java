package controller;

import model.Evento;
import repository.EventoDAO;
import javax.swing.JOptionPane;
import java.util.List;

// controlador, maneja la lógica de los eventos
public class EventoController {

    private final EventoDAO dao = new EventoDAO();

    // pide al dao que traiga todos los eventos
    public List<Evento> listar() {
        try {
            return dao.listar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar eventos: " + ex.getMessage());
            return List.of();
        }
    }

    public List<Evento> listarPorCliente(int clienteId) {
        try {
            return dao.listarPorCliente(clienteId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar eventos del cliente: " + ex.getMessage());
            return List.of();
        }
    }

    // busca eventos que coincidan con un tipo
    public List<Evento> buscar(String tipo) {
        try {
            return dao.buscarPorTipo(tipo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar eventos: " + ex.getMessage());
            return List.of();
        }
    }

    // guarda un nuevo evento en la bd y sus servicios
    public boolean agregar(Evento e, List<model.Servicio> servicios) {
        try {
            int id = dao.insertar(e);
            if (id > 0) {
                dao.actualizarServiciosContratados(id, servicios);
                return true;
            }
            return false;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar evento: " + ex.getMessage());
            return false;
        }
    }

    // actualiza los datos de un evento y sus servicios
    public boolean actualizar(Evento e, List<model.Servicio> servicios) {
        try {
            dao.actualizar(e);
            dao.actualizarServiciosContratados(e.getId(), servicios);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar evento: " + ex.getMessage());
            return false;
        }
    }

    // busca un evento por su ID
    public Evento buscarPorId(int id) {
        try {
            return dao.buscarPorId(id);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar evento: " + ex.getMessage());
            return null;
        }
    }

    // borra un evento
    public boolean eliminar(int id) {
        try {
            dao.eliminar(id);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar evento: " + ex.getMessage());
            return false;
        }
    }

    // verifica si hay superposición
    public boolean existeSuperposicion(int salonId, java.time.LocalDate fecha, java.time.LocalTime hInicio, java.time.LocalTime hFin, int eventoIdIgnorar) {
        try {
            return dao.existeSuperposicion(salonId, fecha, hInicio, hFin, eventoIdIgnorar);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al verificar superposición: " + ex.getMessage());
            return true; // En caso de error, bloqueamos por seguridad
        }
    }

    public List<String> verificarDisponibilidadServicios(List<model.Servicio> servicios, java.time.LocalDate fecha, java.time.LocalTime hInicio, java.time.LocalTime hFin, int eventoIdIgnorar) {
        try {
            return dao.verificarDisponibilidadServicios(servicios, fecha, hInicio, hFin, eventoIdIgnorar);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al verificar disponibilidad de servicios: " + ex.getMessage());
            return List.of();
        }
    }
}
