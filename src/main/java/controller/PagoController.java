package controller;

import model.Pago;
import repository.PagoDAO;
import javax.swing.JOptionPane;
import java.util.List;

public class PagoController {
    private final PagoDAO dao = new PagoDAO();

    public List<Pago> listarPorCliente(int clienteId) {
        try {
            return dao.listarPorCliente(clienteId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar pagos del cliente: " + ex.getMessage());
            return List.of();
        }
    }
}
