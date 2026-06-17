package GUI;

import controller.ReportesControlador;
import model.PagoPorCliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportesPanel extends JPanel {

    private final ReportesControlador controller = new ReportesControlador();
    private JTabbedPane pestanas;
    
    // Modelos para las tablas
    private DefaultTableModel modeloPagos;
    private DefaultTableModel modeloEventos;
    private JComboBox<String> comboSalon;

    public ReportesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Módulo de Reportes", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        pestanas = new JTabbedPane();

        pestanas.addTab("Eventos Confirmados", crearPanelEventos());
        pestanas.addTab("Pagos Totales", crearPanelPagos());

        add(pestanas, BorderLayout.CENTER);

        // Carga inicial
        cargarDatosPagos();
        cargarDatosEventos();
        cargarSalones();
    }

    // --- LÓGICA DE EVENTOS ---
    private JPanel crearPanelEventos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        
        // Inicializar modelo con nombres de columnas dinámicos
        modeloEventos = new DefaultTableModel(controller.getColumnNames(), 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        JTable tablaEventos = new JTable(modeloEventos);
        comboSalon = new JComboBox<>();
        JButton btnRefrescar = new JButton("Refrescar");

        btnRefrescar.addActionListener(e -> cargarDatosEventos());

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.add(new JLabel("Filtrar por salón:"));
        panelFiltros.add(comboSalon);
        panelFiltros.add(btnRefrescar);

        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaEventos), BorderLayout.CENTER);
        return panel;
    }

    // --- LÓGICA DE PAGOS ---
    private JPanel crearPanelPagos() {
        String[] columnas = {"Cliente", "Total Pagado ($)"};
        modeloPagos = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        JTable tablaReporte = new JTable(modeloPagos);
        JButton btnRefrescar = new JButton("Actualizar");
        btnRefrescar.addActionListener(e -> cargarDatosPagos());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(tablaReporte), BorderLayout.CENTER);
        panel.add(btnRefrescar, BorderLayout.SOUTH);
        return panel;
    }

    // --- CARGA DE DATOS ---
    private void cargarSalones() {
        comboSalon.removeAllItems();
        comboSalon.addItem("Todos");
        for (String s : controller.getSalones()) {
            comboSalon.addItem(s);
        }
    }

    private void cargarDatosEventos() {
        modeloEventos.setRowCount(0);
        String salonSeleccionado = (String) comboSalon.getSelectedItem();
        List<Object[]> lista;

        if (salonSeleccionado == null || salonSeleccionado.equals("Todos")) {
            lista = controller.getEventosConfirmados();
        } else {
            lista = controller.getEventosPorSalon(salonSeleccionado);
        }

        if (lista != null) {
            for (Object[] fila : lista) {
                modeloEventos.addRow(fila);
            }
        }
    }

    private void cargarDatosPagos() {
        modeloPagos.setRowCount(0);
        List<PagoPorCliente> lista = controller.listarPagosPorCliente();
        if (lista != null) {
            for (PagoPorCliente p : lista) {
                modeloPagos.addRow(new Object[]{p.getCliente(), p.getTotalPagado()});
            }
        }
    }
}