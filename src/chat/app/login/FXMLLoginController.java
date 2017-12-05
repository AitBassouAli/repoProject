/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app.login;

import bean.User;
import chat.app.main.FXMLMainController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        seConnecterAnchorPane.toFront();
    }

    @FXML
    private void seConnecterButtonOnAction(ActionEvent event) {
        seConnecterAnchorPane.toFront();
        utilisateurSeConnecterTextField.requestFocus();
    }

    @FXML
    private void inscrireButtonOnAction(ActionEvent event) {
        inscrireAnchorPane.toFront();
        utilisateurInscrireTextField.requestFocus();
    }

    @FXML
    private void motDePasseOublieLoginButtonOnAction(ActionEvent event) {
        /*
        hada button dyal mot de passe oublier hnaya ghadi ichof wach lidakhal f textfield
        "utilisateurMotDePasseOublieTextField" ila kan email wala utilisateur ghadir
        requete i verifie dak email wala utilisateur wach kaynin f bd
        ila kaynin ghaygenere wahd mot de passe ibadlo f bd puis isifto l dak email 
         */
        if (!utilisateurMotDePasseOublieTextField.getText().isEmpty()) {
            userFacade.sendPW(utilisateurMotDePasseOublieTextField.getText());
        } else {
            DateUtil.alerter(Alert.AlertType.INFORMATION, null, null, "Saisie votre Email ");
        }
    }

    @FXML
    private void inscrireLoginButtonOnAction(ActionEvent event) {
        /*
        hada button dyal inscrire fhad button khas i verifie kbal wach dak utilisateur kayn deja
        wala la hta l email ila makanoch deja ghadi i ajouter automatique ila makanch i3ti erreur
        o hta password khas ikono matchabhin o fih au moins lettre o digit hna ghansta3mlo les petterns
        ila kan utilisateur kayn deja f bd khas textField "utilisateurInscrireTextField" twali b rouge
        ohta "emailInscrireTextField" dyal email ila kan deja twali b rouge 
         */
        User user = getInscrireParams();
        if (inscriptionPermis(user)) {
            Object[] res = userFacade.addUser(user);
            int res1 = (int) res[0];
            user = (User) res[1];
            if (res1 == 1) {
                seConnecterButtonOnAction(new ActionEvent());
            } else {
                afficheMessage(res1);
            }
        } else {
            DateUtil.alerter(Alert.AlertType.ERROR, "error", null, "Invalid Informations !");
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

        /*
        had button dyal se connecter
        khas hna madakhil user o password otacklicki 3la button dyal se connecter
        ghadi iverfier bwahd requete ila kan dak utilisateur kayn f base de donnes
        ila makanch ghadi i3ti wahd erreur oghadi irad had les textfield
        "utilisateurSeConnecterTextField" et "motDePasseSeConnecterPasswordField"
        b rouge oydir focus 3la textfield "utilisateurSeConnecterTextField" bach i3awd iktib
        obinisba lcode liltaht howa likaykhilina ndozo lpage main had lcode ndiroh mn ba3d manverifiew
        utilisateur bli kayn f bd
         */
 /*
        ohna ghanbadlo ta status nradoh true ya3ni mconnecte o nhato nouvelle ip
        bach dkhal onchofo ila kayn des historique dyal des conversation darhom had user
        ila kano khasna njibo les utilisateur lidar m3ahom f kol conversation
        onhatohom f "utilisateursListView" btartib 3la hssab date de creation likayna f base 
        de donne !! hadchi ila ghandiro les historique ila mandiroch hadchi saf ghadi nlistiw direct
        les utilisateurs online likaynin f base de donnes ya3ni 3andhom status true b ordre alphabetique
        okayn dak textfield Rechercher fokach ma ktib gha yfiltri les utilisateur online o y afichihom
        en meme temp f dik list !!
         */
 /*
         login function 
         */
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

                root.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
                });
                root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        stageMain.setX(event.getScreenX() - xOffset);
                        stageMain.setY(event.getScreenY() - yOffset);
                    }
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
                afficheMessage(res1);
            }
        } else {
            DateUtil.alerter(Alert.AlertType.ERROR, "error", null, "Tout les champs sont requis !");
        }

    }

    public boolean seConecterPermis(User user) {
        return !(utilisateurSeConnecterTextField.getText().isEmpty() || motDePasseSeConnecterPasswordField.getText().isEmpty());
    }

    public void afficheMessage(int res) {
        switch (res) {
            case -1:
                DateUtil.alerter(Alert.AlertType.ERROR, "error", null, "Login unvalide !");
                break;
            case -2:
                DateUtil.alerter(Alert.AlertType.ERROR, "error", null, "Mot de passe incorrect !");
                break;
            case -3:
                DateUtil.alerter(Alert.AlertType.ERROR, "error", null, "Email ou Nom D'utilisature deja prise !");
            case -6:
                DateUtil.alerter(Alert.AlertType.ERROR, "error", null, "Deja connectee sur un autre Session !");
                break;

        }
    }

    private User getInscrireParams() {
        return new User(emailInscrireTextField.getText(), motDePasseInscrirePasswordField.getText(), utilisateurInscrireTextField.getText());
    }

    private User getSeConnecterParams() {
        return new User(motDePasseSeConnecterPasswordField.getText(), utilisateurSeConnecterTextField.getText());
    }

    @FXML
    private void motDePasseOublieLabelOnMouseClicked(MouseEvent event) {
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
