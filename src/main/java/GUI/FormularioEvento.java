package GUI;

import controller.EventoController;
import model.Cliente;
import model.Evento;
import model.Salon;
import controller.ClienteController;
import controller.SalonController;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import controller.ServicioController;
import model.Servicio;

/**
 * Diálogo modal para alta y modificación de Eventos.
 */
public class FormularioEvento extends JDialog {

    private final EventoController controller;
    private final Evento eventoExistente; // null si es alta

    private JComboBox<String> cbTipo;
    private JSpinner spinFecha;
    private JSpinner spinHoraInicio;
    private JSpinner spinHoraFin;
    private JTextField txtCantInvitados;
    private JComboBox<String> cbEstado;
    private JComboBox<Cliente> cbCliente;
    private JComboBox<Salon> cbSalon;
    private JPanel panelServiciosCheck;
    private List<JCheckBox> checkServiciosList;

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
        setMinimumSize(new Dimension(450, 520));
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
        cbTipo.setEditable(false);
        cbTipo.addActionListener(e -> {
            Object sel = cbTipo.getSelectedItem();
            if (sel != null) {
                String val = sel.toString();
                if ("Otros".equals(val)) {
                    cbTipo.setEditable(true);
                    Component editor = cbTipo.getEditor().getEditorComponent();
                    editor.requestFocus();
                    if (editor instanceof JTextField) {
                        ((JTextField) editor).selectAll();
                    }
                } else {
                    boolean isPredef = java.util.Arrays.asList(
                        "Boda", "Casamiento", "Cumpleaños", "Conferencia", "Fiesta de Navidad", "Reunión de Empresa"
                    ).contains(val);
                    if (isPredef) {
                        cbTipo.setEditable(false);
                    }
                }
            }
        });
        
        spinFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinFecha, "dd-MM-yyyy");
        spinFecha.setEditor(dateEditor);

        spinHoraInicio = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinHoraInicio, "HH:mm");
        spinHoraInicio.setEditor(timeEditor);

        spinHoraFin = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditorFin = new JSpinner.DateEditor(spinHoraFin, "HH:mm");
        spinHoraFin.setEditor(timeEditorFin);

        txtCantInvitados = new JTextField();
        cbEstado         = new JComboBox<>(new String[]{"confirmado", "pendiente de confirmacion", "cancelado"});
        cbCliente        = new JComboBox<>();
        cbSalon          = new JComboBox<>();
        
        panelServiciosCheck = new JPanel();
        panelServiciosCheck.setLayout(new BoxLayout(panelServiciosCheck, BoxLayout.Y_AXIS));
        panelServiciosCheck.setBackground(Color.WHITE);
        checkServiciosList = new ArrayList<>();
        
        JScrollPane scrollServicios = new JScrollPane(panelServiciosCheck);
        scrollServicios.setPreferredSize(new Dimension(250, 100));
        scrollServicios.getVerticalScrollBar().setUnitIncrement(16);

        agregarCampo(panel, gbc, 0,  "Tipo de Evento (*):", cbTipo);
        agregarCampo(panel, gbc, 2,  "Fecha (*):", spinFecha);
        agregarCampo(panel, gbc, 4,  "Hora Inicio (*):", spinHoraInicio);
        agregarCampo(panel, gbc, 6,  "Hora Fin (*):", spinHoraFin);
        agregarCampo(panel, gbc, 8,  "Cant. Invitados (*):", txtCantInvitados);
        agregarCampo(panel, gbc, 10, "Estado (*):", cbEstado);
        agregarCampo(panel, gbc, 12, "Cliente (*):", cbCliente);
        agregarCampo(panel, gbc, 14, "Salón (*):", cbSalon);
        agregarCampo(panel, gbc, 16, "Servicios Opcionales:", scrollServicios);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(70, 160, 70));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridy = 18;
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
            List<Cliente> clientes = new ClienteController().listar();
            for (Cliente c : clientes) cbCliente.addItem(c);
            cbCliente.setSelectedIndex(-1);

            List<Salon> salones = new SalonController().listar();
            for (Salon s : salones) cbSalon.addItem(s);
            cbSalon.setSelectedIndex(-1);

            List<Servicio> servicios = new ServicioController().listarServicios();
            for (Servicio s : servicios) {
                JCheckBox chk = new JCheckBox(s.toString());
                chk.putClientProperty("servicio", s);
                chk.setBackground(Color.WHITE);
                checkServiciosList.add(chk);
                panelServiciosCheck.add(chk);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes/salones: " + ex.getMessage());
        }
    }

    private void precargarCampos(Evento e) {
        String tipo = e.getTipo();
        boolean isPredef = java.util.Arrays.asList(
            "Boda", "Casamiento", "Cumpleaños", "Conferencia", "Fiesta de Navidad", "Reunión de Empresa", "Otros"
        ).contains(tipo);
        if (!isPredef) {
            cbTipo.setEditable(true);
        }
        cbTipo.setSelectedItem(tipo);
        
        try {
            java.util.Date d = java.sql.Date.valueOf(e.getFecha());
            spinFecha.setValue(d);
        } catch(Exception ex) {}

        try {
            java.util.Date t = java.sql.Time.valueOf(e.getHoraInicio());
            spinHoraInicio.setValue(t);
        } catch(Exception ex) {}

        try {
            java.util.Date t = java.sql.Time.valueOf(e.getHoraFin());
            spinHoraFin.setValue(t);
        } catch(Exception ex) {}

        txtCantInvitados.setText(String.valueOf(e.getCantInvitados()));
        cbEstado.setSelectedItem(e.getEstado());

        for (int i = 0; i < cbCliente.getItemCount(); i++) {
            Cliente c = cbCliente.getItemAt(i);
            if (c.getId() == e.getClienteId()) {
                cbCliente.setSelectedIndex(i); break;
            }
        }
        for (int i = 0; i < cbSalon.getItemCount(); i++) {
            Salon s = cbSalon.getItemAt(i);
            if (s.getId() == e.getSalonId()) {
                cbSalon.setSelectedIndex(i); 
                break;
            }
        }
        
        // Cargar servicios seleccionados (checkear las casillas correspondientes)
        try {
            List<Integer> idsContratados = new controller.ServicioController().obtenerIdsServiciosPorEvento(e.getId());
            for (JCheckBox chk : checkServiciosList) {
                model.Servicio srv = (model.Servicio) chk.getClientProperty("servicio");
                if (idsContratados.contains(srv.getId())) {
                    chk.setSelected(true);
                }
            }
        } catch (Exception ex) {
            // Ignorar
        }
    }

    private void guardar() {
        String tipo = "";
        if (cbTipo.getSelectedItem() != null) {
            tipo = cbTipo.getSelectedItem().toString().trim();
        }
        String invitados = txtCantInvitados.getText().trim();

        if (tipo.isEmpty() || invitados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cbCliente.getSelectedItem() == null || cbSalon.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar cliente y salón.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cant;
        java.time.LocalDate date;
        java.time.LocalTime tInicio;
        java.time.LocalTime tFin;

        try { cant = Integer.parseInt(invitados); } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invitados debe ser numérico."); return; }
        
        try { 
            java.util.Date d = (java.util.Date) spinFecha.getValue();
            date = new java.sql.Date(d.getTime()).toLocalDate(); 
        } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Formato de fecha inválido."); return; }

        try { 
            java.util.Date t = (java.util.Date) spinHoraInicio.getValue();
            String timeStr = new java.text.SimpleDateFormat("HH:mm").format(t);
            tInicio = java.time.LocalTime.parse(timeStr); 
        } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Formato de hora de inicio inválido."); return; }

        try { 
            java.util.Date t = (java.util.Date) spinHoraFin.getValue();
            String timeStr = new java.text.SimpleDateFormat("HH:mm").format(t);
            tFin = java.time.LocalTime.parse(timeStr); 
        } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Formato de hora de fin inválido."); return; }

        Salon salonSeleccionado = (Salon) cbSalon.getSelectedItem();

        // 1. Validar capacidad del salón
        if (cant > salonSeleccionado.getCapacidad()) {
            JOptionPane.showMessageDialog(this, "La cantidad de invitados (" + cant + ") supera la capacidad del salón (" + salonSeleccionado.getCapacidad() + ").", "Error de Capacidad", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validar superposición temporal
        int idActual = (eventoExistente != null) ? eventoExistente.getId() : -1;
        if (controller.existeSuperposicion(salonSeleccionado.getId(), date, tInicio, tFin, idActual)) {
            JOptionPane.showMessageDialog(this, "El salón ya se encuentra ocupado en ese horario. Por favor, seleccione otro horario o salón.", "Superposición de Evento", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Validar agenda de servicios (Opción B)
        List<Servicio> serviciosSeleccionados = new ArrayList<>();
        for (JCheckBox chk : checkServiciosList) {
            if (chk.isSelected()) {
                serviciosSeleccionados.add((Servicio) chk.getClientProperty("servicio"));
            }
        }
        List<String> ocupados = controller.verificarDisponibilidadServicios(serviciosSeleccionados, date, tInicio, tFin, idActual);
        if (!ocupados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los siguientes servicios ya están reservados en ese horario:\n- " + String.join("\n- ", ocupados) + "\nPor favor, deselecciónelos o cambie el horario del evento.", "Superposición de Servicios", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Evento e = new Evento();
        e.setTipo(tipo);
        e.setFecha(date);
        e.setHoraInicio(tInicio);
        e.setHoraFin(tFin);
        e.setCantInvitados(cant);
        e.setEstado(cbEstado.getSelectedItem().toString());
        e.setClienteId(((Cliente) cbCliente.getSelectedItem()).getId());
        e.setSalonId(((Salon) cbSalon.getSelectedItem()).getId());

        if (eventoExistente == null) {
            if (controller.agregar(e, serviciosSeleccionados)) {
                JOptionPane.showMessageDialog(this, "Evento y servicios guardados exitosamente.");
                dispose();
            }
        } else {
            e.setId(eventoExistente.getId());
            if (controller.actualizar(e, serviciosSeleccionados)) {
                JOptionPane.showMessageDialog(this, "Evento y servicios actualizados exitosamente.");
                dispose();
            }
        }
    }
}
