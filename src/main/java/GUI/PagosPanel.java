package GUI;

import controller.PagoController;
import model.Pago;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de Pagos.
 * Sigue el mismo patrón que ClientesPanel:
 * - Tabla en el centro
 * - Botones (Agregar, Modificar, Eliminar) en el sur
 * - El formulario se abre como JDialog (FormularioPago)
 */
public class PagosPanel extends JPanel {

    private final PagoController controller = new PagoController();

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public PagosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {

        // --- TÍTULO (NORTH) ---
        JLabel titulo = new JLabel("Gestión de Pagos");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (CENTER) ---
        // columnas: ID oculto, Monto, Reserva ID, Cliente ID, Método, Fecha
        String[] columnas = {"ID", "Monto Pagado", "Reserva", "Pagado por", "Método de Pago", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ocultar columna ID visualmente
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- BOTONES (SOUTH) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAgregar   = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar  = new JButton("Eliminar");

        btnAgregar.setBackground(new Color(70, 160, 70));
        btnAgregar.setOpaque(true);
        btnAgregar.setBorderPainted(false);
        btnEliminar.setBackground(new Color(200, 60, 60));
        btnEliminar.setOpaque(true);
        btnEliminar.setBorderPainted(false);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e   -> abrirFormularioAlta());
        btnModificar.addActionListener(e -> abrirFormularioEdicion());
        btnEliminar.addActionListener(e  -> eliminarSeleccionado());
    }

    // carga todos los pagos de la BD a la tabla visual
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Pago> pagos = controller.listar();
        for (Pago p : pagos) {
            modeloTabla.addRow(new Object[]{
                p.getId(),
                String.format("$%.2f", p.getMontoPagado()),
                "Reserva #" + p.getReservaId(),
                p.getPagador(),
                p.getMetodoPago(),
                p.getFechaPago() != null ? p.getFechaPago().toString() : "-"
            });
        }
    }

    // abre el formulario vacío para agregar un pago nuevo
    private void abrirFormularioAlta() {
        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioPago form = new FormularioPago(frame, controller);
        form.setVisible(true);
        cargarTabla();
    }

    // abre el formulario pre-cargado con la fila seleccionada para editar
    private void abrirFormularioEdicion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un pago de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // reconstruir el objeto Pago desde la fila seleccionada
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Pago p = controller.buscarPorId(id);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener el pago seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioPago form = new FormularioPago(frame, controller, p);
        form.setVisible(true);
        cargarTabla();
    }

    // elimina el pago seleccionado previa confirmación
    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un pago de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar el registro seleccionado?",
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            if (controller.eliminar(id)) {
                cargarTabla();
                JOptionPane.showMessageDialog(this, "Pago eliminado exitosamente.");
            }
        }
    }
}
