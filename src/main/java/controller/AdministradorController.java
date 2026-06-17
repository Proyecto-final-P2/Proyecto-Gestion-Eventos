package controller;

import model.Administrador;
import repository.AdministradorDAO;
import javax.swing.JOptionPane;
import java.util.List;

public class AdministradorController {

    private final AdministradorDAO dao = new AdministradorDAO();

    public List<Administrador> listar() {
        try { return dao.listar(); }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return List.of(); }
    }

    public List<Administrador> buscar(String nombre) {
        try { return dao.buscarPorNombre(nombre); }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return List.of(); }
    }

    public boolean agregar(Administrador a) {
        try {
            if (dao.buscarPorEmail(a.getEmail()) != null) {
                JOptionPane.showMessageDialog(null, "Error: El Email ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            dao.insertar(a);
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos: " + ex.getMessage());
            return false;
        }
    }

    public String registrar(Administrador a) {
        try {
            if (dao.buscarPorEmail(a.getEmail()) != null) {
                return "El Email ya está registrado.";
            }
            dao.insertar(a);
            return "OK";
        } catch (Exception ex) {
            return "Error en la base de datos: " + ex.getMessage();
        }
    }

    public boolean actualizar(Administrador a) {
        try { dao.actualizar(a); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return false; }
    }

    public boolean eliminar(int id) {
        try { dao.eliminar(id); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage()); return false; }
    }
}
