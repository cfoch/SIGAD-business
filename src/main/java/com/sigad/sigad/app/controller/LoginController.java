/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigad.sigad.app.controller;

import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.sigad.sigad.app.controller.HomeController;
import com.sigad.sigad.app.controller.ErrorController;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
/**
 * FXML Controller class
 *
 * @author jorgeespinoza
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static String viewPath = "/com/sigad/sigad/app/view/login.fxml";
    public static String windowName = "Login";
    
    @FXML
    private JFXTextField userTxt;
    @FXML
    private JFXPasswordField passwordTxt;
    @FXML
    private StackPane hiddenSp;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void loginClicked(MouseEvent event) throws IOException {
        System.out.println(userTxt.getText());
        System.out.println(passwordTxt.getText());
        
        if(validate()){
            this.loadWindow(HomeController.viewPath, HomeController.windowName);
        }else{
            JFXDialogLayout content =  new JFXDialogLayout();
            content.setHeading(new Text("Error"));
            content.setBody(new Text("Cuenta o contraseña incorrectas"));

            ErrorController error = new ErrorController();
            error.loadDialog(content, hiddenSp);
            
        }
    }
    
    private boolean validate() {
       return userTxt.getText().equals("admin") && passwordTxt.getText().equals("admin");
    }
    
    private void loadWindow(String viewPath, String windowTitle) throws IOException {
       userTxt.getScene().getWindow().hide();
       Parent newRoot = FXMLLoader.load(getClass().getResource(HomeController.viewPath));
       Stage stage = new Stage();
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
}
