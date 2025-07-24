package bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Main extends Application {
    private static String UI = "/UI.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create overlay for animation
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: white;");
        javafx.scene.layout.HBox splashBox = new javafx.scene.layout.HBox(20);
        splashBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Logo design
        javafx.scene.layout.StackPane logoPane = new javafx.scene.layout.StackPane();
        javafx.scene.shape.Circle coin = new javafx.scene.shape.Circle(40, javafx.scene.paint.Color.GOLD);
        javafx.scene.text.Text dollar = new javafx.scene.text.Text("$");
        dollar.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-fill: #ffffffff;");
        logoPane.getChildren().addAll(coin, dollar);

        // Animate logo
        javafx.animation.RotateTransition rotate = new javafx.animation.RotateTransition(javafx.util.Duration.seconds(2), logoPane);
        rotate.setByAngle(360);
        rotate.setCycleCount(javafx.animation.Animation.INDEFINITE);
        rotate.play();

        VBox vbox = new VBox(10);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        javafx.scene.text.Text title = new javafx.scene.text.Text("THE BIG BANK");
        title.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-fill: #2196F3;");
        javafx.scene.text.Text subtitle = new javafx.scene.text.Text("The Bank For Everyone");
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
        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(2), overlay);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(javafx.util.Duration.seconds(5));
        fade.setOnFinished(_ -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(UI));
                Parent root = loader.load();
                Scene frame = new Scene(root);
                primaryStage.setScene(frame);
                primaryStage.centerOnScreen();

                // Animate banner circles
                javafx.scene.shape.Circle circle1 = (javafx.scene.shape.Circle) frame.lookup("#circle1");
                javafx.scene.shape.Circle circle2 = (javafx.scene.shape.Circle) frame.lookup("#circle2");
                javafx.scene.shape.Circle circle3 = (javafx.scene.shape.Circle) frame.lookup("#circle3");
                if (circle1 != null && circle2 != null && circle3 != null) {
                    javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                        new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0),
                            new javafx.animation.KeyValue(circle1.centerXProperty(), 60),
                            new javafx.animation.KeyValue(circle2.centerXProperty(), 400),
                            new javafx.animation.KeyValue(circle3.centerXProperty(), 740)
                        ),
                        new javafx.animation.KeyFrame(javafx.util.Duration.seconds(2),
                            new javafx.animation.KeyValue(circle1.centerXProperty(), 740),
                            new javafx.animation.KeyValue(circle2.centerXProperty(), 60),
                            new javafx.animation.KeyValue(circle3.centerXProperty(), 400)
                        ),
                        new javafx.animation.KeyFrame(javafx.util.Duration.seconds(4),
                            new javafx.animation.KeyValue(circle1.centerXProperty(), 60),
                            new javafx.animation.KeyValue(circle2.centerXProperty(), 400),
                            new javafx.animation.KeyValue(circle3.centerXProperty(), 740)
                        )
                    );
                    timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
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
