package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de Reportes.
 * TODO: implementar siguiendo el mismo patrón que ClientesPanel.
 */
public class ReportesPanel extends JPanel {
    public ReportesPanel() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Panel Reportes - En construcción", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl.setForeground(Color.GRAY);
        add(lbl, BorderLayout.CENTER);
    }
}
