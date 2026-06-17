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

    public List<Pago> listarPorCliente(int clienteId) {
        try {
            return dao.listarPorCliente(clienteId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar pagos del cliente: " + ex.getMessage());
            return List.of();
        }
    }
<<<<<<< Updated upstream
=======

    public List<Pago> listarPorReserva(int reservaId) {
        try {
            return dao.listarPorReserva(reservaId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar pagos de la reserva: " + ex.getMessage());
            return List.of();
        }
    }

    public List<Object[]> listarTodos() {
        try {
            return dao.listarTodos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar pagos: " + ex.getMessage());
            return List.of();
        }
    }
>>>>>>> Stashed changes
}
