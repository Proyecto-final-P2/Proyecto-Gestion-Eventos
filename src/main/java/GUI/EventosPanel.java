package GUI;

import controller.EventoController;
import model.Cliente;
import model.Evento;
import model.Salon;
import repository.ClienteDAO;
import repository.SalonDAO;
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

    // Caché de clientes y salones para mostrar sus nombres en la tabla
    private List<Cliente> clientesList;
    private List<Salon> salonesList;

    public EventosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        cargarCaches();
        initComponents();
        cargarTabla();
    }

    private void cargarCaches() {
        try {
            clientesList = new ClienteDAO().listar();
            salonesList  = new SalonDAO().listar();
        } catch (Exception ignored) {}
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

        // ocultar columna ID visualmente
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

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

        // --- BOTONES (sur) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAgregar   = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar  = new JButton("Eliminar");

        btnAgregar.setBackground(new Color(70, 160, 70));
        btnEliminar.setBackground(new Color(200, 60, 60));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e   -> abrirFormularioAlta());
        btnModificar.addActionListener(e -> abrirFormularioEdicion());
        btnEliminar.addActionListener(e  -> eliminarSeleccionado());
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

    private void abrirFormularioAlta() {
        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioEvento form = new FormularioEvento(frame, controller);
        form.setVisible(true);
        cargarTabla(); // recargar por si agregó
    }

    private void abrirFormularioEdicion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un evento de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Evento e = new Evento();
        e.setId((int) modeloTabla.getValueAt(fila, 0));
        e.setTipo(modeloTabla.getValueAt(fila, 1).toString());
        e.setFecha(java.time.LocalDate.parse(modeloTabla.getValueAt(fila, 2).toString()));
        e.setHorario(java.time.LocalTime.parse(modeloTabla.getValueAt(fila, 3).toString()));
        e.setCantInvitados(Integer.parseInt(modeloTabla.getValueAt(fila, 4).toString()));
        e.setEstado(modeloTabla.getValueAt(fila, 5).toString());
        e.setCostoFinal(Double.parseDouble(modeloTabla.getValueAt(fila, 6).toString()));

        Object valCliente = modeloTabla.getValueAt(fila, 7);
        if (valCliente instanceof Cliente) e.setClienteId(((Cliente) valCliente).getId());

        Object valSalon = modeloTabla.getValueAt(fila, 8);
        if (valSalon instanceof Salon) e.setSalonId(((Salon) valSalon).getId());

        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioEvento form = new FormularioEvento(frame, controller, e);
        form.setVisible(true);
        cargarTabla();
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un evento de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (controller.eliminar(id)) cargarTabla();
    }
}
