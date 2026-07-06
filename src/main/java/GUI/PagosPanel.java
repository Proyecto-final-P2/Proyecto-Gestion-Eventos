package GUI;

import controller.PagoController;
import controller.EventoController;
import model.Pago;
import model.Evento;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel de gestión de Pagos (ABM Completo, Modo Ventana).
 */
public class PagosPanel extends JPanel {

    private final PagoController controller = new PagoController();
    private final EventoController eventoController = new EventoController();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    public PagosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Gestión de Pagos");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (centro) ---
        String[] columnas = {"ID", "Monto Pagado", "Pagador", "Método", "Fecha", "Evento"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ocultar columna ID visualmente
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scroll = new JScrollPane(tabla);

        // Barra de búsqueda sobre la tabla
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar todos");
        panelBuscar.add(new JLabel("Buscar por Pagador:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnMostrarTodos);

        btnBuscar.addActionListener(e -> buscar());
        btnMostrarTodos.addActionListener(e -> cargarTabla());
        txtBuscar.addActionListener(e -> buscar());

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelBuscar, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        // --- BOTONES (sur) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");

        btnAgregar.setBackground(new Color(70, 160, 70));

        btnEliminar.setBackground(new Color(200, 60, 60));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        // Listeners de los botones
        btnAgregar.addActionListener(e   -> abrirFormularioAlta());
        btnModificar.addActionListener(e -> abrirFormularioEdicion());
        btnEliminar.addActionListener(e  -> eliminarSeleccionado());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        
        List<Evento> eventosList = eventoController.listar();
        Map<Integer, Evento> mapaEventos = new HashMap<>();
        for (Evento e : eventosList) {
            mapaEventos.put(e.getId(), e);
        }

        List<Pago> pagos = controller.listar();
        for (Pago p : pagos) {
            Evento e = mapaEventos.get(p.getEventoId());
            String desc = "Evento #" + p.getEventoId();
            if (e != null) {
                desc += " - " + e.getTipo() + " (" + e.getFecha() + ")";
            }
            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getMontoPagado(),
                p.getPagador() != null ? p.getPagador() : "-",
                p.getMetodoPago(),
                p.getFechaPago(),
                desc
            });
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) { cargarTabla(); return; }
        modeloTabla.setRowCount(0);
        
        List<Evento> eventosList = eventoController.listar();
        Map<Integer, Evento> mapaEventos = new HashMap<>();
        for (Evento e : eventosList) {
            mapaEventos.put(e.getId(), e);
        }

        List<Pago> resultados = controller.buscar(texto);
        for (Pago p : resultados) {
            Evento e = mapaEventos.get(p.getEventoId());
            String desc = "Evento #" + p.getEventoId();
            if (e != null) {
                desc += " - " + e.getTipo() + " (" + e.getFecha() + ")";
            }
            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getMontoPagado(),
                p.getPagador() != null ? p.getPagador() : "-",
                p.getMetodoPago(),
                p.getFechaPago(),
                desc
            });
        }
    }

    private void abrirFormularioAlta() {
        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioPago form = new FormularioPago(frame, controller);
        form.setVisible(true);
        cargarTabla();
    }

    private void abrirFormularioEdicion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un pago de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        Pago p = controller.buscarPorId(id);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la información del pago.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioPago form = new FormularioPago(frame, controller, p);
        form.setVisible(true);
        cargarTabla();
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un pago de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar el registro seleccionado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            if (controller.eliminar(id)) {
                cargarTabla();
                JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.");
            }
        }
    }
}
