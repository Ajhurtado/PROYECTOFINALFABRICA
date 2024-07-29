package ec.edu.uce.demo.controller;

import java.sql.Timestamp;
import ec.edu.uce.demo.model.*;
import ec.edu.uce.demo.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService us;

    @Autowired
    private ProductoService ps;

    @Autowired
    private PedidoService pes;

    @Autowired
    private LineaPedidoService lps;

    @GetMapping("/test")
    public String testTemplate() {
        return "simple";
    }

    @GetMapping("/login")
    public String cargaLoginUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String loginUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult br, HttpSession miSesion, Model model, RedirectAttributes ra) {
        if(br.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "login";
        }
        if(usuario.getNombre() != null && usuario.getPassword() != null) {
            for(Usuario usuarioBuscado : us.getUsuarios()) {
                if(usuarioBuscado.getNombre().equals(usuario.getNombre()) && Encriptacion.validarPassword(usuario.getPassword(), usuarioBuscado.getPassword()) == true) {
                    miSesion.setAttribute("nombre", usuario.getNombre());
                    ps.obtenerProductos();
                    return "redirect:/";
                }
            }
        }

        ra.addFlashAttribute("errorUsuarioNoExiste", "Usuario o contraseña incorrectos.");
        return "redirect:/login";
    }

    @GetMapping("/registro")
    public String cargaRegistroUsuario(Model model){
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registroUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult br, Model model, RedirectAttributes ra, @RequestParam(name="repitPassword", required=false) String passwordRepetida, HttpSession miSesion) {
        if(br.hasErrors()) {
            return "usuario/registro";
        }else {
            Usuario usuarioExiste = us.buscarUsuario(usuario.getNombre());
            if(usuarioExiste == null) {
                if(usuario.getNombre() != null && usuario.getPassword() != null && passwordRepetida != null) {
                    Roles rol = Roles.administrador;
                    usuario.setRol(rol);
                    String passwordEncriptada = Encriptacion.encriptarPassword(usuario.getPassword());
                    usuario.setPassword(passwordEncriptada);
                    us.crearUsuario(usuario);
                    miSesion.setAttribute("nombre", usuario.getNombre());
                    return "redirect:/";
                }
            }
            ra.addFlashAttribute("errorUsuarioExiste", "El nombre de usuario ya existe.");
            return "redirect:/registro";
        }
    }

    //ESTE SERA LLAMADO POR EL ADMINISTRADOR PARA VER CUANTOS PEDIDOS TIENE EL USUARIO
    @GetMapping("/miPerfil/misPedidos")
    public String cargaMisPedidos(HttpSession miSesion) {
        if(miSesion.getAttribute("nombre") != null) {
            ArrayList<Pedido> pedidosUsuario = new ArrayList<Pedido>();
            Usuario usuario = us.buscarUsuario(miSesion.getAttribute("nombre").toString());
            pedidosUsuario = pes.obtenerPedidosUsuario(usuario);
            miSesion.setAttribute("pedidosUsuario", pedidosUsuario);
            return "miPedidos";
        }
        return "redirect:/";
    }

    //SI ALCANZA EL TIEMPO,
    @GetMapping("/miPerfil/cancelarPedido/{id}")
    public String cancelarPedido(RedirectAttributes ra, HttpSession miSesion, @PathVariable(name="id") int id) {
        if(miSesion.getAttribute("nombre") != null) {
            Pedido pedidoSelecionado = pes.obtenerPedidoUsuario(id);
            if(pedidoSelecionado.getEstado_pedido() != Estado.E && pedidoSelecionado.getEstado_pedido() != Estado.C && pedidoSelecionado.getEstado_pedido() != Estado.PC) {
                Estado nuevoEstado = Estado.PC;
                pes.cambiarEstado(nuevoEstado, id);
                return "redirect:miPerfil/misPedidos";
            }else {
                String numPedido = pedidoSelecionado.getNum_factura();
                if(pedidoSelecionado.getEstado_pedido() == Estado.E) {
                    ra.addFlashAttribute("errorCancelarPedido", "No se puede cancelar el pedido " + numPedido + " por que ya ha sido enviado.");
                }
                if(pedidoSelecionado.getEstado_pedido() == Estado.C) {
                    ra.addFlashAttribute("errorCancelarPedido", "No se puede cancelar el pedido " + numPedido + " por que ya ha sido cancelado.");
                }
                if(pedidoSelecionado.getEstado_pedido() == Estado.PC) {
                    ra.addFlashAttribute("errorCancelarPedido", "No se puede cancelar el pedido " + numPedido + " por que ya esta pendiente de cancelacion.");
                }
            }
        }
        return "redirect:/miPerfil/misPedidos";
    }

    @GetMapping("/miPerfil/verDetallePedido/{id}")
    public String detallePedido(HttpSession miSesion, @PathVariable(name="id") int id) {
        if(miSesion.getAttribute("nombre") != null) {
            Pedido pedido = pes.obtenerPedidoUsuario(id);
            ArrayList<LineaPedido> detallePedido = new ArrayList<LineaPedido>();
            detallePedido = lps.verLineasPedido(pedido);
            double sinImpuestos = 0.0;
            double iva = 0.0;
            for(LineaPedido detalle : detallePedido) {
                sinImpuestos += detalle.getTotal();
                iva += detalle.getTotal()*0.04;
            }
            miSesion.setAttribute("detallePedido", detallePedido);
            miSesion.setAttribute("datosPedido", pedido);
            miSesion.setAttribute("subtotalSinImpuestos", Math.round(sinImpuestos*100.00)/100.00);
            miSesion.setAttribute("iva", Math.round(iva*100.00)/100.00);
            return "miDetalle";
        }
        return "redirect:/";
    }

    @GetMapping("/carrito")
    public String cargaCarrito(HttpSession miSesion) {
        if(miSesion.getAttribute("carrito") != null) {
            return "carrito";
        }
        return "redirect:/";
    }

    @GetMapping("/carrito/eliminarProducto")
    public String eliminarProducto(HttpSession miSesion, @RequestParam(name="id", required=false) int id) {
        ArrayList<Producto> carrito = (ArrayList<Producto>) miSesion.getAttribute("carrito");
        Producto producto = ps.buscarProducto(id);
        carrito.remove(producto);
        miSesion.setAttribute("carrito", carrito);
        double totalAnterior = (double)miSesion.getAttribute("totalCarrito");
        double nuevoTotal = totalAnterior - producto.getPrecio();
        miSesion.setAttribute("totalCarrito", nuevoTotal);

        return "redirect:/carrito";
    }

    @GetMapping("/carrito/finalizarCompra")
    public String finalizarCompra(HttpSession miSesion) {
        if(miSesion.getAttribute("nombre") != null) {
            if(miSesion.getAttribute("carrito") != null) {
                ArrayList<Producto> carrito = (ArrayList<Producto>) miSesion.getAttribute("carrito");
                int productosTotales = carrito.size();
                double precioSinImpuestos = 0.0;
                double precioTotal = 0.0;
                double impuestos = 0.0;
                for(Producto producto : carrito) {
                    double precio = producto.getPrecio();
                    precioSinImpuestos += precio;
                    double precioImpuestos = precio * 0.04;
                    precio += precioImpuestos;
                    impuestos += precioImpuestos;
                    precioTotal += precio;
                }
                miSesion.setAttribute("productosTotales", productosTotales);
                Usuario usuario = us.buscarUsuario(miSesion.getAttribute("nombre").toString());
                String email = usuario.getEmail();
                miSesion.setAttribute("email", email);
                miSesion.setAttribute("impuestos", Math.round(impuestos*100.00)/100.00);
                miSesion.setAttribute("precioSinImpuestos", Math.round(precioSinImpuestos*100.00)/100.00);
                miSesion.setAttribute("precioTotal", Math.round(precioTotal*100.00)/100.00);
                return "redirect:/pago";
            }else {
                return "redirect:/";
            }

        }

        return "redirect:/login";
    }

    @GetMapping("/pago")
    public String cargaPago(Model model){
        model.addAttribute("usuarioPago", new Usuario());
        return "pago";
    }

    @PostMapping("/pago")
    public String pagarCompra(RedirectAttributes ra, HttpSession miSesion, @RequestParam(name = "paymentMethod") String metodoDePago){
        if(miSesion.getAttribute("nombre") != null) {
            if(miSesion.getAttribute("carrito") != null) {
                ArrayList<Producto> contenidoCarrito = (ArrayList<Producto>)miSesion.getAttribute("carrito");
                Estado estado = Estado.PE;
                Long datetime = System.currentTimeMillis();
                Timestamp fechaActual = new Timestamp(datetime);

                Usuario usuario = us.buscarUsuario(miSesion.getAttribute("nombre").toString());
                String numFactura = String.valueOf(usuario.getId()) + datetime.toString();
                Double total = 0.0;
                for(Producto producto: contenidoCarrito) {
                    double precio = producto.getPrecio();
                    double precioImpuestos = precio * 0.04;
                    precio += precioImpuestos;
                    total += precio;
                }
                Pedido pedidoCreado = new Pedido(usuario ,fechaActual, estado, numFactura, Math.round(total*100.00)/100.00);
                pes.crearPedido(pedidoCreado);
                for(Producto producto : contenidoCarrito) {
                    double impuestod = producto.getPrecio()*0.04;
                    float impuesto = (float)impuestod;
                    float precioUnidad = (float)producto.getPrecio();
                    LineaPedido lineaPedido = new LineaPedido(pedidoCreado, producto, precioUnidad, producto.getCantidad(), impuesto, producto.getPrecio());
                    lps.crearLineaPedido(lineaPedido);
                }
                miSesion.setAttribute("pedido", pedidoCreado);
                miSesion.setAttribute("fechaActual", new SimpleDateFormat("dd/MM/yyyy").format(fechaActual));

                ArrayList<Producto> detalleProductos = (ArrayList<Producto>)miSesion.getAttribute("carrito");
                ra.addFlashAttribute("detalleProductos", detalleProductos.clone());
                miSesion.setAttribute("carrito", null);
                return "redirect:/confirmacionPedido";
            }
            return "redirect:/";
        }

        return "redirect:/login";
    }

    @GetMapping("/confirmacionPedido")
    public String cargaConfirmacion(Model model) {
        model.addAttribute("detalleProductos", model.getAttribute("detalleProductos"));
        return "confirmacionPedido";
    }

    @GetMapping("/confirmacionPedido/verPedidos")
    public String confirmarPedidoVerPedidos(HttpSession miSesion) {
        return "redirect:/miPerfil/misPedidos";
    }

    @GetMapping("/confirmacionPedido/volverInicio")
    public String confirmarPedidoVolverInicio(HttpSession miSesion) {
        return "redirect:/";
    }
}