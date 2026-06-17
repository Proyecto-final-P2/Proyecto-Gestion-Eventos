package GUI;

import controller.ReporteController;
import controller.ReportesControlador;
import model.PagoPorCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de Reportes. Muestra pestañas para diferentes tipos de reportes del sistema.
 */
public class ReportesPanel extends JPanel {

    // Controladores
    private final ReportesControlador confirmadosController = new ReportesControlador();
    private final ReporteController pagosController = new ReporteController();

    // Componentes para Reporte 1: Eventos Confirmados
    private JTable tablaConfirmados;
    private DefaultTableModel modeloConfirmados;
    private JComboBox<String> comboSalon;
    private JButton btnRefrescarConfirmados;

    // Componentes para Reporte 2: Pagos Totales por Cliente
    private JTable tablaPagos;
    private DefaultTableModel modeloPagos;
    private JButton btnRefrescarPagos;

    public ReportesPanel() {
        setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Pestaña 1: Eventos Confirmados
        tabbedPane.addTab("Eventos Confirmados", crearPanelConfirmados());
        
        // Pestaña 2: Pagos por Cliente
        tabbedPane.addTab("Pagos por Cliente", crearPanelPagos());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Cargar datos iniciales
        cargarDatosConfirmados();
        cargarDatosPagos();
    }

    // --- PESTAÑA 1: EVENTOS CONFIRMADOS ---
    private JPanel crearPanelConfirmados() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título
        JLabel titulo = new JLabel("Reporte de Eventos Confirmados");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        // Filtros (Panel superior)
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltros.add(new JLabel("Filtrar por salón:"));

        comboSalon = new JComboBox<>();
        comboSalon.setPreferredSize(new Dimension(180, 25));
        panelFiltros.add(comboSalon);

        btnRefrescarConfirmados = new JButton("Refrescar");
        btnRefrescarConfirmados.addActionListener(e -> refrescarConfirmados());
        panelFiltros.add(btnRefrescarConfirmados);

        // Tabla (Centro)
        tablaConfirmados = new JTable();
        tablaConfirmados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaConfirmados.setDefaultEditor(Object.class, null);

        JScrollPane scroll = new JScrollPane(tablaConfirmados);

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelFiltros, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        panel.add(centro, BorderLayout.CENTER);

        return panel;
    }

    private void cargarDatosConfirmados() {
        try {
            // 1. Cargar las columnas de la vista dinámicamente
            String[] columnas = confirmadosController.getColumnNames();
            modeloConfirmados = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            tablaConfirmados.setModel(modeloConfirmados);

            // 2. Cargar Combo Box
            comboSalon.removeAllItems();
            comboSalon.addItem("Todos");
            List<String> salones = confirmadosController.getSalones();
            if (salones != null) {
                for (String s : salones) {
                    if (s != null) {
                        comboSalon.addItem(s);
                    }
                }
            }
            comboSalon.setSelectedIndex(0);

            // 3. Agregar el listener después de la primera inicialización
            comboSalon.addActionListener(e -> filtrarEventos());

            // 4. Cargar tabla con todos los registros
            filtrarEventos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos del reporte: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarEventos() {
        if (modeloConfirmados == null)
            return;

        try {
            String seleccionado = (String) comboSalon.getSelectedItem();
            if (seleccionado == null)
                seleccionado = "Todos";

            List<Object[]> eventos;
            if (seleccionado.equals("Todos")) {
                eventos = confirmadosController.getEventosConfirmados();
            } else {
                eventos = confirmadosController.getEventosPorSalon(seleccionado);
            }

            cargarTablaConfirmados(eventos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al filtrar los eventos: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescarConfirmados() {
        try {
            String seleccionado = (String) comboSalon.getSelectedItem();

            // Desactivar temporalmente el listener para evitar múltiples llamadas al refrescar
            java.awt.event.ActionListener[] listeners = comboSalon.getActionListeners();
            for (java.awt.event.ActionListener l : listeners) {
                comboSalon.removeActionListener(l);
            }

            comboSalon.removeAllItems();
            comboSalon.addItem("Todos");
            List<String> salones = confirmadosController.getSalones();
            if (salones != null) {
                for (String s : salones) {
                    if (s != null) {
                        comboSalon.addItem(s);
                    }
                }
            }

            if (seleccionado != null) {
                comboSalon.setSelectedItem(seleccionado);
            } else {
                comboSalon.setSelectedIndex(0);
            }

            // Restaurar listeners
            for (java.awt.event.ActionListener l : listeners) {
                comboSalon.addActionListener(l);
            }

            // Recargar datos
            filtrarEventos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al refrescar el reporte: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTablaConfirmados(List<Object[]> filas) {
        modeloConfirmados.setRowCount(0);
        if (filas != null) {
            for (Object[] fila : filas) {
                modeloConfirmados.addRow(fila);
            }
        }
    }

    // --- PESTAÑA 2: PAGOS POR CLIENTE ---
    private JPanel crearPanelPagos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título del reporte
        JLabel lblTitulo = new JLabel("Reporte: Pagos Totales por Cliente", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Configuración de la Tabla (Solo Lectura)
        modeloPagos = new DefaultTableModel(new String[] { "Cliente", "Total Pagado ($)" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPagos = new JTable(modeloPagos);
        JScrollPane scrollPane = new JScrollPane(tablaPagos);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botón para refrescar los datos
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRefrescarPagos = new JButton("Actualizar Reporte");
        btnRefrescarPagos.addActionListener(e -> cargarDatosPagos());
        panelInferior.add(btnRefrescarPagos);
        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarDatosPagos() {
        if (modeloPagos == null) return;
        modeloPagos.setRowCount(0); // Limpiar datos viejos
        List<PagoPorCliente> datos = pagosController.listarPagosPorCliente();

        if (datos.isEmpty()) {
            Object[] filaVacia = { "No hay datos de pagos registrados", 0.0 };
            modeloPagos.addRow(filaVacia);
        } else {
            for (PagoPorCliente p : datos) {
                modeloPagos.addRow(new Object[] { p.getCliente(), p.getTotalPagado() });
            }
        }
    }
}