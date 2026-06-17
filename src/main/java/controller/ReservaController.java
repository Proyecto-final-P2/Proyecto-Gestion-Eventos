package controller;

import model.Reserva;
import repository.ReservaDAO;
import javax.swing.JOptionPane;
import java.util.List;

// controlador que maneja la lógica de negocio de Reservas
public class ReservaController {

    // se conecta con el DAO que habla con la BD
    private final ReservaDAO dao = new ReservaDAO();

    // pide al DAO que traiga todas las reservas
    public List<Reserva> listar() {
        try { return dao.listar(); }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error al listar reservas: " + ex.getMessage()); return List.of(); }
    }
}
