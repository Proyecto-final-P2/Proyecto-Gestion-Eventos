package GUI;

import model.Cliente;
import javax.swing.*;
import java.awt.*;

public class MenuCliente extends JFrame {

    private final Cliente clienteActivo;
    private JPanel panelContenido;

    // recibe al cliente que acaba de iniciar sesion
    public MenuCliente(Cliente clienteActivo) {
        this.clienteActivo = clienteActivo;
        initComponents();
    }

    // muestra la ventana entera, la barra lateral oscura y la zona central
    private void initComponents() {
        setTitle("Gestor de Eventos - Panel de Cliente");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Panel lateral con menú
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 45, 45));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Bienvenida
        JLabel lblBienvenida = new JLabel("Hola, " + clienteActivo.getNombreApellido().split(" ")[0]);
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 14));
        lblBienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblBienvenida);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botones de navegación
        String[] secciones = {"Mis Eventos", "Mis Invitados", "Mis Pagos"};
        for (String seccion : secciones) {
            JButton btn = crearBotonMenu(seccion);
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        sidebar.add(Box.createVerticalGlue());

        // Botón cerrar sesión
        JButton btnSalir = crearBotonMenu("Cerrar Sesión");
        btnSalir.setBackground(new Color(180, 60, 60));
        btnSalir.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
        sidebar.add(btnSalir);

        // Panel de contenido central
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(Color.WHITE);

        JPanel panelBienvenida = new JPanel(new GridBagLayout());
        panelBienvenida.setBackground(Color.WHITE);
        GridBagConstraints gbcCent = new GridBagConstraints();
        gbcCent.gridx = 0; gbcCent.gridy = 0;
        
        JLabel lblIcon = new JLabel("🎉");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        panelBienvenida.add(lblIcon, gbcCent);
        
        gbcCent.gridy = 1; gbcCent.insets = new Insets(10, 0, 10, 0);
        JLabel lblTitulo = new JLabel("¡Bienvenido al Gestor de Eventos!");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(50, 50, 50));
        panelBienvenida.add(lblTitulo, gbcCent);
        
        gbcCent.gridy = 2; gbcCent.insets = new Insets(0, 0, 0, 0);
        JLabel lblSub = new JLabel("Seleccioná una sección en el menú lateral para comenzar.");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSub.setForeground(Color.GRAY);
        panelBienvenida.add(lblSub, gbcCent);

        panelContenido.add(panelBienvenida, BorderLayout.CENTER);

        // Layout principal
        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);
    }

    // crea los botones grises del menu
    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(70, 70, 70));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> cargarPanel(texto));
        return btn;
    }

    // segun el boton que toques, reemplaza el centro de la pantalla con ese panel
    private void cargarPanel(String seccion) {
        panelContenido.removeAll();
        JPanel panel;
        switch (seccion) {
            case "Mis Eventos"   -> panel = new MisEventosPanel(clienteActivo);
            case "Mis Invitados" -> panel = new MisInvitadosPanel(clienteActivo);
            case "Mis Pagos"     -> panel = new MisPagosPanel(clienteActivo);
            default              -> panel = new JPanel();
        }
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}
