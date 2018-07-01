/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.app.controller.LoginController;
import com.sigad.sigad.business.ComboPromocion;
import com.sigad.sigad.business.DetallePedido;
import com.sigad.sigad.business.Insumo;
import com.sigad.sigad.business.MovimientosTienda;
import com.sigad.sigad.business.Pedido;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoCategoriaDescuento;
import com.sigad.sigad.business.ProductoDescuento;
import com.sigad.sigad.business.ProductoInsumo;
import com.sigad.sigad.business.ProductosCombos;
import com.sigad.sigad.business.Tienda;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.CapacidadTiendaHelper;
import com.sigad.sigad.business.helpers.ComboPromocionHelper;
import com.sigad.sigad.business.helpers.GeneralHelper;
import com.sigad.sigad.business.helpers.InsumosHelper;
import com.sigad.sigad.business.helpers.LoteInsumoHelper;
import com.sigad.sigad.business.helpers.MovimientoHelper;
import com.sigad.sigad.business.helpers.PedidoHelper;
import com.sigad.sigad.business.helpers.ProductoCategoriaDescuentoHelper;
import com.sigad.sigad.business.helpers.ProductoDescuentoHelper;
import com.sigad.sigad.business.helpers.ProductoHelper;
import com.sigad.sigad.business.helpers.TiendaHelper;
import com.sigad.sigad.business.helpers.UsuarioHelper;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Alexandra
 */
public class SeleccionarProductosController implements Initializable {

    public static final String viewPath = "/com/sigad/sigad/pedido/view/seleccionarProductos.fxml";
    /**
     * Initializes the controller class.
     */

    @FXML
    private JFXTreeTableView<ProductoLista> treeView;
    @FXML
    private JFXTreeTableView<PedidoLista> treeViewPedido;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private StackPane stackPane;

    @FXML
    JFXButton moreBtn;
    @FXML
    private JFXPopup popup;
    @FXML
    public static JFXDialog userDialog;

    @FXML
    JFXTreeTableColumn<ProductoLista, Boolean> select = new JFXTreeTableColumn<>("Seleccionar");
    @FXML
    JFXTreeTableColumn<ProductoLista, Image> imagen = new JFXTreeTableColumn<>("Imagen");
    @FXML
    JFXTreeTableColumn<ProductoLista, String> nombre = new JFXTreeTableColumn<>("Nombre");
    @FXML
    JFXTreeTableColumn<ProductoLista, String> precio = new JFXTreeTableColumn<>("Precio");
    @FXML
    JFXTreeTableColumn<ProductoLista, String> stock = new JFXTreeTableColumn<>("Max.Stock");
    @FXML
    JFXTreeTableColumn<ProductoLista, String> categoria = new JFXTreeTableColumn<>("Categoria");
    @FXML
    JFXTreeTableColumn<ProductoLista, String> almacen = new JFXTreeTableColumn<>("Almacen");
    @FXML
    JFXTreeTableColumn<PedidoLista, String> nombrePedido = new JFXTreeTableColumn<>("Nombre");
    @FXML
    JFXTreeTableColumn<PedidoLista, String> precioPedido = new JFXTreeTableColumn<>("Precio");
    @FXML
    JFXTreeTableColumn<PedidoLista, String> subTotalPedido = new JFXTreeTableColumn<>("SubTotal");
    @FXML
    JFXTreeTableColumn<PedidoLista, Integer> cantidadPedido = new JFXTreeTableColumn<>("Cantidad");
    @FXML
    JFXTreeTableColumn<PedidoLista, String> descuentoPedido = new JFXTreeTableColumn<>("Descuento(%)");

    @FXML
    private JFXTextField filtro;
    @FXML
    private JFXButton btnContinuar;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lbligv;
    PedidoLista pedidoListaSeleccionado;

    private Pedido pedido;
    private HashMap<Insumo, Integer> insumosCambiantes = new HashMap<>();
    private HashMap<Insumo, Integer> insumosConsumidosIniciales = new HashMap<>();//Usado solo cuando lo editasf
    private final ObservableList<PedidoLista> pedidos = FXCollections.observableArrayList();
    private final ObservableList<ProductoLista> prod = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        columnasPedidos();
        columnasProductos();
        agregarColumnasTablasPedidos();
        agregarColumnasTablasProductos();
        agregarFiltro();

    }

    public void agregarFiltro() {
        filtro.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            treeView.setPredicate((TreeItem<ProductoLista> t) -> {
                Boolean flag = t.getValue().getNombre().getValue().contains(newValue) || t.getValue().getCategoria().getValue().contains(newValue);
                return flag;
            });
        });
    }

    @FXML
    void clickBotonContinuar(MouseEvent event) {
        if (validarCamposLlenos()) {
            goSeleccionarCliente();
        } else {
            ErrorController error = new ErrorController();
            error.loadDialog("Atención", "Debe agregar un producto con stock diferente a 0", "Ok", stackPane);
        }
    }

    public boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            // s is not numeric
            return false;
        }
    }

    public void columnasProductos() {
        select.setPrefWidth(80);
        select.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, Boolean> param) -> param.getValue().getValue().getSeleccion());
        //select.setCellFactory(CheckBoxTreeTableCell.forTreeTableColumn(select));
        select.setCellFactory((TreeTableColumn<ProductoLista, Boolean> p) -> {
            CheckBoxTreeTableCell<ProductoLista, Boolean> cell = new CheckBoxTreeTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        imagen.setPrefWidth(120);
        imagen.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, Image> param) -> param.getValue().getValue().imagen.imageProperty());
        imagen.setCellFactory((TreeTableColumn<ProductoLista, Image> param) -> {
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(50);
            imageview.setFitWidth(50);
            return new TreeTableCell<ProductoLista, Image>() {
                @Override
                protected void updateItem(Image item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setAlignment(Pos.CENTER);
                    if (item != null) {  // choice of image is based on values from item, but it doesn't matter now
                        imageview.setImage(item);
                    } else {
                        imageview.setImage(null);
                    }
                    this.setGraphic(imageview);
                }
            };
        });

        nombre.setPrefWidth(120);
        nombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().getNombre());

        precio.setPrefWidth(120);
        precio.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().getPrecio());

        stock.setPrefWidth(120);
        stock.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().getStock());

        categoria.setPrefWidth(120);
        categoria.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().getCategoria());

        almacen.setPrefWidth(120);
        almacen.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().getAlmacen());
    }

    public void columnasPedidos() {

        nombrePedido.setPrefWidth(180);
        nombrePedido.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PedidoLista, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<PedidoLista, String> param) {
                return param.getValue().getValue().nombre;
            }
        });

        cantidadPedido.setPrefWidth(80);
        cantidadPedido.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PedidoLista, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TreeTableColumn.CellDataFeatures<PedidoLista, Integer> param) {
                return param.getValue().getValue().cantidad.asObject();
            }
        });
        cantidadPedido.setCellFactory(new Callback<TreeTableColumn<PedidoLista, Integer>, TreeTableCell<PedidoLista, Integer>>() {
            @Override
            public TreeTableCell<PedidoLista, Integer> call(TreeTableColumn<PedidoLista, Integer> param) {
                return new EditingCell();
            }
        });
        cantidadPedido.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<PedidoLista, Integer>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<PedidoLista, Integer> event) {

                PedidoLista ped = event.getRowValue().getValue();
                Integer index = prod.indexOf(new ProductoLista("", "", "0", "", "", viewPath, ped.codigo, ped.producto, false));
                ProductoLista p = prod.get(index);
                System.out.println(p.nombre.getValue() + p.stock.getValue());
                Integer stock = Integer.valueOf(p.stock.getValue());
                Double precio = Double.valueOf(ped.precio.get().replaceAll(",", "."));
                Double descuentos = (ped.descuento == null) ? 0.0 : Double.valueOf(ped.descuento.get().replaceAll(",", ".")) / 100.0;
                Double subNew = GeneralHelper.roundTwoDecimals(Float.valueOf(event.getNewValue()) * precio * (1 - descuentos));
                Double subOld = GeneralHelper.roundTwoDecimals(Float.valueOf(event.getOldValue()) * precio * (1 - descuentos));
                PedidoLista nuevo = null;
                if (ped.descuentoProducto != null) {
                    nuevo = new PedidoLista((event.getNewValue() <= stock + event.getOldValue()) ? event.getNewValue() : event.getOldValue(),
                            (event.getNewValue() <= stock + event.getOldValue()) ? subNew.toString() : subOld.toString(),
                            ped.producto, ped.descuentoProducto);
                } else if (ped.descuentoCategoria != null) {
                    nuevo = new PedidoLista((event.getNewValue() <= stock + event.getOldValue()) ? event.getNewValue() : event.getOldValue(),
                            (event.getNewValue() <= stock + event.getOldValue()) ? subNew.toString() : subOld.toString(),
                            ped.producto, ped.descuentoCategoria);

                } else if (ped.combo != null) {
                    nuevo = new PedidoLista((event.getNewValue() <= stock + event.getOldValue()) ? event.getNewValue() : event.getOldValue(),
                            (event.getNewValue() <= stock + event.getOldValue()) ? subNew.toString() : subOld.toString(), ped.combo);
                } else {
                    nuevo = new PedidoLista((event.getNewValue() <= stock + event.getOldValue()) ? event.getNewValue() : event.getOldValue(),
                            (event.getNewValue() <= stock + event.getOldValue()) ? subNew.toString() : subOld.toString(), ped.producto);
                }
                nuevo.detallePedido = ped.detallePedido;
                Integer i = pedidos.indexOf(new PedidoLista(nuevo.nombre.getValue(), viewPath, index, viewPath, nuevo.codigo, viewPath, nuevo.codigo, null, null));
                Integer oldValue = event.getOldValue();
                System.out.println("i->>>>>" + i);
                if (event.getNewValue() <= stock + oldValue && i >= 0) {
                    System.out.println("rec->" + event.getNewValue().toString());
                    recalcularStockDetalle(p, event.getNewValue(), event.getOldValue());
                    mostrarMaximoStock();

                }
                pedidos.remove(nuevo);
                pedidos.add(i, nuevo);
                if (event.getNewValue() <= stock + oldValue) {
                    calcularTotal();
                } else {
                    ErrorController err = new ErrorController();
                    err.loadDialog("Aviso", "No hay suficientes insumos para la cantidad de producto que solicita", "Ok", stackPane);
                }

            }
        });
//       

        precioPedido.setPrefWidth(80);
        precioPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, String> param) -> param.getValue().getValue().precio);

        subTotalPedido.setPrefWidth(80);
        subTotalPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, String> param) -> param.getValue().getValue().subtotal);

        descuentoPedido.setPrefWidth(80);
        descuentoPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, String> param) -> param.getValue().getValue().descuento);

    }

    public void retornarInsumos(ProductoLista producto, Integer cant) {

    }

    public void recalcularStockProducto(Producto producto, Integer nuevoValor, Integer viejoValor) {//Producto

        ProductoHelper helper = new ProductoHelper();
        producto = helper.getProductById(producto.getId().intValue());
        ArrayList<ProductoInsumo> productoxinsumos = new ArrayList(producto.getProductoxInsumos());
        for (int i = 0; i < productoxinsumos.size(); i++) {
            ProductoInsumo get = productoxinsumos.get(i);
            System.out.println(nuevoValor + " + " + viejoValor);
            if (insumosCambiantes.get(get.getInsumo()) != null) {
                Double nuevoStockInsumo = insumosCambiantes.get(get.getInsumo()) - nuevoValor * get.getCantidad() + (viejoValor) * get.getCantidad();
                insumosCambiantes.put(get.getInsumo(), nuevoStockInsumo.intValue());
            }
        }
        insumosCambiantes.forEach((t, u) -> {
            System.out.println(t.getNombre() + "->" + u.toString());
        });

    }

    /**
     *
     * @param item
     * @param nuevoValor
     * @param viejoValor
     */
    public void recalcularStockDetalle(ProductoLista item, Integer nuevoValor, Integer viejoValor) {
        if (item.combo != null) {
            ComboPromocion combo = item.combo;
            for (ProductosCombos combopromo : combo.getProductosxComboArray()) {
                Producto p = combopromo.getProducto();
                Integer cantidad = combopromo.getCantidad();
                recalcularStockProducto(p, nuevoValor * cantidad, viejoValor * cantidad);
            }

        } else if (item.producto != null) {
            recalcularStockProducto(item.getProducto(), nuevoValor, viejoValor);
        }
    }

    public Integer mostrarMaximoStockProducto(Producto t) {
        Integer st = Integer.MAX_VALUE;
        ProductoHelper helper = new ProductoHelper();
        t = helper.getProductById(t.getId().intValue());
        for (ProductoInsumo p : t.getProductoxInsumos()) {
            Integer cantidadInsumo = insumosCambiantes.get(p.getInsumo());
            cantidadInsumo = (cantidadInsumo == null) ? 0 : cantidadInsumo;
            Double posStock = cantidadInsumo / (p.getCantidad());// Si no tengo insumos que se requiere no se debe caer
            st = (posStock.intValue() < st) ? posStock.intValue() : st;
        }
        return st;
    }

    public void mostrarMaximoStock() {
        prod.forEach((t) -> {
            if (t.producto != null) {
                Integer st = mostrarMaximoStockProducto(t.getProducto());
                prod.get(prod.indexOf(t)).stock.setValue(st.toString());
            } else if (t.combo != null) {
                Integer ct = Integer.MAX_VALUE;
                ComboPromocionHelper helper = new ComboPromocionHelper();
                t.combo = helper.getComboById(t.combo.getId().intValue());
                for (ProductosCombos c : t.combo.getProductosxComboArray()) {
                    Integer st = mostrarMaximoStockProducto(c.getProducto()) / c.getCantidad();
                    ct = (st < ct) ? st : ct;
                }
                if (t.combo.getMaxDisponible() != null && t.combo.getMaxDisponible() != null
                        && t.combo.getMaxDisponible() - t.combo.getNumVendidos() < ct) {
                    ct = t.combo.getMaxDisponible() - t.combo.getNumVendidos();
                }
                prod.get(prod.indexOf(t)).stock.setValue(ct.toString());
            }
        });
    }

    public Boolean validarCamposLlenos() {
        try {
            Double total = Double.valueOf(lblTotal.getText());
            for (PedidoLista ped : pedidos) {
                if (ped.cantidad.getValue() == 0) {
                    return false;
                }
            }
            if (total == 0) {
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public void agregarColumnasTablasProductos() {
        final TreeItem<ProductoLista> root = new RecursiveTreeItem<>(prod, RecursiveTreeObject::getChildren);

        treeView.setEditable(true);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView.getColumns().setAll(select, imagen, nombre, precio, stock, categoria);
        treeView.setRoot(root);
        treeView.setShowRoot(false);

        treeView.setRowFactory(new Callback<TreeTableView<ProductoLista>, TreeTableRow<ProductoLista>>() {
            @Override
            public TreeTableRow<ProductoLista> call(TreeTableView<ProductoLista> param) {
                TreeTableRow<ProductoLista> row = new TreeTableRow<>();
                row.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        ProductoLista rowData = row.getItem();
                        if (rowData.producto != null) {
                            mostrarInfoProducto(rowData.getCodigo());
                        } else {
                            mostrarInfoCombo(rowData.combo);
                        }

                    }
                });
                return row; //To change body of generated lambdas, choose Tools | Templates.
            }
        });
    }

    public void mostrarInfoProducto(Integer codigo) {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Producto"));
            Node node;
            //           node = (Node) FXMLLoader.load(SeleccionarProductosController.this.getClass().getResource(DescripcionProductosController.viewPath));

            FXMLLoader loader = new FXMLLoader(SeleccionarProductosController.this.getClass().getResource(DescripcionProductosController.viewPath));
            node = (Node) loader.load();
            DescripcionProductosController desc = loader.getController();
            desc.initModel(codigo);

            content.setBody(node);
            userDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
            userDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(DescripcionProductosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarInfoCombo(ComboPromocion combo) {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Producto"));
            Node node;
            //           node = (Node) FXMLLoader.load(SeleccionarProductosController.this.getClass().getResource(DescripcionProductosController.viewPath));

            FXMLLoader loader = new FXMLLoader(SeleccionarProductosController.this.getClass().getResource(DescripcionProductosController.viewPath));
            node = (Node) loader.load();
            DescripcionProductosController desc = loader.getController();
            desc.initModel(combo);

            content.setBody(node);
            userDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
            userDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(DescripcionProductosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goSeleccionarCliente() {

        if (pedido.getId() != null) {
            actualizarPedido();
        } else {
            try {
                construirPedido();
                Node node;
                FXMLLoader loader = new FXMLLoader(SeleccionarProductosController.this.getClass().getResource(SeleccionarClienteController.viewPath));
                node = (Node) loader.load();
                SeleccionarClienteController desc = loader.getController();
                desc.initModel(pedido, stackPane);
                stackPane.getChildren().setAll(node);
            } catch (IOException ex) {
                Logger.getLogger(SeleccionarClienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void construirPedido() {//Parte 1(Vea la continuaciin en DatosPedidoController
        Set<DetallePedido> detalles = new HashSet<>();
        pedido.setVolumenTotal(0.0);
        pedido.setTotal(0.0);
        pedidos.forEach((t) -> {
            if (t.combo != null) {
                DetallePedido detalle = new DetallePedido(true, t.cantidad.getValue(), Double.valueOf(t.precio.getValue()), 0, t.combo, pedido);
                detalles.add(detalle);
                pedido.setVolumenTotal(pedido.getVolumenTotal() + t.combo.getVolumen());
                pedido.setTotal(pedido.getTotal() + t.combo.getPreciounireal() * t.cantidad.getValue());
            } else if (t.producto != null) {
                DetallePedido detalle = new DetallePedido(true, t.cantidad.getValue(), Double.valueOf(t.precio.getValue()), 0, t.producto, pedido, t.descuentoProducto, t.descuentoCategoria);
                detalles.add(detalle);
                pedido.setVolumenTotal(pedido.getVolumenTotal() + t.producto.getVolumen());
                if (t.descuentoCategoria != null) {
                    pedido.setTotal(pedido.getTotal() + t.producto.getPrecio() * t.cantidad.getValue() * (1 - t.descuentoCategoria.getValue()));
                } else if (t.descuentoProducto != null) {
                    pedido.setTotal(pedido.getTotal() + t.producto.getPrecio() * t.cantidad.getValue() * (1 - t.descuentoProducto.getValorPct()));
                } else {
                    pedido.setTotal(pedido.getTotal() + t.producto.getPrecio() * t.cantidad.getValue());
                }

            }

        });
        pedido.setDetallePedido(detalles);
        pedido.setActivo(true);
        pedido.setModificable(true);
    }

//    public void actualizarPedido() {
//        HashMap<Insumo, Integer> nuevosInsumosConsumidos = new HashMap<>();
//        HashMap<Insumo, Integer> insumosADevolver = new HashMap<>();
//        Set<DetallePedido> detalles = new HashSet<>();
//        pedido.setVolumenTotal(0.0);
//        pedido.setTotal(0.0);
//        pedidos.forEach((t) -> {
//            if (t.combo != null) {
//                DetallePedido detalle = new DetallePedido(true, t.cantidad.getValue(), Double.valueOf(t.precio.getValue()), 0, t.combo, pedido);
//                Integer cantidadADescontar = t.cantidad.getValue();
//                Integer cantidadADevolver = 0;
//                if (t.detallePedido != null) {
//                    detalle.setId(t.detallePedido.getId());
//                    cantidadADescontar = 0;// 0 por que se supone al inicio que no ha habido ni un cambio
//                    if (detalle.getCantidad() < t.detallePedido.getCantidad()) {//Comparar lo que tengo ahora y lo que tenia
//                        //En este caso en el que la cantidad que tengo ahora es menor a la que tenia
//                        //se procede a una devolucion de los insumos a los que pertenecia
//                        //Primero se busca de que lotes de donde los saque y los devuelvo//fin
//                        cantidadADevolver = t.detallePedido.getCantidad() - detalle.getCantidad();
//                    } else if (detalle.getCantidad() > t.detallePedido.getCantidad()) {
//                        //En el caso que la cantidad que tengo es mayor de la que tenia
//                        //Significa que tengo que consumir mas insumos por lo que calculo la diferencia con lo que tenia
//                        //Y los agrego a mis insumos consumidos
//                        cantidadADescontar = detalle.getCantidad() - t.detallePedido.getCantidad();
//                    }
//
//                }
//                System.out.println("Cantidad a descontar--->" + t.combo.getNombre() + "-->" + cantidadADescontar);
//                System.out.println("Cantidad a devolver--->" + t.combo.getNombre() + "-->" + cantidadADevolver);
//
//                ComboPromocion p = t.combo;
//                for (ProductosCombos productosCombos : p.getProductosxComboArray()) {
//                    calcularInsumos(productosCombos.getProducto(), productosCombos.getCantidad() * cantidadADevolver, insumosADevolver);
//                    calcularInsumos(productosCombos.getProducto(), productosCombos.getCantidad() * cantidadADescontar, nuevosInsumosConsumidos);
//                }
//                detalles.add(detalle);
//                pedido.setVolumenTotal(pedido.getVolumenTotal() + t.combo.getVolumen() * t.cantidad.getValue());
//                pedido.setTotal(pedido.getTotal() + t.combo.getPreciounireal() * t.cantidad.getValue());
//            } else if (t.producto != null) {
//                DetallePedido detalle = new DetallePedido(true, t.cantidad.getValue(), Double.valueOf(t.precio.getValue()), 0, t.producto, pedido, t.descuentoProducto, t.descuentoCategoria);
//                Integer cantidadADescontar = t.cantidad.getValue();//Descontar Insumos
//                Integer cantidadADevolver = 0;
//                if (t.detallePedido != null) {
//                    cantidadADescontar = 0;
//                    detalle.setId(t.detallePedido.getId());
//                    if (detalle.getCantidad() < t.detallePedido.getCantidad()) {//Comparar lo que tengo ahora y lo que tenia
//                        //En este caso en el que la cantidad que tengo ahora es menor a la que tenia
//                        //se procede a una devolucion de los insumos a los que pertenecia
//                        //Primero se busca de que lotes de donde los saque y los devuelvo//fin
//                        cantidadADevolver = t.detallePedido.getCantidad() - detalle.getCantidad();
//                    } else if (detalle.getCantidad() > t.detallePedido.getCantidad()) {
//                        //En el caso que la cantidad que tengo es mayor de la que tenia
//                        //Significa que tengo que consumir mas insumos por lo que calculo la diferencia con lo que tenia
//                        //Y los agrego a mis insumos consumidos
//                        cantidadADescontar = detalle.getCantidad() - t.detallePedido.getCantidad();
//                    }
//
//                }
//                System.out.println("Cantidad a descontar--->" + t.producto.getNombre() + "-->" + cantidadADescontar);
//                System.out.println("Cantidad a devolver--->" + t.producto.getNombre() + "-->" + cantidadADevolver);
//                nuevosInsumosConsumidos.forEach((o, u) -> {
//                    System.out.println("Nuevos insumos consumidos>Antes---> " + o.getNombre() + "--" + u);
//                });
//                insumosADevolver.forEach((o, u) -> {
//                    System.out.println("Insumos a devolver>Antes---> " + o.getNombre() + "--" + u);
//                });
//                calcularInsumos(detalle.getProducto(), cantidadADevolver, insumosADevolver);
//                calcularInsumos(t.producto, cantidadADescontar, nuevosInsumosConsumidos);
//                nuevosInsumosConsumidos.forEach((o, u) -> {
//                    System.out.println("Nuevos insumos consumidos>Despues---> " + o.getNombre() + "--" + u);
//                });
//                insumosADevolver.forEach((o, u) -> {
//                    System.out.println("Insumos a devolver>Despues---> " + o.getNombre() + "--" + u);
//                });
//                detalles.add(detalle);
//                pedido.setVolumenTotal(pedido.getVolumenTotal() + t.producto.getVolumen() * t.cantidad.getValue());
//                if (t.descuentoCategoria != null) {
//                    pedido.setTotal(pedido.getTotal() + t.producto.getPrecio() * t.cantidad.getValue() * (1 - t.descuentoCategoria.getValue()));
//                } else if (t.descuentoProducto != null) {
//                    pedido.setTotal(pedido.getTotal() + t.producto.getPrecio() * t.cantidad.getValue() * (1 - t.descuentoProducto.getValorPct()));
//                } else {
//                    pedido.setTotal(pedido.getTotal() + t.producto.getPrecio() * t.cantidad.getValue());
//                }
//
//            } else {
//                System.out.println("Error, no es ni combo ni producto");
//            }
//
//        });
//
//        pedido.setDetallePedido(detalles);
//        pedido.setActivo(true);
//        pedido.setModificable(true);
//        mergeInsumosConsumidosDevueltos(nuevosInsumosConsumidos, insumosADevolver);
//        nuevosInsumosConsumidos.forEach((t, u) -> {
//            System.out.println("Nuevos insumos consumidos---> " + t.getNombre() + "--" + u);
//        });
//        insumosADevolver.forEach((t, u) -> {
//            System.out.println("Insumos a devolver---> " + t.getNombre() + "--" + u);
//        });
//        MovimientoHelper movHelper = new MovimientoHelper();
//        ArrayList<MovimientosTienda> movimientos = movHelper.getLogicMovements(pedido);
//        LoteInsumoHelper lihelper = new LoteInsumoHelper();
////        lihelper.devolverInsumos(insumosADevolver, pedido, movimientos);
////        Boolean ok = lihelper.descontarInsumos(nuevosInsumosConsumidos, pedido.getTienda(), pedido);
//        if (true) {
//            ErrorController err = new ErrorController();
//            err.loadDialog("Alerta", "El pedido fue actualizado satisfactoriamente", "ok", stackPane);
//        } else {
//            ErrorController err = new ErrorController();
//            err.loadDialog("Alerta", "No hay insumos", "ok", stackPane);
//        }
//
//    }
    public void actualizarPedido() {
        HashMap<Insumo, Integer> insumosConsumidosFinal = new HashMap<>();

        Set<DetallePedido> detalles = new HashSet<>();
        pedido.setVolumenTotal(0.0);
        pedido.setTotal(0.0);
        pedidos.forEach((t) -> {
            if (t.combo != null) {
                DetallePedido detalle = new DetallePedido(true, t.cantidad.getValue(), Double.valueOf(t.precio.getValue()), 0, t.combo, pedido);
                if (t.detallePedido != null) {
                    detalle.setId(t.detallePedido.getId());
                }
                ComboPromocion p = t.combo;
                for (ProductosCombos productosCombos : p.getProductosxComboArray()) {
                    calcularInsumos(productosCombos.getProducto(), productosCombos.getCantidad() * t.cantidad.getValue(), insumosConsumidosFinal);
                }
                detalles.add(detalle);
                pedido.setVolumenTotal(pedido.getVolumenTotal() + t.combo.getVolumen() * t.cantidad.getValue());
                pedido.setTotal(pedido.getTotal() + t.combo.getPreciounireal() * t.cantidad.getValue());
            } else if (t.producto != null) {
                DetallePedido detalle = new DetallePedido(true, t.cantidad.getValue(), Double.valueOf(t.precio.getValue()), 0, t.producto, pedido, t.descuentoProducto, t.descuentoCategoria);
                if (t.detallePedido != null) {
                    detalle.setId(t.detallePedido.getId());
                }
                calcularInsumos(detalle.getProducto(), t.cantidad.getValue(), insumosConsumidosFinal);
                detalles.add(detalle);
                pedido.setVolumenTotal(pedido.getVolumenTotal() + t.producto.getVolumen() * t.cantidad.getValue());
                if (t.descuentoCategoria != null) {
                    pedido.setTotal(pedido.getTotal() + t.producto.getPrecio() * t.cantidad.getValue() * (1 - t.descuentoCategoria.getValue()));
                } else if (t.descuentoProducto != null) {
                    pedido.setTotal(pedido.getTotal() + t.producto.getPrecio() * t.cantidad.getValue() * (1 - t.descuentoProducto.getValorPct()));
                } else {
                    pedido.setTotal(pedido.getTotal() + t.producto.getPrecio() * t.cantidad.getValue());
                }

            } else {
                System.out.println("Error, no es ni combo ni producto");
            }

        });

        pedido.setDetallePedido(detalles);
        pedido.setActivo(true);
        pedido.setModificable(true);

        HashMap<Insumo, Integer> nuevosInsumosConsumidos = new HashMap<>();
        HashMap<Insumo, Integer> insumosADevolver = new HashMap<>();
        obtenerInsumosAConsumiryADevolver(insumosConsumidosIniciales, insumosConsumidosFinal, nuevosInsumosConsumidos, insumosADevolver);
        nuevosInsumosConsumidos.forEach((t, u) -> {
            System.out.println("Nuevos insumos consumidos---> " + t.getNombre() + "--" + u);
        });
        insumosADevolver.forEach((t, u) -> {
            System.out.println("Insumos a devolver---> " + t.getNombre() + "--" + u);
        });
        MovimientoHelper movHelper = new MovimientoHelper();
        ArrayList<MovimientosTienda> movimientos = movHelper.getLogicMovements(pedido);
        movHelper.close();
        LoteInsumoHelper lihelper = new LoteInsumoHelper();
        lihelper.devolverInsumosEditar(insumosADevolver, pedido, movimientos);
        Boolean ok = lihelper.descontarInsumos(nuevosInsumosConsumidos, pedido.getTienda(), pedido);
        if (true) {
            ErrorController err = new ErrorController();
            err.loadDialog("Alerta", "El detalle del pedido fue actualizado satisfactoriamente", "ok", stackPane);
            gotoEditarDatosPedido();
        } else {
            ErrorController err = new ErrorController();
            err.loadDialog("Alerta", "No hay insumos", "ok", stackPane);
        }
    }

    public void gotoEditarDatosPedido() {
        try {
            Node node;
            FXMLLoader loader = new FXMLLoader(SeleccionarProductosController.this.getClass().getResource(DatosPedidoController.viewPath));
            node = (Node) loader.load();
            DatosPedidoController desc = loader.getController();
            desc.initModel(pedido, stackPane);
            stackPane.getChildren().setAll(node);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(DescripcionProductosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initModel(Pedido ped, StackPane stack) {
        this.stackPane = stack;
        this.pedido = ped;
        //Basede datos
        TiendaHelper h = new TiendaHelper();
        Tienda tienda = h.getStore(pedido.getTienda().getId().intValue());
        insumosCambiantes = tienda.getInsumos();
        ArrayList<DetallePedido> detalle = new ArrayList<>();
        if (pedido.getId() != null) {
            PedidoHelper pedidohelper = new PedidoHelper();
            this.pedido = pedidohelper.getPedidoEager(pedido.getId());
            detalle = new ArrayList(pedido.getDetallePedido());
            detalle.forEach((t) -> {
                pedidos.add(new PedidoLista(t));
                if (t.getCombo() != null) {
                    for (ProductosCombos productosCombos : t.getCombo().getProductosxComboArray()) {
                        calcularInsumos(productosCombos.getProducto(), productosCombos.getCantidad() * t.getCantidad(), insumosConsumidosIniciales);
                    }
                } else if (t.getProducto() != null) {
                    calcularInsumos(t.getProducto(), t.getCantidad(), insumosConsumidosIniciales);
                }
            });
        }

        ProductoHelper gest = new ProductoHelper();
        ArrayList<Producto> productosDB = gest.getProducts();
        gest.close();
        if (productosDB != null) {
            for (Producto t : productosDB) {
                boolean esta = false;
                for (PedidoLista pedido1 : pedidos) {
                    if (pedido1.producto != null) {
                        esta = (pedido1.producto.getId().equals(t.getId()));
                        if (esta) {
                            break;
                        }
                    }
                }
                if (!esta) {
                    prod.add(new ProductoLista(t.getNombre(), t.getPrecio().toString(), "0", t.getCategoria().getNombre(), pedido.getTienda().getDescripcion(), t.getImagen(), t.getId().intValue(), t, false));
                } else {
                    prod.add(new ProductoLista(t.getNombre(), t.getPrecio().toString(), "0", t.getCategoria().getNombre(), pedido.getTienda().getDescripcion(), t.getImagen(), t.getId().intValue(), t, true));
                }

            }

        }
        ComboPromocionHelper helper = new ComboPromocionHelper();
        ArrayList<ComboPromocion> combosDB = helper.getCombos();
        helper.close();
        if (combosDB != null) {

            combosDB.forEach((t) -> {
                boolean esta = false;
                for (PedidoLista pedido1 : pedidos) {
                    if (pedido1.combo != null) {
                        esta = (pedido1.combo.getId().equals(t.getId()));
                        if (esta) {
                            break;
                        }
                    }
                }
                if (!esta) {
                    prod.add(new ProductoLista(t, "0", false));
                } else {
                    prod.add(new ProductoLista(t, "0", true));
                }
            });

        }
        calcularTotal();
        mostrarMaximoStock();
    }

    @FXML
    public void handleAction(Event event) {

        if (event.getSource() == moreBtn) {
            int count = treeViewPedido.getSelectionModel().getSelectedCells().size();
            System.out.println("Cantidad de filas: " + count);
            if (count > 1) {
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un admiregistro de la tabla", "Ok", stackPane);
            } else if (count <= 0) {
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "Ok", stackPane);
            } else {

                int selected = treeViewPedido.getSelectionModel().getSelectedIndex();
                if (treeViewPedido.getSelectionModel().getModelItem(selected) != null) {

                    pedidoListaSeleccionado = treeViewPedido.getSelectionModel().getModelItem(selected).getValue();
                    initPopup();
                    popup.show(moreBtn, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
                } else {
                    ErrorController error = new ErrorController();
                    error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "Ok", stackPane);

                }

            }

        }
    }

    void devolverInsumos(Producto producto, Integer cantidadADevolver) {
        HashMap<Insumo, Integer> insumos = new HashMap<>();
        calcularInsumos(producto, cantidadADevolver, insumos);

    }

    void calcularInsumos(Producto p, Integer cantidad, HashMap<Insumo, Integer> insumosConsumidos) {
        ProductoHelper helper = new ProductoHelper();
        p = helper.getProducto(p.getId());
        ArrayList<ProductoInsumo> pxi = new ArrayList(p.getProductoxInsumos());
        for (ProductoInsumo productoInsumo : pxi) {
            if (insumosConsumidos.get(productoInsumo.getInsumo()) != null) {
                insumosConsumidos.put(productoInsumo.getInsumo(), productoInsumo.getCantidad().intValue() * cantidad + insumosConsumidos.get(productoInsumo.getInsumo()));
            } else {
                insumosConsumidos.put(productoInsumo.getInsumo(), productoInsumo.getCantidad().intValue() * cantidad);
            }
        }
    }

//    void mergeInsumosConsumidosDevueltos(HashMap<Insumo, Integer> insumosAConsumir, HashMap<Insumo, Integer> insumosADevolver) {
//        insumosAConsumir.forEach((t, u) -> {
//            if (insumosADevolver.get(t) != null) {
//                if (insumosAConsumir.get(t) >= insumosADevolver.get(t)) {
//                    insumosAConsumir.put(t, insumosAConsumir.get(t) - insumosADevolver.get(t));
//                    insumosADevolver.put(t, 0);
//                } else {
//                    insumosADevolver.put(t, insumosADevolver.get(t) - insumosAConsumir.get(t));
//                    insumosAConsumir.put(t, 0);
//                }
//
//            }
//        });
//
//    }
    void obtenerInsumosAConsumiryADevolver(HashMap<Insumo, Integer> insumosIniciales, HashMap<Insumo, Integer> insumosFinales,
            HashMap<Insumo, Integer> insumosAConsumir, HashMap<Insumo, Integer> insumosADevolver) {
        HashMap<Insumo, Integer> totalInsumos = new HashMap<>(insumosIniciales);
        totalInsumos.putAll(insumosFinales);
        totalInsumos.forEach((t, u) -> {
            if (insumosFinales.get(t) != null && insumosIniciales.get(t) != null) {
                if (insumosIniciales.get(t) < insumosFinales.get(t)) {
                    insumosAConsumir.put(t, insumosFinales.get(t) - insumosIniciales.get(t));
                } else {
                    insumosADevolver.put(t, insumosIniciales.get(t) - insumosFinales.get(t));
                }
            } else if (insumosFinales.get(t) != null) {
                insumosADevolver.put(t, insumosIniciales.get(t));
            } else if (insumosIniciales.get(t) != null) {
                insumosAConsumir.put(t, insumosFinales.get(t));
            }

        });
    }

    public void initPopup() {
        JFXButton eliminar = new JFXButton("Eliminar");

        eliminar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                try {

                    //remove selected item from the table list
                    ProductoLista productoLista = prod.get(prod.indexOf(new ProductoLista("", "", "0", viewPath, viewPath, viewPath, pedidoListaSeleccionado.codigo, null, false)));
                    prod.get(prod.indexOf(new ProductoLista("", "", "0", viewPath, viewPath, viewPath, pedidoListaSeleccionado.codigo, null, false))).getSeleccion().setValue(Boolean.FALSE);
//                    recalcularStockDetalle(productoLista, 0, pedidoListaSeleccionado.cantidad.getValue());
//                    mostrarMaximoStock();
//                    pedidos.remove(pedidoListaSeleccionado);

                } catch (Exception ex) {

                }
            }
        });

        eliminar.setPadding(new Insets(20));
        eliminar.setPrefSize(145, 40);

        VBox vBox = new VBox(eliminar);

        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }

    public void calcularTotal() {
        Double total = 0.0;
        for (PedidoLista ped : pedidos) {
            total = Double.valueOf(ped.subtotal.getValue()) + total;
        }
        Double igv = GeneralHelper.roundTwoDecimals(total * HomeController.IGV);
        total = GeneralHelper.roundTwoDecimals(total + igv);
        lbligv.setText(igv.toString());
        lblTotal.setText(total.toString());
    }

    public void agregarColumnasTablasPedidos() {
        final TreeItem<PedidoLista> rootPedido = new RecursiveTreeItem<>(pedidos, RecursiveTreeObject::getChildren);
        treeViewPedido.setEditable(true);
        treeViewPedido.getColumns().setAll(nombrePedido, precioPedido, cantidadPedido, descuentoPedido, subTotalPedido);
        treeViewPedido.setRoot(rootPedido);
        treeViewPedido.setShowRoot(false);
    }

    public String getPct(Double pct) {
        if (pct != null) {
            Double p = GeneralHelper.roundTwoDecimals(pct * 100);
            return p.toString();
        } else {
            return "";
        }
    }

    class ProductoLista extends RecursiveTreeObject<ProductoLista> {

        ImageView imagen;
        private Integer codigo;
        private BooleanProperty seleccion;
        private StringProperty nombre;
        private StringProperty precio;
        private StringProperty stock;
        Integer stockFijo;
        private StringProperty categoria;
        private StringProperty almacen;
        Producto producto;
        ComboPromocion combo;

        public ProductoLista(String nombre, String precio, String stock, String categoria, String almacen, String pathImagen, Integer codigo, Producto producto, Boolean estado) {
            this.nombre = new SimpleStringProperty(nombre);
            this.precio = new SimpleStringProperty(precio);
            this.stock = new SimpleStringProperty(stock);
            this.stockFijo = Integer.valueOf(stock);
            this.categoria = new SimpleStringProperty(categoria);
            this.almacen = new SimpleStringProperty(almacen);
            try {
                Image im = new Image(pathImagen);
                this.imagen = new ImageView(im);
            } catch (Exception e) {
                Image im = new Image(GeneralHelper.defaultImage);
                this.imagen = new ImageView(im);
            }
            this.seleccion = new SimpleBooleanProperty(estado);
            this.codigo = codigo;
            this.producto = producto;
            seleccion.addListener((observable, oldValue, newValue) -> {
                calcularTotal();
                if (newValue) {
                    ProductoDescuentoHelper helper = new ProductoDescuentoHelper();
                    ProductoDescuento descuento = helper.getDescuentoByProducto(codigo);
                    helper.close();
                    ProductoCategoriaDescuentoHelper hcar = new ProductoCategoriaDescuentoHelper();
                    ProductoCategoriaDescuento descCat = hcar.getDescuentoByCategoria(producto.getCategoria().getId().intValue());
                    hcar.close();
                    if (descuento != null && descCat != null) {
                        if (descuento.getValorPct() > descCat.getValue()) {
                            pedidos.add(new PedidoLista(0, "0", producto, descuento));
                        } else {
                            pedidos.add(new PedidoLista(0, "0", producto, descCat));
                        }

                    } else if (descuento != null) {
                        pedidos.add(new PedidoLista(0, "0", producto, descuento));
                    } else if (descCat != null) {
                        pedidos.add(new PedidoLista(0, "0", producto, descCat));
                    } else {
                        pedidos.add(new PedidoLista(nombre, precio, 0, "0", codigo, "0", null, producto, null));
                    }

                    //prod.remove(this);
                } else {
                    Integer ix = pedidos.indexOf(new PedidoLista(nombre, precio, 0, "0", codigo, "0", null, producto, null));
                    if (ix >= 0) {
                        recalcularStockDetalle(prod.get(prod.indexOf(new ProductoLista("", "", "0", viewPath, viewPath, viewPath, codigo, null, false))), 0, pedidos.get(ix).cantidad.getValue());
                    }
                    mostrarMaximoStock();
                    pedidos.remove(new PedidoLista(nombre, precio, 0, "0", codigo, "0", null, producto, null));
                }
                calcularTotal();
            });
        }

        public ProductoLista(ComboPromocion combo, String stock, Boolean estado) {
            this.nombre = new SimpleStringProperty(combo.getNombre());
            this.precio = new SimpleStringProperty(combo.getPreciounireal().toString());
            this.stock = new SimpleStringProperty(stock);
            this.stockFijo = Integer.valueOf(stock);
            this.categoria = new SimpleStringProperty("Combo");
            try {
                Image im = new Image(combo.getImagen());
                this.imagen = new ImageView(im);
            } catch (Exception e) {
                Image im = new Image(GeneralHelper.defaultImage);
                this.imagen = new ImageView(im);
            }
            this.seleccion = new SimpleBooleanProperty(estado);
            this.codigo = combo.getId().intValue();
            this.combo = combo;
            seleccion.addListener((observable, oldValue, newValue) -> {
                calcularTotal();
                if (newValue) {
                    pedidos.add(new PedidoLista(0, "0", combo));
                    //prod.remove(this);
                } else {
                    Integer ix = pedidos.indexOf(new PedidoLista(0, "0", combo));

                    if (ix >= 0) {
                        PedidoLista ped = pedidos.get(ix);
                        recalcularStockDetalle(prod.get(prod.indexOf(new ProductoLista("", "", "0", viewPath, viewPath, viewPath, codigo, null, false))), 0, ped.cantidad.getValue());
                    }
                    mostrarMaximoStock();
                    pedidos.remove(new PedidoLista(0, "0", combo));
                }
                calcularTotal();
            });
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ProductoLista) {
                ProductoLista pl = (ProductoLista) o;
                return pl.getCodigo().equals(getCodigo());
            } else if (o instanceof PedidoLista) {
                PedidoLista pl = (PedidoLista) o;
                return pl.codigo.equals(getCodigo());
            } else if (o instanceof Producto) {
                Producto pl = (Producto) o;
                return pl.equals(producto);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCodigo(), getCodigo().toString());
        }

        public Boolean hasInsumo(Insumo insumo) {
            return getProducto().hasInsumo(insumo);
        }

        /**
         * @return the codigo
         */
        public Integer getCodigo() {
            return codigo;
        }

        /**
         * @param codigo the codigo to set
         */
        public void setCodigo(Integer codigo) {
            this.codigo = codigo;
        }

        /**
         * @return the seleccion
         */
        public BooleanProperty getSeleccion() {
            return seleccion;
        }

        /**
         * @param seleccion the seleccion to set
         */
        public void setSeleccion(BooleanProperty seleccion) {
            this.seleccion = seleccion;
        }

        /**
         * @return the nombre
         */
        public StringProperty getNombre() {
            return nombre;
        }

        /**
         * @param nombre the nombre to set
         */
        public void setNombre(StringProperty nombre) {
            this.nombre = nombre;
        }

        /**
         * @return the precio
         */
        public StringProperty getPrecio() {
            return precio;
        }

        /**
         * @param precio the precio to set
         */
        public void setPrecio(StringProperty precio) {
            this.precio = precio;
        }

        /**
         * @return the stock
         */
        public StringProperty getStock() {
            return stock;
        }

        public Integer getStockInt() {
            return Integer.valueOf(stock.getValue());
        }

        /**
         * @param stock the stock to set
         */
        public void setStock(StringProperty stock) {
            this.stock = stock;
        }

        public void setStock(String stock) {
            this.stock = new SimpleStringProperty(stock);
        }

        /**
         * @return the categoria
         */
        public StringProperty getCategoria() {
            return categoria;
        }

        /**
         * @param categoria the categoria to set
         */
        public void setCategoria(StringProperty categoria) {
            this.categoria = categoria;
        }

        /**
         * @return the almacen
         */
        public StringProperty getAlmacen() {
            return almacen;
        }

        /**
         * @param almacen the almacen to set
         */
        public void setAlmacen(StringProperty almacen) {
            this.almacen = almacen;
        }

        /**
         * @return the producto
         */
        public Producto getProducto() {
            return producto;
        }

        /**
         * @param producto the producto to set
         */
        public void setProducto(Producto producto) {
            this.producto = producto;
        }
    }

    class PedidoLista extends RecursiveTreeObject<PedidoLista> {

        StringProperty nombre;
        StringProperty precio;
        IntegerProperty cantidad;
        StringProperty subtotal;
        StringProperty descuento;
        Integer codigoDescuento;
        Integer codigo;
        Producto producto;
        ComboPromocion combo;
        ProductoDescuento descuentoProducto;
        ProductoCategoriaDescuento descuentoCategoria;
        DetallePedido detallePedido;

        public PedidoLista(String nombre, String precio, Integer cantidad, String subtotal, Integer codigo, String descuento, Integer codigoDesc, Producto producto, ProductoDescuento descuentoProducto) {
            this.nombre = new SimpleStringProperty(nombre);
            this.precio = new SimpleStringProperty(precio);
            this.cantidad = new SimpleIntegerProperty(cantidad);

            this.subtotal = new SimpleStringProperty(subtotal);
            this.codigo = codigo;
            this.codigoDescuento = codigoDesc;
            this.descuento = new SimpleStringProperty(descuento);
            this.producto = producto;
            this.combo = null;
            this.descuentoProducto = descuentoProducto;
            this.descuentoCategoria = null;
        }

        public PedidoLista(Integer cantidad, String subtotal, Producto producto) {
            this.nombre = new SimpleStringProperty(producto.getNombre());
            this.precio = new SimpleStringProperty(producto.getPrecio().toString());
            this.cantidad = new SimpleIntegerProperty(cantidad);
            if (subtotal == null) {
                subtotal = String.valueOf(GeneralHelper.roundTwoDecimals(cantidad * producto.getPrecio()));

            }
            this.subtotal = new SimpleStringProperty(subtotal);
            this.codigo = producto.getId().intValue();
            this.producto = producto;
            this.combo = null;
            this.descuentoProducto = null;
            this.descuentoCategoria = null;
        }

        public PedidoLista(Integer cantidad, String subtotal, Producto producto, ProductoDescuento descuentoProducto) {
            this.nombre = new SimpleStringProperty(producto.getNombre());
            this.precio = new SimpleStringProperty(producto.getPrecio().toString());
            this.cantidad = new SimpleIntegerProperty(cantidad);
            if (subtotal == null) {
                subtotal = String.valueOf(GeneralHelper.roundTwoDecimals(cantidad * producto.getPrecio() * (1 - descuentoProducto.getValorPct())));

            }
            this.subtotal = new SimpleStringProperty(subtotal);
            this.codigo = producto.getId().intValue();
            this.codigoDescuento = descuentoProducto.getId().intValue();
            this.descuento = new SimpleStringProperty(getPct(descuentoProducto.getValorPct()));
            this.producto = producto;
            this.combo = null;
            this.descuentoProducto = descuentoProducto;
            this.descuentoCategoria = null;
        }

        public PedidoLista(Integer cantidad, String subtotal, Producto producto, ProductoCategoriaDescuento descuentoCategoria) {
            this.nombre = new SimpleStringProperty(producto.getNombre());
            this.precio = new SimpleStringProperty(producto.getPrecio().toString());
            this.cantidad = new SimpleIntegerProperty(cantidad);
            if (subtotal == null) {
                subtotal = String.valueOf(GeneralHelper.roundTwoDecimals(cantidad * producto.getPrecio() * (1 - descuentoCategoria.getValue())));
            }
            this.subtotal = new SimpleStringProperty(subtotal);
            this.codigo = producto.getId().intValue();
            this.codigoDescuento = descuentoCategoria.getId().intValue();
            this.descuento = new SimpleStringProperty(getPct(descuentoCategoria.getValue()));
            this.producto = producto;
            this.combo = null;
            this.descuentoProducto = null;
            this.descuentoCategoria = descuentoCategoria;
        }

        public PedidoLista(Integer cantidad, String subtotal, ComboPromocion combo) {
            this.nombre = new SimpleStringProperty(combo.getNombre());
            this.precio = new SimpleStringProperty(combo.getPreciounireal().toString());
            this.cantidad = new SimpleIntegerProperty(cantidad);
            if (subtotal == null) {
                subtotal = String.valueOf(GeneralHelper.roundTwoDecimals(cantidad * producto.getPrecio() * (1 - descuentoCategoria.getValue())));

            }
            this.subtotal = new SimpleStringProperty(subtotal);
            this.codigo = combo.getId().intValue();
            this.combo = combo;
        }

        public PedidoLista(DetallePedido detalle) {
            if (detalle.getProducto() != null) {
                Double sub;
                this.nombre = new SimpleStringProperty(detalle.getProducto().getNombre());
                this.precio = new SimpleStringProperty(detalle.getProducto().getPrecio().toString());
                this.cantidad = new SimpleIntegerProperty(detalle.getCantidad());
                this.producto = detalle.getProducto();
                this.codigo = detalle.getProducto().getId().intValue();
                if (detalle.getDescuentoCategoria() != null) {
                    sub = GeneralHelper.roundTwoDecimals(detalle.getCantidad() * detalle.getProducto().getPrecio() * (1 - detalle.getDescuentoCategoria().getValue()));
                    this.codigoDescuento = detalle.getDescuentoCategoria().getId().intValue();
                    this.descuento = new SimpleStringProperty(getPct(detalle.getDescuentoCategoria().getValue()));
                    this.combo = null;
                    this.descuentoProducto = null;
                    this.descuentoCategoria = detalle.getDescuentoCategoria();
                } else if (detalle.getDescuentoProducto() != null) {
                    sub = GeneralHelper.roundTwoDecimals(detalle.getCantidad() * detalle.getProducto().getPrecio() * (1 - detalle.getDescuentoProducto().getValorPct()));
                    this.codigoDescuento = detalle.getDescuentoProducto().getId().intValue();
                    this.descuento = new SimpleStringProperty(getPct(detalle.getDescuentoProducto().getValorPct()));
                    this.combo = null;
                    this.descuentoProducto = detalle.getDescuentoProducto();
                    this.descuentoCategoria = null;
                } else {
                    sub = GeneralHelper.roundTwoDecimals(detalle.getCantidad() * detalle.getProducto().getPrecio());
                    this.combo = null;
                    this.descuentoProducto = null;
                    this.descuentoCategoria = null;
                }
                this.subtotal = new SimpleStringProperty(sub.toString());
            } else if (detalle.getCombo() != null) {
                Double sub = GeneralHelper.roundTwoDecimals(detalle.getCantidad() * detalle.getCombo().getPreciounitario());
                this.nombre = new SimpleStringProperty(detalle.getCombo().getNombre());
                this.precio = new SimpleStringProperty(detalle.getCombo().getPreciounireal().toString());
                this.cantidad = new SimpleIntegerProperty(detalle.getCantidad());
                this.subtotal = new SimpleStringProperty(sub.toString());
                this.codigo = detalle.getCombo().getId().intValue();
                this.combo = detalle.getCombo();
            }
            this.detallePedido = detalle;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PedidoLista) {
                PedidoLista pl = (PedidoLista) o;
                return codigo.equals(pl.codigo) && nombre.getValue().equals(pl.nombre.getValue());
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + Objects.hashCode(this.codigo);

            return hash;
        }

    }

    class EditingCell extends TreeTableCell<PedidoLista, Integer> {

        private JFXTextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }

            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }

        private void createTextField() {
            textField = new JFXTextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnKeyPressed((KeyEvent t) -> {
                if (t.getCode() == KeyCode.ENTER) {
                    if (isNumeric(textField.getText())) {

                        commitEdit(Integer.parseInt(textField.getText()));
                    }

                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }

    }

    private class ButtonCell extends TreeTableCell<PedidoLista, Boolean> {

        final JFXButton cellButton = new JFXButton("X");

        ButtonCell() {

            //Action when the button is pressed
            cellButton.setButtonType(JFXButton.ButtonType.RAISED);
            cellButton.setBorder(Border.EMPTY);
            cellButton.setStyle("-fx-background-color: white;");
            this.setAlignment(Pos.CENTER);
            cellButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    // get Selected Item
                    PedidoLista current = (PedidoLista) ButtonCell.this.getTreeTableRow().getItem();
                    //remove selected item from the table list
                    prod.get(prod.indexOf(new ProductoLista("", "", "0", viewPath, viewPath, viewPath, current.codigo, null, false))).getSeleccion().setValue(Boolean.FALSE);
//                    recalcularStock(prod.get(prod.indexOf(new ProductoLista("", "", "0", viewPath, viewPath, viewPath, current.codigo, null))), 0, Integer.valueOf(current.cantidad.getValue()));
                    mostrarMaximoStock();
                    pedidos.remove(current);
                }
            });
        }
        //Display button if the row is not empty

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }
    }

}
