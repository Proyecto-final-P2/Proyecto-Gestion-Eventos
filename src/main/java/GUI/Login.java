package GUI;

import controller.LoginController;
import javax.swing.*;
import java.awt.*;

// ventana donde se ingresa email y clave
public class Login extends JFrame {

    // cuadros de texto y el boton
    private JTextField     txtEmail;
    private JPasswordField txtPassword;
    private JButton        btnIngresar;
    private LoginController controller;

    public Login() {
        initComponents();
        controller = new LoginController(this);
    }

    // dibuja la ventana, los botones y los cuadros de texto
    private void initComponents() {
        setTitle("Gestor de Eventos - Iniciar Sesión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        // Definimos la fuente estándar para toda la ventana
        Font fuenteBase = new Font("Segoe UI", Font.PLAIN, 14);
        Font fuenteNegrita = new Font("Segoe UI", Font.BOLD, 14);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Título
        JLabel lblTitulo = new JLabel("Gestor de Eventos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // Email Label
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(fuenteNegrita);
        panel.add(lblEmail, gbc);
        
        // Email Input
        gbc.weightx = 1.0; // Esto hace que el input ocupe todo el ancho disponible
        gbc.gridx = 1; gbc.gridy = 1;
        txtEmail = new JTextField(20);
        txtEmail.setFont(fuenteBase);
        panel.add(txtEmail, gbc);

        // Contraseña Label
        gbc.weightx = 0.0;
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(fuenteNegrita);
        panel.add(lblPassword, gbc);
        
        // Contraseña Input
        gbc.weightx = 1.0;
        gbc.gridx = 1; gbc.gridy = 2;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(fuenteBase);
        panel.add(txtPassword, gbc);

        // Botones
        gbc.weightx = 0.0;
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(fuenteNegrita);
        panel.add(btnIngresar, gbc);

        // accion del boton: le pasa el email y clave al controlador
        btnIngresar.addActionListener(e ->
            controller.iniciarSesion(txtEmail.getText(), new String(txtPassword.getPassword()))
        );

        // Enter también inicia sesión
        txtPassword.addActionListener(e ->
            controller.iniciarSesion(txtEmail.getText(), new String(txtPassword.getPassword()))
        );

        add(panel);
    }
}
