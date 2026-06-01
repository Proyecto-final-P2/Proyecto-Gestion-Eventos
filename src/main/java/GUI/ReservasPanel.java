package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de Reservas.
 * TODO: implementar siguiendo el mismo patrón que ClientesPanel.
 */
public class ReservasPanel extends JPanel {
    public ReservasPanel() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Panel Reservas - En construcción", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl.setForeground(Color.GRAY);
        add(lbl, BorderLayout.CENTER);
    }
}
