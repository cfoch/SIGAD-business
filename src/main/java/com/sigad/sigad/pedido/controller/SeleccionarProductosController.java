/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.pedido.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.business.Producto;
import com.sigad.sigad.business.ProductoDescuento;
import com.sigad.sigad.business.helpers.ProductoDescuentoHelper;
import com.sigad.sigad.business.helpers.ProductoHelper;
import com.sigad.sigad.personal.controller.CrearEditarUsuarioController;
import com.sigad.sigad.personal.controller.PersonalController;
import static com.sigad.sigad.personal.controller.PersonalController.userDialog;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
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
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
    JFXTreeTableColumn<ProductoLista, String> stock = new JFXTreeTableColumn<>("Stock");
    @FXML
    JFXTreeTableColumn<ProductoLista, String> categoria = new JFXTreeTableColumn<>("Categoria");
    @FXML
    JFXTreeTableColumn<ProductoLista, String> almacen = new JFXTreeTableColumn<>("Almacen");
    @FXML
    JFXTreeTableColumn<PedidoLista, Boolean> eliminar = new JFXTreeTableColumn<>("Eliminar");
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

    private final ObservableList<PedidoLista> pedidos = FXCollections.observableArrayList();
    private final ObservableList<ProductoLista> prod = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        columnasPedidos();
        columnasProductos();
        agregarColumnasTablasPedidos();
        agregarColumnasTablasProductos();
        clickBotonContinuar();
        agregarFiltro();

        //Basede datos
        ProductoHelper gest = new ProductoHelper();
        ArrayList<Producto> productosDB = gest.getProducts();
        if (productosDB != null) {
            productosDB.forEach((p) -> {
                Producto t = p;
                System.out.println(t.getPrecio());
                prod.add(new ProductoLista(t.getNombre(), t.getPrecio().toString(), Integer.toString(t.getStockLogico()), /*t.getCategoria().getNombre()*/ "Hola", "", t.getImagen(), t.getId().intValue()));
            });
        }
        gest.close();

    }

    public void agregarFiltro() {
        filtro.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            treeView.setPredicate((TreeItem<ProductoLista> t) -> {
                Boolean flag = t.getValue().nombre.getValue().contains(newValue) || t.getValue().categoria.getValue().contains(newValue);
                return flag;
            });
        });
    }

    public void clickBotonContinuar() {
        btnContinuar.addEventHandler(EventType.ROOT, (event) -> {

            pedidos.forEach((next) -> {
                System.out.println(next.nombre + next.cantidad.toString() + next.subtotal.toString());
            });
        });
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
        select.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, Boolean> param) -> param.getValue().getValue().seleccion);
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
        nombre.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().nombre);

        precio.setPrefWidth(120);
        precio.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().precio);

        stock.setPrefWidth(120);
        stock.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().stock);

        categoria.setPrefWidth(120);
        categoria.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().categoria);

        almacen.setPrefWidth(120);
        almacen.setCellValueFactory((TreeTableColumn.CellDataFeatures<ProductoLista, String> param) -> param.getValue().getValue().almacen);
    }

    public void columnasPedidos() {
        eliminar.setPrefWidth(80);
        eliminar.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PedidoLista, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TreeTableColumn.CellDataFeatures<PedidoLista, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });
        //Adding the Button to the cell
        eliminar.setCellFactory(new Callback<TreeTableColumn<PedidoLista, Boolean>, TreeTableCell<PedidoLista, Boolean>>() {
            @Override
            public TreeTableCell<PedidoLista, Boolean> call(TreeTableColumn<PedidoLista, Boolean> p) {
                return new ButtonCell();
            }

        });

        nombrePedido.setPrefWidth(80);
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

                Integer stock = event.getRowValue().getValue().stock.getValue();
                PedidoLista nuevo = new PedidoLista(event.getRowValue().getValue().nombre.getValue(), event.getRowValue().getValue().precio.getValue(),
                        (event.getNewValue() <= stock) ? event.getNewValue() : event.getOldValue(),
                        String.valueOf(Float.valueOf(event.getRowValue().getValue().precio.get()) * Float.valueOf(event.getNewValue()) - Float.valueOf(event.getRowValue().getValue().precio.get()) * ( - Float.valueOf(event.getRowValue().getValue().descuento.get())) / 100.0),
                        event.getRowValue().getValue().codigo, event.getRowValue().getValue().descuento.get(), event.getRowValue().getValue().stock.getValue());
                Integer i = pedidos.indexOf(nuevo);
                pedidos.remove(nuevo);
                pedidos.add(i, nuevo);
                
                if (event.getNewValue() <= stock) {
                    calcularTotal();
                    
                }

            }
        });
//        cantidadPedido.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
//        cantidadPedido.setOnEditCommit((TreeTableColumn.CellEditEvent<PedidoLista, String> event) -> {
//            TreeItem<PedidoLista> productoEditado = treeViewPedido.getTreeItem(event.getTreeTablePosition().getRow());
//            if (isNumeric(event.getNewValue())) {
//                Double subtotal = Double.valueOf(event.getNewValue()) * Double.valueOf(productoEditado.getValue().precio.getValue());
//                productoEditado.getValue().subtotal = new SimpleStringProperty(subtotal.toString());
//                productoEditado.getValue().cantidad = new SimpleStringProperty(event.getNewValue());
//            } else {
//
//            }
//
//        });

        precioPedido.setPrefWidth(80);
        precioPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, String> param) -> param.getValue().getValue().precio);

        subTotalPedido.setPrefWidth(80);
        subTotalPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, String> param) -> param.getValue().getValue().subtotal);

        descuentoPedido.setPrefWidth(80);
        descuentoPedido.setCellValueFactory((TreeTableColumn.CellDataFeatures<PedidoLista, String> param) -> param.getValue().getValue().descuento);

    }

    public void agregarColumnasTablasProductos() {
        final TreeItem<ProductoLista> root = new RecursiveTreeItem<>(prod, RecursiveTreeObject::getChildren);

        treeView.setEditable(true);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView.getColumns().setAll(select, imagen, nombre, precio, stock, categoria, almacen);
        treeView.setRoot(root);
        treeView.setShowRoot(false);

        treeView.setRowFactory(new Callback<TreeTableView<ProductoLista>, TreeTableRow<ProductoLista>>() {
            @Override
            public TreeTableRow<ProductoLista> call(TreeTableView<ProductoLista> param) {
                TreeTableRow<ProductoLista> row = new TreeTableRow<>();
                row.setOnMouseClicked((event) -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        ProductoLista rowData = row.getItem();
                        mostrarInfoProducto(rowData.codigo);

//                            ProductoLista rowData = row.getItem();
//                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(DescripcionProductosController.viewPath));
//                            Parent root1 = fxmlLoader.load();
//                            fxmlLoader.<DescripcionProductosController>getController().setIdProducto(rowData.codigo);
//                            Stage stage = new Stage();
//                            stage.setTitle("Seleccionar producto");
//                            stage.setScene(new Scene(root1));
//                            stage.show();
                        //FXMLLoader loader = new FXMLLoader(SeleccionarProductosController.this.getClass().getResource(DescripcionProductosController.viewPath));
                        //DescripcionProductosController desc = loader.getController();
//                        desc.setIdProducto(rowData.codigo);
//                        System.out.println(rowData.codigo);
//                        Stage stage = new Stage();
//                        stage.initOwner(treeView.getScene().getWindow());
//                        try {
//                            stage.setScene(new Scene((Parent) loader.load()));
//                        } catch (IOException ex) {
//                            Logger.getLogger(SeleccionarProductosController.class.getName()).log(Level.SEVERE, null, ex);
//                        }
// showAndWait will block execution until the window closes...
//stage.showAndWait();
//                        DescripcionProductosController controller = loader.getController();
//                        text1.setText(controller.getText());
//                        try {
//                            Node node;
//                            node = (Node) FXMLLoader.load(SeleccionarProductosController.this.getClass().getResource(DescripcionProductosController.viewPath));
//                            anchorPane.getChildren().setAll(node);
//                        } catch (IOException ex) {
//                            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, "", ex);
//                        }
                    }
                });
                return row; //To change body of generated lambdas, choose Tools | Templates.
            }
        });
    }

    public void mostrarInfoProducto(Integer codigo) {
        try {
            popup = new JFXPopup();
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Crear Usuario"));
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

    public void calcularTotal() {
        Double total = 0.0;
        for (PedidoLista pedido : pedidos) {
            total = Double.valueOf(pedido.subtotal.getValue()) + total;
        }
        lblTotal.setText(total.toString());
    }

    public void agregarColumnasTablasPedidos() {
        final TreeItem<PedidoLista> rootPedido = new RecursiveTreeItem<>(pedidos, RecursiveTreeObject::getChildren);
        treeViewPedido.setEditable(true);
        treeViewPedido.getColumns().setAll(nombrePedido, precioPedido, cantidadPedido, descuentoPedido, subTotalPedido, eliminar);
        treeViewPedido.setRoot(rootPedido);
        treeViewPedido.setShowRoot(false);
    }
    class ProductoLista extends RecursiveTreeObject<ProductoLista> {

        ImageView imagen;
        Integer codigo;
        BooleanProperty seleccion;
        StringProperty nombre;
        StringProperty precio;
        StringProperty stock;
        StringProperty categoria;
        StringProperty almacen;
        StringProperty cantidad;

        public ProductoLista(String nombre, String precio, String stock, String categoria, String almacen, String pathImagen, Integer codigo) {
            this.nombre = new SimpleStringProperty(nombre);
            this.precio = new SimpleStringProperty(precio);
            this.stock = new SimpleStringProperty(stock);
            this.categoria = new SimpleStringProperty(categoria);
            this.almacen = new SimpleStringProperty(almacen);
            this.imagen = new ImageView(new Image(pathImagen));
            this.seleccion = new SimpleBooleanProperty(false);
            this.codigo = codigo;
            seleccion.addListener((observable, oldValue, newValue) -> {
                calcularTotal();
                if (newValue) {
                    ProductoDescuentoHelper helper = new ProductoDescuentoHelper();
                    ProductoDescuento descuento = helper.getDescuentoByProducto(codigo);
                    if (descuento != null) {
                        pedidos.add(new PedidoLista(nombre, precio, 0, "0", codigo, String.valueOf(descuento.getValorPct() * 100.0), Integer.valueOf(stock)));
                    } else {
                        pedidos.add(new PedidoLista(nombre, precio, 0, "0", codigo, "0", Integer.valueOf(stock)));
                    }
                    helper.close();

                    //prod.remove(this);
                } else {
                    pedidos.remove(new PedidoLista(nombre, precio, 0, "0", codigo, "0", 0));
                }
                calcularTotal();
            });
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ProductoLista) {
                ProductoLista pl = (ProductoLista) o;
                return pl.codigo.equals(codigo);
            } else if (o instanceof PedidoLista) {
                PedidoLista pl = (PedidoLista) o;
                return pl.codigo.equals(codigo);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            return Objects.hash(codigo, codigo.toString());
        }
    }

    class PedidoLista extends RecursiveTreeObject<PedidoLista> {

        StringProperty nombre;
        StringProperty precio;
        IntegerProperty cantidad;
        StringProperty subtotal;
        StringProperty descuento;
        IntegerProperty stock;
        Integer codigo;

        public PedidoLista(String nombre, String precio, Integer cantidad, String subtotal, Integer codigo, String descuento, Integer stock) {
            this.nombre = new SimpleStringProperty(nombre);
            this.precio = new SimpleStringProperty(precio);
            this.cantidad = new SimpleIntegerProperty(cantidad);
            this.subtotal = new SimpleStringProperty(subtotal);
            this.codigo = codigo;
            this.descuento = new SimpleStringProperty(descuento);
            this.stock = new SimpleIntegerProperty(stock);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PedidoLista) {
                PedidoLista pl = (PedidoLista) o;
                return pl.codigo.equals(codigo);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.codigo);
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
            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        if (isNumeric(textField.getText())) {

                            commitEdit(Integer.parseInt(textField.getText()));
                        }

                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

    private class ButtonCell extends TreeTableCell<PedidoLista, Boolean> {

        final JFXButton cellButton = new JFXButton("Delete");

        ButtonCell() {

            //Action when the button is pressed
            cellButton.setButtonType(JFXButton.ButtonType.RAISED);
            cellButton.setBorder(Border.EMPTY);
            cellButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    // get Selected Item
                    PedidoLista current = (PedidoLista) ButtonCell.this.getTreeTableRow().getItem();
                    //remove selected item from the table list
                    prod.get(prod.indexOf(new ProductoLista("", "", viewPath, viewPath, viewPath, viewPath, current.codigo))).seleccion.setValue(Boolean.FALSE);
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
