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

    // Campos del formulario
    private JTextField txtNombre, txtEmail;
    private JPasswordField txtPassword;
    private JButton btnAgregar, btnEditar, btnEliminar, btnLimpiar;

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
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del Administrador"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.gridx = 0;

        txtNombre   = new JTextField();
        txtEmail    = new JTextField();
        txtPassword = new JPasswordField();

        String[] labels = {"Nombre y Apellido:", "Email:", "Contraseña:"};
        JComponent[] fields = {txtNombre, txtEmail, txtPassword};
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

        gbc.gridy = 6; formulario.add(btnAgregar, gbc);
        gbc.gridy = 7; formulario.add(btnEditar, gbc);
        gbc.gridy = 8; formulario.add(btnEliminar, gbc);
        gbc.gridy = 9; formulario.add(btnLimpiar, gbc);

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

    private void cargarFormularioDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtEmail.setText(modeloTabla.getValueAt(fila, 2).toString());
        // Al seleccionar de la tabla no cargamos la contraseña por seguridad,
        // pero la limpiamos por si quiere sobreescribirla
        txtPassword.setText("");
    }

    private void agregar() {
        if (!validarCampos(true)) return;
        Administrador a = new Administrador();
        a.setNombreApellido(txtNombre.getText().trim());
        a.setEmail(txtEmail.getText().trim());
        a.setPassword(new String(txtPassword.getPassword()).trim());
        String res = controller.registrar(a);
        if ("OK".equals(res)) { 
            JOptionPane.showMessageDialog(this, "Administrador agregado con éxito.");
            cargarTabla(); limpiarFormulario(); 
        } else {
            JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccioná un administrador de la tabla."); return; }
        if (!validarCampos(false)) return;
        Administrador a = new Administrador();
        a.setId((int) modeloTabla.getValueAt(fila, 0));
        a.setNombreApellido(txtNombre.getText().trim());
        a.setEmail(txtEmail.getText().trim());
        
        String nuevaClave = new String(txtPassword.getPassword()).trim();
        if (nuevaClave.isEmpty()) {
            // Si la clave está vacía, buscamos la actual para no pisarla
            List<Administrador> admins = controller.listar();
            for (Administrador admin : admins) {
                if (admin.getId() == a.getId()) {
                    a.setPassword(admin.getPassword());
                    break;
                }
            }
        } else {
            a.setPassword(nuevaClave);
        }
        
        if (controller.actualizar(a)) { cargarTabla(); limpiarFormulario(); }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccioná un administrador de la tabla."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (controller.eliminar(id)) { cargarTabla(); limpiarFormulario(); }
    }

    private void limpiarFormulario() {
        txtNombre.setText(""); txtEmail.setText(""); txtPassword.setText("");
        tabla.clearSelection();
    }

    private boolean validarCampos(boolean validacionCompleta) {
        if (txtNombre.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y el email son obligatorios.");
            return false;
        }
        if (validacionCompleta && new String(txtPassword.getPassword()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contraseña es obligatoria para nuevos usuarios.");
            return false;
        }
        return true;
    }
}
