package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de Servicios.
 * TODO: implementar siguiendo el mismo patrón que ClientesPanel.
 */
public class ServiciosPanel extends JPanel {
    public ServiciosPanel() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Panel Servicios - En construcción", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl.setForeground(Color.GRAY);
        add(lbl, BorderLayout.CENTER);
    }
}
