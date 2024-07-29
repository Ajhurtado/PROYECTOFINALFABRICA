package ec.edu.uce.demo.service;


import ec.edu.uce.demo.model.Estado;
import ec.edu.uce.demo.model.Pedido;
import ec.edu.uce.demo.model.Usuario;
import ec.edu.uce.demo.repository.Observer;
import ec.edu.uce.demo.repository.PedidoRepository;
import ec.edu.uce.demo.repository.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService implements Subject {


    @Autowired
    PedidoRepository pr;

    List<Observer> observers=new ArrayList<>();

    public void crearPedido(Pedido pedido) {
        pr.save(pedido);
    }

    //LO QUE UTILIZA EL ADMINISTRADOR PARA OBTENER-VER LOS PEDIDOS DEL USER
    public ArrayList<Pedido> obtenerPedidosUsuario(Usuario usuario){
        ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
        pedidos = pr.findByUsuario(usuario);
        return pedidos;
    }

    public Pedido obtenerPedidoUsuario(int id) {
        Pedido pedido = pr.getById(id);
        return pedido;
    }


    public void cambiarEstado(Estado nuevoEstado, int id) {
        pr.cancelarPedido(nuevoEstado, id);
    }


    public ArrayList<Pedido> getPedidos(){
        ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
        pedidos = (ArrayList<Pedido>)pr.findAll();
        return pedidos;
    }

    @Override
    public void registroObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
