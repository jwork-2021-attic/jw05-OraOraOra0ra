package start;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import world.World;


public class StartScene extends Application {

    private Pane pane = new Pane();
    private Stage stage;

    private int Height;
    private int Width;

    private ImageView bkView = new ImageView(new Image("background.jpeg"));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.Width = 960;
        this.Height = 540;
        initStage();
        initBackground();
        initButton();
        stage.show();
    }

    public void initStage() {
        Scene scene = new Scene(pane,Width,Height);
        stage.setScene(scene);
        stage.setTitle("The story of Huluwa");
    }
    
    public void initBackground() {
        bkView.setFitWidth(Width);
        bkView.setFitHeight(Height);
        pane.getChildren().add(bkView);
    }
    
    public void initButton() {
        Button btn = new Button("Start");
        btn.setOnAction(new StartHandler());
        btn.setLayoutX(Width/2-10);
        btn.setLayoutY(Height/2);
        pane.getChildren().add(btn);
    }

    class StartHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            PlotScene plot = new PlotScene();
            plot.start(stage, 1);
        }
    }
}
