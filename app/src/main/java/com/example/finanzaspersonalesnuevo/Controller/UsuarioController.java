package com.example.finanzaspersonalesnuevo.Controller;

import com.example.finanzaspersonalesnuevo.Model.Usuario;

public class UsuarioController {

    private Usuario usuario;

    public UsuarioController() {
        // Inicializar usuario, por ejemplo, con valores predeterminados
        this.usuario = new Usuario("John Doe", "john@example.com", "1234");
    }

    // Obtener el usuario
    public Usuario obtenerUsuario() {
        return usuario;
    }

    // Actualizar la información del usuario
    public void actualizarUsuario(String nombre, String email, String contrasena) {
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setContrasena(contrasena);
    }
}
