/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app.connexion;

import chat.app.login.FXMLLoginController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author abdel
 */
public class FXMLConnexionController implements Initializable {

    @FXML
    public JFXTextField adresseDuServeurTextField;
    @FXML
    private JFXTextField portTextField;
    @FXML
    private JFXButton connexionButton;
    @FXML
    private JFXButton AnnulerButton;
    
    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void connexionButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat/app/login/FXMLLogin.fxml"));
        Parent root = (Parent) loader.load();
        FXMLLoginController loginController = loader.getController();
        Stage stageLogin = new Stage();
        stageLogin.initStyle(StageStyle.TRANSPARENT);
        
        root.setOnMousePressed((MouseEvent event1) -> {
            xOffset = event1.getSceneX();
            yOffset = event1.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event1) -> {
            stageLogin.setX(event1.getScreenX() - xOffset);
            stageLogin.setY(event1.getScreenY() - yOffset);
        });
        
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stageLogin.setScene(scene);
        ((Stage) connexionButton.getScene().getWindow()).close();
        loginController.seConnecterAnchorPane.toFront();
        loginController.utilisateurSeConnecterTextField.requestFocus();
        stageLogin.show();
    }

    @FXML
    private void annulerButtonOnAction(ActionEvent event) {
        Platform.exit();
    }
    
}
