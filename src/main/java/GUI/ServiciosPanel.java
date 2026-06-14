package GUI;

import controller.ServicioController;
import model.Servicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServiciosPanel extends JPanel {

    private ServicioController controller;

    // Componentes de la Tabla
    private JTable tablaServicios;
    private DefaultTableModel modeloTabla;

    // Componentes del Formulario
    private JTextField txtId;
    private JTextField txtTipo;
    private JTextField txtProveedor;
    private JTextField txtCosto;
    private JTextField txtCantidad;
    private JComboBox<String> cmbEstado; // Requisito del Sprint

    // Botones
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public ServiciosPanel() {
        controller = new ServicioController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarFormulario();
        inicializarTabla();
        cargarTabla();
    }

    private void inicializarFormulario() {
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setPreferredSize(new Dimension(300, 0));

        JPanel panelForm = new JPanel(new GridLayout(7, 2, 5, 15));
        panelForm.setBorder(BorderFactory.createTitledBorder("Gestión de Servicio"));

        // Inicializar campos
        txtId = new JTextField();
        txtTipo = new JTextField();
        txtProveedor = new JTextField();
        txtCosto = new JTextField();
        txtCantidad = new JTextField();
        
        // Requisito: JComboBox con los estados específicos
        cmbEstado = new JComboBox<>(new String[]{"confirmado", "pendiente de confirmacion", "cancelado"});

        // Agregar al panel
        panelForm.add(new JLabel("ID (Solo para guardar):")); panelForm.add(txtId);
        panelForm.add(new JLabel("Tipo:")); panelForm.add(txtTipo);
        panelForm.add(new JLabel("Proveedor:")); panelForm.add(txtProveedor);
        panelForm.add(new JLabel("Costo ($):")); panelForm.add(txtCosto);
        panelForm.add(new JLabel("Cantidad:")); panelForm.add(txtCantidad);
        panelForm.add(new JLabel("Estado:")); panelForm.add(cmbEstado);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelForm.add(new JLabel()); // Espacio vacío
        panelIzquierdo.add(panelForm, BorderLayout.NORTH);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        add(panelIzquierdo, BorderLayout.WEST);

        // --- EVENTOS DE LOS BOTONES ---
        btnGuardar.addActionListener(e -> guardarServicio());
        btnActualizar.addActionListener(e -> actualizarServicio());
        btnEliminar.addActionListener(e -> eliminarServicio());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void inicializarTabla() {
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Tipo", "Proveedor", "Costo", "Cantidad", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita que editen la tabla haciendo doble clic
            }
        };
        tablaServicios = new JTable(modeloTabla);
        
        // Al hacer clic en la tabla, se pasan los datos al formulario para poder editar
        tablaServicios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tablaServicios.getSelectedRow();
                if (fila != -1) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtTipo.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtProveedor.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtCosto.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtCantidad.setText(modeloTabla.getValueAt(fila, 4).toString());
                    cmbEstado.setSelectedItem(modeloTabla.getValueAt(fila, 5).toString());
                    txtId.setEnabled(false); // No se debe cambiar el ID al actualizar
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaServicios);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Servicio> lista = controller.listarServicios();
        for (Servicio s : lista) {
            modeloTabla.addRow(new Object[]{s.getId(), s.getTipo(), s.getProveedor(), s.getCosto(), s.getCantidad(), s.getEstado()});
        }
    }

    private void guardarServicio() {
        if (!validarCamposVacios()) return;

        try {
            // Requisito: Validaciones numéricas
            int id = Integer.parseInt(txtId.getText().trim());
            double costo = Double.parseDouble(txtCosto.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (costo < 0 || cantidad < 0) {
                JOptionPane.showMessageDialog(this, "El costo y la cantidad no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Servicio s = new Servicio(id, txtTipo.getText().trim(), txtProveedor.getText().trim(), costo, cantidad, (String) cmbEstado.getSelectedItem());
            
            if (controller.guardarServicio(s)) {
                JOptionPane.showMessageDialog(this, "Servicio guardado exitosamente.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID, Costo y Cantidad deben ser números válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarServicio() {
        if (!validarCamposVacios()) return;

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            double costo = Double.parseDouble(txtCosto.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (costo < 0 || cantidad < 0) {
                JOptionPane.showMessageDialog(this, "El costo y la cantidad no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Servicio s = new Servicio(id, txtTipo.getText().trim(), txtProveedor.getText().trim(), costo, cantidad, (String) cmbEstado.getSelectedItem());
            
            if (controller.actualizarServicio(s)) {
                JOptionPane.showMessageDialog(this, "Servicio actualizado exitosamente.");
                limpiarCampos();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar en la base de datos.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID, Costo y Cantidad deben ser números válidos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarServicio() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este servicio?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                if (controller.eliminarServicio(id)) {
                    JOptionPane.showMessageDialog(this, "Servicio eliminado.");
                    limpiarCampos();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar de la base de datos.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarCamposVacios() {
        if (txtId.getText().trim().isEmpty() || txtTipo.getText().trim().isEmpty() || txtProveedor.getText().trim().isEmpty() || txtCosto.getText().trim().isEmpty() || txtCantidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtTipo.setText("");
        txtProveedor.setText("");
        txtCosto.setText("");
        txtCantidad.setText("");
        cmbEstado.setSelectedIndex(0);
        txtId.setEnabled(true);
        tablaServicios.clearSelection();
    }
}
