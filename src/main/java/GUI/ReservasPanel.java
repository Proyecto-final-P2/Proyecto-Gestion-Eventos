package GUI;

import model.Reserva;
import repository.ReservaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel de gestión de Reservas.
 */
public class ReservasPanel extends JPanel {

    private final ReservaDAO dao = new ReservaDAO();

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public ReservasPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Gestión de Reservas");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (centro) ---
        String[] columnas = {"ID", "Fecha", "Cliente", "Salón", "Hora Inicio", "Hora Fin", "Monto"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ocultar columna ID visualmente para consistencia con otros paneles
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // --- BOTONES (sur) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        btnAgregar.setBackground(new Color(70, 160, 70));
        btnEliminar.setBackground(new Color(200, 60, 60));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> abrirFormularioAlta());
        btnActualizar.addActionListener(e -> cargarTabla());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
    }

    // Carga los datos de las reservas desde ReservaDAO en la tabla
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Reserva> reservas = dao.listar();
            for (Reserva r : reservas) {
                modeloTabla.addRow(new Object[]{
                    r.getR_ID(),
                    r.getR_Fecha().toString(),
                    r.getClienteNombre(),
                    r.getSalonNombre(),
                    r.getR_HoraInicio().toString(),
                    r.getR_HoraFin().toString(),
                    "$" + String.format("%.2f", r.getR_Monto())
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al listar reservas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Abre el formulario modal para registrar una nueva reserva
    private void abrirFormularioAlta() {
        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioReserva form = new FormularioReserva(frame, dao);
        form.setVisible(true);
        cargarTabla();
    }

    // Elimina la reserva seleccionada de la tabla
    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una reserva de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);

        int ok = JOptionPane.showConfirmDialog(this, "¿Eliminar esta reserva?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        try {
            dao.eliminar(id);
            JOptionPane.showMessageDialog(this, "Reserva eliminada.");
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar reserva: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

/**
 * Diálogo modal para agregar una nueva Reserva.
 */
class FormularioReserva extends JDialog {
    private final ReservaDAO dao;

    private JComboBox<model.Cliente> comboCliente;
    private JComboBox<model.Salon> comboSalon;
    private JTextField txtFecha;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFin;
    private JTextField txtMonto;

    public FormularioReserva(JFrame parent, ReservaDAO dao) {
        super(parent, "Agregar Reserva", true);
        this.dao = dao;
        initComponents();
    }

    private void initComponents() {
        setMinimumSize(new Dimension(380, 380));
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        comboCliente = new JComboBox<>();
        comboSalon = new JComboBox<>();
        txtFecha = new JTextField(LocalDate.now().toString());
        txtHoraInicio = new JTextField("18:00");
        txtHoraFin = new JTextField("23:00");
        txtMonto = new JTextField();

        populateCombos();

        agregarCampo(panel, gbc, 0, "Cliente (*):", comboCliente);
        agregarCampo(panel, gbc, 2, "Salón (*):", comboSalon);
        agregarCampo(panel, gbc, 4, "Fecha (AAAA-MM-DD) (*):", txtFecha);
        agregarCampo(panel, gbc, 6, "Hora Inicio (HH:MM) (*):", txtHoraInicio);
        agregarCampo(panel, gbc, 8, "Hora Fin (HH:MM) (*):", txtHoraFin);
        agregarCampo(panel, gbc, 10, "Monto (*):", txtMonto);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(70, 160, 70));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridy = 12;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(panelBotones, gbc);

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);
        pack();
        setLocationRelativeTo(getParent());
    }

    // Carga dinámicamente los combos de Clientes y Salones usando sus respectivos DAOs
    private void populateCombos() {
        try {
            List<model.Cliente> clientes = new repository.ClienteDAO().listar();
            for (model.Cliente c : clientes) {
                comboCliente.addItem(c);
            }

            List<model.Salon> salones = new repository.SalonDAO().listar();
            for (model.Salon s : salones) {
                comboSalon.addItem(s);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar opciones del formulario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridy = fila;
        gbc.insets = new Insets(6, 5, 0, 5);
        panel.add(new JLabel(label), gbc);
        gbc.gridy = fila + 1;
        gbc.insets = new Insets(2, 5, 5, 5);
        panel.add(campo, gbc);
    }

    // Guarda los datos de la reserva ingresada en la base de datos
    private void guardar() {
        model.Cliente clienteSel = (model.Cliente) comboCliente.getSelectedItem();
        model.Salon salonSel = (model.Salon) comboSalon.getSelectedItem();
        String fechaStr = txtFecha.getText().trim();
        String inicioStr = txtHoraInicio.getText().trim();
        String finStr = txtHoraFin.getText().trim();
        String montoStr = txtMonto.getText().trim();

        if (clienteSel == null || salonSel == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Cliente y un Salón.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fechaStr.isEmpty() || inicioStr.isEmpty() || finStr.isEmpty() || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener formato AAAA-MM-DD (ej: 2026-06-17).", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalTime horaInicio;
        try {
            if (inicioStr.length() == 5) {
                horaInicio = LocalTime.parse(inicioStr, DateTimeFormatter.ofPattern("HH:mm"));
            } else {
                horaInicio = LocalTime.parse(inicioStr);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "La hora de inicio debe tener formato HH:MM (ej: 18:00).", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalTime horaFin;
        try {
            if (finStr.length() == 5) {
                horaFin = LocalTime.parse(finStr, DateTimeFormatter.ofPattern("HH:mm"));
            } else {
                horaFin = LocalTime.parse(finStr);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "La hora de fin debe tener formato HH:MM (ej: 23:00).", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
            if (monto < 0) {
                JOptionPane.showMessageDialog(this, "El monto no puede ser negativo.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un número válido.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Reserva r = new Reserva();
        r.setR_Fecha(fecha);
        r.setR_HoraInicio(horaInicio);
        r.setR_HoraFin(horaFin);
        r.setR_Monto(monto);
        r.setR_ClienteID(clienteSel.getId());
        r.setR_SalonID(salonSel.getId());

        try {
            dao.insertar(r);
            JOptionPane.showMessageDialog(this, "Reserva agregada correctamente.");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar reserva en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
