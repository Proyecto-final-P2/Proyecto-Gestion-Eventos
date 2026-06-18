package GUI;

import controller.ServicioController;
import model.Servicio;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo modal para alta y modificación de Servicios.
 */
public class FormularioServicio extends JDialog {

    private final ServicioController controller;
    private final Servicio servicioExistente; // null si es alta

    private JComboBox<String> cmbTipo;
    private JTextField txtProveedor;
    private JTextField txtCosto;
    private JTextField txtCantidad;
    private JComboBox<String> cmbEstado;

    // ----- Constructor ALTA -----
    public FormularioServicio(JFrame parent, ServicioController controller) {
        super(parent, "Agregar Servicio", true);
        this.controller       = controller;
        this.servicioExistente = null;
        initComponents();
    }

    // ----- Constructor EDICIÓN -----
    public FormularioServicio(JFrame parent, ServicioController controller, Servicio servicio) {
        super(parent, "Modificar Servicio", true);
        this.controller       = controller;
        this.servicioExistente = servicio;
        initComponents();
        precargarCampos(servicio);
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

        cmbTipo = new JComboBox<>(new String[]{"Catering", "DJ", "Decoración", "Sonido", "Iluminación", "Fotografía", "Seguridad", "Otro"});
        txtProveedor = new JTextField();
        txtCosto = new JTextField();
        txtCantidad = new JTextField();
        cmbEstado = new JComboBox<>(new String[]{"confirmado", "pendiente de confirmacion", "cancelado"});

        agregarCampo(panel, gbc, 0, "Tipo (*):", cmbTipo);
        agregarCampo(panel, gbc, 2, "Proveedor (*):", txtProveedor);
        agregarCampo(panel, gbc, 4, "Costo ($) (*):", txtCosto);
        agregarCampo(panel, gbc, 6, "Cantidad (*):", txtCantidad);
        agregarCampo(panel, gbc, 8, "Estado (*):", cmbEstado);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(70, 160, 70));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridy = 10;
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

    private void precargarCampos(Servicio s) {
        cmbTipo.setSelectedItem(s.getTipo());
        txtProveedor.setText(s.getProveedor());
        txtCosto.setText(String.valueOf(s.getCosto()));
        txtCantidad.setText(String.valueOf(s.getCantidad()));
        cmbEstado.setSelectedItem(s.getEstado());
    }

    private void guardar() {
        String proveedor = txtProveedor.getText().trim();
        String costoStr = txtCosto.getText().trim();
        String cantidadStr = txtCantidad.getText().trim();

        if (proveedor.isEmpty() || costoStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos marcados con (*).", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double costo = Double.parseDouble(costoStr);
            int cantidad = Integer.parseInt(cantidadStr);

            if (costo < 0 || cantidad < 0) {
                JOptionPane.showMessageDialog(this, "El costo y la cantidad no pueden ser negativos.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = (servicioExistente == null) ? 0 : servicioExistente.getId();
            Servicio s = new Servicio(id, (String) cmbTipo.getSelectedItem(), proveedor, costo, cantidad, (String) cmbEstado.getSelectedItem());

            if (servicioExistente == null) {
                if (controller.guardarServicio(s)) {
                    JOptionPane.showMessageDialog(this, "Servicio guardado exitosamente.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar el servicio en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (controller.actualizarServicio(s)) {
                    JOptionPane.showMessageDialog(this, "Servicio actualizado exitosamente.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el servicio en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Costo y Cantidad deben ser números válidos.", "Error de validación", JOptionPane.WARNING_MESSAGE);
        }
    }
}
