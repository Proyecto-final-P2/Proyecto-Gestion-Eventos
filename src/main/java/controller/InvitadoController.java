package controller;

import model.Invitado;
import repository.InvitadoDAO;
import javax.swing.JOptionPane;
import java.util.List;

public class InvitadoController {
    private final InvitadoDAO dao = new InvitadoDAO();

    // inserta un nuevo invitado y lo vincula al evento
    public boolean agregar(Invitado i) {
        try {
            dao.insertar(i);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar invitado: " + ex.getMessage());
            return false;
        }
    }

    // lista todos los invitados
    public List<Invitado> listar() {
        try {
            return dao.listar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar invitados: " + ex.getMessage());
            return List.of();
        }
    }

    // lista invitados filtrados por evento (usando JOIN con Asiste)
    public List<Invitado> listarPorEvento(int eventoId) {
        try {
            return dao.listarPorEvento(eventoId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar invitados del evento: " + ex.getMessage());
            return List.of();
        }
    }

    // busca invitados por DNI en un evento específico
    public List<Invitado> buscarPorDni(String dni, int eventoId) {
        try {
            return dao.buscarPorDni(dni, eventoId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar invitados: " + ex.getMessage());
            return List.of();
        }
    }

    // lista invitados de un cliente específico (método existente)
    public List<Invitado> listarPorCliente(int clienteId) {
        try {
            return dao.listarPorCliente(clienteId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar invitados del cliente: " + ex.getMessage());
            return List.of();
        }
    }

    // actualiza los datos de un invitado existente
    public boolean actualizar(Invitado i) {
        try {
            dao.actualizar(i);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar invitado: " + ex.getMessage());
            return false;
        }
    }

    // elimina un invitado por su ID (borra también de Asiste en el DAO)
    public boolean eliminar(int id) {
        try {
            dao.eliminar(id);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar invitado: " + ex.getMessage());
            return false;
        }
    }
}
