package GUI;

import controller.SalonController;
import model.Salon;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo modal para alta y modificación de Salones.
 */
public class FormularioSalon extends JDialog {

    private final SalonController controller;
    private final Salon salonExistente; // null si es alta

    private JTextField txtNombre;
    private JTextField txtDireccion;
    private JTextField txtCapacidad;
    private JTextField txtCantSillas;
    private JTextField txtCantMesas;
    private JTextField txtCosto;

    public FormularioSalon(JFrame parent, SalonController controller) {
        super(parent, "Agregar Salón", true);
        this.controller     = controller;
        this.salonExistente = null;
        initComponents();
    }

    public FormularioSalon(JFrame parent, SalonController controller, Salon salon) {
        super(parent, "Modificar Salón", true);
        this.controller     = controller;
        this.salonExistente = salon;
        initComponents();
        precargarCampos(salon);
    }

    private void initComponents() {
        setMinimumSize(new Dimension(400, 420));
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        txtNombre     = new JTextField();
        txtDireccion  = new JTextField();
        txtCapacidad  = new JTextField();
        txtCantSillas = new JTextField();
        txtCantMesas  = new JTextField();
        txtCosto      = new JTextField();

        agregarCampo(panel, gbc, 0,  "Nombre (*):", txtNombre);
        agregarCampo(panel, gbc, 2,  "Dirección (*):", txtDireccion);
        agregarCampo(panel, gbc, 4,  "Capacidad (*):", txtCapacidad);
        agregarCampo(panel, gbc, 6,  "Sillas (*):", txtCantSillas);
        agregarCampo(panel, gbc, 8,  "Mesas (*):", txtCantMesas);
        agregarCampo(panel, gbc, 10, "Costo (*):", txtCosto);

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

    private void precargarCampos(Salon s) {
        txtNombre.setText(s.getNombre());
        txtDireccion.setText(s.getDireccion());
        txtCapacidad.setText(String.valueOf(s.getCapacidad()));
        txtCantSillas.setText(String.valueOf(s.getCantSillas()));
        txtCantMesas.setText(String.valueOf(s.getCantMesas()));
        txtCosto.setText(String.valueOf(s.getCosto()));
    }

    private void guardar() {
        String nombre    = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String capStr    = txtCapacidad.getText().trim();
        String sillasStr = txtCantSillas.getText().trim();
        String mesasStr  = txtCantMesas.getText().trim();
        String costoStr  = txtCosto.getText().trim();

        if (nombre.isEmpty() || direccion.isEmpty() || capStr.isEmpty() || sillasStr.isEmpty() || mesasStr.isEmpty() || costoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int capacidad, sillas, mesas;
        double costo;

        try {
            capacidad = Integer.parseInt(capStr);
            sillas    = Integer.parseInt(sillasStr);
            mesas     = Integer.parseInt(mesasStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacidad, sillas y mesas deben ser enteros.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            costo = Double.parseDouble(costoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El costo debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Salon s = new Salon();
        s.setNombre(nombre);
        s.setDireccion(direccion);
        s.setCapacidad(capacidad);
        s.setCantSillas(sillas);
        s.setCantMesas(mesas);
        s.setCosto(costo);

        if (salonExistente == null) {
            if (controller.agregar(s)) dispose();
        } else {
            s.setId(salonExistente.getId());
            if (controller.actualizar(s)) dispose();
        }
    }
}
