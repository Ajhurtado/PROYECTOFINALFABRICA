package ec.edu.uce.demo.service;


import ec.edu.uce.demo.model.Roles;
import ec.edu.uce.demo.model.Usuario;
import ec.edu.uce.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UsuarioService{

    @Autowired
    UsuarioRepository ur;

    public void crearUsuario(Usuario usuario) {
        ur.save(usuario);
    }

    public ArrayList<Usuario> getUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios = (ArrayList<Usuario>)ur.findAll();
        return usuarios;
    }

    public Usuario buscarUsuario(String nombre) {
        Usuario usuarioBuscar = null;
        usuarioBuscar = ur.findByNombre(nombre);
        return usuarioBuscar;
    }

    public Usuario getUsuario(int id) {
        Usuario usuarioBuscar = null;
        usuarioBuscar = ur.getById(id);
        return usuarioBuscar;
    }

    public int getIdUsuario(String nombre) {
        Usuario usuarioId = null;
        usuarioId = buscarUsuario(nombre);
        int id = usuarioId.getId();
        return id;
    }

    public void editarUsuario(Usuario usuario) {
        ur.save(usuario);
    }

    public void eliminarUsuario(Usuario usuario) {
        ur.delete(usuario);
    }

    public ArrayList<Usuario> getUsuariosRol(Roles rol) {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios = (ArrayList<Usuario>)ur.findAllByRol(rol);
        return usuarios;
    }
}
