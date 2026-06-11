package GUI;

import controller.AdministradorController;
import model.Administrador;

import javax.swing.*;
import java.awt.*;

public class RegistroAdministrador extends JFrame {

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    private AdministradorController controller;
    private Login loginFrame;

    public RegistroAdministrador(Login loginFrame) {
        this.loginFrame = loginFrame;
        this.controller = new AdministradorController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestor de Eventos - Registro de Administrador");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(450, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Título
        JLabel lblTitulo = new JLabel("Registro de Administrador", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

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

        // Contraseña
        gbc.gridy = 7;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridy = 8;
        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(txtSize);
        panel.add(txtPassword, gbc);

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

        gbc.gridy = 9; gbc.gridwidth = 2;
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
        String password = new String(txtPassword.getPassword()).trim();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "El email tiene un formato inválido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Administrador nuevoAdmin = new Administrador();
        nuevoAdmin.setNombreApellido(nombre + " " + apellido);
        nuevoAdmin.setEmail(email);
        nuevoAdmin.setPassword(password);

        String resultado = controller.registrar(nuevoAdmin);

        if (resultado.equals("OK")) {
            JOptionPane.showMessageDialog(this, "Registro completado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            loginFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
