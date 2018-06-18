/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.personal.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sigad.sigad.app.controller.ErrorController;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.UsuarioHelper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class PersonalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/personal/view/personal.fxml";
    public static String windowName = "Cuentas";
    @FXML
    private JFXTreeTableView userTbl;
    static ObservableList<User> data;
    @FXML
    private JFXButton addBtn;
    @FXML
    private JFXButton moreBtn;
    @FXML
    private StackPane hiddenSp;
    /*Extras*/
    @FXML
    public static JFXDialog userDialog;
    @FXML
    private JFXPopup popup;
    
    public static boolean isUserCreate;
    public static User selectedUser = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        initUserTbl();
        
    }    
    
    public static void updateTable(Usuario u) {
        data.add(
                new User(
                        new SimpleStringProperty(u.getNombres()),
                        new SimpleStringProperty(u.getApellidoMaterno()),
                        new SimpleStringProperty(u.getApellidoPaterno()),
                        new SimpleStringProperty(u.getCorreo()),
                        new SimpleStringProperty(u.getDni()),
                        new SimpleStringProperty(u.getTelefono()),
                        new SimpleStringProperty(u.getCelular()),
                        new SimpleStringProperty(u.getPerfil().getNombre()),
                        new SimpleStringProperty(u.getPerfil().getDescripcion()),
                        new SimpleStringProperty(u.getTienda()!=null ? u.getTienda().getDireccion(): ""),
                        new SimpleStringProperty(u.isActivo()? "Activo": "Inactivo")
                ));
    }

    //usar para llenar LISTA
    private void getDataFromDB() {
        UsuarioHelper userHelper = new UsuarioHelper();
        
        ArrayList<Usuario> lista = userHelper.getUsers();
        if(lista != null){
            lista.forEach((p) -> {
                updateTable(p);
            });
        }
        userHelper.close();
    }

    //usar para botones
    @FXML
    private void handleAction(ActionEvent event) {
        if(event.getSource() == addBtn){
            try {
                CreateEdditUserDialog(true);
            } catch (IOException ex) {
                Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "handleAction()", ex);
            }
        }else if(event.getSource() == moreBtn){
            int count = userTbl.getSelectionModel().getSelectedCells().size();
            if( count > 1){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar solo un registro de la tabla", "Ok", hiddenSp);
            }else if(count<=0){
                ErrorController error = new ErrorController();
                error.loadDialog("Atención", "Debe seleccionar al menos un registro de la tabla", "Ok", hiddenSp);
            }else{
                int selected  = userTbl.getSelectionModel().getSelectedIndex();
                selectedUser = (User) userTbl.getSelectionModel().getModelItem(selected).getValue();
                initPopup();
                popup.show(moreBtn, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
            }
            
        }
    }
    
    private void initPopup(){
        JFXButton edit = new JFXButton("Editar");
        JFXButton delete = new JFXButton("Activar/Desactivar");
        
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
                try {
                    CreateEdditUserDialog(false);
                } catch (IOException ex) {
                    Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "initPopup(): CreateEdditUserDialog()", ex);
                }
            }
        });
        
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                data.remove(PersonalController.selectedUser);
                
                UsuarioHelper helper = new UsuarioHelper();
                Usuario temp = helper.getUser(selectedUser.correo.getValue());
                temp.setActivo(!temp.isActivo());
                helper.close();
                
                UsuarioHelper helper1 = new UsuarioHelper();
                boolean ok = helper1.updateUser(temp);
                if(!ok){
                    ErrorController error = new ErrorController();
                    error.loadDialog("Eror", "No se pudo actualizar el estado", "Ok", hiddenSp);
                    return;
                }
                updateTable(temp);
                
                helper1.close();
                popup.hide();
                
            }
        });
        
        edit.setPadding(new Insets(20));
        edit.setPrefSize(145, 40);
        delete.setPadding(new Insets(20));
        delete.setPrefSize(145, 40);
        
        VBox vBox = new VBox(edit, delete);
        
        popup = new JFXPopup();
        popup.setPopupContent(vBox);
    }
    
    public void CreateEdditUserDialog(boolean iscreate) throws IOException {
        isUserCreate = iscreate;
        
        JFXDialogLayout content =  new JFXDialogLayout();
        
        if(isUserCreate){
            content.setHeading(new Text("Crear Usuario"));
        }else{
            content.setHeading(new Text("Editar Usuario"));
            
            UsuarioHelper helper = new UsuarioHelper();
            Usuario u = helper.getUser(PersonalController.selectedUser.correo.getValue());
            if(u == null){
                ErrorController error = new ErrorController();
                error.loadDialog("Error", "No se pudo obtener el usuario", "Ok", hiddenSp);
                System.out.println("Error al obtener el usuario");
                return;
            }
        }
        
        Node node;
        node = (Node) FXMLLoader.load(PersonalController.this.getClass().getResource(CrearEditarUsuarioController.viewPath));
        content.setBody(node);
        //content.setPrefSize(400,400);
                
        userDialog = new JFXDialog(hiddenSp, content, JFXDialog.DialogTransition.CENTER);
        userDialog.show();
    }

    private void initUserTbl() {
        JFXTreeTableColumn<PersonalController.User, String> name = new JFXTreeTableColumn<>("Nombre");
        name.setPrefWidth(120);
        name.setCellValueFactory((TreeTableColumn.CellDataFeatures<PersonalController.User, String> param) -> param.getValue().getValue().nombres);
        
        JFXTreeTableColumn<PersonalController.User, String> apellidoPaterno = new JFXTreeTableColumn<>("Appelido Paterno");
        apellidoPaterno.setPrefWidth(120);
        apellidoPaterno.setCellValueFactory((TreeTableColumn.CellDataFeatures<PersonalController.User, String> param) -> param.getValue().getValue().apellidoPaterno);
        
        JFXTreeTableColumn<PersonalController.User, String> apellidoMaterno = new JFXTreeTableColumn<>("Appelido Materno");
        apellidoMaterno.setPrefWidth(70);
        apellidoMaterno.setCellValueFactory((TreeTableColumn.CellDataFeatures<PersonalController.User, String> param) -> param.getValue().getValue().apellidoMaterno);
        
        JFXTreeTableColumn<PersonalController.User, String> dni = new JFXTreeTableColumn<>("DNI");
        dni.setPrefWidth(70);
        dni.setCellValueFactory((TreeTableColumn.CellDataFeatures<PersonalController.User, String> param) -> param.getValue().getValue().dni);
        
        JFXTreeTableColumn<PersonalController.User, String> telephone = new JFXTreeTableColumn<>("Teléfono");
        telephone.setPrefWidth(70);
        telephone.setCellValueFactory((TreeTableColumn.CellDataFeatures<PersonalController.User, String> param) -> param.getValue().getValue().telefono);
        
        JFXTreeTableColumn<PersonalController.User, String> cellphone = new JFXTreeTableColumn<>("Celular");
        cellphone.setPrefWidth(70);
        cellphone.setCellValueFactory((TreeTableColumn.CellDataFeatures<PersonalController.User, String> param) -> param.getValue().getValue().celular);
        
        JFXTreeTableColumn<PersonalController.User, String> profile = new JFXTreeTableColumn<>("Perfil");
        profile.setPrefWidth(70);
        profile.setCellValueFactory((TreeTableColumn.CellDataFeatures<PersonalController.User, String> param) -> param.getValue().getValue().profileName);
        
        JFXTreeTableColumn<PersonalController.User, String> profileDesc = new JFXTreeTableColumn<>("Descripción");
        profileDesc.setPrefWidth(70);
        profileDesc.setCellValueFactory((TreeTableColumn.CellDataFeatures<PersonalController.User, String> param) -> param.getValue().getValue().profileDesc);
        
        JFXTreeTableColumn<PersonalController.User, String> tienda = new JFXTreeTableColumn<>("Tienda");
        tienda.setPrefWidth(70);
        tienda.setCellValueFactory((TreeTableColumn.CellDataFeatures<PersonalController.User, String> param) -> param.getValue().getValue().tienda);
        
        JFXTreeTableColumn<PersonalController.User, String> active = new JFXTreeTableColumn<>("Activo");
        active.setPrefWidth(70);
        active.setCellValueFactory((TreeTableColumn.CellDataFeatures<PersonalController.User, String> param) -> param.getValue().getValue().activo);
        
        userTbl.getColumns().add(name);
        userTbl.getColumns().add(apellidoPaterno);
        userTbl.getColumns().add(apellidoMaterno);
        userTbl.getColumns().add(dni);
        userTbl.getColumns().add(telephone);
        userTbl.getColumns().add(cellphone);
        userTbl.getColumns().add(profile);
        userTbl.getColumns().add(profileDesc);
        userTbl.getColumns().add(tienda);
        userTbl.getColumns().add(active);
        
        //DB
        getDataFromDB();
        
        //Double click on row
        userTbl.setRowFactory(ord -> {
            JFXTreeTableRow<PersonalController.User> row = new JFXTreeTableRow<>();
            row.setOnMouseClicked((event) -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                     && event.getClickCount() == 2) {
                    PersonalController.User clickedRow = row.getItem();
                    System.out.println(clickedRow.nombres);
                    
                    selectedUser = clickedRow;
                    //data.remove(selectedUser);
                    try {
                        CreateEdditUserDialog(false);
                    } catch (IOException ex) {
                        Logger.getLogger(PersonalController.class.getName()).log(Level.SEVERE, "initUserTbl(): CreateEdditUserDialog()", ex);
                    }
                }
            });
            return row;
        });
        
        final TreeItem<PersonalController.User> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        
        userTbl.setEditable(true);
        userTbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);        
        userTbl.getColumns().setAll(name,apellidoPaterno,apellidoMaterno, dni, telephone, cellphone, profile, profileDesc, tienda, active);
        userTbl.setRoot(root);
        userTbl.setShowRoot(false);
    }
    
    /*Class for table*/
    public static class User extends RecursiveTreeObject<User> {

        StringProperty nombres;
        StringProperty apellidoMaterno;
        StringProperty apellidoPaterno;
        StringProperty correo;
        StringProperty dni;
        StringProperty telefono;
        StringProperty celular;
        StringProperty profileName;
        StringProperty profileDesc;
        StringProperty tienda;
        StringProperty activo;

        public User(StringProperty nombres, StringProperty apellidoMaterno, StringProperty apellidoPaterno, StringProperty correo, StringProperty dni, StringProperty telefono, StringProperty celular, StringProperty profileName, StringProperty profileDesc, StringProperty tienda, StringProperty activo) {
            this.nombres = nombres;
            this.apellidoMaterno = apellidoMaterno;
            this.apellidoPaterno = apellidoPaterno;
            this.correo = correo;
            this.dni = dni;
            this.telefono = telefono;
            this.celular = celular;
            this.profileName = profileName;
            this.profileDesc = profileDesc;
            this.tienda = tienda;
            this.activo = activo;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof User) {
                User u = (User) o;
                return u.dni.equals(dni);
            }
            return super.equals(o); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.dni);
            return hash;
        }

    }
}
