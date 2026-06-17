package GUI;

import controller.AdministradorController;
import model.Administrador;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdministradoresPanel extends JPanel {

    private final AdministradorController controller = new AdministradorController();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    // Botones de acción
    private JButton btnAgregar, btnEditar, btnEliminar;

    public AdministradoresPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Gestión de Administradores");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (centro) ---
        String[] columnas = {"ID", "Nombre y Apellido", "Email"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        btnAgregar  = new JButton("Agregar");
        btnEditar   = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");

        btnAgregar.setBackground(new Color(70, 160, 70));
        btnEliminar.setBackground(new Color(200, 60, 60));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        // Listeners
        btnAgregar.addActionListener(e  -> agregar());
        btnEditar.addActionListener(e   -> editar());
        btnEliminar.addActionListener(e -> eliminar());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Administrador> admins = controller.listar();
        for (Administrador a : admins) {
            modeloTabla.addRow(new Object[]{
                a.getId(), a.getNombreApellido(), a.getEmail()
            });
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) { cargarTabla(); return; }
        modeloTabla.setRowCount(0);
        List<Administrador> resultados = controller.buscar(texto);
        for (Administrador a : resultados) {
            modeloTabla.addRow(new Object[]{
                a.getId(), a.getNombreApellido(), a.getEmail()
            });
        }
    }

    private void agregar() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JFrame parentFrame = (parentWindow instanceof JFrame) ? (JFrame) parentWindow : null;
        FormularioAdministrador dialog = new FormularioAdministrador(parentFrame, controller);
        dialog.setVisible(true);
        cargarTabla();
    }

    private void editar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { 
            JOptionPane.showMessageDialog(this, "Seleccioná un administrador de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE); 
            return; 
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        String email = (String) modeloTabla.getValueAt(fila, 2);
        
        Administrador admin = new Administrador();
        admin.setId(id);
        admin.setNombreApellido(nombre);
        admin.setEmail(email);
        
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JFrame parentFrame = (parentWindow instanceof JFrame) ? (JFrame) parentWindow : null;
        FormularioAdministrador dialog = new FormularioAdministrador(parentFrame, controller, admin);
        dialog.setVisible(true);
        cargarTabla();
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccioná un administrador de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE); return; }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar el registro seleccionado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            if (controller.eliminar(id)) { 
                cargarTabla(); 
                JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el administrador.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
