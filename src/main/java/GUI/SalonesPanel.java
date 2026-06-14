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

    // Campos del formulario
    private JTextField txtNombre, txtDireccion, txtCapacidad, txtCantSillas, txtCantMesas, txtCosto;
    private JButton    btnAgregar, btnEditar, btnEliminar, btnLimpiar;

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
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFormularioDesdeTabla();
        });

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

        // --- FORMULARIO (este) ---
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del salón"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.gridx = 0;

        txtNombre     = new JTextField();
        txtDireccion  = new JTextField();
        txtCapacidad  = new JTextField();
        txtCantSillas = new JTextField();
        txtCantMesas  = new JTextField();
        txtCosto      = new JTextField();

        String[] labels = {"Nombre:", "Dirección:", "Capacidad:", "Sillas:", "Mesas:", "Costo:"};
        JTextField[] fields = {txtNombre, txtDireccion, txtCapacidad, txtCantSillas, txtCantMesas, txtCosto};
        for (int i = 0; i < labels.length; i++) {
            fields[i].setPreferredSize(new Dimension(250, 40));
            gbc.gridy = i * 2;
            formulario.add(new JLabel(labels[i]), gbc);
            gbc.gridy = i * 2 + 1;
            formulario.add(fields[i], gbc);
        }

        // Botones
        btnAgregar  = new JButton("Agregar");
        btnEditar   = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar  = new JButton("Limpiar");

        btnAgregar.setBackground(new Color(70, 160, 70));
        btnEliminar.setBackground(new Color(200, 60, 60));

        gbc.gridy = 12; formulario.add(btnAgregar, gbc);
        gbc.gridy = 13; formulario.add(btnEditar, gbc);
        gbc.gridy = 14; formulario.add(btnEliminar, gbc);
        gbc.gridy = 15; formulario.add(btnLimpiar, gbc);

        btnAgregar.addActionListener(e  -> agregar());
        btnEditar.addActionListener(e   -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e  -> limpiarFormulario());

        JScrollPane scrollForm = new JScrollPane(formulario);
        scrollForm.setBorder(null);
        scrollForm.setPreferredSize(new Dimension(280, 0));
        scrollForm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollForm, BorderLayout.EAST);
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

    private void cargarFormularioDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtDireccion.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtCapacidad.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtCantSillas.setText(modeloTabla.getValueAt(fila, 4).toString());
        txtCantMesas.setText(modeloTabla.getValueAt(fila, 5).toString());
        txtCosto.setText(modeloTabla.getValueAt(fila, 6).toString());
    }

    private void agregar() {
        if (!validarCampos()) return;
        Salon s = new Salon();
        s.setNombre(txtNombre.getText().trim());
        s.setDireccion(txtDireccion.getText().trim());
        s.setCapacidad(Integer.parseInt(txtCapacidad.getText().trim()));
        s.setCantSillas(Integer.parseInt(txtCantSillas.getText().trim()));
        s.setCantMesas(Integer.parseInt(txtCantMesas.getText().trim()));
        s.setCosto(Double.parseDouble(txtCosto.getText().trim()));
        if (controller.agregar(s)) { cargarTabla(); limpiarFormulario(); }
    }

    private void editar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccioná un salón de la tabla."); return; }
        if (!validarCampos()) return;
        Salon s = new Salon();
        s.setId((int) modeloTabla.getValueAt(fila, 0));
        s.setNombre(txtNombre.getText().trim());
        s.setDireccion(txtDireccion.getText().trim());
        s.setCapacidad(Integer.parseInt(txtCapacidad.getText().trim()));
        s.setCantSillas(Integer.parseInt(txtCantSillas.getText().trim()));
        s.setCantMesas(Integer.parseInt(txtCantMesas.getText().trim()));
        s.setCosto(Double.parseDouble(txtCosto.getText().trim()));
        if (controller.actualizar(s)) { cargarTabla(); limpiarFormulario(); }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccioná un salón de la tabla."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (controller.eliminar(id)) { cargarTabla(); limpiarFormulario(); }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDireccion.setText("");
        txtCapacidad.setText("");
        txtCantSillas.setText("");
        txtCantMesas.setText("");
        txtCosto.setText("");
        tabla.clearSelection();
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() || txtDireccion.getText().trim().isEmpty()
            || txtCapacidad.getText().trim().isEmpty() || txtCantSillas.getText().trim().isEmpty()
            || txtCantMesas.getText().trim().isEmpty() || txtCosto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }
        try {
            Integer.parseInt(txtCapacidad.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero.");
            return false;
        }
        try {
            Integer.parseInt(txtCantSillas.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad de sillas debe ser un número entero.");
            return false;
        }
        try {
            Integer.parseInt(txtCantMesas.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad de mesas debe ser un número entero.");
            return false;
        }
        try {
            Double.parseDouble(txtCosto.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El costo debe ser un número decimal (ej. 1500.50).");
            return false;
        }
        return true;
    }
}
