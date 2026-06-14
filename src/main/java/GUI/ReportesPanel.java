package GUI;

import controller.ReporteController;
import model.PagoPorCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportesPanel extends JPanel {

    private ReporteController controller;
    private JTable tablaReporte;
    private DefaultTableModel modeloTabla;
    private JButton btnRefrescar;

    public ReportesPanel() {
        controller = new ReporteController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        cargarReporte();
    }

    private void inicializarComponentes() {
        // Título del reporte
        JLabel lblTitulo = new JLabel("Reporte: Pagos Totales por Cliente", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        // Configuración de la Tabla (Solo Lectura)
        modeloTabla = new DefaultTableModel(new String[]{"Cliente", "Total Pagado ($)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Vista de solo lectura
            }
        };
        tablaReporte = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaReporte);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botón para refrescar los datos
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRefrescar = new JButton("Actualizar Reporte");
        btnRefrescar.addActionListener(e -> cargarReporte());
        panelInferior.add(btnRefrescar);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarReporte() {
        modeloTabla.setRowCount(0); // Limpiar datos viejos
        List<PagoPorCliente> datos = controller.listarPagosPorCliente();
        
        if (datos.isEmpty()) {
            // Por si la vista no devuelve filas o hay error de conexión
            Object[] filaVacia = {"No hay datos de pagos registrados", 0.0};
            modeloTabla.addRow(filaVacia);
        } else {
            for (PagoPorCliente p : datos) {
                modeloTabla.addRow(new Object[]{p.getCliente(), p.getTotalPagado()});
            }
        }
    }
}