/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.db;

import static com.grupo1.misc.LocationGeneration.genGeoCoords;
import com.sigad.sigad.app.controller.LoginController;
import static com.sigad.sigad.app.controller.LoginController.encrypt;
import com.sigad.sigad.business.DetallePedido;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.PedidoEstado;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Permiso;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoCategoria;
import com.sigad.sigad.business.ProductoFragilidad;
import com.sigad.sigad.business.ProductoInsumo;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.Vehiculo;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Session;

/**
 *
 * @author cfoch
 */
public class DBPopulator {

    public static void populateVehiculoTipo() {
        int i;
        Lorem lorem = LoremIpsum.getInstance();
        String [][] infos = {
            {"Vehículo Turbo", lorem.getWords(3, 5), "Toyota", "A400"},
            {"Camión Sencillo", lorem.getWords(3, 5), "Volkswagen", "A30"},
            {"Doble Troque", lorem.getWords(3, 5), "BMW", "M300"},
            {"Cuatro Manos", lorem.getWords(3, 5), "KIA", "K400"},
        };
        double capacidades[] = {10000, 12000, 15000, 18000};
        try (Session session = LoginController.serviceInit()) {
            session.getTransaction().begin();
            for (i = 0; i < infos.length; i++) {
                Vehiculo.Tipo tipo = new Vehiculo.Tipo();
                tipo.setNombre(infos[i][0]);
                tipo.setDescripcion(infos[i][1]);
                tipo.setMarca(infos[i][2]);
                tipo.setModelo(infos[i][3]);
                tipo.setCapacidad(capacidades[i]);
                session.save(tipo);
            }
            session.getTransaction().commit();
        }
    }

    public static void populateVehiculo() {
        int i, j;
        int maxVehiculosPorTipo = 4;
        int min = 2;
        Random rand = new Random();
        Lorem lorem = LoremIpsum.getInstance();
        try (Session session = LoginController.serviceInit()) {
            List<Vehiculo.Tipo> tipos;
            tipos = (List<Vehiculo.Tipo>)
                    session.createQuery("from Vehiculo$Tipo").list();
            session.getTransaction().begin();
            for (i = 0; i < tipos.size(); i++) {
                Vehiculo.Tipo tipo = tipos.get(i);
                int nVehiculosPorTipo;
                nVehiculosPorTipo =
                        rand.nextInt(maxVehiculosPorTipo - min + 1) + min;
                for (j = 0; j < nVehiculosPorTipo; j++) {
                    Vehiculo vehiculo;
                    vehiculo = new Vehiculo();
                    vehiculo.setNombre(lorem.getTitle(i));
                    vehiculo.setPlaca(lorem.getZipCode());
                    vehiculo.setDescripcion(lorem.getWords(4, 7));
                    vehiculo.setTipo(tipo);
                    session.save(vehiculo);
                }
            }
            session.getTransaction().commit();
        }
    }

    public static void populateTienda() {
        Lorem lorem = LoremIpsum.getInstance();
        String [][] infos = {
            {"Tienda 1", lorem.getCity() +  " " + lorem.getZipCode()},
            {"Tienda 2", lorem.getCity() +  " " + lorem.getZipCode()},
            {"Tienda 3", lorem.getCity() +  " " + lorem.getZipCode()},
            {"Tienda 5", lorem.getCity() +  " " + lorem.getZipCode()},
            {"Tienda 6", lorem.getCity() +  " " + lorem.getZipCode()},
            {"Tienda 7", lorem.getCity() +  " " + lorem.getZipCode()},
            {"Tienda 8", lorem.getCity() +  " " + lorem.getZipCode()},
        };
        double [] capacidades = {
            100000, 120000, 120000, 140000, 60000, 200000, 50000, 90000};
        double [][] coordenadas = {
            {-12.072564, -77.071882},
            // Avenida José Leguía y Meléndez 1786-1846 Pueblo Libre 15084
            {-12.074211, -77.072853},
            // Panaderia Thakrispan. Municipalidad Metropolitana de, Lima 15084
            {-12.077264, -77.074422},
            // Santa Francisca. Cercado de Lima 15088
            {-12.061329, -77.078440},
            // Av. Alejandro Bertello. Cercado de Lima 15083
            {-12.062924, -77.062839},
            // Rodríguez de Mendoza 329-241 Cercado de Lima 15084
            {-12.073089, -77.060483},
            // Calle Los Ruiseñores 120-218. Bellavista 07006
            {-12.060905, -77.095347},
            // Arenas. Callao 070010
            {-12.056830, -77.125136}
        };
        int i;
        try (Session session = LoginController.serviceInit()) {
            session.getTransaction().begin();
            for (i = 0; i < infos.length; i++) {
                Tienda tienda = new Tienda();
                tienda.setDescripcion(infos[i][0]);
                tienda.setDireccion(infos[i][1]);
                tienda.setCapacidad(capacidades[i]);
                tienda.setCooXDireccion(coordenadas[i][0]);
                tienda.setCooYDireccion(coordenadas[i][1]);
                tienda.setActivo(true);
                session.save(tienda);
            }
            session.getTransaction().commit();
        }
    }
    public static void populatePerfiles() throws Exception {
        String [][] infos = {
            {"Cliente", "Cliente de aplicación móvil"},
            {"SuperAdmin", "Super admini can create stores"},
            {"Administrador de tienda", "Administrador de tienda"},
            {"Vendedor", "Encargado de ventas"},
            {"Encargado de almacén", "Encargado de almacén"},
        };
        int i;
        try (Session session = LoginController.serviceInit()) {
            Set<Permiso> permisos;
            permisos = new HashSet<>((List<Permiso>)
                    session.createQuery("from Permiso").list());
            session.getTransaction().begin();
            for (i = 0; i < infos.length; i++) {
                Perfil perfil = new Perfil();
                perfil.setNombre(infos[i][0]);
                perfil.setDescripcion(infos[i][1]);
                perfil.setActivo(true);
                if (perfil.getNombre().equals("SuperAdmin")) {
                    perfil.setPermisos(permisos);
                }
                session.save(perfil);
            }
            session.getTransaction().commit();
        }
    }

    public static void populatePermisos() {
        String [][] infos = {
            {"Productos", "SALE"},
            {"Insumos", "ARRANGE_SEND_BACKWARD"},
            {"Personal", "ACCOUNT_MULTIPLE"},
            {"Repartos", "CAR"},
            {"Pedidos", "CART"},
            {"Tiendas", "STORE"},
            {"Perfiles", "FINGERPRINT"},
            {"Estadísticas", "ELEVATION_RISE"},
            {"Carga Masiva", "ARROW_UP_BOLD_CIRCLE"},
            {"Configuraciones", "SETTINGS"}
        };
        int i;
        try (Session session = LoginController.serviceInit()) {
            session.getTransaction().begin();
            for (i = 0; i < infos.length; i++) {
                Permiso permiso = new Permiso();
                permiso.setMenu(infos[i][0]);
                permiso.setIcono(infos[i][1]);
                session.save(permiso);
            }
            session.getTransaction().commit();
        }
    }

    public static void populateUsuario() {
        int minClientes = 89;
        int maxClientes = 400;
        int maxOtrosPerfiles = 3;
        int i, j;
        try (Session session = LoginController.serviceInit()) {
            Lorem lorem = LoremIpsum.getInstance();
            List<Perfil> perfiles;
            Random rand = new Random();

            perfiles = (List<Perfil>)
                    session.createQuery("from Perfil").list();
            session.getTransaction().begin();
            for (i = 0; i < perfiles.size(); i++) {
                Perfil perfil;
                Usuario usuario;
                int maxUsuarios;

                perfil = perfiles.get(i);
                if ("Cliente".equals(perfil.getNombre())) {
                    maxUsuarios = rand.nextInt(maxClientes - minClientes + 1)
                            + minClientes;
                } else {
                    maxUsuarios = rand.nextInt(maxOtrosPerfiles) + 1;
                }

                for (j = 0; j < maxUsuarios; j++) {
                    usuario = new Usuario();
                    usuario.setActivo(true);
                    if (i % 2 == 0) {
                        usuario.setNombres(lorem.getNameFemale());
                    } else {
                        usuario.setNombres(lorem.getNameMale());
                    }
                    usuario.setApellidoPaterno(lorem.getLastName());
                    usuario.setApellidoMaterno(lorem.getLastName());

                    usuario.setPerfil(perfil);
                    usuario.setIntereses(lorem.getWords(5, 10));
                    usuario.setPassword(encrypt("123"));
                    usuario.setCorreo(String.format("%s%s",
                            RandomStringUtils.random(3, "0123456789"),
                            lorem.getEmail()));
                    usuario.setTelefono(lorem.getPhone());
                    usuario.setCelular(lorem.getPhone());
                    usuario.setDni(RandomStringUtils.random(8, "0123456789"));
                    session.save(usuario);
                }
            }
            session.getTransaction().commit();
        }
    }

    public static void populatePedidoEstado() {
        String [][] infos = {
            {"Venta", "Estado inicial del pedido"},
            {"Despacho", "Cuando una vez generada la ruta de reparto"},
            {"Cancelado", "El pedido fue cancelado"},
            {"Finalizado", "Cuando la entrega concluye"}
        };
        int i;
        try (Session session = LoginController.serviceInit()) {
            session.getTransaction().begin();
            for (i = 0; i < infos.length; i++) {
                PedidoEstado estado = new PedidoEstado();
                estado.setNombre(infos[i][0]);
                estado.setDescripcion(infos[i][1]);
                session.save(estado);
            }
            session.getTransaction().commit();
        }
    }

    public static void populateInsumo() {
        String [][] infos = {
            {
                "Girasoles",
                "Una bonita planta originaria de América del Norte dónde lo " +
                "consideran una planta sagrada puesto que va girando a " +
                "lo largo del día para orientarse hacia el sol."
            },
            {
                "Rosas",
                "Las rosas son naturales de América del Norte, de Europa y " +
                "Asia. Existe un gran número de clases de rosas diferentes " +
                "y sus colores son realmente variables"
            },
            {
                "Hortensias",
                "La hortensia es una planta ornamental proveniente del sur " +
                "y del este de Asia, de zonas de China, de Corea, de Jamón " +
                "y también de Indonesia y del Himalaya, así como también de " +
                "distintas zonas de América."
            },
            {
                "Tulipanes",
                "Los tulipanes son unas plantas clásicas apreciadas por " +
                "floristas y amantes de las plantas. Son originarios de " +
                "Asia y de Europa y también están presentes en algunas zonas " +
                "de Oriente Medio."
            },
            {
                "Dalias",
                "Flores bonitas hay muchas, pero pocas son tan elegantes como" +
                "las dalias. Éstas son unas plantas que se cultivan durante " +
                "la primavera desde un bulbo. Son una planta muy apreciada " +
                "porque presentan tonos realmente bonitos y vivos."
            },
            {
                "Lirios",
                "El Lirio es una planta perteneciente a la familia de las" +
                "liliáceas que crece en toda Europa, en Japón, al sur de la " +
                "India y de Filipinas. Las encontrarás en tonos blancos, " +
                "naranjas o rojos y con un olor que las caracteriza."
            },
            {
                "Claveles",
                "El clavel es originaria del ártico de América del Norte y " +
                "de Sudáfrica, pero actualmente se cultiva en muchas más " +
                "partes del mundo."
            },
            {
                "Cannas índicas",
                "La canna es una flor que cuenta con multitud de colores y " +
                "unas formas realmente bonitas. Sus tallos son rectos y se " +
                "utiliza para formar grupos de flores aisladas"
            }
        };
        int i;
        Random rand = new Random();
        try (Session session = LoginController.serviceInit()) {
            session.getTransaction().begin();
            for (i = 0; i < infos.length; i++) {
                Insumo insumo = new Insumo();
                insumo.setNombre(infos[i][0]);
                insumo.setDescripcion(infos[i][1]);
                insumo.setPrecio(rand.nextInt(10 + 1));
                insumo.setStockTotalFisico(rand.nextInt(100 + 1));
                insumo.setStockTotalLogico(rand.nextInt(100 + 1));
                insumo.setTiempoVida(rand.nextInt(10 + 1));
                insumo.setVolumen((double) (rand.nextInt(300 + 1) + 1));
                insumo.setActivo(true);
                session.save(insumo);
            }
            session.getTransaction().commit();
        }
    }

    public static void populateProductoCategoria() {
        String [][] infos = {
            {"Ramo de flores", "Conjunto de flores"},
            {"Flores San Valentín", "Flores para la fecha de San Valentín"},
            {"Flores funerarias", "Flores para velorios y entierros"},
            {"Flores matrimoniales", "Flores para matrimonios"},
        };
        int i;
        Random rand = new Random();
        try (Session session = LoginController.serviceInit()) {
            session.getTransaction().begin();
            for (i = 0; i < infos.length; i++) {
                ProductoCategoria categoria;
                categoria = new ProductoCategoria();
                categoria.setNombre(infos[i][0]);
                categoria.setDescripcion(infos[i][1]);
                session.save(categoria);
            }
            session.getTransaction().commit();
        }
    }

    public static void populateProductoFragilidad() {
        String [][] infos = {
            {"Baja"},
            {"Media"},
            {"Alta"}
        };
        int i;
        Random rand = new Random();
        try (Session session = LoginController.serviceInit()) {
            session.getTransaction().begin();
            for (i = 0; i < infos.length; i++) {
                ProductoFragilidad fragilidad = new ProductoFragilidad();
                fragilidad.setDescripcion(infos[i][0]);
                fragilidad.setValor(rand.nextInt(100 + 1));
                session.save(fragilidad);
            }
            session.getTransaction().commit();
        }
    }

    public static void populateProducto() {
        int i;
        int nProductos = 100;
        Lorem lorem = LoremIpsum.getInstance();
        Random rand = new Random();
        try (Session session = LoginController.serviceInit()) {
            List<ProductoCategoria> categorias;
            List<ProductoFragilidad> fragilidades;
            List<Insumo> insumos;

            categorias = (List<ProductoCategoria>)
                    session.createQuery("from ProductoCategoria").list();
            fragilidades = (List<ProductoFragilidad>)
                    session.createQuery("from ProductoFragilidad").list();
            insumos = (List<Insumo>)
                    session.createQuery("from Insumo").list();

            session.getTransaction().begin();
            for (i = 0; i < nProductos; i++) {
                int j;
                double volumenProducto = 0.0;
                double precioProducto = 0.0;
                ProductoCategoria categoria;
                ProductoFragilidad fragilidad;
                List<Insumo> insumosSubSet;

                Producto producto = new Producto();
                categoria = categorias.get(rand.nextInt(categorias.size()));
                fragilidad =
                        fragilidades.get(rand.nextInt(fragilidades.size()));
                insumosSubSet = new ArrayList<>(insumos);
                Collections.shuffle(insumosSubSet);
                // Asignar aleatoriamente 1 o la cuarta parte del total de
                // insumos al producto.
                insumosSubSet = insumosSubSet.subList(0,
                        insumos.size() / 4);
                if (insumosSubSet.isEmpty()) {
                    Insumo insumo = insumos.get(rand.nextInt(insumos.size()));
                    insumosSubSet.add(insumo);
                }

                producto.setNombre(lorem.getTitle(2, 5));
                producto.setDescripcion(lorem.getWords(10, 15));
                producto.setCategoria(categoria);
                producto.setFragilidad(fragilidad);
                session.save(producto);
                for (j = 0; j < insumosSubSet.size(); j++) {
                    double cantidadInsumo;
                    cantidadInsumo = (double) rand.nextInt(10 + 1) + 1;
                    ProductoInsumo productoInsumo;
                    productoInsumo = new ProductoInsumo();
                    productoInsumo.setInsumo(insumosSubSet.get(j));
                    productoInsumo.setProducto(producto);
                    productoInsumo.setCantidad(cantidadInsumo);
                    volumenProducto += insumosSubSet.get(j).getVolumen();
                    precioProducto +=
                            insumosSubSet.get(j).getPrecio() * cantidadInsumo;
                    session.save(productoInsumo);
                }
                producto.setVolumen(volumenProducto);
                // Ganar el 20%.
                producto.setPrecio(precioProducto * 1.20);
                session.save(producto);
            }
            session.getTransaction().commit();
        }
    }

    public static void populatePedido() {
        String []turnos = {"M", "T", "N"};
        ArrayList<Pair<Double, Double>> coords;
        int nPedidosPorCliente;
        int maxPedidosPorCliente = 5;
        int minProductosPorPedido = 1;
        int maxProductosPorPedido = 4;
        int i, j, k;

        Random rand = new Random();
        try (Session session = LoginController.serviceInit()) {
            List<Usuario> clientes, vendedores;
            List<Producto> productos;
            List<PedidoEstado> pedidoEstados;
            Perfil perfilCliente, perfilVendedor;
            Lorem lorem = LoremIpsum.getInstance();
            perfilCliente = (Perfil)
                    session.createQuery("from Perfil where nombre='Cliente'")
                            .getSingleResult();
            perfilVendedor = (Perfil)
                    session.createQuery("from Perfil where nombre='Vendedor'")
                            .getSingleResult();
            clientes = (List<Usuario>) session
                    .createQuery("from Usuario where perfil_id = :idPerfil")
                    .setParameter("idPerfil", perfilCliente.getId()).list();
            vendedores = (List<Usuario>) session
                    .createQuery("from Usuario where perfil_id = :idPerfil")
                    .setParameter("idPerfil", perfilVendedor.getId()).list();
            productos = (List<Producto>)
                    session.createQuery("from Producto").list();
            pedidoEstados = (List<PedidoEstado>)
                    session.createQuery("from PedidoEstado").list();
            session.getTransaction().begin();
            for (i = 0; i < clientes.size(); i++) {
                Usuario cliente;
                cliente = clientes.get(i);
                nPedidosPorCliente = rand.nextInt(maxPedidosPorCliente + 1);
                for (j = 0; j < nPedidosPorCliente; j++) {
                    Pedido pedido;
                    Usuario vendedor;
                    PedidoEstado pedidoEstado;
                    String pedidoTurno;
                    List<Producto> randomProductos;
                    Set<DetallePedido> detallesPedido;
                    Calendar calendar;
                    Timestamp fechaVenta;
                    double precioTotal = 0.0;
                    double volumenTotal = 0.0;

                    calendar = new GregorianCalendar();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DAY_OF_MONTH, -rand.nextInt(7));
                    fechaVenta = new Timestamp(calendar.getTime().getTime());

                    pedido = new Pedido();
                    coords = genGeoCoords(1, -12.061318, -77.055494, 3000);
                    vendedor = vendedores.get(rand.nextInt(vendedores.size()));
                    pedidoEstado = pedidoEstados.get(
                            rand.nextInt(pedidoEstados.size()));
                    pedidoTurno = turnos[rand.nextInt(turnos.length)];
                    // Elegir N >= 1 productos aleatorios.
                    randomProductos = new ArrayList<>(productos);
                    Collections.shuffle(randomProductos);
                    randomProductos = productos.subList(0,
                            rand.nextInt(maxProductosPorPedido) + 1);

                    // Crear un pedido y agregar items.
                    detallesPedido = new HashSet<>();
                    for (k = 0; k < randomProductos.size(); k++) {
                        Producto producto;
                        double precio;
                        double volumen;
                        DetallePedido detallePedido;
                        detallePedido = new DetallePedido();
                        producto = randomProductos.get(k);
                        System.out.println("prod: " + producto);
                        detallePedido.setCantidad(rand.nextInt(3) + 1);
                        detallePedido.setProducto(producto);
                        detallePedido.setNumEntregados(0);
                        detallePedido.setPrecioUnitario(producto.getPrecio());
                        detallePedido.setActivo(true);
                        detallePedido.setPedido(pedido);

                        precio = detallePedido.getPrecioUnitario();
                        precio *= detallePedido.getCantidad();
                        precioTotal += precio;
                        volumen = producto.getVolumen();
                        volumen *= detallePedido.getCantidad();
                        volumenTotal += volumen;

                        // Agregar detalle a lista de detalles.
                        detallesPedido.add(detallePedido);
                        // Guardar
                        //session.save(detallePedido);
                    }
                    pedido.setActivo(true);
                    pedido.setCliente(cliente);
                    pedido.setDetallePedido(detallesPedido);
                    pedido.setDireccionDeEnvio(
                            lorem.getCity() + " " + lorem.getZipCode());
                    pedido.setEstado(pedidoEstado);
                    // TODO: Falta agregar histórico de estados.
                    // pedido.setEstadosPedido(estadosPedido);
                    pedido.setFechaVenta(fechaVenta);
                    if (pedido.getEstado().equals("Finalizado")) {
                        Time horaEntregaTime;
                        Timestamp horaEntrega;
                        long ms;
                        int hs2ms = 60 * 60 * 1000;
                        if (pedidoTurno.equals("M")) {
                            // De 8am a 12m
                            ms = rand.nextInt(4 * hs2ms) + 8 * hs2ms;
                        } else if (pedidoTurno.equals("T")) {
                            // De 12m a 6pm
                            ms = rand.nextInt(6 * hs2ms) + 12 * hs2ms;
                        } else {
                            // Noche.
                            // De 6pm a 10pm
                            ms = rand.nextInt(22 * hs2ms) + 16 * hs2ms;
                        }
                        horaEntregaTime = new Time(ms);
                        horaEntrega = new Timestamp(horaEntregaTime.getTime());
                        pedido.setHoraEntrega(horaEntrega);
                    }
                    pedido.setMensajeDescripicion(lorem.getWords(5, 10));
                    pedido.setModificable(false);
                    pedido.setTotal(precioTotal);
                    pedido.setTurno(pedidoTurno);
                    pedido.setVendedor(vendedor);
                    pedido.setVolumenTotal(volumenTotal);
                    pedido.setCooXDireccion(coords.get(0).getLeft());
                    pedido.setCooYDireccion(coords.get(0).getRight());
                    System.out.println("Saving pedido: ");
                    session.save(pedido);
                    System.out.println("Saving " + detallesPedido.size() + " detalles");
                    detallesPedido.forEach((detalle) -> {
                        session.save(detalle);
                    });
                }
            }
            session.getTransaction().commit();
        }
    }
}
