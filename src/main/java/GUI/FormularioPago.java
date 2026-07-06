package GUI;

import controller.PagoController;
import controller.EventoController;
import model.Pago;
import model.Evento;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Diálogo modal para alta y modificación de Pagos.
 */
public class FormularioPago extends JDialog {

    private final PagoController controller;
    private final EventoController eventoController = new EventoController();
    private final Pago pagoExistente; // null si es alta

    private JTextField txtMonto;
    private JComboBox<EventoComboItem> cbEventos;
    private JTextField txtPagador;
    private JComboBox<String> cbMetodoPago;

    // ----- Constructor ALTA -----
    public FormularioPago(JFrame parent, PagoController controller) {
        super(parent, "Agregar Pago", true);
        this.controller = controller;
        this.pagoExistente = null;
        initComponents();
        cargarComboEventos();
    }

    // ----- Constructor EDICIÓN -----
    public FormularioPago(JFrame parent, PagoController controller, Pago pago) {
        super(parent, "Modificar Pago", true);
        this.controller = controller;
        this.pagoExistente = pago;
        initComponents();
        cargarComboEventos();
        precargarCampos(pago);
    }

    private void initComponents() {
        setMinimumSize(new Dimension(400, 380));
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        txtMonto = new JTextField();
        cbEventos = new JComboBox<>();
        txtPagador = new JTextField();
        cbMetodoPago = new JComboBox<>(new String[]{"Transferencia", "Efectivo", "Credito", "Debito", "PagoFacil"});

        agregarCampo(panel, gbc, 0, "Monto Pagado ($) (*):", txtMonto);
        agregarCampo(panel, gbc, 2, "Evento (*):", cbEventos);
        agregarCampo(panel, gbc, 4, "Realizado por:", txtPagador);
        agregarCampo(panel, gbc, 6, "Método de Pago (*):", cbMetodoPago);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.setBackground(new Color(70, 160, 70));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridy = 8;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(panelBotones, gbc);

        btnGuardar.addActionListener(e -> guardar());
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

    private void cargarComboEventos() {
        cbEventos.removeAllItems();
        cbEventos.addItem(new EventoComboItem(-1, "-- Seleccione un evento --"));
        List<Evento> lista = eventoController.listar();
        for (Evento e : lista) {
            String desc = "Evento #" + e.getId() + " - " + e.getTipo() + " (" + e.getFecha() + ")";
            cbEventos.addItem(new EventoComboItem(e.getId(), desc));
        }
    }

    private void precargarCampos(Pago p) {
        txtMonto.setText(String.valueOf(p.getMontoPagado()));
        txtPagador.setText(p.getPagador() != null ? p.getPagador() : "");
        cbMetodoPago.setSelectedItem(p.getMetodoPago());
        
        for (int i = 0; i < cbEventos.getItemCount(); i++) {
            EventoComboItem item = cbEventos.getItemAt(i);
            if (item.getId() == p.getEventoId()) {
                cbEventos.setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardar() {
        String montoStr = txtMonto.getText().trim();
        if (montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el monto pagado.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un valor decimal válido.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (monto <= 0) {
            JOptionPane.showMessageDialog(this, "El monto debe ser mayor a cero.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        EventoComboItem evtItem = (EventoComboItem) cbEventos.getSelectedItem();
        if (evtItem == null || evtItem.getId() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un evento válido.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // VALIDACIÓN: El monto acumulado no debe superar el costo total del evento
        Evento evt = eventoController.buscarPorId(evtItem.getId());
        if (evt == null) {
            JOptionPane.showMessageDialog(this, "No se pudo verificar el evento.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double totalEvento = evt.getCostoTotal();

        double yaPagado = 0;
        List<Pago> pagos = controller.listarPorEvento(evtItem.getId());
        for (Pago pag : pagos) {
            if (pagoExistente == null || pag.getId() != pagoExistente.getId()) {
                yaPagado += pag.getMontoPagado();
            }
        }

        if (yaPagado + monto > totalEvento) {
            double remanente = totalEvento - yaPagado;
            JOptionPane.showMessageDialog(this,
                "El monto ingresado supera el total permitido para el evento.\n" +
                "Costo Total Evento: $" + totalEvento + "\n" +
                "Monto ya registrado: $" + yaPagado + "\n" +
                "Máximo permitido para este pago: $" + (remanente < 0 ? 0.0 : remanente),
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String pagador = txtPagador.getText().trim();
        String metodo = cbMetodoPago.getSelectedItem().toString();

        Pago p = new Pago();
        p.setMontoPagado(monto);
        p.setEventoId(evtItem.getId());
        p.setPagador(pagador.isEmpty() ? null : pagador);
        p.setMetodoPago(metodo);

        if (pagoExistente == null) {
            if (controller.agregar(p)) {
                JOptionPane.showMessageDialog(this, "Pago registrado con éxito.");
                dispose();
            }
        } else {
            p.setId(pagoExistente.getId());
            p.setFechaPago(pagoExistente.getFechaPago());
            if (controller.actualizar(p)) {
                JOptionPane.showMessageDialog(this, "Pago modificado con éxito.");
                dispose();
            }
        }
    }

    private static class EventoComboItem {
        private final int id;
        private final String descripcion;

        public EventoComboItem(int id, String descripcion) {
            this.id = id;
            this.descripcion = descripcion;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return descripcion;
        }
    }
}
