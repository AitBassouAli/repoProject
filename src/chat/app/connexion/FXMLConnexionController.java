/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app.connexion;

import chat.app.alerte.FXMLAlerteController;
import chat.app.login.FXMLLoginController;
import clientServices.ClientMT;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.Session;

/**
 * FXML Controller class
 *
 * @author abdel
 */
public class FXMLConnexionController implements Initializable {

    @FXML
    public JFXTextField adresseDuServeurTextField;
    @FXML
    private JFXButton connexionButton;
    @FXML
    private JFXButton AnnulerButton;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void connexionButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loaderAlerte = new FXMLLoader(getClass().getResource("/chat/app/alerte/FXMLAlerte.fxml"));
        Parent rootAlerte = (Parent) loaderAlerte.load();
        FXMLAlerteController alerteController = loaderAlerte.getController();
        Stage stageAlerte = new Stage();
        stageAlerte.initStyle(StageStyle.TRANSPARENT);

        rootAlerte.setOnMousePressed((MouseEvent event1) -> {
            xOffset = event1.getSceneX();
            yOffset = event1.getSceneY();
        });
        rootAlerte.setOnMouseDragged((MouseEvent event1) -> {
            stageAlerte.setX(event1.getScreenX() - xOffset);
            stageAlerte.setY(event1.getScreenY() - yOffset);
        });

        try {
            if (!(adresseDuServeurTextField.getText().equals(""))) {
                if (((String) Session.getAttribut("adresseIP")) != null) {
                    Session.delete("adresseIP");
                }
                Session.setAttribut(adresseDuServeurTextField.getText(), "adresseIP");
                ClientMT clientMT = new ClientMT();
                clientMT.beforConnection();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat/app/login/FXMLLogin.fxml"));
                Parent root = (Parent) loader.load();
                FXMLLoginController loginController = loader.getController();
                Stage stageLogin = Connexion.getIconToStage(new Stage());
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
            } else {
                Scene scene = new Scene(rootAlerte);
                scene.setFill(Color.TRANSPARENT);
                stageAlerte.initModality(Modality.APPLICATION_MODAL);
                stageAlerte.setScene(scene);

                alerteController.attentionAnchorPane.toFront();
                alerteController.attentionLabel.setText("Veuillez saisir l'adresse du serveur pour se connecter !");
                stageAlerte.show();
            }
        } catch (Exception e) {
            Scene scene = new Scene(rootAlerte);
            scene.setFill(Color.TRANSPARENT);
            stageAlerte.initModality(Modality.APPLICATION_MODAL);
            stageAlerte.setScene(scene);

            alerteController.erreurAnchorPane.toFront();
            alerteController.erreurLabel.setText("Impossible de se connecter au serveur !");
            stageAlerte.show();
        }
    }

    @FXML
    private void annulerButtonOnAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void adresseDuServeurTextFieldOnKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            connexionButtonOnAction(null);
        }
    }

}
