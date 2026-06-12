package GUI;

import controller.PagoController;
import model.Cliente;
import model.Pago;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MisPagosPanel extends JPanel {

    private final PagoController controller = new PagoController();
    private final Cliente cliente;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public MisPagosPanel(Cliente cliente) {
        this.cliente = cliente;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Mis Pagos");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (centro) ---
        String[] columnas = {"ID Pago", "Monto", "ID Reserva"};
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
        List<Pago> pagos = controller.listarPorCliente(cliente.getId());
        for (Pago p : pagos) {
            modeloTabla.addRow(new Object[]{
                p.getId(), "$" + p.getMontoPagado(), p.getReservaId()
            });
        }
    }
}
