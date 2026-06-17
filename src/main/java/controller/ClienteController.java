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

    // guarda un nuevo cliente en la bd
    public boolean agregar(Cliente c) {
        try { dao.insertar(c); JOptionPane.showMessageDialog(null, "Cliente agregado."); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return false; }
    }

    // registra un cliente validando unicidad de dni y email
    public String registrarCliente(Cliente c) {
        try {
            if (dao.buscarPorDni(c.getDni()) != null) {
                return "Error: El DNI ya está registrado.";
            }
            if (dao.buscarPorEmail(c.getEmail()) != null) {
                return "Error: El Email ya está registrado.";
            }
            dao.insertar(c);
            return "OK";
        } catch (Exception ex) {
            return "Error en la base de datos: " + ex.getMessage();
        }
    }

    // actualiza los datos de un cliente que ya existe
    public boolean actualizar(Cliente c) {
        try { dao.actualizar(c); JOptionPane.showMessageDialog(null, "Cliente actualizado."); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return false; }
    }

    // borra un cliente, pero antes pregunta si estás seguro
    public boolean eliminar(int id) {
        // cartel de confirmación (si/no)
        int ok = JOptionPane.showConfirmDialog(null, "¿Eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        // no o cerrar, no hace nada
        if (ok != JOptionPane.YES_OPTION) return false;
        
        // si, le dice al dao que lo elimine
        try { dao.eliminar(id); JOptionPane.showMessageDialog(null, "Cliente eliminado."); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return false; }
    }
}
