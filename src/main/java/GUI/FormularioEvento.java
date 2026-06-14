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

    private JTextField txtTipo;
    private JTextField txtFecha;
    private JTextField txtHorario;
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

        txtTipo          = new JTextField();
        txtFecha         = new JTextField("YYYY-MM-DD");
        txtFecha.setForeground(Color.GRAY);
        txtFecha.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtFecha.getText().equals("YYYY-MM-DD")) {
                    txtFecha.setText(""); txtFecha.setForeground(Color.BLACK);
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtFecha.getText().isEmpty()) {
                    txtFecha.setForeground(Color.GRAY); txtFecha.setText("YYYY-MM-DD");
                }
            }
        });

        txtHorario       = new JTextField("HH:mm:ss");
        txtHorario.setForeground(Color.GRAY);
        txtHorario.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtHorario.getText().equals("HH:mm:ss")) {
                    txtHorario.setText(""); txtHorario.setForeground(Color.BLACK);
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtHorario.getText().isEmpty()) {
                    txtHorario.setForeground(Color.GRAY); txtHorario.setText("HH:mm:ss");
                }
            }
        });

        txtCantInvitados = new JTextField();
        cbEstado         = new JComboBox<>(new String[]{"confirmado", "pendiente de confirmacion", "cancelado"});
        txtCostoFinal    = new JTextField();
        cbCliente        = new JComboBox<>();
        cbSalon          = new JComboBox<>();

        agregarCampo(panel, gbc, 0,  "Tipo de Evento:", txtTipo);
        agregarCampo(panel, gbc, 2,  "Fecha (YYYY-MM-DD):", txtFecha);
        agregarCampo(panel, gbc, 4,  "Horario (HH:mm:ss):", txtHorario);
        agregarCampo(panel, gbc, 6,  "Cant. Invitados:", txtCantInvitados);
        agregarCampo(panel, gbc, 8,  "Estado:", cbEstado);
        agregarCampo(panel, gbc, 10, "Costo Final:", txtCostoFinal);
        agregarCampo(panel, gbc, 12, "Cliente:", cbCliente);
        agregarCampo(panel, gbc, 14, "Salón:", cbSalon);

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
        txtTipo.setText(e.getTipo());
        txtFecha.setForeground(Color.BLACK);
        txtFecha.setText(e.getFecha().toString());
        txtHorario.setForeground(Color.BLACK);
        txtHorario.setText(e.getHorario().toString());
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
        String tipo      = txtTipo.getText().trim();
        String fecha     = txtFecha.getText().trim();
        String horario   = txtHorario.getText().trim();
        String invitados = txtCantInvitados.getText().trim();
        String costo     = txtCostoFinal.getText().trim();

        if (tipo.isEmpty() || fecha.equals("YYYY-MM-DD") || horario.equals("HH:mm:ss") || invitados.isEmpty() || costo.isEmpty()) {
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

        try { date = java.time.LocalDate.parse(fecha); } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Formato de fecha inválido."); return; }

        try { time = java.time.LocalTime.parse(horario); } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Formato de horario inválido."); return; }

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
            if (controller.agregar(e)) dispose();
        } else {
            e.setId(eventoExistente.getId());
            if (controller.actualizar(e)) dispose();
        }
    }
}
