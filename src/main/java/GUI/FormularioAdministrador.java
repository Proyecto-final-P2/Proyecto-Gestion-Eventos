package GUI;

import controller.AdministradorController;
import model.Administrador;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FormularioAdministrador extends JDialog {

    private final AdministradorController controller;
    private final Administrador adminExistente;

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public FormularioAdministrador(JFrame parent, AdministradorController controller) {
        super(parent, "Agregar Administrador", true);
        this.controller = controller;
        this.adminExistente = null;
        initComponents();
    }

    public FormularioAdministrador(JFrame parent, AdministradorController controller, Administrador admin) {
        super(parent, "Modificar Administrador", true);
        this.controller = controller;
        this.adminExistente = admin;
        initComponents();
        precargarCampos(admin);
    }

    private void initComponents() {
        setMinimumSize(new Dimension(350, 300));
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        txtNombre   = new JTextField();
        txtEmail    = new JTextField();
        txtPassword = new JPasswordField();

        agregarCampo(panel, gbc, 0, "Nombre y Apellido (*):", txtNombre);
        agregarCampo(panel, gbc, 2, "Email (*):", txtEmail);
        agregarCampo(panel, gbc, 4, "Contraseña" + (adminExistente != null ? " (Dejar en blanco para no cambiar):" : " (*):"), txtPassword);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(70, 160, 70));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridy = 6;
        gbc.insets = new Insets(20, 5, 5, 5);
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
        campo.setPreferredSize(new Dimension(250, 30));
        panel.add(campo, gbc);
    }

    private void precargarCampos(Administrador a) {
        txtNombre.setText(a.getNombreApellido());
        txtEmail.setText(a.getEmail());
        txtPassword.setText("");
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String email  = txtEmail.getText().trim();
        String pwd    = new String(txtPassword.getPassword()).trim();

        if (nombre.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y el email son obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (adminExistente == null && pwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contraseña es obligatoria para nuevos usuarios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Administrador a = new Administrador();
        a.setNombreApellido(nombre);
        a.setEmail(email);

        if (adminExistente == null) {
            a.setPassword(pwd);
            String res = controller.registrar(a);
            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Administrador agregado con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            a.setId(adminExistente.getId());
            if (pwd.isEmpty()) {
                // Fetch existing password from DB via controller if empty
                List<Administrador> admins = controller.listar();
                for (Administrador adm : admins) {
                    if (adm.getId() == adminExistente.getId()) {
                        a.setPassword(adm.getPassword());
                        break;
                    }
                }
            } else {
                a.setPassword(pwd);
            }
            if (controller.actualizar(a)) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar administrador.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
