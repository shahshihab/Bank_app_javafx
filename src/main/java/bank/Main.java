package bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.animation.RotateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.geometry.Pos;

public class Main extends Application {
    private static String UI = "/UI.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create overlay for animation
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: white;");
        HBox splashBox = new HBox(20);
        splashBox.setAlignment(Pos.CENTER);

        // Logo design
        StackPane logoPane = new StackPane();
        Circle coin = new Circle(40, Color.GOLD);
        Text dollar = new Text("$");
        dollar.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-fill: #ffaa00ff;");
        logoPane.getChildren().addAll(coin, dollar);

        // Animate logo
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), logoPane);
        rotate.setByAngle(360);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.play();

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        Text title = new Text("THE BIG BANK");
        title.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-fill: #2288ff;");
        Text subtitle = new Text("The Bank For Everyone");
        subtitle.setStyle("-fx-font-size: 32px; -fx-fill: #333;");
        vbox.getChildren().addAll(title, subtitle);

        splashBox.getChildren().addAll(logoPane, vbox);
        overlay.getChildren().add(splashBox);

        Scene splashScene = new Scene(overlay, 800, 600);
        primaryStage.setScene(splashScene);
        primaryStage.setTitle("Big Bank");
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Fade out animation
        FadeTransition fade = new FadeTransition(Duration.seconds(2), overlay);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.seconds(5));
        fade.setOnFinished(_ -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(UI));
                Parent root = loader.load();
                Scene frame = new Scene(root);
                primaryStage.setScene(frame);
                primaryStage.centerOnScreen();

                // Animate banner circles
                Circle circle1 = (Circle) frame.lookup("#circle1");
                Circle circle2 = (Circle) frame.lookup("#circle2");
                Circle circle3 = (Circle) frame.lookup("#circle3");
                if (circle1 != null && circle2 != null && circle3 != null) {
                    Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(0),
                            new KeyValue(circle1.centerXProperty(), 00),
                            new KeyValue(circle2.centerXProperty(), 100),
                            new KeyValue(circle3.centerXProperty(), 200)
                        ),
                        new KeyFrame(Duration.seconds(2),
                            new KeyValue(circle1.centerXProperty(), 600),
                            new KeyValue(circle2.centerXProperty(), 700),
                            new KeyValue(circle3.centerXProperty(), 800)
                        ),
                        new KeyFrame(Duration.seconds(4),
                            new KeyValue(circle1.centerXProperty(), 00),
                            new KeyValue(circle2.centerXProperty(), 100),
                            new KeyValue(circle3.centerXProperty(), 200)
                        )
                    );
                    timeline.setCycleCount(Animation.INDEFINITE);
                    timeline.setAutoReverse(false);
                    timeline.play();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        fade.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
