package GUI;

import controller.EventoController;
import model.Cliente;
import model.Evento;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MisEventosPanel extends JPanel {

    private final EventoController controller = new EventoController();
    private final Cliente cliente;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public MisEventosPanel(Cliente cliente) {
        this.cliente = cliente;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Mis Eventos");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (centro) ---
        String[] columnas = {"ID", "Tipo", "Fecha", "Horario", "Cant. Invitados", "Estado", "Costo Final"};
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
        List<Evento> eventos = controller.listarPorCliente(cliente.getId());
        for (Evento e : eventos) {
            modeloTabla.addRow(new Object[]{
                e.getId(), e.getTipo(), e.getFecha(), e.getHorario(), 
                e.getCantInvitados(), e.getEstado(), "$" + e.getCostoFinal()
            });
        }
    }
}
