package start;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import screen.*;
import world.Creature;
import world.World;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import static world.World.blockSize;

public class PlayScene extends Application {

    private int sceneNo;

    private int flag;
    private Thread t;

    private World world;

    private  Creature[] creas;

    private final Image floorR = new Image("floorR.png");
    private final Image floorO = new Image("floorO.png");
    private final Image floorY = new Image("floorY.png");
    private final Image floorG = new Image("floorG.png");
    private final Image floorC = new Image("floorC.png");
    private final Image floorB = new Image("floorB.png");
    private final Image floorP = new Image("floorP.png");
    private final Image floorS = new Image("floorS.png");

    private ImageView[] scoreView = new ImageView[8];
    private ImageView[] floorView = new ImageView[8];

    private int Height;
    private int Width;

    private Stage stage;
    private PlayScreen screen;
    private SnakeScreen sscreen;
    private int whichScreen;
    private Pane pane;
    private GridPane grid;

    private Text msg;
    private ImageView[] huluView = new ImageView[7];

    Random r = new Random();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    //sceneNo==2
    public void start(Stage stage) {
        sceneNo = 2;
        whichScreen=1;
        this.stage = stage;
        Scene scene = initScene();
        initScoreView();
        screen = new PlayScreen(huluView);

        stage.setScene(scene);
        stage.setTitle("The story of Huluwa");
        stage.show();

        setRepaintThread();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    FileOutputStream fileOutputStream = null;
                    fileOutputStream = new FileOutputStream("test.txt");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(sceneNo);
                    objectOutputStream.writeObject(screen.getWorld().getMap());
                    objectOutputStream.writeObject(screen.getCreaturesLocation());
                    objectOutputStream.writeObject(screen.getCreaturesScore());
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //sceneNo==4
    public void start(int sceneNo, Stage stage, World world, int[][] creasLoc, boolean[] state) {
        this.sceneNo = sceneNo;
        this.world = world;
        this.stage = stage;

        whichScreen=2;
        sscreen = new SnakeScreen(huluView, world, creasLoc, state);

        Scene scene = initScene();

        stage.setScene(scene);
        stage.setTitle("The story of Huluwa");
        stage.show();

        setRepaintThread();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    FileOutputStream fileOutputStream = null;
                    fileOutputStream = new FileOutputStream("test.txt");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(sceneNo);
                    objectOutputStream.writeObject(sscreen.getWorld().getMap());
                    objectOutputStream.writeObject(sscreen.getCreaturesLocation());
                    objectOutputStream.writeObject(sscreen.getWorld().getState());
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //sceneNo==2
    public void start(int sceneNo, Stage stage, World world, int[][] creasLoc, int[] score) {
        this.sceneNo = sceneNo;
        this.world = world;
        this.stage = stage;

        whichScreen=1;
        screen = new PlayScreen(huluView, world, creasLoc, score);

        Scene scene = initScene();
        initScoreView();

        stage.setScene(scene);
        stage.setTitle("The story of Huluwa");
        stage.show();

        setRepaintThread();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    FileOutputStream fileOutputStream = null;
                    fileOutputStream = new FileOutputStream("test.txt");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(sceneNo);
                    objectOutputStream.writeObject(screen.getWorld().getMap());
                    objectOutputStream.writeObject(screen.getCreaturesLocation());
                    objectOutputStream.writeObject(screen.getCreaturesScore());
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public Scene initScene() {
        pane = new Pane();
        initGridPane();
        initText();
        initHuluView(25);

        if (whichScreen==1) {
            Height = 680;
            Width = 640;
        } else if (whichScreen==2) {
            Height = 600;
            Width = 640;
        }
        Scene scene = new Scene(pane,Width,Height);
        grid.requestFocus();
        return scene;
    }

    public void initGridPane() {
        grid = new GridPane();
        grid.setLayoutX(0);
        grid.setLayoutY(0);
        if (whichScreen==2) {
            grid.setOnKeyPressed(keyEvent -> sscreen = sscreen.respondToUserInput(keyEvent));
        }
        pane.getChildren().add(grid);
    }

    public void initText() {
        msg = new Text();
        msg.setLayoutX(150);
        msg.setLayoutY(540);
        msg.setFont(Font.font(null, FontWeight.BOLD,15));
        pane.getChildren().add(msg);
    }

    public void initScoreView() {
        for (int i = 0; i < 7; i++) {
            scoreView[i] = new ImageView(new Image((i + 1) + ".png"));
            scoreView[i].setFitHeight(blockSize);
            scoreView[i].setFitWidth(blockSize);
            if (i%2 == 0) {
                scoreView[i].setLayoutX(120);
                scoreView[i].setLayoutY(520 + i/2 * 40);
            } else {
                scoreView[i].setLayoutX(405);
                scoreView[i].setLayoutY(520 + i/2 * 40);
            }
            pane.getChildren().add(scoreView[i]);
        }
        scoreView[7] = new ImageView(new Image("snake.png"));
        scoreView[7].setFitHeight(blockSize);
        scoreView[7].setFitWidth(blockSize);
        scoreView[7].setLayoutX(405);
        scoreView[7].setLayoutY(640);
        pane.getChildren().add(scoreView[7]);

        floorView[0] = new ImageView(new Image("floorR.png"));
        floorView[1] = new ImageView(new Image("floorO.png"));
        floorView[2] = new ImageView(new Image("floorY.png"));
        floorView[3] = new ImageView(new Image("floorG.png"));
        floorView[4] = new ImageView(new Image("floorC.png"));
        floorView[5] = new ImageView(new Image("floorB.png"));
        floorView[6] = new ImageView(new Image("floorP.png"));
        floorView[7] = new ImageView(new Image("floorS.png"));
        for(int i = 0; i < 8; i++) {
            floorView[i].setFitHeight(blockSize);
            floorView[i].setFitWidth(blockSize);
            if (i%2 == 0) {
                floorView[i].setLayoutX(180);
                floorView[i].setLayoutY(520 + i/2 * 40);
            } else {
                floorView[i].setLayoutX(465);
                floorView[i].setLayoutY(520 + i/2 * 40);
            }
            pane.getChildren().add(floorView[i]);
        }
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
                        Thread.sleep(3000);
                        t.interrupt();
                    }
                } catch (InterruptedException e) {
                    Platform.runLater(() -> {
                        PlotScene plot = new PlotScene();
                        if (whichScreen == 1) {
                            World world = screen.getWorld();
                            plot.start(stage, 2, world, screen);
                        }
                        else if (whichScreen == 2) {
                            plot.start(stage, 3);
                        }
                    });
                    break;
                }
            }
        });
        t.start();
    }

    public void repaint() {
        if (whichScreen==1) {
            flag = screen.display(grid);
            setBackground(grid, Color.BLACK);
            ((PlayScreen) screen).displaySize(msg);
        } else if (whichScreen==2) {
            flag = sscreen.display(grid);
            setBackground(grid, Color.BLACK);
            ((SnakeScreen) sscreen).displayMsg(msg);
        }
    }

    private void setBackground(Pane pane, Color color) {
        pane.setBackground(new Background(new BackgroundFill(color, null, null)));
    }

}
