package GUI;

import controller.ClienteController;
import model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class RegistroCliente extends JFrame {

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JTextField txtDni;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private ClienteController controller;
    private Login loginFrame;

    public RegistroCliente(Login loginFrame) {
        this.loginFrame = loginFrame;
        this.controller = new ClienteController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestor de Eventos - Registro de Cliente");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(450, 550); // tamaño de la ventana
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // bordes del panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0); // espacio entre componentes

        // Título
        JLabel lblTitulo = new JLabel("Registro de Cliente", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // Dimensión preferida para hacer los cuadros de texto más altos
        Dimension txtSize = new Dimension(0, 30);

        // Nombre
        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridy = 2;
        txtNombre = new JTextField(20);
        txtNombre.setPreferredSize(txtSize);
        panel.add(txtNombre, gbc);

        // Apellido
        gbc.gridy = 3;
        panel.add(new JLabel("Apellido:"), gbc);
        gbc.gridy = 4;
        txtApellido = new JTextField(20);
        txtApellido.setPreferredSize(txtSize);
        panel.add(txtApellido, gbc);

        // Email
        gbc.gridy = 5;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridy = 6;
        txtEmail = new JTextField(20);
        txtEmail.setPreferredSize(txtSize);
        panel.add(txtEmail, gbc);

        // Teléfono
        gbc.gridy = 7;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridy = 8;
        txtTelefono = new JTextField(20);
        txtTelefono.setPreferredSize(txtSize);
        panel.add(txtTelefono, gbc);

        // DNI
        gbc.gridy = 9;
        panel.add(new JLabel("DNI:"), gbc);
        gbc.gridy = 10;
        txtDni = new JTextField(20);
        txtDni.setPreferredSize(txtSize);
        panel.add(txtDni, gbc);

        // Panel de Botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        
        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBackground(new Color(46, 139, 87));
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(205, 92, 92));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));

        btnPanel.add(btnRegistrar);
        btnPanel.add(btnCancelar);

        gbc.gridy = 11; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(btnPanel, gbc);

        // Acciones
        btnRegistrar.addActionListener(e -> registrar());
        
        btnCancelar.addActionListener(e -> {
            this.dispose();
            loginFrame.setVisible(true);
        });

        // Que al cerrar con la cruz roja también vuelva al login
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                loginFrame.setVisible(true);
            }
        });

        add(panel);
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String dniStr = txtDni.getText().trim();

        // Validación: Campos vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || dniStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación: Formato de Email
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "El email tiene un formato inválido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación: Teléfono (solo números, max 15)
        if (!Pattern.matches("\\d{1,15}", telefono)) {
            JOptionPane.showMessageDialog(this, "El teléfono debe contener solo números (máximo 15).", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación: DNI (solo números)
        int dni;
        try {
            dni = Integer.parseInt(dniStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser numérico.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombreApellido(nombre + " " + apellido);
        nuevoCliente.setEmail(email);
        nuevoCliente.setTelefono(telefono);
        nuevoCliente.setDni(dni);

        String resultado = controller.registrarCliente(nuevoCliente);

        if (resultado.equals("OK")) {
            JOptionPane.showMessageDialog(this, "Registro completado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            loginFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
