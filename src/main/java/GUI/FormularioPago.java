package GUI;

import controller.PagoController;
import controller.ReservaController;
import model.Pago;
import model.Reserva;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Diálogo modal para alta y modificación de Pagos.
 * - Constructor sin Pago → modo ALTA
 * - Constructor con Pago → modo EDICIÓN
 * Sigue el mismo patrón que FormularioCliente.
 */
public class FormularioPago extends JDialog {

    private final PagoController pagoController;
    private final Pago           pagoExistente; // null si es alta

    private JTextField        txtMonto;
    private JTextField        txtPagador;      // texto libre: nombre de quien paga
    private JComboBox<String> cmbReservas;
    private JComboBox<String> cmbMetodoPago;

    // mapa etiqueta → ID real de la reserva
    private final Map<String, Integer> mapaReservas = new HashMap<>();
    // mapa etiqueta → monto total de la reserva (para validar el tope)
    private final Map<String, Double>  mapaMontos   = new HashMap<>();

    private static final String[] METODOS_PAGO = {
        "Efectivo", "Transferencia", "Débito", "Crédito", "PagoFácil"
    };

    // ----- Constructor ALTA -----
    public FormularioPago(JFrame parent, PagoController pagoController) {
        super(parent, "Agregar Pago", true);
        this.pagoController = pagoController;
        this.pagoExistente  = null;
        initComponents();
    }

    // ----- Constructor EDICIÓN -----
    public FormularioPago(JFrame parent, PagoController pagoController, Pago pago) {
        super(parent, "Modificar Pago", true);
        this.pagoController = pagoController;
        this.pagoExistente  = pago;
        initComponents();
        precargarCampos(pago);
    }

    private void initComponents() {
        setMinimumSize(new Dimension(400, 360));
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx   = 0;

        txtMonto      = new JTextField();
        txtPagador    = new JTextField();
        cmbReservas   = new JComboBox<>();
        cmbMetodoPago = new JComboBox<>(METODOS_PAGO);

        // poblar combo de reservas
        cargarReservas();

        // campos en orden vertical
        agregarCampo(panel, gbc, 0, "Monto pagado (*):($)", txtMonto);
        agregarCampo(panel, gbc, 2, "Reserva (*):",         cmbReservas);
        agregarCampo(panel, gbc, 4, "Pagado por (*):",      txtPagador);
        agregarCampo(panel, gbc, 6, "Método de pago (*):",  cmbMetodoPago);

        // botones
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(70, 160, 70));
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridy  = 8;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(panelBotones, gbc);

        btnGuardar.addActionListener(e  -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);
        pack();
        setLocationRelativeTo(getParent());
    }

    // helper: agrega label + campo al GridBagLayout
    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridy  = fila;
        gbc.insets = new Insets(6, 5, 0, 5);
        panel.add(new JLabel(label), gbc);
        gbc.gridy  = fila + 1;
        gbc.insets = new Insets(2, 5, 5, 5);
        panel.add(campo, gbc);
    }

    // puebla el combobox de reservas y guarda también el monto de cada una
    private void cargarReservas() {
        mapaReservas.clear();
        mapaMontos.clear();
        cmbReservas.removeAllItems();
        List<Reserva> reservas = new ReservaController().listar();
        for (Reserva r : reservas) {
            String etiqueta = "Reserva #" + r.getR_ID() + " - " + r.getR_Fecha();
            mapaReservas.put(etiqueta, r.getR_ID());
            mapaMontos.put(etiqueta, r.getR_Monto());
            cmbReservas.addItem(etiqueta);
        }
    }

    // precarga campos en modo edición
    private void precargarCampos(Pago p) {
        txtMonto.setText(String.valueOf(p.getMontoPagado()));
        txtPagador.setText(p.getPagador() != null ? p.getPagador() : "");

        for (Map.Entry<String, Integer> e : mapaReservas.entrySet()) {
            if (e.getValue() == p.getReservaId()) { cmbReservas.setSelectedItem(e.getKey()); break; }
        }
        if (p.getMetodoPago() != null) cmbMetodoPago.setSelectedItem(p.getMetodoPago());
    }

    private void guardar() {
        // 1. validar monto no vacío y numérico positivo
        String montoTexto = txtMonto.getText().trim();
        if (montoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El monto es obligatorio.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double monto;
        try {
            monto = Double.parseDouble(montoTexto);
            if (monto <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un número decimal positivo.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. validar reserva seleccionada
        if (cmbReservas.getSelectedItem() == null || cmbReservas.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Debés seleccionar una reserva.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. validar que el monto no supere el valor total de la reserva
        String etiquetaRes  = (String) cmbReservas.getSelectedItem();
        double montoReserva = mapaMontos.getOrDefault(etiquetaRes, Double.MAX_VALUE);
        if (monto > montoReserva) {
            JOptionPane.showMessageDialog(this,
                String.format("El monto ($%.2f) supera el valor total de la reserva ($%.2f).", monto, montoReserva),
                "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. validar nombre del pagador
        String pagador = txtPagador.getText().trim();
        if (pagador.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de quien paga es obligatorio.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // construir objeto Pago
        int    reservaId = mapaReservas.getOrDefault(etiquetaRes, 0);
        String metodo    = (String) cmbMetodoPago.getSelectedItem();

        Pago p = new Pago(0, monto, reservaId, pagador, metodo, null);

        if (pagoExistente == null) {
            if (pagoController.agregar(p)) {
                JOptionPane.showMessageDialog(this, "Pago guardado exitosamente.");
                dispose();
            }
        } else {
            p.setId(pagoExistente.getId());
            if (pagoController.actualizar(p)) {
                JOptionPane.showMessageDialog(this, "Pago actualizado exitosamente.");
                dispose();
            }
        }
    }
}
