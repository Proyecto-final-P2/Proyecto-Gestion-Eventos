package controller;

import model.Cliente;
import repository.ClienteDAO;
import javax.swing.JOptionPane;
import java.util.List;

// controlador, maneja la lógica de los clientes
public class ClienteController {

    // se conecta con el archivo que habla con la bd
    private final ClienteDAO dao = new ClienteDAO();

    // pide al dao que traiga todos los clientes
    public List<Cliente> listar() {
        try { return dao.listar(); }
        // si falla, muestra un cartel de error y devuelve una lista vacia
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return List.of(); }
    }

    // busca clientes que coincidan con un dni
    public List<Cliente> buscar(String dni) {
        try { return dao.buscarPorDni(dni); }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return List.of(); }
    }

    // guarda un nuevo cliente en la bd validando unicidad
    public boolean agregar(Cliente c) {
        try {
            if (dao.buscarPorDni(c.getDni()) != null) {
                JOptionPane.showMessageDialog(null, "Error: El DNI ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!c.getEmail().trim().isEmpty() && dao.buscarPorEmail(c.getEmail()) != null) {
                JOptionPane.showMessageDialog(null, "Error: El Email ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            dao.insertar(c);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos: " + ex.getMessage());
            return false;
        }
    }

    // actualiza los datos de un cliente que ya existe
    public boolean actualizar(Cliente c) {
        try { dao.actualizar(c); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return false; }
    }

    // borra un cliente
    public boolean eliminar(int id) {
        try { 
            dao.eliminar(id); 
            return true; 
        } catch (Exception ex) { 
            if (ex.getMessage().contains("foreign key constraint")) {
                JOptionPane.showMessageDialog(null, "No se puede eliminar este cliente porque tiene eventos asociados.\nPor favor, elimine primero los eventos de este cliente.", "Error al eliminar", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); 
            }
            return false; 
        }
    }
}
