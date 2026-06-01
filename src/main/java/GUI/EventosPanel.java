package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de Eventos.
 * TODO: implementar siguiendo el mismo patrón que ClientesPanel.
 */
public class EventosPanel extends JPanel {
    public EventosPanel() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Panel Eventos - En construcción", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl.setForeground(Color.GRAY);
        add(lbl, BorderLayout.CENTER);
    }
}
