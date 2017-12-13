/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.app.connexion;

import clientServices.ClientMT;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author abdel
 */
public class Connexion extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Stage stage = Connexion.getIconToStage(primaryStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLConnexion.fxml"));
        Parent root = (Parent) loader.load();
        FXMLConnexionController connexionController = loader.getController();
        stage.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        connexionController.adresseDuServeurTextField.requestFocus();
        stage.show();
    }

    public static Stage getIconToStage(Stage stage) {
        Image image = new Image("/chat/app/images/ChatAppIcon.png");
        stage.getIcons().add(image);
        return stage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
