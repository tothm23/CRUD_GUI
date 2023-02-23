package crud_gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Tóth Milán
 *
 * https://www.youtube.com/watch?v=fML0QS9t_Po
 * https://color.adobe.com/create/color-wheel
 *
 * A #2E28F0 B #2F60F7 C #368EE0 D #2FCDF7 E #2DEDE0
 * https://o7planning.org/11529/javafx-alert-dialog
 * https://edencoding.com/style-tableview-javafx/
 */
public class CRUD_GUI extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("CRUD_GUI.fxml"));
        String css = CRUD_GUI.class.getResource("style.css").toExternalForm();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);

        stage.setTitle("CRUD - GUI application");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

        // When I click on the window, xOffset and yOffset will be initialized
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Subtracts the cursor position from the display position
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
