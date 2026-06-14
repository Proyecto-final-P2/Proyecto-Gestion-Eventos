package GUI;

import controller.InvitadoController;
import model.Cliente;
import model.Invitado;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MisInvitadosPanel extends JPanel {

    private final InvitadoController controller = new InvitadoController();
    private final Cliente cliente;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public MisInvitadosPanel(Cliente cliente) {
        this.cliente = cliente;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Mis Invitados");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (centro) ---
        String[] columnas = {"Nombre", "Email", "Teléfono", "Asistencia", "Menú"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Invitado> invitados = controller.listarPorCliente(cliente.getId());
        for (Invitado i : invitados) {
            modeloTabla.addRow(new Object[]{
                i.getNombreApellido(), i.getEmail(), i.getTelefono(), 
                i.getAsistencia(), i.getPreferenciaMenu()
            });
        }
    }
}
