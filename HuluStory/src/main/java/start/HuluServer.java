package start;

import javafx.scene.input.KeyCode;
import screen.PlayScreen;
import screen.Screen;
import world.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class HuluServer {

    private static PlayScreen screen;

    private static ServerSocketChannel serverSocketChannel;
    private static Selector selector;

    public static void main(String[] args) throws IOException {
        screen = new PlayScreen();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(1209));
        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        new Thread(() -> {
            try {
                buildConnection();
            } catch (IOException ignored) {}
        }).start();
    }

    public static void buildConnection() throws IOException {
        int connect = 0;
        while (true) {
            if (selector.select(30) == 0) {
                continue;
            }

            Set<SelectionKey> keySet = selector.selectedKeys();

            for (SelectionKey key : keySet) {
                if(!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    if (connect < 2) {
                        handleKeyAccept(key, connect++);
                    }
                }
                if (key.isReadable()) {
                    handleKeyRead(key);
                }
            }

            keySet.clear();
        }
    }

    public static void handleKeyAccept(SelectionKey key, int playerNo) throws IOException {
        int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
        SocketChannel client = socketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, interestSet, playerNo);
        sendData(playerNo, client);
    }

    public static void handleKeyRead(SelectionKey key) throws IOException {
        int playerNo = (int) key.attachment();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        byte[] bytes = new byte[1024];
        int len = socketChannel.read(ByteBuffer.wrap(bytes));
        if (len != 0) {
            String name = new String(bytes, 0, len);
            KeyCode keyCode = KeyCode.getKeyCode(name);
            screen = (PlayScreen) screen.respondToUserInput(keyCode, playerNo);
        }
    }

    public static void sendData(int playerNo, SocketChannel channel) {
        new Thread(() -> {
            while (true) {
                boolean isEnd = false;
                if (!channel.isOpen()) {
                    break;
                }
                try {
                    Thread.sleep(30);
                    if (screen.displayTiles() == 1)
                        isEnd = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Data data = getData(playerNo,isEnd);
                if (data != null) {
                    byte[] bytes;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(bos);
                        oos.writeObject(data);
                        oos.flush();
                        bytes = bos.toByteArray();
                        oos.close();
                        bos.close();
                    } catch (IOException ex) {
                        break;
                    }
                    try {
                        channel.write(ByteBuffer.wrap(bytes));
                    } catch (IOException e) {
                        break;
                    }
                }
            }
        }).start();
    }

    public static Data getData(int playerNo, boolean isEnd) {
        int[][] map = screen.getWorld().getMap();
        int[][] creasLoc = screen.getCreaturesLocation();
        int[] score = screen.getCreaturesScore();
        Data data = new Data(map, creasLoc, score, isEnd);
        return data;
    }

}
