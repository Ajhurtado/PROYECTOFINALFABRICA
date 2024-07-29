package ec.edu.uce.demo.repository;


import ec.edu.uce.demo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

	@Query("select p from Producto p where p.nombre like %?1%")
    ArrayList<Producto> buscarCampo(String campoBuscar);


}
