package GUI;

import controller.EventoController;
import controller.InvitadoController;
import model.Evento;
import model.Invitado;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de Invitados.
 * Sigue el mismo patrón que ClientesPanel.
 */
public class InvitadosPanel extends JPanel {

    private final InvitadoController controller = new InvitadoController();
    private final EventoController eventoController = new EventoController();

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> comboEventos;
    private List<Evento> eventos; // para recuperar el ID del evento seleccionado
    private JTextField txtBuscar;

    public InvitadosPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Gestión de Invitados");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- ZONA SUPERIOR: filtro por evento y buscador ---
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltro.add(new JLabel("Evento:"));
        comboEventos = new JComboBox<>();
        cargarComboEventos();
        panelFiltro.add(comboEventos);

        panelFiltro.add(new JLabel("  |  Buscar por DNI:"));
        txtBuscar = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar todos");
        panelFiltro.add(txtBuscar);
        panelFiltro.add(btnBuscar);
        panelFiltro.add(btnMostrarTodos);

        comboEventos.addActionListener(e -> {
            int idx = comboEventos.getSelectedIndex();
            if (idx >= 0 && eventos != null && !eventos.isEmpty()) {
                if (txtBuscar != null) txtBuscar.setText("");
                cargarTabla(eventos.get(idx).getId());
            }
        });

        btnBuscar.addActionListener(e -> buscar());
        btnMostrarTodos.addActionListener(e -> {
            txtBuscar.setText("");
            int idx = comboEventos.getSelectedIndex();
            if (idx >= 0 && eventos != null && !eventos.isEmpty()) {
                cargarTabla(eventos.get(idx).getId());
            }
        });
        txtBuscar.addActionListener(e -> buscar());

        // --- ZONA CENTRAL: tabla ---
        String[] columnas = {"ID", "DNI", "Nombre y Apellido", "Email", "Asistencia", "Menú"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ocultar columna ID visualmente pero mantenerla en el modelo
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scroll = new JScrollPane(tabla);

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelFiltro, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        // --- ZONA INFERIOR: botones ---
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

        // --- ACCIONES ---
        btnAgregar.addActionListener(e -> abrirFormularioAlta());
        btnModificar.addActionListener(e -> abrirFormularioEdicion());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        // carga inicial
        if (eventos != null && !eventos.isEmpty()) {
            cargarTabla(eventos.get(0).getId());
        }

        // recargar eventos al entrar a la pestaña para ver los eventos recién creados
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                int selectedId = getEventoIdSeleccionado();
                cargarComboEventos();
                if (selectedId >= 0 && eventos != null) {
                    for (int i = 0; i < eventos.size(); i++) {
                        if (eventos.get(i).getId() == selectedId) {
                            comboEventos.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    // llena el combo con todos los eventos de la BD
    private void cargarComboEventos() {
        eventos = eventoController.listar();
        comboEventos.removeAllItems();
        for (Evento ev : eventos) {
            comboEventos.addItem(ev.getTipo() + " - " + ev.getFecha());
        }
    }

    // recarga la tabla con los invitados del evento seleccionado
    private void cargarTabla(int eventoId) {
        List<Invitado> lista = controller.listarPorEvento(eventoId);
        modelo.setRowCount(0);
        for (Invitado inv : lista) {
            modelo.addRow(new Object[]{
                inv.getId(),
                inv.getDni(),
                inv.getNombreApellido(),
                inv.getEmail(),
                inv.getAsistencia(),
                inv.getPreferenciaMenu()
            });
        }
    }

    // devuelve el ID del evento actualmente seleccionado en el combo
    private int getEventoIdSeleccionado() {
        int idx = comboEventos.getSelectedIndex();
        if (idx >= 0 && eventos != null && !eventos.isEmpty()) {
            return eventos.get(idx).getId();
        }
        return -1;
    }

    // abre el formulario vacío para agregar un invitado nuevo
    private void abrirFormularioAlta() {
        int eventoId = getEventoIdSeleccionado();
        if (eventoId < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un evento primero.");
            return;
        }
        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioInvitado form = new FormularioInvitado(frame, controller, eventoId);
        form.setVisible(true);
        cargarTabla(eventoId);
    }

    // abre el formulario pre-cargado con la fila seleccionada para editar
    private void abrirFormularioEdicion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un invitado de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Invitado inv = new Invitado();
        inv.setId((int) modelo.getValueAt(fila, 0));
        inv.setDni(Integer.parseInt(modelo.getValueAt(fila, 1).toString()));
        inv.setNombreApellido(modelo.getValueAt(fila, 2).toString());
        inv.setEmail(modelo.getValueAt(fila, 3).toString());
        inv.setAsistencia(modelo.getValueAt(fila, 4).toString());
        inv.setPreferenciaMenu(modelo.getValueAt(fila, 5).toString());
        inv.setEventoId(getEventoIdSeleccionado());

        Window ventana = SwingUtilities.getWindowAncestor(this);
        JFrame frame = ventana instanceof JFrame ? (JFrame) ventana : null;
        FormularioInvitado form = new FormularioInvitado(frame, controller, inv);
        form.setVisible(true);
        cargarTabla(getEventoIdSeleccionado());
    }

    // elimina el invitado seleccionado previa confirmación
    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un invitado de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar el registro seleccionado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modelo.getValueAt(fila, 0);
            if (controller.eliminar(id)) {
                cargarTabla(getEventoIdSeleccionado());
                JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.");
            }
        }
    }

    // filtra la tabla usando el texto del buscador (DNI)
    private void buscar() {
        String texto = txtBuscar.getText().trim();
        int eventoId = getEventoIdSeleccionado();
        if (eventoId < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un evento primero.");
            return;
        }
        if (texto.isEmpty()) {
            cargarTabla(eventoId);
            return;
        }
        modelo.setRowCount(0);
        List<Invitado> resultados = controller.buscarPorDni(texto, eventoId);
        for (Invitado inv : resultados) {
            modelo.addRow(new Object[]{
                inv.getId(),
                inv.getDni(),
                inv.getNombreApellido(),
                inv.getEmail(),
                inv.getAsistencia(),
                inv.getPreferenciaMenu()
            });
        }
    }
}
