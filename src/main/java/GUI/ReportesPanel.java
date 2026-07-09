package GUI;

import controller.ReporteController;
import java.awt.*;
import util.PdfService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

/**
 * Panel de Reportes. Muestra pestañas para diferentes tipos de reportes del sistema.
 */
public class ReportesPanel extends JPanel {

    // Controlador Único
    private final ReporteController controller = new ReporteController();

    // Componentes Operativa
    private JTable tablaConfirmados;
    private DefaultTableModel modeloConfirmados;
    private JComboBox<String> comboSalon;
    private JButton btnGenerarPdf;
    private DefaultTableModel modeloServicios;
    private DefaultTableModel modeloInvitados;

    // Componentes Finanzas
    private JTable tablaPagos;
    private DefaultTableModel modeloPagos;
    private JTable tablaEventosCostosos;
    private DefaultTableModel modeloEventosCostosos;

    // Componentes Estadísticas
    private JTable tablaServiciosTop;
    private DefaultTableModel modeloServiciosTop;
    private JTable tablaSalonesTop;
    private DefaultTableModel modeloSalonesTop;

    public ReportesPanel() {
        setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        tabbedPane.addTab("Eventos", crearPanelConfirmados());
        tabbedPane.addTab("Pagos y Costos", crearPanelFinanzas());
        tabbedPane.addTab("Estadísticas", crearPanelEstadisticas());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Cargar datos
        cargarDatosConfirmados();
        cargarDatosFinanzas();
        cargarDatosEstadisticas();
    }

    // ================== PESTAÑA 1: OPERATIVA ==================
    private JPanel crearPanelConfirmados() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Eventos Confirmados");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltros.add(new JLabel("Filtrar por salón:"));

        comboSalon = new JComboBox<>();
        comboSalon.setPreferredSize(new Dimension(180, 25));
        panelFiltros.add(comboSalon);

        JButton btnRefrescarConfirmados = new JButton("Actualizar");
        btnRefrescarConfirmados.addActionListener(e -> refrescarConfirmados());
        panelFiltros.add(btnRefrescarConfirmados);

        btnGenerarPdf = new JButton("Exportar a PDF");
        btnGenerarPdf.setEnabled(false);
        btnGenerarPdf.addActionListener(e -> generarPdf());
        panelFiltros.add(btnGenerarPdf);

        JTabbedPane tabbedDetalle = new JTabbedPane();
        tabbedDetalle.setPreferredSize(new Dimension(0, 180));
        
        modeloServicios = new DefaultTableModel(new String[]{"Servicio", "Proveedor", "Precio Pactado"}, 0);
        JTable tablaServicios = new JTable(modeloServicios);
        tabbedDetalle.addTab("Servicios Contratados", new JScrollPane(tablaServicios));

        modeloInvitados = new DefaultTableModel(new String[]{"DNI", "Nombre y Apellido", "Email", "Preferencia Menú", "Asistencia"}, 0);
        JTable tablaInvitados = new JTable(modeloInvitados);
        tabbedDetalle.addTab("Lista de Invitados", new JScrollPane(tablaInvitados));

        tablaConfirmados = new JTable();
        tablaConfirmados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaConfirmados.setDefaultEditor(Object.class, null);

        tablaConfirmados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablaConfirmados.getSelectedRow();
                btnGenerarPdf.setEnabled(selectedRow != -1);
                modeloServicios.setRowCount(0);
                modeloInvitados.setRowCount(0);
                
                if (selectedRow != -1) {
                    cargarDetallesEvento(selectedRow);
                }
            }
        });

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelFiltros, BorderLayout.NORTH);
        centro.add(new JScrollPane(tablaConfirmados), BorderLayout.CENTER);
        centro.add(tabbedDetalle, BorderLayout.SOUTH);
        panel.add(centro, BorderLayout.CENTER);

        return panel;
    }

    private void cargarDetallesEvento(int selectedRow) {
        try {
            Object valId = modeloConfirmados.getValueAt(selectedRow, 0);
            if (valId != null) {
                int evId = Integer.parseInt(valId.toString());
                
                List<Object[]> servicios = controller.getServiciosContratados(evId);
                for (Object[] s : servicios) {
                    modeloServicios.addRow(new Object[]{s[0], s[1], "$" + String.format("%.2f", (Double)s[2])});
                }

                List<Object[]> invitados = controller.getInvitadosEvento(evId);
                for (Object[] inv : invitados) {
                    modeloInvitados.addRow(inv);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void generarPdf() {
        int selectedRow = tablaConfirmados.getSelectedRow();
        if (selectedRow == -1) return;

        int columnCount = modeloConfirmados.getColumnCount();
        String[] columnNames = new String[columnCount];
        Object[] rowData = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = modeloConfirmados.getColumnName(i);
            rowData[i] = modeloConfirmados.getValueAt(selectedRow, i);
        }

        int evId = Integer.parseInt(rowData[0].toString());
        List<Object[]> servicios = controller.getServiciosContratados(evId);
        List<Object[]> invitados = controller.getInvitadosEvento(evId);
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte de evento");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));
        fileChooser.setSelectedFile(new File("Evento_Confirmado.pdf"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
                file = new File(file.getAbsolutePath() + ".pdf");
            }
            
            boolean exito = PdfService.generarPdfEvento(columnNames, rowData, file, servicios, invitados);
            if (exito) {
                JOptionPane.showMessageDialog(this, "PDF generado en:\n" + file.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this, "Error al generar el PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarDatosConfirmados() {
        try {
            String[] columnas = controller.getColumnNames();
            modeloConfirmados = new DefaultTableModel(columnas, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            tablaConfirmados.setModel(modeloConfirmados);

            comboSalon.removeAllItems();
            comboSalon.addItem("Todos");
            for (String s : controller.getSalones()) {
                if (s != null) comboSalon.addItem(s);
            }

            comboSalon.addActionListener(e -> filtrarEventos());
            filtrarEventos();
        } catch (Exception ex) {}
    }

    private void filtrarEventos() {
        if (modeloConfirmados == null) return;
        String seleccionado = (String) comboSalon.getSelectedItem();
        List<Object[]> eventos = (seleccionado == null || seleccionado.equals("Todos")) ? 
            controller.getEventosConfirmados() : controller.getEventosPorSalon(seleccionado);
        
        modeloConfirmados.setRowCount(0);
        for (Object[] e : eventos) modeloConfirmados.addRow(e);
    }

    private void refrescarConfirmados() {
        String seleccionado = (String) comboSalon.getSelectedItem();
        cargarDatosConfirmados();
        if (seleccionado != null) comboSalon.setSelectedItem(seleccionado);
    }

    // ================== PESTAÑA 2: PAGOS Y COSTOS ==================
    private JPanel crearPanelFinanzas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Reporte de Pagos y Costos Adicionales");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnRefrescar = new JButton("Actualizar");
        btnRefrescar.addActionListener(e -> cargarDatosFinanzas());
        panelAcciones.add(btnRefrescar);
        
        JButton btnPdf = new JButton("Exportar a PDF");
        btnPdf.addActionListener(e -> generarPdfFinanzas());
        panelAcciones.add(btnPdf);

        JPanel gridTablas = new JPanel(new GridLayout(2, 1, 10, 10));
        
        // Arriba: Pagos por Evento
        JPanel panelPagos = new JPanel(new BorderLayout(5, 5));
        panelPagos.setBorder(BorderFactory.createTitledBorder("Pagos Totales por Evento (por Pagador)"));
        modeloPagos = new DefaultTableModel(new String[]{"ID Evento", "Tipo Evento", "Pagador", "Total Aportado ($)", "Costo Total Evento ($)"}, 0);
        tablaPagos = new JTable(modeloPagos);
        panelPagos.add(new JScrollPane(tablaPagos), BorderLayout.CENTER);

        // Abajo: Eventos Costosos
        JPanel panelCostosos = new JPanel(new BorderLayout(5, 5));
        panelCostosos.setBorder(BorderFactory.createTitledBorder("Eventos con Costo de Servicios Extra > $3000"));
        modeloEventosCostosos = new DefaultTableModel(new String[]{"ID Evento", "Tipo", "Cliente", "Monto Salón ($)", "Monto Servicios ($)", "Costo Total ($)"}, 0);
        tablaEventosCostosos = new JTable(modeloEventosCostosos);
        panelCostosos.add(new JScrollPane(tablaEventosCostosos), BorderLayout.CENTER);

        gridTablas.add(panelPagos);
        gridTablas.add(panelCostosos);

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelAcciones, BorderLayout.NORTH);
        centro.add(gridTablas, BorderLayout.CENTER);
        panel.add(centro, BorderLayout.CENTER);

        return panel;
    }

    private void cargarDatosFinanzas() {
        modeloPagos.setRowCount(0);
        for (Object[] p : controller.listarPagosPorEvento()) {
            modeloPagos.addRow(p);
        }

        modeloEventosCostosos.setRowCount(0);
        for (Object[] e : controller.listarEventosCostosos()) {
            modeloEventosCostosos.addRow(e);
        }
    }
    
    private void generarPdfFinanzas() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte de pagos y costos");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));
        fileChooser.setSelectedFile(new File("Reporte_Pagos_Costos.pdf"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getAbsolutePath().toLowerCase().endsWith(".pdf")) file = new File(file.getAbsolutePath() + ".pdf");
            
            List<String> subtitulos = java.util.Arrays.asList("Pagos Totales por Evento", "Eventos Costosos en Servicios Extra");
            List<String[]> columnas = java.util.Arrays.asList(
                new String[]{"ID", "Tipo", "Pagador", "Total Aportado", "Costo Evento"},
                new String[]{"ID", "Tipo", "Cliente", "Monto Salon", "Monto Serv.", "Total"}
            );
            List<List<Object[]>> filas = java.util.Arrays.asList(
                controller.listarPagosPorEvento(),
                controller.listarEventosCostosos()
            );
            
            boolean exito = PdfService.generarPdfReporteMultiTabla("Reporte de Pagos y Costos", subtitulos, columnas, filas, file);
            if (exito) JOptionPane.showMessageDialog(this, "PDF generado con éxito.");
            else JOptionPane.showMessageDialog(this, "Error al generar el PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================== PESTAÑA 3: ESTADÍSTICAS ==================
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Estadísticas del Negocio");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnRefrescar = new JButton("Actualizar");
        btnRefrescar.addActionListener(e -> cargarDatosEstadisticas());
        panelAcciones.add(btnRefrescar);
        
        JButton btnPdf = new JButton("Exportar a PDF");
        btnPdf.addActionListener(e -> generarPdfEstadisticas());
        panelAcciones.add(btnPdf);

        JPanel gridTablas = new JPanel(new GridLayout(2, 1, 10, 10));

        // Arriba: Servicios Top
        JPanel panelServicios = new JPanel(new BorderLayout(5, 5));
        panelServicios.setBorder(BorderFactory.createTitledBorder("Servicios más solicitados por Tipo de Evento"));
        modeloServiciosTop = new DefaultTableModel(new String[]{"Tipo de Evento", "Cant. Servicios Contratados"}, 0);
        tablaServiciosTop = new JTable(modeloServiciosTop);
        panelServicios.add(new JScrollPane(tablaServiciosTop), BorderLayout.CENTER);

        // Abajo: Salones más solicitados
        JPanel panelSalones = new JPanel(new BorderLayout(5, 5));
        panelSalones.setBorder(BorderFactory.createTitledBorder("Salones más solicitados"));
        modeloSalonesTop = new DefaultTableModel(new String[]{"ID Salón", "Nombre", "Dirección", "Cant. Eventos"}, 0);
        tablaSalonesTop = new JTable(modeloSalonesTop);
        panelSalones.add(new JScrollPane(tablaSalonesTop), BorderLayout.CENTER);

        gridTablas.add(panelServicios);
        gridTablas.add(panelSalones);

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelAcciones, BorderLayout.NORTH);
        centro.add(gridTablas, BorderLayout.CENTER);
        panel.add(centro, BorderLayout.CENTER);

        return panel;
    }

    private void cargarDatosEstadisticas() {
        modeloServiciosTop.setRowCount(0);
        for (Object[] s : controller.listarServiciosPorTipo()) {
            modeloServiciosTop.addRow(s);
        }

        modeloSalonesTop.setRowCount(0);
        for (Object[] c : controller.listarSalonesMasSolicitados()) {
            modeloSalonesTop.addRow(c);
        }
    }
    
    private void generarPdfEstadisticas() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte de estadísticas");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));
        fileChooser.setSelectedFile(new File("Reporte_Estadisticas.pdf"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getAbsolutePath().toLowerCase().endsWith(".pdf")) file = new File(file.getAbsolutePath() + ".pdf");
            
            List<String> subtitulos = java.util.Arrays.asList("Servicios más solicitados por Tipo de Evento", "Salones más solicitados");
            List<String[]> columnas = java.util.Arrays.asList(
                new String[]{"Tipo de Evento", "Cant. Servicios"},
                new String[]{"ID Salón", "Nombre", "Dirección", "Cant. Eventos"}
            );
            List<List<Object[]>> filas = java.util.Arrays.asList(
                controller.listarServiciosPorTipo(),
                controller.listarSalonesMasSolicitados()
            );
            
            boolean exito = PdfService.generarPdfReporteMultiTabla("Estadísticas del Negocio", subtitulos, columnas, filas, file);
            if (exito) JOptionPane.showMessageDialog(this, "PDF generado con éxito.");
            else JOptionPane.showMessageDialog(this, "Error al generar el PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}