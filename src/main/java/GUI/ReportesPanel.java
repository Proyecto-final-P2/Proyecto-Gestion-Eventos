package GUI;

import controller.ReporteController;
import controller.ReportesControlador;
import model.PagoPorCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
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
    private JButton btnGenerarPdf;

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

        btnGenerarPdf = new JButton("Generar PDF");
        btnGenerarPdf.setEnabled(false);
        btnGenerarPdf.addActionListener(e -> {
            int selectedRow = tablaConfirmados.getSelectedRow();
            if (selectedRow != -1) {
                int columnCount = modeloConfirmados.getColumnCount();
                String[] columnNames = new String[columnCount];
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    columnNames[i] = modeloConfirmados.getColumnName(i);
                    rowData[i] = modeloConfirmados.getValueAt(selectedRow, i);
                }
                
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar reporte de evento");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));
                fileChooser.setSelectedFile(new File("Evento_Confirmado.pdf"));
                
                int userSelection = fileChooser.showSaveDialog(this);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String path = file.getAbsolutePath();
                    if (!path.toLowerCase().endsWith(".pdf")) {
                        file = new File(path + ".pdf");
                    }
                    
                    boolean exito = confirmadosController.generarPdfEvento(columnNames, rowData, file);
                    if (exito) {
                        JOptionPane.showMessageDialog(this, 
                            "PDF generado correctamente en:\n" + file.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Error al generar el PDF.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panelFiltros.add(btnGenerarPdf);

        // Pestaña Detalle Secundaria (abajo)
        JTabbedPane tabbedDetalle = new JTabbedPane();
        tabbedDetalle.setPreferredSize(new Dimension(0, 180));
        tabbedDetalle.setFont(new Font("Arial", Font.BOLD, 12));

        // 1. Pestaña de Servicios Contratados
        String[] colServicios = {"Servicio", "Proveedor", "Precio Pactado"};
        final DefaultTableModel modeloServicios = new DefaultTableModel(colServicios, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tablaServicios = new JTable(modeloServicios);
        tablaServicios.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaServicios.setRowHeight(20);
        JScrollPane scrollServicios = new JScrollPane(tablaServicios);
        tabbedDetalle.addTab("Servicios Contratados", scrollServicios);

        // 2. Pestaña de Lista de Invitados
        String[] colInvitados = {"DNI", "Nombre y Apellido", "Email", "Preferencia Menú", "Asistencia"};
        final DefaultTableModel modeloInvitados = new DefaultTableModel(colInvitados, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tablaInvitados = new JTable(modeloInvitados);
        tablaInvitados.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaInvitados.setRowHeight(20);
        JScrollPane scrollInvitados = new JScrollPane(tablaInvitados);
        tabbedDetalle.addTab("Lista de Invitados", scrollInvitados);

        // Tabla (Centro)
        tablaConfirmados = new JTable();
        tablaConfirmados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaConfirmados.setDefaultEditor(Object.class, null);

        tablaConfirmados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablaConfirmados.getSelectedRow();
                btnGenerarPdf.setEnabled(selectedRow != -1);
                
                // Limpiar tablas de detalle
                modeloServicios.setRowCount(0);
                modeloInvitados.setRowCount(0);
                
                if (selectedRow != -1) {
                    try {
                        Object valId = modeloConfirmados.getValueAt(selectedRow, 0);
                        if (valId != null) {
                            int evId = Integer.parseInt(valId.toString());
                            
                            // Cargar Servicios Contratados
                            List<Object[]> servicios = confirmadosController.getServiciosContratados(evId);
                            if (servicios.isEmpty()) {
                                modeloServicios.addRow(new Object[]{"Sin servicios contratados", "-", "-"});
                            } else {
                                for (Object[] s : servicios) {
                                    modeloServicios.addRow(new Object[]{
                                        s[0], 
                                        s[1], 
                                        "$" + String.format("%.2f", (Double)s[2])
                                    });
                                }
                            }

                            // Cargar Lista de Invitados
                            List<Object[]> invitados = confirmadosController.getInvitadosEvento(evId);
                            if (invitados.isEmpty()) {
                                modeloInvitados.addRow(new Object[]{"-", "Sin invitados registrados", "-", "-", "-"});
                            } else {
                                for (Object[] inv : invitados) {
                                    modeloInvitados.addRow(new Object[]{
                                        inv[0], // DNI
                                        inv[1], // Nombre
                                        inv[2], // Email
                                        inv[3], // PreferenciaMenú
                                        inv[4]  // Asistencia
                                    });
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tablaConfirmados);

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelFiltros, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        centro.add(tabbedDetalle, BorderLayout.SOUTH);
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