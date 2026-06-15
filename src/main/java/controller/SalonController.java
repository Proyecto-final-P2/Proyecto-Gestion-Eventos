package controller;

import model.Salon;
import repository.SalonDAO;
import javax.swing.JOptionPane;
import java.util.List;

// controlador, maneja la lógica de los salones
public class SalonController {

    private final SalonDAO dao = new SalonDAO();

    // pide al dao que traiga todos los salones
    public List<Salon> listar() {
        try {
            return dao.listar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al listar salones: " + ex.getMessage());
            return List.of();
        }
    }

    // busca salones que coincidan con un nombre
    public List<Salon> buscar(String nombre) {
        try {
            return dao.buscarPorNombre(nombre);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar salones: " + ex.getMessage());
            return List.of();
        }
    }

    // guarda un nuevo salón en la bd
    public boolean agregar(Salon s) {
        try {
            dao.insertar(s);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar salón: " + ex.getMessage());
            return false;
        }
    }

    // actualiza los datos de un salón que ya existe
    public boolean actualizar(Salon s) {
        try {
            dao.actualizar(s);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar salón: " + ex.getMessage());
            return false;
        }
    }

    // borra un salón
    public boolean eliminar(int id) {
        try {
            dao.eliminar(id);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar salón: " + ex.getMessage());
            return false;
        }
    }
}
