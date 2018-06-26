/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.business.helpers;

import com.google.maps.errors.ApiException;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.business.Constantes;
import com.sigad.sigad.business.DetallePedido;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.PedidoEstado;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoCategoriaDescuento;
import com.sigad.sigad.business.ProductoDescuento;
import com.sigad.sigad.business.ProductoInsumo;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.pedido.controller.SolicitarDireccionController;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Alexandra
 */
public class MigracionPedidosHelper{

    private ArrayList<Pedido> pedidos = new ArrayList<>();
    ;
    private Integer cantidadDetalles;
    private Integer cantidadPedidos;
    private Random rand;
    private ArrayList<Producto> productos = new ArrayList<>();
    private ArrayList<Usuario> clientes = new ArrayList<>();
    ;
    private ArrayList<String> direcciones = new ArrayList<>();
    Double[][][] coo = {
        {
            {-12.0541725, -77.0579092},
            {-12.0538697, -77.0635408},
            {-12.0563811, -77.0571339},
            {-12.0611423, -77.0686821},
            {-12.0570951, -77.0657879}
        },
        {
            {-12.0692001, -77.1043212},
            {-12.075805, -77.0948737},
            {-12.0787368, -77.0961094},
            {-12.0777453, -77.098883},
            {-12.0782122, -77.0924755}
        },
        {
            {-12.0063392, -77.0841182},
            {-12.0281101, -77.0785045},
            {-12.0111311, -77.0776754},
            {-12.0140737, -77.0729654},
            {-12.0147389, -77.0735477}
        },
        {
            {-12.009407, -77.0838552},
            {-12.0754898, -77.0784418},
            {-12.074466, -77.0857107},
            {-12.075764, -77.0858657},
            {-12.074582, -77.0843987}
        },
        {
            {-12.0884318, -77.0799335},
            {-12.0602683, -77.1444936},
            {-12.0627079, -77.1424716},
            {-12.060476, -77.1418855},
            {-12.0628393, -77.1429997}
        }
    };

    String[][] dir = {
        {
            "Av. Tingo Maria 840, Lima",
            "Avenida Naciones Unidas 1721, Lima",
            "Av. Arica 1391, Breña",
            "Av. la Alborada 1327, Lima",
            "Av. Rep. de Venezuela 2291"
        },
        {
            "Av de los Precursores 1031, Lima",
            "Av. Rafael Escardó 545",
            "Av. Rafael Escardó 414, San Miguel",
            "Av. de los Patriotas 323, San Miguel",
            "Av. de la Marina 2563, San Miguel"
        },
        {
            "Av. Angelica Gamarra 1902",
            "Av. Universitaria 201, San Martín de Porres",
            "Av. Tomas Valle 1530, Los Olivos",
            "Av. German Aguirre, San German 569",
            "Av. German Aguirre 640"
        },
        {
            "Av. Universitaria 1851",
            "Avenida Manuel Cipriano Dulanto 1898, Pueblo Libre",
            "Av. La Mar 2382, San Miguel",
            "Calle Mantaro 356, San Miguel",
            "Av. La Mar 2342, San Miguel"
        },
        {
            "Av. Sucre 593, Lima",
            "Jr. Castilla 842, San Miguel",
            "Av. Miguel Grau 853, Callao",
            "Av Saenz Peña 603, Callao",
            "Av. Miguel Grau 819, Callao"
        }
    };

    public MigracionPedidosHelper() {
        //crear
        pedidos = new ArrayList<>();
        cantidadPedidos = 5;
        cantidadDetalles = 5;
        rand = new Random();
        ProductoHelper helper = new ProductoHelper();
        productos = helper.getProducts();
        helper.close();
        PerfilHelper perfilHelper = new PerfilHelper();
        Perfil p = perfilHelper.getProfile(Constantes.PERFIL_CLIENTE);
        perfilHelper.close();
        UsuarioHelper usHelper = new UsuarioHelper();
        clientes = usHelper.getUsers(p);
        for (Usuario cliente : clientes) {
            System.out.println(cliente.getNombres());
        }
    }

    public void direcciones() {
        //obtenerPerfiles
        //-12.054172, -77.056751
    }

    public void crearPedidos() {
        TiendaHelper helper = new TiendaHelper();
        ArrayList<Tienda> tiendas = helper.getStores();
        helper.close();
        Integer z = 0;
        for (Tienda ti : tiendas) {
            TiendaHelper helperTienda = new TiendaHelper();
            Tienda tienda = helperTienda.getStore(ti.getId().intValue());
            HashMap<Insumo, Integer> insumosTienda = tienda.getInsumos();
            HashMap<Producto, Integer> maxStockProductos = new HashMap<>();
            ArrayList<Producto> prod = new ArrayList<>(); //Para sacar random productos con stock
            productos.forEach((t) -> {
                Integer s = mostrarMaximoStockProducto(t, insumosTienda);
                if (s > 0) {
                    maxStockProductos.put(t, s);
                    prod.add(t);
                }

            });
            String[] dirUsuario = dir[z];
            Integer r = 0;
            for (int i = 0; i < cantidadPedidos; i++) {
                if (prod.isEmpty()) {
                    break;
                }
                ArrayList<Producto> disponibles = new ArrayList(prod);
                Usuario cliente = clientes.get(i);
                Pedido pedido = new Pedido();
                pedido.setCliente(cliente);
                pedido.setDireccionDeEnvio(dirUsuario[r]);
                pedido.setCooXDireccion(coo[z][r][0]);
                pedido.setCooYDireccion(coo[z][r][1]);
                pedido.setTienda(tienda);
                pedido.setMensajeDescripicion("Mensaje");
                PedidoEstadoHelper hp = new PedidoEstadoHelper();
                PedidoEstado estado = hp.getEstadoByName(Constantes.ESTADO_VENTA);
                pedido.addEstado(estado);
                pedido.setActivo(true);
                pedido.setEstado(estado);
                pedido.setTurno("T");
                hp.close();
                Date date = new Date();
                Timestamp timeStamp = new Timestamp(date.getTime());
                pedido.setFechaVenta(timeStamp);
                r = r + 1;
                Set<DetallePedido> detalles = new HashSet<>();
                int n = rand.nextInt(cantidadDetalles) + 1;
                Double total = 0.0;
                Double volumen = 0.0;
                for (int j = 0; j < n; j++) {
                    if (disponibles.size() == 0) {
                        break;
                    }
                    System.out.println(disponibles.size());
                    int indexProd = rand.nextInt(disponibles.size());
                    Producto producto = disponibles.get(indexProd);
                    DetallePedido detalle = new DetallePedido();
                    detalle.setProducto(producto);
                    detalle.setActivo(true);
                    detalle.setPrecioUnitario(producto.getPrecio());
                    
                    int cant = rand.nextInt(5) + 1;
                    if (cant > maxStockProductos.get(producto)) {
                        cant = maxStockProductos.get(producto);
                    }
                    if (cant == 0) {
                        disponibles.remove(producto);
                        prod.remove(producto);//Si ya no hay posibilida de armar el producto en esta tienda se retira
                        continue;
                    }
                    total = total + producto.getPrecio() * cant;
                    volumen = volumen  + producto.getVolumen() * cant;
                    detalle.setCantidad(cant);
                    detalle.setNumEntregados(0);

                    //Verifica si tiene descuentos
                    ProductoDescuentoHelper helperdescprod = new ProductoDescuentoHelper();
                    ProductoDescuento descuento = helperdescprod.getDescuentoByProducto(producto.getId().intValue());
                    helper.close();
                    ProductoCategoriaDescuentoHelper hcar = new ProductoCategoriaDescuentoHelper();
                    ProductoCategoriaDescuento descCat = hcar.getDescuentoByCategoria(producto.getCategoria().getId().intValue());
                    hcar.close();
                    if (descuento != null && descCat != null) {
                        if (descuento.getValorPct() > descCat.getValue()) {
                            detalle.setDescuentoProducto(descuento);
                            detalle.setDescuentoCategoria(null);
                        } else {
                            detalle.setDescuentoCategoria(descCat);
                            detalle.setDescuentoProducto(null);
                        }

                    } else if (descuento != null) {
                        detalle.setDescuentoProducto(descuento);
                        detalle.setDescuentoCategoria(null);
                    } else if (descCat != null) {
                        detalle.setDescuentoCategoria(descCat);
                        detalle.setDescuentoProducto(null);
                    } else {
                        detalle.setDescuentoProducto(null);
                        detalle.setDescuentoCategoria(null);
                    }
                    detalle.setCombo(null);
                    detalle.setPedido(pedido);
                    detalles.add(detalle);
                    disponibles.remove(producto);
                    recalcularStockProducto(producto, cant, 0, insumosTienda);
                }
                pedido.setTotal(total - HomeController.IGV * total);
                pedido.setVolumenTotal(volumen);
                if (detalles.size() > 0) {
                    pedido.setDetallePedido(detalles);
                    HashMap<Insumo, Integer> insumosRequeridos = new HashMap<>();
                    for (DetallePedido dp : pedido.getDetallePedido()) {
                        if (dp.getProducto() != null) {
                            calcularInsumos(dp.getProducto(), dp.getCantidad(), insumosRequeridos);
                        }
                    }

                    LoteInsumoHelper lihelper = new LoteInsumoHelper();
                    Boolean ok = lihelper.descontarInsumos(insumosRequeridos, pedido.getTienda(), pedido);
                    pedidos.add(pedido);
                    lihelper.close();
                }

            }
            z = z + 1;
            helperTienda.close();
            
        }
        setDirecciones();

    }

    void setDirecciones() {
        pedidos.forEach((t) -> {
            try {
                GMapsHelper helper = GMapsHelper.getInstance();
                Pair<Double, Double> pair = helper.geocodeAddress(t.getDireccionDeEnvio());
                t.setCooXDireccion(pair.getLeft());
                t.setCooYDireccion(pair.getRight());
                
            } catch (ApiException | InterruptedException | IOException ex) {
                Logger.getLogger(SolicitarDireccionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    void calcularInsumos(Producto p, Integer cantidad, HashMap<Insumo, Integer> insumos) {
        ProductoHelper helper = new ProductoHelper();
        p= helper.getProductById(p.getId().intValue());
        ArrayList<ProductoInsumo> pxi = new ArrayList(p.getProductoxInsumos());
        for (ProductoInsumo productoInsumo : pxi) {
            if (insumos.get(productoInsumo.getInsumo()) != null) {
                insumos.put(productoInsumo.getInsumo(), productoInsumo.getCantidad().intValue() * cantidad + insumos.get(productoInsumo.getInsumo()));
            } else {
                insumos.put(productoInsumo.getInsumo(), productoInsumo.getCantidad().intValue() * cantidad);
            }
        }
    }

    public void recalcularStockProducto(Producto producto, Integer nuevoValor, Integer viejoValor, HashMap<Insumo, Integer> insumos) {//Producto

        ProductoHelper helper = new ProductoHelper();
        producto = helper.getProductById(producto.getId().intValue());
        ArrayList<ProductoInsumo> productoxinsumos = new ArrayList(producto.getProductoxInsumos());
        for (int i = 0; i < productoxinsumos.size(); i++) {
            ProductoInsumo get = productoxinsumos.get(i);
            System.out.println(nuevoValor + " + " + viejoValor);
            if (insumos.get(get.getInsumo()) != null) {
                Double nuevoStockInsumo = insumos.get(get.getInsumo()) - nuevoValor * get.getCantidad() + (viejoValor) * get.getCantidad();
                insumos.put(get.getInsumo(), nuevoStockInsumo.intValue());
            }
        }
        insumos.forEach((t, u) -> {
            System.out.println(t.getNombre() + "->" + u.toString());
        });

    }

    public Integer mostrarMaximoStockProducto(Producto t, HashMap<Insumo, Integer> insumos) {
        Integer st = Integer.MAX_VALUE;
        ProductoHelper helper = new ProductoHelper();
        t = helper.getProductById(t.getId().intValue());
        for (ProductoInsumo p : t.getProductoxInsumos()) {
            Integer cantidadInsumo = insumos.get(p.getInsumo());
            cantidadInsumo = (cantidadInsumo == null) ? 0 : cantidadInsumo;
            Double posStock = cantidadInsumo / (p.getCantidad());// Si no tengo insumos que se requiere no se debe caer
            st = (posStock.intValue() < st) ? posStock.intValue() : st;
        }
        return st;
    }
}
