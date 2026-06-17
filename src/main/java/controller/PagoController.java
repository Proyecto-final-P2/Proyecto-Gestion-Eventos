package controller;

import model.Pago;
import repository.PagoDAO;
import javax.swing.JOptionPane;
import java.util.List;

// controlador que maneja la lógica de negocio de Pagos
public class PagoController {

    // se conecta con el DAO que habla con la BD
    private final PagoDAO dao = new PagoDAO();

    // pide al DAO que guarde un nuevo pago
    public boolean agregar(Pago p) {
        try { dao.insertar(p); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error al agregar pago: " + ex.getMessage()); return false; }
    }

    // busca un pago por su ID (para pre-cargar el formulario de edición)
    public Pago buscarPorId(int id) {
        try { return dao.buscarPorId(id); }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error al buscar pago: " + ex.getMessage()); return null; }
    }

    // pide al DAO que traiga todos los pagos
    public List<Pago> listar() {
        try { return dao.listar(); }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error al listar pagos: " + ex.getMessage()); return List.of(); }
    }

    // pide al DAO que actualice los datos de un pago
    public boolean actualizar(Pago p) {
        try { dao.actualizar(p); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error al actualizar pago: " + ex.getMessage()); return false; }
    }

    // pide al DAO que borre un pago por ID
    public boolean eliminar(int id) {
        try { dao.eliminar(id); return true; }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Error al eliminar pago: " + ex.getMessage()); return false; }
    }
}
