package ec.edu.uce.demo.service;


import ec.edu.uce.demo.model.LineaPedido;
import ec.edu.uce.demo.model.Pedido;
import ec.edu.uce.demo.repository.LineaPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LineaPedidoService {
    @Autowired
    LineaPedidoRepository lpr;

    public void crearLineaPedido(LineaPedido lineapedido) {
        lpr.save(lineapedido);
    }

    public ArrayList<LineaPedido> verLineasPedido(Pedido pedido){
        ArrayList<LineaPedido> lineasPedido = new ArrayList<LineaPedido>();
        lineasPedido = lpr.findByPedido(pedido);
        return lineasPedido;
    }
}
