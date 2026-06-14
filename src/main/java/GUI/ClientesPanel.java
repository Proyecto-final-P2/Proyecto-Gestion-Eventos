package GUI;

import controller.ClienteController;
import model.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de Clientes.
 * ESTRUCTURA MODELO: todos los demás paneles siguen este mismo patrón.
 */
public class ClientesPanel extends JPanel {

    private final ClienteController controller = new ClienteController();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    public ClientesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Gestión de Clientes");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (centro) ---
        String[] columnas = {"ID", "DNI", "Nombre y Apellido", "Email", "Teléfono"};
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

    // carga todos los clientes de la bd a la tabla visual
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Cliente> clientes = controller.listar();
        for (Cliente c : clientes) {
            modeloTabla.addRow(new Object[]{
                c.getId(), c.getDni(), c.getNombreApellido(), c.getEmail(), c.getTelefono()
            });
        }
    }

    // filtra la tabla usando el texto del buscador
    private void buscar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) { cargarTabla(); return; }
        modeloTabla.setRowCount(0);
        List<Cliente> resultados = controller.buscar(texto);
        for (Cliente c : resultados) {
            modeloTabla.addRow(new Object[]{
                c.getId(), c.getDni(), c.getNombreApellido(), c.getEmail(), c.getTelefono()
            });
        }
    }

    // abre el formulario vacío para agregar un cliente nuevo
    private void abrirFormularioAlta() {
        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioCliente form = new FormularioCliente(frame, controller);
        form.setVisible(true);
        cargarTabla();
    }

    // abre el formulario pre-cargado con la fila seleccionada para editar
    private void abrirFormularioEdicion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un cliente de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cliente c = new Cliente();
        c.setId((int) modeloTabla.getValueAt(fila, 0));
        c.setDni(Integer.parseInt(modeloTabla.getValueAt(fila, 1).toString()));
        c.setNombreApellido(modeloTabla.getValueAt(fila, 2).toString());
        c.setEmail(modeloTabla.getValueAt(fila, 3).toString());
        c.setTelefono(modeloTabla.getValueAt(fila, 4).toString());

        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioCliente form = new FormularioCliente(frame, controller, c);
        form.setVisible(true);
        cargarTabla();
    }

    // elimina el cliente seleccionado previa confirmación
    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un cliente de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (controller.eliminar(id)) cargarTabla();
    }
}
