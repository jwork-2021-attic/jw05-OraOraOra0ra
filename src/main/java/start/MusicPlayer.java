package start;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;


public class MusicPlayer extends Thread {
    Player player;
    File music;

    public MusicPlayer() {
        this.music = new File("src\\main\\resources\\bgm.mp3");
        run();
        System.out.println("6666");
    }

    @Override
    public void run() {
        super.run();
        try {
            play();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public void play() throws FileNotFoundException, JavaLayerException {
        BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(music));
        player = new Player(buffer);
        player.play();
    }
}