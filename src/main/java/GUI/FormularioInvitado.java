package GUI;

import controller.InvitadoController;
import model.Invitado;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo modal para alta y modificación de Invitados.
 * - Constructor sin Invitado → modo ALTA (campos vacíos, llama a insertar)
 * - Constructor con Invitado → modo EDICIÓN (campos precargados, llama a actualizar)
 */
public class FormularioInvitado extends JDialog {

    private final InvitadoController controller;
    private final Invitado invitadoExistente; // null si es alta
    private final int eventoId;               // usado solo en alta

    // componentes del formulario
    private JTextField txtDni;
    private JTextField txtNombreApellido;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JComboBox<String> comboAsistencia;
    private JComboBox<String> comboMenu;

    // ----- Constructor ALTA -----
    public FormularioInvitado(JFrame parent, InvitadoController controller, int eventoId) {
        super(parent, "Agregar Invitado", true);
        this.controller       = controller;
        this.invitadoExistente = null;
        this.eventoId          = eventoId;
        initComponents();
    }

    // ----- Constructor EDICIÓN -----
    public FormularioInvitado(JFrame parent, InvitadoController controller, Invitado invitado) {
        super(parent, "Modificar Invitado", true);
        this.controller        = controller;
        this.invitadoExistente = invitado;
        this.eventoId          = invitado.getEventoId();
        initComponents();
        precargarCampos(invitado);
    }

    private void initComponents() {
        setSize(420, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;

        // DNI
        txtDni = new JTextField();
        agregarCampo(panel, gbc, 0, "DNI:", txtDni);

        // Nombre y Apellido
        txtNombreApellido = new JTextField();
        agregarCampo(panel, gbc, 2, "Nombre y Apellido:", txtNombreApellido);

        // Email
        txtEmail = new JTextField();
        agregarCampo(panel, gbc, 4, "Email:", txtEmail);

        // Teléfono
        txtTelefono = new JTextField();
        agregarCampo(panel, gbc, 6, "Teléfono:", txtTelefono);

        // Asistencia
        comboAsistencia = new JComboBox<>(new String[]{
            "confirmado", "pendiente de confirmacion", "cancelado"
        });
        agregarCampo(panel, gbc, 8, "Asistencia:", comboAsistencia);

        // Preferencia de Menú
        comboMenu = new JComboBox<>(new String[]{
            "Celiaco", "Vegetariano", "Vegano", "Clasico", "Infantil"
        });
        agregarCampo(panel, gbc, 10, "Preferencia de Menú:", comboMenu);

        // Botones
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(70, 160, 70));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridy = 12;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(panelBotones, gbc);

        btnGuardar.addActionListener(e  -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);
    }

    // helper para agregar label + campo con GridBagLayout
    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridy = fila;
        gbc.insets = new Insets(5, 5, 0, 5);
        panel.add(new JLabel(label), gbc);
        gbc.gridy = fila + 1;
        gbc.insets = new Insets(2, 5, 5, 5);
        panel.add(campo, gbc);
    }

    // precarga los campos cuando es modo edición
    private void precargarCampos(Invitado inv) {
        txtDni.setText(String.valueOf(inv.getDni()));
        txtNombreApellido.setText(inv.getNombreApellido());
        txtEmail.setText(inv.getEmail());
        txtTelefono.setText(inv.getTelefono());
        comboAsistencia.setSelectedItem(inv.getAsistencia());
        comboMenu.setSelectedItem(inv.getPreferenciaMenu());
    }

    // valida campos y llama a insertar o actualizar según el modo
    private void guardar() {
        String dni    = txtDni.getText().trim();
        String nombre = txtNombreApellido.getText().trim();
        String email  = txtEmail.getText().trim();
        String tel    = txtTelefono.getText().trim();

        // validaciones
        if (dni.isEmpty() || !dni.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El DNI es obligatorio y debe contener solo dígitos.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Nombre y Apellido es obligatorio.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Email es obligatorio.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // construir el objeto
        Invitado inv = new Invitado();
        inv.setDni(Integer.parseInt(dni));
        inv.setNombreApellido(nombre);
        inv.setEmail(email);
        inv.setTelefono(tel);
        inv.setAsistencia((String) comboAsistencia.getSelectedItem());
        inv.setPreferenciaMenu((String) comboMenu.getSelectedItem());

        if (invitadoExistente == null) {
            // ALTA
            inv.setEventoId(eventoId);
            controller.insertar(inv);
        } else {
            // EDICIÓN: preservar el ID original
            inv.setId(invitadoExistente.getId());
            controller.actualizar(inv);
        }

        dispose();
    }
}
