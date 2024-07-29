package ec.edu.uce.demo.service;


import ec.edu.uce.demo.model.Producto;
import ec.edu.uce.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;



@Service
public class ProductoService {

    @Autowired
    ProductoRepository pr;

    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> catalogo = new ArrayList<Producto>();
        catalogo = (ArrayList<Producto>)pr.findAll();
        return catalogo;
    }

    public Producto buscarProducto(int id){
        Producto productoDevolver = null;
        Optional<Producto> opcional = pr.findById(id);
        productoDevolver = opcional.get();

        return productoDevolver;
    }

    public void crearProducto(Producto producto) {
        pr.save(producto);
    }

    public void eliminarProducto(Producto producto) {
        pr.delete(producto);
    }

    public void editarProducto(Producto producto) {
        pr.save(producto);
    }

    public ArrayList<Producto> buscarCampo(String campoBuscar){
        ArrayList<Producto> productos = new ArrayList<Producto>();
        productos = pr.buscarCampo(campoBuscar);
        return productos;
    }


    public void crearProductos(Producto producto) {
      pr.save(producto);
    }

}

