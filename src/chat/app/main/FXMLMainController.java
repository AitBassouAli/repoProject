/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app.main;

import bean.Conversation;
import bean.Message;
import bean.Pays;
import bean.User;
import chat.app.alerte.FXMLAlerteController;
import chat.app.connexion.Connexion;
import chat.app.login.FXMLLoginController;
import clientServices.ClientMT;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.ConnectedUsersFacade;
import service.ConversationFacade;
import service.PaysFacade;
import service.UserFacade;
import util.DateUtil;
import static util.DateUtil.myConvertDateToStringFranch;
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
    private FontAwesomeIconView parametresConversationIconView;
    private Label motDePasseOublieLabel12;
    private Label dateDeCreationConversationLabel;
    @FXML
    private JFXTextField messageAEnvoyerTextArea;
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
    @FXML
    private JFXListView<User> conversationsListView;
    private double xOffset = 0;
    private double yOffset = 0;
    private UserFacade userFacade = new UserFacade();
    PaysFacade paysFacade = new PaysFacade();
    ConnectedUsersFacade connectedUsersFacade = new ConnectedUsersFacade();
    ConversationFacade conversationFacade = new ConversationFacade();
    private User connectedUser = (User) Session.getAttribut("connectedUser");
    private User selectedUser;
    private Conversation conversationCorante;
    @FXML
    private AnchorPane aucuneMessageAnchorPane;
    private Map<Long, String> messagesMap = new TreeMap<Long, String>();
    @FXML
    private Label utilisateurDateDeModificationLabel;
    private Thread t1;
    private Thread t2;
    private volatile boolean stop = false;
    @FXML
    private FontAwesomeIconView supprimerConversationIconView;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            utilisateursAnchorPane.toFront();
            readFromFile();
            List<Conversation> conversations = getConversation();
            if (conversations.isEmpty()) {
                aucuneMessageAnchorPane.toFront();
            } else {
                conversationsListView.getItems().setAll(getUserFromConversation(conversations));
                conversationsListView.toFront();
                utilisateursListView.toBack();
                aucuneMessageAnchorPane.toBack();
            }
            List<User> usersConnectee = userFacade.getUsers();
            usersConnectee.remove(connectedUser);
            utilisateursListView.getItems().setAll(usersConnectee);
            refreshMeassagesTextArea();
            refreshUtilisateursListView();
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refreshMeassagesTextArea() {
        new Thread(() -> {
            t1 = Thread.currentThread();
            System.out.println(Thread.currentThread().getName());
            while (!stop) {
                while (connectedUser != null) {
                    try {
                        // String[] msg = new ClientMT((Socket) Session.getAttribut("connectedSocket")).recieve();
                        Message message = new ClientMT((Socket) Session.getAttribut("connectedSocket")).recieve();
                        User user = userFacade.find(message.getSender());
                        String mesg = message.getMessage();
                        Platform.runLater(() -> {
                            try {
                                Conversation conversationReceive = conversationFacade.findOrCreate(new Conversation(user, connectedUser));
                                if (conversationCorante != null) {
                                    if ((conversationCorante.getReciever()).equals(user) || (conversationCorante.getSender()).equals(user)) {
                                        messagesTextArea.appendText(user.getUserName() + " : " + mesg + "\n");
                                    }
                                } else {
                                    if (conversationReceive != null) {
                                        selectedUser = user;
                                        conversationCorante = conversationReceive;
                                    }
                                    messagesTextArea.appendText(user.getUserName() + " : " + mesg + "\n");
                                }
                                if (conversationReceive != null) {
                                    if (!(messagesMap.containsKey(conversationReceive.getId()))) {
                                        messagesMap.put(conversationReceive.getId(), "");
                                    }
                                    messagesMap.put(conversationReceive.getId(), ((String) messagesMap.get(conversationReceive.getId()) + user.getUserName() + " : " + mesg + System.getProperty("line.separator")));
                                    if ("".equals(rechercherTextField.getText())) {
                                        messagesAnchorPane.toFront();
                                        conversationsListView.toFront();
                                        utilisateursListView.toBack();
                                        aucuneMessageAnchorPane.toBack();
                                    }
                                }
                            } catch (IOException ex) {
                                System.out.println("Probleme dans la fonction refreshMeassagesTextArea()");
                            }
                        });
                    } catch (ClassNotFoundException | IOException ex) {
                        Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            System.out.println("Stopped Thread 1");
        }).start();
    }

    public void refreshUtilisateursListView() {
        new Thread(() -> {
            t2 = Thread.currentThread();
            System.out.println(Thread.currentThread().getName());
            while (!stop) {
                while (connectedUser != null) {
                    try {
                        Thread.sleep(500);
                        if ("".equals(rechercherTextField.getText())) {
                            Platform.runLater(() -> {
                                try {
                                    initListView();
                                } catch (IOException ex) {
                                }
                            });
                            Platform.runLater(() -> {
                                try {
                                    initConverarionListView();
                                } catch (IOException ex) {
                                }
                            });
                        }
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        System.out.println("interrupted");

                    }
                }
            }
            System.out.println("Stopped Thread 2");
        }).start();
    }

    public void initListView() throws IOException {
        if (!stop) {
            List<User> usersConnectee = userFacade.getUsers();
            usersConnectee.remove(connectedUser);
            Platform.runLater(() -> {
                utilisateursListView.getItems().setAll(usersConnectee);
            });
        }
    }

    public void initConverarionListView() throws IOException {
        if (!stop) {
            List<Conversation> conversations = getConversation();
            Platform.runLater(() -> {
                conversationsListView.getItems().setAll(getUserFromConversation(conversations));
            });
        }
    }

    public void readFromFile() throws IOException {
        File folder = new File("logs/");
        File[] listOfFiles = folder.listFiles();
        List<Conversation> conversations = getConversation();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String fileName = (file.getName()).replace(".txt", "");

                long id = Long.parseLong(fileName);

                for (Conversation c : conversations) {
                    if (c.getId() == id) {
                        String path = "logs/" + file.getName();
                        String content = readFile(path);
                        messagesMap.put(id, content);

                        System.out.println(file.getName());
                        System.out.println(fileName);
                    }
                }
            }
        }
    }

    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public void writeFromFile() throws IOException {
        for (Map.Entry<Long, String> entry : messagesMap.entrySet()) {
            String path = "logs/" + entry.getKey() + ".txt";
            File file = new File(path);
            boolean b;
            if (!(file.exists())) {
                file.createNewFile();
            }
            if (file.exists()) {
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    bw.write(entry.getValue());
                    bw.append(System.getProperty("line.separator"));
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    System.out.println("Probleme dans writeFromFile() 1");
                } catch (Exception e) {
                    System.out.println("Probleme dans writeFromFile() 2");
                }
            }
        }
    }

    public List<User> getUserFromConversation(List<Conversation> conversations) {
        List<User> users = userFacade.getUserFromConversation(connectedUser, conversations);
        return users;
    }

    public List<Conversation> getConversation() throws IOException {
        List<Conversation> conversations = conversationFacade.findByUser(connectedUser);
        return conversations;
    }

    @FXML
    private void imageProfilUtilisateurImageViewOnMouseClicked(MouseEvent event) {

    }

    @FXML
    private void motDePasseOublieLabelOnMouseClicked(MouseEvent event) {
    }

    @FXML
    private void parametresLabelOnMouseClicked(MouseEvent event) throws IOException {
        initConnectedUserInfos();
        parametresAnchorPane.toFront();
        utilisateurTextField.setDisable(true);
        chatAppSecondAnchorPane.toFront();
    }

    private void initConnectedUserInfos() throws IOException {
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

    private void deconnect() throws IOException {
        connectedUser = userFacade.deconnecter(connectedUser);
        Session.setAttribut(null, "connectedUser");
    }

    @FXML
    private void deconnexionLabelOnMouseClicked(MouseEvent event) throws IOException, InterruptedException {
        writeFromFile();
        stop = true;
        deconnect();
        Thread.sleep(1000);

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
        ((Stage) deconnexionLabel.getScene().getWindow()).close();
        loginController.seConnecterAnchorPane.toFront();
        loginController.utilisateurSeConnecterTextField.requestFocus();
        stageLogin.show();
    }

    @FXML
    private void minimiserLoginButtonOnMouseClicked(MouseEvent event) {
    }

    @FXML
    private void enregistrerButtonOnAction(ActionEvent event) throws ParseException, IOException {
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

        User user = getNewParams();
        if (conditionPermis(user)) {
            user = userFacade.modifier(user, connectedUser);
            if (user == null) {
                alerteController.erreurAnchorPane.toFront();
                alerteController.erreurLabel.setText("Le nom d'utilisateur ou adresse e-mail existe déjà !");
                stageAlerte.show();
            } else {
                connectedUser = user;
                alerteController.succesAnchorPane.toFront();
                alerteController.succesLabel.setText("Les modifications ont été appliquées");
                stageAlerte.show();
                utilisateursAnchorPane.toFront();
                chatAppFirstAnchorPane.toFront();
                rechercherTextField.requestFocus();
                initListView();
            }

        } else if (user.getUserName().equals("")) {
            alerteController.attentionAnchorPane.toFront();
            alerteController.attentionLabel.setText("Le nom d'utilisateur non valide !");
            stageAlerte.show();
        } else if (user.getEmail().equals("")) {
            alerteController.attentionAnchorPane.toFront();
            alerteController.attentionLabel.setText("L'adresse e-mail non valide !");
            stageAlerte.show();
        } else {
            alerteController.attentionAnchorPane.toFront();
            alerteController.attentionLabel.setText("Les deux mots de passe ne sont pas identiques !");
            stageAlerte.show();
        }
    }

    @FXML
    private void AnnulerButtonOnAction(ActionEvent event) throws IOException {
        utilisateursAnchorPane.toFront();
        chatAppFirstAnchorPane.toFront();
        rechercherTextField.setText("");
        rechercherTextField.requestFocus();
        rechercherTextFieldOnKeyReleased(null);
    }

    @FXML
    private void envoyerMessageIconViewOnMouseClicked(MouseEvent event) throws IOException {
        String msg = messageAEnvoyerTextArea.getText();
        messageAEnvoyerTextArea.setText("");
        User userDist = selectedUser;
        messagesTextArea.applyCss();
        messagesTextArea.appendText("Vous  : " + msg + "\n");
        if (messagesMap.containsKey(conversationCorante.getId())) {
            messagesMap.put(conversationCorante.getId(), ((String) messagesMap.get(conversationCorante.getId()) + "Vous  : " + msg + System.getProperty("line.separator")));
        }
        if (conversationCorante != null) {
            connectedUsersFacade.envoyer(connectedUser, userDist, msg, conversationCorante);
        }
    }

    @FXML
    private void quitterButtonOnMouseClicked(MouseEvent event) throws IOException, InterruptedException {
        //connectedUser = userFacade.quiter(selectedUser);
        writeFromFile();
        stop = true;
        deconnect();
        Thread.sleep(1000);
        Platform.exit();
    }

    @FXML
    private void minimiserButtonOnMouseClicked(MouseEvent event) {
        Stage stage = (Stage) ((Text) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void utilisateursListViewOnMouseClicked(MouseEvent event) throws IOException {
        selectedUser = utilisateursListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null || selectedUser.getId() == null) {
            conversationCorante = conversationFacade.findOrCreate(new Conversation(connectedUser, selectedUser));
            List<Conversation> conversations = getConversation();

            conversationsListView.getItems().setAll(getUserFromConversation(conversations));
            rechercherTextField.setText("");
            conversationsListView.toFront();
            utilisateursListView.toBack();
            aucuneMessageAnchorPane.toBack();

            if (!(messagesMap.containsKey(conversationCorante.getId()))) {
                messagesMap.put(conversationCorante.getId(), "");
            }

            messagesTextArea.clear();
            String userDate = selectedUser.getUserName() + " : " + conversationCorante.getDateModification();
            utilisateurDateDeModificationLabel.setText(userDate);
            messagesTextArea.setText((String) messagesMap.get(conversationCorante.getId()));
            messagesAnchorPane.toFront();
        }
    }

    @FXML
    private void conversationsListViewOnMouseClicked(MouseEvent event) throws IOException, ParseException {
        selectedUser = conversationsListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            conversationCorante = conversationFacade.findOrCreate(new Conversation(connectedUser, selectedUser));
            messagesTextArea.clear();
            String userDate = selectedUser.getUserName() + " : " + myConvertDateToStringFranch(conversationCorante.getDateModification());
            utilisateurDateDeModificationLabel.setText(userDate);

            if (!(messagesMap.containsKey(conversationCorante.getId()))) {
                messagesMap.put(conversationCorante.getId(), "");
            }

            if (messagesMap.containsKey(conversationCorante.getId())) {
                messagesTextArea.setText((String) messagesMap.get(conversationCorante.getId()));
            }
            messagesAnchorPane.toFront();
        }
    }

    @FXML
    private void messageAEnvoyerTextAreaOnKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            envoyerMessageIconViewOnMouseClicked(null);
        }
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
            return false;
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

    public JFXTextField getMessageAEnvoyerTextArea() {
        return messageAEnvoyerTextArea;
    }

    public void setMessageAEnvoyerTextArea(JFXTextField messageAEnvoyerTextArea) {
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

    @FXML
    private void rechercherTextFieldOnKeyReleased(KeyEvent event) throws IOException {
        List<User> utilisateursRecherche = userFacade.findUsersContaints(rechercherTextField.getText());
        utilisateursRecherche.remove(connectedUser);
        chatAppFirstAnchorPane.toFront();
        if ("".equals(rechercherTextField.getText())) {
            List<Conversation> conversations = getConversation();
            if (conversations.isEmpty()) {
                aucuneMessageAnchorPane.toFront();
            } else {
                conversationsListView.getItems().setAll(getUserFromConversation(conversations));
                conversationsListView.toFront();
                utilisateursListView.toBack();
                aucuneMessageAnchorPane.toBack();
            }
        } else {
            utilisateursListView.getItems().setAll(utilisateursRecherche);
            utilisateursListView.toFront();
            conversationsListView.toBack();
            aucuneMessageAnchorPane.toBack();
        }
    }

    @FXML
    private void supprimerConversationIconViewOnMouseClicked(MouseEvent event) {
    }

}
