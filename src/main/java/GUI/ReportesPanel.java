package GUI;

import controller.ReportesControlador;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de Reportes para "Eventos Confirmados".
 * Muestra información en una JTable de solo lectura y permite filtrar por Salón.
 */
public class ReportesPanel extends JPanel {

    private final ReportesControlador controller = new ReportesControlador();

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboSalon;
    private JButton btnRefrescar;

    public ReportesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
        cargarDatosIniciales();
    }

    private void initComponents() {
        // --- TÍTULO ---
        JLabel titulo = new JLabel("Reporte de Eventos Confirmados");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        // --- FILTROS (Panel superior) ---
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltros.add(new JLabel("Filtrar por salon:"));

        comboSalon = new JComboBox<>();
        comboSalon.setPreferredSize(new Dimension(180, 25));
        panelFiltros.add(comboSalon);

        btnRefrescar = new JButton("Refrescar");
        panelFiltros.add(btnRefrescar);

        btnRefrescar.addActionListener(e -> refrescar());

        // --- TABLA (Centro) ---
        tabla = new JTable();
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setDefaultEditor(Object.class, null); // Evita la edición por defecto
        
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.add(panelFiltros, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private void cargarDatosIniciales() {
        try {
            // 1. Cargar las columnas de la vista dinámicamente
            String[] columnas = controller.getColumnNames();
            modeloTabla = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false; // Celda no editable
                }
            };
            tabla.setModel(modeloTabla);

            // 2. Cargar Combo Box
            comboSalon.removeAllItems();
            comboSalon.addItem("Todos");
            List<String> salones = controller.getSalones();
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
            JOptionPane.showMessageDialog(this, "Error al cargar datos del reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarEventos() {
        if (modeloTabla == null) return;

        try {
            String seleccionado = (String) comboSalon.getSelectedItem();
            if (seleccionado == null) seleccionado = "Todos";

            List<Object[]> eventos;
            if (seleccionado.equals("Todos")) {
                eventos = controller.getEventosConfirmados();
            } else {
                eventos = controller.getEventosPorSalon(seleccionado);
            }

            cargarTabla(eventos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al filtrar los eventos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescar() {
        try {
            String seleccionado = (String) comboSalon.getSelectedItem();

            // Desactivar temporalmente el listener para evitar múltiples llamadas al refrescar
            java.awt.event.ActionListener[] listeners = comboSalon.getActionListeners();
            for (java.awt.event.ActionListener l : listeners) {
                comboSalon.removeActionListener(l);
            }

            comboSalon.removeAllItems();
            comboSalon.addItem("Todos");
            List<String> salones = controller.getSalones();
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
            JOptionPane.showMessageDialog(this, "Error al refrescar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Llena el modelo de la tabla limpiando previamente el contenido actual.
     * @param filas filas a agregar
     */
    private void cargarTabla(List<Object[]> filas) {
        modeloTabla.setRowCount(0);
        if (filas != null) {
            for (Object[] fila : filas) {
                modeloTabla.addRow(fila);
            }
        }
    }
}
