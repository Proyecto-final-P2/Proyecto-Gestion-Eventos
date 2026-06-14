package GUI;

import controller.ClienteController;
import model.Cliente;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo modal para alta y modificación de Clientes.
 * - Constructor sin Cliente → modo ALTA
 * - Constructor con Cliente → modo EDICIÓN
 */
public class FormularioCliente extends JDialog {

    private final ClienteController controller;
    private final Cliente clienteExistente; // null si es alta

    private JTextField txtDni;
    private JTextField txtNombreApellido;
    private JTextField txtEmail;
    private JTextField txtTelefono;

    // ----- Constructor ALTA -----
    public FormularioCliente(JFrame parent, ClienteController controller) {
        super(parent, "Agregar Cliente", true);
        this.controller       = controller;
        this.clienteExistente = null;
        initComponents();
    }

    // ----- Constructor EDICIÓN -----
    public FormularioCliente(JFrame parent, ClienteController controller, Cliente cliente) {
        super(parent, "Modificar Cliente", true);
        this.controller       = controller;
        this.clienteExistente = cliente;
        initComponents();
        precargarCampos(cliente);
    }

    private void initComponents() {
        setMinimumSize(new Dimension(400, 320));
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        txtDni            = new JTextField();
        txtNombreApellido = new JTextField();
        txtEmail          = new JTextField();
        txtTelefono       = new JTextField();

        agregarCampo(panel, gbc, 0, "DNI:",              txtDni);
        agregarCampo(panel, gbc, 2, "Nombre y Apellido:", txtNombreApellido);
        agregarCampo(panel, gbc, 4, "Email:",             txtEmail);
        agregarCampo(panel, gbc, 6, "Teléfono:",          txtTelefono);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(70, 160, 70));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridy = 8;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(panelBotones, gbc);

        btnGuardar.addActionListener(e  -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);
        pack();
        setLocationRelativeTo(getParent());
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridy = fila;
        gbc.insets = new Insets(6, 5, 0, 5);
        panel.add(new JLabel(label), gbc);
        gbc.gridy = fila + 1;
        gbc.insets = new Insets(2, 5, 5, 5);
        panel.add(campo, gbc);
    }

    private void precargarCampos(Cliente c) {
        txtDni.setText(String.valueOf(c.getDni()));
        txtNombreApellido.setText(c.getNombreApellido());
        txtEmail.setText(c.getEmail());
        txtTelefono.setText(c.getTelefono());
    }

    private void guardar() {
        String dni    = txtDni.getText().trim();
        String nombre = txtNombreApellido.getText().trim();
        String email  = txtEmail.getText().trim();
        String tel    = txtTelefono.getText().trim();

        if (dni.isEmpty() || nombre.isEmpty() || email.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!dni.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El DNI debe contener solo dígitos.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente c = new Cliente();
        c.setDni(Integer.parseInt(dni));
        c.setNombreApellido(nombre);
        c.setEmail(email);
        c.setTelefono(tel);

        if (clienteExistente == null) {
            if (controller.agregar(c)) dispose();
        } else {
            c.setId(clienteExistente.getId());
            if (controller.actualizar(c)) dispose();
        }
    }
}
