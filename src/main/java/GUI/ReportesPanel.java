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
    // Tabla para tu reporte de Pagos
    private JTable tablaReporte; 

    public ReportesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Módulo de Reportes", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        pestanas = new JTabbedPane();

        // 1. Pestaña de eventos (La lógica original de tu compañero)
        pestanas.addTab("Eventos Confirmados", crearPanelEventos());

        // 2. Tu pestaña (Pagos Totales)
        pestanas.addTab("Pagos Totales", crearPanelPagos());

        add(pestanas, BorderLayout.CENTER);
    }

    // --- LÓGICA DE EVENTOS (Tu compañero) ---
    private JPanel crearPanelEventos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        // Aquí mantendrías los filtros de tu compañero si los necesitas
        JLabel lblInfo = new JLabel("Panel de eventos integrado desde develop", SwingConstants.CENTER);
        panel.add(lblInfo, BorderLayout.CENTER);
        return panel;
    }

    // --- TU LÓGICA (Pagos Totales) ---
    private JPanel crearPanelPagos() {
        String[] columnas = {"Cliente", "Total Pagado ($)"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tablaReporte = new JTable(modelo); // Inicializamos la tabla que definimos arriba
        
        JButton btnRefrescar = new JButton("Actualizar");
        btnRefrescar.addActionListener(e -> {
            modelo.setRowCount(0);
            // Asegurate que en ReportesControlador exista el método listarPagosPorCliente()
            List<PagoPorCliente> lista = controller.listarPagosPorCliente();
            if (lista != null) {
                for (PagoPorCliente p : lista) {
                    modelo.addRow(new Object[]{p.getCliente(), p.getTotalPagado()});
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(tablaReporte), BorderLayout.CENTER);
        panel.add(btnRefrescar, BorderLayout.SOUTH);
        return panel;
    }
}