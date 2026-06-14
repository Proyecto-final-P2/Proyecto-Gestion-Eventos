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

    // Campos del formulario
    private JTextField txtDni, txtNombre, txtEmail, txtTelefono;
    private JButton    btnAgregar, btnEditar, btnEliminar, btnLimpiar;

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
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFormularioDesdeTabla();
        });

        JScrollPane scroll = new JScrollPane(tabla);

        // Barra de búsqueda sobre la tabla
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar todos");
        panelBuscar.add(new JLabel("Buscar por DNI:"));
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
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Esto permite que ocupen todo el espacio vacío a la derecha
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.gridx = 0;

        txtDni      = new JTextField();
        txtNombre   = new JTextField();
        txtEmail    = new JTextField();
        txtTelefono = new JTextField();

        String[] labels = {"DNI:", "Nombre y Apellido:", "Email:", "Teléfono:"};
        JTextField[] fields = {txtDni, txtNombre, txtEmail, txtTelefono};
        for (int i = 0; i < labels.length; i++) {
            fields[i].setPreferredSize(new Dimension(250, 40)); // tamaño de los campos
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

        gbc.gridy = 8; formulario.add(btnAgregar, gbc);
        gbc.gridy = 9; formulario.add(btnEditar, gbc);
        gbc.gridy = 10; formulario.add(btnEliminar, gbc);
        gbc.gridy = 11; formulario.add(btnLimpiar, gbc);

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

    // cuando haces clic en una fila, copia los datos al formulario
    private void cargarFormularioDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtDni.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtNombre.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtEmail.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtTelefono.setText(modeloTabla.getValueAt(fila, 4).toString());
    }

    // toma los datos del formulario y pide al controlador guardarlos
    private void agregar() {
        if (!validarCampos()) return;
        Cliente c = new Cliente();
        c.setDni(Integer.parseInt(txtDni.getText().trim()));
        c.setNombreApellido(txtNombre.getText().trim());
        c.setEmail(txtEmail.getText().trim());
        c.setTelefono(txtTelefono.getText().trim());
        if (controller.agregar(c)) { cargarTabla(); limpiarFormulario(); }
    }

    // toma los datos modificados y actualiza al cliente seleccionado
    private void editar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccioná un cliente de la tabla."); return; }
        if (!validarCampos()) return;
        Cliente c = new Cliente();
        c.setId((int) modeloTabla.getValueAt(fila, 0));
        c.setDni(Integer.parseInt(txtDni.getText().trim()));
        c.setNombreApellido(txtNombre.getText().trim());
        c.setEmail(txtEmail.getText().trim());
        c.setTelefono(txtTelefono.getText().trim());
        if (controller.actualizar(c)) { cargarTabla(); limpiarFormulario(); }
    }

    // le pide al controlador borrar el cliente seleccionado
    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccioná un cliente de la tabla."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (controller.eliminar(id)) { cargarTabla(); limpiarFormulario(); }
    }

    // vacia todos los cuadritos de texto
    private void limpiarFormulario() {
        txtDni.setText(""); txtNombre.setText("");
        txtEmail.setText(""); txtTelefono.setText("");
        tabla.clearSelection();
    }

    // revisa que no dejes campos vacios y que el dni sea numero
    private boolean validarCampos() {
        if (txtDni.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()
            || txtEmail.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }
        try { Integer.parseInt(txtDni.getText().trim()); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "El DNI debe ser numérico."); return false; }
        return true;
    }
}
