package GUI;

import controller.ServicioController;
import model.Servicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServiciosPanel extends JPanel {

    private ServicioController controller;
    private JTable tablaServicios;
    private DefaultTableModel modeloTabla;
    
    // Variable interna para guardar el ID cuando editamos (sin mostrarlo en pantalla)
    private int idServicioSeleccionado = -1;

    public ServiciosPanel() {
        controller = new ServicioController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarTabla();
        inicializarBotones();
        cargarTabla();
    }

    private void inicializarTabla() {
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Tipo", "Proveedor", "Costo", "Cantidad", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tablaServicios = new JTable(modeloTabla);
        
        // Ocultamos la columna ID de la vista de la tabla, pero mantenemos los datos ocultos
        tablaServicios.getColumnModel().getColumn(0).setMinWidth(0);
        tablaServicios.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaServicios.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(tablaServicios);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void inicializarBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnNuevo = new JButton("Nuevo Servicio");
        JButton btnEditar = new JButton("Editar Servicio");
        JButton btnEliminar = new JButton("Eliminar");

        btnNuevo.addActionListener(e -> mostrarVentanaABM(false));
        
        btnEditar.addActionListener(e -> {
            int fila = tablaServicios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un servicio de la tabla para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Guardamos el ID internamente
            idServicioSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            mostrarVentanaABM(true);
        });

        btnEliminar.addActionListener(e -> eliminarServicio());

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // --- MODO VENTANA (JDialog) ---
    private void mostrarVentanaABM(boolean esEdicion) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, esEdicion ? "Editar Servicio" : "Nuevo Servicio", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 15));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Catering", "DJ", "Decoración", "Sonido", "Iluminación", "Fotografía", "Seguridad", "Otro"});
        JTextField txtProveedor = new JTextField();
        JTextField txtCosto = new JTextField();
        JTextField txtCantidad = new JTextField();
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Disponible", "No disponible"});

        // Si es edición, cargamos los datos de la fila seleccionada en la ventanita
        if (esEdicion) {
            int fila = tablaServicios.getSelectedRow();
            cmbTipo.setSelectedItem(modeloTabla.getValueAt(fila, 1).toString());
            txtProveedor.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtCosto.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtCantidad.setText(modeloTabla.getValueAt(fila, 4).toString());
            cmbEstado.setSelectedItem(modeloTabla.getValueAt(fila, 5).toString());
        }

        // NO agregamos el campo ID a la vista
        panelForm.add(new JLabel("Tipo:")); panelForm.add(cmbTipo);
        panelForm.add(new JLabel("Proveedor:")); panelForm.add(txtProveedor);
        panelForm.add(new JLabel("Costo ($):")); panelForm.add(txtCosto);
        panelForm.add(new JLabel("Cantidad:")); panelForm.add(txtCantidad);
        panelForm.add(new JLabel("Estado:")); panelForm.add(cmbEstado);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            if (txtProveedor.getText().trim().isEmpty() || txtCosto.getText().trim().isEmpty() || txtCantidad.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Complete todos los campos.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                double costo = Double.parseDouble(txtCosto.getText().trim());
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());

                if (costo < 0 || cantidad < 0) {
                    JOptionPane.showMessageDialog(dialog, "El costo y la cantidad no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Si es nuevo, mandamos ID 0. Si es edición, mandamos el ID guardado.
                int idGuardar = esEdicion ? idServicioSeleccionado : 0;
                Servicio s = new Servicio(idGuardar, (String) cmbTipo.getSelectedItem(), txtProveedor.getText().trim(), costo, cantidad, (String) cmbEstado.getSelectedItem());

                boolean exito = esEdicion ? controller.actualizarServicio(s) : controller.guardarServicio(s);

                if (exito) {
                    JOptionPane.showMessageDialog(dialog, "Operación exitosa.");
                    dialog.dispose(); // Cierra la ventanita
                    cargarTabla(); // Refresca la tabla
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error en la base de datos.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Costo y Cantidad deben ser números válidos.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel panelSur = new JPanel();
        panelSur.add(btnGuardar);

        dialog.add(panelForm, BorderLayout.CENTER);
        dialog.add(panelSur, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0); 
        List<Servicio> lista = controller.listarServicios();
        for (Servicio s : lista) {
            modeloTabla.addRow(new Object[]{s.getId(), s.getTipo(), s.getProveedor(), s.getCosto(), s.getCantidad(), s.getEstado()});
        }
    }

    private void eliminarServicio() {
        int fila = tablaServicios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este servicio?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            int idEliminar = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            if (controller.eliminarServicio(idEliminar)) {
                JOptionPane.showMessageDialog(this, "Servicio eliminado.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar de la base de datos.");
            }
        }
    }
}