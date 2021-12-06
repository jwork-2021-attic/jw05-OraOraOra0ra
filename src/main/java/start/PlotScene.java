package start;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PlotScene extends Application {

    private Pane pane = new Pane();
    private Stage stage;
    private Text msg = new Text();
    private ImageView bkView = new ImageView(new Image("background.jpeg"));
    private Button btn;

    private int Height;
    private int Width;

    private int textX = 400;
    private int textY = 200;

    private int chapter;

    private int No = 0;
    private String[] plot1 = {"某一天，蛇精在私下分别找到了七个葫芦娃。", "“如果继续对抗，我们将两败俱伤，你和你的兄弟们还会挤在那间破屋子里。”蛇精说。","“不如我们停战，我把我的山都给你们，到时候你就可以自己占山为王了。”她接着说","“好，谅你也不敢刷什么阴谋诡计！”大二三四五六七娃说。"};
    private String[] plot2 = {"“芜湖，住在山里真不戳。这里以后就归我啦。”葫芦娃们说。","“(￢︿̫̿￢☆)，葫芦娃子，别高兴的太早了。”说着，蛇精从手里掏出了..."};
    private String[] plot3 = {"To be continued..."};
    private Text[] text1 = new Text[4];
    private Text[] text2 = new Text[2];
    private Text[] text3 = new Text[1];
    private int[][] tLoc = {{100,200},{100,225},{100,250},{100,275}};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.Height = 540;
        this.Width = 960;

        initStage();
        initBackground();
        initText(plot1,text1);
        initButton();

        stage.show();

        setRepaintThread();
    }

    public void start(Stage stage, int chapter) {
        this.chapter = chapter;
        this.stage = stage;
        this.Height = 540;
        this.Width = 960;

        initStage();
        initBackground();
        if (chapter == 1)
            initText(plot1,text1);
        else if (chapter == 2) {
            initText(plot2, text2);
            initBgm();
        }
        else if (chapter == 3)
            initText(plot3,text3);

        initButton();

        stage.show();

        setRepaintThread();
    }

    public void initStage() {
        Scene scene = new Scene(pane,Width,Height);
        stage.setScene(scene);
        stage.setTitle("The story of Huluwa");
    }

    public void initBgm() {
        new Thread(() -> {
            new MusicPlayer();
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
        else
            btn = new Button("To be continued");
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
                PlotScene plot = new PlotScene();
                plot.start(stage,3);
            }
        }
    }

    private void setRepaintThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
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
            if (No < 1)
                setText(text3[No], tLoc[No][0], tLoc[No][1]);
        }
        No++;
    }
}
