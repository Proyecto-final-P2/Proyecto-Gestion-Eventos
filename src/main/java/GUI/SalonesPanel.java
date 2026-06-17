package GUI;

import controller.SalonController;
import model.Salon;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de Salones.
 */
public class SalonesPanel extends JPanel {

    private final SalonController controller = new SalonController();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    public SalonesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Gestión de Salones");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (centro) ---
        String[] columnas = {"ID", "Nombre", "Dirección", "Capacidad", "Sillas", "Mesas", "Costo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ocultar columna ID visualmente
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scroll = new JScrollPane(tabla);

        // Barra de búsqueda sobre la tabla
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar todos");
        panelBuscar.add(new JLabel("Buscar por nombre:"));
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

        JButton btnAgregar   = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar  = new JButton("Eliminar");

        btnAgregar.setBackground(new Color(70, 160, 70));
        btnEliminar.setBackground(new Color(200, 60, 60));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e   -> abrirFormularioAlta());
        btnModificar.addActionListener(e -> abrirFormularioEdicion());
        btnEliminar.addActionListener(e  -> eliminarSeleccionado());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Salon> salones = controller.listar();
        for (Salon s : salones) {
            modeloTabla.addRow(new Object[]{
                s.getId(), s.getNombre(), s.getDireccion(), s.getCapacidad(), s.getCantSillas(), s.getCantMesas(), s.getCosto()
            });
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) { cargarTabla(); return; }
        modeloTabla.setRowCount(0);
        List<Salon> resultados = controller.buscar(texto);
        for (Salon s : resultados) {
            modeloTabla.addRow(new Object[]{
                s.getId(), s.getNombre(), s.getDireccion(), s.getCapacidad(), s.getCantSillas(), s.getCantMesas(), s.getCosto()
            });
        }
    }

    private void abrirFormularioAlta() {
        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioSalon form = new FormularioSalon(frame, controller);
        form.setVisible(true);
        cargarTabla();
    }

    private void abrirFormularioEdicion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un salón de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Salon s = new Salon();
        s.setId((int) modeloTabla.getValueAt(fila, 0));
        s.setNombre(modeloTabla.getValueAt(fila, 1).toString());
        s.setDireccion(modeloTabla.getValueAt(fila, 2).toString());
        s.setCapacidad(Integer.parseInt(modeloTabla.getValueAt(fila, 3).toString()));
        s.setCantSillas(Integer.parseInt(modeloTabla.getValueAt(fila, 4).toString()));
        s.setCantMesas(Integer.parseInt(modeloTabla.getValueAt(fila, 5).toString()));
        s.setCosto(Double.parseDouble(modeloTabla.getValueAt(fila, 6).toString()));

        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioSalon form = new FormularioSalon(frame, controller, s);
        form.setVisible(true);
        cargarTabla();
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un salón de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
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
