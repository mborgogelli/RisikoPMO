package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainTest extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Creiamo un'etichetta con un messaggio
        Label label = new Label("JavaFX è installato correttamente!");
        
     // Crea alcuni nodi da impilare
        Rectangle rectangle = new Rectangle(200, 150);
        rectangle.setFill(Color.LIGHTBLUE);
        
        // Creiamo un layout e aggiungiamo l'etichetta
        StackPane root = new StackPane();
        root.getChildren().addAll(rectangle,label);
        
        // Creiamo la scena
        Scene scene = new Scene(root, 400, 200);
        
        // Impostiamo il titolo della finestra
        primaryStage.setTitle("Test JavaFX");
        
        // Impostiamo la scena e mostriamo la finestra
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        // Avvio dell'applicazione JavaFX
        launch(args);
    }
}
