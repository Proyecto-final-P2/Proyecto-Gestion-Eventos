package controller;

import model.Cliente;
import repository.ClienteDAO;
import javax.swing.JOptionPane;
import java.util.List;

public class ClienteController {

    private final ClienteDAO dao = new ClienteDAO();

    public List<Cliente> listar() {
        try { return dao.listar(); }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return List.of(); }
    }

    public List<Cliente> buscar(String nombre) {
        try { return dao.buscarPorNombre(nombre); }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return List.of(); }
    }

    public boolean agregar(Cliente c) {
        try { dao.insertar(c); JOptionPane.showMessageDialog(null, "Cliente agregado."); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return false; }
    }

    public boolean actualizar(Cliente c) {
        try { dao.actualizar(c); JOptionPane.showMessageDialog(null, "Cliente actualizado."); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return false; }
    }

    public boolean eliminar(int id) {
        int ok = JOptionPane.showConfirmDialog(null, "¿Eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return false;
        try { dao.eliminar(id); JOptionPane.showMessageDialog(null, "Cliente eliminado."); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return false; }
    }
}
