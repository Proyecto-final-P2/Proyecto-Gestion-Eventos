package GUI;

import controller.ServicioController;
import model.Servicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de Servicios.
 * Sigue la estructura visual exacta de ClientesPanel.java.
 */
public class ServiciosPanel extends JPanel {

    private final ServicioController controller = new ServicioController();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    public ServiciosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Gestión de Servicios");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (centro) ---
        String[] columnas = {"ID", "Tipo", "Proveedor", "Costo", "Cantidad", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
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
        panelBuscar.add(new JLabel("Buscar por Proveedor:"));
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

    // carga todos los servicios de la bd a la tabla visual
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Servicio> servicios = controller.listarServicios();
        for (Servicio s : servicios) {
            modeloTabla.addRow(new Object[]{
                s.getId(), s.getTipo(), s.getProveedor(), s.getCosto(), s.getCantidad(), s.getEstado()
            });
        }
    }

    // filtra la tabla usando el texto del buscador
    private void buscar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) { cargarTabla(); return; }
        modeloTabla.setRowCount(0);
        List<Servicio> resultados = controller.buscar(texto);
        for (Servicio s : resultados) {
            modeloTabla.addRow(new Object[]{
                s.getId(), s.getTipo(), s.getProveedor(), s.getCosto(), s.getCantidad(), s.getEstado()
            });
        }
    }

    // abre el formulario vacío para agregar un servicio nuevo
    private void abrirFormularioAlta() {
        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioServicio form = new FormularioServicio(frame, controller);
        form.setVisible(true);
        cargarTabla();
    }

    // abre el formulario pre-cargado con la fila seleccionada para editar
    private void abrirFormularioEdicion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un servicio de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Servicio s = new Servicio();
        s.setId((int) modeloTabla.getValueAt(fila, 0));
        s.setTipo(modeloTabla.getValueAt(fila, 1).toString());
        s.setProveedor(modeloTabla.getValueAt(fila, 2).toString());
        s.setCosto(Double.parseDouble(modeloTabla.getValueAt(fila, 3).toString()));
        s.setCantidad(Integer.parseInt(modeloTabla.getValueAt(fila, 4).toString()));
        s.setEstado(modeloTabla.getValueAt(fila, 5).toString());

        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioServicio form = new FormularioServicio(frame, controller, s);
        form.setVisible(true);
        cargarTabla();
    }

    // elimina el servicio seleccionado previa confirmación
    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un servicio de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar el registro seleccionado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            if (controller.eliminarServicio(id)) {
                cargarTabla();
                JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.");
            }
        }
    }
}