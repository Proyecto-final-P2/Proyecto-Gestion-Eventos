package controller;

import model.Administrador;
import repository.AdministradorDAO;
import GUI.MenuPrincipal;
import GUI.Login;
import javax.swing.JOptionPane;
import java.util.List;

// controlador del inicio de sesion
public class LoginController {

    // ventana y la conexion a la bd
    private final Login vista;
    private final AdministradorDAO dao = new AdministradorDAO();

    // se le pasa la ventana de login
    public LoginController(Login vista) {
        this.vista = vista;
    }

    // metodo que intenta iniciar sesion
    public void iniciarSesion(String email, String password) {
        try {
            // trae el administrador por email
            Administrador admin = dao.buscarPorEmail(email);
            
            // verifica si existe y si la password coincide
            if (admin != null && admin.getPassword().equals(password)) {
                // cierra la ventana de login
                vista.dispose();
                // abre el menu principal pasandole el administrador que entro
                new MenuPrincipal(admin).setVisible(true);
            } else {
                // si no lo encuentra o clave mal, tira error
                JOptionPane.showMessageDialog(vista,
                    "Email o contraseña incorrectos.",
                    "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            // si se cae la base de datos, tira error de conexion
            JOptionPane.showMessageDialog(vista,
                "Error al conectar con la base de datos:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
