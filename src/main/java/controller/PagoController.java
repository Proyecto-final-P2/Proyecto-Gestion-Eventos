package controller;

import model.Pago;
import repository.PagoDAO;
import javax.swing.JOptionPane;
import java.util.List;

public class PagoController {
    private final PagoDAO dao = new PagoDAO();

    // Guarda un nuevo pago en la BD
    public boolean agregar(Pago p) {
        try {
            dao.insertar(p);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el pago: " + ex.getMessage());
            return false;
        }
    }

    // Lista todos los pagos registrados
    public List<Pago> listar() {
        try {
            return dao.listar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar pagos: " + ex.getMessage());
            return List.of();
        }
    }

    // Actualiza un pago existente
    public boolean actualizar(Pago p) {
        try {
            dao.actualizar(p);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el pago: " + ex.getMessage());
            return false;
        }
    }

    // Elimina un pago por ID
    public boolean eliminar(int id) {
        try {
            dao.eliminar(id);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el pago: " + ex.getMessage());
            return false;
        }
    }

    // Busca un pago por su ID
    public Pago buscarPorId(int id) {
        try {
            return dao.buscarPorId(id);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar el pago por ID: " + ex.getMessage());
            return null;
        }
    }

    // Busca pagos que coincidan con el nombre del pagador
    public List<Pago> buscar(String pagador) {
        try {
            return dao.buscarPorPagador(pagador);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar pagos: " + ex.getMessage());
            return List.of();
        }
    }

    public List<Pago> listarPorCliente(int clienteId) {
        try {
            return dao.listarPorCliente(clienteId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar pagos del cliente: " + ex.getMessage());
            return List.of();
        }
    }
    public List<Pago> listarPorEvento(int eventoId) {
        try {
            return dao.listarPorEvento(eventoId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar pagos del evento: " + ex.getMessage());
            return List.of();
        }
    }
}
