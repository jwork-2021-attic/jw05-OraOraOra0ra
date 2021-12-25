package start;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import screen.ClientPlayScreen;
import world.Data;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class HuluClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private ClientPlayScreen screen = new ClientPlayScreen();

    private ImageView[] scoreView = new ImageView[9];
    private ImageView[] floorView = new ImageView[9];

    private int Height;
    private int Width;

    private Pane pane;
    private GridPane grid;

    private Text msg;

    private int flag = 0;

    private SocketChannel acceptChannel;
    private SocketChannel channel;
    private Selector selector;


    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = initScene();
        initScoreView();
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.setScene(scene);
        stage.setTitle("The hulu's color war");
        stage.show();

        startClient();
        buildConnection();
    }

    private void startClient() throws IOException {
        acceptChannel = SocketChannel.open();
        selector = Selector.open();
        acceptChannel.configureBlocking(false);
        acceptChannel.register(selector, SelectionKey.OP_CONNECT);
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 1209);
        acceptChannel.connect(socketAddress);
    }

    private void buildConnection() {
        new Thread(() -> {
            while (true) {
                try {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    for (SelectionKey key : keys) {
                        if (!key.isValid()) {
                            continue;
                        }
                        if (key.isConnectable()) {
                            if (acceptChannel.finishConnect()) {
                                handleKeyConnect(key);
                            }
                        }
                        if (key.isReadable()) {
                            Data data = getData(key);
                            if (data != null) {
                                Platform.runLater(() -> {
                                    grid.getChildren().clear();
                                    repaint(data);
                                    if (data.getState()==true) {
                                        showWinner(data.getWinner());
                                        return;
                                    }
                                });
                            }
                        }
                    }
                    keys.clear();
                } catch (IOException | ClassNotFoundException ignored) {}
            }
        }).start();
    }

    private void handleKeyConnect(SelectionKey key) throws IOException {
        channel = (SocketChannel) key.channel();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        grid.setOnKeyPressed(keyEvent -> {
            String name = keyEvent.getCode().getName();
            try {
                channel.write(ByteBuffer.wrap(name.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Data getData(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.register(selector, SelectionKey.OP_READ);
        ByteBuffer readBuf = ByteBuffer.allocate(8000);
        int count = socketChannel.read(readBuf);
        if (count != -1) {
            readBuf.flip();
            byte[] bytes = new byte[readBuf.remaining()];
            readBuf.get(bytes);
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            Object obj = oi.readObject();
            if (obj != null) {
                return (Data) obj;
            }
        }
        return null;
    }

    public void repaint(Data data) {
        setBackground(grid, Color.BLACK);
        int[][] map = data.getMap();
        int[][] creasLoc = data.getCreasLoc();
        int[] score = data.getScore();
        screen.display(grid, map, creasLoc);
        screen.displaySize(msg, score);
    }

    public void setBackground(Pane pane, Color color) {
        pane.setBackground(new Background(new BackgroundFill(color, null, null)));
    }

    public Scene initScene() {
        pane = new Pane();
        initGridPane();
        initText();
        //initHuluView(25);

        Height = 720;
        Width = 625;
        Scene scene = new Scene(pane,Width,Height);
        grid.requestFocus();
        return scene;
    }

    public void initGridPane() {
        grid = new GridPane();
        grid.setLayoutX(0);
        grid.setLayoutY(0);
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
            scoreView[i].setFitHeight(25);
            scoreView[i].setFitWidth(25);
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
        scoreView[7].setFitHeight(25);
        scoreView[7].setFitWidth(25);
        scoreView[7].setLayoutX(120);
        scoreView[7].setLayoutY(680);
        scoreView[8] = new ImageView(new Image("grandfather.png"));
        scoreView[8].setFitHeight(25);
        scoreView[8].setFitWidth(25);
        scoreView[8].setLayoutX(405);
        scoreView[8].setLayoutY(680);
        pane.getChildren().add(scoreView[7]);
        pane.getChildren().add(scoreView[8]);

        floorView[0] = new ImageView(new Image("floorR.png"));
        floorView[1] = new ImageView(new Image("floorO.png"));
        floorView[2] = new ImageView(new Image("floorY.png"));
        floorView[3] = new ImageView(new Image("floorG.png"));
        floorView[4] = new ImageView(new Image("floorC.png"));
        floorView[5] = new ImageView(new Image("floorB.png"));
        floorView[6] = new ImageView(new Image("floorP.png"));
        floorView[7] = new ImageView(new Image("floorS.png"));
        floorView[8] = new ImageView(new Image("floorF.png"));
        for(int i = 0; i < 9; i++) {
            floorView[i].setFitHeight(25);
            floorView[i].setFitWidth(25);
            if (i < 7) {
                if (i % 2 == 0) {
                    floorView[i].setLayoutX(180);
                    floorView[i].setLayoutY(520 + i / 2 * 40);
                } else {
                    floorView[i].setLayoutX(465);
                    floorView[i].setLayoutY(520 + i / 2 * 40);
                }
            } else {
                if (i % 2 == 0) {
                    floorView[i].setLayoutX(465);
                    floorView[i].setLayoutY(680);
                } else {
                    floorView[i].setLayoutX(180);
                    floorView[i].setLayoutY(680);
                }
            }
            pane.getChildren().add(floorView[i]);
        }
    }

    public void showWinner(String name) {
        if (flag == 0) {
            flag = 1;
            playMusic();
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("游戏结束");
            alert.setContentText("The winner is " + name + " !");
            alert.showAndWait();
        }
    }

    public void playMusic() {
        MusicPlayer mp = new MusicPlayer();
        mp.start();
    }
}
