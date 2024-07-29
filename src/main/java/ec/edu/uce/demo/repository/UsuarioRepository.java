package ec.edu.uce.demo.repository;


import ec.edu.uce.demo.model.Roles;
import ec.edu.uce.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByNombre(String nombre);

    ArrayList<Usuario> findAllByRol(Roles rol);

}
