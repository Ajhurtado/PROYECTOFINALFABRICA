package ec.edu.uce.demo.repository;


import ec.edu.uce.demo.model.LineaPedido;
import ec.edu.uce.demo.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Integer> {
    ArrayList<LineaPedido> findByPedido(Pedido pedido);
}
