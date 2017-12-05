/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app.main;

import bean.Pays;
import bean.User;
import chat.app.login.FXMLLoginController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.PaysFacade;
import service.UserFacade;
import util.DateUtil;
import util.Session;

/**
 * FXML Controller class
 *
 * @author abdel
 */
public class FXMLMainController implements Initializable {

    @FXML
    private AnchorPane profilUtilisateurAnchorPane;
    @FXML
    private MaterialDesignIconView imageProfilUtilisateurIconView;
    @FXML
    private ImageView imageProfilUtilisateurImageView;
    @FXML
    public Label utilisateurLabel;
    @FXML
    private Label parametresLabel;
    @FXML
    private Label deconnexionLabel;
    @FXML
    public AnchorPane utilisateursAnchorPane;
    @FXML
    private AnchorPane utilisateurs;
    @FXML
    private FontAwesomeIconView minimiserLoginButton;
    @FXML
    public JFXTextField rechercherTextField;
    @FXML
    private JFXListView<User> utilisateursListView;
    @FXML
    public AnchorPane chatAppSecondAnchorPane;
    @FXML
    private AnchorPane parametresAnchorPane;
    @FXML
    private JFXTextField nomTextField;
    @FXML
    private JFXTextField prenomTextField;
    @FXML
    private JFXTextField emailTextField;
    @FXML
    private JFXDatePicker dateDeNaissanceDatePicker = new JFXDatePicker();
    @FXML
    private JFXComboBox<String> sexeComboBox;
    @FXML
    private JFXComboBox<Pays> paysComboBox;
    @FXML
    private JFXTextField utilisateurTextField;
    @FXML
    private JFXPasswordField motDePassePasswordField;
    @FXML
    private JFXPasswordField confirmerMotDePassePasswordField;
    @FXML
    private JFXButton enregistrerButton;
    @FXML
    private JFXButton AnnulerButton;
    @FXML
    private AnchorPane messagesAnchorPane;
    @FXML
    private FontAwesomeIconView parametresConversationIconView;
    @FXML
    private Label motDePasseOublieLabel12;
    @FXML
    private Label dateDeCreationConversationLabel;
    @FXML
    private JFXTextArea messageAEnvoyerTextArea;
    @FXML
    private FontAwesomeIconView envoyerMessageIconView;
    @FXML
    private TextArea messagesTextArea;
    @FXML
    public AnchorPane chatAppFirstAnchorPane;
    @FXML
    private FontAwesomeIconView quitterButton;
    @FXML
    private FontAwesomeIconView minimiserButton;

    private double xOffset = 0;
    private double yOffset = 0;
    private UserFacade userFacade = new UserFacade();
    private PaysFacade paysFacade = new PaysFacade();
    private User connectedUser = (User) Session.getAttribut("connectedUser");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(() -> {
            while (connectedUser != null) {
                try {
                    initListView();
                    System.out.println("initialisation ....");
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    System.out.println("interrupted");
                }
            }
        }).start();
        List<User> usersConnectee = userFacade.getUsers();
        usersConnectee.remove(connectedUser);
        utilisateursListView.getItems().setAll(usersConnectee);
        // TODO
    }

    public void initListView() {
        List<User> usersConnectee = userFacade.getUsers();
        usersConnectee.remove(connectedUser);
        utilisateursListView.getItems().setAll(usersConnectee);
        // TODO
    }

    @FXML
    private void imageProfilUtilisateurImageViewOnMouseClicked(MouseEvent event) {
        /*
        hna maykliki user hnaya khas i3tih ibadal photo dyalo maybadal khas taficha f "imageProfilUtilisateurImageView"
        o en meme temp tsajal f bd
         */
    }

    @FXML
    private void motDePasseOublieLabelOnMouseClicked(MouseEvent event) {
    }

    @FXML
    private void parametresLabelOnMouseClicked(MouseEvent event) {
        /*
        hna ma tcklicki 3la parametre khas t3amar les text field likaynin f "parametresAnchorPane"
        intilakan mn bd bhal Nom et Prenom et Email etc 3la hssab user lidkhil l app o t3amar
        combobox d Sexe (masculin et feminin) et combobox dyal pays hadi njiboha mn base de donnes 
        nkono deja 3amarna table d pays b les pays likaynin
         */
        initConnectedUserInfos();
        parametresAnchorPane.toFront();
        chatAppSecondAnchorPane.toFront();
    }

    private void initConnectedUserInfos() {
        nomTextField.setText(connectedUser.getNom() == null ? "" : connectedUser.getNom());
        prenomTextField.setText(connectedUser.getPrenom() == null ? "" : connectedUser.getPrenom());
        emailTextField.setText(connectedUser.getEmail() == null ? "" : connectedUser.getEmail());
        dateDeNaissanceDatePicker.setValue(connectedUser.getDateNaissance() == null ? LocalDate.now() : DateUtil.dateToLocalDate(connectedUser.getDateNaissance()));
        sexeComboBox.setItems(FXCollections.observableArrayList(Arrays.asList("Muscelin", "Fiminun")));
        sexeComboBox.getSelectionModel().select(connectedUser.getSexe());
        paysComboBox.setItems(FXCollections.observableArrayList(paysFacade.findAll()));
        paysComboBox.getSelectionModel().select(connectedUser.getPaye() == null ? paysComboBox.getItems().get(0) : connectedUser.getPaye());
        utilisateurTextField.setText(connectedUser.getUserName() == null ? "" : connectedUser.getUserName());
    }

    private void deconnect() {
        connectedUser = userFacade.deconnecter(connectedUser);
        Session.setAttribut(null, "connectedUser");
    }

    @FXML
    private void deconnexionLabelOnMouseClicked(MouseEvent event) throws IOException {

        /*
        hada dyal Deconnexion hnaya utilisateur maycklicki 3la deconnexion khas status twili false
        o IP iwili null o lcode likayn ltaht kayhiyad interface main okayaftah login
         */
        deconnect();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat/app/login/FXMLLogin.fxml"));
        Parent root = (Parent) loader.load();
        FXMLLoginController loginController = loader.getController();
        Stage stageLogin = new Stage();
        stageLogin.initStyle(StageStyle.TRANSPARENT);

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
                stageLogin.setX(event.getScreenX() - xOffset);
                stageLogin.setY(event.getScreenY() - yOffset);
            }
        });

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stageLogin.setScene(scene);
        ((Stage) deconnexionLabel.getScene().getWindow()).close();
        loginController.seConnecterAnchorPane.toFront();
        loginController.utilisateurSeConnecterTextField.requestFocus();
        stageLogin.show();
    }

    @FXML
    private void minimiserLoginButtonOnMouseClicked(MouseEvent event) {
    }

    @FXML
    private void enregistrerButtonOnAction(ActionEvent event) throws ParseException {
        /*
        fhad button khas tkon enregistrer l ga3 les informations limodifya utilisateurs
         */
        User user = getNewParams();
        if (conditionPermis(user)) {
            user = userFacade.modifier(user, connectedUser);
            if (user == null) {
                DateUtil.alerter(Alert.AlertType.INFORMATION, null, null, "Email or Nom d'utilisateur deja prise !");
            } else {
                connectedUser = user;
                initListView();
                utilisateursAnchorPane.toFront();
                chatAppFirstAnchorPane.toFront();
                rechercherTextField.requestFocus();
            }

        } else {
            DateUtil.alerter(Alert.AlertType.INFORMATION, null, null, "Modefication non permise avec ces donnees !");
        }
    }

    @FXML
    private void AnnulerButtonOnAction(ActionEvent event) {
        initListView();
        utilisateursAnchorPane.toFront();
        chatAppFirstAnchorPane.toFront();
        rechercherTextField.requestFocus();
    }

    @FXML
    private void envoyerMessageIconViewOnMouseClicked(MouseEvent event) {
        /*
        hhhhhh hna likayn lkhadma had button howa lighaysift email l user 2 
        ya3ni les socket etc hhh
         */
    }

    @FXML
    private void quitterButtonOnMouseClicked(MouseEvent event) {
        deconnect();
        Platform.exit();
    }

    @FXML
    private void minimiserButtonOnMouseClicked(MouseEvent event) {
        Stage stage = (Stage) ((Text) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void utilisateursListViewOnMouseClicked(MouseEvent event) {
        messagesAnchorPane.toFront();
    }

    @FXML
    private void rechercherTextFieldOnKeyPressed(KeyEvent event) {
        /*
        hnaya ghaykon filtrage dyal les utilisateurs limconnectin fokach ma ktib user
        ghatabda ListView taffichier les utilisateur likaybdaw bdackchi liktab utilisateur
         */
    }

    private User getNewParams() throws ParseException {
        User user = new User();
        user.setId(connectedUser.getId());
        user.setNom(nomTextField.getText());
        user.setPrenom(prenomTextField.getText());
        user.setEmail(emailTextField.getText());
        user.setUserName(utilisateurTextField.getText());
        user.setDateNaissance(DateUtil.localDateToDate(dateDeNaissanceDatePicker.getValue()));
        user.setSexe("Muscelin".equals(sexeComboBox.getValue()) ? 0 : 1);
        user.setPaye(paysComboBox.getValue());
        user.setPassword(motDePassePasswordField.getText());
        return user;
    }

    private boolean conditionPermis(User user) {
        if (!user.getPassword().equals("") && !confirmerMotDePassePasswordField.getText().equals("") && !user.getPassword().equals(confirmerMotDePassePasswordField.getText())) {
            return false; //il faut confirmer le mot de passe
        } else {
            return !(user.getUserName().equals("") || user.getEmail().equals(""));
        }
    }

    public AnchorPane getProfilUtilisateurAnchorPane() {
        return profilUtilisateurAnchorPane;
    }

    public void setProfilUtilisateurAnchorPane(AnchorPane profilUtilisateurAnchorPane) {
        this.profilUtilisateurAnchorPane = profilUtilisateurAnchorPane;
    }

    public MaterialDesignIconView getImageProfilUtilisateurIconView() {
        return imageProfilUtilisateurIconView;
    }

    public void setImageProfilUtilisateurIconView(MaterialDesignIconView imageProfilUtilisateurIconView) {
        this.imageProfilUtilisateurIconView = imageProfilUtilisateurIconView;
    }

    public ImageView getImageProfilUtilisateurImageView() {
        return imageProfilUtilisateurImageView;
    }

    public void setImageProfilUtilisateurImageView(ImageView imageProfilUtilisateurImageView) {
        this.imageProfilUtilisateurImageView = imageProfilUtilisateurImageView;
    }

    public Label getUtilisateurLabel() {
        return utilisateurLabel;
    }

    public void setUtilisateurLabel(Label utilisateurLabel) {
        this.utilisateurLabel = utilisateurLabel;
    }

    public Label getParametresLabel() {
        return parametresLabel;
    }

    public void setParametresLabel(Label parametresLabel) {
        this.parametresLabel = parametresLabel;
    }

    public Label getDeconnexionLabel() {
        return deconnexionLabel;
    }

    public void setDeconnexionLabel(Label deconnexionLabel) {
        this.deconnexionLabel = deconnexionLabel;
    }

    public AnchorPane getUtilisateursAnchorPane() {
        return utilisateursAnchorPane;
    }

    public void setUtilisateursAnchorPane(AnchorPane utilisateursAnchorPane) {
        this.utilisateursAnchorPane = utilisateursAnchorPane;
    }

    public AnchorPane getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(AnchorPane utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public FontAwesomeIconView getMinimiserLoginButton() {
        return minimiserLoginButton;
    }

    public void setMinimiserLoginButton(FontAwesomeIconView minimiserLoginButton) {
        this.minimiserLoginButton = minimiserLoginButton;
    }

    public JFXTextField getRechercherTextField() {
        return rechercherTextField;
    }

    public void setRechercherTextField(JFXTextField rechercherTextField) {
        this.rechercherTextField = rechercherTextField;
    }

    public JFXListView<?> getUtilisateursListView() {
        return utilisateursListView;
    }

    public void setUtilisateursListView(JFXListView<User> utilisateursListView) {
        this.utilisateursListView = utilisateursListView;
    }

    public AnchorPane getChatAppSecondAnchorPane() {
        return chatAppSecondAnchorPane;
    }

    public void setChatAppSecondAnchorPane(AnchorPane chatAppSecondAnchorPane) {
        this.chatAppSecondAnchorPane = chatAppSecondAnchorPane;
    }

    public AnchorPane getParametresAnchorPane() {
        return parametresAnchorPane;
    }

    public void setParametresAnchorPane(AnchorPane parametresAnchorPane) {
        this.parametresAnchorPane = parametresAnchorPane;
    }

    public JFXTextField getNomTextField() {
        return nomTextField;
    }

    public void setNomTextField(JFXTextField nomTextField) {
        this.nomTextField = nomTextField;
    }

    public JFXTextField getPrenomTextField() {
        return prenomTextField;
    }

    public void setPrenomTextField(JFXTextField prenomTextField) {
        this.prenomTextField = prenomTextField;
    }

    public JFXTextField getEmailTextField() {
        return emailTextField;
    }

    public void setEmailTextField(JFXTextField emailTextField) {
        this.emailTextField = emailTextField;
    }

    public JFXDatePicker getDateDeNaissanceDatePicker() {
        return dateDeNaissanceDatePicker;
    }

    public void setDateDeNaissanceDatePicker(JFXDatePicker dateDeNaissanceDatePicker) {
        this.dateDeNaissanceDatePicker = dateDeNaissanceDatePicker;
    }

    public JFXComboBox<String> getSexeComboBox() {
        return sexeComboBox;
    }

    public void setSexeComboBox(JFXComboBox<String> sexeComboBox) {
        this.sexeComboBox = sexeComboBox;
    }

    public JFXComboBox<Pays> getPaysComboBox() {
        return paysComboBox;
    }

    public void setPaysComboBox(JFXComboBox<Pays> paysComboBox) {
        this.paysComboBox = paysComboBox;
    }

    public JFXTextField getUtilisateurTextField() {
        return utilisateurTextField;
    }

    public void setUtilisateurTextField(JFXTextField utilisateurTextField) {
        this.utilisateurTextField = utilisateurTextField;
    }

    public JFXPasswordField getMotDePassePasswordField() {
        return motDePassePasswordField;
    }

    public void setMotDePassePasswordField(JFXPasswordField motDePassePasswordField) {
        this.motDePassePasswordField = motDePassePasswordField;
    }

    public JFXPasswordField getConfirmerMotDePassePasswordField() {
        return confirmerMotDePassePasswordField;
    }

    public void setConfirmerMotDePassePasswordField(JFXPasswordField confirmerMotDePassePasswordField) {
        this.confirmerMotDePassePasswordField = confirmerMotDePassePasswordField;
    }

    public JFXButton getEnregistrerButton() {
        return enregistrerButton;
    }

    public void setEnregistrerButton(JFXButton enregistrerButton) {
        this.enregistrerButton = enregistrerButton;
    }

    public JFXButton getAnnulerButton() {
        return AnnulerButton;
    }

    public void setAnnulerButton(JFXButton AnnulerButton) {
        this.AnnulerButton = AnnulerButton;
    }

    public AnchorPane getMessagesAnchorPane() {
        return messagesAnchorPane;
    }

    public void setMessagesAnchorPane(AnchorPane messagesAnchorPane) {
        this.messagesAnchorPane = messagesAnchorPane;
    }

    public FontAwesomeIconView getParametresConversationIconView() {
        return parametresConversationIconView;
    }

    public void setParametresConversationIconView(FontAwesomeIconView parametresConversationIconView) {
        this.parametresConversationIconView = parametresConversationIconView;
    }

    public Label getMotDePasseOublieLabel12() {
        return motDePasseOublieLabel12;
    }

    public void setMotDePasseOublieLabel12(Label motDePasseOublieLabel12) {
        this.motDePasseOublieLabel12 = motDePasseOublieLabel12;
    }

    public Label getDateDeCreationConversationLabel() {
        return dateDeCreationConversationLabel;
    }

    public void setDateDeCreationConversationLabel(Label dateDeCreationConversationLabel) {
        this.dateDeCreationConversationLabel = dateDeCreationConversationLabel;
    }

    public JFXTextArea getMessageAEnvoyerTextArea() {
        return messageAEnvoyerTextArea;
    }

    public void setMessageAEnvoyerTextArea(JFXTextArea messageAEnvoyerTextArea) {
        this.messageAEnvoyerTextArea = messageAEnvoyerTextArea;
    }

    public FontAwesomeIconView getEnvoyerMessageIconView() {
        return envoyerMessageIconView;
    }

    public void setEnvoyerMessageIconView(FontAwesomeIconView envoyerMessageIconView) {
        this.envoyerMessageIconView = envoyerMessageIconView;
    }

    public TextArea getMessagesTextArea() {
        return messagesTextArea;
    }

    public void setMessagesTextArea(TextArea messagesTextArea) {
        this.messagesTextArea = messagesTextArea;
    }

    public AnchorPane getChatAppFirstAnchorPane() {
        return chatAppFirstAnchorPane;
    }

    public void setChatAppFirstAnchorPane(AnchorPane chatAppFirstAnchorPane) {
        this.chatAppFirstAnchorPane = chatAppFirstAnchorPane;
    }

    public FontAwesomeIconView getQuitterButton() {
        return quitterButton;
    }

    public void setQuitterButton(FontAwesomeIconView quitterButton) {
        this.quitterButton = quitterButton;
    }

    public FontAwesomeIconView getMinimiserButton() {
        return minimiserButton;
    }

    public void setMinimiserButton(FontAwesomeIconView minimiserButton) {
        this.minimiserButton = minimiserButton;
    }

    public double getxOffset() {
        return xOffset;
    }

    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

}
