/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app.login;

import bean.User;
import chat.app.alerte.FXMLAlerteController;
import chat.app.main.FXMLMainController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.UserFacade;
import util.DateUtil;
import util.Session;

/**
 * FXML Controller class
 *
 * @author abdel
 */
public class FXMLLoginController implements Initializable {

    @FXML
    private JFXButton seConnecterButton;
    @FXML
    private JFXButton inscrireButton;
    @FXML
    private AnchorPane motDePasseOublieAnchorPane;
    @FXML
    private JFXTextField utilisateurMotDePasseOublieTextField;
    @FXML
    private JFXButton motDePasseOublieLoginButton;
    @FXML
    private AnchorPane inscrireAnchorPane;
    @FXML
    private JFXTextField utilisateurInscrireTextField;
    @FXML
    private JFXTextField emailInscrireTextField;
    @FXML
    private JFXButton inscrireLoginButton;
    @FXML
    public AnchorPane seConnecterAnchorPane;
    @FXML
    public JFXTextField utilisateurSeConnecterTextField;
    @FXML
    private JFXButton seConnecterLoginButton;
    @FXML
    private Label motDePasseOublieLabel;
    @FXML
    private FontAwesomeIconView quitterInscrireButton;
    @FXML
    private FontAwesomeIconView minimiserInscrireButton;
    @FXML
    private FontAwesomeIconView quitterMotDePasseOublieButton;
    @FXML
    private FontAwesomeIconView minimiserMotDePasseOublieButton;
    @FXML
    private FontAwesomeIconView quitterLoginButton;
    @FXML
    private FontAwesomeIconView minimiserLoginButton;
    @FXML
    private JFXPasswordField motDePasseInscrirePasswordField;
    @FXML
    private JFXPasswordField confirmerMotDePasseInscrirePasswordField;
    @FXML
    private JFXPasswordField motDePasseSeConnecterPasswordField;

    private double xOffset = 0;
    private double yOffset = 0;
    private UserFacade userFacade = new UserFacade();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        seConnecterAnchorPane.toFront();
    }

    @FXML
    private void seConnecterButtonOnAction(ActionEvent event) {
        utilisateurSeConnecterTextField.setText("");
        motDePasseSeConnecterPasswordField.setText("");
        seConnecterAnchorPane.toFront();
        utilisateurSeConnecterTextField.requestFocus();
    }

    @FXML
    private void inscrireButtonOnAction(ActionEvent event) {
        utilisateurInscrireTextField.setText("");
        emailInscrireTextField.setText("");
        motDePasseInscrirePasswordField.setText("");
        confirmerMotDePasseInscrirePasswordField.setText("");
        inscrireAnchorPane.toFront();
        utilisateurInscrireTextField.requestFocus();
    }

    @FXML
    private void motDePasseOublieLoginButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat/app/alerte/FXMLAlerte.fxml"));
        Parent root = (Parent) loader.load();
        FXMLAlerteController alerteController = loader.getController();
        Stage stageAlerte = new Stage();
        stageAlerte.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed((MouseEvent event1) -> {
            xOffset = event1.getSceneX();
            yOffset = event1.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event1) -> {
            stageAlerte.setX(event1.getScreenX() - xOffset);
            stageAlerte.setY(event1.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stageAlerte.initModality(Modality.APPLICATION_MODAL);
        stageAlerte.setScene(scene);
        
        if (!utilisateurMotDePasseOublieTextField.getText().isEmpty()) {
            int result = userFacade.sendPW(utilisateurMotDePasseOublieTextField.getText());
            switch (result) {
                case -2:
                    alerteController.erreurAnchorPane.toFront();
                    alerteController.erreurLabel.setText("Échec de l'envoi du mot de passe !");
                    stageAlerte.show();
                    break;
                case -1:
                    alerteController.erreurAnchorPane.toFront();
                    alerteController.erreurLabel.setText("Le nom d'utilisateur ou adresse e-mail n'existe pas !");
                    stageAlerte.show();
                    break;
                case 1:
                    alerteController.succesAnchorPane.toFront();
                    alerteController.succesLabel.setText("Votre mot de passe a été envoyé");
                    stageAlerte.show();
                    seConnecterButtonOnAction(new ActionEvent());
                    break;
            }
        } else {
            alerteController.infomationAnchorPane.toFront();
            alerteController.informationLabel.setText("Veuillez saisir votre nom d'utilisateur ou votre adresse e-mail");
            stageAlerte.show();
        }
    }

    @FXML
    private void inscrireLoginButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat/app/alerte/FXMLAlerte.fxml"));
        Parent root = (Parent) loader.load();
        FXMLAlerteController alerteController = loader.getController();
        Stage stageAlerte = new Stage();
        stageAlerte.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed((MouseEvent event1) -> {
            xOffset = event1.getSceneX();
            yOffset = event1.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event1) -> {
            stageAlerte.setX(event1.getScreenX() - xOffset);
            stageAlerte.setY(event1.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stageAlerte.initModality(Modality.APPLICATION_MODAL);
        stageAlerte.setScene(scene);
        
        User user = getInscrireParams();
        if (inscriptionPermis(user)) {
            Object[] res = userFacade.addUser(user);
            int res1 = (int) res[0];
            user = (User) res[1];
            if (res1 == 1) {
                alerteController.succesAnchorPane.toFront();
                alerteController.succesLabel.setText("Votre compte a été créé avec succès");
                stageAlerte.show();
                seConnecterButtonOnAction(new ActionEvent());
            } else {
                switch (res1) {
                    case -2:
                        alerteController.erreurAnchorPane.toFront();
                        alerteController.erreurLabel.setText("L'adresse e-mail existe déjà !");
                        stageAlerte.show();
                        break;
                    case -1:
                        alerteController.erreurAnchorPane.toFront();
                        alerteController.erreurLabel.setText("Le nom d'utilisateur existe déjà !");
                        stageAlerte.show();
                        break;
                }
            }
        } else {
            if (user.getUserName().equals("")) {
                alerteController.attentionAnchorPane.toFront();
                alerteController.attentionLabel.setText("Le nom d'utilisateur non valide !");
                stageAlerte.show();
            } else if (user.getEmail().equals("")) {
                alerteController.attentionAnchorPane.toFront();
                alerteController.attentionLabel.setText("L'adresse e-mail non valide !");
                stageAlerte.show();
            } else if (user.getPassword().equals("")) {
                alerteController.attentionAnchorPane.toFront();
                alerteController.attentionLabel.setText("Mot de passe non valide !");
                stageAlerte.show();
            } else {
                alerteController.attentionAnchorPane.toFront();
                alerteController.attentionLabel.setText("Les deux mots de passe ne sont pas identiques !");
                stageAlerte.show();
            }
        }

    }

    public boolean inscriptionPermis(User user) {
        if (user.getPassword().equals("") || !user.getPassword().equals(confirmerMotDePasseInscrirePasswordField.getText())) {
            return false; //il faut confirmer le mot de passe
        } else {
            return !(user.getUserName().equals("") || user.getEmail().equals(""));
        }
    }

    @FXML
    private void seConnecterLoginButtonOnAction(ActionEvent event) throws Exception {
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

        Scene sceneAlerte = new Scene(rootAlerte);
        sceneAlerte.setFill(Color.TRANSPARENT);
        stageAlerte.initModality(Modality.APPLICATION_MODAL);
        stageAlerte.setScene(sceneAlerte);

        User user = getSeConnecterParams();
        if (seConecterPermis(user)) {
            Object[] res = userFacade.seConnecter(user);
            int res1 = (int) res[0];
            user = (User) res[1];
            if (res1 == 1) {
                Session.setAttribut(res[1], "connectedUser");
                //Bienvenue 

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat/app/main/FXMLMain.fxml"));
                Parent root = (Parent) loader.load();
                FXMLMainController mainController = loader.getController();
                Stage stageMain = new Stage();
                stageMain.initStyle(StageStyle.TRANSPARENT);

                root.setOnMousePressed((MouseEvent event1) -> {
                    xOffset = event1.getSceneX();
                    yOffset = event1.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event1) -> {
                    stageMain.setX(event1.getScreenX() - xOffset);
                    stageMain.setY(event1.getScreenY() - yOffset);
                });

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stageMain.setScene(scene);
                ((Stage) seConnecterLoginButton.getScene().getWindow()).close();
                mainController.utilisateurLabel.setText(utilisateurSeConnecterTextField.getText());
                mainController.utilisateursAnchorPane.toFront();
                mainController.chatAppFirstAnchorPane.toFront();
                mainController.rechercherTextField.requestFocus();
                stageMain.show();

            } else {
                switch (res1) {
                    case -5:
                    case -4:
                        alerteController.erreurAnchorPane.toFront();
                        alerteController.erreurLabel.setText("Votre compte a été bloqué !");
                        stageAlerte.show();
                        break;
                    case -3:
                        alerteController.erreurAnchorPane.toFront();
                        alerteController.erreurLabel.setText("Le mot de passe est incorrect !");
                        stageAlerte.show();
                        break;
                    case -2:
                        alerteController.erreurAnchorPane.toFront();
                        alerteController.erreurLabel.setText("Le nom d'utilisateur n'existe pas !");
                        stageAlerte.show();
                        break;
                    case -1:
                        alerteController.attentionAnchorPane.toFront();
                        alerteController.attentionLabel.setText("Tout les champs sont requis !");
                        stageAlerte.show();
                        break;
                }
            }
        } else {
            alerteController.attentionAnchorPane.toFront();
            alerteController.attentionLabel.setText("Tout les champs sont requis !");
            stageAlerte.show();
        }

    }

    public boolean seConecterPermis(User user) {
        return !(utilisateurSeConnecterTextField.getText().isEmpty() || motDePasseSeConnecterPasswordField.getText().isEmpty());
    }

    private User getInscrireParams() {
        return new User(emailInscrireTextField.getText(), motDePasseInscrirePasswordField.getText(), utilisateurInscrireTextField.getText());
    }

    private User getSeConnecterParams() {
        return new User(motDePasseSeConnecterPasswordField.getText(), utilisateurSeConnecterTextField.getText());
    }

    @FXML
    private void motDePasseOublieLabelOnMouseClicked(MouseEvent event) {
        utilisateurMotDePasseOublieTextField.setText("");
        motDePasseOublieAnchorPane.toFront();
        utilisateurMotDePasseOublieTextField.requestFocus();
    }

    @FXML
    private void quitterInscrireButtonOnMouseClicked(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    private void minimiserInscrireButtonOnMouseClicked(MouseEvent event) {
        Stage stage = (Stage) ((Text) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void quitterMotDePasseOublieButtonOnMouseClicked(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    private void minimiserMotDePasseOublieButtonOnMouseClicked(MouseEvent event) {
        Stage stage = (Stage) ((Text) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void quitterLoginButtonOnMouseClicked(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    private void minimiserLoginButtonOnMouseClicked(MouseEvent event) {
        Stage stage = (Stage) ((Text) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

}
