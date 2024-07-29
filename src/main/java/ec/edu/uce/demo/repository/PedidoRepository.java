package ec.edu.uce.demo.repository;


import ec.edu.uce.demo.model.Estado;
import ec.edu.uce.demo.model.Pedido;
import ec.edu.uce.demo.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    ArrayList<Pedido> findByUsuario(Usuario usuario);

    @Transactional
    @Modifying
    @Query("update Pedido p set p.estado_pedido = ?1 where p.id = ?2")
    void cancelarPedido(Estado estado_pedido, int id);
}
