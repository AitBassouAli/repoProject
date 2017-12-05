/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app.alerte;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author abdel
 */
public class FXMLAlerteController implements Initializable {

    @FXML
    public AnchorPane succesAnchorPane;
    @FXML
    private JFXButton annulerSuccesButton;
    @FXML
    public Label succesLabel;
    @FXML
    public AnchorPane infomationAnchorPane;
    @FXML
    private JFXButton annulerInformationButton;
    @FXML
    public Label informationLabel;
    @FXML
    public AnchorPane attentionAnchorPane;
    @FXML
    private JFXButton annulerAttentionButton;
    @FXML
    public Label attentionLabel;
    @FXML
    public AnchorPane erreurAnchorPane;
    @FXML
    private JFXButton annulerErreurButton;
    @FXML
    public Label erreurLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void annulerButtonOnAction(ActionEvent event) {
        ((Stage) annulerInformationButton.getScene().getWindow()).close();
    }
    
}
