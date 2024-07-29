package ec.edu.uce.demo.controller;


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

import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

    @Autowired
    UsuarioService us;

    @Autowired
    ProductoService ps;

    @Autowired
    PedidoService pes;

    @Autowired
    LineaPedidoService lps;

    @GetMapping("")
    public String cargaAdministracion(HttpSession miSesion){
        if(miSesion.getAttribute("usuarioAdmin") != null) {
            String nombreAdministrador = miSesion.getAttribute("usuarioAdmin").toString();
            Usuario usuarioAdministrador = us.buscarUsuario(nombreAdministrador);
            miSesion.setAttribute("datosAdministrador", usuarioAdministrador);
            return "administracionPerfil";
        }

        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String cargaLogin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrarAdmin";
    }

    @PostMapping("/login")
    public String loginAdmin(@ModelAttribute Usuario usuario, HttpSession miSesion) {
        if(usuario.getNombre() != null && usuario.getPassword() != null) {
            for(Usuario usuarioBuscado : us.getUsuarios()) {
                if(usuarioBuscado.getNombre().equals(usuario.getNombre()) && Encriptacion.validarPassword(usuario.getPassword(), usuarioBuscado.getPassword()) == true && (usuarioBuscado.getRol().equals(Roles.administrador))) {
                    miSesion.setAttribute("usuarioAdmin", usuario.getNombre());
                    if(usuarioBuscado.getRol().equals(Roles.administrador)) {
                        miSesion.setAttribute("rolUsuario", Roles.administrador.toString());
                    }
                    return "redirect:/admin";
                }
            }
        }

        return "redirect:/admin/login";
    }

    @GetMapping("/cerrarSesion")
    public String cerrarSesion(HttpSession miSesion) {
        if(miSesion.getAttribute("usuarioAdmin") != null) {
            miSesion.setAttribute("usuarioAdmin", null);
            miSesion.setAttribute("rolUsuario", null);
            return "redirect:/admin/login";
        }

        return "redirect:/admin";
    }

    @GetMapping("/productos")
    public String cargaProductos(Model model, HttpSession miSesion) {
        if (miSesion.getAttribute("usuarioAdmin") != null) {
            ArrayList<Producto> productos = new ArrayList<Producto>();
            productos = ps.obtenerProductos();
            miSesion.setAttribute("productosAdmin", productos);
            return "administracionProductos";
        }

        return "redirect:/admin/login";
    }

    @GetMapping("/productos/nuevoProducto")
    public String botonNuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "nuevoProducto";
    }

    @PostMapping("/productos/crearNuevoProducto")
    public String nuevoProducto(Model model, @ModelAttribute Producto producto, HttpSession miSesion) {
        if(producto.getNombre() != null && producto.getDescripcion() != null && producto.getCantidad() != 0 && producto.getPrecio() != 0) {
            ps.crearProducto(producto);
            return "redirect:/admin/productos";
        }

        return "redirect:/crearNuevoProducto";
    }



    @GetMapping("/productos/editarProducto/{id}")
    public String editarProducto(Model model, @PathVariable(name="id", required=false) int id) {
        Producto producto = ps.buscarProducto(id);
        model.addAttribute("producto", producto);
        return "editarProducto";
    }


    @PostMapping("/productos/editarProductoExistente")
    public String editarProductoExistente(HttpSession miSesion, @ModelAttribute Producto producto) {
        if(miSesion.getAttribute("usuarioAdmin") != null) {
            if(producto != null) {
                ps.editarProducto(producto);
                return "redirect:/admin/productos";
            }
        }

        return "editarProducto";
    }

    @GetMapping("/productos/eliminarProducto/{id}")
    public String eliminarProducto(HttpSession miSesion, @PathVariable(name="id", required=false) int id) {
        if(miSesion.getAttribute("usuarioAdmin") != null) {
            Producto producto = ps.buscarProducto(id);
            ps.eliminarProducto(producto);
            return "redirect:/admin/productos";
        }

        return "administracionProductos";
    }

    @GetMapping("/clientes")
    public String cargaClientes(Model model, HttpSession miSesion) {
        if(miSesion.getAttribute("usuarioAdmin") != null) {
            ArrayList<Usuario> clientes = new ArrayList<Usuario>();
            Roles rol = Roles.cliente;
            clientes = us.getUsuariosRol(rol);
            miSesion.setAttribute("clientesAdmin", clientes);
            return "administracionClientes";
        }

        return "redirect:/admin/login";
    }



    @GetMapping("/clientes/nuevoCliente")
    public String botonNuevoCliente(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "nuevoCliente";
    }

    @PostMapping("/clientes/crearNuevoCliente")
    public String nuevoCliente(@Valid @ModelAttribute Usuario usuario, BindingResult br, Model model, HttpSession miSesion) {
        if(br.hasErrors()) {
            return "administrador/nuevoCliente";
        }else {
            if(usuario.getNombre() != null && usuario.getPassword()  != null && usuario.getNombreUsuario() != null && usuario.getApellido() != null && usuario.getEmail() != null && usuario.getDireccion() != null && usuario.getLocalidad() != null && usuario.getProvincia() != null && usuario.getPais() != null && usuario.getTelefono() != null && usuario.getRol() != null) {
                String passwordEncriptada = Encriptacion.encriptarPassword(usuario.getPassword());
                Usuario usuarioNuevo = new Usuario(usuario.getNombre(), usuario.getEmail(), passwordEncriptada, usuario.getNombreUsuario(), usuario.getApellido(),  usuario.getDireccion(), usuario.getProvincia(), usuario.getLocalidad(), usuario.getPais(), usuario.getTelefono(), usuario.getRol());
                us.crearUsuario(usuarioNuevo);
                return "redirect:/admin/clientes";
            }

            return "redirect:/admin/clientes/crearNuevoCliente";
        }
    }

    @GetMapping("/clientes/editarCliente/{id}")
    public String editarCliente(Model model, @PathVariable(name="id", required=false) int id) {
        Usuario usuario = us.getUsuario(id);
        model.addAttribute("usuario", usuario);
        return "editarCliente";
    }

    @PostMapping("/clientes/editarClienteExistente")
    public String editarClienteExistente(@Valid @ModelAttribute Usuario usuario, BindingResult br, HttpSession miSesion) {
        if(br.hasErrors()) {
            return "editarCliente";
        }else {
            if(miSesion.getAttribute("usuarioAdmin") != null) {
                if(usuario != null) {
                    us.editarUsuario(usuario);
                    return "redirect:/admin/clientes";
                }
            }

            return "editarCliente";
        }
    }

    @GetMapping("/clientes/eliminarCliente/{id}")
    public String eliminarCliente(HttpSession miSesion, @PathVariable(name="id", required=false) int id) {
        if(miSesion.getAttribute("usuarioAdmin") != null) {
            Usuario empleado = us.getUsuario(id);
            us.eliminarUsuario(empleado);
            return "redirect:/admin/clientes";
        }

        return "administracionClientes";
    }


    @GetMapping("/procesarPedidos")
    public String cargaPedidos(Model model, HttpSession miSesion) {
        if(miSesion.getAttribute("usuarioAdmin") != null) {
            //pes=> PedidoServicio
            ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
            pedidos = pes.getPedidos();
            miSesion.setAttribute("pedidosAdmin", pedidos);
            return "administracionProcesarPedidos";
        }

        return "redirect:/admin/login";
    }

    @GetMapping("/procesarPedidos/cancelarPedido/{id}")
    public String cancelarPedido(RedirectAttributes ra, HttpSession miSesion, @PathVariable(name="id") int id) {
        if(miSesion.getAttribute("usuarioAdmin") != null) {
            Pedido pedidoSelecionado = pes.obtenerPedidoUsuario(id);
            if(pedidoSelecionado.getEstado_pedido() != Estado.E && pedidoSelecionado.getEstado_pedido() != Estado.C) {
                Estado nuevoEstado = Estado.C;
                pes.cambiarEstado(nuevoEstado, id);
                return "redirect:/admin/procesarPedidos";
            }else {
                String numPedido = pedidoSelecionado.getNum_factura();
                if(pedidoSelecionado.getEstado_pedido() == Estado.E) {
                    ra.addFlashAttribute("errorCancelarPedidoAdmin", "No se puede cancelar el pedido " + numPedido + " por que ya ha sido enviado.");
                }
                if(pedidoSelecionado.getEstado_pedido() == Estado.C) {
                    ra.addFlashAttribute("errorCancelarPedidoAdmin", "No se puede cancelar el pedido " + numPedido + " por que ya ha sido cancelado.");
                }
                return "redirect:/admin/procesarPedidos";
            }
        }
        return "redirect:/admin";
    }
}
