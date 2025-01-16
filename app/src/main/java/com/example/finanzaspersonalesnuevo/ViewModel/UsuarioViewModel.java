package com.example.finanzaspersonalesnuevo.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finanzaspersonalesnuevo.Controller.UsuarioController;
import com.example.finanzaspersonalesnuevo.Model.Usuario;

public class UsuarioViewModel extends ViewModel {

    private final UsuarioController usuarioController;
    private final MutableLiveData<Usuario> usuarioLiveData;

    public UsuarioViewModel() {
        usuarioController = new UsuarioController();
        usuarioLiveData = new MutableLiveData<>();
        cargarUsuario();
    }

    // Método para cargar el usuario inicial
    private void cargarUsuario() {
        usuarioLiveData.setValue(usuarioController.obtenerUsuario());
    }

    // Obtener LiveData del usuario
    public LiveData<Usuario> obtenerUsuarioLiveData() {
        return usuarioLiveData;
    }

    // Actualizar la información del usuario
    public void actualizarUsuario(String nombre, String email, String contrasena) {
        usuarioController.actualizarUsuario(nombre, email, contrasena);
        cargarUsuario();
    }
}
