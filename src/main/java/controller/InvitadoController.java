package controller;

import model.Invitado;
import repository.InvitadoDAO;
import javax.swing.JOptionPane;
import java.util.List;

public class InvitadoController {
    private final InvitadoDAO dao = new InvitadoDAO();

    public List<Invitado> listarPorCliente(int clienteId) {
        try {
            return dao.listarPorCliente(clienteId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar invitados del cliente: " + ex.getMessage());
            return List.of();
        }
    }
}
