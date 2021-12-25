package world;

import java.io.Serializable;

public class Data implements Serializable {
    private int[][] map;
    private int[][] creasLoc;
    private int[] score;
    private boolean isEnd;

    public Data(int[][] map, int[][] creasLoc, int[] score, boolean isEnd) {
        this.map = map;
        this.creasLoc = creasLoc;
        this.score = score;
        this.isEnd = isEnd;
    }

    public int[][] getMap() {
        return map;
    }

    public int[][] getCreasLoc() {
        return creasLoc;
    }

    public int[] getScore() {
        return score;
    }

    public boolean getState() { return isEnd; }

    public String getWinner() {
        int no = 0;
        int max = 0;
        String name = "";
        for (int i = 0; i < score.length; i++)
            if (score[i] > max) {
                max = score[i];
                no = i;
            }
        switch (no) {
            case 0:
                name = "Big hulu";
                break;
            case 1:
                name = "Vision hulu";
                break;
            case 2:
                name = "Iron hulu";
                break;
            case 3:
                name = "Fire hulu";
                break;
            case 4:
                name = "Water hulu";
                break;
            case 5:
                name = "Invisible hulu";
                break;
            case 6:
                name = "Hulu hulu";
                break;
            case 7:
                name = "Snake";
                break;
            case 8:
                name = "the Old man";
                break;
        }
        return name;
    }
}
