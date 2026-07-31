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
 * Implementa un patrón Maestro-Detalle con Panel Lateral de Detalles.
 */
public class EventosPanel extends JPanel {

    private final EventoController controller = new EventoController();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    // Etiquetas para el panel lateral de detalles
    private JLabel lblDetalleHoraInicio;
    private JLabel lblDetalleHoraFin;
    private JLabel lblDetalleInvitados;
    private JLabel lblDetalleCostoTotal;
    
    private JPanel panelContenedorServicios;

    // Caché de clientes y salones para mostrar sus nombres en la tabla
    private List<Cliente> clientesList;
    private List<Salon> salonesList;
    // Caché de los eventos mostrados para poder acceder rápidamente a todos sus datos
    private List<Evento> eventosActuales;

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
        String[] columnas = {"ID", "Fecha", "Tipo", "Cliente", "Salón", "Estado", "Saldo Pendiente"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ocultar columna ID visualmente
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        // Evento de selección en la tabla para actualizar el Panel Lateral
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarPanelLateral();
            }
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

        // --- PANEL LATERAL DE DETALLES (Este) ---
        JPanel panelDerecho = crearPanelDetalles();
        add(panelDerecho, BorderLayout.EAST);

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

    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(190, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Detalles Extra"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(245, 245, 245));

        lblDetalleHoraInicio = new JLabel("-");
        lblDetalleHoraFin = new JLabel("-");
        lblDetalleInvitados = new JLabel("-");
        lblDetalleCostoTotal = new JLabel("-");

        agregarFilaDetalle(panel, "Hora Inicio:", lblDetalleHoraInicio);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        agregarFilaDetalle(panel, "Hora Fin:", lblDetalleHoraFin);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        agregarFilaDetalle(panel, "Invitados:", lblDetalleInvitados);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Separador para Costo Total
        panel.add(new JSeparator(SwingConstants.HORIZONTAL));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        agregarFilaDetalle(panel, "Costo Total:", lblDetalleCostoTotal);
        lblDetalleCostoTotal.setFont(lblDetalleCostoTotal.getFont().deriveFont(Font.BOLD));
        
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Lista de servicios (en vez de tabla, usamos un panel con scroll)
        JLabel lblServicios = new JLabel("Servicios Contratados:");
        lblServicios.setForeground(Color.GRAY);
        panel.add(lblServicios);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        panelContenedorServicios = new JPanel();
        panelContenedorServicios.setLayout(new BoxLayout(panelContenedorServicios, BoxLayout.Y_AXIS));
        panelContenedorServicios.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollServicios = new JScrollPane(panelContenedorServicios);
        scrollServicios.setPreferredSize(new Dimension(170, 150));
        scrollServicios.setBorder(BorderFactory.createEmptyBorder()); // Sin borde para que se vea más limpio
        scrollServicios.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollServicios);

        return panel;
    }

    private void agregarFilaDetalle(JPanel panel, String titulo, JLabel valorLabel) {
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.GRAY);
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 2)));
        panel.add(valorLabel);
    }

    private void actualizarPanelLateral() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || eventosActuales == null || fila >= eventosActuales.size()) {
            // Limpiar si no hay selección
            lblDetalleHoraInicio.setText("-");
            lblDetalleHoraFin.setText("-");
            lblDetalleInvitados.setText("-");
            lblDetalleCostoTotal.setText("-");
            return;
        }

        int idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        Evento evt = buscarEventoEnCache(idSeleccionado);
        if (evt != null) {
            lblDetalleHoraInicio.setText(evt.getHoraInicio().toString());
            lblDetalleHoraFin.setText(evt.getHoraFin().toString());
            lblDetalleInvitados.setText(String.valueOf(evt.getCantInvitados()));
            lblDetalleCostoTotal.setText("$ " + evt.getCostoTotal());
            
            // Cargar servicios en el panel dinámico
            panelContenedorServicios.removeAll();
            try {
                List<Object[]> servicios = new controller.ReportesControlador().getServiciosContratados(idSeleccionado);
                if (servicios.isEmpty()) {
                    JLabel lblVacio = new JLabel("<html><i>Ninguno</i></html>");
                    lblVacio.setForeground(Color.GRAY);
                    panelContenedorServicios.add(lblVacio);
                } else {
                    for (int i = 0; i < servicios.size(); i++) {
                        Object[] srv = servicios.get(i);
                        String tipo = srv[0].toString();
                        String prov = srv[1].toString();
                        String precio = String.format("%.2f", (Double) srv[2]);
                        
                        String html = "<html><p style='margin:0; padding-bottom:5px;'>"
                                    + "<b>Servicio:</b> " + tipo + "<br>"
                                    + "<b>Proveedor:</b> " + prov + "<br>"
                                    + "<b>Costo:</b> $" + precio 
                                    + "</p></html>";
                                    
                        JLabel lblCard = new JLabel(html);
                        lblCard.setFont(new Font("Arial", Font.PLAIN, 11));
                        
                        // Añadir separador abajo de cada uno, excepto el último
                        if (i < servicios.size() - 1) {
                            lblCard.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                                BorderFactory.createEmptyBorder(0, 0, 5, 0)
                            ));
                        } else {
                            lblCard.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
                        }
                        
                        panelContenedorServicios.add(lblCard);
                        panelContenedorServicios.add(Box.createRigidArea(new Dimension(0, 5)));
                    }
                }
            } catch (Exception ex) {
                // Ignore
            }
            panelContenedorServicios.revalidate();
            panelContenedorServicios.repaint();
        }
    }

    private Evento buscarEventoEnCache(int id) {
        if (eventosActuales != null) {
            for (Evento e : eventosActuales) {
                if (e.getId() == id) return e;
            }
        }
        return null;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        eventosActuales = controller.listar();
        llenarFilas(eventosActuales);
        actualizarPanelLateral(); // Limpia panel al recargar
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) { cargarTabla(); return; }
        modeloTabla.setRowCount(0);
        eventosActuales = controller.buscar(texto);
        llenarFilas(eventosActuales);
        actualizarPanelLateral();
    }

    private void llenarFilas(List<Evento> lista) {
        for (Evento e : lista) {
            Cliente c = buscarClienteEnCache(e.getClienteId());
            Salon s = buscarSalonEnCache(e.getSalonId());
            modeloTabla.addRow(new Object[]{
                e.getId(),
                e.getFecha().toString(),
                e.getTipo(),
                c != null ? c.getNombreApellido() : "ID: " + e.getClienteId(),
                s != null ? s.getNombre() : "ID: " + e.getSalonId(),
                e.getEstado(),
                "$ " + e.getSaldoPendiente()
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

        int idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        Evento e = buscarEventoEnCache(idSeleccionado);
        if (e == null) return;

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
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar el registro seleccionado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            if (controller.eliminar(id)) {
                cargarTabla();
                JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.");
            }
        }
    }
}
