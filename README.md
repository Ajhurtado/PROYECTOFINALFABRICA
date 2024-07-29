# PROYECTOFINALFABRICA
Proyecto Final Avanzada 2 Grupal
La aplicación web cuenta con una cabecera y un pie de página comunes para todas sus secciones, utilizando includes/fragments. Además, incluye un menú de navegación para facilitar el acceso a las diferentes páginas.
# DESCRIPCION
# Página Principal
Catálogo de Productos: En la página de inicio se presenta un catálogo completo de productos (tabla Productos).
Filtro de Búsqueda: La página principal incluye un filtro para buscar productos en el catálogo por diferentes características, como búsqueda general o categoría (tabla Categorías).
Autenticación y Roles de Usuario
Formulario de Login: La aplicación dispone de un formulario de inicio de sesión accesible para todos los usuarios, independientemente de su rol.
Roles de Usuario: La aplicación cuenta con dos roles de usuario (tabla Roles). Los roles incluyen:
Cliente: Los usuarios pueden registrarse en la aplicación, obteniendo automáticamente el rol de 'cliente' (tabla Usuarios).
Administrador: Gestionar administradores (altas, actualizaciones, bajas lógicas): solo el usuario ‘admin’ (superadministrador).
Proceso de Compra: Para realizar una compra, los usuarios deben iniciar sesión con el rol de 'cliente'. No es necesario conectar con una pasarela de pago real; se simula el proceso.
Gestión de Pedidos: Los pedidos (tabla Pedidos) pueden tener los estados: 'pendiente envío' (PE), 'pendiente cancelación' (PC), 'enviado' (E) o 'cancelado' (C) (campo estado).
Líneas de Pedido: Cada pedido consta de varias líneas de pedido (tabla Detalles_pedido), cada una asociada a un producto y su cantidad.
#Funcionalidades por Rol

# Cliente
Realizar pedidos.
Ver historial y detalles de pedidos.
Ver y modificar el perfil de usuario.
# Administrador
Gestión de productos y clientes (bajas lógicas).
Gestión de empleados (altas, actualizaciones, bajas lógicas).
Procesar cancelaciones de pedidos, cambiando su estado a 'cancelado'.
Tecnologías Utilizadas
# Front End:
HTML
CSS
# Back End:
Java
