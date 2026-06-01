package controller;

import model.Cliente;
import repository.ClienteDAO;
import GUI.MenuPrincipal;
import GUI.Login;
import javax.swing.JOptionPane;
import java.util.List;

public class LoginController {

    private final Login vista;
    private final ClienteDAO dao = new ClienteDAO();

    public LoginController(Login vista) {
        this.vista = vista;
    }

    public void iniciarSesion(String email, String password) {
        try {
            List<Cliente> clientes = dao.listar();
            boolean encontrado = clientes.stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email));

            if (encontrado) {
                Cliente cliente = clientes.stream()
                    .filter(c -> c.getEmail().equalsIgnoreCase(email))
                    .findFirst().orElse(null);
                vista.dispose();
                new MenuPrincipal(cliente).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(vista,
                    "Email o contraseña incorrectos.",
                    "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                "Error al conectar con la base de datos:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
