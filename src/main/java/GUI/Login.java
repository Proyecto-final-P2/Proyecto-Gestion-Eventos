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
    private JButton        btnRegistrarse;
    private LoginController controller;

    public Login() {
        initComponents();
        controller = new LoginController(this);
    }

    // dibuja la ventana, los botones y los cuadros de texto
    private void initComponents() {
        setTitle("Gestor de Eventos - Iniciar Sesión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
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
        
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(70, 130, 180));
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.setBackground(new Color(46, 139, 87));
        btnRegistrarse.setFont(new Font("Arial", Font.BOLD, 14));

        btnPanel.add(btnIngresar);
        btnPanel.add(btnRegistrarse);
        
        panel.add(btnPanel, gbc);

        // accion del boton: le pasa el email y clave al controlador
        btnIngresar.addActionListener(e ->
            controller.iniciarSesion(txtEmail.getText(), new String(txtPassword.getPassword()))
        );

        // Enter también inicia sesión
        txtPassword.addActionListener(e ->
            controller.iniciarSesion(txtEmail.getText(), new String(txtPassword.getPassword()))
        );

        // accion del boton registrarse: abre la otra ventana
        btnRegistrarse.addActionListener(e -> {
            this.setVisible(false);
            new RegistroAdministrador(this).setVisible(true);
        });

        add(panel);
    }
}
