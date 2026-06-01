package app;

import GUI.Login;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class GestorDeEventos {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
