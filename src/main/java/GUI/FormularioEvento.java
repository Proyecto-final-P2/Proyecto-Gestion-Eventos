package GUI;

import controller.EventoController;
import model.Cliente;
import model.Evento;
import model.Salon;
import repository.ClienteDAO;
import repository.SalonDAO;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Diálogo modal para alta y modificación de Eventos.
 */
public class FormularioEvento extends JDialog {

    private final EventoController controller;
    private final Evento eventoExistente; // null si es alta

    private JComboBox<String> cbTipo;
    private JSpinner spinFecha;
    private JSpinner spinHorario;
    private JTextField txtCantInvitados;
    private JComboBox<String> cbEstado;
    private JTextField txtCostoFinal;
    private JComboBox<Cliente> cbCliente;
    private JComboBox<Salon> cbSalon;

    public FormularioEvento(JFrame parent, EventoController controller) {
        super(parent, "Agregar Evento", true);
        this.controller      = controller;
        this.eventoExistente = null;
        initComponents();
        cargarCombos();
    }

    public FormularioEvento(JFrame parent, EventoController controller, Evento evento) {
        super(parent, "Modificar Evento", true);
        this.controller      = controller;
        this.eventoExistente = evento;
        initComponents();
        cargarCombos();
        precargarCampos(evento);
    }

    private void initComponents() {
        setMinimumSize(new Dimension(450, 550));
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        cbTipo = new JComboBox<>(new String[]{
            "Boda", "Casamiento", "Cumpleaños", "Conferencia", "Fiesta de Navidad", "Reunión de Empresa", "Otros"
        });
        cbTipo.setEditable(true);
        
        spinFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinFecha, "dd-MM-yyyy");
        spinFecha.setEditor(dateEditor);

        spinHorario = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinHorario, "HH:mm");
        spinHorario.setEditor(timeEditor);

        txtCantInvitados = new JTextField();
        cbEstado         = new JComboBox<>(new String[]{"confirmado", "pendiente de confirmacion", "cancelado"});
        txtCostoFinal    = new JTextField();
        cbCliente        = new JComboBox<>();
        cbSalon          = new JComboBox<>();

        agregarCampo(panel, gbc, 0,  "Tipo de Evento (*):", cbTipo);
        agregarCampo(panel, gbc, 2,  "Fecha (*):", spinFecha);
        agregarCampo(panel, gbc, 4,  "Horario (*):", spinHorario);
        agregarCampo(panel, gbc, 6,  "Cant. Invitados (*):", txtCantInvitados);
        agregarCampo(panel, gbc, 8,  "Estado (*):", cbEstado);
        agregarCampo(panel, gbc, 10, "Costo Final (*):", txtCostoFinal);
        agregarCampo(panel, gbc, 12, "Cliente (*):", cbCliente);
        agregarCampo(panel, gbc, 14, "Salón (*):", cbSalon);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(70, 160, 70));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridy = 16;
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

    private void cargarCombos() {
        try {
            List<Cliente> clientes = new ClienteDAO().listar();
            for (Cliente c : clientes) cbCliente.addItem(c);
            cbCliente.setSelectedIndex(-1);

            List<Salon> salones = new SalonDAO().listar();
            for (Salon s : salones) cbSalon.addItem(s);
            cbSalon.setSelectedIndex(-1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes/salones: " + ex.getMessage());
        }
    }

    private void precargarCampos(Evento e) {
        cbTipo.setSelectedItem(e.getTipo());
        
        try {
            java.util.Date d = java.sql.Date.valueOf(e.getFecha());
            spinFecha.setValue(d);
        } catch(Exception ex) {}

        try {
            java.util.Date t = java.sql.Time.valueOf(e.getHorario());
            spinHorario.setValue(t);
        } catch(Exception ex) {}
        txtCantInvitados.setText(String.valueOf(e.getCantInvitados()));
        cbEstado.setSelectedItem(e.getEstado());
        txtCostoFinal.setText(String.valueOf(e.getCostoFinal()));

        for (int i = 0; i < cbCliente.getItemCount(); i++) {
            Cliente c = cbCliente.getItemAt(i);
            if (c.getId() == e.getClienteId()) {
                cbCliente.setSelectedIndex(i); break;
            }
        }
        for (int i = 0; i < cbSalon.getItemCount(); i++) {
            Salon s = cbSalon.getItemAt(i);
            if (s.getId() == e.getSalonId()) {
                cbSalon.setSelectedIndex(i); break;
            }
        }
    }

    private void guardar() {
        String tipo = "";
        if (cbTipo.getSelectedItem() != null) {
            tipo = cbTipo.getSelectedItem().toString().trim();
        }
        String invitados = txtCantInvitados.getText().trim();
        String costo     = txtCostoFinal.getText().trim();

        if (tipo.isEmpty() || invitados.isEmpty() || costo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cbCliente.getSelectedItem() == null || cbSalon.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar cliente y salón.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cant;
        double costoNum;
        java.time.LocalDate date;
        java.time.LocalTime time;

        try { cant = Integer.parseInt(invitados); } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invitados debe ser numérico."); return; }
        
        try { costoNum = Double.parseDouble(costo); } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Costo debe ser numérico."); return; }

        try { 
            java.util.Date d = (java.util.Date) spinFecha.getValue();
            date = new java.sql.Date(d.getTime()).toLocalDate(); 
        } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Formato de fecha inválido."); return; }

        try { 
            java.util.Date t = (java.util.Date) spinHorario.getValue();
            // LocalTime a partir de Date formateado (para no tener problemas de zona horaria)
            String timeStr = new java.text.SimpleDateFormat("HH:mm").format(t);
            time = java.time.LocalTime.parse(timeStr); 
        } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Formato de horario inválido."); return; }

        Salon salonSeleccionado = (Salon) cbSalon.getSelectedItem();

        // 1. Validar capacidad del salón
        if (cant > salonSeleccionado.getCapacidad()) {
            JOptionPane.showMessageDialog(this, "La cantidad de invitados (" + cant + ") supera la capacidad del salón (" + salonSeleccionado.getCapacidad() + ").", "Error de Capacidad", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validar superposición temporal
        int idActual = (eventoExistente != null) ? eventoExistente.getId() : -1;
        if (controller.existeSuperposicion(salonSeleccionado.getId(), date, idActual)) {
            JOptionPane.showMessageDialog(this, "El salón ya se encuentra reservado para esa fecha. Por favor, seleccione otro salón u otra fecha.", "Superposición de Evento", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Evento e = new Evento();
        e.setTipo(tipo);
        e.setFecha(date);
        e.setHorario(time);
        e.setCantInvitados(cant);
        e.setEstado(cbEstado.getSelectedItem().toString());
        e.setCostoFinal(costoNum);
        e.setClienteId(((Cliente) cbCliente.getSelectedItem()).getId());
        e.setSalonId(((Salon) cbSalon.getSelectedItem()).getId());

        if (eventoExistente == null) {
            if (controller.agregar(e)) {
                JOptionPane.showMessageDialog(this, "Evento guardado exitosamente.");
                dispose();
            }
        } else {
            e.setId(eventoExistente.getId());
            if (controller.actualizar(e)) {
                JOptionPane.showMessageDialog(this, "Evento actualizado exitosamente.");
                dispose();
            }
        }
    }
}
