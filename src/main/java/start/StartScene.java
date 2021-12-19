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
import javafx.stage.WindowEvent;
import screen.PlayScreen;
import screen.SnakeScreen;
import world.Creature;
import world.World;
import world.WorldBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class StartScene extends Application {

    private Pane pane = new Pane();
    private Stage stage;

    private int Height;
    private int Width;

    private ImageView bkView = new ImageView(new Image("background.jpeg"));

    ObjectInputStream objectInputStream;

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

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.print("监听到窗口关闭");
            }
        });
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

    public Boolean judgeLoad() {
        try {
            FileInputStream fileInputStream = new FileInputStream("test.txt");
            objectInputStream = new ObjectInputStream(fileInputStream);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    public void initButton() {
        Button btn = new Button("New Story");
        btn.setOnAction(new StartHandler());
        btn.setLayoutX(Width/2-10);
        btn.setLayoutY(Height/2);
        pane.getChildren().add(btn);

        if (judgeLoad()) {
            Button btn2 = new Button("Load Story");
            btn2.setOnAction(new LoadHandler());
            btn2.setLayoutX(Width/2-10);
            btn2.setLayoutY(Height/2+30);
            pane.getChildren().add(btn2);
        }
    }

    class StartHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            PlotScene plot = new PlotScene();
            plot.start(stage, 1, false);
        }
    }

    class LoadHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {

            Object scene = null;
            int whichScene = 0;
            try {
                scene = objectInputStream.readObject();
                whichScene = (int)scene;

                if (whichScene == 1 | whichScene == 3 | whichScene == 5) {
                    Object chap = objectInputStream.readObject();
                    int chapter = (int)chap;
                    PlotScene plot = new PlotScene();
                    plot.start(stage, chapter, true);
                } else if (whichScene == 2 | whichScene == 4) {
                    Object m = objectInputStream.readObject();
                    Object cl = objectInputStream.readObject();
                    int[][] map = (int[][])m;
                    World world = new WorldBuilder(map).makeMaze(map).build();
                    int[][] creasLoc = (int[][])cl;
                    Object s = null;
                    int[] score;
                    boolean[] state;

                    PlayScene ps = new PlayScene();
                    if (whichScene == 2) {
                        s = objectInputStream.readObject();
                        score = (int[]) s;
                        ps.start(whichScene, stage, world, creasLoc, score);
                    } else if (whichScene == 4) {
                        s = objectInputStream.readObject();
                        state = (boolean[])s;
                        ps.start(whichScene, stage, world, creasLoc, state);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }
}
