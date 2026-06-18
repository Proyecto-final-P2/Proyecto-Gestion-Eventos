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

            // Traducir los botones de los pop-ups a español para todo el sistema
            UIManager.put("OptionPane.yesButtonText", "Sí");
            UIManager.put("OptionPane.noButtonText", "No");
            UIManager.put("OptionPane.cancelButtonText", "Cancelar");
            UIManager.put("OptionPane.okButtonText", "Aceptar");
        } catch (Exception e) {
            // si algo falla se muestra el error
            System.out.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace(); // mostrar el stack trace completo del error
        }

        // abre la primera pantalla del programa (Login) y la muestra
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
