package GUI;

import controller.LoginController;
import model.Cliente;
import javax.swing.*;
import java.awt.*;

// ventana donde se ingresa email y clave
public class Login extends JFrame {

    // cuadros de texto y el boton
    private JTextField     txtEmail;
    private JPasswordField txtPassword;
    private JButton        btnIngresar;
    private JTextField     txtDni;
    private JButton        btnAccederCliente;
    private LoginController controller;

    public Login() {
        initComponents();
        controller = new LoginController(this);
    }

    // dibuja la ventana, los botones y los cuadros de texto
    private void initComponents() {
        setTitle("Gestor de Eventos - Iniciar Sesión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Título
        JLabel lblTitulo = new JLabel("Gestor de Eventos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // Email
        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridy = 2;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);

        // Contraseña
        gbc.gridy = 3;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridy = 4;
        txtPassword = new JPasswordField(20);
        panel.add(txtPassword, gbc);

        // Botones
        gbc.gridy = 5;
        
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 14));
        
        panel.add(btnIngresar, gbc);

        // accion del boton: le pasa el email y clave al controlador
        btnIngresar.addActionListener(e ->
            controller.iniciarSesion(txtEmail.getText(), new String(txtPassword.getPassword()))
        );

        // Enter también inicia sesión
        txtPassword.addActionListener(e ->
            controller.iniciarSesion(txtEmail.getText(), new String(txtPassword.getPassword()))
        );

        // --- Separador ---
        gbc.gridy = 6;
        panel.add(new JSeparator(), gbc);

        // --- Login Cliente ---
        gbc.gridy = 7;
        JLabel lblCliente = new JLabel("¿Sos cliente? Ingresá tu DNI", SwingConstants.CENTER);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblCliente, gbc);

        gbc.gridy = 8;
        txtDni = new JTextField(20);
        panel.add(txtDni, gbc);

        gbc.gridy = 9;
        btnAccederCliente = new JButton("Acceder como cliente");
        btnAccederCliente.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(btnAccederCliente, gbc);

        btnAccederCliente.addActionListener(e -> {
            try {
                int dni = Integer.parseInt(txtDni.getText().trim());
                Cliente cliente = controller.loginCliente(dni);
                if (cliente == null) {
                    JOptionPane.showMessageDialog(this, "DNI no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    dispose();
                    new MenuCliente(cliente).setVisible(true);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El DNI debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
    }
}
