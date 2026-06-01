package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de Invitados.
 * TODO: implementar siguiendo el mismo patrón que ClientesPanel.
 */
public class InvitadosPanel extends JPanel {
    public InvitadosPanel() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Panel Invitados - En construcción", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        lbl.setForeground(Color.GRAY);
        add(lbl, BorderLayout.CENTER);
    }
}
