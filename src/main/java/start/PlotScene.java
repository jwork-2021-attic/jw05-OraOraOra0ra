package start;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import screen.PlayScreen;
import world.Creature;
import world.World;
import world.WorldBuilder;

import java.io.*;

public class PlotScene extends Application {

    private int sceneNo;

    boolean isLoad;

    private PlayScreen screen;

    int[][] map;
    int[][] creasLoc;
    int[] state;

    private Pane pane = new Pane();
    private GridPane grid = new GridPane();
    private Stage stage;
    private Text msg = new Text();
    private ImageView bkView = new ImageView(new Image("background.jpeg"));
    private Button btn;

    MusicPlayer mp = new MusicPlayer();

    private int Height;
    private int Width;

    private int textX = 400;
    private int textY = 200;

    private int chapter;

    private int No = 0;
    private String[] plot1 = {"某一天，蛇精在私下分别找到了七个葫芦娃。", "“如果继续对抗，我们将两败俱伤，你和你的兄弟们还会挤在那间破屋子里。”蛇精说。","“不如我们停战，我把我的山都给你们，到时候你就可以自己占山为王了。”她接着说","“好，谅你也不敢刷什么阴谋诡计！”大二三四五六七娃说。"};
    private String[] plot2 = {"“芜湖，住在山里真不戳。这里以后就归我啦。”葫芦娃们说。","“(￢︿̫̿￢☆)，葫芦娃子，别高兴的太早了。”说着，蛇精从手里掏出了..."};
    private String[] plot3 = {"“葫芦娃子们，跟老娘斗，你们还是嫩了点。哈哈哈哈哈哈~”蛇精大笑","这时，一个头发灰白的男人出现了......","To be continued..."};
    private Text[] text1 = new Text[4];
    private Text[] text2 = new Text[2];
    private Text[] text3 = new Text[3];
    private int[][] tLoc = {{100,200},{100,225},{100,250},{100,275}};

    World world;

    private  Creature[] creas;

    ObjectInputStream objectInputStream;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.Height = 540;
        this.Width = 960;

        sceneNo = 1;

        initStage();
        initBackground();
        initText(plot1,text1);
        initButton();

        stage.show();

        setRepaintThread();
    }

    //sceneNo==1 | 3 | 5
    public void start(Stage stage, int chapter, boolean isLoad) {
        this.isLoad = isLoad;
        this.chapter = chapter;
        this.stage = stage;
        this.Height = 540;
        this.Width = 960;

        initStage();
        initBackground();

        if (chapter == 1) {
            initText(plot1, text1);
            sceneNo = 1;
            initButton();
        } else if (chapter == 2) {
            initBgm();
            initText(plot2, text2);
            sceneNo = 3;
            initButton();
        } else if (chapter == 3) {
            initBgm();
            initText(plot3, text3);
            sceneNo = 5;
        }

        stage.show();

        if (isLoad) {
            if (judgeLoad()) {
                Object scene = null;
                Object chap = null;
                Object no = null;
                Object m = null;
                Object cl = null;
                Object st = null;
                try {
                    scene = objectInputStream.readObject();
                    chap = objectInputStream.readObject();
                    no = objectInputStream.readObject();
                    if (sceneNo == 3) {
                        m = objectInputStream.readObject();
                        cl = objectInputStream.readObject();
                        st = objectInputStream.readObject();
                        map = (int[][])m;
                        creasLoc = (int[][])cl;
                        state = (int[])st;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

                this.No = (int) no;

                if (sceneNo == 1) {
                    for (int i = 0; i < No && i < 4; i++)
                        setText(text1[i], tLoc[i][0], tLoc[i][1]);
                    if (No >= 5)
                        pane.getChildren().add(btn);
                }   else if (sceneNo == 3) {
                    for (int i = 0; i < No && i < 2; i++)
                        setText(text2[i], tLoc[i][0], tLoc[i][1]);
                    if (No >= 3)
                        pane.getChildren().add(btn);
                } else if (sceneNo == 5) {
                    for (int i = 0; i < No && i < 3; i++)
                        setText(text3[i], tLoc[i][0], tLoc[i][1]);
                }

            }
        }

        setRepaintThread();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    FileOutputStream fileOutputStream = null;
                    fileOutputStream = new FileOutputStream("test.txt");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(sceneNo);
                    objectOutputStream.writeObject(chapter);
                    objectOutputStream.writeObject(No);
                    if (sceneNo == 3) {
                        objectOutputStream.writeObject(map);
                        objectOutputStream.writeObject(creasLoc);
                    }
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //sceneNo==3
    public void start(Stage stage, int chapter, World world, PlayScreen screen) {
        isLoad = false;
        this.screen = screen;
        this.creasLoc = screen.getCreaturesLocation();
        if (chapter == 2)
            this.sceneNo = 3;
        else if (chapter == 3)
            this.sceneNo = 5;
        this.creas = screen.getCreatures();
        this.world = world;
        this.chapter = chapter;
        this.stage = stage;
        this.Height = 540;
        this.Width = 960;

        initStage();
        initBackground();
        if (chapter == 2) {
            initText(plot2, text2);
            initBgm();
            initButton();
        } else if (chapter == 3) {
            initText(plot3, text3);
            initBgm();
        }

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
                    objectOutputStream.writeObject(chapter);
                    objectOutputStream.writeObject(No);
                    objectOutputStream.writeObject(screen.getWorld().getMap());
                    objectOutputStream.writeObject(screen.getCreaturesLocation());
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //sceneNo==5
    public void start(Stage stage, int chapter) {
        isLoad = false;
        if (chapter == 3)
            this.sceneNo = 5;
        this.chapter = chapter;
        this.stage = stage;
        this.Height = 540;
        this.Width = 960;

        initStage();
        initBackground();
        if (chapter == 3) {
            initText(plot3, text3);
            initBgm();
        }

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
                    objectOutputStream.writeObject(chapter);
                    objectOutputStream.writeObject(No);
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initStage() {
        Scene scene = new Scene(pane,Width,Height);
        stage.setScene(scene);
        stage.setTitle("The story of Huluwa");
    }

    public void initBgm() {
        new Thread(() -> {
            mp.run();
        }).start();
    }

    public void initBackground() {
        bkView.setFitWidth(Width);
        bkView.setFitHeight(Height);
        pane.getChildren().add(bkView);
    }

    public void initText(String[] plot, Text[] text) {
        for (int i = 0; i < plot.length; i++) {
            text[i] = new Text();
            text[i].setFill(Color.WHITE);
            text[i].setFont(Font.font(null, FontWeight.BOLD,20));
            text[i].setText(plot[i]);
        }
    }

    public void setText(Text msg, int x, int y) {
        msg.setLayoutX(x);
        msg.setLayoutY(y);
        pane.getChildren().add(msg);
    }

    public void initButton() {
        if (chapter == 1)
            btn = new Button("Start");
        else if (chapter == 2)
            btn = new Button("Continue");
        btn.setOnAction(new StartHandler());
        btn.setLayoutX(Width/2-10);
        btn.setLayoutY(4*Height/5);
    }

    class StartHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            if (chapter == 1) {
                PlayScene play = new PlayScene();
                play.start(stage);
            }
            else if (chapter == 2) {
                //PlotScene plot = new PlotScene();
                //plot.start(stage,3);
                mp.end();

                if (isLoad) {
                    World world = new WorldBuilder(map).makeMaze(map).build();
                    PlayScene play = new PlayScene();
                    play.start(4, stage, world, creasLoc, state);
                } else {
                    PlayScene play = new PlayScene();
                    boolean[] s = new boolean[8];
                    for (int i = 0; i < 8; i++)
                        s[i] = true;
                    play.start(4, stage, screen.getWorld(), creasLoc, s);
                }


            }
            /*else if (chapter == 3){
                PlayScene play = new PlayScene();
                play.start(stage,grid);
            }*/
        }
    }

    private void setRepaintThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        repaint();
                        if (No == 5)
                            return;
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void repaint() {
        if (chapter == 1) {
            if (No < 4)
                setText(text1[No], tLoc[No][0], tLoc[No][1]);
            if (No == 4)
                pane.getChildren().add(btn);
        }
        else if (chapter == 2) {
            if (No < 2)
                setText(text2[No], tLoc[No][0], tLoc[No][1]);
            if (No == 2)
                pane.getChildren().add(btn);
        }
        else if (chapter == 3) {
            if (No < 3)
                setText(text3[No], tLoc[No][0], tLoc[No][1]);
        }
        No++;
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
}
