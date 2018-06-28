/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sigad.sigad.business.Perfil;
import com.sigad.sigad.business.Permiso;
import com.sigad.sigad.business.Usuario;
import com.sigad.sigad.business.helpers.PerfilHelper;
import com.sigad.sigad.business.helpers.PermisoHelper;
import com.sigad.sigad.business.helpers.UsuarioHelper;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static final String viewPath = "/com/sigad/sigad/app/view/login.fxml";
    public static String windowName = "Login";
    private static Configuration config = null;
    private static SessionFactory sessionFactory = null;
    @FXML
    private JFXTextField userTxt;
    @FXML
    private JFXPasswordField passwordTxt;
    @FXML
    private StackPane hiddenSp;
    
    public static Stage stage;
    public static Usuario user = null;
    public static int MAX_PERMISOS= 13;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Create admin if not exist
        Session session = serviceInit();
        
        Query query  = session.createQuery("from Perfil p where p.nombre='SuperAdmin'");
        int count = query.list().size();
        if(count == 0){
            System.out.println("Primer inicio de sesión");
            
            Transaction tx = null;
            
            try {
                if(session.getTransaction().isActive()){
                    tx = session.getTransaction();
                }else{
                    tx = session.beginTransaction();
                }
                //Crear un perfil de cliente
//                PerfilHelper helper = new PerfilHelper();
//                Perfil cliente = new Perfil("Cliente", "Cliente de aplicación móvil", true, null);
//                helper.saveProfile(cliente);
//                helper.close();
                
                Set<Permiso> list = new HashSet<>();
                list.add(new Permiso(8, "Ventas", "BASKET"));
                list.add(new Permiso(7, "Descuentos", "SALE"));
                list.add(new Permiso(4, "Insumos", "ARRANGE_SEND_BACKWARD"));
                list.add(new Permiso(2, "Usuarios", "ACCOUNT_MULTIPLE"));
                list.add(new Permiso(9, "Repartos", "CAR"));
                list.add(new Permiso(5, "Pedidos", "CART_PLUS"));
                list.add(new Permiso(3, "Tiendas", "STORE"));
                list.add(new Permiso(1, "Perfiles", "FINGERPRINT"));
                list.add(new Permiso(11, "Estadísticas", "ELEVATION_RISE"));
                list.add(new Permiso(0, "Carga Masiva", "ARROW_UP_BOLD_CIRCLE"));
                list.add(new Permiso(6, "Productos", "ART_TRACK"));
                list.add(new Permiso(10, "Movimientos", "CLOCK_FAST"));
                
                MAX_PERMISOS = list.size();
                
                for (Permiso p : list) {
                    PermisoHelper helper1 = new PermisoHelper();
                    helper1.savePermission(p);
                    helper1.close();
                }
                
                //Crear un perfil de superadmin
                Perfil adminProfile = new Perfil("SuperAdmin", "Super admini can create stores", true, list, false);

                String hash = encrypt("admin");

                Usuario user = new Usuario("Juan", "Tonos", "Tonos", adminProfile,
                        "123456789", "71067346", "943821232", true,
                        "admin@sigad.net", hash , "");
                session.save(adminProfile);
                session.save(user);

                tx.commit();
                session.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                tx.rollback();
            }
            
        }
        
        session.close();
    }    

    @FXML
    private void loginClicked(MouseEvent event) throws IOException {
        
        if(validate()){
            this.loadWindow(HomeController.viewPath, HomeController.windowName);
        }
    }
    
    public static Session serviceInit(){
        Session session = null;
        
        if(config==null || sessionFactory==null) {
            try {
                config = new Configuration();
                config.configure("hibernate.cfg.xml");
                sessionFactory = config.buildSessionFactory();
                session = sessionFactory.openSession();
            } catch (HibernateException e) {
                System.out.println(e.getMessage());
            }
        } else {
            session = sessionFactory.openSession();
        }
        
        return session;
    }
    
    public static boolean serviceEnd(){
        if(sessionFactory!=null){
            try {
                sessionFactory.close();
            } catch (HibernateException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        
        return true;
    } 
    
    private boolean validate(){
        UsuarioHelper helper = new UsuarioHelper();
        user = helper.getUser(userTxt.getText());
        
        if(user==null){
            ErrorController error = new ErrorController();
            error.loadDialog("Error", "El correo indicado no existe","Ok", hiddenSp);
            return false;
        }else{
            if(!user.isActivo()){
                ErrorController error = new ErrorController();
                error.loadDialog("Error", "Cuenta no activa, por favor contacte con un administrador para activar su cuenta","Ok", hiddenSp);
                return false;
            }
            String text = decrypt(user.getPassword());
            if(!passwordTxt.getText().equals(text)){
                ErrorController error = new ErrorController();
                error.loadDialog("Error", "Contrase incorrecta","Ok", hiddenSp);
                return false;
            }
        }
        
        return true;
    }
    
    private void loadWindow(String viewPath, String windowTitle) throws IOException {
       userTxt.getScene().getWindow().hide();
       Parent newRoot = FXMLLoader.load(getClass().getResource(viewPath));
       stage = new Stage();
       stage.setTitle(windowTitle);
       stage.setScene(new Scene(newRoot));
       stage.show();
    }

    @FXML
    private void signupClicked(MouseEvent event) {
    }

    @FXML
    private void recoverPasswordClicked(MouseEvent event) {
    }

    @FXML
    private void passwordKeyPressed(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.ENTER){
            loginClicked(null);
        }
    }

    public static String encrypt(String texto) {
        String hash = "";
        String secretKey = "sigadTestKey"; //llave para encriptar datos
        String base64EncryptedString = "";
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes;
            base64Bytes = Base64.getEncoder().encode(buf);
            base64EncryptedString = new String(base64Bytes);
            
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "encrypt()", ex);
        } 
        return base64EncryptedString;
    }
    
    public static String decrypt(String textoEncriptado){

        String secretKey = "sigadTestKey"; //llave para desenciptar datos
        String base64EncryptedString = "";

        try {
            byte[] message = Base64.getDecoder().decode(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            base64EncryptedString = new String(plainText, "UTF-8");

        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "decrypt()", ex);
        }
        return base64EncryptedString;
    }
}
