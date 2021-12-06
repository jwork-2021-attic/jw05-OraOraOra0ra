package start;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import screen.PlayScreen;
import screen.RestartScreen;
import screen.Screen;
import screen.StartScreen;

import java.util.Random;

public class PlayScene extends Application {
    private int flag;
    private Thread t;

    private Stage stage;
    private PlayScreen screen;
    private Pane pane;
    private GridPane grid;

    private Text msg;
    private ImageView[] huluView = new ImageView[7];

    Random r = new Random();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        Scene scene = initScene();
        screen = new PlayScreen(huluView);

        stage.setScene(scene);
        stage.setTitle("The story of Huluwa");
        stage.show();

        setRepaintThread();
    }

    public Scene initScene() {
        pane = new Pane();
        initGridPane();
        initText();
        initHuluView(25);

        Scene scene = new Scene(pane,640,512);
        return scene;
    }

    public void initGridPane() {
        grid = new GridPane();
        grid.setLayoutX(0);
        grid.setLayoutY(0);
        pane.getChildren().add(grid);
    }

    public void initText(){
        msg = new Text();
        msg.setLayoutX(0);
        msg.setLayoutY(600);
        msg.setFont(Font.font(null, FontWeight.BOLD,15));
        pane.getChildren().add(msg);
    }

    public void initHuluView(int blockSize) {
        for (int i = 0; i < 7; i++) {
            huluView[i] = new ImageView((i+1) + ".png");
            huluView[i].setFitHeight(blockSize);
            huluView[i].setFitWidth(blockSize);
            int rx = r.nextInt(700);
            int ry = r.nextInt(500);
            huluView[i].setLayoutX(25*(rx/25));
            huluView[i].setLayoutY(25*(ry/25));
        }
    }

    private void setRepaintThread() {
        t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(50);
                    Platform.runLater(() -> {
                        grid.getChildren().clear();
                        repaint();
                    });
                    if (flag == 1) {
                        Thread.sleep(2000);
                        t.interrupt();
                    }
                } catch (InterruptedException e) {
                    Platform.runLater(() -> {
                        PlotScene plot = new PlotScene();
                        plot.start(stage,2);
                    });
                    break;
                }
            }
        });
        t.start();
    }

    public void repaint() {
        flag = screen.display(grid);
        if (screen instanceof PlayScreen) {
            setBackground(grid, Color.BLACK);
            ((PlayScreen) screen).displayMessages(msg);
        }
    }

    private void setBackground(Pane pane, Color color) {
        pane.setBackground(new Background(new BackgroundFill(color, null, null)));
    }
}
