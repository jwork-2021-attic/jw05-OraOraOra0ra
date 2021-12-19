package world;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import java.util.Arrays;

public class MazeBuilder {

    public class Node {
        public final int x;
        public final int y;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private Stack<Node> stack = new Stack<>();
    private Random rand = new Random();
    private static int[][] maze;
    private int width;
    private int height;

    private static int[][] Maze = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,0,0,0,1,0,1,1,1,0,1,1,0,1,1,0,1,0,1,1,0,1,1},
            {1,1,0,1,1,1,1,0,0,0,0,0,1,1,0,1,1,0,1,0,0,1,0,1,1},
            {1,1,0,1,1,1,1,0,1,1,1,0,1,1,0,1,1,0,1,0,1,0,0,1,1},
            {1,1,1,0,0,0,1,0,1,1,1,0,1,1,0,0,0,0,1,0,1,1,0,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,0,1,1,1,0,1,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1},
            {1,1,1,1,0,1,1,0,0,0,1,1,1,0,1,1,1,0,1,1,0,0,0,1,1},
            {1,0,1,1,0,1,0,0,1,0,0,1,1,1,0,1,0,1,1,0,0,1,0,0,1},
            {1,0,0,0,0,1,0,1,1,1,0,1,1,1,1,0,1,1,1,0,1,1,1,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    MazeBuilder(int width, int height) {
        maze = new int[width][height];
        this.width = width;
        this.height = height;
    }

    public void MazeBuilder() {
        stack.push(new Node(0,0));
        while (!stack.empty()) {
            Node next = stack.pop();
            if (validNextNode(next)) {
                maze[next.x][next.y] = 1;
                ArrayList<Node> neighbors = findNeighbors(next);
                randomlyAddNodesToStack(neighbors);
            }
        }
    }

    public String getRawMaze() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : maze) {
            sb.append(Arrays.toString(row) + "\n");
        }
        return sb.toString();
    }

    public String getSymbolicMaze() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                sb.append(maze[i][j] == 1 ? "*" : " ");
                sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private boolean validNextNode(Node node) {
        int numNeighboringOnes = 0;
        for (int x = node.x-1; x < node.x+2; x++) {
            for (int y = node.y-1; y < node.y+2; y++) {
                if (pointOnGrid(x, y) && pointNotNode(node, x, y) && maze[x][y] == 1) {
                    numNeighboringOnes++;
                }
            }
        }
        return (numNeighboringOnes < 3) && maze[node.x][node.y] != 1;
    }

    private void randomlyAddNodesToStack(ArrayList<Node> nodes) {
        int targetIndex;
        while (!nodes.isEmpty()) {
            targetIndex = rand.nextInt(nodes.size());
            stack.push(nodes.remove(targetIndex));
        }
    }

    private ArrayList<Node> findNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (int x = node.x-1; x < node.x+2; x++) {
            for (int y = node.y-1; y < node.y+2; y++) {
                if (pointOnGrid(x, y) && pointNotCorner(node, x, y)
                        && pointNotNode(node, x, y)) {
                    neighbors.add(new Node(x, y));
                }
            }
        }
        return neighbors;
    }

    private Boolean pointOnGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    private Boolean pointNotCorner(Node node, int x, int y) {
        return (x == node.x || y == node.y);
    }

    private Boolean pointNotNode(Node node, int x, int y) {
        return !(x == node.x && y == node.y);
    }

    public static int[][] makeMyMaze(int[][] Maze) {
        int[][] maze = new int[World.getBlockWidthCount()][World.getBlockHeightCount()];
        for (int i=0;i<World.getBlockWidthCount();i++)
            for(int j=0;j<World.getBlockHeightCount();j++){
                maze[i][j] = Maze[j][i];
            }
        return maze;
    }

    public static int[][] mazeArray() {
        return makeMyMaze(Maze);
    }
    public static int[][] theMazeArray(){
        return Maze;
    }
}