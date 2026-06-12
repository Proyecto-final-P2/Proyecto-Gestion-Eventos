package GUI;

import controller.EventoController;
import model.Evento;
import model.Cliente;
import model.Salon;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de Eventos.
 */
public class EventosPanel extends JPanel {

    private final EventoController controller = new EventoController();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    // Campos del formulario
    private JTextField txtTipo, txtFecha, txtHorario, txtCantInvitados, txtCostoFinal;
    private JComboBox<String> cbEstado;
    private JComboBox<Cliente> cbCliente;
    private JComboBox<Salon> cbSalon;
    
    private JButton btnAgregar, btnEditar, btnEliminar, btnLimpiar;

    // Listas auxiliares cargadas en memoria (caché)
    private List<Cliente> clientesList;
    private List<Salon> salonesList;

    public EventosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarCombos();
        cargarTabla();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Gestión de Eventos");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- TABLA (centro) ---
        String[] columnas = {"ID", "Tipo", "Fecha", "Horario", "Invitados", "Estado", "Costo", "Cliente", "Salón"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFormularioDesdeTabla();
        });

        JScrollPane scroll = new JScrollPane(tabla);

        // Barra de búsqueda sobre la tabla
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar todos");
        panelBuscar.add(new JLabel("Buscar por tipo:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnMostrarTodos);

        btnBuscar.addActionListener(e -> buscar());
        btnMostrarTodos.addActionListener(e -> cargarTabla());
        txtBuscar.addActionListener(e -> buscar());

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelBuscar, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        // --- FORMULARIO (este) ---
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del evento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(4, 5, 4, 5); // padding reducido para que entre todo bien
        gbc.gridx = 0;

        txtTipo          = new JTextField();
        txtFecha         = new JTextField("YYYY-MM-DD");
        txtFecha.setForeground(Color.GRAY);
        txtFecha.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtFecha.getText().equals("YYYY-MM-DD")) {
                    txtFecha.setText("");
                    txtFecha.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtFecha.getText().isEmpty()) {
                    txtFecha.setForeground(Color.GRAY);
                    txtFecha.setText("YYYY-MM-DD");
                }
            }
        });

        txtHorario       = new JTextField("HH:mm:ss");
        txtHorario.setForeground(Color.GRAY);
        txtHorario.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtHorario.getText().equals("HH:mm:ss")) {
                    txtHorario.setText("");
                    txtHorario.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtHorario.getText().isEmpty()) {
                    txtHorario.setForeground(Color.GRAY);
                    txtHorario.setText("HH:mm:ss");
                }
            }
        });

        txtCantInvitados = new JTextField();
        cbEstado         = new JComboBox<>(new String[]{"confirmado", "pendiente de confirmacion", "cancelado"});
        txtCostoFinal    = new JTextField();
        cbCliente        = new JComboBox<>();
        cbSalon          = new JComboBox<>();

        // Configuración de tamaño de campos
        JComponent[] inputs = {txtTipo, txtFecha, txtHorario, txtCantInvitados, cbEstado, txtCostoFinal, cbCliente, cbSalon};
        for (JComponent input : inputs) {
            input.setPreferredSize(new Dimension(250, 30));
        }

        // Agregar labels e inputs al formulario
        gbc.gridy = 0; formulario.add(new JLabel("Tipo de Evento:"), gbc);
        gbc.gridy = 1; formulario.add(txtTipo, gbc);

        gbc.gridy = 2; formulario.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridy = 3; formulario.add(txtFecha, gbc);

        gbc.gridy = 4; formulario.add(new JLabel("Horario (HH:mm:ss):"), gbc);
        gbc.gridy = 5; formulario.add(txtHorario, gbc);

        gbc.gridy = 6; formulario.add(new JLabel("Cant. Invitados:"), gbc);
        gbc.gridy = 7; formulario.add(txtCantInvitados, gbc);

        gbc.gridy = 8; formulario.add(new JLabel("Estado:"), gbc);
        gbc.gridy = 9; formulario.add(cbEstado, gbc);

        gbc.gridy = 10; formulario.add(new JLabel("Costo Final:"), gbc);
        gbc.gridy = 11; formulario.add(txtCostoFinal, gbc);

        gbc.gridy = 12; formulario.add(new JLabel("Cliente:"), gbc);
        gbc.gridy = 13; formulario.add(cbCliente, gbc);

        gbc.gridy = 14; formulario.add(new JLabel("Salón:"), gbc);
        gbc.gridy = 15; formulario.add(cbSalon, gbc);

        // Botones
        btnAgregar  = new JButton("Agregar");
        btnEditar   = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar  = new JButton("Limpiar");

        btnAgregar.setBackground(new Color(70, 160, 70));
        btnEliminar.setBackground(new Color(200, 60, 60));

        gbc.gridy = 16; formulario.add(btnAgregar, gbc);
        gbc.gridy = 17; formulario.add(btnEditar, gbc);
        gbc.gridy = 18; formulario.add(btnEliminar, gbc);
        gbc.gridy = 19; formulario.add(btnLimpiar, gbc);

        btnAgregar.addActionListener(e  -> agregar());
        btnEditar.addActionListener(e   -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e  -> limpiarFormulario());

        JScrollPane scrollForm = new JScrollPane(formulario);
        scrollForm.setBorder(null);
        scrollForm.setPreferredSize(new Dimension(280, 0));
        scrollForm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollForm, BorderLayout.EAST);
    }

    private void cargarCombos() {
        try {
            clientesList = new repository.ClienteDAO().listar();
            cbCliente.removeAllItems();
            for (Cliente c : clientesList) {
                cbCliente.addItem(c);
            }
            cbCliente.setSelectedIndex(-1);

            salonesList = new repository.SalonDAO().listar();
            cbSalon.removeAllItems();
            for (Salon s : salonesList) {
                cbSalon.addItem(s);
            }
            cbSalon.setSelectedIndex(-1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes/salones: " + ex.getMessage());
        }
    }

    private Cliente buscarClienteEnCache(int id) {
        if (clientesList != null) {
            for (Cliente c : clientesList) {
                if (c.getId() == id) return c;
            }
        }
        return null;
    }

    private Salon buscarSalonEnCache(int id) {
        if (salonesList != null) {
            for (Salon s : salonesList) {
                if (s.getId() == id) return s;
            }
        }
        return null;
    }

    private void seleccionarClienteEnCombo(int id) {
        for (int i = 0; i < cbCliente.getItemCount(); i++) {
            Cliente c = cbCliente.getItemAt(i);
            if (c != null && c.getId() == id) {
                cbCliente.setSelectedIndex(i);
                return;
            }
        }
        cbCliente.setSelectedIndex(-1);
    }

    private void seleccionarSalonEnCombo(int id) {
        for (int i = 0; i < cbSalon.getItemCount(); i++) {
            Salon s = cbSalon.getItemAt(i);
            if (s != null && s.getId() == id) {
                cbSalon.setSelectedIndex(i);
                return;
            }
        }
        cbSalon.setSelectedIndex(-1);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Evento> eventos = controller.listar();
        for (Evento e : eventos) {
            Cliente c = buscarClienteEnCache(e.getClienteId());
            Salon s = buscarSalonEnCache(e.getSalonId());
            modeloTabla.addRow(new Object[]{
                e.getId(),
                e.getTipo(),
                e.getFecha().toString(),
                e.getHorario().toString(),
                e.getCantInvitados(),
                e.getEstado(),
                e.getCostoFinal(),
                c != null ? c : "ID: " + e.getClienteId(),
                s != null ? s : "ID: " + e.getSalonId()
            });
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) { cargarTabla(); return; }
        modeloTabla.setRowCount(0);
        List<Evento> resultados = controller.buscar(texto);
        for (Evento e : resultados) {
            Cliente c = buscarClienteEnCache(e.getClienteId());
            Salon s = buscarSalonEnCache(e.getSalonId());
            modeloTabla.addRow(new Object[]{
                e.getId(),
                e.getTipo(),
                e.getFecha().toString(),
                e.getHorario().toString(),
                e.getCantInvitados(),
                e.getEstado(),
                e.getCostoFinal(),
                c != null ? c : "ID: " + e.getClienteId(),
                s != null ? s : "ID: " + e.getSalonId()
            });
        }
    }

    private void cargarFormularioDesdeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtTipo.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtFecha.setForeground(Color.BLACK);
        txtFecha.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtHorario.setForeground(Color.BLACK);
        txtHorario.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtCantInvitados.setText(modeloTabla.getValueAt(fila, 4).toString());
        
        String estado = modeloTabla.getValueAt(fila, 5).toString();
        cbEstado.setSelectedItem(estado);
        
        txtCostoFinal.setText(modeloTabla.getValueAt(fila, 6).toString());

        // Seleccionar cliente
        Object valCliente = modeloTabla.getValueAt(fila, 7);
        if (valCliente instanceof Cliente) {
            seleccionarClienteEnCombo(((Cliente) valCliente).getId());
        } else {
            cbCliente.setSelectedIndex(-1);
        }

        // Seleccionar salón
        Object valSalon = modeloTabla.getValueAt(fila, 8);
        if (valSalon instanceof Salon) {
            seleccionarSalonEnCombo(((Salon) valSalon).getId());
        } else {
            cbSalon.setSelectedIndex(-1);
        }
    }

    private void agregar() {
        if (!validarCampos()) return;
        Evento e = new Evento();
        e.setTipo(txtTipo.getText().trim());
        e.setFecha(java.time.LocalDate.parse(txtFecha.getText().trim()));
        e.setHorario(java.time.LocalTime.parse(txtHorario.getText().trim()));
        e.setCantInvitados(Integer.parseInt(txtCantInvitados.getText().trim()));
        e.setEstado(cbEstado.getSelectedItem().toString());
        e.setCostoFinal(Double.parseDouble(txtCostoFinal.getText().trim()));
        
        Cliente c = (Cliente) cbCliente.getSelectedItem();
        e.setClienteId(c.getId());
        
        Salon s = (Salon) cbSalon.getSelectedItem();
        e.setSalonId(s.getId());
        
        if (controller.agregar(e)) { cargarTabla(); limpiarFormulario(); }
    }

    private void editar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccioná un evento de la tabla."); return; }
        if (!validarCampos()) return;
        Evento e = new Evento();
        e.setId((int) modeloTabla.getValueAt(fila, 0));
        e.setTipo(txtTipo.getText().trim());
        e.setFecha(java.time.LocalDate.parse(txtFecha.getText().trim()));
        e.setHorario(java.time.LocalTime.parse(txtHorario.getText().trim()));
        e.setCantInvitados(Integer.parseInt(txtCantInvitados.getText().trim()));
        e.setEstado(cbEstado.getSelectedItem().toString());
        e.setCostoFinal(Double.parseDouble(txtCostoFinal.getText().trim()));
        
        Cliente c = (Cliente) cbCliente.getSelectedItem();
        e.setClienteId(c.getId());
        
        Salon s = (Salon) cbSalon.getSelectedItem();
        e.setSalonId(s.getId());
        
        if (controller.actualizar(e)) { cargarTabla(); limpiarFormulario(); }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccioná un evento de la tabla."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (controller.eliminar(id)) { cargarTabla(); limpiarFormulario(); }
    }

    private void limpiarFormulario() {
        txtTipo.setText("");
        txtFecha.setForeground(Color.GRAY);
        txtFecha.setText("YYYY-MM-DD");
        txtHorario.setForeground(Color.GRAY);
        txtHorario.setText("HH:mm:ss");
        txtCantInvitados.setText("");
        cbEstado.setSelectedIndex(0);
        txtCostoFinal.setText("");
        cbCliente.setSelectedIndex(-1);
        cbSalon.setSelectedIndex(-1);
        tabla.clearSelection();
    }

    private boolean validarCampos() {
        String fechaText = txtFecha.getText().trim();
        String horarioText = txtHorario.getText().trim();
        
        if (txtTipo.getText().trim().isEmpty() 
            || fechaText.isEmpty() || fechaText.equals("YYYY-MM-DD")
            || horarioText.isEmpty() || horarioText.equals("HH:mm:ss")
            || txtCantInvitados.getText().trim().isEmpty() 
            || txtCostoFinal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }
        
        if (cbCliente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.");
            return false;
        }
        if (cbSalon.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un salón.");
            return false;
        }

        try {
            Integer.parseInt(txtCantInvitados.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad de invitados debe ser un número entero.");
            return false;
        }

        try {
            Double.parseDouble(txtCostoFinal.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El costo final debe ser un número decimal (ej. 5000.00).");
            return false;
        }

        try {
            java.time.LocalDate.parse(fechaText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener el formato YYYY-MM-DD (ej. 2026-06-15).");
            return false;
        }

        try {
            java.time.LocalTime.parse(horarioText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El horario debe tener el formato HH:mm:ss (ej. 18:30:00).");
            return false;
        }

        return true;
    }
}
