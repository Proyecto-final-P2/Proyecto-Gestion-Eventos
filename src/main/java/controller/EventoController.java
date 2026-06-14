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

    // guarda un nuevo evento en la bd
    public boolean agregar(Evento e) {
        try {
            dao.insertar(e);
            JOptionPane.showMessageDialog(null, "Evento agregado.");
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar evento: " + ex.getMessage());
            return false;
        }
    }

    // actualiza los datos de un evento que ya existe
    public boolean actualizar(Evento e) {
        try {
            dao.actualizar(e);
            JOptionPane.showMessageDialog(null, "Evento actualizado.");
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar evento: " + ex.getMessage());
            return false;
        }
    }

    // borra un evento, pero antes pregunta si estás seguro
    public boolean eliminar(int id) {
        int ok = JOptionPane.showConfirmDialog(null, "¿Eliminar este evento?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return false;
        
        try {
            dao.eliminar(id);
            JOptionPane.showMessageDialog(null, "Evento eliminado.");
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar evento: " + ex.getMessage());
            return false;
        }
    }
}
