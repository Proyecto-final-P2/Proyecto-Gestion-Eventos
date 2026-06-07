package app;

import GUI.Login;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// clase principal
public class GestorDeEventos {
    
    // metodo main, lo primero que se ejecuta en el programa
    public static void main(String[] args) {
        try {
            // que la app use el estilo visual de windows
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // si algo falla se ignora y sigue
        }

        // abre la primera pantalla del programa (Login) y la muestra
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
