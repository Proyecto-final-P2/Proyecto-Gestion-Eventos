package controller;

import model.Reserva;
import repository.ReservaDAO;
import javax.swing.JOptionPane;
import java.util.List;

public class ReservaController {
    private final ReservaDAO dao = new ReservaDAO();

    // Trae todas las reservas de la BD
    public List<Reserva> listar() {
        try {
            return dao.listar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar reservas: " + ex.getMessage());
            return List.of();
        }
    }

    // Busca una reserva por su ID exacto
    public Reserva buscarPorId(int id) {
        try {
            return dao.buscarPorId(id);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar reserva por ID: " + ex.getMessage());
            return null;
        }
    }
}

