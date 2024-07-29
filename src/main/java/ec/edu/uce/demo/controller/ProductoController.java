package ec.edu.uce.demo.controller;


import ec.edu.uce.demo.model.Producto;
import ec.edu.uce.demo.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;


@Controller
@RequestMapping("")
public class ProductoController {

    @Autowired
    ProductoService ps;

    @GetMapping("")
    public String inicio(Model model, HttpSession miSesion) {
        ArrayList<Producto> catalogo = ps.obtenerProductos();
        model.addAttribute("catalogo", catalogo);
        Object carritoSesion = miSesion.getAttribute("carrito");
        ArrayList<Producto> carrito;

        if(carritoSesion == null) {
            carrito = new ArrayList<Producto>();
        }else {
            carrito = (ArrayList<Producto>) carritoSesion;
        }

        miSesion.setAttribute("carrito", carrito);
        return "index";
    }

    @GetMapping("/meterProducto")
    public String meterProducto(HttpSession miSesion, @RequestParam(name="id", required=false) int id) {
        ArrayList<Producto> carrito = (ArrayList<Producto>) miSesion.getAttribute("carrito");
        Producto producto;
        producto = ps.buscarProducto(id);
        carrito.add(producto);
        miSesion.setAttribute("carrito", carrito);

        double totalCarrito = 0.0;
        for(Producto productos : carrito) {
            double precio = productos.getPrecio();
            totalCarrito += precio;
        }
        miSesion.setAttribute("totalCarrito", Math.round(totalCarrito*100.00)/100.00);

        return "redirect:/";
    }

	@GetMapping("/buscarProducto")
	public String buscarProducto(Model model, HttpSession miSesion, @RequestParam(name="buscar", required=false) String campoBuscar) {

			ArrayList<Producto> productosBuscados = new ArrayList<Producto>();
			productosBuscados = ps.buscarCampo(campoBuscar);
			miSesion.setAttribute("productosBuscados", productosBuscados);
			model.addAttribute("productos", productosBuscados);
            return "redirect:/";

	}

    @GetMapping("detalleProducto/{id}")
    public String detalleProducto(@PathVariable("id") Integer id, HttpSession miSesion) {
        Producto detalleDeProducto = ps.buscarProducto(id);
        miSesion.setAttribute("detalleProducto", detalleDeProducto);
        return "detalleProducto";
    }
}
